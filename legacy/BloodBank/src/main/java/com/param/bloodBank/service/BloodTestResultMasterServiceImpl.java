package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodTestResultMasterDao;
import com.param.bloodBank.dto.BloodTestResultMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodTestResultMasterServiceImpl implements IBloodTestResultMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTestResultMasterServiceImpl.class);


	@Autowired
	IBloodTestResultMasterDao iBloodTestResultMasterDao;

	@Override
	public Response saveBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodTestResultMasterDao.getBloodTestResultMasterByName(bloodTestResultMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodTestResultMasterDao.saveBloodTestResultMaster(bloodTestResultMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodTestResultMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodTestResultMasterDao.getBloodTestResultMasterByNameNotId(bloodTestResultMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodTestResultMasterDao.updateBloodTestResultMaster(bloodTestResultMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodTestResultMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodTestResultMasterStatus(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodTestResultMasterDao.updateBloodTestResultMasterStatus(bloodTestResultMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodTestResultMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodTestResultMasterList(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {

				return iBloodTestResultMasterDao.getBloodTestResultMasterList(bloodTestResultMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodTestResultMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodTestResultMasterById(Integer bloodTestResultId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodTestResultMasterDao.getBloodTestResultMasterById(bloodTestResultId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodTestResultMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodTestResultMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodTestResultMasterDao.getActiveBloodTestResultMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodTestResultMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestResultMasterCount(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestResultMasterDao.getBloodTestResultMasterCount(bloodTestResultMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestResultMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestResultMasterByName(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestResultMasterDao.getBloodTestResultMasterByName(bloodTestResultMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestResultMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestResultMasterByNameNotId(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestResultMasterDao.getBloodTestResultMasterByNameNotId(bloodTestResultMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestResultMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}

