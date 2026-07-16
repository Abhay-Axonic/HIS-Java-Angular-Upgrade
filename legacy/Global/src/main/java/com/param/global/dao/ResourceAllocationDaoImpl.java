package com.param.global.dao;

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

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ResourceAllocationDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.ResourceAllocation;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResourceAllocationDaoImpl extends GenericDao<ResourceAllocation, Integer>
		implements IResourceAllocationDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(ResourceAllocationDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public ResourceAllocationDaoImpl() {
		super(ResourceAllocation.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getResourceAllocationByName(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			List<ResourceAllocationDto> tarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_ALLOCATION_LIST_BY_NAME")
					.setParameter("vehicalNo", resourceAllocationDto.getVehicalNo().toLowerCase())
					.setParameter("organizationId", resourceAllocationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ResourceAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, tarrifMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getResourceAllocationByName failed", he);
		} catch (Exception e) {
			logger.error("getResourceAllocationByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			
			ResourceAllocation resourceAllocation=new ResourceAllocation();
			resourceAllocation.setAmbulanceType(resourceAllocationDto.getAmbulanceType());
			resourceAllocation.setAmbulanceProvider(resourceAllocationDto.getAmbulanceProvider());
			resourceAllocation.setVehicalNo(resourceAllocationDto.getVehicalNo());
			resourceAllocation.setResourceName(resourceAllocationDto.getResourceName());
			resourceAllocation.setMobileNo(resourceAllocationDto.getMobileNo());
			resourceAllocation.setStatus(resourceAllocationDto.getStatus());
			resourceAllocation.setOrganizationId(resourceAllocationDto.getOrganizationId());
			resourceAllocation.setCreatedBy(resourceAllocationDto.getCreatedBy());
			resourceAllocation.setCreatedDate(resourceAllocationDto.getCreatedDate().toString());
			resourceAllocation.setUpdatedBy(resourceAllocationDto.getUpdatedBy());
			resourceAllocation.setUpdatedDate(resourceAllocationDto.getUpdatedDate().toString());
			
		    sessionFactory.getCurrentSession().save(resourceAllocation);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveResourceAllocation failed", he);
		} catch (Exception e) {
			logger.error("saveResourceAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getResourceAllocationList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			List<ResourceAllocationDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_ALLOCATION_LIST")
					.setParameter("organizationId", resourceAllocationDto.getOrganizationId())
					.setFirstResult(resourceAllocationDto.getOffset() != null ? resourceAllocationDto.getOffset() : 0)
					.setMaxResults(resourceAllocationDto.getNoOfRecordsPerPage() != null ? resourceAllocationDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ResourceAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
		logger.error("getResourceAllocationList failed", he);
		} catch (Exception e) {
		logger.error("getResourceAllocationList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public Response getResourceAllocationListCount(ResourceAllocationDto resourceAllocationDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ResourceAllocation.class);
			crit.add(Restrictions.eq("organizationId", resourceAllocationDto.getOrganizationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getResourceAllocationListCount failed", he);
		} catch (Exception e) {
		logger.error("getResourceAllocationListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getResourceAllocationActiveList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			List<ResourceAllocationDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_RESOURCE_ALLOCATION_LIST")
					.setParameter("organizationId", resourceAllocationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ResourceAllocation.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
		logger.error("getResourceAllocationActiveList failed", he);
		} catch (Exception e) {
		logger.error("getResourceAllocationActiveList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	
	@Override
	public Response getResourceAllocationByID(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			List<ResourceAllocationDto> ambTarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_ALLOCATION_LIST_BY_ID")
					.setParameter("resourceAllocationMasterId", resourceAllocationDto.getResourceAllocationMasterId())
					.setParameter("organizationId", resourceAllocationDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ResourceAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, ambTarrifMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getResourceAllocationByID failed", he);
		} catch (Exception e) {
		logger.error("getResourceAllocationByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			List<ResourceAllocationDto> ambulanceResourceMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_ALLOCATION_SINGLE_REC")
					.setParameter("organizationId", resourceAllocationDto.getOrganizationId())
					.setParameter("resourceAllocationMasterId", resourceAllocationDto.getResourceAllocationMasterId())
					.setFirstResult(resourceAllocationDto.getOffset() != null ? resourceAllocationDto.getOffset() : 0)
					.setMaxResults(resourceAllocationDto.getNoOfRecordsPerPage() != null ? resourceAllocationDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ResourceAllocationDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceResourceMasterList, null);

		} catch (HibernateException he) {
		logger.error("getSingleResourceAllocation failed", he);
		} catch (Exception e) {
		logger.error("getSingleResourceAllocation failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			ResourceAllocation resourceAllocation = findById(resourceAllocationDto.getResourceAllocationMasterId());
			resourceAllocation.setAmbulanceType(resourceAllocationDto.getAmbulanceType());
			resourceAllocation.setAmbulanceProvider(resourceAllocationDto.getAmbulanceProvider());
			resourceAllocation.setVehicalNo(resourceAllocationDto.getVehicalNo());
			resourceAllocation.setResourceName(resourceAllocationDto.getResourceName());
			resourceAllocation.setMobileNo(resourceAllocationDto.getMobileNo());
			resourceAllocation.setOrganizationId(resourceAllocationDto.getOrganizationId());
			resourceAllocation.setUpdatedBy(resourceAllocationDto.getUpdatedBy());
			resourceAllocation.setUpdatedDate(resourceAllocationDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(resourceAllocation);
			return new Response<ResourceAllocationDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateResourceAllocation failed", he);
			return new Response<ResourceAllocationDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateResourceAllocation failed", e);
			return new Response<ResourceAllocationDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response changeResourceAllocationStatus(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {

			ResourceAllocation resourceAllocation = findById(resourceAllocationDto.getResourceAllocationMasterId());
			resourceAllocation.setUpdatedBy(resourceAllocationDto.getUpdatedBy());
			resourceAllocation.setStatus(resourceAllocationDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			resourceAllocation.setUpdatedDate(resourceAllocationDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(resourceAllocation);
			return new Response<ResourceAllocationDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("changeResourceAllocationStatus failed", he);
			return new Response<ResourceAllocationDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("changeResourceAllocationStatus failed", e);
			return new Response<ResourceAllocationDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}


}
