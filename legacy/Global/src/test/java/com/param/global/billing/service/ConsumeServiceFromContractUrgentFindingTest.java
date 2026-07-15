package com.param.global.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
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
import com.param.global.common.Response;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;

/**
 * URGENT FINDING -- flagged live to the owner per standing instruction, not held for a batch
 * summary. Isolated confirming test for one specific branch of {@code consumeServiceFromContract}
 * (Global/.../billing/service/ContractConsumptionServiceImpl.java, lines 740-745): when a
 * first-priority, amount-capped (type==2) contract's remaining balance is exceeded by the
 * service's net amount, {@code selfPayable} and {@code creaditPayable} are computed with the
 * cap-amount and the overage SWAPPED relative to the sibling type==1 cap branch 150 lines above
 * it in the very same method (lines 590-593), which is the sibling's demonstrably-intended,
 * consistent behavior: contract absorbs up to the cap, patient absorbs the overage. Here it is the
 * opposite: the patient is billed the CAP amount and the contract is billed only the OVERAGE.
 *
 * This test does not exercise the whole method's full downstream response construction (out of
 * scope for a quick confirming test) -- it captures the arguments passed into
 * billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(...), which is
 * where the already-swapped creaditPayable/selfPayable values are read off. The swap has already
 * happened by the time this call is made -- nothing this mocked method might do internally can
 * fix it, so mocking it out does not hide the bug.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("URGENT: consumeServiceFromContract swaps self/credit payable when a type==2 amount-capped contract's balance is exceeded")
class ConsumeServiceFromContractUrgentFindingTest {

    @Mock private IContractConsumptionDao contractConsumptionDao;
    @Mock private IEncounterMasterDao encounterMasterDao;
    @Mock private IEncounterMasterService encounterMasterService;
    @Mock private BillingCommonMethods billingCommonMethods;

    private ContractConsumptionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContractConsumptionServiceImpl();
        service.iContractConsumptionDao = contractConsumptionDao;
        service.iEncounterMasterDao = encounterMasterDao;
        service.iEncounterMasterService = encounterMasterService;
        service.billingCommonMethods = billingCommonMethods;
    }

    @Test
    @DisplayName("netAmt=500, remaining balance cap=300 -> contract billed 200 (overage), patient billed 300 (the cap) -- backwards")
    void balanceCapExceeded_selfAndCreditPayableAreSwapped() {
        ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
        consumptionDto.setContractId(1);
        consumptionDto.setVisitTypeId(1); // OP
        consumptionDto.setServiceId(500);
        consumptionDto.setServiceRate(500.0);
        consumptionDto.setQuantity(null);
        consumptionDto.setOrdDiscountPer(0.0);
        consumptionDto.setChangedPrice(null);
        consumptionDto.setServiceChargeStatus(null);
        consumptionDto.setPatientClassPercentage(null);
        consumptionDto.setProcedureId(null);
        consumptionDto.setIsBOM(null);
        consumptionDto.setIsPeriodicityService(null);
        consumptionDto.setIsConsultationService(null);
        consumptionDto.setCurrentDate("2026-07-15 10:00:00");
        consumptionDto.setUnitId(1);
        consumptionDto.setBillingBedCategoryId(1);

        ContractConsumptionDto resDto = new ContractConsumptionDto();
        resDto.setType(2);
        resDto.setPriorityId(1);
        resDto.setTillDischarge(true);
        resDto.setApplicableDate(null);
        resDto.setBalanceAmount(300.0); // remaining cap
        resDto.setBalanceQuantity(null);
        resDto.setBalanceCapAmount(null);
        resDto.setTaxAmt(0.0);
        resDto.setTaxPercentage(null);
        resDto.setIsSchemeDiscount('N'); // takes the patient-class-lookup else-if without needing isDiscount
        resDto.setPreAuthBalAmt(0.0);
        resDto.setCoShare(null);

        List<ContractConsumptionDto> serviceDetailslist = Arrays.asList(resDto);

        when(encounterMasterDao.getIsBedChargesService(any())).thenReturn('N');
        when(contractConsumptionDao.getGstApplicableForBillingBedCategory(anyInt())).thenReturn(false);
        when(contractConsumptionDao.getServiceDetailsByContractId(any())).thenReturn(serviceDetailslist);
        lenient().when(contractConsumptionDao.getPatientClassAndPercentageByVisit(any())).thenReturn(null);

        ContractConsumptionDto passthrough = new ContractConsumptionDto();
        passthrough.setCreaditPayable(200.0);
        passthrough.setSelfPayable(300.0);
        when(billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                any(), any(), any(), any(), any(), eq(null), eq(true), eq(false)))
                .thenReturn(passthrough);
        when(encounterMasterService.getBasePriceByServiceTariffMaster(any(UnitServiceTariffMasterDto.class)))
                .thenReturn(new UnitServiceTariffMasterDto());

        service.consumeServiceFromContract(consumptionDto);

        // Confirms the FINDING: the amount routed to the contract/insurer (creditPayable arg,
        // position 3) is the $200 overage, and the amount routed to the patient (selfPayable arg,
        // position 4) is the $300 cap -- the opposite of the sibling type==1 branch's semantics
        // (contract pays up to the cap, patient pays the overage), which would be creditPayable=
        // $300, selfPayable=$200 for these same inputs.
        org.mockito.Mockito.verify(billingCommonMethods).updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                eq(consumptionDto), eq(resDto), eq(200.0), eq(300.0), any(), eq(null), eq(true), eq(false));
    }
}
