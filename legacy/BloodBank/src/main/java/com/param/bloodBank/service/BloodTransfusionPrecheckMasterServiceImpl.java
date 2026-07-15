package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodTransfusionPrecheckMasterDao;
import com.param.bloodBank.dto.BloodTransfusionPrecheckMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodTransfusionPrecheckMasterServiceImpl implements IBloodTransfusionPrecheckMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTransfusionPrecheckMasterServiceImpl.class);


	@Autowired
	IBloodTransfusionPrecheckMasterDao iBloodTransfusionPrecheckMasterDao;

	@Override
	public Response saveBloodTransfusionPrecheckMaster(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.saveBloodTransfusionPrecheckMaster(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodTransfusionPrecheckMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodTransfusionPrecheckMasterStatus(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.updateBloodTransfusionPrecheckMasterStatus(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodTransfusionPrecheckMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterList(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.getBloodTransfusionPrecheckMasterList(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodTransfusionPrecheckMasterList(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.getActiveBloodTransfusionPrecheckMasterList(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("getActiveBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterListPagination(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.getBloodTransfusionPrecheckMasterListPagination(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListPagination failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterListCount(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			return iBloodTransfusionPrecheckMasterDao.getBloodTransfusionPrecheckMasterListCount(bloodTransfusionPrecheckMasterDto);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
}
