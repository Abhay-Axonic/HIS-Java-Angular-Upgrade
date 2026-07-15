package com.param.procurement.consignment.dao.patientConsumption;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionRequest;

public interface IPatientConsumptionDao {

	List<Issue> patientConsumptionSearch(PatientConsumptionRequest request, Integer screenId);

	Issue patientConsumption(Integer id, Integer unitId);

	List<IssueDetail> getpatientConsumptionDetail(Integer id, Integer unitId);

	IssueDetail getpatientConsumptionDetails(Integer id, Integer integer);

	Issue updateIssue(Issue issue);

	List<Issue> patientConsumtionIssueListSearch(PatientConsumptionRequest request, Integer screenId);
	
	List<Issue> patientConsumtionConsignmentIssueListSearch(PatientConsumptionRequest request, Integer screenId);

	Issue patientIssueById(Integer id, Integer unitId);
	
	List<IssueDetail> patientIssueDetailById(Integer id, Integer unitId);

	Sale saveSale(Sale sale);
	
	

}
