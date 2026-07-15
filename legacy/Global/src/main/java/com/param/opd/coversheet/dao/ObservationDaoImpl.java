package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.model.Observation;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ObservationDaoImpl extends GenericDao<Observation,  Integer> implements IObservationDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ObservationDaoImpl.class);


	public ObservationDaoImpl() {
		super(Observation.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveObservation(ObservationDto observationDto) throws ApplicationException {
		try {
			 Observation observation=new Observation();
			 observation.setObservationDesc(observationDto.getObservationDesc());
			 observation.setPatientId(observationDto.getPatientId());
			 if(observationDto.getEncounterId()!=null){
			 observation.setEncounterId(observationDto.getEncounterId());
			 }else{
				 observation.setAdmissionId(observationDto.getAdmissionId());
			 }
			 observation.setDoctorId(observationDto.getDoctorId());
			 observation.setOrganizationId(observationDto.getOrganizationId());
			 observation.setUnitId(observationDto.getUnitId());
			 observation.setStatus(observationDto.getStatus());
			 observation.setCreatedBy(observationDto.getCreatedBy());
			 observation.setCreatedDate(GlobalCommonDateUtils.getDate(observationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 observation.setUpdatedBy(observationDto.getUpdatedBy());
			 observation.setUpdatedDate(GlobalCommonDateUtils.getDate(observationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(observation);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveObservation failed", he);
		} catch (Exception e) {
		logger.error("saveObservation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateObservation(ObservationDto observationDto) throws ApplicationException {
		try {
			 Observation observation=findById(observationDto.getOrganizationId());
			 observation.setObservationDesc(observationDto.getObservationDesc());
			 observation.setPatientId(observationDto.getPatientId());
			 if(observationDto.getEncounterId()!=null){
				 observation.setEncounterId(observationDto.getEncounterId());
				 }else{
					 observation.setAdmissionId(observationDto.getAdmissionId());
				 }			 observation.setDoctorId(observationDto.getDoctorId());
			 observation.setOrganizationId(observationDto.getOrganizationId());
			 observation.setUnitId(observationDto.getUnitId());
			 observation.setUpdatedBy(observationDto.getUpdatedBy());
			 observation.setUpdatedDate(GlobalCommonDateUtils.getDate(observationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(observation);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateObservation failed", he);
		} catch (Exception e) {
		logger.error("updateObservation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateObservationStatus(ObservationDto observationDto) throws ApplicationException {
		try {
			 Observation observation=findById(observationDto.getOrganizationId());
			 observation.setStatus(observationDto.getStatus());
			 observation.setUpdatedBy(observationDto.getUpdatedBy());
			 observation.setUpdatedDate(GlobalCommonDateUtils.getDate(observationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(observation);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateObservationStatus failed", he);
		} catch (Exception e) {
		logger.error("updateObservationStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getObservationList(ObservationDto observationDto) throws ApplicationException {
		try {
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OBSERVATION_LIST").getQueryString());
			if(observationDto.getAdmissionId()!=null && observationDto.getAdmissionId()!=0){
				stringQuery.append(" AND observation.admissionId = :admissionId");
			}else if(observationDto.getEncounterId()!=null && observationDto.getEncounterId()!=0){
				stringQuery.append(" AND observation.encounterId = :encounterId");
			}else {
				stringQuery.append(" AND to_char(observation.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate");
				stringQuery.append(" AND observation.patientId = :patientId");
			}
			Query queryObject=sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
					.setParameter("organizationId",  observationDto.getOrganizationId())
					.setParameter("unitId",  observationDto.getUnitId());

			if (observationDto.getAdmissionId() != null && observationDto.getAdmissionId() != 0) {
				queryObject.setParameter("admissionId", observationDto.getAdmissionId());
			} else if (observationDto.getEncounterId() != null && observationDto.getEncounterId() != 0) {
				queryObject.setParameter("encounterId", observationDto.getEncounterId());
			} else {
				queryObject.setParameter("fromDate", observationDto.getFromDate());
				queryObject.setParameter("toDate", observationDto.getToDate());
				queryObject.setParameter("patientId", observationDto.getPatientId());
			}

			List<ObservationDto> list=	queryObject.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getObservationList failed", he);
		} catch (Exception e) {
		logger.error("getObservationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getObservationById(Integer observationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<ObservationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OBSERVATION_BY_ID")
					.setParameter("observationId",  observationId)
					.setParameter("organizationId",  organizationId)
					.setParameter("unitId",  unitId)
					.setResultTransformer(Transformers.aliasToBean(ObservationDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getObservationById failed", he);
		} catch (Exception e) {
		logger.error("getObservationById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getObservationCount(ObservationDto observationDto) throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Observation.class);
			crit.add(Restrictions.eq("organizationId", observationDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", observationDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getObservationCount failed", he);
		} catch (Exception e) {
		logger.error("getObservationCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savefPatientObservation(List<ObservationDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
		try{
			
			for (ObservationDto dto : list){
				if(dto.getCovTemplateId() == null){
				 Observation observation=new Observation();
				 observation.setObservationDesc(dto.getObservationDesc());
//				 observation.setPatientId(resDto.getPatientId());
//				 if(dto.getEncounterId()!=null){
//				 observation.setEncounterId(resDto.getEncounterId());
//				 }else{
//					 observation.setAdmissionId(resDto.getAdmissionId());
//				 }
				 observation.setDoctorId(resDto.getDoctorId());
				 observation.setOrganizationId(resDto.getOrganizationId());
				 observation.setUnitId(resDto.getUnitId());
				 observation.setStatus(resDto.getStatus());
				 observation.setCreatedBy(resDto.getCreatedBy());
				 observation.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
				 observation.setUpdatedBy(resDto.getUpdatedBy());
				 observation.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
				 observation.setCovTemplateId(dto.getCovTemplateId());
				 save(observation);
			
				}/*else {
					 Observation observation=findById(dto.getCovTemplateId());
					 observation.setObservationDesc(dto.getObservationDesc());
//					 observation.setPatientId(resDto.getPatientId());
//					 if(dto.getEncounterId()!=null){
//					 observation.setEncounterId(resDto.getEncounterId());
//					 }else{
//						 observation.setAdmissionId(resDto.getAdmissionId());
//					 }
					 observation.setDoctorId(resDto.getDoctorId());
					 observation.setOrganizationId(resDto.getOrganizationId());
					 observation.setUnitId(resDto.getUnitId());
					 observation.setStatus(resDto.getStatus());
					 observation.setCreatedBy(resDto.getCreatedBy());
					 observation.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					 observation.setUpdatedBy(resDto.getUpdatedBy());
					 observation.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					 update(observation);
				
					}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempPatientObservation(List<ObservationDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
try{
			
			for (ObservationDto dto : list){
				if(dto.getCovTemplateId() != null){
				 
					 Observation observation=findById(dto.getCovTemplateId());
					 observation.setObservationDesc(dto.getObservationDesc());
//					 observation.setPatientId(resDto.getPatientId());
//					 if(dto.getEncounterId()!=null){
//					 observation.setEncounterId(resDto.getEncounterId());
//					 }else{
//						 observation.setAdmissionId(resDto.getAdmissionId());
//					 }
					 observation.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
					 observation.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					 observation.setUnitId(coversheetTemplateMasterDto.getUnitId());
					 observation.setStatus(coversheetTemplateMasterDto.getStatus());
					 observation.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					 observation.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					 observation.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					 observation.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					 update(observation);
				
					}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}
	
	

}
