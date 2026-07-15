package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.doctor.model.DoctorGrade;
import com.param.global.billing.dto.DoctorGradeDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorGradeDaoImpl extends GenericDao<DoctorGrade, Integer> implements IDoctorGradeDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(DoctorGradeDaoImpl.class);

	 public DoctorGradeDaoImpl() {
		super(DoctorGrade.class);
		
	}
	 
	 @Autowired
	 SessionFactory sessionFactory;
	
	@Override
	public Response saveDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			DoctorGrade doctorGrade=new DoctorGrade();
			doctorGrade.setDocGradeCode(doctorGradeDto.getDocGradeCode());
			doctorGrade.setDocGradeName(doctorGradeDto.getDocGradeName());
			doctorGrade.setOrgId(doctorGradeDto.getOrgId());
			doctorGrade.setOrgUnitId(doctorGradeDto.getOrgUnitId());
			doctorGrade.setCreatedBy(doctorGradeDto.getCreatedBy());
			doctorGrade.setCreatedDate(GlobalCommonDateUtils.getDate(doctorGradeDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorGrade.setUpdatedBy(doctorGradeDto.getUpdatedBy());
			doctorGrade.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorGradeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorGrade.setStatus(doctorGradeDto.getStatus());
			
			save(doctorGrade);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error("saveDoctorGrade failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDoctorGrade failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			DoctorGrade doctorGrade=findById(doctorGradeDto.getDoctorGradeId());
			doctorGrade.setDocGradeCode(doctorGradeDto.getDocGradeCode());
			doctorGrade.setDocGradeName(doctorGradeDto.getDocGradeName());
			doctorGrade.setOrgId(doctorGradeDto.getOrgId());
			doctorGrade.setOrgUnitId(doctorGradeDto.getOrgUnitId());
			doctorGrade.setUpdatedBy(doctorGradeDto.getUpdatedBy());
			doctorGrade.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorGradeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(doctorGrade);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateDoctorGrade failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDoctorGrade failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorGradeStatus(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			DoctorGrade doctorGrade=findById(doctorGradeDto.getDoctorGradeId());
			doctorGrade.setUpdatedBy(doctorGradeDto.getUpdatedBy());
			doctorGrade.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorGradeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorGrade.setStatus(doctorGradeDto.getStatus());
			update(doctorGrade);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateDoctorGradeStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDoctorGradeStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
		List<DoctorGradeDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DOCTOR_GRADE_LIST")
				.setParameter("orgId", doctorGradeDto.getOrgId())
				.setParameter("orgUnitId", doctorGradeDto.getOrgUnitId())
				.setFirstResult(doctorGradeDto.getOffset() != null ? doctorGradeDto.getOffset() : 0)
				.setMaxResults(doctorGradeDto.getNoOfRecordsPerPage() != null ? doctorGradeDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(DoctorGradeDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getDoctorGradeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorGradeList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDoctorGradeById(Integer doctorGradeId, Integer orgId, Integer orgUnitId) throws ApplicationException {
		try{
			List<DoctorGradeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_GRADE_BY_ID")
					.setParameter("doctorGradeId", doctorGradeId)
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorGradeDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getDoctorGradeById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorGradeById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDoctorGradeCount(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(DoctorGrade.class);
			c.add(Restrictions.eq("orgId", doctorGradeDto.getOrgId()));
			c.add(Restrictions.eq("orgUnitId", doctorGradeDto.getOrgUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("getDoctorGradeCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorGradeCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			List<DoctorGradeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_GRADE_LIST")
					.setParameter("orgId", doctorGradeDto.getOrgId())
					.setParameter("orgUnitId", doctorGradeDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorGradeDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getActiveDoctorGradeList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveDoctorGradeList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorGradeByName(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			List<DoctorGradeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_GRADE_BY_NAME")
					.setParameter("docGradeName", doctorGradeDto.getDocGradeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoctorGradeDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getDoctorGradeByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorGradeByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorGradeByNameNotId(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try{
			List<DoctorGradeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_GRADE_BY_NAME_NOT_ID")
					.setParameter("doctorGradeId", doctorGradeDto.getDoctorGradeId())
					.setParameter("docGradeName", doctorGradeDto.getDocGradeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoctorGradeDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getDoctorGradeByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorGradeByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
