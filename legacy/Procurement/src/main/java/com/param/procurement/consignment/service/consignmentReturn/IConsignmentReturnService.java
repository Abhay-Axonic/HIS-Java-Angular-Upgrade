package com.param.procurement.consignment.service.consignmentReturn;

import java.util.List;

import com.param.procurement.consignment.dto.consignmentReturn.ConsignmentReturnDropdown;
import com.param.procurement.consignment.dto.consignmentReturn.ImplantReturnSave;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstIssueResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnDetails;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnRequest;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnResponse;

public interface IConsignmentReturnService {

	ConsignmentReturnDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	List<PatientIssueReturnResponse> consignmentMainSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	List<PatientIssueReturnResponse> implantMainSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	PatientIssueReturnDetails patientIssueReturnById(Integer id, Integer unitId);
	
	List<PatientAgainstIssueResponse> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	PatientIssueReturnAddItemResponse patientIssueReturnAddNew(PatientIssueReturnAddItem patientIssueReturnAddItem);

	Object implantReturnSave(ImplantReturnSave implantReturnSave);

		
	//List<ConsignmentReturnResponse> getConsignmentReturnList(ConsignmentReturnRequest request);
	
	/*Object saveConsignmentReturn(ConsignmentReturnRequest request);
	
	ConsignmentReturnResponse getConsignmentReturnDetails(Integer id);

	Object updateConsignmentReturn(ConsignmentReturnRequest request);

	*/
}
