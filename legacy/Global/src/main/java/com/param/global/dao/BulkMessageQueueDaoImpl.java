package com.param.global.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BulkMessageQueueDto;
import com.param.global.model.BulkMessageQueue;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class BulkMessageQueueDaoImpl extends GenericDao<BulkMessageQueue, Integer>implements IBulkMessageQueueDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(BulkMessageQueueDaoImpl.class);


	public BulkMessageQueueDaoImpl() {
		super(BulkMessageQueue.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response addBulkMailMessageInQueue(BulkMessageQueueDto bulkDto) throws ApplicationException {
		try
		{	
			BulkMessageQueue dto = new BulkMessageQueue();
				
			 dto.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setMailContent(bulkDto.getMailContent());
			 dto.setMailFileName(bulkDto.getMailFileName());
			 dto.setMailSubject(bulkDto.getMailSubject());
			 dto.setPatientId(bulkDto.getPatientId());
			 dto.setMessageQueueId(bulkDto.getMessageQueueId());
			 dto.setOrganizationId(bulkDto.getOrganizationId());
			 dto.setReportString(bulkDto.getReportString());
			 dto.setUniqueId(bulkDto.getUniqueId());
			 dto.setUnitId(bulkDto.getUnitId());
			 dto.setRetryAttemps(RetryAttemps);
			 dto.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto = save(dto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (Exception e)
		{
		logger.error("addBulkMailMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getbulkMessageListByMessageQId(Integer messageQueueId) throws ApplicationException {
		try {
			
			List<BulkMessageQueueDto> messageQueueIdList = sessionFactory.getCurrentSession().createSQLQuery(
					"select " + 
					"bulk_message_queue_id as \"bulkMessageQueueId\", " + 
					"	patient_id as \"patientId\", " + 
					"	unique_id as \"uniqueId\", " + 
					"	message_queue_id as \"messageQueueId\", " + 
					"	mail_content as \"mailContent\", " + 
					"	mail_subject as \"mailSubject\", " + 
					"	mail_file_name as \"mailFileName\", " + 
					"	report_string as \"reportString\", " + 
					"	retry_attemps as \"retryAttemps\" " + 
					"	from t_bulk_message_queue   " + 
					"	where message_log_id is null and message_queue_id =:messageQueueId " )
					.setParameter("messageQueueId",messageQueueId)
					.setResultTransformer(Transformers.aliasToBean(BulkMessageQueueDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, messageQueueIdList, null);
			
			
					
		
			
		} catch (Exception e) {
		logger.error("getbulkMessageListByMessageQId failed", e);
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response deletMessageFromBulkQueqe(Integer bulkMessageQueueId) throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession().createSQLQuery(" DELETE FROM public.t_bulk_message_queue WHERE bulk_message_queue_id  =:bulkMessageQueueId ").setParameter("bulkMessageQueueId",bulkMessageQueueId).executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (Exception e) {
	logger.error("deletMessageFromBulkQueqe failed", e);
	return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	}

	@Override
	public Response updateBulkMailRetryCount(Integer bulkMessageQueueId, String errorMessage)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			String query = "UPDATE t_bulk_message_queue " +
					"SET retry_attemps = (retry_attemps - 1), error_message = :errorMessage " +
					"WHERE bulk_message_queue_id = :bulkMessageQueueId";

			Integer count = (Integer) session.createSQLQuery(query)
					.setParameter("errorMessage", errorMessage)
					.setParameter("bulkMessageQueueId", bulkMessageQueueId)
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
	} catch (Exception e) {
	logger.error("updateBulkMailRetryCount failed", e);
	return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	}

	@Override
	public Response updateBulkMailLogId(Integer bulkMessageQueueId, String errorMessage, Integer logId)
			throws ApplicationException {
		try {

			String query = "UPDATE t_bulk_message_queue " +
					"SET retry_attemps = (retry_attemps - 1), message_log_id = :logId, error_message = :errorMessage " +
					"WHERE bulk_message_queue_id = :bulkMessageQueueId";

			Integer count = (Integer) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("logId", logId)
					.setParameter("errorMessage", errorMessage)
					.setParameter("bulkMessageQueueId", bulkMessageQueueId)
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
	} catch (Exception e) {
	logger.error("updateBulkMailLogId failed", e);
	return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	}

	@Override
	public Response checkAlreadyExitOrNot(String uniqueId) throws ApplicationException {
		try {

			String query = "SELECT bulk_message_queue_id AS \"bulkMessageQueueId\" " +
					"FROM public.t_bulk_message_queue " +
					"WHERE unique_id = :uniqueId";

			List<BulkMessageQueueDto> messageQueueIdList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("uniqueId", uniqueId)
					.setResultTransformer(Transformers.aliasToBean(BulkMessageQueueDto.class))
					.list();

			return new Response(SUCCESS, SUCCESS_CODE, null, messageQueueIdList, null);
		} catch (Exception e) {
		logger.error("checkAlreadyExitOrNot failed", e);
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	
}
