package com.param.pharmacy.qms.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dao.IQMSDeptMasterDao;
import com.param.pharmacy.qms.dto.QMSDeptMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class QMSDeptMasterServiceImpl implements IQMSDeptMasterService,ICommonConstants {

	@Autowired
	IQMSDeptMasterDao iQMSDeptMasterDao;

	private static final Logger logger = Logger.getLogger(QMSDeptMasterServiceImpl.class);


	@Override
	public Response saveQMSDeptMaster(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			Response res=iQMSDeptMasterDao.getQMSDeptMasterByName(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{				
				return iQMSDeptMasterDao.saveQMSDeptMaster(dto);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSDeptMaster(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			Response res=iQMSDeptMasterDao.getQMSDeptMasterByNameNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iQMSDeptMasterDao.updateQMSDeptMaster(dto);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSDeptMasterStatus(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.updateQMSDeptMasterStatus(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterList(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getQMSDeptMasterList(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterCount(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getQMSDeptMasterCount(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSDeptMasterById(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getQMSDeptMasterById(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveQMSDeptMasterList(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getActiveQMSDeptMasterList(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getReceptionListByKiosk(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getReceptionListByKiosk(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDeptListByKioskId(QMSDeptMasterDto dto) throws ApplicationException {
		try {
			return iQMSDeptMasterDao.getActiveDeptListByKioskId(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
