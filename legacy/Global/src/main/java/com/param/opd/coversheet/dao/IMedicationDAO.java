package com.param.opd.coversheet.dao;

import java.util.List;

import com.fasterxml.jackson.databind.jsonSchema.types.ArraySchema.Items;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.DrugMasterDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.model.PrescriptionMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;


/*
 * 
 * Created By Tushar
 */
@SuppressWarnings("rawtypes")
public interface IMedicationDAO extends IGenericDao<PrescriptionMaster, Integer>  {
	
	public Response saveMedication(MedicationDTO medicationDTO)throws ApplicationException;

	public Response updateMedication(MedicationDTO medicationDTO)throws ApplicationException;
	
	public Response deleteMedication(Integer medicationId)throws ApplicationException;
	
	public Response getMedicIntructions() throws ApplicationException;
	
	public Response getMedicFavorites(Integer doctorId) throws ApplicationException;
	
	public List<DrugMasterDto> getDrugList(Integer unitId,Integer orgId,String searchDrug)throws ApplicationException;
	
	public Response getLastOrder(Integer patientId,Integer encounterId)throws ApplicationException;
	
	public Response getActiveMedication(Integer patientId,String currentDate, Integer unitId)throws ApplicationException;
	
	public Response getMedicationDetails(PrescriptionDto prescriptionDto)throws ApplicationException;
	
	public Response getFrequencyDetails()throws ApplicationException;

	public Response getRoaMasters() throws ApplicationException;

	public Response getDrugListByTypeSearch(Integer unitId, Integer orgId, String searchDrug)throws ApplicationException;

	public Response getRoaListByFormulationTypeId(Integer unitId, Integer orgId, Integer formulationTypeId,Integer genericId, Integer patientId)throws ApplicationException;

	public Response getFrequencyList(Integer unitId, Integer orgId, String searchPar)throws ApplicationException;

	public Response getUOMList(Integer unitId, Integer orgId, Integer itemId)throws ApplicationException;

	public Response savePrescription(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response savePrescriptionDetais(PrescriptionDetailsDto prescriptionDetailsDto)throws ApplicationException;

	public Response savePrescriptionFrequencyDetails(PrescriptionDetailsDto prescriptionDetailsDto)throws ApplicationException;
	
	public Response updatePrescription(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response updatePrescriptionDetail(PrescriptionDetailsDto prescriptionDetailsDto)throws ApplicationException;

	public Response updatePrescriptionFrequencyDetails(PrescriptionDetailsDto prescriptionDetailsDto)throws ApplicationException;

	public Response getEncounterMedicationHistory(MedicationDTO medicationDTO)throws ApplicationException;

	public Response getMedicationHistoryByEncounterId(MedicationDTO medicationDTO) throws ApplicationException;

	public Response checkGenericId(Integer genericId, Integer patientId,Integer unitId, Integer orgId)throws ApplicationException;

	public Response getMedicationByAdmissionId(Integer admissionId)throws ApplicationException;

	public Response getICPCSYSTEMList()throws ApplicationException;

	public Response getICPCMasterList(Integer icpcSyatemId, String searchICPC)throws ApplicationException;

	public Response getRoaListByFormulationTypeId(Integer unitId, Integer orgId, Integer patientId)throws ApplicationException;
	
	public Response getMasterListOfICPCSystem(Integer icpcSystemId, String searchICPC)throws ApplicationException;
	
	public Response getDateWiseListOfPatientMedication(Integer patientId,Integer organizationId, String fromDate, String toDate)throws ApplicationException;

	public Response getMedicationListById(Integer prescriptionId,Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getOPDMedicationListById(Integer prescriptionId, Integer prescriptionDetailsid ,Integer patientId,Integer itemId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response getDischargeMedication(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getMedicationDetailsByDoctorId(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getStoreIdForNursing(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getMedicationDetailsForMedicationHistory(PrescriptionDto prescriptionDto)throws ApplicationException;
	
	public Response getListOfLastEncounterAndAdmission(PrescriptionDto medicationDTOs)throws ApplicationException;

/*	public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs)throws ApplicationException;
*/
	public Response getLastEncounterAndAdmissionList(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getGenericListForGenericSearch(Integer unitId, Integer orgId, String searchDrug)throws ApplicationException;

	public Response getPrescriptionIdByEncounterId(Integer encounterId, Boolean boolean1)throws ApplicationException;

	public Response updatePrescriptionStatusId(Integer encounterId)throws ApplicationException;

	public Response searchMedicationByBrandName(Integer unitId, Integer orgId, String searchKeyword)throws ApplicationException;

	public Response getOrderedMedicationsForDoctorApp(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getCheckIsNarcoticItem(Integer itemId)throws ApplicationException;

	public Response getOrderedQty(Integer itemId, Integer prescriptionId, Integer encounterId)throws ApplicationException;

	public Response getFrequentlyUsedMedicationList(PrescriptionDto medicationDTOs)throws ApplicationException;

	public Response getOPDMedicationListByIdForNonFormulary(Integer prescriptionId, Integer prescriptionDetailsid,
			Integer patientId, Integer unitId, Integer organizationId)throws ApplicationException;

	public Response getGenericListForBrandNameSearch(Integer unitId, Integer orgId, Integer genericId)throws ApplicationException;

	public Response saveCIMMS(PrescriptionDto prescriptionDto)throws ApplicationException;

	public Response getPresDtlDataByPresId(Integer prescriptionId, Integer organizationId, Integer unitId);

	public Response searchDrugByGenericNameOrItemName(Integer unitId, Integer orgId, String searchDrug)throws ApplicationException;

	public Response updatePrescriptionDetailList(CoversheetTemplateMasterDto coversheetTemplateMasterDto);

	public Response getItemListWithSubstituteForNewEMR(MedicationListDto dto)throws ApplicationException;

	public Response getSubstituteListForNewEMR(MedicationListDto dto)throws ApplicationException;

	public Integer getFormularyMedicationCount(Integer presId);

	public void updateIsFormularyFlagForPrescription(Integer encounterId, Integer prescriptionId);

	public void updateIsFormularyFlagForPrescriptionY(Integer encounterId, Integer prescriptionId);

	public Response getNonFormularyItemForWorklistPharmacy(Integer prescriptionId,  Integer unitId,
			Integer organizationId)throws ApplicationException;

	public Response savePrescriptionDetaisForPharmacy(PrescriptionDetailsDto prescriptionDetailsDto)throws ApplicationException;

	public Response getIssueDetailsId(PrescriptionDetailsDto dto)throws ApplicationException;

//	public Response saveCurrrentMedicationsOfPatient(List<MedicationDTO> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

//	public Response updatePriorityOfPrescriptionAccordingToPresDtl(Integer id) throws ApplicationException;
//
//	public Response getPriorityOfPrescriptionAccordingToPresDtl(PrescriptionDetailsDto prescriptionDetailsDto) throws ApplicationException;



}
