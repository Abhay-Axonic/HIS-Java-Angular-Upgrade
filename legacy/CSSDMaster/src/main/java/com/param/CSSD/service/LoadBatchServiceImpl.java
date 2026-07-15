package com.param.CSSD.service;

import in.param.exception.ApplicationException;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSD.dao.ILoadBatchDao;
import com.param.CSSD.dto.LoadBatchDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoadBatchServiceImpl implements ILoadBatchService {

	final static Logger logger = Logger.getLogger(LoadBatchServiceImpl.class);


	@Autowired
	private ILoadBatchDao iLoadBatchDao;

	@Override
	@Transactional
	public Response searchKitsToLoadBatch(Integer organizationId,Integer unitId,Integer storeId,String name ) {
		try {
			return iLoadBatchDao.searchKitsToLoadBatch(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchKitsToLoadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getRecentBioIndiDate(Integer organizationId,Integer unitId,Integer storeId,Integer autoclaveId ) {
		try {
			return iLoadBatchDao.getRecentBioIndiDate(organizationId,unitId,storeId,autoclaveId);
		} catch (Exception e) {
			logger.error("getRecentBioIndiDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response loadBatch(LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchDao.loadBatch(loadBatchDto);
		} catch (Exception e) {
			logger.error("loadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getLoadBatchListByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,Boolean biologicalIndicator,String status) {
		try {
			return iLoadBatchDao.getLoadBatchListByMultipleFields(organizationId,unitId,storeId,from,to,batchNo,biologicalIndicator,status);
		} catch (Exception e) {
			logger.error("getLoadBatchListByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response savePendingIndicators(LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchDao.savePendingIndicators(loadBatchDto);
		} catch (Exception e) {
			logger.error("savePendingIndicators failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response getBatchDetailsByBatchNumber(Integer organizationId,Integer unitId,Integer storeId,Integer batchNo) {
		try {
			return iLoadBatchDao.getBatchDetailsByBatchNumber(organizationId,unitId,storeId,batchNo);
		} catch (Exception e) {
			logger.error("getBatchDetailsByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getLoadBatchListToSendToMicrobiologyByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,String status) {
		try {
			return iLoadBatchDao.getLoadBatchListToSendToMicrobiologyByMultipleFields(organizationId,unitId,storeId,from,to,batchNo,status);
		} catch (Exception e) {
			logger.error("getLoadBatchListToSendToMicrobiologyByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response sendToMicrobiology(LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchDao.sendToMicrobiology(loadBatchDto);
		} catch (Exception e) {
			logger.error("sendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
	@Transactional
	public Response loadBatch(LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchDao.loadBatch(loadBatchDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response("ERROR", null, null, null, null);
	}*/
	
	
	
	@Override
	@Transactional
	public Response getBatchListToSendToMicrobiology() {
		try {
			return iLoadBatchDao.getBatchListToSendToMicrobiology();
		} catch (Exception e) {
			logger.error("getBatchListToSendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListByBatchNumber(Integer batchNo) {
		try {
			return iLoadBatchDao.getKitListByBatchNumber(batchNo);
		} catch (Exception e) {
			logger.error("getKitListByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	@Override
	@Transactional
	public Response getLoadBatchList() {
		try {
			return iLoadBatchDao.getLoadBatchList();
		} catch (Exception e) {
			logger.error("getLoadBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@Override
	@Transactional
	public Response getLoadBatchListByMultipleFields(Date from,Date to,Integer batchNo, String status) {
		try {
			return iLoadBatchDao.getLoadBatchListByMultipleFields(from,to,batchNo,status);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response("ERROR", null, null, null, null);
	}*/
	
	
	
	@Override
	@Transactional
	public Response getBatchNoListForControl() {
		try {
			return iLoadBatchDao.getBatchNoListForControl();
		} catch (Exception e) {
			logger.error("getBatchNoListForControl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getBatchNoListForTest() {
		try {
			return iLoadBatchDao.getBatchNoListForTest();
		} catch (Exception e) {
			logger.error("getBatchNoListForTest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getBatchListForApplication(Integer batchNo,Date fromDate,Date toDate,Integer autoclaveNo,String status) {
		try {
			return iLoadBatchDao.getBatchListForApplication(batchNo,fromDate,toDate,autoclaveNo,status);
		} catch (Exception e) {
			logger.error("getBatchListForApplication failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	
}
