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
 * FIXED 2026-07-16: owner-approved regression test for the coShare money-conservation bug in
 * {@code consumeServiceFromContractPackage}'s round-1, {@code resDto.getType()==1},
 * {@code resDto.getBalanceCapAmount()!=null} branch (lines 2106-2107). Same defect shape as the
 * very first fix of this whole session (updatePreAuthForPharmacyContract's coShare adjustment --
 * see docs/DECISION_NEEDED_PharmacyContractCoShareMoneyLeak.md): was, the self-payable delta was
 * computed off an ALREADY-REASSIGNED credit-payable instead of the original pre-adjustment value,
 * so the amount removed from credit and the amount added to self didn't match -- money vanished
 * from the combined total. Fixed the same way as that original fix: swapped the order of the two
 * lines so selfPayable is computed first, from the still-original creaditPayable.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FIXED: consumeServiceFromContractPackage's cap-defined coShare split now conserves the total")
class ConsumeServiceFromContractPackageCoShareFixTest {

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
    @DisplayName("creaditPayable=1000, balanceCapAmount=800, coShare=20% -> creaditPayable=640, selfPayable=360 -- total conserved at 1000")
    void capDefinedCoShareSplit_conservesTotal() {
        ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
        consumptionDto.setVisitTypeId(1); // OP
        consumptionDto.setContractId(1);
        consumptionDto.setSelfPayable(1000.0);
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
        resDto.setIsTariffRateApplicable('N'); // takes the non-tariff-rate leg to reach the cap-defined branch below it
        resDto.setCreaditPayable(1000.0);
        resDto.setSelfPayable(0.0);
        resDto.setBalanceCapAmount(800.0);
        resDto.setCoShare(20.0);
        resDto.setPreAuthBalAmt(0.0);

        List<ContractConsumptionDto> serviceDetailslist = Arrays.asList(resDto);

        when(encounterMasterDao.getIsBedChargesService(any())).thenReturn('N');
        when(contractConsumptionDao.getGstApplicableForBillingBedCategory(anyInt())).thenReturn(false);
        lenient().when(encounterMasterDao.getTaxPercentageByServiceId(any(), any())).thenReturn(null);
        ContractConsumptionDto patientClassDto = new ContractConsumptionDto();
        lenient().when(contractConsumptionDao.getPatientClassAndPercentageByVisit(any())).thenReturn(patientClassDto);

        ContractConsumptionDto passthrough = new ContractConsumptionDto();
        passthrough.setCreaditPayable(640.0);
        passthrough.setSelfPayable(360.0);
        when(billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                any(), eq(resDto), any(), any(), any(), eq(null), eq(false), eq(true)))
                .thenReturn(passthrough);

        service.consumeServiceFromContractPackage(consumptionDto, serviceDetailslist);

        // Confirms the FIX: line 2103 sets selfPayable=0+(1000-800)=200, line 2104 sets
        // creaditPayable=800. selfPayable is now computed FIRST (line 2106, post-fix), using the
        // still-original creaditPayable=800: 200+(800*0.20)=200+160=360. creaditPayable is THEN
        // reassigned (line 2107, post-fix): 800-(800*0.20)=640. Total: 640+360=1000, conserved
        // (was 640+328=968 before the fix -- $32 vanished).
        verify(billingCommonMethods).updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                eq(consumptionDto), eq(resDto), eq(640.0), eq(360.0), any(), eq(null), eq(false), eq(true));
    }
}
