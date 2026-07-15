package com.param.global.org.common.service;


import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.org.common.dto.EmployeeSurveyDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IEmployeeSurveyService {
	
	public Response saveEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto) throws ApplicationException;

	public Response getEmployeeSurveyById(Integer userId, Integer organizationId, Integer unitId) throws ApplicationException;

}
