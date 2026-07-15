package com.param.global.org.common.dao;

import java.util.List;
import com.param.global.common.Response;
import com.param.global.org.common.dto.EmployeeSurveyDto;
import com.param.global.org.common.dto.EmployeeSurveyListDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IEmployeeSurveyDao {
	public Response saveEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto)  throws ApplicationException;

	public Response updateEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto) throws ApplicationException;

	public Response getEmployeeSurveyById(Integer userId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response inactiveSavedEmployeeSurveyList(List <EmployeeSurveyListDto> employeeSurveyListDto);
	
}
