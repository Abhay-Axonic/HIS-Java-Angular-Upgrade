package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IEmployeeCategoryMasterDao;
import com.param.global.billing.dto.EmployeeCategoryMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.UserRoleMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class EmployeeCategoryMasterServiceImpl implements IEmployeeCategoryMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(EmployeeCategoryMasterServiceImpl.class);

	@Autowired
	IEmployeeCategoryMasterDao iEmployeeCategoryMasterDao;


	@Override
	public Response saveEmployeeCategoryMaster(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		Response response = iEmployeeCategoryMasterDao.getEmployeeCategoryMasterByName(employeeCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iEmployeeCategoryMasterDao.saveEmployeeCategoryMaster(employeeCategoryMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveEmployeeCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryMasterById(int employeeId, int orgId) throws ApplicationException  {
		try {
			return iEmployeeCategoryMasterDao.getEmployeeCategoryMasterById(employeeId, orgId);

		} catch (Exception e) {
			logger.error("getEmployeeCategoryMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public Response getEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto) throws ApplicationException {
		try {
			return iEmployeeCategoryMasterDao.getEmployeeCategoryMasterList(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getEmployeeCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getActiveEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto) throws ApplicationException {
		try {
			return iEmployeeCategoryMasterDao.getActiveEmployeeCategoryMasterList(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getActiveEmployeeCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	@Override
	public Response updateEmployeeCategoryMaster(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException{
		Response response = iEmployeeCategoryMasterDao.getEmployeeCategoryMasterByNameNotId(employeeCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iEmployeeCategoryMasterDao.updateEmployeeCategoryMaster(employeeCategoryMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateEmployeeCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateEmployeeCategoryMasterStatus(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try {
			return iEmployeeCategoryMasterDao.updateEmployeeCategoryMasterStatus(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateEmployeeCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEmployeeCategoryCount(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
		try{
			return iEmployeeCategoryMasterDao.getEmployeeCategoryCount(employeeCategoryMasterDto);
		}catch (Exception e) {
			logger.error("getEmployeeCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllEmployeeCategoryMasterList(EmployeeCategoryMasterDto employeeCategoryMasterDto)
			throws ApplicationException {
			try {
				return iEmployeeCategoryMasterDao.getAllEmployeeCategoryMasterList(employeeCategoryMasterDto);
			} catch (Exception e) {
				logger.error("getAllEmployeeCategoryMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	
}
