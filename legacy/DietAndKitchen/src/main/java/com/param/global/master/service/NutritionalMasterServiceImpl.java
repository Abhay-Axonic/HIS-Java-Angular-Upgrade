package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.INutritionalMasterDao;
import com.param.global.master.dto.NutritionalMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NutritionalMasterServiceImpl implements INutritionalMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(NutritionalMasterServiceImpl.class);

	@Autowired
	INutritionalMasterDao inutritionalMasterDao;

	public Response saveNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {

		try {
			Response deptResponse = inutritionalMasterDao.getNutritionByName(nutritionalMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				return inutritionalMasterDao.saveNutritionalMaster(nutritionalMasterDto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionalMasterList(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			return inutritionalMasterDao.getNutritionalMasterList(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			Response res=inutritionalMasterDao.checkNutritionByIdNotByName(nutritionalMasterDto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}else{
				return inutritionalMasterDao.updateNutritionalMaster(nutritionalMasterDto);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionalMasterById(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			return inutritionalMasterDao.getNutritionalMasterById(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllNutritionalMasterList(Integer organizationId,Integer unitId , Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			return inutritionalMasterDao.getAllNutritionalMasterList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveNutritionalMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return inutritionalMasterDao.getActiveNutritionalMasterList(organizationId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateNutritionMasterStatus(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			return inutritionalMasterDao.updateNutritionMasterStatus(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionMasterCount(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			return inutritionalMasterDao.getNutritionMasterCount(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
