package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IDiagnosticProcedureConfigDao;
import com.param.global.unit.dto.DiagnosticProcedureConfigDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class DiagnosticProcedureConfigServiceImpl implements IDiagnosticProcedureConfigService,ICommonConstants{
	final static Logger logger = Logger.getLogger(DiagnosticProcedureConfigServiceImpl.class);


	@Autowired
	private IDiagnosticProcedureConfigDao iDiagnosticProcedureConfigDao;
	
	@Override
	public Response saveDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			Response res=iDiagnosticProcedureConfigDao.getProcedureConfigByServiceId(diagnosticProcedureConfigDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDiagnosticProcedureConfigDao.saveDiagnosticProcedureConfig(diagnosticProcedureConfigDto);
			}
			
		}catch (Exception e) {
		logger.error("saveDiagnosticProcedureConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigList(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			return iDiagnosticProcedureConfigDao.getDiagnosticProcedureConfigList(diagnosticProcedureConfigDto);
		}catch(Exception e) {
		logger.error("getDiagnosticProcedureConfigList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			Response res=iDiagnosticProcedureConfigDao.getProcedureConfigByNameNotId(diagnosticProcedureConfigDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDiagnosticProcedureConfigDao.updateDiagnosticProcedureConfig(diagnosticProcedureConfigDto);
			}
		}catch(Exception e) {
		logger.error("updateDiagnosticProcedureConfig failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDiagnosticProcedureConfigStatus(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			return iDiagnosticProcedureConfigDao.updateDiagnosticProcedureConfigStatus(diagnosticProcedureConfigDto);
		}catch (Exception e) {
		logger.error("updateDiagnosticProcedureConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigCount(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			return iDiagnosticProcedureConfigDao.getDiagnosticProcedureConfigCount(diagnosticProcedureConfigDto);
		}catch (Exception e) {
		logger.error("getDiagnosticProcedureConfigCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigById(Integer diagnosticProcedureConfigId, Integer organizationId,
			Integer unitId) {
		try {
			return iDiagnosticProcedureConfigDao.getDiagnosticProcedureConfigById(diagnosticProcedureConfigId,organizationId,unitId);
		}catch (Exception e) {
		logger.error("getDiagnosticProcedureConfigById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	
}
