package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistItemMasterDto;
import com.param.otims.service.IChecklistItemMstrService;

@RestController
@RequestMapping("/global/otims")
@SuppressWarnings({ "rawtypes" })
public class ChecklistItemMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ChecklistItemMasterController.class);

	@Autowired
	IChecklistItemMstrService iChecklistItemMasterservice;

	
/*	@RequestMapping(value = "/saveChecklistItemMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<ChecklistItemMasterDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, ChecklistItemMasterDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/

	@RequestMapping(value = "/saveChecklistItemMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveChecklistItemMaster(@RequestBody ChecklistItemMasterDto checklistItemMasterdto) {
		try {
			return iChecklistItemMasterservice.saveChecklistItemMaster(checklistItemMasterdto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateChecklistItemMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateChecklistItemMasterStatus(@RequestBody ChecklistItemMasterDto checklistItemMasterdto) {
		try {
			return iChecklistItemMasterservice.updateChecklistItemMasterStatus(checklistItemMasterdto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getChecklistItemMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChecklistItemMasterList(@RequestBody ChecklistItemMasterDto checklistItemMasterdto) {
		try {
			return iChecklistItemMasterservice.getChecklistItemMasterList(checklistItemMasterdto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchChecklistItemName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchChecklistItemName(@RequestBody ChecklistItemMasterDto checklistItemMasterdto) {
		try {
			return iChecklistItemMasterservice.searchChecklistItemName(checklistItemMasterdto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
