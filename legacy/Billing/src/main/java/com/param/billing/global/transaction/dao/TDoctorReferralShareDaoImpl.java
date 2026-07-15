package com.param.billing.global.transaction.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.TDoctorReferralShare;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TDoctorReferralShareDaoImpl extends GenericDao<TDoctorReferralShare, Integer> implements IDoctorReferralShareDao, ICommonConstants, IError{

	public TDoctorReferralShareDaoImpl() {
		super(TDoctorReferralShare.class);
	}

	
	@Override
	public Response getDoctorReferralShareList(TDoctorShareReqDto reqDto) throws ApplicationException {
		try{
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_LIST_BY_FILTER").getQueryString());

			if(reqDto.getDoctorId() != null)
				query.append(" AND docreffshare.referral_doc_id =:doctorId ");

			if(reqDto.getVisitTypeId() != null)
				query.append(" AND bm.visit_type_id =:visitTypeId ");

			if(reqDto.getVisitTypeId() != null && reqDto.getVisitTypeId() == IP)
				query.append(" and admdt.status = 'I' and admdt.is_discharge = 'D' ");
		
			 Date fromDate =(reqDto.getFromDate()!= null && !reqDto.getFromDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy"):TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId());
			 Date toDate =(reqDto.getToDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy"):TimeZoneComponent.getDateByzone(reqDto.getOrgUnitId());
			
		   Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
				   .setParameter("orgId", reqDto.getOrgId())
				   .setParameter("orgUnitId", reqDto.getOrgUnitId())
				   .setParameter("fromDate", fromDate).setDate("toDate", toDate);

			if(reqDto.getDoctorId() != null)
				sqlQuery.setParameter("doctorId", reqDto.getDoctorId());

			if(reqDto.getVisitTypeId() != null)
				sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

			List<TDoctorReferralShareDto> listTDoctorReferralShareDto = sqlQuery
				   .setResultTransformer(Transformers.aliasToBean(TDoctorReferralShareDto.class)).list();


			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorReferralShareDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(TDoctorShareReqDto reqDto)
			throws ApplicationException {
		try{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_PAYMENT_VOUCHER_DTLS_BY_REFERRAL_SHARE_ID").getQueryString());
		   List<TDoctorShareRespDto> listTDoctorShareRespDto= sessionFactory.getCurrentSession().createSQLQuery(query.toString()).setParameter("orgId", reqDto.getOrgId())
				   .setParameter("referralShareId", reqDto.getReferralShareId())
				   .setResultTransformer(Transformers.aliasToBean(TDoctorShareRespDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listTDoctorShareRespDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorReferralShare(TDoctorReferralShareDto tDoctorShareReqDto) throws ServiceException {
		try{
			TDoctorReferralShare tDocShareVoucher = findById(tDoctorShareReqDto.getReferralShareId());
			if (tDocShareVoucher.getReferralDocId() != 0)
			{
				tDocShareVoucher.setReferralShareId(tDocShareVoucher.getReferralShareId());
				//double tempDueAmt=tDoctorShareReqDto.getDueShareAmt()-tDoctorShareReqDto.getProcessedShareAmt();
				tDocShareVoucher.setDueShareAmt(0.0);
				tDocShareVoucher.setProcessedShareAmt(tDoctorShareReqDto.getDueShareAmt());
				tDocShareVoucher.setReferralSharePer(tDoctorShareReqDto.getReferralSharePer());
				tDocShareVoucher.setUpdatedBy(tDoctorShareReqDto.getUpdatedBy());
				tDocShareVoucher.setUpdatedDate(TimeZoneComponent.getDateByzone(tDoctorShareReqDto.getOrgUnitId()).getTime());
				tDocShareVoucher = update(tDocShareVoucher);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDocShareVoucher);
			}else {
				
				return new Response<>(ERROR, ERROR, COMMON_ERROR_MESSAGE, null, tDocShareVoucher);
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDoctorReferralShare(TDoctorReferralShareDto tDoctorReferralShareDto) throws ServiceException {
		try{
			    TDoctorReferralShare tDoctorReferralShare =  new TDoctorReferralShare();
			    tDoctorReferralShare.setDueShareAmt(tDoctorReferralShareDto.getDueShareAmt());
			    tDoctorReferralShare.setUpdatedBy(tDoctorReferralShareDto.getUpdatedBy());
			    tDoctorReferralShare.setDiscount(tDoctorReferralShareDto.getDiscount());
			    tDoctorReferralShare.setBillId(tDoctorReferralShareDto.getBillId());
			    tDoctorReferralShare.setAdmissionId(tDoctorReferralShareDto.getAdmissionId());
			    tDoctorReferralShare.setIsBillCrDr(tDoctorReferralShareDto.getIsBillCrDr());
			    tDoctorReferralShare.setOrgId(tDoctorReferralShareDto.getOrgId());
			    tDoctorReferralShare.setOrgUnitId(tDoctorReferralShareDto.getOrgUnitId());
			    tDoctorReferralShare.setNetAmount(tDoctorReferralShareDto.getNetAmount());
			    tDoctorReferralShare.setTotalAmount(tDoctorReferralShareDto.getTotalAmount());
			    tDoctorReferralShare.setStatus(ACTIVE);
			    tDoctorReferralShare.setReferralDocId(tDoctorReferralShareDto.getReferralDocId());
			    tDoctorReferralShare.setReferralSharePer(tDoctorReferralShareDto.getReferralSharePer());
			    tDoctorReferralShare.setTaxId(tDoctorReferralShareDto.getTaxId());
			    tDoctorReferralShare.setTaxType(tDoctorReferralShareDto.getTaxType());
			    tDoctorReferralShare.setTaxAmt(tDoctorReferralShareDto.getTaxAmt());
			    tDoctorReferralShare.setConcession(tDoctorReferralShareDto.getConcession());
			    tDoctorReferralShare.setProcessedShareAmt(0.0);
			   // tDoctorReferralShare.setDueShareAmt(((tDoctorReferralShareDto.getNetAmount()*tDoctorReferralShareDto.getReferralSharePer())/100));
			    tDoctorReferralShare.setDueShareAmt(tDoctorReferralShareDto.getNetAmount());
			    tDoctorReferralShare.setCreatedBy(tDoctorReferralShareDto.getCreatedBy());
			    tDoctorReferralShare.setCreatedDate(tDoctorReferralShareDto.getCreatedDate());
				tDoctorReferralShare.setUpdatedDate(TimeZoneComponent.getDateByzone(tDoctorReferralShareDto.getOrgUnitId()).getTime());
				tDoctorReferralShare = save(tDoctorReferralShare);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, tDoctorReferralShare);
			
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDoctorReferralShareMasterList(TDoctorShareReqDto reqDto) throws ApplicationException {
      try{
    	  
    	  List<Integer> listDouble = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_MASTER_LIST_BY_DOCTOR_REFERRAL_ID")
					.setParameter("orgId", reqDto.getOrgId()).setParameter("orgUnitId", reqDto.getOrgId()).setParameter("docRefShareId", reqDto.getDoctorShareId()).list();
    
		/*	StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_MASTER_LIST_BY_DOCTOR_REFERRAL_ID").getQueryString());
		   List<Double> listMDoctorReferralShareDto= sessionFactory.getCurrentSession().createQuery(query.toString()).setInteger("orgId", reqDto.getOrgId()).setInteger("orgUnitId", reqDto.getOrgId()).setInteger("docRefShareId", reqDto.getDoctorShareId()).setResultTransformer(Transformers.aliasToBean(Double.class)).list();
		*/	return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listDouble, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	
}
