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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.common.BillingCommonMethods;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.opd.dao.IEncounterMasterDao;

/**
 * URGENT FINDING -- flagged live to the owner per standing instruction, not held for a batch
 * summary. Found while building the fix-confirming test for the second-round quantity-cap
 * credit-loss repeat in this same method. Isolated confirming test for
 * {@code consumeServiceFromContractPackage}'s round-1, type==2, amount-tracked-balance-cap
 * branch (lines 2204-2225): when the service amount exceeds the remaining balance cap,
 * BOTH {@code creaditPayable} and {@code selfPayable} are set to {@code resDto.getBalanceAmount()}
 * (the cap amount) -- the same value, twice -- instead of the patient absorbing the overage
 * ({@code rate - resDto.getBalanceAmount()}). The DEAD, commented-out code three lines below the
 * live ternaries shows what was clearly intended: {@code selfPayable = rate - resDto.getBalanceAmount()}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("URGENT: consumeServiceFromContractPackage's amount-cap branch duplicates the cap-amount formula into both creaditPayable and selfPayable")
class ConsumeServiceFromContractPackageAmountCapDuplicateFormulaTest {

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

    @Test
    @DisplayName("rate=$500 (consumptionDto.selfPayable), remaining balance cap=$300 -> creaditPayable=$300 (correct) AND selfPayable=$300 (should be $200, the overage)")
    void balanceCapExceeded_selfPayableDuplicatesTheCapAmountInsteadOfTheOverage() {
        ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
        consumptionDto.setVisitTypeId(1); // OP
        consumptionDto.setContractId(1);
        consumptionDto.setSelfPayable(500.0);
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
        resDto.setType(2);
        resDto.setPriorityId(1);
        resDto.setTillDischarge(true);
        resDto.setApplicableDate(null);
        resDto.setBalanceAmount(300.0);
        resDto.setBalanceQuantity(null);
        resDto.setIsTariffRateApplicable('Y');
        resDto.setPreAuthBalAmt(0.0);
        resDto.setIsSchemeDiscount('N');

        List<ContractConsumptionDto> serviceDetailslist = Arrays.asList(resDto);

        when(encounterMasterDao.getIsBedChargesService(any())).thenReturn('N');
        when(contractConsumptionDao.getGstApplicableForBillingBedCategory(anyInt())).thenReturn(false);
        lenient().when(encounterMasterDao.getTaxPercentageByServiceId(any(), any())).thenReturn(null);
        // NOTE: resDto.getIsTariffRateApplicable()=='Y' unconditionally calls
        // patientClassDto.getMarkupStatus() with NO null-check on patientClassDto itself (unlike
        // the sibling type==1 branch a few lines up, which does check `patientClassDto != null`
        // first) -- a real, separate NPE-crash-risk finding, noted but not the focus of this test.
        // Mocking a non-null return here so this test can isolate the amount-cap duplicate-formula
        // finding below.
        ContractConsumptionDto patientClassDto = new ContractConsumptionDto();
        lenient().when(contractConsumptionDao.getPatientClassAndPercentageByVisit(any())).thenReturn(patientClassDto);

        ContractConsumptionDto passthrough = new ContractConsumptionDto();
        passthrough.setCreaditPayable(300.0);
        passthrough.setSelfPayable(300.0);
        when(billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                any(), eq(resDto), any(), any(), any(), eq(null), eq(false), eq(false)))
                .thenReturn(passthrough);

        service.consumeServiceFromContractPackage(consumptionDto, serviceDetailslist);

        // Confirms the FINDING: creaditPayable correctly gets the cap amount ($300), but
        // selfPayable ALSO gets $300 (the cap amount again) instead of the overage ($500-$300=$200).
        // The combined total booked ($600) exceeds the actual service cost ($500) by $100 -- the
        // patient is overcharged by exactly the amount the dead code three lines below confirms
        // was intended to be the overage calculation, not a repeat of the cap amount.
        verify(billingCommonMethods).updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                eq(consumptionDto), eq(resDto), eq(300.0), eq(300.0), any(), eq(null), eq(false), eq(false));
    }
}
