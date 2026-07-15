package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;
import com.param.opd.coversheet.model.DoctorFavouritesDiagnosis;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DoctorFavouritesDiagnosisDaoImpl extends GenericDao<DoctorFavouritesDiagnosis, Integer> implements IDoctorFavouritesDiagnosisDao, ICommonConstants,IError{
	final static Logger logger = Logger.getLogger(DoctorFavouritesDiagnosisDaoImpl.class);


	public DoctorFavouritesDiagnosisDaoImpl() {
		super(DoctorFavouritesDiagnosis.class);
	}

	@Override
	public Response saveDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		try {
			DoctorFavouritesDiagnosis doctorFavouritesDiagnosis=new DoctorFavouritesDiagnosis();
			doctorFavouritesDiagnosis.setDoctorId(doctorFavouritesDiagnosisDto.getDoctorId());
			doctorFavouritesDiagnosis.setDiagnosisId(doctorFavouritesDiagnosisDto.getDiagnosisId());
			doctorFavouritesDiagnosis.setOrganizationId(doctorFavouritesDiagnosisDto.getOrganizationId());
			doctorFavouritesDiagnosis.setUnitId(doctorFavouritesDiagnosisDto.getUnitId());
			doctorFavouritesDiagnosis.setStatus(doctorFavouritesDiagnosisDto.getStatus());
			doctorFavouritesDiagnosis.setCreatedBy(doctorFavouritesDiagnosisDto.getCreatedBy());
			doctorFavouritesDiagnosis.setCreatedDate(GlobalCommonDateUtils.getDate(doctorFavouritesDiagnosisDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorFavouritesDiagnosis.setUpdatedBy(doctorFavouritesDiagnosisDto.getUpdatedBy());
			doctorFavouritesDiagnosis.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorFavouritesDiagnosisDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(doctorFavouritesDiagnosis);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveDoctorFavouriteDiagnosis failed", he);
		} catch (Exception e) {
		logger.error("saveDoctorFavouriteDiagnosis failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatusForDoctorFavouriteDiagnosis(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		try {
			if(doctorFavouritesDiagnosisDto.getDocFavDiagId()!= null) {
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavouritesDiagnosis docFavDig set "
					+ "docFavDig.status='I', "
					+ "docFavDig.updatedBy=:updatedBy, "
					+ "docFavDig.updatedDate=:updatedDate "
					+ "WHERE docFavDig.doctorId=:doctorId "
					+ "AND docFavDig.docFavDiagId=:docFavDiagId "
					+ "AND docFavDig.organizationId=:orgId "
					+ "AND docFavDig.unitId=:unitId ")
					.setParameter("doctorId", doctorFavouritesDiagnosisDto.getDoctorId())
					.setParameter("docFavDiagId", doctorFavouritesDiagnosisDto.getDocFavDiagId())
					.setParameter("orgId", doctorFavouritesDiagnosisDto.getOrganizationId())
					.setParameter("unitId", doctorFavouritesDiagnosisDto.getUnitId())
					.setParameter("updatedBy", doctorFavouritesDiagnosisDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorFavouritesDiagnosisDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss"))
					.executeUpdate();
			}else {
				sessionFactory.getCurrentSession().createQuery("UPDATE DoctorFavouritesDiagnosis docFavDig set "
						+ "docFavDig.status='I', "
						+ "docFavDig.updatedBy=:updatedBy, "
						+ "docFavDig.updatedDate=:updatedDate "
						+ "WHERE docFavDig.doctorId=:doctorId "
						+ "AND docFavDig.diagnosisId=:diagnosisId "
						+ "AND docFavDig.organizationId=:orgId "
						+ "AND docFavDig.unitId=:unitId ")
				        .setParameter("doctorId", doctorFavouritesDiagnosisDto.getDoctorId())
						.setParameter("diagnosisId", doctorFavouritesDiagnosisDto.getDiagnosisId())
						.setParameter("orgId", doctorFavouritesDiagnosisDto.getOrganizationId())
						.setParameter("unitId", doctorFavouritesDiagnosisDto.getUnitId())
						.setParameter("updatedBy", doctorFavouritesDiagnosisDto.getUpdatedBy())
						.setParameter("updatedDate", GlobalCommonDateUtils.getDate(doctorFavouritesDiagnosisDto.getUpdatedDate(),"yyyy-M-dd HH:mm:ss"))
						.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, "Removed diagnosis from Favorites !!", null, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("updateStatusForDoctorFavouriteDiagnosis failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response isDoctorFavoritesDiagnosisExist(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		
		try {
			
			List<DoctorFavouritesDiagnosisDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_FAVOURITES_DIAGNOSIS")
					.setParameter("doctorId", doctorFavouritesDiagnosisDto.getDoctorId())
					.setParameter("diagnosisId", doctorFavouritesDiagnosisDto.getDiagnosisId())
					.setParameter("unitId", doctorFavouritesDiagnosisDto.getUnitId())
					.setParameter("orgId", doctorFavouritesDiagnosisDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DoctorFavouritesDiagnosisDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("isDoctorFavoritesDiagnosisExist failed", he);
		} catch (Exception e) {
		logger.error("isDoctorFavoritesDiagnosisExist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorFavouriteDiagnosisListByDocId(DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto)
			throws ApplicationException {
		try {
			
			List<DoctorFavouritesDiagnosisDto> list;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_FAVOURITE_DIAGNOSIS_LIST_BY_DOC_ID").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			if(doctorFavouritesDiagnosisDto.getDoctorId()!=null){
				subQuery.append(" AND docFavDig.doctorId = :doctorId");
			}
				query = query.concat(subQuery.toString());

			Query query1 =sessionFactory.getCurrentSession().createQuery(query)
//					.setParameter("doctorId", doctorFavouritesDiagnosisDto.getDoctorId())
					.setParameter("unitId", doctorFavouritesDiagnosisDto.getUnitId())
					.setParameter("orgId", doctorFavouritesDiagnosisDto.getOrganizationId());

			if (doctorFavouritesDiagnosisDto.getDoctorId() != null) {
				query1.setParameter("doctorId", doctorFavouritesDiagnosisDto.getDoctorId());
			}

			 list = query1.setResultTransformer(Transformers.aliasToBean(DoctorFavouritesDiagnosisDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getDoctorFavouriteDiagnosisListByDocId failed", he);
		} catch (Exception e) {
		logger.error("getDoctorFavouriteDiagnosisListByDocId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
