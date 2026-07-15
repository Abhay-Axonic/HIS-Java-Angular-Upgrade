package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.BulkMessageQueueDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBulkMessageQueueDao {


	Response addBulkMailMessageInQueue(BulkMessageQueueDto bulkDto)throws ApplicationException;

	Response getbulkMessageListByMessageQId(Integer messageQueueId)throws ApplicationException;

	Response deletMessageFromBulkQueqe(Integer bulkMessageQueueId)throws ApplicationException;

	Response updateBulkMailRetryCount(Integer bulkMessageQueueId, String errorMessage)throws ApplicationException;

	Response updateBulkMailLogId(Integer bulkMessageQueueId, String errorMessage, Integer logId)throws ApplicationException;

	Response checkAlreadyExitOrNot(String uniqueId)throws ApplicationException;

}
