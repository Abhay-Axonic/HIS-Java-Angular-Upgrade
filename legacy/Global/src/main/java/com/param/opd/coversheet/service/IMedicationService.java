package com.param.opd.coversheet.service;

import java.util.List;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.jsonSchema.types.ArraySchema.Items;
import com.param.data.dto.AdministrationWorklistTimeDetailsDto;
import com.param.global.common.Response;
import com.param.global.dto.AdvisedDateAppointmentDto;
import com.param.opd.coversheet.dto.AdministrationWorklistDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IMedicationService {
	
	public Response saveMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response updateMedication(MedicationDTO medicationDTO)throws ApplicationException;
	
	public Response deleteMedication(Integer medicationId)throws ApplicationException;
	
	public Response getMedicIntructions()throws ApplicationException;
	
	public Response getMedicFavorites(Integer doctorId)throws ApplicationException;
	
	public Response getDrugList(Integer unitId,Integer orgId,String searchDrug)throws ApplicationException;
	
	public Response getLastOrder(Integer patientId,Integer encounterId)throws ApplicationException;
	
	public Response getActiveMedication(Integer patientId,String currentDate, Integer unitId)throws ApplicationException;
	
	public Response getMedicationDetails(PrescriptionDto prescriptionDto)throws ApplicationException;
	
	public Response getFrequencyDetails()throws ApplicationException;

	public Response getRoaMasters() throws ApplicationException;

	public Response getRoaList(Integer unitId, Integer orgId, Integer formulationTypeId,Integer genericId,Integer patientId)throws ApplicationException;

	public Response getFrequencyList(Integer orgId, Integer unitId, String searchPar)throws ApplicationException;

	public Response getUOMList(Integer orgId, Integer unitId, Integer itemId)throws ApplicationException;

	public Response getEncounterMedicationHistory(MedicationDTO medicationDTO)throws ApplicationException;

	Response getMedicationHistoryByEncounterId(MedicationDTO medicationDTO) throws ApplicationException;

	public Response getMedicationByAdmissionId(Integer admissionId) throws ApplicationException;

	public Response getICPCSYSTEMList()throws ApplicationException;

	public Response getICPCMasterList(Integer icpcSyatemId, String searchICPC)throws ApplicationException;
	
	public Response saveMedicationHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMedicationHistoryList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getAdministrationWorkListList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;
	
	public Response getDetailedListOfMedication(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getRoaListNonFormulary(Integer unitId, Integer orgId, Integer patientId)throws ApplicationException;
	
	public Response getMasterListOfICPCSystem(Integer icpcSystemId ,String searchICPC)throws ApplicationException;

	public Response getDateWiseListOfPatientMedication(Integer patientId, Integer unitId, String fromDate, String toDate)throws ApplicationException;
	
	public Response getMedicationEncountersList(Integer patientId, Integer encounterId, Integer unitId, Integer organizationId) throws ApplicationException;

	public Response getMedicationListById(Integer prescriptionId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response getOPDMedicationListById(Integer prescriptionId,Integer prescriptionDetailsid ,Integer patientId,Integer itemId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response medicationUpdate(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getDischargeMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getMedicationDetailsByDoctorId(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getStoreIdForNursing(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getMedicationDetailsForMedicationHistory(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getListOfLastEncounterAndAdmission(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getLastEncounterAndAdmissionList(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveJustificationForMedicationStopForHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getContinuedMedicationList(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMedicationHistoryListForBookView(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getGenericListForGenericSearch(Integer unitId, Integer orgId, String searchDrug)throws ApplicationException;

	public Response getMasterListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getMasterListOfICPCSystemForCoversheetForCode(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getDetailedListForAdministrationWorklist(AdministrationWorklistDto administrationWorklistDto)throws ApplicationException;

	public Response getBatchListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getUomListByBatchIdForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getSubstituteListForActiveMedication(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getBrandListByGenericIdForVaccinationChart(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response saveJustificationForMedicationDelete(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response updateAssignPickerAndIssueDetails(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getAvailableStokeByItemId(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response searchMedicationByBrandName(Integer unitId, Integer orgId, String searchKeyword)throws ApplicationException;

	public Response getPatientMedicationHistoryForDoctorApp(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getOrderedMedicationsForDoctorApp(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getCheckIsNarcoticItem(Integer itemId)throws ApplicationException;

	public Response saveAdvisedDateAppointment(AdvisedDateAppointmentDto advisedDateAppointmentDto)throws ApplicationException;

	public Response deleteMedicationHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;
	
	public Response getAdvisedDateAppointmentList(AdvisedDateAppointmentDto advisedDateAppointmentDto)throws ApplicationException;

	public Response getAdvisedDateAppointmentListCount(AdvisedDateAppointmentDto advisedDateAppointmentDto)throws ApplicationException;

	public Response getFrequentlyUsedMedicationList(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getOPDMedicationListByIdForNonFormulary(Integer prescriptionId, Integer prescriptionDetailsid,
			Integer patientId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response updateMedicationHistory(MedicationHistoryDto medicationHistoryDto)throws ApplicationException;

	public Response getGenericListForBrandNameSearch(Integer unitId, Integer orgId, Integer genericId)throws ApplicationException;

	public Response saveCIMMS(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response searchDrugByGenericNameOrItemName(Integer unitId, Integer orgId, String searchDrug)throws ApplicationException;

	public Response saveAdministratorWorkList(List<AdministrationWorklistTimeDetailsDto> dto)throws ApplicationException;

	public Response getItemListWithSubstituteForNewEMR(MedicationListDto dto)throws ApplicationException;

	public Response getSubstituteListForNewEMR(MedicationListDto dto)throws ApplicationException;

	public Response getNonFormularyItemForWorklistPharmacy(Integer prescriptionId, Integer unitId,
			Integer organizationId)throws ApplicationException;

	public Response deleteMedicationForPharmacy(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response saveMedicationPrescriptionDrtailsSkipPrescription(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getIssueDetailsId(PrescriptionDetailsDto dto)throws ApplicationException;

	public Response saveJustificationForMedicationDeleteMultipleMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getUniqueDates(PrescriptionDto dto) throws ApplicationException;

	Response getIpdDoctorCoverSheetDataByDate(PrescriptionDto dto) throws ApplicationException;
}
