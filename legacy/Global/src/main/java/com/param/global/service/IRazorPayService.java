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



}
