package com.param.global.unit.service;

import com.param.global.common.Response;
import com.param.global.unit.dto.EmployeeVaccinationDto;
import com.param.global.unit.dto.UserRoleMasterDto;

import in.param.exception.ApplicationException;

public interface IEmployeeVaccinationService {

	Response saveEmployeeVaccination(EmployeeVaccinationDto employeeVaccinationDto);

	Response searchEmployeeVaccinationByRole(EmployeeVaccinationDto employeeVaccinationDto);

	Response getVaccination(EmployeeVaccinationDto employeeVaccinationDto)throws ApplicationException;

}
