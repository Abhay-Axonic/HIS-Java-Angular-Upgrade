package com.param.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.Response;
import com.param.global.dto.MessageQueueDto;
import com.param.global.service.IMessageQueueService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/autoMail")
public class MessageQueueController {
	final static Logger logger = Logger.getLogger(MessageQueueController.class);


	@Autowired
	IMessageQueueService iMessageQueueService;
	
	@RequestMapping(value = "/sendOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response send(@RequestBody MessageQueueDto dto) {
		try {
			return iMessageQueueService.send(dto);
		} catch (Exception e) {
		logger.error("send failed", e);
		}
	return new Response(null, null, null, null, null);
	}

	@RequestMapping(value = "/sendOTPonMobile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendOTPonMobile(@RequestBody MessageQueueDto dto) {
		try {
			return iMessageQueueService.sendOTPonMobile(dto);
		} catch (Exception e) {
		logger.error("sendOTPonMobile failed", e);
		}
	return new Response(null, null, null, null, null);
	}
	
}
