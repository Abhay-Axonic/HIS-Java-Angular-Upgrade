package com.param.global.billing.service;

import static org.mockito.ArgumentMatchers.any;
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
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.opd.dao.IEncounterMasterDao;

/**
 * SHADOW-TEST HARNESS EXPANSION (Step 2 resumed) -- {@code consumePackageFromContract} (~445
 * lines), targeted approach per owner instruction. Structurally simpler than its two sibling giant
 * methods: it has ONLY {@code type==1} (flat contract-rate) logic in both round 1 and round 2 --
 * no {@code type==2}/days-or-quantity-cap branches at all, and no coShare adjustment on the
 * cap-defined split (unlike the near-identical branch in {@code consumeServiceFromContractPackage},
 * which does have a coShare step and was found to leak money there -- see
 * ConsumeServiceFromContractPackageCoShareFixTest).
 *
 * An agent-assisted sweep of this entire method (all ~445 lines) for both bug shapes found this
 * session (reassign-vs-accumulate running totals, and duplicate/stale-variable formula pairs)
 * found NO instances of either. Only 2 risky-method call sites exist in the whole method:
 * {@code updatePreAuthAndServiceCapAmountForFirstPriorityContract} (line ~2904) and
 * {@code updatePreAuthAndServiceCapAmountForSecondContract} (line ~3009), both in cap-defined
 * branches whose credit/self split ternaries were traced and confirmed correct (matching the
 * already-proven-correct pattern used elsewhere in this class).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContractConsumptionServiceImpl.consumePackageFromContract -- targeted characterization (current behavior, not verified-correct behavior)")
class ConsumePackageFromContractCharacterizationTest {

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
    @DisplayName("round-1, type==1, balanceCapAmount defined -- correct credit/self split (not a repeat of any found-and-fixed bug)")
    class Round1CapDefinedSplit {

        @Test
        @DisplayName("creaditPayable=1000, balanceCapAmount=800 -> creaditPayable=800 (cap), selfPayable=200 (overage) -- correct")
        void capExceeded_creditGetsCapAmount_selfGetsOverage() {
            ServiceSearchResDto searchResDto = new ServiceSearchResDto();
            searchResDto.setAdmissionId(null);
            searchResDto.setEncounterId(1);
            searchResDto.setContractId(1);
            searchResDto.setServiceMasterId(700);
            searchResDto.setOrderDate("2026-07-16 10:00:00");
            searchResDto.setOrganizationId(1);
            searchResDto.setUnitId(1);
            searchResDto.setVisitTypeId(1); // OP
            searchResDto.setBillingBedCategoryId(1);
            searchResDto.setPatientCategoryId(null);
            searchResDto.setDefaultSelfTariffId(9);
            searchResDto.setPackageMasterId(50);
            searchResDto.setChangedPrice(null);
            searchResDto.setOrdDiscPer(0.0);
            searchResDto.setPatientClassId(null);
            searchResDto.setUpdateCap(null);
            searchResDto.setPatientClassPer(null);
            searchResDto.setIsFromEncounter(null);
            searchResDto.setApplyAuth(false);

            ContractConsumptionDto resDto = new ContractConsumptionDto();
            resDto.setType(1);
            resDto.setPriorityId(1);
            resDto.setIsDiscount('N');
            resDto.setIsSchemeDiscount('N');
            resDto.setServiceRate(1000.0);
            resDto.setTaxPercentage(0.0);
            resDto.setCoShare(0.0);
            resDto.setBalanceCapAmount(800.0);
            resDto.setPreAuthBalAmt(0.0);

            List<ContractConsumptionDto> serviceDetailslist = Arrays.asList(resDto);
            when(contractConsumptionDao.getPackageDetailsByContractId(any())).thenReturn(serviceDetailslist);
            lenient().when(contractConsumptionDao.getPatientClassAndPercentageByVisit(any())).thenReturn(null);

            ContractConsumptionDto passthrough = new ContractConsumptionDto();
            passthrough.setCreaditPayable(800.0);
            passthrough.setSelfPayable(200.0);
            when(billingCommonMethods.updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    any(), eq(resDto), any(), any(), any(), eq(null), eq(false), eq(true)))
                    .thenReturn(passthrough);

            service.consumePackageFromContract(searchResDto);

            // Confirms: creaditPayable=800 (up to the cap), selfPayable=1000-800=200 (the overage)
            // -- correct, complementary split (no coShare step in this method, unlike the sibling
            // branch in consumeServiceFromContractPackage that was found leaking money). The first
            // argument (consumptionDto) is freshly constructed inside the method from searchResDto,
            // so it can't be matched by reference -- any() is the right matcher here.
            verify(billingCommonMethods).updatePreAuthAndServiceCapAmountForFirstPriorityContract(
                    any(), eq(resDto), eq(800.0), eq(200.0), any(), eq(null), eq(false), eq(true));
        }
    }
}
