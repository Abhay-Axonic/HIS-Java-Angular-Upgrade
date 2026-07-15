package com.param.global.mpesa.Dao;

import com.param.global.common.Response;
import com.param.global.mpesa.dto.CCAvenueLogDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICCAvenueLogDao {

	Response saveCCAvenueLog(CCAvenueLogDto dto)throws ApplicationException;

	Response getCCAvenuePayementDetailsByMobileNo(String mobileNo,Integer patientId)throws ApplicationException;

	Response updatePaidAmount(CCAvenueLogDto dto)throws ApplicationException;

	String getpatientEmail(Integer patientId)throws ApplicationException;

	Response getCCAvenueAPIUrl(Integer unitId)throws ApplicationException;

	Response updateCCAvenueStatus(Integer ccCvenueLogId)throws ApplicationException;

	Response updateRazorPayStatus(Integer razorpayOrderDetId)throws ApplicationException;
	
}
