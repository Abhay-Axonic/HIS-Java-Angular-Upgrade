package com.param.CSSDMaster.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSDMaster.dao.ISterilizationTypeMasterDao;
import com.param.CSSDMaster.dto.SterilizationTypeMasterDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationTypeMasterServiceImpl implements ISterilizationTypeMasterService {

	final static Logger logger = Logger.getLogger(SterilizationTypeMasterServiceImpl.class);


	@Autowired
	private ISterilizationTypeMasterDao iMasterDao;

	@Override
	@Transactional
	public Response updateMaster(SterilizationTypeMasterDto masterDto) {
		try {
			return iMasterDao.updateSterilizationType(masterDto);
		} catch (Exception e) {
			logger.error("updateMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateMasterStatus(SterilizationTypeMasterDto masterDto) {
		try {
			return iMasterDao.updateSterilizationTypeStatus(masterDto);
		} catch (Exception e) {
			logger.error("updateMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getMasterList(Integer organizationId,Integer unitId ,Integer offset,Integer noOfRecordsPerPage) {
		try {
			return iMasterDao.getSterilizationTypeList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getActiveMasterList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getActiveSterilizationTypeList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createMaster(SterilizationTypeMasterDto masterDto) {
		try {
			return iMasterDao.createSterilizationType(masterDto);
		} catch (Exception e) {
			logger.error("createMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSterilizationTypesListCount(SterilizationTypeMasterDto sterilizationTypeMasterDto) {
		try {
			return iMasterDao.getSterilizationTypesListCount(sterilizationTypeMasterDto);
		} catch (Exception e) {
			logger.error("getSterilizationTypesListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
