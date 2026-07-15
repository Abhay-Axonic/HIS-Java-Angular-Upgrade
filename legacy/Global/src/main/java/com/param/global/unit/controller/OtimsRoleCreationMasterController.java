package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.OtimsRoleMasterDto;
import com.param.global.unit.service.IOtimsRoleCreationMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/global/otimsRole")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OtimsRoleCreationMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OtimsRoleCreationMasterController.class);


	@Autowired
	private IOtimsRoleCreationMasterService iOtimsRoleCreationMasterService;
		
	@RequestMapping(value = "/saveOtimsRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOtimsRoleMaster(@RequestBody OtimsRoleMasterDto otimsRoleMasterDto) {
		try {
			return iOtimsRoleCreationMasterService.saveOtimsRoleMaster(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("saveOtimsRoleMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getAllOtimsRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllOtimsRoleMasterList(@RequestBody OtimsRoleMasterDto otimsRoleMasterDto) {
		try {
			return iOtimsRoleCreationMasterService.getAllOtimsRoleMasterList(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("getAllOtimsRoleMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateOtimsRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOtimsRoleMaster(@RequestBody OtimsRoleMasterDto otimsRoleMasterDto) {
		try {
			return iOtimsRoleCreationMasterService.updateOtimsRoleMaster(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("updateOtimsRoleMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateOtimsRoleMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOtimsRoleMasterStatus(@RequestBody OtimsRoleMasterDto otimsRoleMasterDto) {
		try {
			return iOtimsRoleCreationMasterService.updateOtimsRoleMasterStatus(otimsRoleMasterDto);
		} catch (Exception e) {
		logger.error("updateOtimsRoleMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
