package com.param.pharmacy.inPatient.dao.patientConsumableIssueList;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;

public interface IPatientConsumableIssueListDao {

	List<Issue> patientConsumableIssueListSearch(PatientConsumableIssueListRequest request, Integer screenId);

	List<Issue> patientConsumableIssueListSearchForDrugRecall(PatientDetailsRequestForDrugRecall request, Integer screenId);
	
	Issue patientIssueById(Integer id, Integer unitId);
	
	List<IssueDetail> patientIssueDetailById(Integer id);
}