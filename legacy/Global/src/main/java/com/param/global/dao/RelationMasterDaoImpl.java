package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
import com.param.global.common.ServiceException;
import com.param.global.dto.RelationMasterDto;
import com.param.global.model.RelationMaster;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class RelationMasterDaoImpl extends GenericDao<RelationMaster, Integer>
		implements IRelationMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(RelationMasterDaoImpl.class);

	public RelationMasterDaoImpl() {
		super(RelationMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response getCountryByName(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RELATION_LIST_BY_NAME")
					.setParameter("desc", relationMasterDto.getDesc().toLowerCase())
					.setParameter("orgId", relationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCountryByName failed", he);
		} catch (Exception e) {
			logger.error("getCountryByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addCountryMaster(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			RelationMaster relationMaster = mapper.map(relationMasterDto, RelationMaster.class,
					"RelationMasterDto_to_RelationMaster");
			// create binds the JWT-verified org, never a client-supplied value
			relationMaster.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			relationMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			relationMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			relationMaster.setStatus('A');
			relationMaster = save(relationMaster);
			return new Response(SUCCESS, null, null, null, relationMaster);
		} catch (HibernateException he) {
			logger.error("addCountryMaster failed", he);
		} catch (Exception e) {
			logger.error("addCountryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRelationMasterList(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RELATION_LIST")
					.setParameter("orgId", relationMasterDto.getOrganizationId())
					.setFirstResult(relationMasterDto.getOffset() != null ? relationMasterDto.getOffset() : 0)
					.setMaxResults(relationMasterDto.getNoOfRecordsPerPage() != null ? relationMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getRelationMasterList failed", he);
		} catch (Exception e) {
			logger.error("getRelationMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRelationById(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RELATION_LIST_BY_ID")
					.setParameter("id", relationMasterDto.getId())
					.setParameter("orgId", verifiedIdentityService.getVerifiedOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getRelationById failed", he);
		} catch (Exception e) {
			logger.error("getRelationById failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveRelationList(Integer orgId) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RELATION_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveRelationList failed", he);
		} catch (Exception e) {
			logger.error("getActiveRelationList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRelationByNameNotId(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RELATION_LIST_BY_NAME_NOT_REL_ID")
					.setParameter("desc", relationMasterDto.getDesc())
					.setParameter("id", relationMasterDto.getId())
					.setParameter("orgId", relationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getRelationByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getRelationByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRelationMaster(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {

			RelationMaster relationMaster = findById(relationMasterDto.getId());
			// ownership guard: reject before any write when the target row is not
			// owned by the JWT-verified tenant
			if (relationMaster == null || relationMaster.getOrganizationId() == null
					|| !relationMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<RelationMasterDto, Integer>(ERROR, ERROR_CODE, "RELATION_OWNERSHIP_DENIED", null, null);
			}
			relationMaster.setCode(relationMasterDto.getCode());
			relationMaster.setDesc(relationMasterDto.getDesc());
			relationMaster.setUpdatedBy(relationMasterDto.getUpdatedBy());
			relationMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(relationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(relationMaster);
			return new Response<RelationMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, relationMaster);

		} catch (HibernateException he) {
			logger.error("updateRelationMaster failed", he);
			return new Response<RelationMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateRelationMaster failed", e);
			return new Response<RelationMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response updateRelationStatus(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {

			RelationMaster relationMaster = findById(relationMasterDto.getId());
			// ownership guard: reject before any write when the target row is not
			// owned by the JWT-verified tenant
			if (relationMaster == null || relationMaster.getOrganizationId() == null
					|| !relationMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<RelationMasterDto, Integer>(ERROR, ERROR_CODE, "RELATION_OWNERSHIP_DENIED", null, null);
			}
			relationMaster.setUpdatedBy(relationMasterDto.getUpdatedBy());
			relationMaster.setStatus(relationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			relationMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(relationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(relationMaster);
			return new Response<RelationMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, relationMaster.getOrganizationId());

		} catch (HibernateException he) {
			logger.error("updateRelationStatus failed", he);
			return new Response<RelationMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateRelationStatus failed", e);
			return new Response<RelationMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getCount(RelationMasterDto relationMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(RelationMaster.class); 
			c.add(Restrictions.eq("organizationId", relationMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getActiveRelationListForWebPage(RelationMasterDto relationMasterDto) throws ApplicationException {
		try {
			List<RelationMasterDto> relationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RELATION_LIST")
					.setParameter("orgId", relationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(RelationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, relationMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveRelationListForWebPage failed", he);
		} catch (Exception e) {
			logger.error("getActiveRelationListForWebPage failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

}
