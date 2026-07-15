package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.admission.dto.BedAllocationDto;
import com.param.adt.admission.dto.NursingPatientAcceptanceDto;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedCategoryMasterDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.global.dto.AdmissionDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBedAllocationService 
{
	
	@Transactional
	Response getActiveBedList(BedMasterDto bedMasterDto) throws ApplicationException;
	
	@Transactional
	Response getActiveFloorMasterList(BedAllocationDto bedAllocationDto) throws ApplicationException;
	
	@Transactional
	Response getWardListByFloorId(BedAllocationDto bedAllocationDto) throws ApplicationException;
	
	@Transactional
	Response getNursingStationListByWard(BedAllocationDto bedAllocationDto)throws ApplicationException;
	
	@Transactional
	Response getBedCategoryList(BedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;
	
	@Transactional
	Response getBedStatusList() throws ApplicationException;
	
	@Transactional
	Response getMealPreference(MealPreferenceMasterDto mealPreferenceMasterDto) throws ApplicationException;
	
	@Transactional
	Response savePatientAdmission(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response getBedListByMultipleCriteria(BedMasterDto bedMasterDto)throws ApplicationException;

	Response updateWatingListNumber(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Boolean isPatientAlreadyAdmitted(int patientId, int orgId, int unitId)throws ApplicationException;
	
	@Transactional
	com.param.global.common.Response test() throws ApplicationException;

	
	//Response getPatientAcceptanceListForNursing(int orgId, int unitId)throws ApplicationException;

	@Transactional
	Response acceptNursingAcceptanceRequet(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response rejectNursingAcceptanceRequet(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response getPatientAcceptanceListForNursing(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;

	Response updateAutorenderServices(AdmissionDto admissionDto)throws ApplicationException;

	Response postAutorenderServices(AdmissionDto admissionDto) throws ApplicationException;

	Response postAdmissionServiceCharges(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response getActiveLaborBedList(BedMasterDto bedMasterDto)throws ApplicationException;
	
	@Transactional
	Response getActiveHemodialysisBedList(BedMasterDto bedMasterDto)throws ApplicationException;
	
	@Transactional
	Response getPatientAcceptanceListForNursingCount(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;
	
	@Transactional
	Response getcheckBedAllocation(AdmissionDto admissionDto) throws ApplicationException;
	
	@Transactional
	Response isBedBookedOrNot(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response universalSearchForNursingAcceptance(NursingPatientAcceptanceDto nursingPatientAcceptanceDto)throws ApplicationException;

	@Transactional
	Response getAdmisssoinTAT(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response updateNotRequiredServices(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response updateRemarkAdmissionDetailsByAdissionId(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response editAdmissionDetailsByAdissionIdAdmiitedPatientList(AdmissionDto admission)throws ApplicationException;

	@Transactional
	Response updateOtStatusAgainstAdmission(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response getBedCategoryListByVisitId(BedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;

}
