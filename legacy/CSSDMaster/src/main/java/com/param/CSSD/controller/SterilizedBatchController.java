package com.param.CSSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSD.dto.SterilizedBatchDto;
import com.param.CSSD.service.ISterilizedBatchService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizedBatchController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizedBatchController.class);


	@Autowired
	private ISterilizedBatchService iSterilizedBatchService;

	/**
	 * This method is used to transfer a batch to sterilize zone
	 * @param sterilizedBatchDto
	 * @return Response
	 */
	@RequestMapping(value = "/transferToSterilizeZone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response transferToSterilizeZone(@RequestBody SterilizedBatchDto sterilizedBatchDto) {
		try {
			return iSterilizedBatchService.transferToSterilizeZone(sterilizedBatchDto);
		} catch (Exception e) {
			logger.error("transferToSterilizeZone failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the indicators of a batch by batch number.
	 * @param batchNo
	 * @return Response
	 */
	@RequestMapping(value = "/getIndicatorsByBatchNo", method = RequestMethod.GET)
	public @ResponseBody Response getIndicatorsByBatchNo(@RequestParam("batchNo") Integer batchNo ) {
		try {
			return iSterilizedBatchService.getIndicatorsByBatchNo(batchNo);
		} catch (Exception e) {
			logger.error("getIndicatorsByBatchNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
		
}
