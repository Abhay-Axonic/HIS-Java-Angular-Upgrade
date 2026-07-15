package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IDietTypeMasterDao;
import com.param.global.master.dto.DietTypeMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTypeMasterServiceImpl implements IDietTypeMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTypeMasterServiceImpl.class);

	@Autowired
	IDietTypeMasterDao iDietTypeMasterDao;

	@Override
	public Response saveDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			Response dietTypeResponse = iDietTypeMasterDao.getDietTypeByName(dietTypeMasterDto);
			if (dietTypeResponse.getListObject() != null && dietTypeResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iDietTypeMasterDao.saveDietTypeMaster(dietTypeMasterDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterById(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {

			return iDietTypeMasterDao.getDietTypeMasterById(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietTypeMaster(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			iDietTypeMasterDao.updateDietTypeMaster(dietTypeMasterDto);
			return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietTypeMasterStatus(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			return iDietTypeMasterDao.updateDietTypeMasterStatus(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {
			return iDietTypeMasterDao.getDietTypeMasterList(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return iDietTypeMasterDao.getAllDietTypeMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDietTypeMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return iDietTypeMasterDao.getActiveDietTypeMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTypeMasterPaginationList(DietTypeMasterDto dietTypeMasterDto) throws ApplicationException {
		try {

			return iDietTypeMasterDao.getDietTypeMasterPaginationList(dietTypeMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getDietTypeMasterPaginationListCount(DietTypeMasterDto dietTypeMasterDto)
			throws ApplicationException {
		try {

			return iDietTypeMasterDao.getDietTypeMasterPaginationListCount(dietTypeMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

}
