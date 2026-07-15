package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.model.TDoctorReferralShare;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorReferralShareDao extends IGenericDao<TDoctorReferralShare, Integer>{
	public Response getDoctorReferralShareList(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	
	public Response getDoctorReferralShareMasterList(TDoctorShareReqDto tDoctorShareDto)throws ApplicationException;
	
	public Response getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(TDoctorShareReqDto tDoctorShareReqDto) throws ApplicationException;
	public Response updateDoctorReferralShare(TDoctorReferralShareDto TDoctorReferralShareDto)throws ServiceException;
	public Response saveDoctorReferralShare(TDoctorReferralShareDto TDoctorReferralShareDto)throws ServiceException;
}
