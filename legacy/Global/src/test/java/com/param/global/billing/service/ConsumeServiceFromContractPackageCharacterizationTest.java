package com.param.global.billing.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.common.BillingCommonMethods;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.opd.dao.IEncounterMasterDao;

/**
 * SHADOW-TEST HARNESS EXPANSION (Step 2 resumed) -- {@code consumeServiceFromContractPackage}
 * (~716 lines), targeted approach per owner instruction: characterize only branches that call
 * into logic already proven buggy/risky elsewhere this session. First test in this file exists
 * specifically to pin the fix for a repeat of the second-round quantity-cap credit-loss bug
 * (found in this method's own sibling branch to the one already fixed in
 * {@code consumeServiceFromContract}) -- see docs/BUSINESS_RULE_MANIFEST.md.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContractConsumptionServiceImpl.consumeServiceFromContractPackage -- targeted characterization (current behavior, not verified-correct behavior)")
class ConsumeServiceFromContractPackageCharacterizationTest {

    @Mock private IContractConsumptionDao contractConsumptionDao;
    @Mock private IEncounterMasterDao encounterMasterDao;
    @Mock private BillingCommonMethods billingCommonMethods;

    private ContractConsumptionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContractConsumptionServiceImpl();
        service.iContractConsumptionDao = contractConsumptionDao;
        service.iEncounterMasterDao = encounterMasterDao;
        service.billingCommonMethods = billingCommonMethods;
    }

    @Nested
    @DisplayName("FIXED: second-round loop, secondRound.getType()==2 with a quantity-tracked balance cap -- round-1 contribution now survives")
    class SecondRoundTypeTwoQuantityCap {

        @Test
        @DisplayName("round-1 contributes $120 credit (60% co-share on $300); second contract's quantity-cap branch now accumulates its own $300 onto it -> $420 total")
        void quantityCapBranch_accumulatesOntoRunningTotal() {
            ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
            consumptionDto.setVisitTypeId(1); // OP
            consumptionDto.setContractId(1);
            consumptionDto.setSelfPayable(300.0);
            consumptionDto.setQuantity(1.0);
            consumptionDto.setOrdDiscountPer(0.0);
            consumptionDto.setPatientClassPercentage(null);
            consumptionDto.setCurrentDate("2026-07-16 10:00:00");
            consumptionDto.setUnitId(1);
            consumptionDto.setBillingBedCategoryId(1);
            consumptionDto.setAdmissionId(null);
            consumptionDto.setApplyAuth(false);
            consumptionDto.setServiceId(700);

            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setType(1);
            resDto.setPriorityId(1);
            resDto.setIsDiscount('N');
            resDto.setIsSchemeDiscount('N');
            resDto.setIsTariffRateApplicable('Y');
            resDto.setCoShare(60.0);
            resDto.setBalanceCapAmount(null);
            resDto.setPreAuthBalAmt(0.0);

            ContractConsumptionDto secondRound = new ContractConsumptionDto();
            secondRound.setType(2);
            secondRound.setPriorityId(2);
            secondRound.setTillDischarge(true);
            secondRound.setApplicableDate(null);
            secondRound.setBalanceAmount(null);
            secondRound.setBalanceQuantity(1.0);
            secondRound.setPreAuthBalAmt(0.0);
            secondRound.setIsSchemeDiscount('N');

            List<ContractConsumptionDto> serviceDetailslist = Arrays.asList(resDto, secondRound);

            when(encounterMasterDao.getIsBedChargesService(any())).thenReturn('N');
            when(contractConsumptionDao.getGstApplicableForBillingBedCategory(anyInt())).thenReturn(false);
            lenient().when(encounterMasterDao.getTaxPercentageByServiceId(any(), any())).thenReturn(null);
            lenient().when(contractConsumptionDao.getPatientClassAndPercentageByVisit(any())).thenReturn(null);

            ContractConsumptionDto roundTwoPassthrough = new ContractConsumptionDto();
            roundTwoPassthrough.setCreaditPayable(300.0);
            roundTwoPassthrough.setSelfPayable(0.0);
            when(billingCommonMethods.updatePreAuthAndServiceCapAmountForSecondContract(
                    eq(secondRound), any(), any(), any(), any(), any(), eq(consumptionDto), eq(true), eq(1.00), eq(false)))
                    .thenReturn(roundTwoPassthrough);

            service.consumeServiceFromContractPackage(consumptionDto, serviceDetailslist);

            // FIXED 2026-07-16: the quantity-tracked-cap branch (lines 2469-2483, this method's own
            // sibling of the branch already fixed in consumeServiceFromContract) now accumulates
            // onto the running totals instead of reassigning them. Round 1 (60% co-share on $300)
            // leaves totalCreditPayable=120, totalSelfPayable=180. secondRound's own
            // creaditPayableSecondRound = rate = consumptionDto.getSelfPayable() = 300.0. After the
            // fix: totalCreditPayable = 120+300 = 420 (was 300 before the fix -- round 1's $120
            // discarded). totalSelfPayable = 180-300 = -120 (a separate, already-logged, NOT-fixed
            // gap: this branch has no quantity-sufficiency check, same as its sibling).
            verify(billingCommonMethods).updatePreAuthAndServiceCapAmountForSecondContract(
                    eq(secondRound), eq(300.0), eq(-120.0), eq(420.0), any(), any(), eq(consumptionDto), eq(true), eq(1.00), eq(false));
        }
    }
}
