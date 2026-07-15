package com.param.pharmacy.nursing.workstation.dao.patientIssueReturn;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;

import com.param.entity.model.pharmacy.SaleDetail;

import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

public interface IPatientIssueReturnDao {

	List<PatientIssueReturn> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId);
	
	List<PatientIssueReturnRequest> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	List<PatientAgainstItemResponse> patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
	
	List<Issue> patientIssueReturnAddItemByIssueId(PatientIssueReturnAddItem patientIssueReturnAddItem);
	
	List<Issue> patientIssueReturnAddItemByItemId(PatientIssueReturnAddItem patientIssueReturnAddItem);
	
	List<PatientAgainstItemResponse> getItemByIssueAndItemId(Integer issueId, List<Integer> itemIds, Integer unitId, List<Integer> batchIds);
	
	List<PatientAgainstItemResponse> getItemByIssueId(List<Integer> issueIds, Integer unitId);
	
	PatientIssueReturn patientIssueReturnById(Integer id, Integer unitId);
	
	List<PatientIssueReturnDetail> patientIssueReturnDetailById(Integer issueReturnId, Integer unitId);
	
	PatientIssueReturn patientIssueReturnSave(PatientIssueReturn patientIssueReturn);
	
	PatientIssueReturnDetail patientIssueReturnDetailByReturnId(Integer issueReturnId);

	//Long getSaleDetailIdByIssueDetailMapper(Integer issueDetailBatchMapper);

	//List<SaleDetail> getSaleDetailsByPatientIssueReturnById(Integer id);

	PharmacySaleReturnDto getSaleDetailIdByIssueDetailMapper(Integer issueDetailBatchMapper);

	Issue getIssue(Integer id);

	void updateIssue(Issue issue);

	boolean getCOPFlagFromSale(Integer saleDetailsId);

	List<PatientIssueReturnRequest> patientAgainstIssueDetailsSearch( PatientIssueReturnRequest patientIssueReturnRequest);

	List<PatientIssueReturnRequest> patientAgainstIssueDetailsBatchMapperSearch(PatientIssueReturnRequest issueDetail);

	ReturnAcceptanceResponse getDocDetailForPatientIssueReturn(Integer id, Integer unitId);

	Integer patientReturnAcceptanceStatus(ReturnAcceptanceSave returnAcceptanceSave);

	Integer patientIssueReturnDao(Integer batchMapperId);

	List<PatientAgainstItemResponse> patientIssueReturnList(List<Integer> batchMapperIds);

	
}