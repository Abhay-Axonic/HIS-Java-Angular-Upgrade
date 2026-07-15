package com.param.pharmacy.ip.dao.patientIssueRejectionConsumable;

import java.util.List;

import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.pharmacy.ip.dto.patientIssueRejectionConsumable.PatientIssueRejectionConsumableSearch;

public interface IPatientIssuesRejectionConsumableDao {
	
	List<Reject> patientIssuesRejectionConsumableSearch(PatientIssueRejectionConsumableSearch patientIssueRejectionSearch, Integer screenId);
	
	Reject patientIssuesRejectionMedicationById(Integer id);
	
	List<RejectDetail> patientIssuesRejectionMedicationDetailById(Integer rejectId);
	
	RejectDetail patientIssuesRejectionMedicationDetailByIdValue(Integer rejectDetailId);
	
	Reject patientIssuesRejectionMedicationSave(Reject reject);
	
	RejectedRejection patientIssuesRejectedRejectionMedicationSave(RejectedRejection rejectedRejection);
}