package com.param.billing.patientType.dao;

import com.param.billing.patientType.dto.PatientTypeDto;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IPatientTypeDao {

	Response savePatientType(PatientTypeDto patientTypeDto) throws Exception;

	Response getPatientType(int organizationId, int unitId) throws Exception;

	Response updatePatientTypeStatus(PatientTypeDto patientTypeDto) throws Exception;

	Response updatePatientType(PatientTypeDto patientTypeDto) throws Exception;

	Response checkPatientType(PatientTypeDto patientTypeDto)throws Exception;

	Response updateCheckPatientType(PatientTypeDto patientTypeDto)throws Exception;


	
}
