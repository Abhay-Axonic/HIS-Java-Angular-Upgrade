package com.param.lis.transaction.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.lis.global.common.ClinicalInterventionProcedureDto;
import org.springframework.web.bind.annotation.RequestBody;

import com.param.global.dto.LastInvestigationDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.CollectedSampleDto;
import com.param.lis.transaction.dto.LabReportDocumentDetailsDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDtoIP;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import java.time.LocalDate;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ILabTransactionService {

	/**
	 * PhlebotomyWorklist Details
	 * 
	 * @version 1.0
	 */

	public Response getPhlebotomyWorklist(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage,Character isPreOrPostBilling) throws ApplicationException;

	public Response getTotalPhlobotomyRecord(SearchDto searchDto)
			throws ApplicationException;

	public Response getPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId, Integer deptId,Character isPreOrPostBilling)
			throws ApplicationException;

	public com.param.global.common.Response sampleCollection(LabSampleMasterDto labSampleMasterDto) throws ApplicationException;
	
	public Response collectSample(LabSampleMasterDto labSampleMasterDto) throws ApplicationException;

	public com.param.global.common.Response sampleCollectionIp(List<LabSampleMasterDto> listLabSampleMasterDto) throws ApplicationException;

	public Response getCollectedSample(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage) throws ApplicationException;

	public Response sendToCr(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto) throws ApplicationException;

	public Response getTotalSendToCrRecord(SearchDto searchDto) throws ApplicationException;

	public Response searchPhlebotomyWorklistPatient(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response searchSendToCRPatientlist(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response getFilteredPhlebotomyWorklistPatient(SearchDto searchDto) throws ApplicationException;

	public Response getFilteredSendToCRPatientlist(SearchDto searchDto) throws ApplicationException;

	public Response searchLabGenralLabSampleNo(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response reprintBarcode(@RequestBody List<String> sampleIds, Integer integer) throws ApplicationException;
	
	public Response updateOrderStatus(Integer orderId) throws ApplicationException;

	/**
	 * PhlebotomyWorklist Details
	 * 
	 * @version 1.1
	 */

	public Response v1GetPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId)
			throws ApplicationException;

	/** IN patient Order 
	 * @param deptId */

	public Response getInPatientOrders(int orgId, int orgUnitId, int patientId, int admissionId, int deptId)
			throws ApplicationException;

	public Response<LabSampleDetailsMasterDtoIP, Integer> getInPatientRadiologyOrders(int orgId, int orgUnitId,int admissionId)
			throws ApplicationException;

	public Response processCyclicServiceAppointment(List<SlotMasterDto> slotMasterDtoList)throws ApplicationException;

	public Response getPerformTestId(CollectedSampleDto collectedSampleDto)throws ApplicationException;

	public Response getPatHistoryByEncounterId(LabSampleMasterDto labSampleMasterDto)throws ApplicationException;

	public Response updatePatHistoryByEncounterId(EncounterMasterDto encounterMasterDto);

	public Response getPhlebotomistList(LabSampleMasterDto labSampleMasterDto)throws ApplicationException;

	public Response  userAllocationHomeCollectionSearch(SearchDto searchDto)throws ApplicationException;

	public Response labSampleHomeCollectionSearch(SearchDto searchDto)throws ApplicationException;

	public Response getHomeColletionTestList(Integer orgId, Integer orgUnitId,
			Integer orderId, Integer deptId, Character isPreOrPostBilling)throws ApplicationException;

	public Response updateLabSampleDetails(SearchDto searchDto)throws ApplicationException;

	public Response updateAssignUnitAndTestId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response getPatientLastHistoryOfTest(LastInvestigationDto dto)throws ApplicationException;

	public Response getPhlebotomyWorklistType(SearchDto searchDto)throws ApplicationException;

	public Response getLabSortingList()throws ApplicationException;

	public Response getPhlebotomyWorklistOnNewOPBill(List<SearchDto> searchList)throws ApplicationException;
	public Response printBarcodeLab(RadiologyOrdersListDto radiologyOrdersListDto)throws ApplicationException;

	public Response checkForOrderCompletion(SearchDto searchDto)throws ApplicationException;

	public Response uploadLabReportDocument(LabReportDocumentDetailsDto labReportDocumentDetailsDto)throws ApplicationException;

	public Response getLabReportDocumentDetailsByTest(SearchDto searchDto)throws ApplicationException;

	public Response validateBarcode(LabSampleDetailsMasterDto searchDto)throws ApplicationException;

	public Response getCompanyOrInsuranceNameById(LabSampleMasterDto dto)throws ApplicationException;

	public Response getBinaryStringOfLABReport(SearchDto dto)throws ApplicationException, Exception;
	
	public Response getDatesByLabDetialsId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response updateDatesByLabDetialsId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response printBarcodeLab1(List<RadiologyOrdersListDto> list);

	public Response<LabSampleMasterDto, Integer> PhlebotomyWorklistDetails(SearchDto searchDto)throws ApplicationException;

	public Response getSpecialityAndSubSpecialityListByUserIdByUnitId(SearchDto searchDto) throws ApplicationException;

	public Response printBarcodeLab2(RadiologyOrdersListDto dto);

	public Response amendReportReason(LabResultMasterDto dto) throws ApplicationException;

	public Response<LabSampleMasterDto, Integer> getSampleDataForInterface(String interfaceName , String sampleID, String sampleInfoDate, int apiNumber, int orgUnitId) throws ApplicationException;

	public Response<LabSampleDetailsMasterDtoIP, Integer> getPOCLabServices(SearchDto searchDto) throws ApplicationException;

	public Response<LabSampleDetailsMasterDtoIP, Integer> saveIntervention(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException;

	public Response<LabSampleDetailsMasterDtoIP, Integer> updateInterventionForCompleteProcedure(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException;
}
