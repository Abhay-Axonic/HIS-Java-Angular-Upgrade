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

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.IncidentCategoryMaster;
import com.param.global.unit.dto.IncidentCategoryMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public class IncidentCategoryMasterDaoImpl extends  GenericDao<IncidentCategoryMaster, Integer> implements IIncidentCategoryMasterDaoImpl ,
ICommonConstants {
	final static Logger logger = Logger.getLogger(IncidentCategoryMasterDaoImpl.class);

	
	public IncidentCategoryMasterDaoImpl() {
		super(IncidentCategoryMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			IncidentCategoryMaster incidentCategoryMaster=new IncidentCategoryMaster();
			incidentCategoryMaster.setIncidentCategoryCode(incidentCategoryMasterDto.getIncidentCategoryCode());
			incidentCategoryMaster.setIncidentCategoryDesc(incidentCategoryMasterDto.getIncidentCategoryDesc());
			incidentCategoryMaster.setOrganizationId(incidentCategoryMasterDto.getOrganizationId());
			incidentCategoryMaster.setUnitId(incidentCategoryMasterDto.getUnitId());
			incidentCategoryMaster.setStatus(incidentCategoryMasterDto.getStatus());
			incidentCategoryMaster.setCreatedBy(incidentCategoryMasterDto.getCreatedBy());
			incidentCategoryMaster.setCreatedDate(GlobalCommonDateUtils.getDate(incidentCategoryMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			incidentCategoryMaster.setUpdatedBy(incidentCategoryMasterDto.getUpdatedBy());
			incidentCategoryMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(incidentCategoryMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveIncidentCategoryMaster failed", he);
		} catch (Exception e) {
		logger.error("saveIncidentCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getIncidentCategoryByName(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			
			List<IncidentCategoryMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_CATEGORY_MASTER_BY_NAME")
					.setParameter("incidentCategoryDesc", incidentCategoryMasterDto.getIncidentCategoryDesc().toLowerCase())
					.setParameter("unitId", incidentCategoryMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(IncidentCategoryMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIncidentCategoryByName failed", he);
		} catch (Exception e) {
		logger.error("getIncidentCategoryByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getIncidentCategoryMasterByNameNotId(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			List<IncidentCategoryMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_CATEGORY_BY_NAME_NOT_ID")
					.setParameter("incidentCategoryId", incidentCategoryMasterDto.getIncidentCategoryId())
					.setParameter("unitId", incidentCategoryMasterDto.getUnitId())
					.setParameter("incidentCategoryDesc", incidentCategoryMasterDto.getIncidentCategoryDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(IncidentCategoryMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIncidentCategoryMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto) throws ApplicationException {
		try {
			IncidentCategoryMaster incidentCategoryMaster=findById(incidentCategoryMasterDto.getIncidentCategoryId());
			incidentCategoryMaster.setIncidentCategoryCode(incidentCategoryMasterDto.getIncidentCategoryCode());
			incidentCategoryMaster.setIncidentCategoryDesc(incidentCategoryMasterDto.getIncidentCategoryDesc());
			incidentCategoryMaster.setOrganizationId(incidentCategoryMasterDto.getOrganizationId());
			incidentCategoryMaster.setUnitId(incidentCategoryMasterDto.getUnitId());
			incidentCategoryMaster.setUpdatedBy(incidentCategoryMasterDto.getUpdatedBy());
			incidentCategoryMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		update(incidentCategoryMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateIncidentCategoryMaster failed", he);
	} catch (Exception e) {
	logger.error("updateIncidentCategoryMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}



	@Override
	public Response updateIncidentCategoryMasterStatus(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			IncidentCategoryMaster incidentCategoryMaster=findById(incidentCategoryMasterDto.getIncidentCategoryId());
			incidentCategoryMaster.setStatus(incidentCategoryMasterDto.getStatus());
			incidentCategoryMaster.setUpdatedBy(incidentCategoryMasterDto.getUpdatedBy());
			incidentCategoryMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentCategoryMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(incidentCategoryMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateIncidentCategoryMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateIncidentCategoryMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}



	@Override
	public Response getIncidentCategoryMasterById(Integer incidentCategoryId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<IncidentCategoryMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_CATEGORY_MASTER_BY_ID")
					.setParameter("incidentCategoryId", incidentCategoryId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(IncidentCategoryMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIncidentCategoryMasterById failed", he);
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getIncidentCategoryMasterList(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			List<IncidentCategoryMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_CATEGORY_MASTER_LIST")
					.setParameter("organizationId",  incidentCategoryMasterDto.getOrganizationId())
					.setParameter("unitId",  incidentCategoryMasterDto.getUnitId())
					.setFirstResult(incidentCategoryMasterDto.getOffset() != null ? incidentCategoryMasterDto.getOffset()  : 0)
					.setMaxResults(incidentCategoryMasterDto.getNoOfRecordsPerPage() != null ? incidentCategoryMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(IncidentCategoryMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIncidentCategoryMasterList failed", he);
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getIncidentCategoryMasterCount(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(IncidentCategoryMaster.class);
			crit.add(Restrictions.eq("organizationId", incidentCategoryMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", incidentCategoryMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getIncidentCategoryMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getIncidentCategoryMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getActiveIncidentCategoryMaster(Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<IncidentCategoryMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_INCIDENT_CATEGORY_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(IncidentCategoryMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveIncidentCategoryMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveIncidentCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
