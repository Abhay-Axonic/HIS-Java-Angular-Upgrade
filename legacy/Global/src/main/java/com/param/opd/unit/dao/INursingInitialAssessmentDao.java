package com.param.opd.unit.dao;

import com.param.global.common.Response;
import com.param.global.dto.NursingInitialAssessmentDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface INursingInitialAssessmentDao {

	Response saveAppointmentVitalMapper(NursingInitialAssessmentDto nursingInitialAssessmentDto) throws ApplicationException;

	Response getNursingInitialAssessmentList(NursingInitialAssessmentDto nursingInitialAssessmentDto)throws ApplicationException;

}
