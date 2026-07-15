package com.param.kitchen.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dao.IDietPlanningDao;
import com.param.kitchen.dto.DietPlanningDto;
import com.param.global.common.ICommonConstants;
import in.param.exception.ApplicationException;

@Service
public class DietPlanningServiceImpl implements IDietPlanningService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietPlanningServiceImpl.class);

	@Autowired
	IDietPlanningDao iDietPlanningDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response saveDietPlanning(DietPlanningDto dietPlanningDto)
			throws ApplicationException {
		try {
			return iDietPlanningDao.saveDietPlanning(dietPlanningDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getAllDietPlanning()
			throws ApplicationException {
		try {
			return iDietPlanningDao.getAllDietPlanning();
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}*/
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getDietPlanningByPatientId(Integer patientId)
			throws ApplicationException {
		try {
			return iDietPlanningDao.getDietPlanningByPatientId(patientId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}*/
	
	

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response updateDietPlanning(DietPlanningDto dietPlanningDto)
			throws ApplicationException {
		try {
			return iDietPlanningDao.updateDietPlanning(dietPlanningDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}*/
	

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response deleteDietPlanning(Integer patientId)
			throws ApplicationException {
		try {
			return iDietPlanningDao.deleteDietPlanning(patientId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(com.param.global.common.ICommonConstants.ERROR,com.param.global.common.ICommonConstants.COMMON_ERROR_CODE, null, null, null);
	}*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getDietPreferenceByPatientId(Integer patientId)
			throws ApplicationException {
		try {
			return iDietPlanningDao.getDietPreferenceByPatientId(patientId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
