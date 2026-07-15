package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IWeekDaysMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeekDaysMasterServiceImpl implements IWeekDaysMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(WeekDaysMasterServiceImpl.class);

	@Autowired
	IWeekDaysMasterDao iWeekDaysMasterDao;

	@Override
	public Response getAllWeekDaysMaster() throws ApplicationException {
		try {
			return iWeekDaysMasterDao.getAllWeekDaysMaster();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
