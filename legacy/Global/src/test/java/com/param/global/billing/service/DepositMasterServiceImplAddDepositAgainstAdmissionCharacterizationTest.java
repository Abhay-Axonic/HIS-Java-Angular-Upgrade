package com.param.global.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.model.DepositMaster;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPaymentDepositDetailsDao;
import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code DepositMasterServiceImpl.addDepositAgainstAdmission} -- one of ADT's 3
 * originally flagged live financial calls, triggered from
 * {@code BedAllocationServiceImpl.savePatientAdmission} when a deposit is collected
 * during an admission. Scoped to this method's own logic (deposit-number validation
 * gate, OP-vs-IP/DC/ER admission/encounter linkage, payment-detail creation); the huge
 * (600+ line) ADT-side {@code savePatientAdmission} caller is not driven end-to-end --
 * this test calls the Global-side destination directly, same strategy already used for
 * postServicesIntoExistingBill and convertServicesFromOPToIP.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("DepositMasterServiceImpl.addDepositAgainstAdmission -- admission-deposit characterization")
class DepositMasterServiceImplAddDepositAgainstAdmissionCharacterizationTest {

    @Mock private IDepositMasterDao iDepositMasterDao;
    @Mock private IPaymentDepositDetailsDao iPaymentDepositDetailsDao;
    @Mock private IGlobalUnitMasterDao iGlobalUnitMasterDao;

    private DepositMasterServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DepositMasterServiceImpl();
        setPrivate(service, "iDepositMasterDao", iDepositMasterDao);
        setPrivate(service, "iPaymentDepositDetailsDao", iPaymentDepositDetailsDao);
        setPrivate(service, "iGlobalUnitMasterDao", iGlobalUnitMasterDao);

        lenient().when(iDepositMasterDao.getCurrentId()).thenReturn(1L);
    }

    private static void setPrivate(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private DepositCollectionReqDto request(Integer visitTypeId, Integer admissionId, Integer encounterId) {
        DepositMasterDto deposit = new DepositMasterDto();
        deposit.setDepositAmount(1000.0);
        deposit.setUnitId(0); // 0 -> TimeZoneComponent's unitId<=0 branch, avoids the static timezone map
        deposit.setCreatedBy(99);
        deposit.setVisitTypeId(visitTypeId);
        deposit.setAdmissionId(admissionId);
        deposit.setEncounterId(encounterId);

        PaymentDepositDetailsDto payment = new PaymentDepositDetailsDto();
        payment.setAmount(1000.0);
        payment.setPaymentModeId(1);

        DepositCollectionReqDto req = new DepositCollectionReqDto();
        req.setDepositMasterDto(deposit);
        req.setListPaymentDepositDetailsDto(java.util.Arrays.asList(payment));
        return req;
    }

    @Test
    @DisplayName("deposit-number generator returns null -> stops immediately with a config-error Response, before touching anything else")
    void depositNumberGeneratorReturnsNull_stopsImmediately() throws Exception {
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn(null);

        Response result = service.addDepositAgainstAdmission(request(2 /* IP */, 400, null));

        assertThat(result.getStatus()).isEqualTo(ICommonConstants.ERROR);
        assertThat(result.getMessage()).isEqualTo("Please Configure Deposit Number.");
        verify(iDepositMasterDao, never()).saveDeposit(any());
    }

    @Test
    @DisplayName("visitTypeId=IP: admissionId is kept, encounterId is explicitly nulled out")
    void visitTypeIP_keepsAdmissionId_nullsEncounterId() throws Exception {
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-001");
        DepositMaster savedDeposit = new DepositMaster();
        savedDeposit.setDepositMasterId(9001);
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, savedDeposit));

        DepositCollectionReqDto req = request(2 /* IP */, 400, 300);
        service.addDepositAgainstAdmission(req);

        assertThat(req.getDepositMasterDto().getAdmissionId()).isEqualTo(400);
        assertThat(req.getDepositMasterDto().getEncounterId()).isNull();
    }

    @Test
    @DisplayName("visitTypeId=OP: encounterId is kept, admissionId is explicitly nulled out (opposite of the IP/DC/ER branch)")
    void visitTypeOP_keepsEncounterId_nullsAdmissionId() throws Exception {
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-002");
        DepositMaster savedDeposit = new DepositMaster();
        savedDeposit.setDepositMasterId(9002);
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, savedDeposit));

        DepositCollectionReqDto req = request(1 /* OP */, 400, 300);
        service.addDepositAgainstAdmission(req);

        assertThat(req.getDepositMasterDto().getEncounterId()).isEqualTo(300);
        assertThat(req.getDepositMasterDto().getAdmissionId()).isNull();
    }

    @Test
    @DisplayName("FIXED 2026-07-17: deposit save fails (non-success Response) -> returns an ERROR Response instead of crashing (was: BillingException wrapping an NPE)")
    void depositSaveFails_returnsErrorResponseInsteadOfCrashing() throws Exception {
        // FIXED (docs/DECISION_NEEDED_ADTFinancialCallsCrashBugs.md, fix 2): a
        // `if (depositMaster == null) { return ... }` guard now sits directly before the
        // method's success-return line, mirroring the file's own pre-existing early-return
        // idiom used a few lines earlier for "Please Configure Deposit Number." When the
        // deposit-master save doesn't succeed as expected, the caller now gets a defined
        // ERROR Response instead of an uncaught NPE wrapped as BillingException.
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-003");
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, null));

        DepositCollectionReqDto req = request(2, 400, 300);

        Response result = service.addDepositAgainstAdmission(req);

        assertThat(result.getStatus()).isEqualTo(ICommonConstants.ERROR);
        assertThat(result.getMessage()).isEqualTo("Failed to save deposit.");
        verify(iPaymentDepositDetailsDao, never()).savePaymentDepositDetails(any());
    }
}
