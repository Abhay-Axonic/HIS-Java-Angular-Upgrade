package com.param.opd.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NursingInitialAssessmentDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface INursingInitialAssessmentService {

	public Response saveNursingInitialAssessment(NursingInitialAssessmentDto nursingInitialAssessmentDto)throws ApplicationException;

	public Response getNursingInitialAssessmentList(NursingInitialAssessmentDto nursingInitialAssessmentDto)throws ApplicationException;

}
