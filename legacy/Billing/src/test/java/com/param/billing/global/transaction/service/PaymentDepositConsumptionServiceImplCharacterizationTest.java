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
 * TWO GENUINE FINDINGS from reading the method closely while building fixtures for it:
 *
 * 1. BACKWARDS-LOOKING NULL CHECK (still open, blocked pending a business-intent answer -- see
 *    docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 4b): `if (depositDto.getBillId() !=
 *    null) depositDto.setBillId(reqDto.getBillingMasterId());` (and the identical pattern for
 *    `billPayeeDetailsId`) only overwrites the field when it is ALREADY non-null -- the opposite of
 *    the usual "fill in if missing" idiom. A `PaymentDepositDto` arriving with `billId == null` keeps
 *    it null; the request's own `billingMasterId` is silently never attached to it. Whether this is
 *    intentional (only stamp a bill reference onto records that already indicate they need one) or an
 *    inverted condition is a real question this session does not answer. See BillIdBackwardsCheck
 *    below.
 *
 * 2. READ-THEN-WRITE-BACK NO-OP -- FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md,
 *    finding 4c, owner-approved). Previously: `Double detailsConsumedAmount = depositDto
 *    .getConsumedAmount(); depositDto.setConsumedAmount(detailsConsumedAmount);` read the DTO's own
 *    field and immediately wrote the identical value back -- confirmed via a test that this had no
 *    observable effect. Now: the pointless write-back line is deleted; `detailsConsumedAmount` is
 *    still read and used exactly as before everywhere else in the method (the deposit-consumption-
 *    details save, and as the local running-total base). See ReadWriteBackNoOp below, which still
 *    passes unchanged -- the whole point of deleting a true no-op is that nothing observable moves.
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

            assertThat(updates.getFirst().getConsumedAmount()).isEqualTo(400.0); // first row fully drained
            assertThat(updates.getFirst().getBalanceAmount()).isEqualTo(0.0);
            assertThat(updates.get(1).getConsumedAmount()).isEqualTo(200.0); // remaining 600-400
            assertThat(updates.get(1).getBalanceAmount()).isEqualTo(800.0); // 1000 - 200
            assertThat(result.getObject()).isEqualTo(600.0); // finalConsumption across both rows
        }

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 4a, owner-approved): stops touching rows once the requested amount is fully exhausted, instead of still writing a no-op update to every remaining row")
        void stopsAtExhaustion_insteadOfTouchingLaterRows() throws Exception {
            stubAddDepositConsumption(999);
            PaymentDepositDto deposit = depositDto(10, 1, 100.0);
            stubDepositDetails(10, Arrays.asList(detailRow(100.0), detailRow(500.0)));

            service.consumeDeposit(request(100.0, Arrays.asList(deposit)));

            // Previously, the `if(consumedAmount>0)` guard wrapped the whole
            // `while(listIterator.hasNext())` loop, not each iteration -- once entered, the loop ran
            // to the end of the list regardless of whether consumedAmount had since reached 0, so row
            // 2 still got a real (no-op) updateDepositDetails call after row 1 exactly satisfied the
            // request. Now the loop condition itself is `listIterator.hasNext() && consumedAmount>0`,
            // re-checked every iteration -- once row 1 exhausts the requested amount, the loop stops
            // before ever touching row 2.
            ArgumentCaptor<PaymentDepositDetailsDto> captor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
            verify(iDepositMasterService, times(1)).updateDepositDetails(captor.capture());
            PaymentDepositDetailsDto onlyUpdate = captor.getValue();
            assertThat(onlyUpdate.getConsumedAmount()).isEqualTo(100.0); // row 1, fully consumed by the request
            assertThat(onlyUpdate.getBalanceAmount()).isEqualTo(0.0);
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
    @DisplayName("ReadWriteBackNoOp -- FIXED 2026-07-15: the no-op write-back line is deleted, behavior unchanged")
    class ReadWriteBackNoOp {

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 4c, owner-approved): the deposit master update still receives exactly the consumedAmount the caller already set, now via the deleted no-op line's absence rather than its presence -- byte-identical output, confirming the deletion was truly a no-op")
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
