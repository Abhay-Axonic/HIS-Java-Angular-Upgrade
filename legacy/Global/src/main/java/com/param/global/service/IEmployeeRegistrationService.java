package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.dto.EmployeeRegistrationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IEmployeeRegistrationService {

	@Transactional
	Response saveEmployeeRegistration(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	@Transactional
	Response getEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	@Transactional
	Response updateEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	@Transactional
	Response updateEmployeeStatus(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	@Transactional
	Response searchEmployeeByCriteria(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;
	
	@Transactional
	Response saveEmployeeImage(int orgId,int unitId,int employeeId,MultipartFile inputFile)throws ApplicationException;

	@Transactional
	Response getPlainEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto);
	
	@Transactional
	Response searchEmployeeByName(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response getEmployeeRegistrationCount(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Response getEmployeeListBySpecialityAndSubSpeciality(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	@Transactional
	Response validateEmpCode(String empCode,Integer orgId, Integer doctorId, Integer employeeId);

	@Transactional
	Response universalSearchForEmployee(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;


	Response searchEmployeeByNameAndRole(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;
}
