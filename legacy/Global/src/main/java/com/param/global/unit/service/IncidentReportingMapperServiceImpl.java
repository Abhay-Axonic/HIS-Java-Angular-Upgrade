package com.param.global.unit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.unit.dao.IIncidentReportingMapperDao;
import com.param.global.unit.dao.IIncidentReportingMapperDao;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class IncidentReportingMapperServiceImpl implements IIncidentReportingMapperService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(IncidentReportingMapperServiceImpl.class);

	
	@Autowired
	IIncidentReportingMapperDao iIncidentReportingMapperDao;
	
	@Override
	public Response saveIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.saveIncidentReportingMapper(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("saveIncidentReportingMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.updateIncidentReportingMapper(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("updateIncidentReportingMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportingMapperById(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.getIncidentReportingMapperById(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("getIncidentReportingMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.getIncidentReportingMapperList(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("getIncidentReportingMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateIncidentReportingMapperStatus(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.updateIncidentReportingMapperStatus(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("updateIncidentReportingMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.getActiveIncidentReportingMapperList(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("getActiveIncidentReportingMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportingMapperCount(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.getIncidentReportingMapperCount(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("getIncidentReportingMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoleWiseUserList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			return iIncidentReportingMapperDao.getRoleWiseUserList(incidentReportingMapperDto);

		} catch (Exception e) {
			logger.error("getRoleWiseUserList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
