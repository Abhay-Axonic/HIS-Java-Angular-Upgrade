package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IDietTimingMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTimingMasterServiceImpl implements IDietTimingMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTimingMasterServiceImpl.class);

	@Autowired
	IDietTimingMasterDao iDietTimingMasterDao;

	@Override
	public Response getAllDietTimingMaster() throws ApplicationException {
		try {
			return iDietTimingMasterDao.getAllDietTimingMaster();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
