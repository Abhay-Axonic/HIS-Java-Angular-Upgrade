package com.param.global.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.dto.ContractConsumptionDto;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * This test pins down what {@link BillingCommonMethods#updatePreAuthAndServiceCapAmountForSecondContract}
 * actually does today. It is NOT a claim that this behavior is correct -- several assertions below
 * document behavior that looks like a real defect (see the "KNOWN GAP" test group). The point of a
 * characterization test, per docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Part 3, is to make any
 * future change to this method -- a Java upgrade, a refactor, a deliberate bug fix -- show up as a
 * failing assertion that forces a conscious decision, logged as a Behavioral Change per RULES.md,
 * rather than silently changing what a "second round" contract co-pay/pre-auth/cap split produces.
 *
 * Ranked #1 in docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 (Global's shared
 * co-pay/pre-auth-cap split -- highest fan-in of any calculation in scope: this exact method has 7
 * call sites across ContractConsumptionServiceImpl.java alone).
 *
 * GAP FOUND WHILE WRITING THIS TEST -- FIXED 2026-07-15
 * (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 1, owner-approved, after review of
 * concrete before/after dollar examples for all 7 call sites). Previously: at every one of its 7 call
 * sites, this method's caller pre-computed its own local totalSelfPayable/totalCreditPayable, called
 * this method, and captured the return value -- but the co-share-percentage and pre-auth-ceiling
 * adjustments this method computes for those two figures were LOCAL to the method (Double parameters
 * are pass-by-value in Java) and were never written onto the returned ContractConsumptionDto (it only
 * got setContractId called on it). The DATABASE writes (updatePreAuthAmount / updateCapAmount /
 * updateServiceCapAmount) always received the correctly-adjusted amount -- only the returned object,
 * and therefore the caller's own running totals (which is what ultimately becomes the patient's bill),
 * did not.
 *
 * Now: this method also stamps the adjusted totalSelfPayable/totalCreditPayable onto the returned
 * consumptionDto via setSelfPayable/setCreaditPayable, mirroring the sibling
 * updatePreAuthAndServiceCapAmountForFirstPriorityContract's existing pattern. Critically, all 7 call
 * sites in ContractConsumptionServiceImpl.java were updated to read the correction back via plain
 * ASSIGNMENT (totalSelfPayable = creditAndSelfAmountDto.getSelfPayable()), not accumulation (+=) --
 * this method receives and returns the caller's entire running total, not a per-round delta the way
 * the sibling method does, so copying the sibling callers' += pattern verbatim would have silently
 * double-counted the adjustment. See ReturnValueGap below, which now pins the fixed behavior.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract -- characterization (current behavior, not verified-correct behavior)")
class BillingCommonMethodsCharacterizationTest {

    @Mock
    private IContractConsumptionDao contractConsumptionDao;

    private BillingCommonMethods<Object> billingCommonMethods;

    @BeforeEach
    void setUp() {
        billingCommonMethods = new BillingCommonMethods<>();
        // Package-private field injection (no Spring context needed for this unit) -- same package,
        // matches the field's actual (default) visibility in BillingCommonMethods.java.
        billingCommonMethods.iContractConsumptionDao = contractConsumptionDao;
    }

    private ContractConsumptionDto secondRoundContract(Double coShare, Double balanceCapAmount) {
        ContractConsumptionDto secondRound = new ContractConsumptionDto();
        secondRound.setCoShare(coShare);
        secondRound.setBalanceCapAmount(balanceCapAmount);
        secondRound.setPreauthId(4242);
        secondRound.setContractId(99);
        return secondRound;
    }

    private ContractConsumptionDto billContext(boolean applyAuth) {
        ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
        consumptionDto.setApplyAuth(applyAuth);
        consumptionDto.setUpdateCap(null); // null is required for any DAO write branch to fire at all
        return consumptionDto;
    }

