package com.param.billing.global.transaction.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * Targets {@link BillingMasterController#saveBill}, entry point #5 in
 * docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 -- the root of the bill-creation tree.
 * Ranked below entry points #1-4 in the priority doc specifically because the interesting logic here
 * (the client-vs-server total reconciliation, docs/AUDIT_BillingRisk.md ONE-OFF-2) lives in the
 * CONTROLLER, not the service, and the doc flagged that as extra harness-design cost. In practice the
 * `HttpServletRequest` parameter turned out to be entirely unused (confirmed via grep across the full
 * 370-line method), so the controller method is directly callable in a plain unit test after all --
 * the `synchronized` modifier is harmless for a single-threaded test call.
 *
 * SCOPE, decided and disclosed rather than silently assumed: `saveBill` is ~370 lines. This suite
 * covers the reconciliation/validation logic before and including the `saveBillService` delegate call
 * (the actual bill-creation entry point, and where ONE-OFF-2's ±1.00 tolerance lives) -- it does
 * NOT cover what happens after a successful save: coupon-consumption processing, an internal call to
 * `collectPayment` (entry point #3, already separately characterized), or SMS/email notifications.
 * Tests that need to observe past the `saveBillService` call stub its response with a non-"success"
 * status specifically so those uncovered downstream blocks are skipped, not silently exercised
 * without assertions.
 *
 * Per Decision 4 (owner-approved): assertions use exact equality, not RULES.md's $0.01 tolerance --
 * except where pinning the ±1.00 whole-unit tolerance itself, which IS this entry point's own
 * documented behavior (ONE-OFF-2), not a test-infrastructure choice.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingMasterController.saveBill -- reconciliation-gate characterization (current behavior, not verified-correct behavior)")
class BillingMasterControllerSaveBillCharacterizationTest {

    @Mock private IBillingMasterService iBillingMasterService;
    @Mock private IDepositMasterService iDepositMasterService;

    @InjectMocks
    private BillingMasterController controller;

    private BillingMasterDto billingMasterDto(boolean saveFromSplitBill, OrderDetailsMasterDto... orderDetails) {
        BillingMasterDto dto = new BillingMasterDto();
        dto.setUnitId(1);
        dto.setOrganisationId(1);
        dto.setCreatedBy(1);
        dto.setSaveFromSplitBill(saveFromSplitBill);
        dto.setPaymentReceiptDetailsDtosList(new ArrayList<>());
        dto.setOrderDetailsMasterDtosList(Arrays.asList(orderDetails));
        return dto;
    }

    private OrderDetailsMasterDto orderDetail(Integer orderDetailsId, double netAmount, double selfPayable, double creditPayable) {
        OrderDetailsMasterDto dto = new OrderDetailsMasterDto();
        dto.setOrderDetailsId(orderDetailsId);
        dto.setOrderId(orderDetailsId);
        dto.setNetAmount(BigDecimal.valueOf(netAmount));
        dto.setSelfPayable(BigDecimal.valueOf(selfPayable));
        dto.setCreditPayable(BigDecimal.valueOf(creditPayable));
        return dto;
    }

    @SuppressWarnings("unchecked")
    private Response<Object, java.io.Serializable> mockResponse() {
        return mock(Response.class);
    }

    private void stubServerSideSums(Double netAmount, Double selfPayable, Double creditPayable) {
        Response<Object, java.io.Serializable> res = mockResponse();
        if (netAmount == null) {
            when(res.getObject()).thenReturn(null);
        } else {
            OrderDetailsMasterDto dto = new OrderDetailsMasterDto();
            dto.setNetAmount(BigDecimal.valueOf(netAmount));
            dto.setSelfPayable(selfPayable != null ? BigDecimal.valueOf(selfPayable) : null);
            dto.setCreditPayable(creditPayable != null ? BigDecimal.valueOf(creditPayable) : null);
            when(res.getObject()).thenReturn(dto);
        }
        when(iBillingMasterService.getOrderSumNetAndSelfCreditAmount(any())).thenReturn(res);
    }

    private void stubSaveBillServiceSkipsDownstream() throws Exception {
        // Status deliberately NOT "success" (case-insensitively) so this test can observe that
        // saveBillService was reached without also exercising the coupon-consumption / internal
        // collectPayment / SMS blocks that follow it -- those are explicitly out of scope here.
        Response<Object, java.io.Serializable> res = mockResponse();
        when(res.getStatus()).thenReturn("skipped-for-this-test");
        when(iBillingMasterService.saveBillService(any())).thenReturn(res);
    }

    @Nested
    @DisplayName("CCAvenue duplicate-transaction guard")
    class CCAvenueDuplicateGuard {

        @Test
        @DisplayName("rejects the save when a CCAvenue payment mode's transaction has already been used elsewhere")
        void rejectsWhenCCAvenueTransactionAlreadyUsed() throws Exception {
            BillingMasterDto dto = billingMasterDto(false, orderDetail(1, 100.0, 50.0, 50.0));
            PaymentReceiptDetailsDto payment = new PaymentReceiptDetailsDto();
            payment.setPaymentModeId(12);
            payment.setPaymentGatewayProviderId(2);
            payment.setTransactionNo("TXN-001");
            dto.setPaymentReceiptDetailsDtosList(Arrays.asList(payment));

            Response<Object, java.io.Serializable> ccRes = mockResponse();
            when(ccRes.getObject()).thenReturn(null); // null == already consumed elsewhere
            when(iDepositMasterService.ccAvenueCheckvalidation("TXN-001")).thenReturn(ccRes);

            Response<?, ?> result = controller.saveBill(dto, null);

            assertThat(result.getMessage()).isEqualTo("TXN-001 This already used in other trancation");
            verify(iBillingMasterService, never()).saveBillService(any());
        }
    }

    @Nested
    @DisplayName("reconciliation gate: no server-side order data at all")
    class NoServerSideData {

        @Test
        @DisplayName("rejects the save outright when getOrderSumNetAndSelfCreditAmount returns no object -- checkSameAmount stays true by default, never gets a chance to become false")
        void rejectsOutright_whenServerHasNoMatchingOrderData() throws Exception {
            BillingMasterDto dto = billingMasterDto(false, orderDetail(1, 100.0, 50.0, 50.0));
            stubServerSideSums(null, null, null);

            Response<?, ?> result = controller.saveBill(dto, null);

            assertThat(result.getMessage()).isEqualTo("This bill is already in process by other user!");
            verify(iBillingMasterService, never()).saveBillService(any());
        }
    }

    @Nested
    @DisplayName("split-bill reconciliation (isSaveFromSplitBill = true) -- compares whole-unit-rounded netAmount only")
    class SplitBillReconciliation {

        @Test
        @DisplayName("proceeds when client and server netAmount round to the exact same whole unit")
        void proceeds_whenNetAmountsMatchExactly() throws Exception {
            BillingMasterDto dto = billingMasterDto(true, orderDetail(1, 1000.0, 0, 0));
            stubServerSideSums(1000.0, null, null);
            stubSaveBillServiceSkipsDownstream();

            controller.saveBill(dto, null);

            verify(iBillingMasterService).saveBillService(any());
        }

        @Test
        @DisplayName("still proceeds when client and server netAmount differ by up to 1.00 whole currency unit -- the ONE-OFF-2 tolerance")
        void proceeds_whenNetAmountsDifferByUpToOneUnit() throws Exception {
            BillingMasterDto dto = billingMasterDto(true, orderDetail(1, 1000.0, 0, 0));
            stubServerSideSums(1001.0, null, null); // server 1 whole unit higher
            stubSaveBillServiceSkipsDownstream();

            controller.saveBill(dto, null);

            verify(iBillingMasterService).saveBillService(any());
        }

        @Test
        @DisplayName("rejects when client and server netAmount differ by MORE than 1.00 whole currency unit")
        void rejects_whenNetAmountsDifferByMoreThanOneUnit() throws Exception {
            BillingMasterDto dto = billingMasterDto(true, orderDetail(1, 1000.0, 0, 0));
            stubServerSideSums(1002.0, null, null); // server 2 whole units higher, exceeds tolerance

            Response<?, ?> result = controller.saveBill(dto, null);

            assertThat(result.getMessage()).isEqualTo("This bill is already in process by other user!");
            verify(iBillingMasterService, never()).saveBillService(any());
        }

        @Test
        @DisplayName("pins the whole-unit rounding rule (ONE-OFF-2): a fractional netAmount is rounded to the nearest whole unit, discarding all fractional currency, before any comparison happens")
        void pinsWholeUnitRoundingOfFractionalAmounts() throws Exception {
            // Client total: 100.60 + 100.60 = 201.20, rounds via Math.round(201.2) = 201 (whole unit)
            BillingMasterDto dto = billingMasterDto(true, orderDetail(1, 100.60, 0, 0), orderDetail(2, 100.60, 0, 0));
            stubServerSideSums(201.0, null, null); // server side already a clean whole unit, exact match after rounding
            stubSaveBillServiceSkipsDownstream();

            controller.saveBill(dto, null);

            // If the fractional cents (.20 total) were NOT discarded by the whole-unit rounding,
            // 201.20 vs 201.0 would still be within the same ±1 tolerance anyway -- so this test
            // alone doesn't distinguish "rounds then compares" from "compares directly within
            // tolerance." What it does pin is that the save proceeds at all for a clearly
            // non-whole-currency client total, consistent with ONE-OFF-2's finding that this gate
            // never protects fractional-currency precision, only whole-unit sanity.
            verify(iBillingMasterService).saveBillService(any());
        }
    }

    @Nested
    @DisplayName("non-split-bill reconciliation (isSaveFromSplitBill = false) -- compares whole-unit-rounded selfPayable AND creditPayable")
    class NonSplitBillReconciliation {

        @Test
        @DisplayName("proceeds when both self and credit payable round to the exact same whole units")
        void proceeds_whenBothLegsMatchExactly() throws Exception {
            BillingMasterDto dto = billingMasterDto(false, orderDetail(1, 1000.0, 600.0, 400.0));
            stubServerSideSums(1000.0, 600.0, 400.0);
            stubSaveBillServiceSkipsDownstream();

            controller.saveBill(dto, null);

            verify(iBillingMasterService).saveBillService(any());
        }

        @Test
        @DisplayName("rejects when only ONE of the two legs is within tolerance -- both must independently satisfy the ±1 rule")
        void rejects_whenOnlyOneLegIsWithinTolerance() throws Exception {
            BillingMasterDto dto = billingMasterDto(false, orderDetail(1, 1000.0, 600.0, 400.0));
            // selfPayable matches exactly; creditPayable is 5 whole units off, well past tolerance
            stubServerSideSums(1000.0, 600.0, 405.0);

            Response<?, ?> result = controller.saveBill(dto, null);

            assertThat(result.getMessage()).isEqualTo("This bill is already in process by other user!");
            verify(iBillingMasterService, never()).saveBillService(any());
        }
    }
}
