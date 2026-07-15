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
import com.param.global.otims.dto.ProcedureOrderServiceDto;
import com.param.otims.model.ProcedureOrderService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ProcedureOrderServiceDaoImpl extends GenericDao<ProcedureOrderService, Integer>implements IProcedureOrderServiceDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOrderServiceDaoImpl.class);

	
	public ProcedureOrderServiceDaoImpl() {
		super(ProcedureOrderService.class);
	}

	@Override
	public Response getServiceDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException {
		try{
			List<ProcedureOrderServiceDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ORDER_SERVICE_BY_PROCEDURE_ORDER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("procedureOrderId", dto.getProcedureOrderId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderServiceDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveProcedureOrderServiceList(List<ProcedureOrderServiceDto> procedureOrderServiceList,
			int proReqId, Integer procedureOrderId) throws ApplicationException {
		try{
			//Session session=sessionFactory.getCurrentSession();
		/*	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_service set status='I' "
					+ " where procedure_order_id='"+procedureOrderId+"' ")
					.executeUpdate();*/

			for (ProcedureOrderServiceDto procedureOrderServiceDto : procedureOrderServiceList) {
				ProcedureOrderService mstr = new ProcedureOrderService();
				mstr.setProcedureOrderId(procedureOrderId);
				mstr.setProcedureRequestId(proReqId);
				mstr.setServiceId(procedureOrderServiceDto.getServiceId());
				mstr.setCreatedBy(procedureOrderServiceDto.getCreatedBy());
				mstr.setCreatedDate(TimeZoneComponent.getDateByzone(procedureOrderServiceDto.getUnitId()));
				mstr.setOrganizationId(procedureOrderServiceDto.getOrganizationId());
				mstr.setUnitId(procedureOrderServiceDto.getUnitId());
				mstr.setUpdatedBy(procedureOrderServiceDto.getUpdatedBy());
				mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureOrderServiceDto.getUnitId()));
			
				if(procedureOrderServiceDto.getProcedureOrderServiceId()!=null) {
					mstr.setProcedureOrderServiceId(procedureOrderServiceDto.getProcedureOrderServiceId());
					mstr.setStatus(procedureOrderServiceDto.getStatus());
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
	public Response cancelProcedureOrderServiceList(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_service set status = 'I' "
					+ " where procedure_request_id =:procedureRequestId  ")
					.setParameter("procedureRequestId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelProcedureOrderServiceList failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureOrderServiceList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}