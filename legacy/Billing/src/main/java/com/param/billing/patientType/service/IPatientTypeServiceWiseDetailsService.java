package com.param.billing.patientType.service;

import com.param.billing.patientType.dto.PatientTypeDto;
import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.billing.patientType.dto.PatientTypeServiceWiseDetailsDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IPatientTypeServiceWiseDetailsService {
	
	Response savePatientTypeServiceWiseDetails(PatientTypeDto patientTypeDto) throws Exception;

	Response getPatientTypeServiceWiseDetails(int patientTypeId, int organizationId, int unitId) throws Exception;

	Response savePatientType(PatientTypeDto patientTypeDto)throws Exception;

	Response getPatientType(int organizationId, int unitId)throws Exception;

	Response updatePatientTypeStatus(PatientTypeDto patientTypeDto) throws Exception;

	Response updatePatientType(PatientTypeDto patientTypeDto) throws Exception;

	Response updatePatientTypeGroupStatus(PatientTypeGroupWiseDetailsDto patientTypeGroupWiseDetailsDto) throws Exception;

	Response updatePatientTypeServiceStatus(PatientTypeServiceWiseDetailsDto patientTypeServiceWiseDetailsDto) throws Exception;



}
