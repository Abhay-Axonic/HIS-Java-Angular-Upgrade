package com.param.pharmacy.qms.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dao.IQMSKioskMasterDao;
import com.param.pharmacy.qms.dto.QMSKioskMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class QMSKioskMasterServiceImpl implements IQMSKioskMasterService, ICommonConstants {

	@Autowired
	IQMSKioskMasterDao iQMSKioskMasterDao;

	private static final Logger logger = Logger.getLogger(QMSKioskMasterServiceImpl.class);


	@Override
	public Response saveQMSMaster(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			Response res =iQMSKioskMasterDao.getQMSMasterListByName(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iQMSKioskMasterDao.saveQMSMaster(dto);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSMaster(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			Response res =iQMSKioskMasterDao.getQMSMasterListByNameNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iQMSKioskMasterDao.updateQMSMaster(dto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSMasterStatus(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			return iQMSKioskMasterDao.updateQMSMasterStatus(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterList(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			return iQMSKioskMasterDao.getQMSMasterList(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterCount(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			return iQMSKioskMasterDao.getQMSMasterCount(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterById(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			return iQMSKioskMasterDao.getQMSMasterById(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveQMSMasterList(QMSKioskMasterDto dto) throws ApplicationException {
		try{
			return iQMSKioskMasterDao.getActiveQMSMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
