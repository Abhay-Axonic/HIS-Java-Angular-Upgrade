package com.param.adt.admission.dao;


import com.param.adt.admission.dto.BedAllocationDto;
import com.param.adt.admission.dto.NursingPatientAcceptanceDto;
import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientSponsorDetailsDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBedAllocationDao extends IGenericDao<AdmissionDetails, Integer>
{
	
	Response getActiveBedList(BedMasterDto bedMasterDto) throws ApplicationException;
	
	Response getActiveFloorMasterList(BedAllocationDto bedAllocationDto) throws ApplicationException;
	
	Response getWardListByFloorId(BedAllocationDto bedAllocationDto) throws ApplicationException;
	
	Response getNursingStationListByWard(BedAllocationDto bedAllocationDto)throws ApplicationException;
	
	Response getBedCategoryList(BedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;
	
	Response getBedStatusList() throws ApplicationException;

	Response getMealPreference(MealPreferenceMasterDto mealPreferenceMasterDto)throws ApplicationException;

	Response savePatientAdmission(AdmissionDto admissionDto)throws ApplicationException;

	Response savePatientDetails(AdmissionDto admissionDto)throws ApplicationException;
	
	Response getBedListByMultipleCriteria(BedMasterDto bedMasterDto)throws ApplicationException;

	Response saveTransfer(TransferDto transferDto) throws ApplicationException;

	Response updateWatingListNumber(AdmissionDto admissionDto)throws ApplicationException;
	
	Response getWatingListNumbers(AdmissionDto admissionDto)throws ApplicationException;

	Boolean isPatientAlreadyAdmitted(int patientId, int orgId, int unitId)throws ApplicationException;

	public Response savePatientSponsorDetails(PatientSponsorDetailsDto patientSponsorDetailsDto)throws ApplicationException;

	Response acceptNursingAcceptanceRequet(AdmissionDto admission)throws ApplicationException;

	Response rejectNursingAcceptanceRequet(AdmissionDto admission)throws ApplicationException;

	Response getPatientAcceptanceListForNursing(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;

	Response getActiveLaborBedList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getActiveHemodialysisBedList(BedMasterDto bedMasterDto)throws ApplicationException;

	Response getPatientAcceptanceListForNursingCount(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;

	Response getcheckBedAllocation(AdmissionDto admissionDto)throws ApplicationException;

	Response universalSearchForNursingAcceptance(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;

	Response getAdmisssoinTAT(AdmissionDto admissionDto)throws ApplicationException;

	Response updateNotRequiredServices(AdmissionDto admission)throws ApplicationException;

	Response updateRemarkAdmissionDetailsByAdissionId(AdmissionDto admission)throws ApplicationException;

	Admission getAdmissionByAdissionId(AdmissionDto admission) throws ApplicationException;

	Admission updateAdmissionObject(Admission admission) throws ApplicationException;

	Response updateOtStatusAgainstAdmission(AdmissionDto admissionDto)throws ApplicationException;

	Response getBedCategoryListByVisitId(BedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;



	
	
}
