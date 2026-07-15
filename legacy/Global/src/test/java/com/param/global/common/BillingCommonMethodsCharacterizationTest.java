package com.param.global.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageConsumptionServiceDetailDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;

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
    @Mock
    private com.param.global.dao.ITPackageConsumptionCapDetailsDao tPackageConsumptionCapDetailsDao;

    private BillingCommonMethods<Object> billingCommonMethods;

    @BeforeEach
    void setUp() {
        billingCommonMethods = new BillingCommonMethods<>();
        // Package-private field injection (no Spring context needed for this unit) -- same package,
        // matches the field's actual (default) visibility in BillingCommonMethods.java.
        billingCommonMethods.iContractConsumptionDao = contractConsumptionDao;
        billingCommonMethods.iTPackageConsumptionCapDetailsDao = tPackageConsumptionCapDetailsDao;
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

    private ContractConsumptionDto gstBracket(boolean isGstApplicable, Integer gstAmountRange, Integer gstPercentage) {
        ContractConsumptionDto dto = new ContractConsumptionDto();
        dto.setIsGstApplicable(isGstApplicable);
        dto.setGstAmountRange(gstAmountRange);
        dto.setGstPercentage(gstPercentage);
        return dto;
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

    // =========================================================================================
    // SHADOW-TEST HARNESS EXPANSION (Step 2), batch 1 -- taxCalculation,
    // calculateSelfCreditAndFinalAmount, updatePreAuthAndServiceCapAmountForFirstPriorityContract.
    // Same rigor and process as wave 1: characterize current behavior, log findings, fix nothing.
    // =========================================================================================

    @Nested
    @DisplayName("BillingCommonMethods.taxCalculation -- characterization (current behavior, not verified-correct behavior)")
    class TaxCalculation {

        @Test
        @DisplayName("computes taxAmt as (rate - (concession + discountAmt)) * (gstPercentage / 100) when the single GST bracket applies")
        void computesTax_whenBracketApplies() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(
                    Arrays.asList(gstBracket(true, 500, 18)));

            // rate=1000.0 represents serviceAmount1*qty at the real call site, not a per-unit rate.
            ContractConsumptionDto result = billingCommonMethods.taxCalculation(
                    1000.0, 100.0, 50.0, 7, resDto, /* serviceReteAfterCalculaion */ 900.0);

            assertThat(result.getTaxId()).isEqualTo(0);
            assertThat(result.getTaxPercentage()).isEqualTo(18.0);
            assertThat(result.getTaxAmt()).isEqualTo((1000.0 - (100.0 + 50.0)) * 0.18);
        }

        @Test
        @DisplayName("zeroes out tax when the bracket's amount range is not met, even though isGstApplicable is true")
        void zeroesTax_whenBelowBracketRange() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(
                    Arrays.asList(gstBracket(true, 500, 18)));

            ContractConsumptionDto result = billingCommonMethods.taxCalculation(
                    1000.0, 0.0, 0.0, 7, resDto, /* serviceReteAfterCalculaion */ 400.0);

            assertThat(result.getTaxId()).isEqualTo(0);
            assertThat(result.getTaxPercentage()).isEqualTo(0.00);
            assertThat(result.getTaxAmt()).isEqualTo(0.00);
        }

        @Test
        @DisplayName("returns resDto completely untouched when no GST bracket rows exist -- the loop body never runs at all")
        void leavesDtoUntouched_whenNoBracketsConfigured() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setTaxAmt(999.0); // arbitrary sentinel already on the dto before the call
            resDto.setTaxPercentage(77.0);
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(new ArrayList<>());

            ContractConsumptionDto result = billingCommonMethods.taxCalculation(
                    1000.0, 0.0, 0.0, 7, resDto, 900.0);

            // Neither the "applicable" nor the "not applicable" branch ever executes -- whatever the
            // caller had already set stays exactly as-is, since `dto = resDto` is an alias, not a copy.
            assertThat(result.getTaxAmt()).isEqualTo(999.0);
            assertThat(result.getTaxPercentage()).isEqualTo(77.0);
        }

        @Test
        @DisplayName("FINDING (logged, not fixed): with multiple GST bracket rows, the LAST row in the list always wins, even overwriting an earlier row that correctly applied")
        void lastBracketRowWinsUnconditionally() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            // Row 1 applies and would set a real 18% tax. Row 2 does NOT apply (range not met) and
            // resets everything to zero -- the loop has no "first match wins" or "break" logic, so
            // whichever row is LAST in the list decides the outcome, regardless of which one(s)
            // actually matched. This looks like a real defect: if a unit's GST bracket configuration
            // ever has more than one row, the tax computed depends on list order, not on which
            // bracket the current amount genuinely falls into.
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(Arrays.asList(
                    gstBracket(true, 500, 18),
                    gstBracket(true, 50000, 12))); // range not met by serviceReteAfterCalculaion=900.0

            ContractConsumptionDto result = billingCommonMethods.taxCalculation(
                    1000.0, 0.0, 0.0, 7, resDto, 900.0);

            // Row 1 alone would have produced taxAmt=180.0 (18% of 1000.0). Row 2 overwrites it to 0.
            assertThat(result.getTaxPercentage()).isEqualTo(0.00);
            assertThat(result.getTaxAmt()).isEqualTo(0.00);
        }

        @Test
        @DisplayName("FINDING (logged, not fixed): a null gstAmountRange on any bracket row throws a NullPointerException via auto-unboxing in the >= comparison")
        void nullAmountRangeThrowsNpe() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(
                    Arrays.asList(gstBracket(true, /* gstAmountRange */ null, 18)));

            org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                    billingCommonMethods.taxCalculation(1000.0, 0.0, 0.0, 7, resDto, 900.0));
        }
    }

    @Nested
    @DisplayName("BillingCommonMethods.calculateSelfCreditAndFinalAmount -- characterization (current behavior, not verified-correct behavior)")
    class SelfCreditAndFinalAmountCalculation {
        // SCOPE, disclosed: this method has 4 structurally distinct branches (GST vs non-GST tax
        // source, and within checkConsultaionService=true, tariff-applicable vs not, which itself has
        // 2 further sub-branches on rate-vs-contractRate). This batch covers the non-GST path, the GST
        // path, the checkConsultaionService=false path, the tariff='Y' path, and ONE of the two
        // rate-vs-contractRate sub-branches (rate >= contractRate). NOT YET covered: the
        // rate < contractRate sub-branch's own two further sub-cases (creditPayable >= rate or not).

        @Test
        @DisplayName("no concession, no discount, non-GST tax source, checkConsultaionService=false: self/credit split by coShare percent of the tax-inclusive final price")
        void simplestPath_noConcessionNoDiscountNonGst() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setOrdDiscountPer(0.0);
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setTaxPercentage(10.0);
            resDto.setCoShare(20.0);

            ContractConsumptionDto result = billingCommonMethods.calculateSelfCreditAndFinalAmount(
                    consumptionDto, resDto, /* qty */ 1.0, /* gstApplicableForBillingBedCategory */ false,
                    /* concessionPer */ 0.0, /* serviceAmount1 */ 1000.0, /* checkConsultaionService */ false);

            // taxAmt = (1000.0 - 0) * 0.10 = 100.0; finalPrice = 1000.0 + 100.0 = 1100.0
            assertThat(result.getTaxAmt()).isEqualTo(100.0);
            assertThat(result.getFinalPrice()).isEqualTo(1100.0);
            assertThat(result.getServiceRate()).isEqualTo(1000.0);
            // selfPayable = 1100.0 * 0.20 = 220.0; creaditPayable = 1100.0 - 220.0 = 880.0
            assertThat(result.getSelfPayable()).isEqualTo(220.0);
            assertThat(result.getCreaditPayable()).isEqualTo(880.0);
            assertThat(result.getOrdDiscount()).isEqualTo(0.0);
            assertThat(result.getConcession()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("concession and discount both apply, no tax configured: both amounts net out of the final price, taxAmt stays zero")
        void concessionAndDiscountBothApply_noTaxConfigured() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setOrdDiscountPer(5.0);
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            // resDto.getTaxPercentage() left null -- non-GST branch treats that as "no tax at all".

            ContractConsumptionDto result = billingCommonMethods.calculateSelfCreditAndFinalAmount(
                    consumptionDto, resDto, /* qty */ 2.0, false,
                    /* concessionPer */ 10.0, /* serviceAmount1 */ 1000.0, false);

            // concession = (1000.0*2.0) * 0.10 = 200.0
            // discountAmt = ((2000.0) - 200.0) * 0.05 = 1800.0 * 0.05 = 90.0
            assertThat(result.getConcession()).isEqualTo(200.0);
            assertThat(result.getOrdDiscount()).isEqualTo(90.0);
            assertThat(result.getTaxAmt()).isEqualTo(0.00);
            // finalPrice = (2000.0 - |200.0+90.0|) + 0.00 = 1710.0
            assertThat(result.getFinalPrice()).isEqualTo(1710.0);
            // no coShare set -> selfPayable = 1710.0*0 = 0.0, creaditPayable = 1710.0
            assertThat(result.getSelfPayable()).isEqualTo(0.0);
            assertThat(result.getCreaditPayable()).isEqualTo(1710.0);
        }

        @Test
        @DisplayName("GST-applicable bed service routes tax through taxCalculation instead of resDto's own taxPercentage")
        void gstApplicableBedService_routesThroughTaxCalculation() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setOrdDiscountPer(0.0);
            consumptionDto.setIsBedService('Y');
            consumptionDto.setBillingBedCategoryId(7);
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setCoShare(20.0);
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(
                    Arrays.asList(gstBracket(true, 500, 18)));

            ContractConsumptionDto result = billingCommonMethods.calculateSelfCreditAndFinalAmount(
                    consumptionDto, resDto, 1.0, /* gstApplicableForBillingBedCategory */ true,
                    0.0, 1000.0, false);

            // serviceReteAfterCalculaion via CommonGSTCalculation with no concession/discount = 1000.0,
            // which meets the bracket's 500 range -> taxAmt = 1000.0 * 0.18 = 180.0.
            assertThat(result.getTaxAmt()).isEqualTo(180.0);
            assertThat(result.getFinalPrice()).isEqualTo(1180.0);
            assertThat(result.getSelfPayable()).isEqualTo(236.0); // 1180.0 * 0.20
            assertThat(result.getCreaditPayable()).isEqualTo(944.0);
        }

        @Test
        @DisplayName("checkConsultaionService=true, isTariffRateApplicable='Y': uses the exact same selfPayable/creaditPayable formula as the non-consultation branch")
        void consultationService_tariffApplicable_sameFormulaAsNonConsultationBranch() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setOrdDiscountPer(0.0);
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setTaxPercentage(10.0);
            resDto.setCoShare(20.0);
            resDto.setIsTariffRateApplicable('Y');

            ContractConsumptionDto result = billingCommonMethods.calculateSelfCreditAndFinalAmount(
                    consumptionDto, resDto, 1.0, false, 0.0, 1000.0, /* checkConsultaionService */ true);

            // Identical inputs/outputs to simplestPath_noConcessionNoDiscountNonGst above -- the
            // tariff='Y' branch and the checkConsultaionService=false branch compute selfPayable/
            // creaditPayable via the exact same formula (finalPrice * coShare%, and the remainder).
            assertThat(result.getFinalPrice()).isEqualTo(1100.0);
            assertThat(result.getSelfPayable()).isEqualTo(220.0);
            assertThat(result.getCreaditPayable()).isEqualTo(880.0);
        }

        @Test
        @DisplayName("checkConsultaionService=true, isTariffRateApplicable!='Y', rate >= contractRate: self absorbs the excess over the pre-existing contract price, creaditPayable is left COMPLETELY UNCHANGED from whatever the caller set it to")
        void consultationService_tariffNotApplicable_rateAboveContractRate() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setOrdDiscountPer(0.0);
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setTaxPercentage(10.0);
            resDto.setIsTariffRateApplicable('N');
            resDto.setFinalPrice(900.0); // the pre-existing "contract rate" baseline, captured BEFORE
                                          // this call overwrites finalPrice with the freshly computed one
            resDto.setSelfPayable(50.0); // pre-existing selfPayable this branch adds onto
            resDto.setCreaditPayable(333.0); // arbitrary sentinel -- see the assertion below

            ContractConsumptionDto result = billingCommonMethods.calculateSelfCreditAndFinalAmount(
                    consumptionDto, resDto, 1.0, false, 0.0, 1000.0, true);

            // Freshly computed finalPrice = (1000.0-0)+100.0(10% tax) = 1100.0 -- this becomes `rate`.
            // contractRate was captured as 900.0 (the pre-call finalPrice) before being overwritten.
            // rate(1100.0) >= contractRate(900.0) -> selfPayable = 50.0 + (1100.0-900.0) = 250.0.
            assertThat(result.getFinalPrice()).isEqualTo(1100.0);
            assertThat(result.getSelfPayable()).isEqualTo(250.0);
            // FINDING (logged, not fixed): in this specific sub-branch, creaditPayable is never
            // reassigned at all -- it stays at whatever arbitrary value (333.0) the caller happened to
            // leave on resDto going in, completely disconnected from the freshly computed finalPrice.
            assertThat(result.getCreaditPayable()).isEqualTo(333.0);
        }
    }

    @Nested
    @DisplayName("BillingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract -- characterization (current behavior, not verified-correct behavior)")
    class FirstPriorityContractAdjustment {

        @Test
        @DisplayName("no co-share, no pre-auth: creditPayable passes through unmodified to updateCapAmount")
        void simplestPath_noAdjustments() {
            ContractConsumptionDto consumptionDto = billContext(false);
            ContractConsumptionDto resDto = secondRoundContract(null, 1000.0);

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    consumptionDto, resDto, /* creditPayable */ 500.0, /* selfPayable */ 200.0,
                    /* preAuthAmount */ null, null, /* fromContact */ false, /* checkFlag */ true);

            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 500.0);
            assertThat(result.getCreaditPayable()).isEqualTo(500.0);
            assertThat(result.getSelfPayable()).isEqualTo(200.0);
        }

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_FirstPriorityContractMissingUpdateCapGuard.md, batch 1 finding 4, owner-approved): the checkFlag=true branch now skips updateCapAmount when consumptionDto.updateCap is already non-null, matching its checkFlag=false branch and the sibling second-round method")
        void checkFlagTrueBranch_nowRespectsTheUpdateCapGuard() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setUpdateCap('Y'); // non-null -- every other guarded branch in this file
                                               // skips all writes when this is set
            ContractConsumptionDto resDto = secondRoundContract(null, 1000.0);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    consumptionDto, resDto, 500.0, 200.0, null, null, false, /* checkFlag */ true);

            // Previously the checkFlag=true branch (lines ~296-302) only checked `creditPayable > 0`
            // -- no `consumptionDto.getUpdateCap() == null` guard at all, unlike its own checkFlag=
            // false branch (see the next test) and unlike updatePreAuthAndServiceCapAmountForSecond
            // Contract's equivalent checkFlag=true branch, which already checked updateCap==null. Now
            // this branch has the same guard added, so a second/repeat call with updateCap already
            // set is silently skipped instead of double-writing the contract's cap balance.
            verify(contractConsumptionDao, never()).updateCapAmount(any(), any());
        }

        @Test
        @DisplayName("contrast case: the checkFlag=false branch DOES respect the updateCap guard -- no write when updateCap is already non-null")
        void checkFlagFalseBranch_respectsTheUpdateCapGuard() {
            ContractConsumptionDto consumptionDto = billContext(false);
            consumptionDto.setUpdateCap('Y');
            ContractConsumptionDto resDto = secondRoundContract(null, 1000.0);

            billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    consumptionDto, resDto, 500.0, 200.0, null, null, false, /* checkFlag */ false);

            verify(contractConsumptionDao, never()).updateServiceCapAmount(any(), any(), any());
        }

        @Test
        @DisplayName("co-share on fromContact=true shifts coShare percent of creditPayable onto selfPayable before the database write")
        void coShareAdjustment_fromContactTrue() {
            ContractConsumptionDto consumptionDto = billContext(false);
            ContractConsumptionDto resDto = secondRoundContract(/* coShare */ 20.0, 1000.0);

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    consumptionDto, resDto, 500.0, 100.0, null, null, /* fromContact */ true, true);

            // 20% of 500.0 = 100.0 shifts from credit to self.
            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 400.0);
            assertThat(result.getSelfPayable()).isEqualTo(200.0);
            assertThat(result.getCreaditPayable()).isEqualTo(400.0);
        }

        @Test
        @DisplayName("pre-authorization ceiling caps creditPayable and records the pre-auth consumption, same shape as the second-round sibling")
        void preAuthCeilingExceeded() {
            ContractConsumptionDto consumptionDto = billContext(/* applyAuth */ true);
            ContractConsumptionDto resDto = secondRoundContract(null, 1000.0);

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    consumptionDto, resDto, /* creditPayable */ 500.0, /* selfPayable */ 100.0,
                    /* preAuthAmount */ 300.0, null, false, true);

            verify(contractConsumptionDao).updatePreAuthAmount(4242, 300.0);
            verify(contractConsumptionDao).updateCapAmount(consumptionDto, 300.0);
            assertThat(result.getSelfPayable()).isEqualTo(300.0); // 100.0 + (500.0 - 300.0)
            assertThat(result.getCreaditPayable()).isEqualTo(300.0);
        }
    }

    // =========================================================================================
    // SHADOW-TEST HARNESS EXPANSION (Step 2), batch 2 -- updatePreAuthForPharmacyContract,
    // createOrderDetailsPackageMapperDtoAndUpdatePackageCap,
    // createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap,
    // createOrderDetailsPackageMapperForMultiEncounter. Same rigor and process as wave 1/batch 1:
    // characterize current behavior, log findings, fix nothing.
    // =========================================================================================

    @Nested
    @DisplayName("BillingCommonMethods.updatePreAuthForPharmacyContract -- characterization (current behavior, not verified-correct behavior)")
    class PreAuthForPharmacyContract {

        @Test
        @DisplayName("no coShare, no preauthId: credit/self pass through completely unmodified")
        void noAdjustments_passThrough() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            PharmacyBillCommonDto dto = new PharmacyBillCommonDto();

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthForPharmacyContract(
                    resDto, /* creditPayable */ 500.0, /* selfPayable */ 100.0, dto, new java.util.HashMap<>());

            assertThat(result.getCreaditPayable()).isEqualTo(500.0);
            assertThat(result.getSelfPayable()).isEqualTo(100.0);
            verifyNoInteractions(contractConsumptionDao);
        }

        @Test
        @DisplayName("URGENT FINDING (logged, not fixed): applying coShare loses money from the combined total -- selfPayable is credited off the ALREADY-REDUCED creditPayable, not the original amount")
        void coShareAdjustment_moneyConservationBug() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setCoShare(20.0); // 20%
            PharmacyBillCommonDto dto = new PharmacyBillCommonDto();

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthForPharmacyContract(
                    resDto, /* creditPayable */ 1000.0, /* selfPayable */ 0.0, dto, new java.util.HashMap<>());

            // Line 1 reassigns creditPayable FIRST: 1000.0 - (1000.0 * 0.20) = 800.0.
            // Line 2 then computes the selfPayable addition off that ALREADY-REDUCED 800.0, not the
            // original 1000.0: 0.0 + (800.0 * 0.20) = 160.0 -- not the 200.0 that was actually removed
            // from creditPayable. $200 left creditPayable; only $160 arrived at selfPayable.
            assertThat(result.getCreaditPayable()).isEqualTo(800.0);
            assertThat(result.getSelfPayable()).isEqualTo(160.0);
            // The combined total shrank from 1000.0 (1000.0 credit + 0.0 self) to 960.0 (800.0 + 160.0)
            // -- $40 has simply disappeared from this calculation. Not a values-disagree-elsewhere gap
            // like BR-PENDING-copay-preauth-split-return-value-gap (Finding 1) -- the total itself is
            // wrong, in a live pharmacy-billing pre-authorization calculation.
            assertThat(result.getCreaditPayable() + result.getSelfPayable()).isEqualTo(960.0);
        }

        @Test
        @DisplayName("pre-auth ceiling exceeded, dto.getConsume()=true: caps creditPayable and writes the pre-auth consumption to the database")
        void preAuthCeilingExceeded_consumeTrue() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setPreauthId(99);
            PharmacyBillCommonDto dto = new PharmacyBillCommonDto();
            dto.setConsume(true);
            java.util.Map<Integer, Double> preAuthDetails = new java.util.HashMap<>();
            preAuthDetails.put(99, 300.0);

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthForPharmacyContract(
                    resDto, /* creditPayable */ 500.0, /* selfPayable */ 100.0, dto, preAuthDetails);

            verify(contractConsumptionDao).updatePreAuthAmount(99, 300.0);
            assertThat(result.getCreaditPayable()).isEqualTo(300.0);
            assertThat(result.getSelfPayable()).isEqualTo(300.0); // 100.0 + (500.0 - 300.0)
        }

        @Test
        @DisplayName("pre-auth ceiling exceeded, dto.getConsume()=false: the adjustment still happens, but the database write is skipped")
        void preAuthCeilingExceeded_consumeFalse() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setPreauthId(99);
            PharmacyBillCommonDto dto = new PharmacyBillCommonDto();
            dto.setConsume(false);
            java.util.Map<Integer, Double> preAuthDetails = new java.util.HashMap<>();
            preAuthDetails.put(99, 300.0);

            ContractConsumptionDto result = billingCommonMethods.updatePreAuthForPharmacyContract(
                    resDto, 500.0, 100.0, dto, preAuthDetails);

            verify(contractConsumptionDao, never()).updatePreAuthAmount(any(), any());
            // The cap-adjustment arithmetic runs regardless of getConsume() -- only the DB write is gated.
            assertThat(result.getCreaditPayable()).isEqualTo(300.0);
            assertThat(result.getSelfPayable()).isEqualTo(300.0);
        }

        @Test
        @DisplayName("FINDING (logged, not fixed): a preauthId not present in preAuthDetails throws a NullPointerException via auto-unboxing")
        void preauthIdMissingFromMap_throwsNpe() {
            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setPreauthId(77);
            PharmacyBillCommonDto dto = new PharmacyBillCommonDto();

            org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                    billingCommonMethods.updatePreAuthForPharmacyContract(
                            resDto, 500.0, 100.0, dto, new java.util.HashMap<>()));
        }
    }

    @Nested
    @DisplayName("BillingCommonMethods.createOrderDetailsPackageMapperDtoAndUpdatePackageCap -- characterization (current behavior, not verified-correct behavior)")
    class PackageCapMapper {

        @Test
        @DisplayName("builds the mapper dto, accumulates packageConsumptionAmt by serviceRate (not consumeAmt), and writes the negated consumeAmt to the cap DAO")
        void buildsMapperAndUpdatesCap() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            resDto.setPackageId(10);
            resDto.setPackageName("Basic Package");
            resDto.setPackageCode("PKG01");
            TPackageConsumptionCapDetailsDto capDtls = new TPackageConsumptionCapDetailsDto();
            capDtls.setPackageConsumptionMasterId(20);
            capDtls.setPackageConsumptionCapDetailsId(30);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setServiceRate(250.0);
            consumptionDto.setPackageConsumptionAmt(java.math.BigDecimal.valueOf(1000.0));

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperDtoAndUpdatePackageCap(
                    resDto, capDtls, consumptionDto, /* consumeAmt */ 150.0);

            assertThat(result.getPackageId()).isEqualTo(10);
            assertThat(result.getIsPackageService()).isEqualTo('Y');
            assertThat(result.getIsCapService()).isEqualTo('Y');
            assertThat(result.getPackageAmount()).isEqualByComparingTo(java.math.BigDecimal.valueOf(150.0));
            assertThat(result.getServiceAmount()).isEqualByComparingTo(java.math.BigDecimal.valueOf(250.0));
            // The running accumulator is incremented by serviceRate (250.0), NOT consumeAmt (150.0) --
            // worth noting precisely, since the sibling method below accumulates by consumeAmt instead.
            assertThat(consumptionDto.getPackageConsumptionAmt()).isEqualByComparingTo(java.math.BigDecimal.valueOf(1250.0));
            verify(tPackageConsumptionCapDetailsDao).updatePackageCap(30, -150.0);
        }
    }

    @Nested
    @DisplayName("BillingCommonMethods.createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap -- characterization (current behavior, not verified-correct behavior)")
    class PackageServiceCapMapper {

        @Test
        @DisplayName("isUpdatePackageService=true: writes to the service-cap DAO and stamps the service-detail/master ids onto the mapper dto")
        void updatesServiceCap_whenFlagTrue() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            resDto.setPackageId(10);
            resDto.setPackageName("X");
            resDto.setPackageCode("Y");
            PackageConsumptionServiceDetailDto srv = new PackageConsumptionServiceDetailDto();
            srv.setPackageConsumptionServiceDetailId(40);
            srv.setPackageConsumptionMasterId(50);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setServiceRate(300.0);
            consumptionDto.setPackageConsumptionAmt(java.math.BigDecimal.ZERO);

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(
                    resDto, srv, consumptionDto, /* consumeAmt */ 200.0, /* isCapService */ 'N', /* isUpdatePackageService */ true);

            // Here the accumulator is incremented by consumeAmt (200.0) -- NOT serviceRate, unlike the
            // sibling createOrderDetailsPackageMapperDtoAndUpdatePackageCap above, which uses serviceRate.
            assertThat(consumptionDto.getPackageConsumptionAmt()).isEqualByComparingTo(java.math.BigDecimal.valueOf(200.0));
            verify(tPackageConsumptionCapDetailsDao).updatePackageServiceCap(40, -200.0);
            assertThat(result.getPacakgeConsumptionServiceDetailId()).isEqualTo(40);
            assertThat(result.getPackageConsumptionMasterId()).isEqualTo(50);
        }

        @Test
        @DisplayName("isUpdatePackageService=false: the DAO write and the two id fields are skipped, but packageConsumptionAmt still accumulates")
        void skipsServiceCapUpdate_whenFlagFalse() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            PackageConsumptionServiceDetailDto srv = new PackageConsumptionServiceDetailDto();
            srv.setPackageConsumptionServiceDetailId(40);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setServiceRate(300.0);
            consumptionDto.setPackageConsumptionAmt(java.math.BigDecimal.ZERO);

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(
                    resDto, srv, consumptionDto, 200.0, 'N', /* isUpdatePackageService */ false);

            verify(tPackageConsumptionCapDetailsDao, never()).updatePackageServiceCap(any(), any());
            assertThat(result.getPacakgeConsumptionServiceDetailId()).isNull();
            assertThat(consumptionDto.getPackageConsumptionAmt()).isEqualByComparingTo(java.math.BigDecimal.valueOf(200.0));
        }
    }

    @Nested
    @DisplayName("BillingCommonMethods.createOrderDetailsPackageMapperForMultiEncounter -- characterization (current behavior, not verified-correct behavior)")
    class MultiEncounterPackageMapper {

        @Test
        @DisplayName("consumeService != 0: the entire quantity/pricing block is skipped, only the common fields are set")
        void skipsEntireBlock_whenAlreadyConsumed() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            resDto.setPackageId(10);
            PackageConsumptionServiceDetailDto srv = new PackageConsumptionServiceDetailDto();
            srv.setPackageConsumptionServiceDetailId(40);
            srv.setPackageConsumptionMasterId(50);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setConsumeService(1); // non-zero -- "already consumed"

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperForMultiEncounter(
                    resDto, srv, /* availableQty */ 10, consumptionDto, /* serviceQty */ 5, /* setPkgAmt */ false);

            assertThat(result.getPackageId()).isEqualTo(10);
            assertThat(result.getPacakgeConsumptionServiceDetailId()).isEqualTo(40);
            assertThat(result.getPackageQty()).isNull(); // never touched -- the whole inner block was skipped
            verifyNoInteractions(tPackageConsumptionCapDetailsDao);
        }

        @Test
        @DisplayName("consumeService==0, availableQty >= serviceQty: full quantity satisfied, rate comes from the package's availableCap, setPkgAmt's initial packageAmount gets silently overwritten")
        void fullQuantitySatisfied_setPkgAmtOverwritten() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            PackageConsumptionServiceDetailDto srv = new PackageConsumptionServiceDetailDto();
            srv.setPackageConsumptionServiceDetailId(40);
            srv.setAvailableCap(50.0);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setConsumeService(0);
            consumptionDto.setSelfPayable(999.0); // sentinel -- see the finding below

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperForMultiEncounter(
                    resDto, srv, /* availableQty */ 10, consumptionDto, /* serviceQty */ 5, /* setPkgAmt */ true);

            // FINDING (logged, not fixed): setPkgAmt=true initially sets packageAmount from
            // consumptionDto.getSelfPayable() (999.0), but this specific branch (availableQty >=
            // serviceQty) unconditionally OVERWRITES packageAmount a few lines later with the freshly
            // computed ordTotalAmount -- setPkgAmt's effect is silently discarded whenever this branch
            // runs, regardless of what the caller passed.
            assertThat(result.getPackageAmount()).isEqualByComparingTo(java.math.BigDecimal.valueOf(250.0)); // NOT 999.0
            assertThat(result.getPackageQty()).isEqualTo(5);
            assertThat(consumptionDto.getServiceRate()).isEqualTo(50.0); // from srv.getAvailableCap()
            assertThat(consumptionDto.getOrdTotalAmount()).isEqualTo(250.0); // 50.0 * 5
            assertThat(consumptionDto.getSelfPayable()).isEqualTo(0.0); // reset unconditionally
            assertThat(srv.getBalanceQuantity()).isEqualTo(5); // 10 - 5
            verify(tPackageConsumptionCapDetailsDao).updatePackageQty(40, -5);
        }

        @Test
        @DisplayName("FINDING (logged, not fixed): consumeService==0, availableQty < serviceQty (partial fulfillment): packageQty and the DAO deduction are hardcoded to 1, not the actual remaining quantity")
        void partialQuantity_hardcodedToOne() {
            PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();
            PackageConsumptionServiceDetailDto srv = new PackageConsumptionServiceDetailDto();
            srv.setPackageConsumptionServiceDetailId(40);
            srv.setAvailableCap(40.0);
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setConsumeService(0);
            consumptionDto.setServiceRate(80.0); // used as the per-unit rate in THIS branch, unlike the
                                                  // if-branch above, which instead takes the rate from
                                                  // srv.getAvailableCap() -- a real asymmetry between
                                                  // the two branches' rate source.

            OrderDetailsPackageMapperDto result = billingCommonMethods.createOrderDetailsPackageMapperForMultiEncounter(
                    resDto, srv, /* availableQty */ 3, consumptionDto, /* serviceQty */ 5, false);

            // Remaining quantity actually needed after exhausting availableQty=3 of the requested 5 is
            // 2 -- but packageQty is hardcoded to 1, and the DAO deduction is hardcoded to -1, not -2.
            // srv.getBalanceQuantity() is likewise hardcoded to 0, not derived from any calculation.
            assertThat(result.getPackageQty()).isEqualTo(1);
            verify(tPackageConsumptionCapDetailsDao).updatePackageQty(40, -1);
            assertThat(srv.getBalanceQuantity()).isEqualTo(0);
            // packageConsumptionAmt = availableQty(3) * srv.getAvailableCap()(40.0) = 120.0
            assertThat(consumptionDto.getPackageConsumptionAmt()).isEqualByComparingTo(java.math.BigDecimal.valueOf(120.0));
            // ordTotalAmount = serviceRate(80.0, carried over unchanged) * the NEW serviceQty (5-3=2) = 160.0
            assertThat(consumptionDto.getOrdTotalAmount()).isEqualTo(160.0);
            // FINDING: netAmount here is a real subtraction (ordTotalAmount - packageConsumptionAmt =
            // 160.0 - 120.0 = 40.0) -- a DIFFERENT formula from the if-branch above, which sets
            // netAmount = consumptionDto.getFinalPrice() (an unrelated carried-over value, no
            // subtraction at all). The two branches derive netAmount inconsistently.
            assertThat(consumptionDto.getNetAmount()).isEqualTo(40.0);
            assertThat(consumptionDto.getSelfPayable()).isEqualTo(40.0); // set equal to netAmount here
        }
    }
}
