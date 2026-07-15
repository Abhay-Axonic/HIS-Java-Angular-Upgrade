package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IScreeningTestMasterDao;
import com.param.bloodBank.dto.ScreeningTestMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class ScreeningTestMasterServiceImpl implements IScreeningTestMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(ScreeningTestMasterServiceImpl.class);

	@Autowired
	IScreeningTestMasterDao iScreeningTestMasterDao;

	@Override
	public Response saveScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			Response response=iScreeningTestMasterDao.getScreeningTestMasterByName(screeningTestMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iScreeningTestMasterDao.saveScreeningTestMaster(screeningTestMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveScreeningTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			Response response=iScreeningTestMasterDao.getScreeningTestMasterByNameNotId(screeningTestMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iScreeningTestMasterDao.updateScreeningTestMaster(screeningTestMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateScreeningTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateScreeningTestMasterStatus(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			
				return iScreeningTestMasterDao.updateScreeningTestMasterStatus(screeningTestMasterDto);
			
		} catch (Exception e) {
			logger.error("updateScreeningTestMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getScreeningTestMasterList(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {

				return iScreeningTestMasterDao.getScreeningTestMasterList(screeningTestMasterDto);
			
		} catch (Exception e) {
			logger.error("getScreeningTestMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getScreeningTestMasterById(Integer screeningTestId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iScreeningTestMasterDao.getScreeningTestMasterById(screeningTestId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getScreeningTestMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveScreeningTestMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iScreeningTestMasterDao.getActiveScreeningTestMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveScreeningTestMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getScreeningTestMasterCount(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {

			return iScreeningTestMasterDao.getScreeningTestMasterCount(screeningTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getScreeningTestMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getScreeningTestMasterByName(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {

			return iScreeningTestMasterDao.getScreeningTestMasterByName(screeningTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getScreeningTestMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getScreeningTestMasterByNameNotId(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {

			return iScreeningTestMasterDao.getScreeningTestMasterByNameNotId(screeningTestMasterDto);
		
	} catch (Exception e) {
		logger.error("getScreeningTestMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}



