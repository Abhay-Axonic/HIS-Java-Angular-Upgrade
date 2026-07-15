package com.param.bloodBank.dao;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IPatientRequisitionDetailsDao {

	Response getComponentListByPatientId(PatientRequisitionDetailsDto dto) throws ApplicationException;

	Response savePatientRequisitionDetails(PatientRequisitionDetailsDto dto) throws ApplicationException;

	Response getPatientRequisitionDetailsOnCrossMatch(PatientRequisitionDetailsDto dto)throws ApplicationException;

	Response updateExportedToCrossMatchStatus(PatientRequisitionDetailsDto dto)throws ApplicationException;

	Response updatePatientRequisitionDetails(CrossMatchRegisterDto crossMatchRegisterDto)throws ApplicationException;

}
