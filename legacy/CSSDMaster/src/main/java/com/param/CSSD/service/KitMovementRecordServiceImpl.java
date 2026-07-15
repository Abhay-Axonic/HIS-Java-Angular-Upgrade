package com.param.CSSD.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.IKitIssueDao;
import com.param.CSSD.dao.IKitMovementRecordDao;
import com.param.CSSD.dto.KitIssueDto;
import com.param.CSSD.model.KitMovementRecord;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitMovementRecordServiceImpl implements IKitMovementRecordService { 

	final static Logger logger = Logger.getLogger(KitMovementRecordServiceImpl.class);

	
	@Autowired
	private IKitMovementRecordDao iKitMovementRecordDao;

	@Override
	@Transactional
	public void createKitMovementRecord(KitMovementRecord kitMovementRecord) {
		try {
			iKitMovementRecordDao.createKitMovementRecord(kitMovementRecord);
		} catch (Exception e) {
			logger.error("createKitMovementRecord failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}





	