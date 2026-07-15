package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.razorpay.RazorpayPaymentReqDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IRazorPayDao {

	Response logRazorPayOrder(RazorpayPaymentReqDto reqDto) throws ApplicationException;

	Response getAppointmentDataByRazorPayOrderId(String razorPayOrderId)throws ApplicationException;

	Response getPayementDetailsByMobileNo(String mobileNo)throws ApplicationException;

	Response savePaymentLinkLog(RazorpayPaymentReqDto reqDto)throws ApplicationException;

	Response updatePaymentLinkLog(String razorpayPaymentLinkStatus, String razorpayPaymentLinkId)throws ApplicationException;

	Response getOrderLogDetailsByOrderId(String orderId)throws ApplicationException;

}
