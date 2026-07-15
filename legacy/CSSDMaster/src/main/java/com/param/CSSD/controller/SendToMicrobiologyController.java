package com.param.CSSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.service.ILoadBatchService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SendToMicrobiologyController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(SendToMicrobiologyController.class);


	@Autowired
	private ILoadBatchService iloadBatchService;

	
	
	/**
	 * This method is used to get batch list available to send to Microbiology.
	 * @return Response
	 */
	@RequestMapping(value = "/getBatchListToSendToMicrobiology", method = RequestMethod.GET)
	public @ResponseBody Response getBatchListToSendToMicrobiology() {
		try {
			return iloadBatchService.getBatchListToSendToMicrobiology();
		} catch (Exception e) {
			logger.error("getBatchListToSendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
		
}
