package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IExaminationMasterDao;
import com.param.global.dto.ExaminationMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExaminationMasterServiceImpl implements IExaminationMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(ExaminationMasterServiceImpl.class);

	@Autowired
	private IExaminationMasterDao iExaminationMasterDao;

	@Override
	public Response saveExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			return iExaminationMasterDao.saveExaminationMaster(examinationMasterDto);
		} catch (Exception e) {
		logger.error("saveExaminationMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			return iExaminationMasterDao.updateExaminationMaster(examinationMasterDto);
		} catch (Exception e) {
		logger.error("updateExaminationMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateExaminationMasterStatus(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			return iExaminationMasterDao.updateExaminationMasterStatus(examinationMasterDto);
		} catch (Exception e) {
		logger.error("updateExaminationMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getExaminationMasterById(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			return iExaminationMasterDao.getExaminationMasterById(examinationMasterDto);
		} catch (Exception e) {
		logger.error("getExaminationMasterById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getExaminationMasterList(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			return iExaminationMasterDao.getExaminationMasterList(examinationMasterDto);
		} catch (Exception e) {
		logger.error("getExaminationMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			return iExaminationMasterDao.getExaminationMasterActiveList(examinationMasterDto);
		} catch (Exception e) {
		logger.error("getExaminationMasterActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getExaminationMasterCount(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			return iExaminationMasterDao.getExaminationMasterCount(examinationMasterDto);
		} catch (Exception e) {
		logger.error("getExaminationMasterCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			return iExaminationMasterDao.searchExaminationMasterActiveList(examinationMasterDto);
		} catch (Exception e) {
		logger.error("searchExaminationMasterActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
