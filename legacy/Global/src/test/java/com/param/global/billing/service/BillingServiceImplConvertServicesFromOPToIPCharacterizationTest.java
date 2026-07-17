package com.param.global.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.service.IMPackageMasterService;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code BillingServiceImpl.convertServicesFromOPToIP} -- one of ADT's 3 originally
 * flagged live financial calls (triggered from
 * {@code BedAllocationServiceImpl.acceptNursingAcceptanceRequet} and
 * {@code BedAllocationController}), which re-prices OP order-details onto a new IP
 * order when a patient is admitted.
 *
 * Deliberately NOT covered: the actual rate/tax/split computation itself, which lives
 * in {@code IMPackageMasterService.checkServiceAvailableInPackageContract} (a separate,
 * not-yet-characterized method) -- this test mocks that call's result and pins what
 * {@code convertServicesFromOPToIP} itself does with it.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BillingServiceImpl.convertServicesFromOPToIP -- OP-to-IP re-pricing characterization")
class BillingServiceImplConvertServicesFromOPToIPCharacterizationTest {

    @Mock private IBillingDao iBillingDao;
    @Mock private IOrderMasterDao iOrderMasterDao;
    @Mock private IGlobalUnitMasterDao iGlobalUnitMasterDao;
    @Mock private IMPackageMasterService iMPackageMasterService;
    @Mock private IOrderDetailsMasterDao iOrderDetailsMasterDao;

    private BillingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BillingServiceImpl();
        service.iBillingDao = iBillingDao;
        service.iOrderMasterDao = iOrderMasterDao;
        service.iGlobalUnitMasterDao = iGlobalUnitMasterDao;
        service.iMPackageMasterService = iMPackageMasterService;
        setPrivate(service, "iOrderDetailsMasterDao", iOrderDetailsMasterDao);

