package com.param.global.opd.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.dto.sync.PatientMasterDto;
import com.param.opd.encounter.dto.*;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PACSDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PhysiotherapyOrdersListDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IEncounterMasterService {
	
	public Response saveEncounterMaster(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response saveChildEncounter(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateAiSummaryAndQuestion(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getEncounterDetailsById(Integer encounterId)throws ApplicationException;
	
	Integer assignVisitTypeIdAccordingToConditions(SlotMasterDto encounterMasterDto)throws ApplicationException;

	public Response getEncounterDetails(EncounterMasterDto encounterMasterDto);
	
	public Response getListOfEncounterMasterDao(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response getSearchedListEncounterMaster(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response getListOfAppointmentStatus(AppointmentStatusMasterDto appointmentStatusMasterDto)throws ApplicationException;

	public Response getListOfEncountersByPatientId(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	Response updateEncounterStatus(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	Response saveDocumentAgainstEncounter(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;

	Response saveDocumentFromMylife(PatientMasterDto encounterDocDetailsDto) throws ApplicationException;

	Response getPatientDocDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException;

	UnitServiceTariffMasterDto getBasePriceByServiceTariffMaster(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response retestRadiologyResultEntry(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response getEHCPatientsList(EncounterMasterDto encounterMasterDto,Character isPreOrPostBilling)throws ApplicationException;
	
	public Response getEHCConsultation(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getRadiologyListForSampleCollection(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;
	
	public Response getInsuranceDocByPatientIDDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException;
	
	public Response saveDocumentAgainstEncounterInsuranceAuthorizationsDoc(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;

	public Response getOpPatientListByDocId(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response saveOrderDetails(EncounterMasterDto encounterMasterDto,Integer serviceId,Integer turn);
	
	public Response saveOrderDetailsAfterEncounter(EncounterMasterDto encounterMasterDto,Integer serviceId,Integer turn);

	Integer saveIPConsultationOrder(EncounterMasterDto encounterMasterDto, Integer serviceId);

	public Response getTokenList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response updatePatientCritical(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response opPatientListByDocIdForCasualty(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response patientCalled(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getDoctorTokenList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getNursingTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response scheduleAppointmentFromKiosk(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response generateEncounter(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response billAmountForKiosk(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getCheckedInPatientByDoctorId(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateCallCount(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getListOfRadiologyTests(Integer admissionId, Integer orgId, Integer orgUnitId)throws ApplicationException;

	public Response updateDoctorFromNursingWorkList(Integer encounterId, Integer oldDocId, Integer docId, Integer reasonId)throws ApplicationException;

	Response updateAutorenderServices(EncounterMasterDto encounterMasterDto,Integer turn);
	
	Response updateAutorenderServicesAfterEncounter(EncounterMasterDto encounterMasterDto,Integer turn);

	public Response getBillDetailsOnEncouter(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getTokenMsgList(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	Response getEnounterDataForEMR(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response savePACSDetails(PACSDto dto) throws ApplicationException;

	public Response getPendingServicesOfEncounter(Integer encounterId)throws ApplicationException;
	
	public Response getNephrologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response acceptHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)throws ApplicationException;

	Response updateFutureOrderedServices(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getPatientServicesForFutureDate(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response rejectHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)throws ApplicationException;
	
	public Response getReviewApplicableServicesByPatient(Integer patientId,Integer orgId,Integer unitId)throws ApplicationException;

	public Response rejectRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Integer generateDocQueueNumber(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Integer generateSpecialityQueueNumber(EncounterMasterDto encounterMasterDto);

	public Response getEncounterDetailsForCoversheet(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getOtherServicesForRendering(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	public Response renderOtherServices(RadiologyOrdersListDto radiologyOrdersListDto);

	public Response getPatientListForPhysiotherapyWorkbench(PhysiotherapyOrdersListDto physiotherapyOrdersListDto);

	public Response billAmountForAppointment(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response savePackageConsumptionForDirectEHCEncounter(List<OrderDetailsMasterDto> listToBeAdded) throws ApplicationException;

	public Response getEncountersDetailsForCanelledBills(Integer encounterId);

	public Response appendCensentData(PatientConsentMapperDto dto);
	
	public Response getAdmissionsById(int admissionId)throws BillingException;

	public Response getSystemGeneratedVisitTypeIdForApp(ScheduleAppointmentReqDto scheduleAppointmentReqDto)throws ApplicationException;

	
	/**@author Ganesh*/
	public Response checkFeedBackFormSubmitted(TemplateTransactionDto dto)throws ApplicationException;
	public Response updateBackFormSubmitted(TemplateTransactionDto dto)throws ApplicationException;

	public Response updateFollowUpDateOfPatient(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getSearchedListEncounterMasterorNursingAssToken(SlotMasterDto slotMasterDto)throws ApplicationException;
	public Response universalSearchOpPatientListByDocId(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	public Response getUniversalSearchedListEncounterMaster(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public EncounterMasterDto getEncounterDetailsForUpdatePackage(Integer encounterId);

	public Response updateEncounterDetailsPatientByEncounterId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getWardAndFloorAndConsultingDoctorDetails(RadiologyOrdersListDto dto)throws ApplicationException;

	public Response getPatientClassIdByEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getListOfEncountersByPatientIdForWalkinSale(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getFollowUpDateEncounterList(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateFollowUpDateEncounterRemark(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getLastEncounteredListOfPatient(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateLatestCount(SlotMasterDto dto)throws ApplicationException;

	public Response updateDoctorLatestCount(SlotMasterDto dto)throws ApplicationException;

	public Response getLastEncounterDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getSubSpecialityById(Integer serviceId)throws ApplicationException;

	public Response getReferalDetailsByEncounterId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response PatientDocStatusUpdate(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response getPackageOrderCount(OrderDetailsMasterDto ordDtlsDto)throws ApplicationException;

	public Response getOpPatientListByDocIdForVideo(AppointmentStatusSyncDto encounterMasterDto)throws ApplicationException;

	public Response getPatientDocDetailsForNabh(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response saveDocumentAgainstNabh(EncounterDocDetailsDto encounterDocDetailsDto, MultipartFile[] files) throws ApplicationException;

	public Response saveDocumentAgainstIncident(EncounterDocDetailsDto encounterDocDetailsDto, MultipartFile[] files)throws ApplicationException;

	public Response getPatientDocDetailsForIncident(PatientDocReqDto patientDocReqDto)throws ApplicationException;

    Response getServiceDetails(List<Integer> serviceIds, Integer unitId)throws ApplicationException;

	public Response analyzeReportsWithAI(HttpServletRequest request, EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;

	public Response saveAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto, String json)throws ApplicationException;

	public Response getAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;

	public void updateEncounterStopTime(EncounterAssignmenMapperDto mpprDto) throws ApplicationException;

	public Response getAugmentDoctorList(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateAugmentDoctor(EncounterMasterDto encounterMasterDto) throws ApplicationException;
}
