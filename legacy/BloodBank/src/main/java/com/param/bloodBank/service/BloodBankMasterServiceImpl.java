package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBankMasterDao;
import com.param.bloodBank.dto.BloodBankMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})

public class BloodBankMasterServiceImpl implements IBloodBankMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankMasterServiceImpl.class);

	@Autowired
	IBloodBankMasterDao iBloodBankMasterDao;

	@Override
	public Response saveBloodBankMaster(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBankMasterDao.getBloodBankMasterByName(bloodBankMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBankMasterDao.saveBloodBankMaster(bloodBankMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodBankMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBankMaster(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodBankMasterDao.getBloodBankMasterByNameNotId(bloodBankMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodBankMasterDao.updateBloodBankMaster(bloodBankMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodBankMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBankMasterStatus(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodBankMasterDao.updateBloodBankMasterStatus(bloodBankMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodBankMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBankMasterList(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {

				return iBloodBankMasterDao.getBloodBankMasterList(bloodBankMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodBankMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodBankMasterById(Integer bloodBankId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodBankMasterDao.getBloodBankMasterById(bloodBankId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodBankMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodBankMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodBankMasterDao.getActiveBloodBankMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodBankMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBankMasterCount(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {

			return iBloodBankMasterDao.getBloodBankMasterCount(bloodBankMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBankMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBankMasterByName(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {

			return iBloodBankMasterDao.getBloodBankMasterByName(bloodBankMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBankMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodBankMasterByNameNotId(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {

			return iBloodBankMasterDao.getBloodBankMasterByNameNotId(bloodBankMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodBankMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}

