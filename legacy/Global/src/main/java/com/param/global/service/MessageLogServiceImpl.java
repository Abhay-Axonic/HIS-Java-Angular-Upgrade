package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IMessageLogDao;
import com.param.global.model.MessageLog;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class MessageLogServiceImpl implements IMessageLogService,ICommonConstants{
	final static Logger logger = Logger.getLogger(MessageLogServiceImpl.class);

	
	@Autowired
	IMessageLogDao iMessageLogDao;

	
	@Override
	public Response saveMessageLog(MessageLog logDto) throws ApplicationException {
		try
		{
			return iMessageLogDao.saveMessageLog(logDto);

		} catch (Exception e)
		{
		logger.error("saveMessageLog failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getLogDetailsByQuequeId(Integer messageQueueId) throws ApplicationException {
		try
		{
			return iMessageLogDao.getLogDetailsByQuequeId(messageQueueId);

		} catch (Exception e)
		{
		logger.error("getLogDetailsByQuequeId failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	
}
