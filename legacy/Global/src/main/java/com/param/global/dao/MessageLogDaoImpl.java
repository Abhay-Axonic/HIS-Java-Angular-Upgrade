package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.MessageLogDto;
import com.param.global.model.MessageLog;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class MessageLogDaoImpl extends GenericDao<MessageLog, Integer>implements IMessageLogDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(MessageLogDaoImpl.class);

	public MessageLogDaoImpl() {
		super(MessageLog.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	
	@Override
	public Response saveMessageLog(MessageLog logDto) throws ApplicationException {
		try
		{	
			MessageLog mstr = new MessageLog();
			mstr = save(logDto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mstr.getId());

		} catch (Exception e)
		{
			logger.error("Error while saving message log", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getLogDetailsByQuequeId(Integer messageQueueId) throws ApplicationException {
	try {
			
			List<MessageLogDto> messageQueueIdList = sessionFactory.getCurrentSession().createSQLQuery(
					"select message_log_id as \"id\" from public.t_message_log where   message_queue_id  =:messageQueueId ")
					.setParameter("messageQueueId",messageQueueId)
					.setResultTransformer(Transformers.aliasToBean(MessageLogDto.class)).list();
			Integer id = null;
			if(messageQueueIdList != null  && messageQueueIdList.size() >0 ) {
				id = messageQueueIdList.get(0).getId();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, id);
			}else {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			
					
		
			
		} catch (Exception e) {
			logger.error("Error while fetching log details by queue id", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	
}
