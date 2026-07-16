package com.param.global.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.common.BillingCommonMethods;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IDoctorConsultationServiceMapperDao;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IUnitServiceMapperDao;
import com.param.global.dao.IUnitServiceTariffMasterDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorConsultationServiceMapperDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IMPackageMasterService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;

/**
 * SHADOW-TEST HARNESS EXPANSION (Step 2) -- ContractConsumptionServiceImpl's 13 small methods
 * (~300 lines combined, per docs/BUSINESS_RULE_MANIFEST.md's Step 2 scope estimate), covered in
 * full per explicit owner instruction. The other 3 methods in this class
 * (consumeServiceFromContract ~1,842 lines, consumeServiceFromContractPackage ~716 lines,
 * consumePackageFromContract ~445 lines) are handled separately with a targeted, not exhaustive,
 * approach -- see the follow-up characterization work and docs/BUSINESS_RULE_MANIFEST.md for what
 * is and is not covered there.
 *
 * Same rigor and process as every prior batch: characterize current behavior, log findings, fix
 * nothing. getSerivceRateGlobal and getSerivceRateGlobalAfterTariffChange each call into one of
 * the 3 large methods internally -- rather than skip them or require the large methods to be
 * characterized first, this suite uses Mockito.spy() to stub out exactly that one inner call,
 * isolating and testing only these two methods' OWN logic (the consultation-rate lookup and the
 * payee/package-mapper construction around the call, in getSerivceRateGlobal's case).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContractConsumptionServiceImpl -- the 13 small methods, characterization (current behavior, not verified-correct behavior)")
class ContractConsumptionServiceImplCharacterizationTest {

    @Mock private IContractConsumptionDao contractConsumptionDao;
    @Mock private IEncounterMasterService encounterMasterService;
    @Mock private IMPackageMasterDao mPackageMasterDao;
    @Mock private IUnitServiceTariffMasterDao unitServiceTariffMasterDao;
    @Mock private IUnitServiceMapperDao unitServiceMapperDao;
    @Mock private IOrderDetailsMasterDao orderDetailsMasterDao;
    @Mock private IEncounterMasterDao encounterMasterDao;
    @Mock private SessionService sessionService;
    @Mock private IDoctorConsultationServiceMapperDao doctorConsultationServiceMapperDao;
    @Mock private IMPackageMasterService mPackageMasterService;
    @Mock private BillingCommonMethods billingCommonMethods;

    private ContractConsumptionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContractConsumptionServiceImpl();
        // Package-private field injection (no Spring context needed) -- same package, matches the
        // fields' actual (default) visibility in ContractConsumptionServiceImpl.java.
        service.iContractConsumptionDao = contractConsumptionDao;
        service.iEncounterMasterService = encounterMasterService;
        service.iMPackageMasterDao = mPackageMasterDao;
        service.iUnitServiceTariffMasterDao = unitServiceTariffMasterDao;
        service.iUnitServiceMapperDao = unitServiceMapperDao;
        service.iOrderDetailsMasterDao = orderDetailsMasterDao;
        service.iEncounterMasterDao = encounterMasterDao;
        service.sessionService = sessionService;
        service.iDoctorConsultationServiceMapperDao = doctorConsultationServiceMapperDao;
        service.iMPackageMasterService = mPackageMasterService;
        service.billingCommonMethods = billingCommonMethods;
    }

    @Nested
    @DisplayName("resetContractCapping")
    class ResetContractCapping {

        @Test
        @DisplayName("delegates to the DAO with the same ids and returns a SUCCESS response, ignoring the DAO's own Integer return value")
        void delegatesAndReturnsSuccess() throws Exception {
            Response result = service.resetContractCapping(7, 8);

            verify(contractConsumptionDao).resetContractCapping(7, 8);
            assertThat(result.getStatus()).isEqualTo("success");
        }

        @Test
        @DisplayName("wraps any DAO exception as a ServiceException")
        void daoException_wrappedAsServiceException() throws Exception {
            when(contractConsumptionDao.resetContractCapping(any(), any())).thenThrow(new RuntimeException("boom"));

            assertThatThrownBy(() -> service.resetContractCapping(7, 8)).isInstanceOf(ServiceException.class);
        }
    }

    @Nested
    @DisplayName("addCapAmount")
    class AddCapAmount {

        @Test
        @DisplayName("no orderDetailsId, no saleId, empty payee/package lists: nothing is written, still returns SUCCESS")
        void simplestPath_noWrites() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();

            Response result = service.addCapAmount(dto);

            assertThat(result.getStatus()).isEqualTo("success");
            verify(orderDetailsMasterDao, never()).getOrderDetailsById(anyInt());
            verify(orderDetailsMasterDao, never()).cancelUnRenderedServices(any());
        }

        @Test
        @DisplayName("orderDetailsId set, quantity null, order already cancelled: rejects with a specific error BEFORE any cap/package writes")
        void orderAlreadyCancelled_rejectsEarly() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsId(55);
            dto.setOrderDetailsPayeeMapperDtosList(Arrays.asList(new OrderDetailsPayeeMapperDto()));

            OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
            orderDetails.setOrderQty(3);
            orderDetails.setOrdCancelled('Y');
            Response ordDtlsRes = org.mockito.Mockito.mock(Response.class);
            when(ordDtlsRes.getObject()).thenReturn(orderDetails);
            when(orderDetailsMasterDao.getOrderDetailsById(55)).thenReturn(ordDtlsRes);

            Response result = service.addCapAmount(dto);

            assertThat(result.getMessage()).isEqualTo("Service is already cancelled by another user...!!");
            verify(contractConsumptionDao, never()).addCapAmount(any());
        }

        @Test
        @DisplayName("saleId null, non-empty payee list: writes the cap amount via the DAO")
        void nonEmptyPayeeList_writesCapAmount() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsPayeeMapperDtosList(Arrays.asList(new OrderDetailsPayeeMapperDto()));

            service.addCapAmount(dto);

            verify(contractConsumptionDao).addCapAmount(dto);
        }

        @Test
        @DisplayName("saleId null, non-empty package list: cancels the package service via the DAO")
        void nonEmptyPackageList_cancelsPackageService() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsPackageMapperDtosList(Arrays.asList(new OrderDetailsPackageMapperDto()));

            service.addCapAmount(dto);

            verify(mPackageMasterDao).cancelPackageService(dto);
        }

        @Test
        @DisplayName("saleId set (non-null): skips BOTH the payee-list cap write and the package-list cancellation, even if both lists are non-empty")
        void saleIdSet_skipsBothWrites() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setSaleId(999);
            dto.setOrderDetailsPayeeMapperDtosList(Arrays.asList(new OrderDetailsPayeeMapperDto()));
            dto.setOrderDetailsPackageMapperDtosList(Arrays.asList(new OrderDetailsPackageMapperDto()));

            service.addCapAmount(dto);

            verify(contractConsumptionDao, never()).addCapAmount(any());
            verify(mPackageMasterDao, never()).cancelPackageService(any());
        }

        @Test
        @DisplayName("orderDetailsId > 0: cancels un-rendered services via the DAO, using createdBy/reason/remark from the dto")
        void positiveOrderDetailsId_cancelsUnRenderedServices() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsId(55);
            dto.setQuantity(2.0); // set so the getOrderDetailsById lookup is skipped
            dto.setCreatedBy(42);
            dto.setOrdCancelReasonId(3);
            dto.setOrdCancelRemark("patient request");

            service.addCapAmount(dto);

            verify(orderDetailsMasterDao).cancelUnRenderedServices(argThatCancelOrderMatches(55, 42, 3, "patient request"));
        }

        private com.param.global.dto.CancelOrderDto argThatCancelOrderMatches(Integer orderDetailsId, Integer userId, Integer reasonId, String remark) {
            return org.mockito.ArgumentMatchers.argThat(c ->
                    c.getOrderDetailsId().equals(orderDetailsId) && c.getUserId().equals(userId)
                            && c.getOrdCancelReasonId().equals(reasonId) && c.getOrdCancelRemark().equals(remark));
        }
    }

    @Nested
    @DisplayName("addCapContract")
    class AddCapContract {

        @Test
        @DisplayName("non-empty payee list: writes the cap amount via the DAO")
        void nonEmptyList_writesCapAmount() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsPayeeMapperDtosList(Arrays.asList(new OrderDetailsPayeeMapperDto()));

            service.addCapContract(dto);

            verify(contractConsumptionDao).addCapAmount(dto);
        }

        @Test
        @DisplayName("empty/null payee list: no write, still returns SUCCESS")
        void emptyList_noWrite() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();

            Response result = service.addCapContract(dto);

            verify(contractConsumptionDao, never()).addCapAmount(any());
            assertThat(result.getStatus()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("addCapPackage")
    class AddCapPackage {

        @Test
        @DisplayName("non-empty package list: cancels the package service via the DAO")
        void nonEmptyList_cancelsPackageService() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setOrderDetailsPackageMapperDtosList(Arrays.asList(new OrderDetailsPackageMapperDto()));

            service.addCapPackage(dto);

            verify(mPackageMasterDao).cancelPackageService(dto);
        }

        @Test
        @DisplayName("empty/null package list: no write, still returns SUCCESS")
        void emptyList_noWrite() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();

            Response result = service.addCapPackage(dto);

            verify(mPackageMasterDao, never()).cancelPackageService(any());
            assertThat(result.getStatus()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("getActivePaymentEntitlement")
    class GetActivePaymentEntitlement {

        @Test
        @DisplayName("empty contract result list: no package lookup at all")
        void emptyContractList_noPackageLookup() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            Response contractRes = org.mockito.Mockito.mock(Response.class);
            when(contractRes.getListObject()).thenReturn(new ArrayList<>());
            when(contractConsumptionDao.getActivePaymentEntitlement(dto)).thenReturn(contractRes);

            service.getActivePaymentEntitlement(dto);

            verify(mPackageMasterService, never()).getExistingActivePackageByPatientEncounter(any(), any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("non-empty result, patientId set, visitTypeId==OP(1): package lookup uses encounterId, not admissionId")
        void opVisit_usesEncounterId() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            ContractConsumptionDto found = new ContractConsumptionDto();
            found.setPatientId(11);
            found.setVisitTypeId(1); // OP
            found.setEncounterId(21);
            found.setAdmissionId(31);
            found.setOrganizationId(1);
            found.setUnitId(2);
            Response contractRes = org.mockito.Mockito.mock(Response.class);
            when(contractRes.getListObject()).thenReturn(Arrays.asList(found));
            when(contractConsumptionDao.getActivePaymentEntitlement(dto)).thenReturn(contractRes);

            service.getActivePaymentEntitlement(dto);

            verify(mPackageMasterService).getExistingActivePackageByPatientEncounter(11, 21, 1, 1, 2, 0);
        }

        @Test
        @DisplayName("non-empty result, patientId set, visitTypeId!=OP: package lookup uses admissionId instead of encounterId")
        void nonOpVisit_usesAdmissionId() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            ContractConsumptionDto found = new ContractConsumptionDto();
            found.setPatientId(11);
            found.setVisitTypeId(2); // not OP
            found.setEncounterId(21);
            found.setAdmissionId(31);
            found.setOrganizationId(1);
            found.setUnitId(2);
            Response contractRes = org.mockito.Mockito.mock(Response.class);
            when(contractRes.getListObject()).thenReturn(Arrays.asList(found));
            when(contractConsumptionDao.getActivePaymentEntitlement(dto)).thenReturn(contractRes);

            service.getActivePaymentEntitlement(dto);

            verify(mPackageMasterService).getExistingActivePackageByPatientEncounter(11, 31, 2, 1, 2, 0);
        }
    }

    @Nested
    @DisplayName("isConsultationAmountPaidBySelf")
    class IsConsultationAmountPaidBySelf {

        @Test
        @DisplayName("passes straight through to the DAO")
        void delegatesToDao() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            Response daoRes = org.mockito.Mockito.mock(Response.class);
            when(contractConsumptionDao.isConsultationAmountPaidBySelf(dto)).thenReturn(daoRes);

            Response result = service.isConsultationAmountPaidBySelf(dto);

            assertThat(result).isSameAs(daoRes);
        }
    }

    @Nested
    @DisplayName("getPatientClassByVisit")
    class GetPatientClassByVisit {

        @Test
        @DisplayName("passes straight through to the DAO")
        void delegatesToDao() throws Exception {
            OrderMasterDto orderMasterDto = new OrderMasterDto();
            when(contractConsumptionDao.getPatientClassByVisit(orderMasterDto)).thenReturn(orderMasterDto);

            assertThat(service.getPatientClassByVisit(orderMasterDto)).isSameAs(orderMasterDto);
        }
    }

    @Nested
    @DisplayName("getIsBedChargesService")
    class GetIsBedChargesService {

        @Test
        @DisplayName("passes straight through to the encounter DAO")
        void delegatesToDao() {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            when(encounterMasterDao.getIsBedChargesService(dto)).thenReturn('Y');

            assertThat(service.getIsBedChargesService(dto)).isEqualTo('Y');
        }
    }

    @Nested
    @DisplayName("getGstApplicableForBillingBedCategory")
    class GetGstApplicableForBillingBedCategory {

        @Test
        @DisplayName("passes straight through to the DAO")
        void delegatesToDao() {
            when(contractConsumptionDao.getGstApplicableForBillingBedCategory(7)).thenReturn(true);

            assertThat(service.getGstApplicableForBillingBedCategory(7)).isTrue();
        }
    }

    @Nested
    @DisplayName("getgstDetails")
    class GetGstDetails {

        @Test
        @DisplayName("passes straight through to the DAO")
        void delegatesToDao() {
            List<ContractConsumptionDto> brackets = Arrays.asList(new ContractConsumptionDto());
            when(contractConsumptionDao.getgstDetails(7)).thenReturn(brackets);

            assertThat(service.getgstDetails(7)).isSameAs(brackets);
        }
    }

    @Nested
    @DisplayName("consumeServiceFromContractAfterTariffChange")
    class ConsumeServiceFromContractAfterTariffChange {

        @Test
        @DisplayName("FINDING (logged, not fixed): with multiple service-detail rows, only the LAST row's serviceRate survives -- same 'last item silently wins' shape as taxCalculation's GST bracket finding (batch 1)")
        void multipleRows_lastServiceRateWins() {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            ContractConsumptionDto row1 = new ContractConsumptionDto();
            row1.setServiceRate(100.0);
            ContractConsumptionDto row2 = new ContractConsumptionDto();
            row2.setServiceRate(200.0);
            when(contractConsumptionDao.getServiceDetailsByContractIdAfterTariffChange(dto))
                    .thenReturn(Arrays.asList(row1, row2));

            Response result = service.consumeServiceFromContractAfterTariffChange(dto);

            // The loop unconditionally overwrites finalDto.serviceRate on every iteration -- there is
            // no accumulation, no selection logic, just a bare for-each that discards every row except
            // the last one in the list. Confirmed: only row2's 200.0 survives, row1's 100.0 is lost.
            ContractConsumptionDto finalDto = (ContractConsumptionDto) result.getObject();
            assertThat(finalDto.getServiceRate()).isEqualTo(200.0);
        }

        @Test
        @DisplayName("empty service-detail list: returns a dto with serviceRate never set (null)")
        void emptyList_serviceRateStaysNull() {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            when(contractConsumptionDao.getServiceDetailsByContractIdAfterTariffChange(dto)).thenReturn(new ArrayList<>());

            Response result = service.consumeServiceFromContractAfterTariffChange(dto);

            ContractConsumptionDto finalDto = (ContractConsumptionDto) result.getObject();
            assertThat(finalDto.getServiceRate()).isNull();
        }
    }

    @Nested
    @DisplayName("getSerivceRateGlobalAfterTariffChange (spy: consumeServiceFromContractAfterTariffChange runs for real, it's small and already characterized above)")
    class GetSerivceRateGlobalAfterTariffChange {

        @Test
        @DisplayName("delegates to consumeServiceFromContractAfterTariffChange and repackages its object into a fresh SUCCESS response")
        void delegatesAndRepackages() throws Exception {
            ContractConsumptionDto dto = new ContractConsumptionDto();
            when(contractConsumptionDao.getServiceDetailsByContractIdAfterTariffChange(dto)).thenReturn(new ArrayList<>());

            Response result = service.getSerivceRateGlobalAfterTariffChange(dto, false, false);

            assertThat(result.getStatus()).isEqualTo("success");
        }
    }

    @Nested
    @DisplayName("getSerivceRateGlobal (spy: consumeServiceFromContract is stubbed, isolating this method's OWN logic)")
    class GetSerivceRateGlobal {

        @Test
        @DisplayName("isConsultationService='Y', consultationAmount null, visit type 1: looks up and sets the NEW-visit consultation rate before delegating")
        void newVisit_looksUpNewVisitRate() throws Exception {
            ContractConsumptionServiceImpl spyService = spy(service);
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setIsConsultationService('Y');
            dto.setEncounterId(21);

            EncounterMasterDto encounter = new EncounterMasterDto();
            encounter.setUserDefinedVisitTypeId(1); // new visit
            when(encounterMasterDao.getEncounterDetailsForUpdatePackage(21)).thenReturn(encounter);
            DoctorConsultationServiceMapperDto rateDto = new DoctorConsultationServiceMapperDto();
            rateDto.setNewVisitRate(150.0);
            when(doctorConsultationServiceMapperDao.getDoctorConsultationServiceBySpecialityAndDoctorIdForUpdatePackage(encounter))
                    .thenReturn(rateDto);

            ContractConsumptionDto delegateResult = new ContractConsumptionDto();
            delegateResult.setFinalPrice(150.0);
            delegateResult.setServiceRate(150.0);
            Response stubbedResponse = new Response<>("success", null, null, new ArrayList<>(), delegateResult);
            doReturn(stubbedResponse).when(spyService).consumeServiceFromContract(dto);

            spyService.getSerivceRateGlobal(dto, false, false);

            assertThat(dto.getConsultationAmount()).isEqualTo(150.0);
            verify(spyService).consumeServiceFromContract(dto);
        }

        @Test
        @DisplayName("consultationAmount already set: skips the visit-type rate lookup entirely, even if isConsultationService='Y'")
        void consultationAmountAlreadySet_skipsLookup() throws Exception {
            ContractConsumptionServiceImpl spyService = spy(service);
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setIsConsultationService('Y');
            dto.setConsultationAmount(999.0); // already set

            ContractConsumptionDto delegateResult = new ContractConsumptionDto();
            delegateResult.setFinalPrice(100.0);
            delegateResult.setServiceRate(100.0);
            Response stubbedResponse = new Response<>("success", null, null, new ArrayList<>(), delegateResult);
            doReturn(stubbedResponse).when(spyService).consumeServiceFromContract(dto);

            spyService.getSerivceRateGlobal(dto, false, false);

            verify(encounterMasterDao, never()).getEncounterDetailsForUpdatePackage(any());
            assertThat(dto.getConsultationAmount()).isEqualTo(999.0); // unchanged
        }

        @Test
        @DisplayName("builds a single self-payee mapper and a single non-package, non-cap package mapper around the delegate's result")
        void buildsPayeeAndPackageMappersAroundDelegateResult() throws Exception {
            ContractConsumptionServiceImpl spyService = spy(service);
            ContractConsumptionDto dto = new ContractConsumptionDto();
            dto.setServiceId(88);
            dto.setOrganizationId(1);
            dto.setUnitId(2);

            ContractConsumptionDto delegateResult = new ContractConsumptionDto();
            delegateResult.setSelfPayeeId(500);
            delegateResult.setFinalPrice(300.0);
            delegateResult.setServiceRate(250.0);
            Response stubbedResponse = new Response<>("success", null, null, new ArrayList<>(), delegateResult);
            doReturn(stubbedResponse).when(spyService).consumeServiceFromContract(dto);

            OrderDetailsPayeeMapperDto payeeMapper = new OrderDetailsPayeeMapperDto();
            when(billingCommonMethods.createOrderDetaildPayeeObjFinal(eq(500), eq(300.0), eq(88))).thenReturn(payeeMapper);

            Response result = spyService.getSerivceRateGlobal(dto, false, false);

            ContractConsumptionDto finalResult = (ContractConsumptionDto) result.getObject();
            assertThat(finalResult.getOrderDetailsPayeeMapperDtosList()).containsExactly(payeeMapper);
            assertThat(finalResult.getOrderDetailsPackageMapperDtosList()).hasSize(1);
            OrderDetailsPackageMapperDto pkgMapper = finalResult.getOrderDetailsPackageMapperDtosList().getFirst();
            assertThat(pkgMapper.getIsPackageService()).isEqualTo('N');
            assertThat(pkgMapper.getIsCapService()).isEqualTo('N');
            assertThat(pkgMapper.getServiceAmount()).isEqualByComparingTo(java.math.BigDecimal.valueOf(250.0));
        }
    }
}
