package com.param.CSSD.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSD.dto.LoadBatchDto;
import com.param.CSSD.model.SendToMicrobiology;
import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SendToMicrobiologyDaoImpl extends GenericDao<SendToMicrobiology, Integer>
		implements ISendToMicrobiologyDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(SendToMicrobiologyDaoImpl.class);


	public SendToMicrobiologyDaoImpl() {
		super(SendToMicrobiology.class);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;


	@Override
	public Response sendToMicrobiology(LoadBatchDto loadBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			SendToMicrobiology sendToMicrobiology = new SendToMicrobiology();
			
			sendToMicrobiology.setBatchNumber(loadBatchDto.getBatchNumber());
			sendToMicrobiology.setStatus(loadBatchDto.getStatus());
			session.save(sendToMicrobiology);
			
			return new Response(SUCCESS, "200","Sent to Microbiology Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("sendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getBatchListToSendToMicrobiology() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<LoadBatchDto> loadBatchDtoList = statelessSession
					.getNamedQuery("GET_BATCH_LIST_TO_SEND_TO_MICROBIOLOGY")
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getBatchListToSendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	}
