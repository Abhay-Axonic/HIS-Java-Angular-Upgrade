package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dao.IRejectedDietListDao;
import com.param.kitchen.dto.RejectedDietListRequestDto;
import com.param.global.common.ICommonConstants;
import in.param.exception.ApplicationException;

@Service
public class RejectedDietListServiceImpl implements IRejectedDietListService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(RejectedDietListServiceImpl.class);

	@Autowired
	IRejectedDietListDao iRejectedDietListDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getRejectedDietListByMultipleFields(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			if(rejectedDietListRequest.getMealId() == null || rejectedDietListRequest.getMealId()==0)
			{
				return iRejectedDietListDao.getRejectedDietOrderListByMultipleFieldsWithoutMealId(rejectedDietListRequest,organizationId,unitId);
			}
			else
			{
				return iRejectedDietListDao.getRejectedDietOrderListByMultipleFields(rejectedDietListRequest,organizationId,unitId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getRejectedDietListByOnlyDate(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			return iRejectedDietListDao.getRejectedDietListByOnlyDate(rejectedDietListRequest,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}




	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response reIssueRejectedDietOrderByDietOrderId(List<Integer> orderIds)
			throws ApplicationException {
		try {
			return iRejectedDietListDao.reIssueRejectedDietOrderByDietOrderId(orderIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
