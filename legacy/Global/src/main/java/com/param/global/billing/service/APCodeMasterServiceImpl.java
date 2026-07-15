package com.param.global.billing.service;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IAPCodeMasterDao;
import com.param.global.billing.dto.APCodeMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class APCodeMasterServiceImpl implements IAPCodeMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(APCodeMasterServiceImpl.class);

	@Autowired
	IAPCodeMasterDao iAPCodeMasterDao;

	@Override
	public Response saveAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			Response response=iAPCodeMasterDao.getAPCodeMasterByName(aPCodeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iAPCodeMasterDao.saveAPCodeMaster(aPCodeMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@Override
	public Response updateAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			Response response=iAPCodeMasterDao.getAPCodeMasterByNameNotId(aPCodeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iAPCodeMasterDao.updateAPCodeMaster(aPCodeMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@Override
	public Response updateAPCodeMasterStatus(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			return iAPCodeMasterDao.updateAPCodeMasterStatus(aPCodeMasterDto);

		} catch (Exception e) {
			logger.error("updateAPCodeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAPCodeMasterList(Integer orgId, Integer unitId,  Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getAPCodeMasterList(orgId, unitId, offset, noOfRecordsPerPage);

		} catch (Exception e) {
			logger.error("getAPCodeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAPCodeMasterById(Integer aPCodeId,Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getAPCodeMasterById(aPCodeId,orgId,unitId);

		} catch (Exception e) {
			logger.error("getAPCodeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAPCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getAPCodeMasterCount(orgId, unitId);

		} catch (Exception e) {
			logger.error("getAPCodeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveAPCodeMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getActiveAPCodeMaster(orgId, unitId);

		} catch (Exception e) {
			logger.error("getActiveAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAPCodeMasterByName(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getAPCodeMasterByName(aPCodeMasterDto);

		} catch (Exception e) {
			logger.error("getAPCodeMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAPCodeMasterByNameNotId(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			return iAPCodeMasterDao.getAPCodeMasterByNameNotId(aPCodeMasterDto);

		} catch (Exception e) {
			logger.error("getAPCodeMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
