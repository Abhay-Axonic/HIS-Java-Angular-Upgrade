package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodComponentMasterDao;
import com.param.bloodBank.dto.BloodComponentMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodComponentMasterServiceImpl implements IBloodComponentMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentMasterServiceImpl.class);

	@Autowired
	IBloodComponentMasterDao iBloodComponentMasterDao;

	@Override
	public Response saveBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodComponentMasterDao.getBloodComponentMasterByName(bloodComponentMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodComponentMasterDao.saveBloodComponentMaster(bloodComponentMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodComponentMasterDao.getBloodComponentMasterByNameNotId(bloodComponentMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodComponentMasterDao.updateBloodComponentMaster(bloodComponentMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodComponentMasterStatus(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodComponentMasterDao.updateBloodComponentMasterStatus(bloodComponentMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodComponentMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodComponentMasterList(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {

				return iBloodComponentMasterDao.getBloodComponentMasterList(bloodComponentMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodComponentMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodComponentMasterById(Integer bloodComponentId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodComponentMasterDao.getBloodComponentMasterById(bloodComponentId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodComponentMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodComponentMasterDao.getActiveBloodComponentMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodComponentMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodComponentMasterCount(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {

			return iBloodComponentMasterDao.getBloodComponentMasterCount(bloodComponentMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodComponentMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodComponentMasterByName(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {

			return iBloodComponentMasterDao.getBloodComponentMasterByName(bloodComponentMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodComponentMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodComponentMasterByNameNotId(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {

			return iBloodComponentMasterDao.getBloodComponentMasterByNameNotId(bloodComponentMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodComponentMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}

