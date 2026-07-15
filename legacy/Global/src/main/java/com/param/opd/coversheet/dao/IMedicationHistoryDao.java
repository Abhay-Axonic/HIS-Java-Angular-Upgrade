package com.param.opd.coversheet.dao;


import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.AdministrationWorklistDto;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.model.PrescriptionMaster;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMedicationHistoryDao {
	
	public Response  saveMedicationHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;
	
	public Response  getMedicationHistoryList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getAdministrationWorkListList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;
	
	public Response getDetailedListOfMedication(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;
	
	public Response getMedicationEncountersList(Integer patientId, Integer encounterId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response saveJustificationForMedicationStopForHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getContinuedMedicationList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMedicationHistoryListForBookView(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMasterListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMasterListOfICPCSystemForCoversheetForCode(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getDetailedListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getBatchListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getUomListByBatchIdForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto) throws ApplicationException;

	public Response getSubstituteListForActiveMedication(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getBrandListByGenericIdForVaccinationChart(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

//	public List<AdministrationWorklistDto> getPrescriptionDetailsList(AdministrationWorklistDto administrationWorklistDto);

	public Response getPrescriptionDetailsList(Integer prescriptionId, Integer storeId);

	public Response getAvailableStokeByItemId(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getPatientMedicationHistoryForDoctorApp(MedicationHistoryDto medicationHistoryDto);

	public Response deleteMedicationHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response updateMedicationHistory(MedicationHistoryDto dto)throws ApplicationException;

	public Response saveCurrentMedicationHistory(List<MedicationHistoryDto> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

	public Response updateCovTempCurrentMedicationHistory(List<MedicationHistoryDto> list, CoversheetTemplateMasterDto coversheetTemplateMasterDto);

	public Response getFavriteListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;



}
