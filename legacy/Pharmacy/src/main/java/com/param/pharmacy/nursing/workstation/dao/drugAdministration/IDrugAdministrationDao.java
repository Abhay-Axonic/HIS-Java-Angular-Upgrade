package com.param.pharmacy.nursing.workstation.dao.drugAdministration;

import java.util.List;

import com.param.entity.model.pharmacy.DrugAdministration;
import com.param.entity.model.pharmacy.DrugAdministrationDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DoseTime;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DoseTime1;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationItem;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequest;

public interface IDrugAdministrationDao {

	List<DrugAdministrationItem> patientIssueReturnSearch(DrugAdministrationRequest request);
	
	List<DoseTime1> getDrugAdministrationItem(DrugAdministrationRequest request);
	
	List<Issue> getPatientDetails(DrugAdministrationRequest request);
	
	DrugAdministration drugAdministrationSave(DrugAdministration request);
	
	DrugAdministrationDetail drugAdministrationDetail(Long prescriptionDetailId, Integer frequencyDetailId);
	
	DrugAdministration getDrugAdministration(Integer id);
	
	DrugAdministrationDetail getDrugAdministrationDetail(Integer id, Integer frequencyDetailId);
	
	DrugAdministrationDetail getDrugAdministrationDetailById(Integer id);
	
	DrugAdministrationDetail getDrugAdministrationDetailByDrugId(Integer drugId);
	
	DrugAdministrationDetail drugAdministrationDetailUpdate(DrugAdministrationDetail request);

	List<Prescription> getPatientDetailsPrescription(DrugAdministrationRequest request);

	List<DrugAdministrationItem> patientIssueReturnSearchStatMedication(DrugAdministrationRequest request);

	List<DrugAdministrationItem> patientIssueReturnForDrugAdministration(DrugAdministrationRequest request);

	DrugAdministrationItem checkReturnQuantity(DrugAdministrationRequest request);

	DrugAdministrationItem checkRejectedIssueQuantity(DrugAdministrationRequest request);

	DrugAdministrationItem checkReturnedIssueQuantity(DrugAdministrationRequest request);
	
}