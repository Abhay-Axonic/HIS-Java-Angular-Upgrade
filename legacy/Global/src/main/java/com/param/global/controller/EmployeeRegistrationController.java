package com.param.global.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.ServletUtils;
import com.param.global.dto.EmployeeRegistrationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.service.IEmployeeRegistrationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;

@RestController
@RequestMapping(value="api/global")
@SuppressWarnings({ "unchecked", "rawtypes"})
public class EmployeeRegistrationController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(EmployeeRegistrationController.class);

	@Autowired
	IEmployeeRegistrationService iEmployeeRegistrationService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	
	@RequestMapping(value="/employeeRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveEmployeeRegistration(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto,HttpServletRequest request) {
		try {
			Response res =  iEmployeeRegistrationService.saveEmployeeRegistration(employeeRegistrationDto);
			if(res.getStatus().equals(SUCCESS) && res.getResObject()!=null) {
				UserMasterDto userMasterDto = (UserMasterDto)res.getResObject();
				if(userMasterDto.getEmployeeId()!=null) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(employeeRegistrationDto.getUnitId());
				emailinfo.setOrganizationId(employeeRegistrationDto.getOrganizationId());
				emailinfo.setEmployeeId(userMasterDto.getEmployeeId());
				emailinfo.setPassword(userMasterDto.getPassword()!=null ? userMasterDto.getPassword() :"");
				emailinfo.setUserName(userMasterDto.getUserName()!=null ? userMasterDto.getUserName() : "");
				emailinfo.setApplicationLink(ServletUtils.getBaseUrl(request)+"/#/login");
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.EMPLOYEE_REGISTRATION, emailinfo);
				}
			}
			return res;
		}catch (Exception e) {
			logger.error("Error while saving the employee registration", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getEmployeeDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEmployeeDetails(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			return iEmployeeRegistrationService.getEmployeeDetails(employeeRegistrationDto);
		}catch (Exception e) {
			logger.error("Error while fetching the employee details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPlainEmployeeDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPlainEmployeeDetails(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			return iEmployeeRegistrationService.getPlainEmployeeDetails(employeeRegistrationDto);
		}catch (Exception e) {
			logger.error("Error while fetching the plain employee details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEmployeeDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmployeeDetails(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto,HttpServletRequest request) {
		try {
			Response res = iEmployeeRegistrationService.updateEmployeeDetails(employeeRegistrationDto);
			if(res.getStatus().equals(SUCCESS) && res.getResObject()!=null) {
				UserMasterDto userMasterDto = (UserMasterDto)res.getResObject();
				if(userMasterDto.getEmployeeId()!=null) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(employeeRegistrationDto.getUnitId());
				emailinfo.setOrganizationId(employeeRegistrationDto.getOrganizationId());
				emailinfo.setEmployeeId(userMasterDto.getEmployeeId());
				emailinfo.setPassword(userMasterDto.getPassword()!=null ? userMasterDto.getPassword() :"");
				emailinfo.setUserName(userMasterDto.getUserName()!=null ? userMasterDto.getUserName() : "");
				emailinfo.setApplicationLink(ServletUtils.getBaseUrl(request)+"/#/login");
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.EMPLOYEE_REGISTRATION, emailinfo);
				}
			}
			return res;
		}catch (Exception e) {
			logger.error("Error while updating the employee details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateEmployeeStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmployeeStatus(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			return iEmployeeRegistrationService.updateEmployeeStatus(employeeRegistrationDto);
		}catch (Exception e) {
			logger.error("Error while updating the employee status", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchEmployeeByCriteria", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchEmployeeByCriteria(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			return iEmployeeRegistrationService.searchEmployeeByCriteria(employeeRegistrationDto);
		}catch (Exception e) {
			logger.error("Error while searching for employees by criteria", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	 @RequestMapping(value = "/employee/image/{orgId}/{unitId}/{employeeId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveEmployeeImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("employeeId") int employeeId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iEmployeeRegistrationService.saveEmployeeImage(orgId,unitId,employeeId,inputFile);
	       }catch(Exception e)
	      {
	      logger.error("method failed", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }
	 
	 @RequestMapping(value="/searchEmployeeByName", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response searchEmployeeByName(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
			try {
				return iEmployeeRegistrationService.searchEmployeeByName(employeeRegistrationDto);
			}catch (Exception e) {
				logger.error("Error while searching for employees by name", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
			try {
				
				JsonCheck.mapJsonToObject(sampleMasterDto, EmployeeRegistrationDto.class);
				return null;
			} catch (Exception e) {
				logger.error("Error while mapping the sample master dto", e);
				return new Response(ERROR, ERROR_CODE, null, null, null);
			}
		}
	 
	 @RequestMapping(value = "/getEmployeeRegistrationCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getEmployeeRegistrationCount(@RequestBody EmployeeRegistrationDto employeeRegistrationDto) {
			try {
				return iEmployeeRegistrationService.getEmployeeRegistrationCount(employeeRegistrationDto);
			} catch (Exception e) {
				logger.error("Error while fetching the employee registration count", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	 
	 @RequestMapping(value = "/validateEmpCode/{empCode}/{orgId}/{doctorId}/{employeeId}", method = RequestMethod.GET)
		public Response validateEmpCode(@PathVariable("empCode") String empCode,@PathVariable("orgId")Integer orgId,
				@PathVariable("doctorId")Integer doctorId,@PathVariable("employeeId")Integer employeeId) {
			try {
				return iEmployeeRegistrationService.validateEmpCode(empCode,orgId,doctorId,employeeId);
			} catch (Exception e) {
				logger.error("Error while validating the employee code", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	 
	 @RequestMapping(value = "/getEmployeeListBySpecialityAndSubSpeciality", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getEmployeeListBySpecialityAndSubSpeciality(@RequestBody EmployeeRegistrationDto employeeRegistrationDto) {
			try {
				return iEmployeeRegistrationService.getEmployeeListBySpecialityAndSubSpeciality(employeeRegistrationDto);

			} catch (Exception e) {
				logger.error("Error while fetching the employee list by speciality and sub speciality", e);
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
	 
	 @RequestMapping(value="/universalSearchForEmployee", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response universalSearchForEmployee(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
			try {
				return iEmployeeRegistrationService.universalSearchForEmployee(employeeRegistrationDto);
			}catch (Exception e) {
				logger.error("Error while performing the universal search for employee", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}

	@RequestMapping(value="/searchEmployeeByNameAndRole", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchEmployeeByNameAndRole(@RequestBody  EmployeeRegistrationDto employeeRegistrationDto) {
		try {
			return iEmployeeRegistrationService.searchEmployeeByNameAndRole(employeeRegistrationDto);
		}catch (Exception e) {
			logger.error("Error while searching for employees by name and role", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
