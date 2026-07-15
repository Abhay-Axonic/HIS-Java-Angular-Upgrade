package com.param.bloodBank.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodGroupMasterDao;
import com.param.bloodBank.dto.BloodGroupMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodGroupMasterServiceImpl implements IBloodGroupMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodGroupMasterServiceImpl.class);

	@Autowired
	IBloodGroupMasterDao iBloodGroupMasterDao;

	@Override
	@Transactional
	public Response saveBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodGroupMasterDao.getBloodGroupMasterByName(bloodGroupMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodGroupMasterDao.saveBloodGroupMaster(bloodGroupMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodGroupMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodGroupMasterDao.getBloodGroupMasterByNameNotId(bloodGroupMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodGroupMasterDao.updateBloodGroupMaster(bloodGroupMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodGroupMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodGroupMasterStatus(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodGroupMasterDao.updateBloodGroupMasterStatus(bloodGroupMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodGroupMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodGroupMasterList(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {

				return iBloodGroupMasterDao.getBloodGroupMasterList(bloodGroupMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodGroupMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodGroupMasterById(Integer bloodGroupId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodGroupMasterDao.getBloodGroupMasterById(bloodGroupId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodGroupMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodGroupMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodGroupMasterDao.getActiveBloodGroupMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodGroupMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodGroupMasterCount(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {

			return iBloodGroupMasterDao.getBloodGroupMasterCount(bloodGroupMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodGroupMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	
}




