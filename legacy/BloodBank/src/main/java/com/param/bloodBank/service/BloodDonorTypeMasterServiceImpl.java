package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodDonorTypeMasterDao;
import com.param.bloodBank.dto.BloodDonorTypeMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodDonorTypeMasterServiceImpl implements IBloodDonorTypeMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodDonorTypeMasterServiceImpl.class);

	@Autowired
	IBloodDonorTypeMasterDao iBloodDonorTypeMasterDao;

	@Override
	public Response saveBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodDonorTypeMasterDao.getBloodDonorTypeMasterByName(bloodDonorTypeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodDonorTypeMasterDao.saveBloodDonorTypeMaster(bloodDonorTypeMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodDonorTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodDonorTypeMasterDao.getBloodDonorTypeMasterByNameNotId(bloodDonorTypeMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodDonorTypeMasterDao.updateBloodDonorTypeMaster(bloodDonorTypeMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodDonorTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodDonorTypeMasterStatus(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodDonorTypeMasterDao.updateBloodDonorTypeMasterStatus(bloodDonorTypeMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodDonorTypeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodDonorTypeMasterList(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {

				return iBloodDonorTypeMasterDao.getBloodDonorTypeMasterList(bloodDonorTypeMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodDonorTypeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodDonorTypeMasterById(Integer bloodDonorTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodDonorTypeMasterDao.getBloodDonorTypeMasterById(bloodDonorTypeId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodDonorTypeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodDonorTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodDonorTypeMasterDao.getActiveBloodDonorTypeMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodDonorTypeMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodDonorTypeMasterCount(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodDonorTypeMasterDao.getBloodDonorTypeMasterCount(bloodDonorTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodDonorTypeMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodDonorTypeMasterByName(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodDonorTypeMasterDao.getBloodDonorTypeMasterByName(bloodDonorTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodDonorTypeMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodDonorTypeMasterByNameNotId(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {

			return iBloodDonorTypeMasterDao.getBloodDonorTypeMasterByNameNotId(bloodDonorTypeMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodDonorTypeMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}
