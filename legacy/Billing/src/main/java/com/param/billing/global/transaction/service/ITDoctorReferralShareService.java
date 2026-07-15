package com.param.billing.global.transaction.service;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITDoctorReferralShareService {
	public Response getDoctorReferralShareList(TDoctorShareReqDto tDoctorShareDto) throws ApplicationException ;
	public Response saveReferralDoctorShareProcessList(TDoctorShareProcessReqDto reqDto) throws ApplicationException; 
	public Response getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(TDoctorShareReqDto tDoctorShareReqDto) throws ApplicationException;
	//save doctor referral share data through cron job
	public Response saveDoctorReferralShare(TDoctorReferralShareDto TDoctorReferralShareDto)throws ServiceException;
	
}
