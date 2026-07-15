package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.UHIDMergeDto;
import com.param.global.unit.service.ICabinMasterService;
import com.param.global.unit.service.IUHIDMergeRequestService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class UHIDMergeRequestController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(UHIDMergeRequestController.class);

	@Autowired
	private IUHIDMergeRequestService iUHIDMergeRequestService;
	
	@RequestMapping(value = "/getUHIDUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserUHIDMergeList(@RequestBody UHIDMergeDto uHIDMergeDto){

		 try {
			 
			return iUHIDMergeRequestService.getUserUHIDMergeList(uHIDMergeDto);

		} catch (Exception e)  {
		logger.error("getUserUHIDMergeList failed", e);
			 return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveUHIDMergeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveUHIDMergeMaster(@RequestBody UHIDMergeDto uHIDMergeDto){

		try {
			return iUHIDMergeRequestService.saveUHIDMergeMaster(uHIDMergeDto);
			
		} catch (Exception e) {
		logger.error("saveUHIDMergeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
		
}
