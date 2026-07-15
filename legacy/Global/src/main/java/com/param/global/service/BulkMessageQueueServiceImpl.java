package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IBulkMessageQueueDao;
import com.param.global.dto.BulkMessageQueueDto;

import org.apache.log4j.Logger;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class BulkMessageQueueServiceImpl implements IBulkMessageQueueService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(BulkMessageQueueServiceImpl.class);

	@Autowired
	IBulkMessageQueueDao iBulkMessageQueueDao;

	@Override
	public Response addBulkMailMessageInQueue(BulkMessageQueueDto bulkDto) throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.addBulkMailMessageInQueue(bulkDto);

		} catch (Exception e)
		{
			logger.error("addBulkMailMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getbulkMessageListByMessageQId(Integer messageQueueId) throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.getbulkMessageListByMessageQId(messageQueueId);

		} catch (Exception e)
		{
			logger.error("getbulkMessageListByMessageQId failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response deletMessageFromBulkQueqe(Integer bulkMessageQueueId) throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.deletMessageFromBulkQueqe(bulkMessageQueueId);

		} catch (Exception e)
		{
			logger.error("deletMessageFromBulkQueqe failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateBulkMailRetryCount(Integer bulkMessageQueueId, String errorMessage)
			throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.updateBulkMailRetryCount(bulkMessageQueueId,errorMessage);

		} catch (Exception e)
		{
			logger.error("updateBulkMailRetryCount failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateBulkMailLogId(Integer bulkMessageQueueId, String errorMessage, Integer logId)
			throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.updateBulkMailLogId(bulkMessageQueueId,errorMessage,logId);

		} catch (Exception e)
		{
			logger.error("updateBulkMailLogId failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response checkAlreadyExitOrNot(String uniqueId) throws ApplicationException {
		try
		{
			return iBulkMessageQueueDao.checkAlreadyExitOrNot(uniqueId);

		} catch (Exception e)
		{
			logger.error("checkAlreadyExitOrNot failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}
