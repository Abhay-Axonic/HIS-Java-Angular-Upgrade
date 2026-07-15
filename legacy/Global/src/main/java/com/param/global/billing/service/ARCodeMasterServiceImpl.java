package com.param.global.billing.service;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IARCodeMasterDao;
import com.param.global.billing.dto.ARCodeMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class ARCodeMasterServiceImpl implements IARCodeMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(ARCodeMasterServiceImpl.class);

	@Autowired
	IARCodeMasterDao iARCodeMasterDao;
	@Override
	public Response saveARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			Response response=iARCodeMasterDao.getARCodeMasterByName(aRCodeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iARCodeMasterDao.saveARCodeMaster(aRCodeMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			Response response=iARCodeMasterDao.getARCodeMasterByNameNotId(aRCodeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iARCodeMasterDao.updateARCodeMaster(aRCodeMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateARCodeMasterStatus(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			return iARCodeMasterDao.updateARCodeMasterStatus(aRCodeMasterDto);

		} catch (Exception e) {
			logger.error("updateARCodeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getARCodeMasterList(Integer orgId, Integer unitId,  Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			return iARCodeMasterDao.getARCodeMasterList(orgId, unitId, offset, noOfRecordsPerPage);

		} catch (Exception e) {
			logger.error("getARCodeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getARCodeMasterById(Integer aRCodeId, Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iARCodeMasterDao.getARCodeMasterById(aRCodeId, orgId, unitId);

		} catch (Exception e) {
			logger.error("getARCodeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getARCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iARCodeMasterDao.getARCodeMasterCount(orgId, unitId);

		} catch (Exception e) {
			logger.error("getARCodeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getActiveARCodeMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iARCodeMasterDao.getActiveARCodeMaster(orgId,unitId);

		} catch (Exception e) {
			logger.error("getActiveARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getARCodeMasterByName(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			return iARCodeMasterDao.getARCodeMasterByName(aRCodeMasterDto);

		} catch (Exception e) {
			logger.error("getARCodeMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getARCodeMasterByNameNotId(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			return iARCodeMasterDao.getARCodeMasterByNameNotId(aRCodeMasterDto);

		} catch (Exception e) {
			logger.error("getARCodeMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
