package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAllergyMasterDao;
import com.param.global.dao.IFormulationTypeToDosageMapperDao;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.dto.FormulationTypeToDosageMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class FormulationTypeToDosageMapperServiceImpl implements IFormulationTypeToDosageMapperService, ICommonConstants  {
	final static Logger logger = Logger.getLogger(FormulationTypeToDosageMapperServiceImpl.class);

	@Autowired
	private IFormulationTypeToDosageMapperDao iFormulationTypeToDosageMapperDao;

	@Override
	public Response saveFormulationTypeToDosageMapper(FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto)
			throws ApplicationException {
		Response res = iFormulationTypeToDosageMapperDao.getFormulationTypeToDosageMapperByName(formulationTypeToDosageMapperDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iFormulationTypeToDosageMapperDao.saveFormulationTypeToDosageMapper(formulationTypeToDosageMapperDto);
			}
		} catch (Exception e) {
		logger.error("saveFormulationTypeToDosageMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getFormulationTypeToDosageMapperList(
			FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
		try {
			return iFormulationTypeToDosageMapperDao.getFormulationTypeToDosageMapperList(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
		logger.error("getFormulationTypeToDosageMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateFormulationTypeToDosageMapper(
			FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
		Response res = iFormulationTypeToDosageMapperDao.getFormulationTypeToDosageMapperListByNameNotById(formulationTypeToDosageMapperDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iFormulationTypeToDosageMapperDao.updateFormulationTypeToDosageMapper(formulationTypeToDosageMapperDto);
			}
		} catch (Exception e) {
		logger.error("updateFormulationTypeToDosageMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateFormulationTypeToDosageMapperStatus(
			FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
		try {
			return iFormulationTypeToDosageMapperDao.updateFormulationTypeToDosageMapperStatus(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
		logger.error("updateFormulationTypeToDosageMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getFormTypeToDosMappListByFormTypeId(
			FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
		try {
			return iFormulationTypeToDosageMapperDao.getFormTypeToDosMappListByFormTypeId(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
		logger.error("getFormTypeToDosMappListByFormTypeId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
