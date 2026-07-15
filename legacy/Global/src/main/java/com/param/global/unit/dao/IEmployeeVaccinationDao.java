package com.param.global.unit.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.unit.dto.EmployeeVaccinationDto;
@SuppressWarnings({"rawtypes"})
public interface IEmployeeVaccinationDao {
	public Response saveEmployeeVaccination(EmployeeVaccinationDto employeeVaccinationDto) throws ApplicationException;
	public Response searchEmployeeVaccinationByRole(EmployeeVaccinationDto employeeVaccinationDto);
	public Response getVaccination(EmployeeVaccinationDto employeeVaccinationDto)throws ApplicationException;

}
