package com.param.global.mpesa.Dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.CCAvenueLog;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" ,"unused"})
public class CCAvenueLogDaoImpl implements ICCAvenueLogDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(CCAvenueLogDaoImpl.class);

	@Autowired
	private SessionFactory SessionFactory;

	
	@Override
	public Response saveCCAvenueLog(CCAvenueLogDto dto) throws ApplicationException {
		try {
			
			CCAvenueLog mstr = new CCAvenueLog();
			mstr.setIsUsed(false);
			mstr.setMobileNo(dto.getMobileNo());
			mstr.setOrderNo(dto.getOrderNo());
			mstr.setRequestAmount(dto.getRequestAmount());
			mstr.setPatientId(dto.getPatientId()!= null ? dto.getPatientId()  : null);
			mstr.setPayLink(dto.getPayLink());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUnitId(dto.getUnitId());
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus('A');
			mstr.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			SessionFactory.getCurrentSession().save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);


		} catch (HibernateException he)
		{
			logger.error("saveCCAvenueLog failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e)
		{
			logger.error("saveCCAvenueLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCCAvenuePayementDetailsByMobileNo(String mobileNo, Integer patientId) throws ApplicationException {
		try {
			
			StringBuilder sb = new  StringBuilder();
				sb.append("select mrold.cc_avenue_log_id as \"ccCvenueLogId\" ,"
						+ "mrold.is_used as \"isUsed\", "
						+ "mrold.mobile_no as \"mobileNo\","
						+ " mrold.request_amount as \"requestAmount\","
						+ " mrold.order_no as \"orderNo\","
						+ " mrold.pay_link as \"payLink\","
						+ " mrold.result_code as \"resultCode\","
						+ " mrold.paid_amount as  \"paidAmount\","
						+ "to_char(mrold.created_date ,'YYYY-MM-DD HH24:mi:ss') as  \"createdDate\"  "
						+ " from billing.t_cc_avenue_log mrold where mrold.mobile_no ='"+mobileNo+"' ");
		
	        if(patientId!=null) {
		    	  sb.append(" AND mrold.patient_id =:patientId");
		      }
	        
	        sb.append(" and status= 'A' order by 1 desc limit 1 "); 
	        
	        Query createdQuery=SessionFactory.getCurrentSession().createSQLQuery(sb.toString());
			if(patientId!=null) {
				createdQuery.setParameter("patientId", patientId);
			}

	        CCAvenueLogDto dto=(CCAvenueLogDto) createdQuery
	        		.setResultTransformer(Transformers.aliasToBean(CCAvenueLogDto.class)).uniqueResult();
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, dto);

		} catch (HibernateException he) {
			logger.error("getCCAvenuePayementDetailsByMobileNo failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCCAvenuePayementDetailsByMobileNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updatePaidAmount(CCAvenueLogDto dto) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_cc_avenue_log set paid_amount= "+dto.getPaidAmount()+",result_code ='"+dto.getResultCode()+"'"+" ,result_description ='"+dto.getResultDescription()+"'"
					+ " WHERE cc_avenue_log_id =:logId ")
			.setParameter("logId", dto.getCcCvenueLogId())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updatePaidAmount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaidAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public String getpatientEmail(Integer patientId) throws ApplicationException {
		try {
			CCAvenueLogDto dto=(CCAvenueLogDto)SessionFactory.getCurrentSession().createSQLQuery("select email as \"emailId\" from patient.t_patient_registration tpr where patient_id="+patientId)
			.setResultTransformer(Transformers.aliasToBean(CCAvenueLogDto.class)).uniqueResult();
			
			if(dto != null && dto.getEmailId()!= null) {
				return dto.getEmailId();
			}else {
				return null;
			}
			
			

		} catch (HibernateException he) {
			logger.error("getpatientEmail failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getpatientEmail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCCAvenueAPIUrl(Integer unitId) throws ApplicationException {
		try {
			CCAvenueLogDto dto=(CCAvenueLogDto)SessionFactory.getCurrentSession().createSQLQuery("select ccavenue_api_url as \"payLink\", ccavenue_url_expired_time as \"ccavenueUrlExpiredTime\" from public.t_unit_application_billing_configuration where unit_id ="+unitId)
			.setResultTransformer(Transformers.aliasToBean(CCAvenueLogDto.class)).uniqueResult();
			
			if(dto!= null) {
				return new Response(SUCCESS, null, null, null, dto);
			}else {
				return new Response(SUCCESS, null, null, null, null);
			}
			

		} catch (HibernateException he) {
			logger.error("getCCAvenueAPIUrl failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCCAvenueAPIUrl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateCCAvenueStatus(Integer ccCvenueLogId) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_cc_avenue_log set status= 'I' WHERE cc_avenue_log_id =:logId ")
			.setParameter("logId", ccCvenueLogId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateCCAvenueStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCCAvenueStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateRazorPayStatus(Integer razorpayOrderDetId) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE public.m_razorpay_order_log_details set status= 'I' WHERE razorpay_order_det_id =:logId ")
			.setParameter("logId", razorpayOrderDetId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateRazorPayStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateRazorPayStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
