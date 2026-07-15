package com.param.billing.global.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.dao.IPaymentDepositConsumptionDao;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * Targets {@link PaymentDepositConsumptionServiceImpl#consumeDeposit}, entry point #4 in
 * docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 -- also the exact method
 * docs/AUDIT_BillingRisk.md RISK-3 flags for the lost-update / absolute-`SET` deposit-balance
 * pattern (no version/lock on the write). This suite characterizes what `consumeDeposit` itself
 * computes and passes to its collaborators (`IDepositMasterService.updateDepositMaster` /
 * `updateDepositDetails`) -- it does not reach into those collaborators' own DAO implementations,
 * since whether the eventual database write is absolute or relative is that layer's own concern,
 * not this method's.
 *
 * TWO GENUINE FINDINGS from reading the method closely while building fixtures for it (flagged, not
 * fixed -- needs an owner decision, not a unilateral AI fix):
 *
 * 1. BACKWARDS-LOOKING NULL CHECK: `if (depositDto.getBillId() != null) depositDto.setBillId(reqDto
 *    .getBillingMasterId());` (and the identical pattern for `billPayeeDetailsId`) only overwrites
 *    the field when it is ALREADY non-null -- the opposite of the usual "fill in if missing" idiom.
 *    A `PaymentDepositDto` arriving with `billId == null` keeps it null; the request's own
 *    `billingMasterId` is silently never attached to it. Whether this is intentional (only stamp a
 *    bill reference onto records that already indicate they need one) or an inverted condition is a
 *    real question this session does not answer. See BillIdBackwardsCheck below.
 *
 * 2. READ-THEN-WRITE-BACK NO-OP: `Double detailsConsumedAmount = depositDto.getConsumedAmount();
 *    depositDto.setConsumedAmount(detailsConsumedAmount);` reads the DTO's own field and immediately
 *    writes the identical value back -- confirmed via a test that this has no observable effect
 *    (whatever the caller/request set `consumedAmount` to is exactly what reaches
 *    `updateDepositMaster`). Not fixed; noted because it reads like either dead code left over from
 *    a refactor, or a mistaken assignment that was meant to compute something else.
 *
 * Per Decision 4 (owner-approved): assertions use exact equality, not RULES.md's $0.01 tolerance.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentDepositConsumptionServiceImpl.consumeDeposit -- characterization (current behavior, not verified-correct behavior)")
class PaymentDepositConsumptionServiceImplCharacterizationTest {

    @Mock private IDepositMasterService iDepositMasterService;
    @Mock private IPaymentDepositConsumptionDao iPaymentDepositConsumptionDao;
    @Mock private IGlobalUnitMasterDao iGlobalUnitMasterDao;

    @InjectMocks
    private PaymentDepositConsumptionServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        when(iGlobalUnitMasterDao.getGeneratedNo(anyInt(), anyInt(), anyInt())).thenReturn("DC-000001");
    }

    @SuppressWarnings("unchecked")
    private Response<Object, java.io.Serializable> mockResponse() {
        return mock(Response.class);
    }

    private BillPaymentCollectionReqDto request(Double totalDepositConsumption, List<PaymentDepositDto> deposits) {
        BillPaymentCollectionReqDto req = new BillPaymentCollectionReqDto();
        req.setUnitId(1);
        req.setBillingMasterId(500);
        req.setBillPayeeDetailsId(700);
        req.setTotalDepositConsumption(totalDepositConsumption);
        req.setPaymentDepositDetailsList(deposits);
        req.setUpdatedBy(1);
        req.setUpdatedDate("2026-07-15");
        return req;
    }

    private PaymentDepositDto depositDto(Integer depositMasterId, Integer billId, Double consumedAmount) {
        PaymentDepositDto dto = new PaymentDepositDto();
        dto.setDepositMasterId(depositMasterId);
        dto.setBillId(billId);
        dto.setBillPayeeDetailsId(billId); // deliberately mirrors billId for this test's purposes
        dto.setConsumedAmount(consumedAmount);
        dto.setOrganizationId(1);
        dto.setUnitId(1);
        dto.setRemark("test");
        dto.setCreatedBy(1);
        dto.setDepConsumptionDate("2026-07-15");
        return dto;
    }

    private PaymentDepositDetailsDto detailRow(Double balanceAmount) {
        PaymentDepositDetailsDto dto = new PaymentDepositDetailsDto();
        dto.setBalanceAmount(balanceAmount);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void stubAddDepositConsumption(Integer depositConsumptionId) throws Exception {
        Response<Object, java.io.Serializable> res = mockResponse();
        when(res.getObject()).thenReturn(depositConsumptionId);
        when(iPaymentDepositConsumptionDao.addDepositConsumption(any())).thenReturn(res);
    }

    @SuppressWarnings("unchecked")
    private void stubDepositDetails(Integer depositMasterId, List<PaymentDepositDetailsDto> details) throws Exception {
        Response<Object, java.io.Serializable> res = mockResponse();
        when(res.getListObject()).thenReturn((List) details);
        when(iDepositMasterService.getDepositDetailsByDepositId(depositMasterId)).thenReturn(res);
    }

    @Nested
    @DisplayName("single deposit, single detail row")
    class SingleDetailRow {

        @Test
        @DisplayName("fully consumes a detail row whose balance covers the requested amount, in one pass")
        void fullyConsumesDetailRow_whenBalanceCoversRequest() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 300.0);
            stubDepositDetails(10, Arrays.asList(detailRow(500.0)));

            BillPaymentCollectionReqDto req = request(300.0, Arrays.asList(deposit));

            Response<?, ?> result = service.consumeDeposit(req);

            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService).updateDepositDetails(captor.capture());
            assertThat(captor.getValue().getConsumedAmount()).isEqualTo(300.0);
            assertThat(captor.getValue().getBalanceAmount()).isEqualTo(200.0); // 500.0 - 300.0
            assertThat(result.getObject()).isEqualTo(300.0); // finalConsumption
        }

        @Test
        @DisplayName("consumes only what the detail row's balance allows when the request exceeds it, leaving zero balance")
        void consumesOnlyAvailableBalance_whenRequestExceedsIt() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 800.0);
            stubDepositDetails(10, Arrays.asList(detailRow(500.0)));

            BillPaymentCollectionReqDto req = request(800.0, Arrays.asList(deposit));

            Response<?, ?> result = service.consumeDeposit(req);

            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService).updateDepositDetails(captor.capture());
            assertThat(captor.getValue().getConsumedAmount()).isEqualTo(500.0);
            assertThat(captor.getValue().getBalanceAmount()).isEqualTo(0.0);
            // Only 500.0 was actually available and consumed -- the remaining 300.0 of the
            // requested 800.0 is simply never accounted for anywhere in this method (no error,
            // no partial-consumption flag) -- pinning this as current behavior, not endorsing it.
            assertThat(result.getObject()).isEqualTo(500.0);
        }

        @Test
        @DisplayName("pins the exact Math.round(balance * 100.0) / 100.0 rounding rule (RISK-2) against a non-round balance")
        void pinsTheExactRoundingRule() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 100.0);
            stubDepositDetails(10, Arrays.asList(detailRow(333.336)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService).updateDepositDetails(captor.capture());
            // Pinned to the literal observed double value, not re-derived through a separate
            // Math.round(...)-then-subtract expression in this test -- that parallel calculation
            // was tried first and produced a different last-bit result (233.33999999999997) than
            // production's actual output (233.34) despite looking like the same arithmetic, which
            // is itself a small illustration of why Decision 4 (exact-equality pinning against
            // observed output, not re-derivation) is the right rule for wave 1.
            assertThat(captor.getValue().getBalanceAmount()).isEqualTo(233.34);
        }
    }

    @Nested
    @DisplayName("one deposit, multiple detail rows -- consumption spans rows")
    class MultipleDetailRows {

        @Test
        @DisplayName("drains the first row, then continues into the second row for the remainder, in list order")
        void spansAcrossDetailRowsInListOrder() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 600.0);
            stubDepositDetails(10, Arrays.asList(detailRow(400.0), detailRow(1000.0)));

            Response<?, ?> result = service.consumeDeposit(request(600.0, Arrays.asList(deposit)));

            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService, times(2)).updateDepositDetails(captor.capture());
            List<PaymentDepositDetailsDto> updates = captor.getAllValues();

            assertThat(updates.get(0).getConsumedAmount()).isEqualTo(400.0); // first row fully drained
            assertThat(updates.get(0).getBalanceAmount()).isEqualTo(0.0);
            assertThat(updates.get(1).getConsumedAmount()).isEqualTo(200.0); // remaining 600-400
            assertThat(updates.get(1).getBalanceAmount()).isEqualTo(800.0); // 1000 - 200
            assertThat(result.getObject()).isEqualTo(600.0); // finalConsumption across both rows
        }

        @Test
        @DisplayName("still touches every remaining row with a real (no-op) update even after the requested amount is fully exhausted -- the >0 guard is checked once BEFORE the loop, not per-iteration")
        void stillTouchesLaterRows_withZeroConsumedAmount_onceExhausted() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 100.0);
            stubDepositDetails(10, Arrays.asList(detailRow(100.0), detailRow(500.0)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            // The `if(consumedAmount>0)` guard wraps the whole `while(listIterator.hasNext())` loop,
            // not each iteration -- once entered (because consumedAmount started > 0), the loop runs
            // to the end of the list regardless of whether consumedAmount has since reached 0. Row 1
            // exactly satisfies the request; row 2 still gets a real updateDepositDetails call, with
            // consumedAmount forced to 0 (0 >= detailsAmount is false, so the else-branch sets
            // consumedAmount(0)) and its balance left numerically unchanged -- a real database write
            // that changes nothing, on every row after the amount is exhausted.
            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService, times(2)).updateDepositDetails(captor.capture());
            PaymentDepositDetailsDto secondRowUpdate = captor.getAllValues().get(1);
            assertThat(secondRowUpdate.getConsumedAmount()).isEqualTo(0.0);
            assertThat(secondRowUpdate.getBalanceAmount()).isEqualTo(500.0); // unchanged
        }
    }

    @Nested
    @DisplayName("BillIdBackwardsCheck -- flagged during test authoring, not fixed, needs an owner decision")
    class BillIdBackwardsCheck {

        @Test
        @DisplayName("does NOT attach the request's billingMasterId when the deposit's own billId starts null -- only overwrites when already non-null")
        void doesNotFillInBillId_whenStartingNull() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, /* billId */ null, 100.0);
            deposit.setBillPayeeDetailsId(null);
            stubDepositDetails(10, Arrays.asList(detailRow(500.0)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            // reqDto.getBillingMasterId() is 500 (set in request()) -- if the intent were "fill in
            // the bill reference," this would now be 500. It is not: it stays null, because the
            // guard is `if (getBillId() != null)`, not `== null`.
            assertThat(deposit.getBillId()).isNull();
        }

        @Test
        @DisplayName("DOES overwrite billId with the request's billingMasterId when the deposit's own billId already had some non-null placeholder")
        void overwritesBillId_whenAlreadyNonNull() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, /* billId */ -1, 100.0); // any non-null placeholder
            stubDepositDetails(10, Arrays.asList(detailRow(500.0)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            assertThat(deposit.getBillId()).isEqualTo(500); // reqDto.getBillingMasterId()
        }
    }

    @Nested
    @DisplayName("ReadWriteBackNoOp -- flagged during test authoring, not fixed, needs an owner decision")
    class ReadWriteBackNoOp {

        @Test
        @DisplayName("the deposit master update receives exactly the consumedAmount the caller already set -- the read-then-write-back has no observable effect")
        void updateDepositMasterReceivesTheOriginalConsumedAmountUnchanged() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 777.0); // an arbitrary value the "no-op" line reads and writes back
            stubDepositDetails(10, Arrays.asList(detailRow(500.0)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            ArgumentCaptor<PaymentDepositDto> captor = ArgumentCaptor.forClass(PaymentDepositDto.class);
            verify(iDepositMasterService).updateDepositMaster(captor.capture());
            assertThat(captor.getValue().getConsumedAmount()).isEqualTo(777.0);
        }
    }
}
