package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.model.MessageLog;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMessageLogDao {

	Response saveMessageLog(MessageLog logDto)throws ApplicationException;

	Response getLogDetailsByQuequeId(Integer messageQueueId)throws ApplicationException;

}
