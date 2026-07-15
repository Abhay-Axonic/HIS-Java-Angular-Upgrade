package com.param.CSSD.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSD.dao.ISterilizedBatchDao;
import com.param.CSSD.dto.SterilizedBatchDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizedBatchServiceImpl implements ISterilizedBatchService {

	final static Logger logger = Logger.getLogger(SterilizedBatchServiceImpl.class);


	@Autowired
	private ISterilizedBatchDao iSterilizedBatchDao;

	@Override
	@Transactional
	public Response transferToSterilizeZone(SterilizedBatchDto sterilizedBatchDto) {
		try {
			return iSterilizedBatchDao.transferToSterilizeZone(sterilizedBatchDto);
		} catch (Exception e) {
			logger.error("transferToSterilizeZone failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getIndicatorsByBatchNo(Integer batchNo) {
		try {
			return iSterilizedBatchDao.getIndicatorsByBatchNo(batchNo);
		} catch (Exception e) {
			logger.error("getIndicatorsByBatchNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}