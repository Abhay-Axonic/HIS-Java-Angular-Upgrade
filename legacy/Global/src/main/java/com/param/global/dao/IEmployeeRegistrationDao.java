package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.EmployeeRegistrationDto;
import com.param.global.dto.UserUnitMapperDto;
import com.param.global.model.EmployeeMaster;
import com.param.global.unit.dto.OTIMSUserRoleMapperDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;


@SuppressWarnings("rawtypes")
public interface IEmployeeRegistrationDao extends IGenericDao<EmployeeMaster, Integer>{

	Response saveEmployeeRegistration(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response checkUniqueEmployee(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response saveEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response getEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response updateEmployeeDetails(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response updateEmployeeStatus(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response searchEmployeeByCriteria(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;
	
	public Response updateEmployeeImage(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;
	
	Response searchEmployeeByName(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response saveOTIMSUserRoleMapper(OTIMSUserRoleMapperDto otimsUserRoleMapperDto)throws ApplicationException;

	Response inactiveRoleStatusOfOtimsUser(OTIMSUserRoleMapperDto otimsUserRoleMapperDto);

	Response inactiveUnitMapperByUserId(UserUnitMapperDto userUnitMapperDto)throws ApplicationException;

	Response getEmployeeRegistrationCount(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Response getEmployeeListBySpecialityAndSubSpeciality(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Response validateEmpCode(String empCode,Integer orgId, Integer doctorId, Integer employeeId);

	Response universalSearchForEmployee(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Response checkTabAccessAvail(UserUnitMapperDto userUnitMapperDto) throws ApplicationException;

	String getUserNamwByUserId(Integer userId) throws ApplicationException;

	public Response saveStaffSpecialityMapper(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Response saveStaffSubSpecialityMapper(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response inactiveSpecialityStatusOfUser(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response inactiveSubSpecialityStatusOfUser(EmployeeRegistrationDto employeeRegistrationDto)throws ApplicationException;

	Response inactiveUnitMapperByUserIdAndUnits(List<Integer> unitIds, Integer userId)throws ApplicationException;

	Response searchEmployeeByNameAndRole(EmployeeRegistrationDto employeeRegistrationDto) throws ApplicationException;

	Integer getUserCount(EmployeeRegistrationDto employeeRegistrationDto);

	Integer getConfiguredUserLimit(EmployeeRegistrationDto employeeRegistrationDto);
}
