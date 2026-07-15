package com.param.opd.coversheet.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.model.PatientHabitDetails;

public interface IPatientHabitDetailsMapperDao extends IGenericDao<PatientHabitDetails, Integer>{

	public Response savePatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response getListOfPatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
	
	public Response updatePatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
	
	
	public Response getOldPatientHabitDetails(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
	
	public Response getCurrentPatientHabitDetails(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
	
	public Response updatePatientHabitDetails(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
	
	public Response updateSaveNurseReviewedFlag(PatientHabitDetails patientHabitDetails)throws ApplicationException;
	
	public Response updateStatusEnterInError(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response getPatientHabitsForBookView(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response saveCurrentPatientHabitDetails(List<PatientHabitDetailsDto> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

	public Response updateCovTempCurrentPatientHabitDetails(List<PatientHabitDetailsDto> list, CoversheetTemplateMasterDto coversheetTemplateMasterDto);

	public Response getPatientHabitsForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response getSearchedHabits(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response removePatientHabits(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response savePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response removePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;

	public Response updatePatientHabitRemarkForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException;
}
