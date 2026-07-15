package com.param.global.unit.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IEmployeeCategoryMasterDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IEmployeeVaccinationDao;
import com.param.global.unit.dto.EmployeeVaccinationDto;
import com.param.global.unit.dto.UserRoleMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class EmployeeVaccinationServiceImp implements IEmployeeVaccinationService, ICommonConstants{
	final static Logger logger = Logger.getLogger(EmployeeVaccinationServiceImp.class);

@Autowired
IEmployeeVaccinationDao iEmployeeVaccinationDao;

	@Override
	@Transactional
	public Response saveEmployeeVaccination(EmployeeVaccinationDto employeeVaccinationDto) {
		
		try
		{
			Response res = iEmployeeVaccinationDao.saveEmployeeVaccination(employeeVaccinationDto);
		    return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	}

 catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

}

	@Override
	@Transactional
	public Response searchEmployeeVaccinationByRole(EmployeeVaccinationDto employeeVaccinationDto) {
		try {
			return iEmployeeVaccinationDao.searchEmployeeVaccinationByRole(employeeVaccinationDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getVaccination(EmployeeVaccinationDto employeeVaccinationDto)
			throws in.param.exception.ApplicationException {
		try {
			return iEmployeeVaccinationDao.getVaccination(employeeVaccinationDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
}

	


