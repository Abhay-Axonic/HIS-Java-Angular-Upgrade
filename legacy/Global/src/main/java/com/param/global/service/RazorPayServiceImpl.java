package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IRazorPayDao;
import com.param.global.mpesa.Dao.ICCAvenueLogDao;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.global.razorpay.RazorpayPaymentReqDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Service
public class RazorPayServiceImpl implements IRazorPayService,ICommonConstants {

	final static Logger logger = Logger.getLogger(RazorPayServiceImpl.class);

	@Autowired
	private IRazorPayDao razorPayDao;

	@Autowired
	ICCAvenueLogDao iCCAvenueLogDao;


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



}
