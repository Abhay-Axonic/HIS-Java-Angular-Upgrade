package com.param.global.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.MessageQueueDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IMessageQueueService {

	
	Response addTextSMSMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response addMailMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response addWhatsAppMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response getMessageFromQueqe(int type)throws ApplicationException;

	Response checkAlreadyExitsOrNot(String uniqueId, Integer sendmail, Integer triggerId)throws ApplicationException;

	Response send(MessageQueueDto dto)throws ApplicationException;

	Response sendWhatsAppNewMethod(MessageQueueDto messageQueueDto) throws ApplicationException;
		// TODO Auto-generated method stub

	Response addTextWhatsAppMessageInMessageLog(MessageQueueDto dto)throws ApplicationException;

	Response addTextSMSMessageInMessageLogText(MessageQueueDto dto)throws ApplicationException;

	Response sendOTPonMobile(MessageQueueDto dto)throws ApplicationException;

}
