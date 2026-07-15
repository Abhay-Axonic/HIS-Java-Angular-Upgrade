package com.param.pharmacy.ip.dao.rejectedRejection;

import java.util.List;

import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.model.PatientSponsorDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionSearch;

public interface IRejectedRejectionMedicationDao {

	List<RejectedRejection> rejectedRejectionMedicationSearch(RejectedRejectionSearch rejectedRejectionSearch, Integer screenId);
	
	RejectedRejection rejectedRejectionMedicationById(Integer id, Integer unitId);
	
	List<RejectedRejectionDetail> rejectedRejectionMedicationDetailById(Integer rejectId, Integer unitId);
	
	RejectedRejectionDetail rejectedRejectionMedicationDetailByIdValue(Integer rejectDetailId, Integer unitId);
	
	RejectedRejection rejectedRejectionMedicationSave(RejectedRejection reject);

	void updatePatientIssueReturn(Integer id, Integer acceptQuantity);
	
	List<PatientSponsorDetails> findByEncounterId(Integer encounterId);

	List<PatientSponsorDetails> findByAdmissionId(Integer admissionId);
	
}