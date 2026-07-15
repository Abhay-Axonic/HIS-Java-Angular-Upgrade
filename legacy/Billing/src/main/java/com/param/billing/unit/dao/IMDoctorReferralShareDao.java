package com.param.billing.unit.dao;

import java.util.Date;

import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;
import com.param.billing.global.transaction.model.DoctorReferalShareCountMapper;
import com.param.billing.global.transaction.model.DoctorReferralShareMapper;
import com.param.billing.global.transaction.model.MDoctorReferralShare;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IMDoctorReferralShareDao extends IGenericDao<MDoctorReferralShare, Integer>{
//	public Response saveDoctorReferralShareDao(MDoctorReferralShareDto detailDto)throws ServiceException;

	public Response saveDoctorReferralShareDao(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ServiceException;

	public Response saveDoctorReferralMapper(DoctorReferralShareMapper mstMapper)throws ServiceException;
	
	public Response getDoctorReferralDetails()throws ServiceException;

	//get details of refer doctor
	public Response getReferralDetails(Integer referralDocId, Integer visitTypeId, Integer orgId, Integer unitId)throws ServiceException;
	public Response saveReferralCount(DoctorReferalShareCountMapper master)throws ServiceException;
	public Response updateReferralByReferralDocId(MDoctorReferralShareReqDto dto)throws ServiceException;
	public Response updateReferralCountMapper(MDoctorReferralShareReqDto dto)throws ServiceException;
	public Response getNoOfCount(Integer referralDocId)throws ServiceException;
	public Response getShareDetails(Integer referralDocId)throws ServiceException;
	public Response getAllReferralCountDetails(Integer referralDocId, Integer visitTypeId, Integer orgId, Integer unitId)throws ServiceException;
	public Response getDoctorReferralDetailsByDoctorReferralId(Integer referralDocId, Integer visitTypeId, Integer orgId, Integer unitId)throws ServiceException;
	//public Response getDoctorReferralShareList(Integer orgId,Integer orgUnitId) throws ApplicationException;

	public Response getEncounterAndAdmissionIds(Integer referraledDocId, Date firstDate, Integer visitTypeId, Integer orgId, Integer unitId)throws ServiceException;
	public Response updateReferralCountDetails(Integer referraledDocId, Integer visitTypeId, Integer orgId, Integer unitId)throws ServiceException;

	public Response getDoctorReferralShareDetails(MDoctorReferralShareReqDto mDoctorReferralShareReqDto);

	public Response getDoctorReferralShareDetailsByRefDocId(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ServiceException;

	public Response updateStatusDoctorReferralShare(DoctorReferralShareMapperDto dto)throws ServiceException;

	public Response checkALreadyExistOrNot(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)throws ServiceException;
}
