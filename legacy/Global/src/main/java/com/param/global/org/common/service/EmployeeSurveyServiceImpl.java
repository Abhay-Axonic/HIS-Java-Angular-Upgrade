package com.param.global.org.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.adt.employeesurvey.EmployeeSurvey;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IEmployeeSurveyDao;
import com.param.global.org.common.dto.EmployeeSurveyDto;
import com.param.global.org.common.dto.EmployeeSurveyListDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class EmployeeSurveyServiceImpl implements IEmployeeSurveyService, ICommonConstants {
	final static Logger logger = Logger.getLogger(EmployeeSurveyServiceImpl.class);

    
    @Autowired
    private IEmployeeSurveyDao iEmployeeSurveyDao;

    @Transactional
    @Override
    public Response saveEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto) throws ApplicationException {
    	 try {
    		 
             Response getListResponse = iEmployeeSurveyDao.getEmployeeSurveyById(employeeSurveyDto.getUserId(),employeeSurveyDto.getOrganizationId(),employeeSurveyDto.getUnitId());
             if(!getListResponse.getListObject().isEmpty()){
            	 iEmployeeSurveyDao.inactiveSavedEmployeeSurveyList(getListResponse.getListObject());
             }	 
             return iEmployeeSurveyDao.saveEmployeeSurvey(employeeSurveyDto);
    	
        } catch (Exception e) {
            throw e;
        }
    
    }

	@Override
	public Response getEmployeeSurveyById(Integer userId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
			return iEmployeeSurveyDao.getEmployeeSurveyById(userId, organizationId, unitId);
	
	} catch (Exception e) {
	logger.error("getEmployeeSurveyById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}

	


