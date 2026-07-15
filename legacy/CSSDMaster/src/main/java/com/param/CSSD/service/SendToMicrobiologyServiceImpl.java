package com.param.CSSD.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSD.dao.ISendToMicrobiologyDao;
import com.param.CSSD.dto.LoadBatchDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SendToMicrobiologyServiceImpl implements ISendToMicrobiologyService {

	final static Logger logger = Logger.getLogger(SendToMicrobiologyServiceImpl.class);


	@Autowired
	private ISendToMicrobiologyDao iSendToMicrobiologyDao;

	@Override
	@Transactional
	public Response sendToMicrobiology(LoadBatchDto loadBatchDto) {
		try {
			return iSendToMicrobiologyDao.sendToMicrobiology(loadBatchDto);
		} catch (Exception e) {
			logger.error("sendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getBatchListToSendToMicrobiology() {
		try {
			return iSendToMicrobiologyDao.getBatchListToSendToMicrobiology();
		} catch (Exception e) {
			logger.error("getBatchListToSendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}