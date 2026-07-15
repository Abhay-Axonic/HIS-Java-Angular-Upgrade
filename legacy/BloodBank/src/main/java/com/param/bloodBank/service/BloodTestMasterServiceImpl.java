package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodTestMasterDao;
import com.param.bloodBank.dto.BloodTestMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodTestMasterServiceImpl implements IBloodTestMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTestMasterServiceImpl.class);

	@Autowired
	IBloodTestMasterDao iBloodTestMasterDao;

	@Override
	public Response saveBloodTestMaster(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodTestMasterDao.getBloodTestMasterByName(bloodTestMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodTestMasterDao.saveBloodTestMaster(bloodTestMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBloodTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodTestMaster(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			Response response=iBloodTestMasterDao.getBloodTestMasterByNameNotId(bloodTestMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iBloodTestMasterDao.updateBloodTestMaster(bloodTestMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateBloodTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodTestMasterStatus(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			
				return iBloodTestMasterDao.updateBloodTestMasterStatus(bloodTestMasterDto);
			
		} catch (Exception e) {
			logger.error("updateBloodTestMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodTestMasterList(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {

				return iBloodTestMasterDao.getBloodTestMasterList(bloodTestMasterDto);
			
		} catch (Exception e) {
			logger.error("getBloodTestMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getBloodTestMasterById(Integer bloodTestId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iBloodTestMasterDao.getBloodTestMasterById(bloodTestId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getBloodTestMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodTestMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iBloodTestMasterDao.getActiveBloodTestMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveBloodTestMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestMasterCount(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestMasterDao.getBloodTestMasterCount(bloodTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestMasterByName(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestMasterDao.getBloodTestMasterByName(bloodTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getBloodTestMasterByNameNotId(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {

			return iBloodTestMasterDao.getBloodTestMasterByNameNotId(bloodTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getBloodTestMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}



