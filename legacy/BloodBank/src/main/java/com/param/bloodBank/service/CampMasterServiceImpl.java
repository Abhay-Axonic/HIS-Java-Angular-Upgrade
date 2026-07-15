package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.bloodBank.dao.ICampMasterDao;
import com.param.bloodBank.dto.CampMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class CampMasterServiceImpl implements ICampMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(CampMasterServiceImpl.class);


	@Autowired
	ICampMasterDao iCampMasterDao;
	
	@Override
	public Response saveCampMaster(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			return iCampMasterDao.saveCampMaster(campMasterDto);
		} catch (Exception e) {
			logger.error("saveCampMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCampMasterList(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			return iCampMasterDao.getCampMasterList(campMasterDto);
		} catch (Exception e) {
			logger.error("getCampMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCampMasterStatus(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			return iCampMasterDao.updateCampMasterStatus(campMasterDto);
		} catch (Exception e) {
			logger.error("updateCampMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCampMaster(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			return iCampMasterDao.updateCampMaster(campMasterDto);
		} catch (Exception e) {
			logger.error("updateCampMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCampMasterListPaginationList(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			
			return iCampMasterDao.getCampMasterListPaginationList(campMasterDto);
	
	} catch (Exception e) {
		logger.error("getCampMasterListPaginationList failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getCampMasterListCount(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			
			return iCampMasterDao.getCampMasterListCount(campMasterDto);
	
	} catch (Exception e) {
		logger.error("getCampMasterListCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}
