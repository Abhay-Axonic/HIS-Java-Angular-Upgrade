package com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.MedicationHandover;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.global.common.Response;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceItem;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearch;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;

public interface IPendingIssuesAcceptanceMedicationDao {
	
	List<Issue> pendingIssuesAcceptanceMedicationSearch(PendingIssuesAcceptanceSearch pendingIssuesAcceptanceSearch, Integer screenId);
	
	Issue pendingIssuesAcceptanceMedicationById(Integer id);
	
	List<PendingIssuesAcceptanceItem> pendingIssuesAcceptanceMedicationDetails(Integer issueId);
	
	Issue pendingIssuesAcceptanceMedicationSave(Issue issue);
	
	IssueDetail pendingIssuesAcceptanceMedicationDetailById(Integer issueDetailId);
	
	Reject pendingIssuesAcceptanceMedicationRejectSave(Reject reject);

	ReturnOfEmptyAmpoules saveReturnOfEmptyAmpules(ReturnOfEmptyAmpoules ampoules);

	List<PrescriptionDetail> medicationHandoverSearch(MedicationReconciliationSearchRequest request, Integer screenId,
			Integer admissionId);

	MedicationReconciliationSearchDto getDischargeDate(Integer admissionId);

	void medicationHandoverSave(MedicationHandover mh);

	void saveAmpoules(ReturnOfEmptyAmpoules ampoules);

	Response getIssueStatus(Integer id);

	Prescription getPrescription(Integer prescriptionId);

	void update(Prescription p);

	MedicationReconciliationSearchDto getIssueAcceptedQuantity(Long id);

	Boolean getMedicationFlag(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave);

	String getRecievedBy(Integer id);
	
}