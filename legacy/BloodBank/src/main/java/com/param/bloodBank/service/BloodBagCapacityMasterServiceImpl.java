package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBagCapacityMasterDao;
import com.param.bloodBank.dto.BloodBagCapacityMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodBagCapacityMasterServiceImpl implements IBloodBagCapacityMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCapacityMasterServiceImpl.class);


	@Autowired
	IBloodBagCapacityMasterDao iBloodBagCapacityMasterDao;

	@Override
	public Response saveBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBagCapacityMasterDao.getBloodBagCapacityMasterByName(bloodBagCapacityMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBagCapacityMasterDao.saveBloodBagCapacityMaster(bloodBagCapacityMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodBagCapacityMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBagCapacityMasterDao.getBloodBagCapacityMasterByNameNotId(bloodBagCapacityMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBagCapacityMasterDao.updateBloodBagCapacityMaster(bloodBagCapacityMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodBagCapacityMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBagCapacityMasterStatus(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodBagCapacityMasterDao.updateBloodBagCapacityMasterStatus(bloodBagCapacityMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodBagCapacityMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBagCapacityMasterList(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {

				return iBloodBagCapacityMasterDao.getBloodBagCapacityMasterList(bloodBagCapacityMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodBagCapacityMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBagCapacityMasterById(Integer bloodBagCapacityId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodBagCapacityMasterDao.getBloodBagCapacityMasterById(bloodBagCapacityId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodBagCapacityMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodBagCapacityMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodBagCapacityMasterDao.getActiveBloodBagCapacityMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodBagCapacityMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagCapacityMasterCount(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagCapacityMasterDao.getBloodBagCapacityMasterCount(bloodBagCapacityMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagCapacityMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagCapacityMasterByName(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagCapacityMasterDao.getBloodBagCapacityMasterByName(bloodBagCapacityMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagCapacityMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagCapacityMasterByNameNotId(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagCapacityMasterDao.getBloodBagCapacityMasterByNameNotId(bloodBagCapacityMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagCapacityMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}


