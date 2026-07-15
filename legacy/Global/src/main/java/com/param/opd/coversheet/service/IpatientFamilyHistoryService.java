package com.param.opd.coversheet.service;

import in.param.exception.ApplicationException;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
@Transactional
@SuppressWarnings("rawtypes")
public interface IpatientFamilyHistoryService {

	public Response savePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;

	public Response getOldPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;
	
	public Response getCurrentPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;
	
	public Response updatePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;
	
	public Response updateSaveNurseReviewedFlag(PatientFamilyHistory patientFamilyHistory)throws ApplicationException;
	
	public Response updateStatusEnterInError(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;

	public Response getCurrentPatientFamilyHistoryForCoversheet(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;

	public Response getPatientFamilyHistoryForBookView(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;

	public Response updateStatusRemoveFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;

	public Response getPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException;
}
