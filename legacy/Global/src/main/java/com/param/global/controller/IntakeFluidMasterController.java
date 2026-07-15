package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.IIntakeFluidMasterService;
import com.param.global.unit.dto.IntakeFluidMasterDto;
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;
import com.param.global.unit.dto.OutputTypeMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IntakeFluidMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(IntakeFluidMasterController.class);

	@Autowired
	private IIntakeFluidMasterService iIntakeFluidMasterService;
	
	@RequestMapping(value = "/getIntakeFluidMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeFluidMasterList(@RequestBody IntakeFluidMasterDto intakeFluidMasterDto){
		
		try {
			return iIntakeFluidMasterService.getIntakeFluidMasterList(intakeFluidMasterDto);
			
		} catch (Exception e) {
		logger.error("getIntakeFluidMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getIntakeFluidTypeMasterListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeFluidTypeMasterListById(@RequestBody IntakeFluidTypeMasterDto intakeFluidTypeMasterDto){
		
		try {
			return iIntakeFluidMasterService.getIntakeFluidTypeMasterListById(intakeFluidTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getIntakeFluidTypeMasterListById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getOutputFluidMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOutputFluidMasterList(@RequestBody OutputTypeMasterDto outputTypeMasterDto){
		
		try {
			return iIntakeFluidMasterService.getOutputFluidMasterList(outputTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getOutputFluidMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
