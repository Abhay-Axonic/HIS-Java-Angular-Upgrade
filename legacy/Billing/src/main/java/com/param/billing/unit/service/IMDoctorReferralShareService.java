package com.param.billing.unit.service;

import javax.transaction.Transactional;

import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface IMDoctorReferralShareService {
	
	
	@Transactional
	public  Response saveDoctorReferralShare(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ApplicationException;
	@Transactional
	public Response getDoctorReferralShareDetails(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ApplicationException;
	@Transactional
	public Response getDoctorReferralShareDetailsByRefDocId(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ApplicationException;
	@Transactional
	public Response updateStatusDoctorReferralShare(DoctorReferralShareMapperDto dto)throws ApplicationException;
	
	/*@Transactional
	public  Response getDoctorGradeMaster(Integer orgId, Integer orgUnitId)throws ApplicationException;
*/	
	
	/*@Transactional
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException;*/
}
