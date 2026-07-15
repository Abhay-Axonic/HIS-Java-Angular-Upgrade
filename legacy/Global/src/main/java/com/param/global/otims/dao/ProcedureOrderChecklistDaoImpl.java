package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderChecklistDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.otims.model.ProcedureOrderChecklist;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class ProcedureOrderChecklistDaoImpl extends GenericDao<ProcedureOrderChecklist, Integer>implements IProcedureOrderChecklistDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOrderChecklistDaoImpl.class);

	
	public ProcedureOrderChecklistDaoImpl() {
		super(ProcedureOrderChecklist.class);
	}
	
	@Override
	public Response getChecklistDetailsByProcedureOrderId(ProcedureOrderConsentDto consentDto) throws ApplicationException {
		try{
			List<ProcedureOrderChecklistDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_DETAILS_BY_PROCEDURE_ORDER_ID")
					.setParameter("organizationId", consentDto.getOrganizationId())
					.setParameter("unitId", consentDto.getUnitId())
					.setParameter("procedureOrderId", consentDto.getProcedureOrderId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderChecklistDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response saveProcedureOrderCheckList(List<ProcedureOrderChecklistDto> procedureOrderCheckList, int proReqId,
			Integer procedureOrderId) throws ApplicationException {
		try{
		//	Session session=sessionFactory.getCurrentSession();
		/*	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_checklist set status='I' "
					+ " where procedure_order_id='"+procedureOrderId+"' ")
					.executeUpdate();*/

			for (ProcedureOrderChecklistDto procedureOrderChecklistDto : procedureOrderCheckList) {
				ProcedureOrderChecklist mstr = new ProcedureOrderChecklist();
				mstr.setProcedureOrderId(procedureOrderId);
				mstr.setProcedureRequestId(proReqId);
				mstr.setCreatedBy(procedureOrderChecklistDto.getCreatedBy());
				mstr.setChecklistId(procedureOrderChecklistDto.getChecklistId());
				mstr.setCreatedDate(procedureOrderChecklistDto.getCreatedDate() != null && !procedureOrderChecklistDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderChecklistDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderChecklistDto.getUnitId()));
				mstr.setOrganizationId(procedureOrderChecklistDto.getOrganizationId());
				mstr.setUnitId(procedureOrderChecklistDto.getUnitId());
				mstr.setUpdatedBy(procedureOrderChecklistDto.getUpdatedBy());
				mstr.setUpdatedDate(procedureOrderChecklistDto.getUpdatedDate() != null && !procedureOrderChecklistDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderChecklistDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderChecklistDto.getUnitId()));
				
				if(procedureOrderChecklistDto.getProcedureOrderChecklistId()!=null) {
					mstr.setProcedureOrderChecklistId(procedureOrderChecklistDto.getProcedureOrderChecklistId());
					mstr.setStatus(procedureOrderChecklistDto.getStatus());
					sessionFactory.getCurrentSession().update(mstr);
				}else {
					mstr.setStatus('A');
					sessionFactory.getCurrentSession().save(mstr);
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
		
	}

	@Override
	public Response cancelProcedureOrderCheckList(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_checklist set status = 'I' "
					+ " where procedure_request_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelProcedureOrderCheckList failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureOrderCheckList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
