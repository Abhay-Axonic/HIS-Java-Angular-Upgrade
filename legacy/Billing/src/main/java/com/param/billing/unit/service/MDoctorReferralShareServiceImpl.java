package com.param.billing.unit.service;

import java.util.Date;
import java.util.ListIterator;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;
import com.param.billing.global.transaction.model.DoctorReferralShareMapper;
import com.param.billing.global.transaction.model.MDoctorReferralShare;
import com.param.billing.unit.dao.IMDoctorReferralShareDao;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MDoctorReferralShareServiceImpl implements IMDoctorReferralShareService, ICommonConstants {

	@Autowired
	IMDoctorReferralShareDao iMDoctorReferralShareDao;

	@Override
	public Response saveDoctorReferralShare(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)
			throws ApplicationException {
		try{
			Response chkingResponse=iMDoctorReferralShareDao.checkALreadyExistOrNot(mDoctorReferralShareReqDto);
			if(chkingResponse.getListObject()!=null && chkingResponse.getListObject().size()>0){
				 return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			Response res=iMDoctorReferralShareDao.saveDoctorReferralShareDao(mDoctorReferralShareReqDto);
			if(res.getObject()!=null){
				MDoctorReferralShare mDoctorReferralShare=(MDoctorReferralShare)res.getObject();
				if(mDoctorReferralShareReqDto.getListMDoctorReferralShareDto() != null && mDoctorReferralShareReqDto.getListMDoctorReferralShareDto().size() > 0){
					ListIterator<DoctorReferralShareMapperDto> mpperList=mDoctorReferralShareReqDto.getListMDoctorReferralShareDto().listIterator();
					while(mpperList.hasNext()){
						DoctorReferralShareMapper mstMapper=new DoctorReferralShareMapper();
						DoctorReferralShareMapperDto mpper=mpperList.next();
						mstMapper.setCreatedBy(mDoctorReferralShareReqDto.getCreatedBy());
						mstMapper.setUpdatedBy(mDoctorReferralShareReqDto.getUpdatedBy());
						mstMapper.setReferralShareId(mDoctorReferralShare.getReferralShareId());
						mstMapper.setEffectiveDate(new Date(mDoctorReferralShare.getEffectiveDate()));
						mstMapper.setNoOfCases(mpper.getNoOfCases());
						mstMapper.setShare(mpper.getShare());
						mstMapper.setOrgId(mDoctorReferralShareReqDto.getOrgId());
						mstMapper.setOrgUnitId(mDoctorReferralShareReqDto.getUnitId());
						mstMapper.setReferraledDocId(mpper.getReferraledDocId());
						mstMapper.setStatus('A');
						mstMapper.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(mDoctorReferralShareReqDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						mstMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(mDoctorReferralShareReqDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					    iMDoctorReferralShareDao.saveDoctorReferralMapper(mstMapper);
					}
				}
			}
			 return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorReferralShareDetails(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)
			throws ApplicationException {
		try{
			return iMDoctorReferralShareDao.getDoctorReferralShareDetails(mDoctorReferralShareReqDto);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorReferralShareDetailsByRefDocId(MDoctorReferralShareReqDto mDoctorReferralShareReqDto) {
		try{
			return iMDoctorReferralShareDao.getDoctorReferralShareDetailsByRefDocId(mDoctorReferralShareReqDto);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStatusDoctorReferralShare(DoctorReferralShareMapperDto dto)
			throws ApplicationException {
		try{
			return iMDoctorReferralShareDao.updateStatusDoctorReferralShare(dto);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	/*@Override
	public Response saveDoctorReferralShare(MDoctorReferralShareReqDto reqDto)
			throws ApplicationException {
		try{
			if(reqDto != null){
			//save Doctor Referral Share details
			if(reqDto.getListMDoctorReferralShareDto() != null && reqDto.getListMDoctorReferralShareDto().size() > 0){
				ListIterator<MDoctorReferralShareDto> servicDtlsItr = reqDto.getListMDoctorReferralShareDto().listIterator();
				MDoctorReferralShareDto mDoctorReferralShareReqDto = null;
				while(servicDtlsItr.hasNext()){
					mDoctorReferralShareReqDto = servicDtlsItr.next();
					mDoctorReferralShareReqDto.setOrgId(reqDto.getOrgId());
					mDoctorReferralShareReqDto.setOrgUnitId(reqDto.getOrgId());
					iMDoctorReferralShareDao.saveDoctorReferralShareDao(mDoctorReferralShareReqDto);
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, DOCTOR_REFERRAL_SHARE, null, null);
		 }
			
		  return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			
		}catch(ServiceException se){
			s
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}*/
/*
	@Override
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.searchCompanyContract(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/


	/*@Override
	public Response getDoctorGradeMaster(Integer orgId, Integer orgUnitId) throws ApplicationException {
		try {
			
			return iGlobalDoctorShareGroupWiseDao.getDoctorGradeList(orgId, orgUnitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/

	
}
