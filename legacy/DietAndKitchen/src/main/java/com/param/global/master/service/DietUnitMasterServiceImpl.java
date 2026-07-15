package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IDietUnitMasterDao;
import com.param.global.master.dto.UnitOfMeasurmentMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietUnitMasterServiceImpl implements IDietUnitMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(DietUnitMasterServiceImpl.class);

	@Autowired
	IDietUnitMasterDao iDietUnitMasterDao;

	@Override
	public Response saveDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iDietUnitMasterDao.checkAvailableUnitByName(unitOfMeasurmentMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iDietUnitMasterDao.saveDietUnitMaster(unitOfMeasurmentMasterDto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateDietUnitMaster(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
		Response res=iDietUnitMasterDao.checkAvailableUnitById(unitOfMeasurmentMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDietUnitMasterDao.updateDietUnitMaster(unitOfMeasurmentMasterDto);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDietUnitMasterStatus(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
		try {

				return iDietUnitMasterDao.updateDietUnitMasterStatus(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietUnitMasterById(Integer cabinId, Integer organizationId) throws ApplicationException {
			try {

				return iDietUnitMasterDao.getDietUnitMasterById(cabinId, organizationId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietUnitMasterList(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
			try {

				return iDietUnitMasterDao.getDietUnitMasterList(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietUnitMasterCount(UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) throws ApplicationException{
			try {

				return iDietUnitMasterDao.getDietUnitMasterCount(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDietUnitMaster(Integer organizationId) throws ApplicationException{
			try {

				return iDietUnitMasterDao.getActiveDietUnitMaster(organizationId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


}
