package com.param.procurement.consignment.dao.consignmentReturn;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnRequest;

public interface  IConsignmentReturnDao {
	
	List<PatientIssueReturn> consignmentMainSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId);
	
	List<PatientIssueReturn> implantMainSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId);
	
	List<Issue> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	List<PatientAgainstItemResponse> patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	
	List<Issue> patientIssueReturnAddItemByItemId(PatientIssueReturnAddItem patientIssueReturnAddItem);
	
	List<Object> getItemByIssueAndItemId(Integer issueId, List<Integer> itemIds, Integer unitId);
	
	List<Issue> patientIssueReturnAddItemByIssueId(PatientIssueReturnAddItem patientIssueReturnAddItem);
	
	List<Object> getItemByIssueId(Integer issueId, Integer unitId);
	
	PatientIssueReturn patientIssueReturnById(Integer id, Integer unitId);
	
	ConsignmentPOCreationResponse patientIssueReturnForPO(Integer id);
	
	List<PatientIssueReturnDetail> patientIssueReturnDetailById(Integer issueReturnId, Integer unitId);
	
	PatientIssueReturn implantReturnSave(PatientIssueReturn patientIssueReturn);
	
	
	

}
