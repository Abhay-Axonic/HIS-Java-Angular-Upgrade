package com.param.global.otims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTChecklistDetailsDto;
import com.param.global.otims.service.IOTChecklistDetailsService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/otims/OTchecklist")
@SuppressWarnings("rawtypes")
public class OTChecklistDetailsController  implements ICommonConstants {
	final static Logger logger = Logger.getLogger(OTChecklistDetailsController.class);


	@Autowired
	IOTChecklistDetailsService iOTChecklistDetailsService;
	
	@RequestMapping(value = "/saveOTChecklistDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response saveOTChecklistDetails(@RequestBody List<OTChecklistDetailsDto> oTChecklistDetailsDto)
			throws ApplicationException {
		try {
				return iOTChecklistDetailsService.saveOTChecklistDetails(oTChecklistDetailsDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/*@RequestMapping(value = "/getIntraOperativeChecklistByProReqId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getIntraOperativeChecklistByProReqId(@RequestBody OTChecklistDetailsDto oTChecklistDetailsDto)
			throws ApplicationException {
		try {
				return iOTChecklistDetailsService.getIntraOperativeChecklistByProReqId(oTChecklistDetailsDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
}
