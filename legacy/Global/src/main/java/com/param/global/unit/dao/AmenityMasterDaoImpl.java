package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.AmenityMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.AmenityMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class AmenityMasterDaoImpl extends GenericDao<AmenityMaster, Integer>
		implements IAmenityMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmenityMasterDaoImpl.class);


	public AmenityMasterDaoImpl() {
		super(AmenityMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			AmenityMaster amenityMaster = new AmenityMaster();
			amenityMaster.setAmenityCode(amenityMasterDto.getAmenityCode());
			amenityMaster.setAmenityDesc(amenityMasterDto.getAmenityDesc());
			amenityMaster.setCreatedBy(amenityMasterDto.getCreatedBy());
			amenityMaster.setUpdatedBy(amenityMasterDto.getUpdatedBy());
			amenityMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(amenityMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			amenityMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(amenityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			amenityMaster.setStatus(amenityMasterDto.getStatus());
			amenityMaster.setOrganizationId(amenityMasterDto.getOrganizationId());
			amenityMaster.setUnitId(amenityMasterDto.getUnitId());
			save(amenityMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveAmenityMaster failed", he);
		} catch (Exception e) {
		logger.error("saveAmenityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			AmenityMaster amenityMaster = findById(amenityMasterDto.getAmenityId());
			amenityMaster.setAmenityCode(amenityMasterDto.getAmenityCode());
			amenityMaster.setAmenityDesc(amenityMasterDto.getAmenityDesc());
			amenityMaster.setUpdatedBy(amenityMasterDto.getUpdatedBy());
			amenityMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(amenityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(amenityMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateAmenityMaster failed", he);
		} catch (Exception e) {
		logger.error("updateAmenityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAmenityMasterStatus(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			AmenityMaster amenityMaster = findById(amenityMasterDto.getAmenityId());
			amenityMaster.setStatus(amenityMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			amenityMaster.setUpdatedBy(amenityMasterDto.getUpdatedBy());
			amenityMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(amenityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(amenityMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateAmenityMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateAmenityMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			List<AmenityMasterDto> amenityMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMENITY_MASTER_LIST")
					.setParameter("orgId", amenityMasterDto.getOrganizationId())
					.setParameter("unitId", amenityMasterDto.getUnitId())
					.setFirstResult(amenityMasterDto.getOffset() != null ? amenityMasterDto.getOffset() : 0)
					.setMaxResults(amenityMasterDto.getNoOfRecordsPerPage() != null ? amenityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmenityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, amenityMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getAmenityMasterList failed", he);
		} catch (Exception e) {
		logger.error("getAmenityMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			List<AmenityMasterDto> amenityMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMENITY_MASTER_LIST")
					.setParameter("orgId", amenityMasterDto.getOrganizationId())
					.setParameter("unitId", amenityMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AmenityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, amenityMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getActiveAmenityMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveAmenityMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListCount(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(AmenityMaster.class);
			c.add(Restrictions.eq("organizationId", amenityMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", amenityMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long l=(Long) c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, l);
		} catch (HibernateException he) {
		logger.error("getAmenityMasterListCount failed", he);
		} catch (Exception e) {
		logger.error("getAmenityMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListById(Integer amenityId) throws ApplicationException {
		try {
			List<AmenityMasterDto> amenityMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMENITY_MASTER_LIST_BY_ID")
					.setParameter("amenityId", amenityId)
					.setResultTransformer(Transformers.aliasToBean(AmenityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, amenityMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getAmenityMasterListById failed", he);
		} catch (Exception e) {
		logger.error("getAmenityMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListByName(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			List<AmenityMasterDto> amenityMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMENITY_MASTER_LIST_BY_NAME")
					.setParameter("orgId", amenityMasterDto.getOrganizationId())
					.setParameter("unitId", amenityMasterDto.getUnitId())
					.setParameter("name", amenityMasterDto.getAmenityDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AmenityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, amenityMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getAmenityMasterListByName failed", he);
		} catch (Exception e) {
		logger.error("getAmenityMasterListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListByNameNotById(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			List<AmenityMasterDto> amenityMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMENITY_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", amenityMasterDto.getOrganizationId())
					.setParameter("unitId", amenityMasterDto.getUnitId())
					.setParameter("name", amenityMasterDto.getAmenityDesc().toLowerCase())
					.setParameter("amenityId", amenityMasterDto.getAmenityId())
					.setResultTransformer(Transformers.aliasToBean(AmenityMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, amenityMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getAmenityMasterListByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getAmenityMasterListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
