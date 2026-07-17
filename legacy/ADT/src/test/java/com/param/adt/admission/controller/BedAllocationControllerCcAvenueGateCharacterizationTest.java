package com.param.adt.admission.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.adt.admission.service.IBedAllocationService;
import com.param.adt.common.Response;
import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.dto.AdmissionDto;

/**
 * Characterization test (current behavior, not verified-correct behavior) for
 * {@code BedAllocationController.savePatientAdmission}'s ccAvenue duplicate-transaction
 * gate -- one of ADT's 3 originally flagged live financial calls
 * ({@code IDepositMasterService.ccAvenueCheckvalidation}). Structurally identical to the
 * already-characterized gate in Pharmacy's {@code BillListService.saveBillDetails}
 * (BR-PENDING-pharmacy-billlistservice-savebilldetails-batch1): payment mode 12 +
 * gateway provider 2 triggers a duplicate-transaction check before anything else in the
 * method runs; a null result rejects the whole admission save immediately.
 *
 * Deliberately NOT covered in this test: the rest of {@code savePatientAdmission}
 * (bed-booking check, patient-sponsor priority, procedure-order-triggered
 * {@code convertServicesFromOPToIP} calls) -- that money math is characterized
 * separately, at its other trigger point in
 * {@code BillingServiceImplConvertServicesFromOPToIPCharacterizationTest} (Global). This
 * controller method is 270+ lines behind one try/catch with many prerequisites
 * (bed-booking, procedure-config lookups); reaching its OWN copy of the
 * convertServicesFromOPToIP trigger would require a disproportionately large fixture for
 * no new money-math coverage beyond what's already pinned at the other trigger point.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BedAllocationController.savePatientAdmission -- ccAvenue duplicate-transaction gate characterization")
class BedAllocationControllerCcAvenueGateCharacterizationTest {

    @Mock private IBedAllocationService iBedAllocationService;
    @Mock private IBillingService globalBillingService;
    @Mock private IDepositMasterService iDepositMasterService;

    private BedAllocationController controller;

    @BeforeEach
    void setUp() {
        controller = new BedAllocationController();
        controller.iBedAllocationService = iBedAllocationService;
        controller.globalBillingService = globalBillingService;
        setPrivate(controller, "iDepositMasterService", iDepositMasterService);
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

    private AdmissionDto admissionWithCcAvenuePayment(String transactionNo) {
        PaymentDepositDetailsDto payment = new PaymentDepositDetailsDto();
        payment.setPaymentModeId(12);
        payment.setPaymentGatewayProviderId(2);
        payment.setTransactionNo(transactionNo);

        DepositCollectionReqDto reqDto = new DepositCollectionReqDto();
        reqDto.setListPaymentDepositDetailsDto(Arrays.asList(payment));

        AdmissionDto admission = new AdmissionDto();
        admission.setReqDto(reqDto);
        return admission;
    }

    @Test
    @DisplayName("ccAvenue transaction already used elsewhere (null result) -> save is rejected immediately, before the bed-booking check ever runs")
    void ccAvenueTransactionAlreadyUsed_rejectsBeforeAnythingElseRuns() {
        AdmissionDto admission = admissionWithCcAvenuePayment("TXN-001");
        when(iDepositMasterService.ccAvenueCheckvalidation("TXN-001"))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, null, null));

        Response result = controller.savePatientAdmission(admission);

        assertThat(result.getStatus()).isEqualTo("error");
        assertThat(result.getMessage()).isEqualTo("TXN-001 This already used in other trancation");
        verify(iBedAllocationService, never()).isBedBookedOrNot(any());
        verify(iBedAllocationService, never()).savePatientAdmission(any());
    }

    @Test
    @DisplayName("ccAvenue transaction is genuinely new (non-null result) -> the gate passes and the method proceeds to the bed-booking check")
    void ccAvenueTransactionIsNew_gatePassesAndProceeds() {
        AdmissionDto admission = admissionWithCcAvenuePayment("TXN-002");
        when(iDepositMasterService.ccAvenueCheckvalidation("TXN-002"))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, null, "some-non-null-marker"));
        // Bed-booking check itself returns "bed already occupied" -- the next gate in the
        // method -- just to confirm the ccAvenue gate's pass-through reaches it, without
        // needing to build a fixture for the rest of this large method.
        when(iBedAllocationService.isBedBookedOrNot(admission))
                .thenReturn(new Response<>("success", null, null, null, null));

        Response result = controller.savePatientAdmission(admission);

        verify(iBedAllocationService).isBedBookedOrNot(admission);
        assertThat(result.getMessage()).isEqualTo("Bed is already occupied...!!");
    }
}
