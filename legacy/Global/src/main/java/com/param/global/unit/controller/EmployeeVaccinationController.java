package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.EmployeeVaccinationDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.service.IEmployeeVaccinationService;

@RestController
@RequestMapping("api/global ")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmployeeVaccinationController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(EmployeeVaccinationController.class);

	@Autowired
	IEmployeeVaccinationService iEmployeeVaccinationService;
	
   @RequestMapping(value = "/saveEmployeeVaccination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveUserRoleMaster(@RequestBody EmployeeVaccinationDto employeeVaccinationDto) {
			try {
				return iEmployeeVaccinationService.saveEmployeeVaccination(employeeVaccinationDto);
			} catch (Exception e) {
				logger.error("saveUserRoleMaster failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
   
   @RequestMapping(value="/searchEmployeeVaccinationByRole", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchRolReByName(@RequestBody  EmployeeVaccinationDto employeeVaccinationDto) {
		try {
			return iEmployeeVaccinationService.searchEmployeeVaccinationByRole(employeeVaccinationDto);
		}catch (Exception e) {
			logger.error("searchRolReByName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
   
   
   @RequestMapping(value="/getVaccination", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVaccination(@RequestBody  EmployeeVaccinationDto employeeVaccinationDto) {
		try {
			return iEmployeeVaccinationService.getVaccination(employeeVaccinationDto);
		}catch (Exception e) {
			logger.error("getVaccination failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

}


