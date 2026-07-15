package com.param.opd.coversheet.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientSelfTrigeDto;
import com.param.opd.coversheet.dto.StPatientAllergyDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IPatientSelfTrigeService {


	Response getListOfPatientSelfTrige(PatientSelfTrigeDto patientSelfTrigeDto)throws ApplicationException;

}
