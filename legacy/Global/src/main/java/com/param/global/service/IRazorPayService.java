package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.global.razorpay.RazorpayPaymentReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface IRazorPayService
{

	@Transactional
	Response logRazorPayOrder(RazorpayPaymentReqDto reqDto)throws ApplicationException;

	@Transactional
	Response getAppointmentDataByRazorPayOrderId(String razorPayOrderId)throws ApplicationException;

	@Transactional
	Response getPayementDetailsByMobileNo(String mobileNo)throws ApplicationException;
	
	@Transactional
	Response savePaymentLinkLog(RazorpayPaymentReqDto reqDto)throws ApplicationException;
	
	@Transactional
	Response updatePaymentLinkLog(String razorpayPaymentLinkStatus, String razorpayPaymentLinkId)throws ApplicationException;
	
	@Transactional
	Response getCCAvenuePayementDetailsByMobileNo(String mobileNo, Integer patientId)throws ApplicationException;

	@Transactional
	Response saveCCAvenueLog(CCAvenueLogDto reqDto)throws ApplicationException;

	@Transactional
	Response updatePaidAmount(CCAvenueLogDto dto)throws ApplicationException;

	@Transactional
	String getpatientEmail(Integer patientId)throws ApplicationException;

	@Transactional
	Response getCCAvenueAPIUrl(Integer unitId)throws ApplicationException;

	@Transactional
	Response updateCCAvenueStatus(Integer ccCvenueLogId)throws ApplicationException;

	@Transactional
	Response updateRazorPayStatus(Integer razorpayOrderDetId)throws ApplicationException;

	@Transactional
	Response getOrderLogDetailsByOrderId(String orderId)throws ApplicationException;

	/**
	 * Verifies a Razorpay payment claim against Razorpay's own signature and order status,
	 * rather than trusting any client-supplied amount/status. Returns a SUCCESS Response whose
	 * getObject() is the verified paid amount in rupees (Double) on success, or an ERROR
	 * Response with a message explaining the failure otherwise. Never throws for an invalid/
	 * unverifiable payment -- callers should check getStatus(), not rely on an exception.
	 */
	@Transactional
	Response verifyRazorpayPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			Integer unitId, Integer organizationId) throws ApplicationException;

}
