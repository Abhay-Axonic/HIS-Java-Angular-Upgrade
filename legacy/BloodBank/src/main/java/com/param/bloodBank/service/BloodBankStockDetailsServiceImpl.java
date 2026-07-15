package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBankStockDetailsDao;
import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodBankStockDetailsServiceImpl implements IBloodBankStockDetailsService,ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockDetailsServiceImpl.class);


	@Autowired
	private IBloodBankStockDetailsDao iBloodBankStockDetailsDao;

	@Override
	public Response searchBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDetailsDao.searchBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("searchBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDetailsDao.saveBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("saveBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDetailsDao.updateBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("updateBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodBankStockByComponentId(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			return iBloodBankStockDetailsDao.getBloodBankStockByComponentId(dto);

		} catch (Exception e) {
			logger.error("getBloodBankStockByComponentId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
