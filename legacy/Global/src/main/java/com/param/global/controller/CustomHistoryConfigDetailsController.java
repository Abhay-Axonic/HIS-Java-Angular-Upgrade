package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.service.ICustomHistoryConfigDetailsService;

@Controller
@SuppressWarnings({"rawtypes","unchecked"})
@RequestMapping("/global/customHistory")
public class CustomHistoryConfigDetailsController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(CustomHistoryConfigDetailsController.class);

	@Autowired
    private ICustomHistoryConfigDetailsService iCustomHistoryConfigDetailsService;

	@RequestMapping(value="/getCustomHistoryConfigurationList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCustomHistoryConfigurationList(@RequestBody CustomHistoryConfigurationDetailsDto dto){
		try{
			return iCustomHistoryConfigDetailsService.getCustomHistoryConfigurationList(dto);
		}catch(Exception e){
			logger.error("Error while fetching the custom history configuration list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/updateCustomHistoryDetails", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCustomHistoryDetails(@RequestBody CustomHistoryConfigurationDetailsDto dto){
		try{
			return iCustomHistoryConfigDetailsService.updateCustomHistoryDetails(dto);
		}catch(Exception e){
			logger.error("Error while updating the custom history details", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/deleteCustomHistoryDetailsRecord", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteCustomHistoryDetailsRecord(@RequestBody CustomHistoryConfigurationDetailsDto dto){
		try{
			return iCustomHistoryConfigDetailsService.deleteCustomHistoryDetailsRecord(dto);
		}catch(Exception e){
			logger.error("Error while deleting the custom history details record", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getPatientCustomHistoryDetails", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientCustomHistoryDetails(@RequestBody CustomHistoryConfigurationDetailsDto dto){
		try{
			return iCustomHistoryConfigDetailsService.getPatientCustomHistoryDetails(dto);
		}catch(Exception e){
			logger.error("Error while fetching the patient custom history details", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
