package com.param.global.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IRazorPayDao;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.mpesa.Dao.ICCAvenueLogDao;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.global.razorpay.RazorpayPaymentReqDto;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Service
public class RazorPayServiceImpl implements IRazorPayService,ICommonConstants {

	final static Logger logger = Logger.getLogger(RazorPayServiceImpl.class);

	@Autowired
	private IRazorPayDao razorPayDao;

	@Autowired
	ICCAvenueLogDao iCCAvenueLogDao;

	@Autowired
	private IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;


	@Override
	public Response logRazorPayOrder(RazorpayPaymentReqDto reqDto) throws ApplicationException {
		try {
			return razorPayDao.logRazorPayOrder(reqDto);
		} catch (Exception e) {
			logger.error("logRazorPayOrder failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAppointmentDataByRazorPayOrderId(String razorPayOrderId) throws ApplicationException {
		try {
			return razorPayDao.getAppointmentDataByRazorPayOrderId(razorPayOrderId);
		} catch (Exception e) {
			logger.error("getAppointmentDataByRazorPayOrderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPayementDetailsByMobileNo(String mobileNo) throws ApplicationException {
		try {
			return razorPayDao.getPayementDetailsByMobileNo(mobileNo);
		} catch (Exception e) {
			logger.error("getPayementDetailsByMobileNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePaymentLinkLog(RazorpayPaymentReqDto reqDto) throws ApplicationException {
		try {
			return razorPayDao.savePaymentLinkLog(reqDto);
		} catch (Exception e) {
			logger.error("savePaymentLinkLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaymentLinkLog(String razorpayPaymentLinkStatus, String razorpayPaymentLinkId)
			throws ApplicationException {
		try {
			return razorPayDao.updatePaymentLinkLog(razorpayPaymentLinkStatus,razorpayPaymentLinkId);
		} catch (Exception e) {
			logger.error("updatePaymentLinkLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCCAvenuePayementDetailsByMobileNo(String mobileNo, Integer patientId) throws ApplicationException {
		try {
			return iCCAvenueLogDao.getCCAvenuePayementDetailsByMobileNo(mobileNo,patientId);
		} catch (Exception e) {
			logger.error("getCCAvenuePayementDetailsByMobileNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveCCAvenueLog(CCAvenueLogDto reqDto) throws ApplicationException {
		try {
			return iCCAvenueLogDao.saveCCAvenueLog(reqDto);
		} catch (Exception e) {
			logger.error("saveCCAvenueLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updatePaidAmount(CCAvenueLogDto dto) throws ApplicationException {
		try {
			return iCCAvenueLogDao.updatePaidAmount(dto);
		} catch (Exception e) {
			logger.error("updatePaidAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String getpatientEmail(Integer patientId) throws ApplicationException {
		try {
			return iCCAvenueLogDao.getpatientEmail(patientId);
		} catch (Exception e) {
			logger.error("getpatientEmail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCCAvenueAPIUrl(Integer unitId) throws ApplicationException {
		try {
			return iCCAvenueLogDao.getCCAvenueAPIUrl(unitId);
		} catch (Exception e) {
			logger.error("getCCAvenueAPIUrl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCCAvenueStatus(Integer ccCvenueLogId) throws ApplicationException {
		try {
			return iCCAvenueLogDao.updateCCAvenueStatus(ccCvenueLogId);
		} catch (Exception e) {
			logger.error("updateCCAvenueStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateRazorPayStatus(Integer razorpayOrderDetId) throws ApplicationException {
		try {
			return iCCAvenueLogDao.updateRazorPayStatus(razorpayOrderDetId);
		} catch (Exception e) {
			logger.error("updateRazorPayStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOrderLogDetailsByOrderId(String orderId) throws ApplicationException {
		try {
			return razorPayDao.getOrderLogDetailsByOrderId(orderId);
		} catch (Exception e) {
			logger.error("getOrderLogDetailsByOrderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response verifyRazorpayPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			Integer unitId, Integer organizationId) throws ApplicationException {
		try {
			if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
				return new Response(ERROR, ERROR_CODE, "Missing Razorpay verification fields", null, null);
			}

			UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
			unitAppConfigReqDto.setOrganizationId(organizationId);
			unitAppConfigReqDto.setUnitId(unitId);
			Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
			if (appConfigRes == null || appConfigRes.getObject() == null) {
				logger.error("PAYMENT_RECONCILIATION_GAP: verifyRazorpayPayment -- no Razorpay unit config found for order " + razorpayOrderId + " (unitId=" + unitId + ", organizationId=" + organizationId + ")");
				return new Response(ERROR, ERROR_CODE, "No Razorpay configuration found for this unit", null, null);
			}
			UnitApplicationConfigurationDto unitConfig = (UnitApplicationConfigurationDto) appConfigRes.getObject();

			boolean signatureValid;
			try {
				JSONObject sigAttrs = new JSONObject();
				sigAttrs.put("razorpay_order_id", razorpayOrderId);
				sigAttrs.put("razorpay_payment_id", razorpayPaymentId);
				sigAttrs.put("razorpay_signature", razorpaySignature);
				signatureValid = Utils.verifyPaymentSignature(sigAttrs, unitConfig.getRazorPaySecretKey());
			} catch (Exception e) {
				logger.error("PAYMENT_RECONCILIATION_GAP: verifyRazorpayPayment -- signature check threw for order " + razorpayOrderId + ". " + e.getMessage(), e);
				return new Response(ERROR, ERROR_CODE, "Razorpay signature verification failed", null, null);
			}
			if (!signatureValid) {
				logger.error("PAYMENT_RECONCILIATION_GAP: verifyRazorpayPayment -- invalid Razorpay signature for order " + razorpayOrderId);
				return new Response(ERROR, ERROR_CODE, "Invalid Razorpay signature", null, null);
			}

			Order verifiedOrder;
			try {
				RazorpayClient client = new RazorpayClient(unitConfig.getRazorPayKeyId(), unitConfig.getRazorPaySecretKey());
				verifiedOrder = client.Orders.fetch(razorpayOrderId);
			} catch (Exception e) {
				logger.error("PAYMENT_RECONCILIATION_GAP: verifyRazorpayPayment -- could not fetch order " + razorpayOrderId + " from Razorpay's API. " + e.getMessage(), e);
				return new Response(ERROR, ERROR_CODE, "Could not verify Razorpay order", null, null);
			}
			String orderStatus = verifiedOrder.get("status");
			if (!"paid".equals(orderStatus)) {
				logger.error("PAYMENT_RECONCILIATION_GAP: verifyRazorpayPayment -- Razorpay order " + razorpayOrderId + " status is \"" + orderStatus + "\", not \"paid\"");
				return new Response(ERROR, ERROR_CODE, "Razorpay order is not paid", null, null);
			}

			Integer amountPaidPaise = verifiedOrder.get("amount_paid");
			Double verifiedAmount = BigDecimal.valueOf(amountPaidPaise).divide(BigDecimal.valueOf(100)).doubleValue();
			return new Response(SUCCESS, SUCCESS_CODE, "Verified", null, verifiedAmount);
		} catch (Exception e) {
			logger.error("verifyRazorpayPayment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
