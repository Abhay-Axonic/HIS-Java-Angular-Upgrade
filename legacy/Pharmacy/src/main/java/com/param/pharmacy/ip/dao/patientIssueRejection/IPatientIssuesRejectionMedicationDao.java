package com.param.pharmacy.ip.dao.patientIssueRejection;

import java.util.List;

import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.global.model.PatientSponsorDetails;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearch;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;

public interface IPatientIssuesRejectionMedicationDao {
	
	List<Reject> patientIssuesRejectionMedicationSearch(PatientIssueRejectionSearch patientIssueRejectionSearch, Integer screenId);
	
	Reject patientIssuesRejectionMedicationById(Integer id);
	
	List<RejectDetail> patientIssuesRejectionMedicationDetailById(Integer rejectId);
	
	RejectDetail patientIssuesRejectionMedicationDetailByIdValue(Integer rejectDetailId);
	
	Reject patientIssuesRejectionMedicationSave(Reject reject);
	
	RejectedRejection patientIssuesRejectedRejectionMedicationSave(RejectedRejection rejectedRejection);

	List<PatientIssueRejectionSearchResult> patientIssuesRejectionMedicationSearchPT(PatientIssueRejectionSearch patientIssueRejectionSearch,
			Integer screenId);

	List<PatientSponsorDetails> findByEncounterId(Integer encounterId);

	List<PatientSponsorDetails> findByAdmissionId(Integer admissionId);
}