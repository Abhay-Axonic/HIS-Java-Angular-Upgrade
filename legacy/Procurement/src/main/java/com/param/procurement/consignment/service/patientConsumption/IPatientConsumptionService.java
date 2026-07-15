package com.param.procurement.consignment.service.patientConsumption;

import java.util.List;

import com.param.global.pharmacy.dto.PatientConsumtionIssueSave;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionRequest;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionResponse;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDetail;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDropdown;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListDetails;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListResponse;

public interface IPatientConsumptionService {
	
	PatientConsumtionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	List<PatientConsumptionResponse> patientConsumptionSearch(PatientConsumptionRequest request);

	PatientConsumtionDetail patientConsumptionDetails(Integer id, Integer unitId, Integer storeId, Integer orgId, Integer cookieStoreId)throws Exception;

	Object patientConsumptionSave(PatientConsumtionIssueSave request);
	
	List<PatientConsumtionIssueListResponse> patientConsumtionListSearch(PatientConsumptionRequest request);
	
	List<PatientConsumptionResponse> patientConsumtionIssueListSearch(PatientConsumptionRequest request);
	
	PatientConsumtionIssueListDetails patientConsumtionIssueListDetails(Integer id, Integer unitId, Integer storeId, Integer orgId) throws Exception;
   
	//patient consumption
	//Response consumeItemFromContractPatientConsumption(PatientConsumtionIssueSave patientConsumtionIssueSave,Integer admissionId,Integer encounterId) throws Exception;

}
