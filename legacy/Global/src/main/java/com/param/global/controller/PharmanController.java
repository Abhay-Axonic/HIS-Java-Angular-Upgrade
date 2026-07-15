package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PharmanDrugDto;
import com.param.global.service.IPharmanService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class PharmanController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(PharmanController.class);

	@Autowired
	private IPharmanService iPharmanService;
	
	@RequestMapping(value = "/getPharmanDrugList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPharmanDrugList(@RequestBody PharmanDrugDto pharmanDrugDto){
		
		try {
			return iPharmanService.getPharmanDrugList(pharmanDrugDto);
			
		} catch (Exception e) {
		logger.error("getPharmanDrugList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		
	}
	
	@RequestMapping(value = "/getPharmanDrugFormByName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPharmanDrugFormByName(@RequestBody PharmanDrugDto pharmanDrugDto){
		
		try {
			return iPharmanService.getPharmanDrugFormByName(pharmanDrugDto);
			
		} catch (Exception e) {
		logger.error("getPharmanDrugFormByName failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
	}
	
	@RequestMapping(value = "/getPopUpData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPopUpData(@RequestBody PharmanDrugDto pharmanDrugDto){
		
		try {
			return iPharmanService.getPopUpData(pharmanDrugDto);
			
		} catch (Exception e) {
		logger.error("getPopUpData failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPopUpDataWithId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPopUpDataWithId(@RequestBody PharmanDrugDto pharmanDrugDto){
		
		try {
			return iPharmanService.getPopUpDataWithId(pharmanDrugDto);
			
		} catch (Exception e) {
		logger.error("getPopUpDataWithId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
