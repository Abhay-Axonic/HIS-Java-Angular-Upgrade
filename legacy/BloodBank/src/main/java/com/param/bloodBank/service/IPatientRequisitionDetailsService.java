package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IPatientRequisitionDetailsService {
	Response getComponentListByPatientId(PatientRequisitionDetailsDto dto) throws ApplicationException;

	Response savePatientRequisitionDetails(PatientRequisitionDetailsDto dto) throws ApplicationException;

	Response getPatientRequisitionDetailsOnCrossMatch(PatientRequisitionDetailsDto dto)throws ApplicationException;

	Response updateExportedToCrossMatchStatus(PatientRequisitionDetailsDto dto) throws ApplicationException;

	Response updatePatientRequisitionDetails(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException;

}
