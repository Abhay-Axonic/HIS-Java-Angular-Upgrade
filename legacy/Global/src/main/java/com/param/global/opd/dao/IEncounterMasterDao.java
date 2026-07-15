package com.param.global.opd.dao;

import java.util.Date;
import java.util.List;

import com.param.entity.model.master.Tax;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PACSDto;
import com.param.global.dto.PhysiotherapyOrdersListDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.coversheet.model.EncounterMaster;
import com.param.opd.encounter.dto.*;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IEncounterMasterDao extends IGenericDao<EncounterMaster, Integer>{
	public Response saveEncounterMaster(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getEncounterDetailsById(Integer encounterId)throws ApplicationException;
	
	public Long getNextValEncounterMasterId() throws ApplicationException;
	
	public List<EncounterMasterDto> getEncounterDetailsByPatientDoctorVisitTypeId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getEncounterDetails(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	/*	ADDED BY JYOTI
	 * 	DATE 03-05-2018
	 * */
	public Response getListOfEncounterMasterDao(SlotMasterDto slotMasterDto)throws ApplicationException;
	public Response getSearchedListEncounterMaster(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response getListOfAppointmentStatus(AppointmentStatusMasterDto appointmentStatusMasterDto)throws ApplicationException;
	public Response getEncounterByIdForSync(int encounterId)throws ApplicationException;

	public Response getListOfEncountersByPatientId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public List<UnitServiceTariffMasterDto> getBasePriceByServiceTariffMaster(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	public Response getAutoRenderedServicesByPatient(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateAutorenderServices(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;

	public Response updateEncounterStatus(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	
	public Response saveDocumentAgainstEncounter(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;
	//Get Document
    Response getPatientDocDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException;

	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response retestRadiologyResultEntry(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response getEHCPatientsList(EncounterMasterDto encounterMasterDto,Character isPreOrPostBilling);
	
	public Response getEHCConsultation(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getRadiologyListForSampleCollection(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;
	
	public Response getInsuranceDocByPatientIDDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException;

	public Response getOpPatientListByDocId(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public ContractConsumptionDto getServiceRateByCompanyId(ContractConsumptionDto consumptionDto)throws ApplicationException;

	public Integer getPreviousDocQueueNumber(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Integer getPreviousUnitTokenNumber(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getTokenList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Integer getPreviousSpecialityQueueNumber(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updatePatientCritical(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response opPatientListByDocIdForCasualty(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response patientCalled(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getDoctorTokenList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getNursingTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getCheckedInPatientByDoctorId(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateCallCount(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getListOfRadiologyTests(Integer patientId, Integer orgId, Integer orgUnitId)throws ApplicationException;

	public Response updateDoctorFromNursingWorkList(Integer encounterId,Integer oldDocId, Integer docId, Integer reasonId)throws ApplicationException;

	public Response getTokenMsgList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getEnounterDataForEMR(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response savePACSDetails(PACSDto dto)throws ApplicationException;

	public Response getPendingServicesOfEncounter(Integer encounterId)throws ApplicationException;
	
	public Response getNephrologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response acceptHemodialysisRequest(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response getPatientServicesForFutureDate(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response rejectHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)throws ApplicationException;
	
	public Response getReviewApplicableServicesByPatient(Integer patientId,Integer orgId,Integer unitId,Date date)throws ApplicationException;

	public Response checkReferalAndUpdate(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response saveRadiologyMapper(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;
	
	public Response updateEncounterServiceMpperStatus(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getEncounterDetailsForCoversheet(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	Response getOtherServicesForRendering(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	Response renderOtherServices(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException;

	public Response updateChildEncounter(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public EncounterMasterDto getEncounterDetailsForUpdatePackage(Integer encounterId);

	public Response getPatientListForPhysiotherapyWorkbench(PhysiotherapyOrdersListDto physiotherapyOrdersListDto);

	public Response getOrderDetailByAppointment(EncounterMasterDto encounterMasterDto2);

	public Response getEncountersDetailsForCanelledBills(Integer encounterId);

	Tax getTaxPercentageByServiceId(Integer serviceId, Integer unitId);

	public Response getConsentParamDetails(List<String> paramNames);

	String getResult(String query);
	
	public Response getAdmissionsById(int admissionId)throws BillingException;
	
	/**@author Ganesh*/
	public Response checkFeedBackFormSubmitted(TemplateTransactionDto dto)throws ApplicationException;
	public Response updateBackFormSubmitted(TemplateTransactionDto dto)throws ApplicationException;

	public Response updateFollowUpDateOfPatient(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getSearchedListEncounterMasterForNursingAssToken(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response universalSearchOpPatientListByDocId(EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	public Response getUniversalSearchedListEncounterMaster(SlotMasterDto slotMasterDto)throws ApplicationException;

	public Response updateEncounterDetailsPatientByEncounterId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getWardAndFloorAndConsultingDoctorDetails(RadiologyOrdersListDto dto)throws ApplicationException;

	public Response getPatientClassIdByEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getListOfEncountersByPatientIdForWalkinSale(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response getFollowUpDateEncounterList(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateFollowUpDateEncounterRemark(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateAdvisedBy(BillingMasterDto billingMasterDto) throws ApplicationException;

	public Response getLastEncounteredListOfPatient(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response updateLatestCount(SlotMasterDto dto)throws ApplicationException;

	public Response updateDoctorLatestCount(SlotMasterDto dto)throws ApplicationException;

	public Response getLastEncounterDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getLastAdmisionDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getSubSpecialityById(Integer serviceId)throws ApplicationException;

	public Character getIsBedChargesService(ContractConsumptionDto consumptionDto);

	public Response getReferalDetailsByEncounterId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response PatientDocStatusUpdate(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response getPackageOrderCount(OrderDetailsMasterDto ordDtlsDto)throws ApplicationException;

	public Response getOpPatientListByDocIdForVideo(AppointmentStatusSyncDto encounterMasterDto)throws ApplicationException;

	public Response getLastEncounteredListOfPatientForAbdm(EncounterMasterDto encounterMasterDto)throws ApplicationException, Exception;
	public Response getPatientDocDetailsForNabh(PatientDocReqDto patientDocReqDto)throws ApplicationException;

	public Response saveDocumentAgainstEncounterForNbh(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response saveDocumentAgainstEncounterForIncident(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response getPatientDocDetailsForIncident(PatientDocReqDto patientDocReqDto)throws ApplicationException;

    Response getServiceDetails(List<Integer> serviceIds, Integer unitId)throws ApplicationException;

	public String getDocumentAnalyzeUrl(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;

	public Response saveAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto, String json) throws ApplicationException;

	public Response getAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException;


	public Response updateAiSummaryAndQuestion(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public void updateEncounterStopTime(EncounterAssignmenMapperDto mpprDto) throws ApplicationException;

	public Response getAllCoversheetDataByPatientId(Integer patientId) throws ApplicationException;

	public  String getAiLetterCallbackUrl() throws ApplicationException;


	public  Integer getLatestSummaryDocumentPKId(Integer patientId,Integer documentId) throws ApplicationException;
	public  Integer updateSummaryDocument(Integer pkdocId, String docPath) throws ApplicationException;

	public Response getAugmentDoctorList(EncounterMasterDto encounterMasterDto) throws ApplicationException;

	public Response updateAugmentDoctor(EncounterMasterDto encounterMasterDto) throws ApplicationException;
}
