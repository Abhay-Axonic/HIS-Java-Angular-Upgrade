package com.param.kitchen.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dao.IDietOrderDao;
import com.param.kitchen.dto.DietOrderRequestDto;
import com.param.global.common.ICommonConstants;
import in.param.exception.ApplicationException;

@Service
public class DietOrderServiceImpl implements IDietOrderService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietOrderServiceImpl.class);

	@Autowired
	IDietOrderDao iDietOrderDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getDietOrderByMultipleFields(DietOrderRequestDto dietOrderRequestDto,Integer organizationId,Integer unitId)
			throws ApplicationException {
		try {
			return iDietOrderDao.getDietOrderByMultipleFields(dietOrderRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

@SuppressWarnings({ "rawtypes", "unchecked" })
@Override
@Transactional
public Response getPatientDetailsByPatientId(Integer patientId,Integer organizationId,Integer unitId)
		throws ApplicationException {
	try {
		return iDietOrderDao.getPatientDetailsByPatientId(patientId,organizationId,unitId);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

		
}
