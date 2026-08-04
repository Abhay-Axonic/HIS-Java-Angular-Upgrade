package com.param.global.dao;

import java.util.List;

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
import com.param.global.dto.VisitTypeMasterDto;
import com.param.global.model.VisitTypeMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class VisitTypeMasterDaoImpl extends GenericDao<VisitTypeMaster, Integer>
		implements IVisitTypeMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(VisitTypeMasterDaoImpl.class);


	public VisitTypeMasterDaoImpl() {
		super(VisitTypeMaster.class);
	}

	@Override
	public Response getActiveVisitTypeList(Integer unitId, Integer orgId) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> listVisitTypeMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_VISIT_TYPE_LIST")
					// BR-161-class fix (2026-08-04, live-proven): the query previously had
					// NO org filter AND bound no params -- every org's rows were returned
					// (forged org-999 saw org-1 rows + victims). Now org-scoped.
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listVisitTypeMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getActiveVisitTypeList failed", he);
		} catch (Exception e) {
		logger.error("getActiveVisitTypeList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVisitTypeList(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> listVisitTypeMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_LIST")
					// BR-161-class fix: org-scoped (the query had no org filter).
					.setParameter("orgId", visitTypeMasterDto.getOrganizationId())
					.setFirstResult(visitTypeMasterDto.getOffset() != null ? visitTypeMasterDto.getOffset() : 0)
					.setMaxResults(visitTypeMasterDto.getNoOfRecordsPerPage() != null ? visitTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listVisitTypeMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getVisitTypeList failed", he);
		} catch (Exception e) {
		logger.error("getVisitTypeList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVisitTypeCount(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(VisitTypeMaster.class); 
			c.add(Restrictions.eq("organizationId", visitTypeMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getVisitTypeCount failed", he);
		} catch (Exception e) {
		logger.error("getVisitTypeCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitTypeStatus(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			VisitTypeMaster visitTypeMaster = findById(visitTypeMasterDto.getVisitTypeId());
			visitTypeMaster.setUpdatedBy(visitTypeMasterDto.getUpdatedBy());
			visitTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(visitTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			visitTypeMaster.setStatus(visitTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(visitTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateVisitTypeStatus failed", he);
		} catch (Exception e) {
		logger.error("updateVisitTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			VisitTypeMaster visitTypeMaster = findById(visitTypeMasterDto.getVisitTypeId());
			visitTypeMaster.setUpdatedBy(visitTypeMasterDto.getUpdatedBy());
			visitTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(visitTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			visitTypeMaster.setVisitTypeCode(visitTypeMasterDto.getVisitTypeCode());
			visitTypeMaster.setVisitTypeName(visitTypeMasterDto.getVisitTypeName());
			update(visitTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateVisitType failed", he);
		} catch (Exception e) {
		logger.error("updateVisitType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitTypeById(Integer visitId,Integer orgId) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> listVisitTypeMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_BY_ID")
					.setParameter("visitId", visitId)
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listVisitTypeMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getVisitTypeById failed", he);
		} catch (Exception e) {
		logger.error("getVisitTypeById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {

		try {
			VisitTypeMaster visitTypeMaster = new VisitTypeMaster();
			visitTypeMaster.setCreatedBy(visitTypeMasterDto.getCreatedBy());
			visitTypeMaster.setUpdatedBy(visitTypeMasterDto.getUpdatedBy());
			visitTypeMaster.setOrganizationId(visitTypeMasterDto.getOrganizationId());
			visitTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(visitTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			visitTypeMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(visitTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			visitTypeMaster.setStatus('A');
			visitTypeMaster.setVisitTypeCode(visitTypeMasterDto.getVisitTypeCode());
			visitTypeMaster.setVisitTypeName(visitTypeMasterDto.getVisitTypeName());
			save(visitTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, visitTypeMaster);
		} catch (HibernateException he) {
		logger.error("saveVisitType failed", he);
		} catch (Exception e) {
		logger.error("saveVisitType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getVisitTypeByName(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> listVisitTypeMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_BY_NAME")
					.setParameter("name", visitTypeMasterDto.getVisitTypeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listVisitTypeMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getVisitTypeByName failed", he);
		} catch (Exception e) {
		logger.error("getVisitTypeByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVisitTypeByNameNotById(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> listVisitTypeMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VISIT_TYPE_BY_NAME_NOT_BY_ID")
					.setParameter("visitId", visitTypeMasterDto.getVisitTypeId())
					.setParameter("name",visitTypeMasterDto.getVisitTypeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(VisitTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listVisitTypeMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getVisitTypeByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getVisitTypeByNameNotById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
