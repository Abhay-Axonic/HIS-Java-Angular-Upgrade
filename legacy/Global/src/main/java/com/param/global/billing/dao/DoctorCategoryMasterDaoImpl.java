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

import com.param.global.billing.dto.DoctorCategoryMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.DoctorCategoryMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorCategoryMasterDaoImpl extends GenericDao<DoctorCategoryMaster, Integer>
		implements IDoctorCategoryMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(DoctorCategoryMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public DoctorCategoryMasterDaoImpl() {
		super(DoctorCategoryMaster.class);
	}

	@Override
	public Response saveDoctorCategoryMaster(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {
			DoctorCategoryMaster doctorCategoryMaster = new DoctorCategoryMaster();
			doctorCategoryMaster.setCreatedBy(doctorCategoryMasterDto.getCreatedBy());
			doctorCategoryMaster.setUpdatedBy(doctorCategoryMasterDto.getUpdatedBy());
			doctorCategoryMaster.setDoctorCategoryCode(doctorCategoryMasterDto.getDoctorCategoryCode());
			doctorCategoryMaster.setDoctorCategoryDescription(doctorCategoryMasterDto.getDoctorCategoryDescription());
			doctorCategoryMaster.setStatus(doctorCategoryMasterDto.getStatus());
			doctorCategoryMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(doctorCategoryMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(doctorCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorCategoryMaster.setOrganizationId(doctorCategoryMasterDto.getOrganizationId());
			doctorCategoryMaster=save(doctorCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, doctorCategoryMaster);
		} catch (HibernateException he) {
			logger.error("saveDoctorCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryMasterById(int doctorId, int orgId) throws ApplicationException {
		try {
			List<DoctorCategoryMasterDto> doctorCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CATEGORY_MASTER_BY_ID").setParameter("doctorId", doctorId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, doctorCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getDoctorCategoryMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryMasterList(DoctorCategoryMasterDto doctorCategoryMasterDto) throws ApplicationException {
		try {
			List<DoctorCategoryMasterDto> doctorCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CATEGORY_MASTER_LIST")
					.setParameter("orgId", doctorCategoryMasterDto.getOrganizationId())
					.setFirstResult(doctorCategoryMasterDto.getOffset() != null ? doctorCategoryMasterDto.getOffset() : 0)//uncomment by minesh
					.setMaxResults(doctorCategoryMasterDto.getNoOfRecordsPerPage() != null ? doctorCategoryMasterDto.getNoOfRecordsPerPage() : 10)//uncomment by minesh
					.setResultTransformer(Transformers.aliasToBean(DoctorCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorCategoryMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoctorCategoryMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorCategoryMaster(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {

			DoctorCategoryMaster doctorCategoryMaster = findById(doctorCategoryMasterDto.getDoctorCategoryId());
			doctorCategoryMaster.setUpdatedBy(doctorCategoryMasterDto.getUpdatedBy());
			doctorCategoryMaster.setDoctorCategoryCode(doctorCategoryMasterDto.getDoctorCategoryCode());
			doctorCategoryMaster.setDoctorCategoryDescription(doctorCategoryMasterDto.getDoctorCategoryDescription());
			doctorCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(doctorCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorCategoryMaster =update(doctorCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, doctorCategoryMaster);
		} catch (HibernateException he) {
			logger.error("updateDoctorCategoryMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorCategoryMasterStatus(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {
			DoctorCategoryMaster doctorCategoryMaster = findById(doctorCategoryMasterDto.getDoctorCategoryId());
			doctorCategoryMaster.setStatus(doctorCategoryMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			doctorCategoryMaster.setUpdatedBy(doctorCategoryMasterDto.getUpdatedBy());
			doctorCategoryMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(doctorCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(doctorCategoryMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateDoctorCategoryMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDoctorCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryMasterByNameNotId(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {
			List<DoctorCategoryMasterDto> doctorCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CATEGORY_MASTER_BY_NAME_NOT_ID")
					.setParameter("doctorId", doctorCategoryMasterDto.getDoctorCategoryId())
					.setParameter("orgId", doctorCategoryMasterDto.getOrganizationId())
					.setParameter("doctorDesc", doctorCategoryMasterDto.getDoctorCategoryDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoctorCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorCategoryMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoctorCategoryMasterByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryMasterByName(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {

			List<DoctorCategoryMasterDto> doctorCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CATEGORY_MASTER_BY_NAME")
					.setParameter("orgId", doctorCategoryMasterDto.getOrganizationId())
					.setParameter("doctorDesc", doctorCategoryMasterDto.getDoctorCategoryDescription().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoctorCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorCategoryMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoctorCategoryMasterByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryCount(DoctorCategoryMasterDto doctorCategoryMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(DoctorCategoryMaster.class); 
			c.add(Restrictions.eq("organizationId", doctorCategoryMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getDoctorCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDoctorCategoryMasterList(int orgId) throws ApplicationException {
		try {
			List<DoctorCategoryMasterDto> doctorCategoryMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_CATEGORY_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCategoryMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, doctorCategoryMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveDoctorCategoryMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getActiveDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
