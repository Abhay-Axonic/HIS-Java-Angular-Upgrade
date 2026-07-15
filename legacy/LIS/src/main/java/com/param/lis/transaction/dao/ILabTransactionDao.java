package com.param.lis.transaction.dao;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.dto.LastInvestigationDto;
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

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ILabTransactionDao extends IGenericDao<LabSampleMaster, Integer> {

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

	public Response sampleCollection(LabSampleMasterDto labSampleMasterDto) throws ApplicationException;

	public Response getCollectedSample(Integer orgId, Integer orgUnitId, Integer deptId, Integer offset,
			Integer recordPerPage) throws ApplicationException;

	public Response sendToCr(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto) throws ApplicationException;

	public Response getTotalSendToCrRecord(SearchDto searchDto)
			throws ApplicationException;

	public Response updateOrderStatus(Integer orderId, Integer orderStatusId) throws ApplicationException;

	public Response updateServiceRenderStatus(Integer orderDetailsId, Integer unitId) throws ApplicationException;

	public Response searchPhlebotomyWorklistPatient(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response reprintBarcode(@RequestBody List<String> sampleIds, Integer labSampleDetailsId) throws ApplicationException;

	public Response getFilteredPhlebotomyWorklistPatient(SearchDto searchDto) throws ApplicationException;

	public Response searchSendToCRPatientlist(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response getFilteredSendToCRPatientlist(SearchDto searchDto) throws ApplicationException;

	public Response searchLabGenralLabSampleNo(SearchCommonDto searchCommonDto) throws ApplicationException;

	public Response updateOrderStatus(Integer orderId) throws ApplicationException;
	/**
	 * PhlebotomyWorklist Details
	 * 
	 * @version 1.1
	 */
	public Response v1GetPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId)
			throws ApplicationException;

	/** In patient Collection 
	 * @param deptId */
	public Response getInPatientOrders(int orgId, int orgUnitId, int patientId, int admissionId, int deptId)
			throws ApplicationException;

	public Response<LabSampleDetailsMasterDtoIP, Integer> getInPatientRadiologyOrders(int orgId, int orgUnitId,int admissionId)
			throws ApplicationException;

	public Response getFilteredSendToCRPatientlistCount(SearchDto searchDto)throws ApplicationException;

	public Response getPerformTestId(CollectedSampleDto collectedSampleDto)throws ApplicationException;

	public String getTestName(Integer orderId,Integer unitId);

	public Response getPatHistoryByEncounterId(LabSampleMasterDto labSampleMasterDto)throws ApplicationException;

	public Response updatePatHistoryByEncounterId(EncounterMasterDto encounterMasterDto)throws ApplicationException;

	public Response getPhlebotomistList(LabSampleMasterDto labSampleMasterDto)throws ApplicationException;

	public Response userAllocationHomeCollectionSearch(SearchDto searchDto)throws ApplicationException;

	public Response labSampleHomeCollectionSearch(SearchDto searchDto)throws ApplicationException;

	public Response getHomeColletionTestList(Integer orgId, Integer orgUnitId, Integer orderId, Integer deptId,
			Character isPreOrPostBilling)throws ApplicationException;

	public Response updateLabSampleDetails(SearchDto searchDto)throws ApplicationException;

	public Response updateAssignUnitAndTestId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response getPatientLastHistoryOfTest(LastInvestigationDto dto)throws ApplicationException;

	public Response getPhlebotomyWorklistType(SearchDto searchDto)throws ApplicationException;

	public Response getLabSortingList()throws ApplicationException;

	public Response getPhlebotomyWorklistOnNewOPBill(List<SearchDto> searchDto)throws ApplicationException;

	public Response checkForOrderCompletion(SearchDto searchDto);

	public Response uploadLabReportDocument(LabReportDocumentDetailsDto labReportDocumentDetailsDto)throws ApplicationException;

	public Response getLabReportDocumentDetailsByTest(SearchDto searchDto)throws ApplicationException;

	public Response validateBarcode(LabSampleDetailsMasterDto searchDto)throws ApplicationException;

	public Response getCompanyOrInsuranceNameById(LabSampleMasterDto dto)throws ApplicationException;

	public Response getDatesByLabDetialsId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response updateDatesByLabDetialsId(LabSampleDetailsMasterDto dto)throws ApplicationException;

	public Response PhlebotomyWorklistDetails(SearchDto searchDto)throws ApplicationException;

	public Response getSpecialityAndSubSpecialityListByUserIdByUnitId(SearchDto searchDto)throws ApplicationException;

	public String getTestNamePhlebotomy(Integer orderId, Integer unitId, SearchDto searchDto);

	public Response amendReportReason(LabResultMasterDto dto);

	public Response<LabSampleMasterDto, Integer> getSampleDataForInterface(String interfaceName , String sampleID, String sampleInfoDate, int apiNumber, int orgUnitId);

	public Response getOrderDetailsData(List<Integer> orderIds)throws ApplicationException;
//
//	public Response getPOCLabServices(int orgId, int orgUnitId, int patientId, int admissionId, int deptId) throws ApplicationException;
}
