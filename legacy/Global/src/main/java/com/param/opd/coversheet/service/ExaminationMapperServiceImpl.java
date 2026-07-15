package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IExaminationMapperDao;
import com.param.opd.coversheet.dto.ExaminationMapperDto;
import com.param.global.common.ICommonConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class ExaminationMapperServiceImpl implements IExaminationMapperService, ICommonConstants ,IError{
	final static Logger logger = Logger.getLogger(ExaminationMapperServiceImpl.class);


	
	@Autowired
	private IExaminationMapperDao iExaminationMapperDao;

	@Override
	public Response saveExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {
			Response res=iExaminationMapperDao.checkAvailableExaminationMapper(examinationMapperDto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iExaminationMapperDao.saveExaminationMapper(examinationMapperDto);
			}
			
		} catch (Exception e) {
		logger.error("saveExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {

			return iExaminationMapperDao.updateExaminationMapper(examinationMapperDto);

		} catch (Exception e) {
		logger.error("updateExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getExaminationMapperById(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {

			return iExaminationMapperDao.getExaminationMapperById(examinationMapperDto);

		} catch (Exception e) {
		logger.error("getExaminationMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getExaminationMapperList(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {

			return iExaminationMapperDao.getExaminationMapperList(examinationMapperDto);

		} catch (Exception e) {
		logger.error("getExaminationMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMostlyUsedExaminationMapperListFor(ExaminationMapperDto examinationMapperDto)
			throws ApplicationException {
		try {

			return iExaminationMapperDao.getMostlyUsedExaminationMapperListFor(examinationMapperDto);

		} catch (Exception e) {
		logger.error("getMostlyUsedExaminationMapperListFor failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response removeExaminationMapper(ExaminationMapperDto examinationMapperDto) throws ApplicationException {
		try {

			return iExaminationMapperDao.removeExaminationMapper(examinationMapperDto);

		} catch (Exception e) {
		logger.error("removeExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
