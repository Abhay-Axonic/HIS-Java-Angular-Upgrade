package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.NewBornAssessmentDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface INewBornAssessmentService {
	
	Response saveNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException;
	
	Response getNewBornAssessmentDetails(NewBornAssessmentDto assessmentDto) throws ApplicationException;

	Response updateNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException;
	
}

