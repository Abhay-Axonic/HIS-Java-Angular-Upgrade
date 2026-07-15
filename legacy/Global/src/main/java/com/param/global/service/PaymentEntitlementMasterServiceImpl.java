package com.param.global.service;

import in.param.exception.ApplicationException;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPaymentEntitlementMasterDao;
import com.param.global.dto.PaymentEntitlementMasterDto;

@Service
@SuppressWarnings("rawtypes")
public class PaymentEntitlementMasterServiceImpl implements IPaymentEntitlementMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(PaymentEntitlementMasterServiceImpl.class);

	@Autowired
	IPaymentEntitlementMasterDao iPaymentEntitlementMasterDao;

	@Override
	public Response getPaymentEntitlementMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentEntitlementMasterDao.getPaymentEntitlementMaster(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymentEntitlementMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymentEntitlementListByOrgId(int orgId) throws ApplicationException {

		try {
			return iPaymentEntitlementMasterDao.getPaymentEntitlementListByOrgId(orgId);
		} catch (Exception e) {
			logger.error("getPaymentEntitlementListByOrgId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementById(int orgId, int paymentEntId) throws ApplicationException {
		try {
			return iPaymentEntitlementMasterDao.getPaymententitlementById(orgId, paymentEntId);
		} catch (Exception e) {
			logger.error("getPaymententitlementById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentEntitlementMasterDao.getPaymententitlementList(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymententitlementList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		Response res = iPaymentEntitlementMasterDao.getPaymententitlementByName(paymentEntitlementMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iPaymentEntitlementMasterDao.savePaymententitlement(paymentEntitlementMasterDto);
			}

		} catch (Exception e) {
			logger.error("savePaymententitlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		Response res = iPaymentEntitlementMasterDao.getPaymententitlementByNameNotById(paymentEntitlementMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iPaymentEntitlementMasterDao.updatePaymententitlement(paymentEntitlementMasterDto);
			}
		} catch (Exception e) {
			logger.error("updatePaymententitlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymententitlementStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentEntitlementMasterDao.updatePaymententitlementStatus(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("updatePaymententitlementStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPaymententitlementCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)
			throws ApplicationException {
		try {
			return iPaymentEntitlementMasterDao.getPaymententitlementCount(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymententitlementCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
