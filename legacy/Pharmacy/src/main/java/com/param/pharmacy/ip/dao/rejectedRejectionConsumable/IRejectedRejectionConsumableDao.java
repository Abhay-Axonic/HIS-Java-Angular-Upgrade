package com.param.pharmacy.ip.dao.rejectedRejectionConsumable;

import java.util.List;

import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.model.PatientSponsorDetails;
import com.param.pharmacy.ip.dto.rejectedRejectionConsumable.RejectedRejectionConsumableSearch;

public interface IRejectedRejectionConsumableDao {

	List<RejectedRejection> rejectedRejectionConsumableSearch(RejectedRejectionConsumableSearch rejectedRejectionSearch, Integer screenId);
	
	RejectedRejection rejectedRejectionMedicationById(Integer id, Integer unitId);
	
	List<RejectedRejectionDetail> rejectedRejectionMedicationDetailById(Integer rejectId, Integer unitId);
	
	RejectedRejectionDetail rejectedRejectionMedicationDetailByIdValue(Integer rejectDetailId, Integer unitId);
	
	RejectedRejection rejectedRejectionMedicationSave(RejectedRejection reject);

	void updatePatientIssueReturn(Integer id, Integer acceptQuantity);
	
	List<PatientSponsorDetails> findByEncounterId(Integer encounterId);

	List<PatientSponsorDetails> findByAdmissionId(Integer admissionId);
	
}