package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.global.billing.dao.ICurrencyMasterDao;
import com.param.global.billing.dto.CurrencyMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrencyMasterServiceImpl implements ICurrencyMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(CurrencyMasterServiceImpl.class);

	@Autowired
	ICurrencyMasterDao iCurrencyMasterDao;

	@Override
	public Response saveCurrencyMaster(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		Response response = iCurrencyMasterDao.getCurrencyMasterByName(currencyMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iCurrencyMasterDao.saveCurrencyMaster(currencyMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterById(int currencyId, int orgId) throws ApplicationException {
		try {
			return iCurrencyMasterDao.getCurrencyMasterById(currencyId, orgId);
		} catch (Exception e) {
			logger.error("getCurrencyMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterList(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			return iCurrencyMasterDao.getCurrencyMasterList(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override //Added by Dilip
	public Response getAllCurrencyMasterList(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			return iCurrencyMasterDao.getAllCurrencyMasterList(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getAllCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyMaster(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		Response deptResponse = iCurrencyMasterDao.getCurrencyMasterByNameNotId(currencyMasterDto);

		try {
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iCurrencyMasterDao.updateCurrencyMaster(currencyMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyMasterStatus(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			return iCurrencyMasterDao.updateCurrencyMasterStatus(currencyMasterDto);
		} catch (Exception e) {
			logger.error("updateCurrencyMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyCount(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			return iCurrencyMasterDao.getCurrencyCount(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
