package com.param.global.otims.dao;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderPatientEducationDto;
import com.param.otims.model.ProcedureOrderPatientEducation;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class ProcedureOrderPatientEducationDaoImpl extends GenericDao<ProcedureOrderPatientEducation, Integer>implements IProcedureOrderPatientEducationDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOrderPatientEducationDaoImpl.class);

	
	public ProcedureOrderPatientEducationDaoImpl() {
		super(ProcedureOrderPatientEducation.class);
	}

	@Override
	public Response getPatientEducationDetailsByProcedureOrderId(ProcedureOrderConsentDto dto)
			throws ApplicationException {
		try{
			ProcedureOrderPatientEducationDto patientEducatDto = (ProcedureOrderPatientEducationDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ORDER_PATIENTEDU_BY_PROCEDURE_ORDER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("procedureOrderId", dto.getProcedureOrderId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderPatientEducationDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, patientEducatDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveProcedureOrderPatientEducation(
			ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto, int proReqId, Integer procedureOrderId)
			throws ApplicationException {
		try{
			
		/*	Criteria crit = sessionFactory.getCurrentSession().createCriteria(ProcedureOrderPatientEducation.class);
			crit.add(Restrictions.eq("procedureOrderId", procedureOrderId));
			crit.setProjection(Projections.rowCount());
			Long row = (Long) crit.uniqueResult();
			if(row>0) {
				Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_patient_education set status='I' "
						+ " where procedure_order_id='"+procedureOrderId+"' ")
						.executeUpdate();
				
			}*/
				ProcedureOrderPatientEducation mstr = new ProcedureOrderPatientEducation();
				mstr.setProcedureOrderId(procedureOrderId);
				mstr.setProcedureRequestId(proReqId);
				mstr.setPatientEducationTempale(procedureOrderPatientEducationDto.getPatientEducationTempale());
				mstr.setCreatedBy(procedureOrderPatientEducationDto.getCreatedBy());
				mstr.setCreatedDate(TimeZoneComponent.getDateByzone(procedureOrderPatientEducationDto.getUnitId()));
				mstr.setOrganizationId(procedureOrderPatientEducationDto.getOrganizationId());
				mstr.setUnitId(procedureOrderPatientEducationDto.getUnitId());
				mstr.setUpdatedBy(procedureOrderPatientEducationDto.getUpdatedBy());
				mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureOrderPatientEducationDto.getUnitId()));
				if(procedureOrderPatientEducationDto.getProcedureOrderPatientEducationId()!=null) {
					mstr.setProcedureOrderPatientEducationId(procedureOrderPatientEducationDto.getProcedureOrderPatientEducationId());
					mstr.setStatus(procedureOrderPatientEducationDto.getStatus());
					 sessionFactory.getCurrentSession().update(mstr);
				}else {
					mstr.setStatus('A');
					sessionFactory.getCurrentSession().save(mstr);
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mstr);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response cancelProcedureOrderPatientEducation(OTReservationDto oTReservationdto)
			throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_patient_education set status = 'I' "
					+ " where procedure_request_id =:procedureRequestId  ")
					.setParameter("procedureRequestId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("cancelProcedureOrderPatientEducation failed", he);
		} catch (Exception e) {
		logger.error("cancelProcedureOrderPatientEducation failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}