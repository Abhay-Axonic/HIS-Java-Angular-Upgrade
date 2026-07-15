package com.param.pharmacy.inPatient.dao.patientConsumableIssue;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.Sale;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;

public interface IPatientConsumableIssueDao {

	Issue patientConsumableIssue(Integer id);

	Issue patientConsumableIssueById(Integer id, Integer unitId);
	
	IssueDetail patientConsumableIssueDetail(Integer id);
	
	List<Issue> patientConsumableIssueSearch(PatientConsumableIssueRequest request, Integer screenId);
	
	List<IssueDetail> getpatientConsumableIssueDetail(Integer issueId);
	
	PatientIndent savePatientIndent(PatientIndent patientIndent);

	Sale saveSale(Sale sale);

	List<Integer> getIssueDetailids(Integer id, Integer unitId);

	List<IssueDetail> getpatientConsumableIssueDetailByIds(List<Integer> ids, Integer screenFlag, Integer unitId);

	List<Integer> getBatchMapperIds(Integer id);

	List<IssueDetailBatchMapper> getBatchMapperList(List<Integer> batchMapperIds);

	List<IssueDetail> patientConsumableIssueDetail(Integer id, List<Integer> ids);

	void updateIssueDetail(IssueDetail issueD);

	void updatePatientIndent(PatientIndent patientIndent);

	List<SubstitutePatientConsumableDto> getSubstituteIssueDetails(Integer id);

	List<IssueDetailBatchMapper> getBatchMapperForSubstitute(Integer issueDetailId);


	
	
}
