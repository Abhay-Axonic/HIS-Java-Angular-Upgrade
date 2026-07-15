package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.model.MessageLog;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IMessageLogService {

	Response saveMessageLog(MessageLog logDto)throws ApplicationException;

	Response getLogDetailsByQuequeId(Integer messageQueueId)throws ApplicationException;

}
