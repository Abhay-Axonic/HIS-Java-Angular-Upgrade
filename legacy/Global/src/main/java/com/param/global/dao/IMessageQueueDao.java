package com.param.global.dao;

import java.util.Date;

import com.param.global.common.Response;
import com.param.global.dto.MessageQueueDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMessageQueueDao {

	Response addMailMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response addWhatsAppMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response addTextSMSMessageInQueue(MessageQueueDto dto)throws ApplicationException;

	Response getMessageFromQueqe(int type)throws ApplicationException;

	Response deletMessageFromQueqe(Integer messageQueueId)throws ApplicationException;

	Response updateRetryCount(Integer messageQueueId, String error)throws ApplicationException;

	Response checkAlreadyExitsOrNot(String uniqueId, Integer sendmail, Integer triggerId)throws ApplicationException;

	Response updateBulkMailLogId(Integer bulkMessageQueueId, String errorMessage, Integer logId)throws ApplicationException;

}
