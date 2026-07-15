package com.param.billing.patientType.dao;

import java.util.List;

import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IPatientTypeGroupWiseDetailsDao {

	
	Response savePatientTypeGroupWiseDetails(List<PatientTypeGroupWiseDetailsDto> patientTypeGroupWiseDetailsDto);

	Response getPatientTypeGroupWiseDetails(int patientTypeId, int organizationId, int unitId);

	Response updatePatientTypeGroupStatus(PatientTypeGroupWiseDetailsDto patientTypeGroupWiseDetailsDto);

}
