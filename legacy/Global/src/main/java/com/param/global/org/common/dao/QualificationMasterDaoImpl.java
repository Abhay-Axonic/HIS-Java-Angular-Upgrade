package com.param.global.org.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.common.dto.QualificationMasterDto;
import com.param.global.org.common.model.QualificationMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QualificationMasterDaoImpl extends GenericDao<QualificationMaster, Integer>
		implements IQualificationMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(QualificationMasterDaoImpl.class);

	public QualificationMasterDaoImpl() {
		super(QualificationMaster.class);
	}

	@Override
	public Response saveQualificationMaster(QualificationMasterDto qualificationMasterDto) throws ApplicationException {
		try {
			QualificationMaster qualificationMaster = new QualificationMaster();
			qualificationMaster.setCreatedBy(qualificationMasterDto.getCreatedBy());
			qualificationMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(qualificationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			qualificationMaster.setOrganizationId(qualificationMasterDto.getOrganizationId());
			qualificationMaster.setQualificationCode(qualificationMasterDto.getQualificationCode());
			qualificationMaster.setQualificationDesc(qualificationMasterDto.getQualificationDesc());
			qualificationMaster.setStatus(qualificationMasterDto.getStatus());
			qualificationMaster.setUpdatedBy(qualificationMasterDto.getUpdatedBy());
			qualificationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(qualificationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(qualificationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveQualificationMaster failed", he);
		} catch (Exception e) {
		logger.error("saveQualificationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterById(int qualificationId, int orgId) throws ApplicationException {
		try {
			List<QualificationMasterDto> qualificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_QUALIFICATION_MASTER_BY_ID").setInteger("orgId", orgId)
					.setInteger("qualificationId", qualificationId)
					.setResultTransformer(Transformers.aliasToBean(QualificationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, qualificationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getQualificationMasterById failed", he);
		} catch (Exception e) {
		logger.error("getQualificationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterList(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			List<QualificationMasterDto> qualificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_QUALIFICATION_MASTER_LIST")
					.setInteger("orgId", qualificationMasterDto.getOrganizationId())
					.setFirstResult(qualificationMasterDto.getOffset() != null ? qualificationMasterDto.getOffset() : 0)
					.setMaxResults(qualificationMasterDto.getNoOfRecordsPerPage() != null ? qualificationMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(QualificationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, qualificationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getQualificationMasterList failed", he);
		} catch (Exception e) {
		logger.error("getQualificationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateQualificationMaster(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			QualificationMaster qualificationMaster = findById(qualificationMasterDto.getQualificationId());
			qualificationMaster.setQualificationCode(qualificationMasterDto.getQualificationCode());
			qualificationMaster.setQualificationDesc(qualificationMasterDto.getQualificationDesc());
			qualificationMaster.setUpdatedBy(qualificationMasterDto.getUpdatedBy());
			qualificationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(qualificationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(qualificationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateQualificationMaster failed", he);
		} catch (Exception e) {
		logger.error("updateQualificationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateQualificationMasterStatus(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			QualificationMaster qualificationMaster = findById(qualificationMasterDto.getQualificationId());
			qualificationMaster.setStatus(qualificationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			qualificationMaster.setUpdatedBy(qualificationMasterDto.getUpdatedBy());
			qualificationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(qualificationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(qualificationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateQualificationMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateQualificationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterCount(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(QualificationMaster.class);
			c.add(Restrictions.eq("organizationId", qualificationMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getQualificationMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getQualificationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterByName(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {

			List<QualificationMasterDto> qualificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_QUALIFICATION_MASTER_BY_NAME")
					.setInteger("orgId", qualificationMasterDto.getOrganizationId())
					.setString("qualificationDesc", qualificationMasterDto.getQualificationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(QualificationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, qualificationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getQualificationMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getQualificationMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterByNameNotById(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			List<QualificationMasterDto> qualificationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_QUALIFICATION_MASTER_BY_NAME_NOT_BY_ID")
					.setInteger("qualificationId", qualificationMasterDto.getQualificationId())
					.setInteger("orgId", qualificationMasterDto.getOrganizationId())
					.setString("qualificationDesc", qualificationMasterDto.getQualificationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(QualificationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, qualificationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getQualificationMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getQualificationMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationlist(QualificationMasterDto qualificationMasterDto) throws ApplicationException {
		try {
			List<QualificationMasterDto> qualificationMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("select qualification_id as\"qualificationId\", qualification_desc as \"qualificationDesc\" "
					+ " from public.m_qualification_master "
					+ " where Status='A' and organization_id =:orgId "
					+ " ORDER BY qualification_desc ASC ")
					.setInteger("orgId", qualificationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(QualificationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, qualificationMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getQualificationlist failed", he);
		} catch (Exception e) {
		logger.error("getQualificationlist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