        lenient().when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 140)).thenReturn("ORD-001");
        lenient().when(iOrderMasterDao.saveOrderMaster(any())).thenReturn(
                new com.param.global.common.Response<>("success", null, null, null, 9001));
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

    private ConvertServicesFromOptoIpDto request(List<Integer> orderDetailsIds) {
        ConvertServicesFromOptoIpDto dto = new ConvertServicesFromOptoIpDto();
        dto.setOrderDetailsIds(orderDetailsIds);
        dto.setEncounterId(300);
        dto.setAdmissionId(400);
        dto.setUserId(99);
        dto.setVisitTypeId(1);
        dto.setPatientId(500);
        dto.setOrganizationId(1);
        dto.setUnitId(0); // 0 -> TimeZoneComponent's unitId<=0 branch, avoids the static timezone map
        return dto;
    }

    private AdmissionListIpdChargesResDto admission() {
        AdmissionListIpdChargesResDto dto = new AdmissionListIpdChargesResDto();
        dto.setPatientCategoryId(1);
        dto.setBedBillingCategoryId(2);
        dto.setContractId(null);
        dto.setAdmissionId(400);
        dto.setDefaultSelfTariffId(3);
        dto.setPatientId(500);
        dto.setSelfPayeeId(1);
        dto.setPatientClassId(1);
        return dto;
    }

    private OrderDetailsMaster orderDetail(int id, int orgUnitId, int orgId, int serviceId) {
        OrderDetailsMaster m = mock(OrderDetailsMaster.class);
        lenient().when(m.getOrderId()).thenReturn(id);
        lenient().when(m.getOrgUnitId()).thenReturn(orgUnitId);
        lenient().when(m.getOrgId()).thenReturn(orgId);
        lenient().when(m.getServiceId()).thenReturn(serviceId);
        lenient().when(m.getOrderDate()).thenReturn(1000L);
        return m;
    }

    @Test
    @DisplayName("package/contract found: every returned field (rate, tax, self/credit-payable, contract, package-consumption) is copied onto the order-detail before saving")
    void contractFound_copiesAllReturnedFieldsOntoOrderDetail() throws Exception {
        when(iBillingDao.getAdmissionsById(400)).thenReturn(admission());
        OrderDetailsMaster od = orderDetail(7001, 0, 1, 55);
        when(iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(7001)).thenReturn(od);

        ContractConsumptionDto found = new ContractConsumptionDto();
        found.setServiceRate(500.0);
        found.setConcession(10.0);
        found.setTaxAmt(25.0);
        found.setFinalPrice(515.0);
        found.setTaxPercentage(5.0);
        found.setSelfPayable(515.0);
        found.setPayeeId(1);
        found.setCreaditPayable(0.0);
        found.setContractId(77);
        found.setPackageConsumptionAmt(BigDecimal.ZERO);
        found.setBillingBedCategoryId(2);
        found.setOrderDetailsPayeeMapperDtosList(Collections.emptyList());
        found.setOrderDetailsPackageMapperDtosList(Collections.emptyList());
        when(iMPackageMasterService.checkServiceAvailableInPackageContract(any(ContractConsumptionDto.class)))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, null, found));

        service.convertServicesFromOPToIP(request(Arrays.asList(7001)));

        // NOTE (characterization only): production mixes `new BigDecimal(double)` (exact binary
        // representation) and `BigDecimal.valueOf(double)` (via Double.toString, i.e. the more
        // "expected" decimal representation) across these fields -- setTaxPer alone uses
        // valueOf(); every other converted field here uses `new BigDecimal(...)`. Matched exactly
        // below so this test pins the real behavior rather than a normalized approximation.
        verify(od).setServiceAmt(new BigDecimal(500.0));
        verify(od).setDummyServiceAmt(new BigDecimal(500.0));
        verify(od).setOrdTotalAmt(new BigDecimal(500.0));
        verify(od).setTaxAmount(new BigDecimal(25.0));
        verify(od).setNetAmount(new BigDecimal(515.0));
        verify(od).setTaxPer(BigDecimal.valueOf(5.0));
        verify(od).setSelfPayable(new BigDecimal(515.0));
        verify(od).setPayeeId(1);
        verify(od).setCreditPayable(new BigDecimal(0.0));
        verify(od).setContractId(77);
        verify(od).setBedCategoryId(2);
        verify(iOrderDetailsMasterDao).saveOrderDetailsMaster(od, Collections.emptyList(), Collections.emptyList());
    }

    @Test
    @DisplayName("FIXED 2026-07-17: no package/contract found for the service -> that order-detail's IP-side save is skipped, the conversion no longer crashes (was: BillingException wrapping an NPE)")
    void contractNotFound_skipsSaveForThatOrderDetailButDoesNotCrash() throws Exception {
        when(iBillingDao.getAdmissionsById(400)).thenReturn(admission());
        OrderDetailsMaster od = orderDetail(7001, 0, 1, 55);
        when(iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(7001)).thenReturn(od);

        // checkServiceAvailableInPackageContract found nothing for this service -- a plausible,
        // non-exotic outcome (e.g. a self-pay patient with no active package/contract for this
        // particular service).
        when(iMPackageMasterService.checkServiceAvailableInPackageContract(any(ContractConsumptionDto.class)))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, null, null));

        // FIXED (docs/DECISION_NEEDED_ADTFinancialCallsCrashBugs.md, fix 1a): the
        // `iOrderDetailsMasterDao.saveOrderDetailsMaster(...)` call is now guarded by
        // `if (consumptionDto != null)`. When no package/contract is found, this one
        // order-detail's IP-side save is skipped instead of throwing an NPE that aborted the
        // WHOLE conversion (every other order-detail in the same batch, including ones already
        // processed earlier in the loop, used to be left in an inconsistent half-committed state
        // by the crash). The method now completes normally and returns success.
        com.param.global.common.Response result = service.convertServicesFromOPToIP(request(Arrays.asList(7001)));

        assertThat(result.getStatus()).isEqualTo("success");

        // The order-detail was fetched, but its IP-side pricing was never saved -- this order
        // detail simply doesn't get converted, rather than crashing the whole batch.
        verify(iOrderDetailsMasterDao, org.mockito.Mockito.never())
                .saveOrderDetailsMaster(any(), any(), any());
    }

    @Test
    @DisplayName("two order-details, both resolve successfully: each gets its OWN re-priced values saved, not the last one's values applied to both")
    void twoOrderDetails_eachGetsItsOwnValues() throws Exception {
        when(iBillingDao.getAdmissionsById(400)).thenReturn(admission());
        OrderDetailsMaster od1 = orderDetail(7001, 0, 1, 55);
        OrderDetailsMaster od2 = orderDetail(7002, 0, 1, 66);
        when(iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(7001)).thenReturn(od1);
        when(iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(7002)).thenReturn(od2);

        ContractConsumptionDto result1 = new ContractConsumptionDto();
        result1.setServiceRate(100.0);
        result1.setConcession(0.0);
        result1.setTaxAmt(5.0);
        result1.setFinalPrice(105.0);
        result1.setTaxPercentage(5.0);
        result1.setSelfPayable(105.0);
        result1.setPayeeId(1);
        result1.setCreaditPayable(0.0);
        result1.setContractId(77);
        result1.setPackageConsumptionAmt(BigDecimal.ZERO);
        result1.setBillingBedCategoryId(2);
        result1.setOrderDetailsPayeeMapperDtosList(Collections.emptyList());
        result1.setOrderDetailsPackageMapperDtosList(Collections.emptyList());

        ContractConsumptionDto result2 = new ContractConsumptionDto();
        result2.setServiceRate(200.0);
        result2.setConcession(0.0);
        result2.setTaxAmt(10.0);
        result2.setFinalPrice(210.0);
        result2.setTaxPercentage(5.0);
        result2.setSelfPayable(210.0);
        result2.setPayeeId(1);
        result2.setCreaditPayable(0.0);
        result2.setContractId(78);
        result2.setPackageConsumptionAmt(BigDecimal.ZERO);
        result2.setBillingBedCategoryId(2);
        result2.setOrderDetailsPayeeMapperDtosList(Collections.emptyList());
        result2.setOrderDetailsPackageMapperDtosList(Collections.emptyList());

        when(iMPackageMasterService.checkServiceAvailableInPackageContract(any(ContractConsumptionDto.class)))
                .thenAnswer(inv -> {
                    ContractConsumptionDto arg = inv.getArgument(0);
                    ContractConsumptionDto result = arg.getServiceId() != null && arg.getServiceId() == 66 ? result2 : result1;
                    return new com.param.global.common.Response<>("success", null, null, null, result);
                });

        service.convertServicesFromOPToIP(request(Arrays.asList(7001, 7002)));

        verify(od1).setServiceAmt(new BigDecimal(100.0));
        verify(od1).setContractId(77);
        verify(od2).setServiceAmt(new BigDecimal(200.0));
        verify(od2).setContractId(78);
    }
}
