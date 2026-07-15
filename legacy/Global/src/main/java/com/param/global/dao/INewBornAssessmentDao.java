package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.model.NewBornAssessment;
import com.param.global.model.NewBornAssessmentApgar;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INewBornAssessmentDao extends IGenericDao<NewBornAssessment, Integer>{

	Response saveNewBornAssessment(NewBornAssessmentDto Dto)throws ApplicationException;
	
	Response saveNewBornAssessmentApgar(NewBornAssessmentApgar apgar)throws ApplicationException;
	
	Response getNewBornAssessmentDetails(NewBornAssessmentDto assessmentDto)throws ApplicationException;
	
	Response updateNewBornAssessment(NewBornAssessmentDto Dto)throws ApplicationException;

}
