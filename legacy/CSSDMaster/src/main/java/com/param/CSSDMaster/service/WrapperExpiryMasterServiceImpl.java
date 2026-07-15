package com.param.CSSDMaster.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSDMaster.dao.IWrapperExpiryMasterDao;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WrapperExpiryMasterServiceImpl implements IWrapperExpiryMasterService {

	final static Logger logger = Logger.getLogger(WrapperExpiryMasterServiceImpl.class);


	@Autowired
	private IWrapperExpiryMasterDao iMasterDao;

	@Override
	@Transactional
	public Response updateMaster(WrapperExpiryMasterDto masterDto) {
		try {
			return iMasterDao.updateWrapperExpiry(masterDto);
		} catch (Exception e) {
			logger.error("updateMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateMasterStatus(WrapperExpiryMasterDto masterDto) {
		try {
			return iMasterDao.updateWrapperExpiryStatus(masterDto);
		} catch (Exception e) {
			logger.error("updateMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) {
		try {
			return iMasterDao.getWrapperExpiryList(organizationId,unitId,offset, noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getItemList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getItemList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getItemList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getActiveMasterList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getActiveWrapperExpiryList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createMaster(WrapperExpiryMasterDto masterDto) {
		try {
			return iMasterDao.createWrapperExpiry(masterDto);
		} catch (Exception e) {
			logger.error("createMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getActiveWrapperExpiryByCode(Integer organizationId,Integer unitId,String code) {
		try {
			return iMasterDao.getActiveWrapperExpiryByCode(organizationId,unitId,code);
		} catch (Exception e) {
			logger.error("getActiveWrapperExpiryByCode failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getWrapperListcount(WrapperExpiryMasterDto wrapperExpiryMasterDto) {
		try {
			return iMasterDao.getWrapperListcount(wrapperExpiryMasterDto);
		} catch (Exception e) {
			logger.error("getWrapperListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
