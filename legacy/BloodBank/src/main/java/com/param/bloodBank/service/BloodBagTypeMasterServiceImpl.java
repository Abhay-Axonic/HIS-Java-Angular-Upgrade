package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBagTypeMasterDao;
import com.param.bloodBank.dto.BloodBagTypeMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodBagTypeMasterServiceImpl implements IBloodBagTypeMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagTypeMasterServiceImpl.class);

	@Autowired
	IBloodBagTypeMasterDao iBloodBagTypeMasterDao;

	@Override
	public Response saveBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBagTypeMasterDao.getBloodBagTypeMasterByName(bloodBagTypeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBagTypeMasterDao.saveBloodBagTypeMaster(bloodBagTypeMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodBagTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBagTypeMasterDao.getBloodBagTypeMasterByNameNotId(bloodBagTypeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBagTypeMasterDao.updateBloodBagTypeMaster(bloodBagTypeMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodBagTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBagTypeMasterStatus(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodBagTypeMasterDao.updateBloodBagTypeMasterStatus(bloodBagTypeMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodBagTypeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBagTypeMasterList(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {

				return iBloodBagTypeMasterDao.getBloodBagTypeMasterList(bloodBagTypeMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodBagTypeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBagTypeMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodBagTypeMasterDao.getBloodBagTypeMasterById(bloodBagTypeId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodBagTypeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodBagTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodBagTypeMasterDao.getActiveBloodBagTypeMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodBagTypeMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagTypeMasterCount(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagTypeMasterDao.getBloodBagTypeMasterCount(bloodBagTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagTypeMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagTypeMasterByName(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagTypeMasterDao.getBloodBagTypeMasterByName(bloodBagTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagTypeMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBagTypeMasterByNameNotId(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodBagTypeMasterDao.getBloodBagTypeMasterByNameNotId(bloodBagTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBagTypeMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}