    @Nested
    @DisplayName("no co-share, no pre-auth ceiling -- the simplest real path")
    class NoAdjustments {

        @Test
        @DisplayName("passes the caller's credit amount straight through to updateCapAmount, unmodified")
        void passesCreditAmountThroughUnmodified_whenCheckFlagIsTrue() {
            ContractConsumptionDto secondRound = secondRoundContract(null, 500.0);
            ContractConsumptionDto consumptionDto = billContext(false);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound,
                    /* creditPayableSecondRound */ 300.0,
                    /* totalSelfPayable */ 700.0,
                    /* totalCreditPayable */ 300.0,
                    /* secondRounfCoShare */ null,
                    /* preAuthAmount */ null,
                    consumptionDto,
                    /* checkFlagUpdateCapOrServiceCap */ true,
                    /* qtyToBeUpdated */ null,
                    /* fromContact */ false);

            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 300.0);
            verify(contractConsumptionDao, never()).updateServiceCapAmount(any(), any(), any());
            verify(contractConsumptionDao, never()).updatePreAuthAmount(any(), any());
        }

        @Test
        @DisplayName("routes to updateServiceCapAmount instead of updateCapAmount when checkFlagUpdateCapOrServiceCap is false")
        void routesToServiceCapAmount_whenCheckFlagIsFalse() {
            ContractConsumptionDto secondRound = secondRoundContract(null, 500.0);
            ContractConsumptionDto consumptionDto = billContext(false);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, /* checkFlagUpdateCapOrServiceCap */ false, /* qtyToBeUpdated */ null, false);

            verify(contractConsumptionDao).updateServiceCapAmount(secondRound, 300.0, null);
            verify(contractConsumptionDao, never()).updateCapAmount(any(), any());
        }

        @Test
        @DisplayName("passes a quantity instead of an amount to updateServiceCapAmount when qtyToBeUpdated is supplied")
        void passesQuantity_whenQtyToBeUpdatedIsSupplied() {
            ContractConsumptionDto secondRound = secondRoundContract(null, 500.0);
            ContractConsumptionDto consumptionDto = billContext(false);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, false, /* qtyToBeUpdated */ 2.0, false);

            // Current behavior: when a quantity is supplied, the amount argument is dropped to null
            // entirely -- not "amount AND quantity", one or the other.
            verify(contractConsumptionDao).updateServiceCapAmount(secondRound, null, 2.0);
        }
    }

    @Nested
    @DisplayName("co-share percentage on the second-round contract (fromContact=true)")
    class CoShareAdjustment {

        @Test
        @DisplayName("reduces the amount sent to the database by exactly coShare percent")
        void reducesDatabaseAmountByCoSharePercent() {
            ContractConsumptionDto secondRound = secondRoundContract(/* coShare */ 20.0, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(false);

            // creditPayableSecondRound=300.0, coShare=20% -> co-share = 60.0 -> adjusted credit = 240.0
            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, true, null, /* fromContact */ true);

            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 240.0);
        }

        @Test
        @DisplayName("does NOT apply the co-share reduction when fromContact is false, even with the same coShare set")
        void noCoShareReduction_whenFromContactIsFalse() {
            ContractConsumptionDto secondRound = secondRoundContract(20.0, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(false);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, true, null, /* fromContact */ false);

            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 300.0);
        }
    }

    @Nested
    @DisplayName("pre-authorization ceiling")
    class PreAuthCeiling {

        @Test
        @DisplayName("caps the database write at preAuthAmount when the credit amount exceeds it, and records the pre-auth consumption")
        void capsAtPreAuthAmount_whenExceeded() {
            ContractConsumptionDto secondRound = secondRoundContract(null, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(/* applyAuth */ true);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, /* creditPayableSecondRound */ 500.0, 700.0, 300.0, null,
                    /* preAuthAmount */ 350.0, consumptionDto, true, null, false);

            verify(contractConsumptionDao).updatePreAuthAmount(4242, 350.0);
            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 350.0);
        }

        @Test
        @DisplayName("does not touch preAuthAmount at all when applyAuth is false on the bill context, even if preAuthAmount is set")
        void ignoresPreAuthAmount_whenApplyAuthIsFalse() {
            ContractConsumptionDto secondRound = secondRoundContract(null, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(/* applyAuth */ false);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 500.0, 700.0, 300.0, null, /* preAuthAmount */ 350.0,
                    consumptionDto, true, null, false);

            verify(contractConsumptionDao, never()).updatePreAuthAmount(any(), any());
            // Full 500.0 reaches the cap write, uncapped, since the pre-auth branch never ran.
            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 500.0);
        }
    }

    @Nested
    @DisplayName("guard: consumptionDto.updateCap already set")
    class UpdateCapAlreadySet {

        @Test
        @DisplayName("skips every database write when updateCap is non-null, regardless of any other input")
        void skipsAllWrites_whenUpdateCapIsAlreadySet() {
            ContractConsumptionDto secondRound = secondRoundContract(20.0, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(true);
            consumptionDto.setUpdateCap('Y');

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 500.0, 700.0, 300.0, null, 350.0,
                    consumptionDto, true, null, true);

            verifyNoInteractions(contractConsumptionDao);
        }
    }

    @Nested
    @DisplayName("ReturnValueGap -- FIXED 2026-07-15: the returned dto now carries the adjusted totals")
    class ReturnValueGap {

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 1, owner-approved): the returned dto now carries the co-share/pre-auth-adjusted totalSelfPayable and totalCreditPayable")
        void returnedDto_nowCarriesTheAdjustedTotals() {
            ContractConsumptionDto secondRound = secondRoundContract(/* coShare */ 20.0, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setSelfPayable(null);
            consumptionDto.setCreaditPayable(null);

            ContractConsumptionDto returned = billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, true, null, /* fromContact */ true);

            // The database write receives the co-share-adjusted amount (240.0, not 300.0) --
            // confirmed by CoShareAdjustment#reducesDatabaseAmountByCoSharePercent above. Now the
            // object the caller gets back carries the SAME adjustment: 20% of 300.0 = 60.0 shifts
            // from credit to self, so totalSelfPayable (700.0 + 60.0 = 760.0) and totalCreditPayable
            // (300.0 - 60.0 = 240.0) are both stamped onto the returned dto -- previously they stayed
            // exactly as the caller left them (null, in this test).
            assertThat(returned).isSameAs(consumptionDto);
            assertThat(returned.getSelfPayable()).isEqualTo(760.0);
            assertThat(returned.getCreaditPayable()).isEqualTo(240.0);
        }

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 1, owner-approved): setContractId AND the adjusted selfPayable/creaditPayable are now both invoked on the returned dto, overwriting whatever the caller had set before the call")
        void contractIdAndAdjustedTotals_areBothMutatedOnTheReturnedDto() {
            ContractConsumptionDto secondRound = secondRoundContract(20.0, 1000.0);
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setContractId(1);
            consumptionDto.setSelfPayable(111.0);
            consumptionDto.setCreaditPayable(222.0);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    secondRound, 300.0, 700.0, 300.0, null, null,
                    consumptionDto, true, null, true);

            assertThat(consumptionDto.getContractId()).isEqualTo(secondRound.getContractId());
            // Previously these stayed exactly as the caller had set them (111.0/222.0), proving the
            // method never touched them. Now they're overwritten with the freshly computed adjusted
            // totals -- 700.0 + 60.0 (20% co-share of 300.0) = 760.0 self, 300.0 - 60.0 = 240.0
            // credit -- unrelated to whatever pre-existing values consumptionDto happened to carry
            // going in.
            assertThat(consumptionDto.getSelfPayable()).isEqualTo(760.0);
            assertThat(consumptionDto.getCreaditPayable()).isEqualTo(240.0);
        }
    }
}
