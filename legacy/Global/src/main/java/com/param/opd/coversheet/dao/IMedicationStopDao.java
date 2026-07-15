package com.param.opd.coversheet.dao;

import java.math.BigInteger;
import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.PrescriptionDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMedicationStopDao {
	
	public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveJustificationForMedicationDelete(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getCountOfMedicationsByPresciptionId(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveMedicationDeleteForPrescription(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response updateAssignPickerAndIssueDetails(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response checkStatusFromIssueDetails(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response checkStatusFromIssueDetailsForUpdate(Integer prescriptionId, Integer prescriptionDetailsid,
			Integer patientId, Integer organizationId)throws ApplicationException;

	public Response checkParentSubstituteId(List<BigInteger> predetailIds)throws ApplicationException;

	public Response updateAssignPickerAndIssueDetailsTest(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response checkStatusFromIssueDetailsMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveJustificationForMedicationDeleteMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response updateAssignPickerAndIssueDetailsMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveMedicationDeleteForPrescriptionMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response updateAssignPickerAndIssueDetailsTestMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	

}
