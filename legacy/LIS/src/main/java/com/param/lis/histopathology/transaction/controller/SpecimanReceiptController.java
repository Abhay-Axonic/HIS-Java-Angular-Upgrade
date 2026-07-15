package com.param.lis.histopathology.transaction.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.dto.SampleMasterDto;
import com.param.lis.histopathology.transaction.dto.specimanReceiptDto;
import com.param.lis.histopathology.transaction.service.ISpecimanReceiptService;

import io.swagger.annotations.Api;


@RestController
@Api(value = "LIS_Histopathology", tags = "Speciman Receipt Master")
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/histopathology")
public class SpecimanReceiptController implements ICommonConstants, IError{
	
	@Autowired 
	ISpecimanReceiptService iSpecimanReceiptService;
	

final static Logger logger = Logger.getLogger(SpecimanReceiptController.class);
	
	@RequestMapping(value = "/listSpecimanReceipt/{isPreOrPostBilling}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response listSpecimanReceipt(
			@RequestBody SearchDto searchDto,@PathVariable("isPreOrPostBilling")Character isPreOrPostBilling)
	{
		Response<specimanReceiptDto, Integer> response = null;
		try
		{
			response = iSpecimanReceiptService.listSpecimanReceipt(searchDto,isPreOrPostBilling);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/getTotalSpecimanReceiptRecord/{isPreOrPostBilling}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBioChemistryWorklistCount(@RequestBody SearchDto searchDto,@PathVariable("isPreOrPostBilling")Character isPreOrPostBilling)
	{
		Response<SampleMasterDto, Integer> response = null;
		try
		{
			response = iSpecimanReceiptService.getTotalSpecimanReceiptRecord(searchDto,isPreOrPostBilling);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	
	@RequestMapping(value = "/specimenCollection", method = RequestMethod.POST)
	public @ResponseBody Response collectionSpeciman(@RequestBody List<specimanReceiptDto> listspecimanReceiptDto) {
		Response<SampleMasterDto, Integer> response = null;
		try {
			response = iSpecimanReceiptService.collectionSpeciman(listspecimanReceiptDto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

}
