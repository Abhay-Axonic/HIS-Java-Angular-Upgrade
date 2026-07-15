package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.otims.model.ProcedureOrderConsent;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ProcedureOrderConsentDaoImpl extends GenericDao<ProcedureOrderConsent, Integer>implements IProcedureOrderConsentDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOrderConsentDaoImpl.class);

	
	public ProcedureOrderConsentDaoImpl() {
		super(ProcedureOrderConsent.class);
	}

	@Override
	public Response getConsentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException {
		try{
			List<ProcedureOrderConsentDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_DETAILS_BY_PROCEDURE_ORDER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("procedureOrderId", dto.getProcedureOrderId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderConsentDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveProcedureOrderConsent(List<ProcedureOrderConsentDto> procedureOrderConsentList, int proReqId,Integer procedureOrderId) throws ApplicationException {
		try{
		/*	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_consent set status='I' "
					+ " where procedure_order_id='"+procedureOrderId+"' ")
					.executeUpdate();*/
			
			for (ProcedureOrderConsentDto procedureOrderConsentDto : procedureOrderConsentList) {
				ProcedureOrderConsent mstr = new ProcedureOrderConsent();
				mstr.setProcedureOrderId(procedureOrderId);
				mstr.setProcedureRequestId(proReqId);
				mstr.setConsentId(procedureOrderConsentDto.getConsentId());
				mstr.setCreatedBy(procedureOrderConsentDto.getCreatedBy());
				mstr.setCreatedDate(TimeZoneComponent.getDateByzone(procedureOrderConsentDto.getUnitId()));
				mstr.setOrganizationId(procedureOrderConsentDto.getOrganizationId());
				mstr.setUnitId(procedureOrderConsentDto.getUnitId());
				mstr.setUpdatedBy(procedureOrderConsentDto.getUpdatedBy());
				mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureOrderConsentDto.getUnitId()));
				mstr.setStatus('A');
				mstr.setProcedureOrderConsentId(procedureOrderConsentDto.getProcedureOrderConsentId()!=null?procedureOrderConsentDto.getProcedureOrderConsentId():null);
				sessionFactory.getCurrentSession().saveOrUpdate(mstr);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelProcedureOrderConsent(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_consent set status = 'I' "
					+ " where procedure_request_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelProcedureOrderConsent failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureOrderConsent failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
