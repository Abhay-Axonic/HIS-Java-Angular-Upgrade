package com.param.billing.patientType.dao;

import java.util.List;

import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.billing.patientType.dto.PatientTypeServiceWiseDetailsDto;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IPatientTypeServiceWiseDetailsDao {

	Response savePatientTypeServiceWiseDetails(List<PatientTypeServiceWiseDetailsDto> patientTypeServiceWiseDetailsDto);

	Response getPatientTypeServiceWiseDetails(int patientTypeId, int organizationId, int unitId);

	Response updatePatientTypeServiceStatus(PatientTypeServiceWiseDetailsDto patientTypeServiceWiseDetailsDto);

}
