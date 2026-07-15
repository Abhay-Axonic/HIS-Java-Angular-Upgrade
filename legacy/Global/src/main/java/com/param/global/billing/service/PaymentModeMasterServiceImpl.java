package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IPaymentModeMasterDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PaymentEntitlementMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class PaymentModeMasterServiceImpl implements IPaymentModeMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(PaymentModeMasterServiceImpl.class);

	@Autowired
	IPaymentModeMasterDao iPaymentModeMasterDao;

	@Override
	public Response savePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		Response res = iPaymentModeMasterDao.getPaymentModeMasterListByName(paymentEntitlementMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iPaymentModeMasterDao.savePaymentModeMaster(paymentEntitlementMasterDto);
			}
		} catch (Exception e) {
			logger.error("savePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterById(int payModeId, int orgId) throws ApplicationException {
		try {
			return iPaymentModeMasterDao.getPaymentModeMasterById(payModeId, orgId);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentModeMasterDao.getPaymentModeMasterList(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		Response res = iPaymentModeMasterDao.getPaymentModeMasterListByNameNotById(paymentEntitlementMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iPaymentModeMasterDao.updatePaymentModeMaster(paymentEntitlementMasterDto);
			}
		} catch (Exception e) {
			logger.error("updatePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymentModeMasterStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentModeMasterDao.updatePaymentModeMasterStatus(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("updatePaymentModeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentModeMasterCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentModeMasterDao.getPaymentModeMasterCount(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActivePaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentModeMasterDao.getActivePaymentModeMasterList(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getActivePaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
