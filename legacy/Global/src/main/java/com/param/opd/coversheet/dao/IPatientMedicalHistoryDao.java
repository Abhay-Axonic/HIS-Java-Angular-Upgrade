package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.model.PatientMedicalHistory;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPatientMedicalHistoryDao extends IGenericDao<PatientMedicalHistory, Integer>{

	public Response savePatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response getOldPatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;
	
	public Response getCurrentPatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;
	
	
	public Response updatePatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;
	
	public Response updateSaveNurseReviewedFlag(PatientMedicalHistory patientMedicalHistory)throws ApplicationException;
	
	public Response updateStatusEnterInError(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response getCurrentPatientMedicalHistoryDetailsForCoversheet(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response getPatientMedicalHistoryForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response updateStatusRemoveMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response getPatientDataForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response getPatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException;

	public Response saveMededicalHistoryCurrent(List<PatientMedicalHistoryDto> list, CoversheetTemplateMasterDto resDto)throws ApplicationException;

	public Response updateCovTempMededicalHistoryCurrent(List<PatientMedicalHistoryDto> list, CoversheetTemplateMasterDto coversheetTemplateMasterDto);
}
