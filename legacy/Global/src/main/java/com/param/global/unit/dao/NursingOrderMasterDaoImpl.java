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
import com.param.global.model.NursingOrderMaster;
import com.param.global.unit.dto.NursingOrderMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public  class NursingOrderMasterDaoImpl  extends GenericDao<NursingOrderMaster, Integer> implements INursingOrderMasterDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingOrderMasterDaoImpl.class);


	public NursingOrderMasterDaoImpl() {
		super(NursingOrderMaster.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		try {
			NursingOrderMaster nursingOrderMaster=new NursingOrderMaster();
			nursingOrderMaster.setNursingOrderCode(nursingOrderMasterDto.getNursingOrderCode());
			nursingOrderMaster.setNursingOrderDesc(nursingOrderMasterDto.getNursingOrderDesc());
			nursingOrderMaster.setServiceId(nursingOrderMasterDto.getServiceId());
			nursingOrderMaster.setSpecialityId(nursingOrderMasterDto.getSpecialityId());
			nursingOrderMaster.setSubSpecialityId(nursingOrderMasterDto.getSubSpecialityId());
			nursingOrderMaster.setOrganizationId(nursingOrderMasterDto.getOrganizationId());
			nursingOrderMaster.setUnitId(nursingOrderMasterDto.getUnitId());
			nursingOrderMaster.setStatus(nursingOrderMasterDto.getStatus());
			nursingOrderMaster.setCreatedBy(nursingOrderMasterDto.getCreatedBy());
			nursingOrderMaster.setCreatedDate(GlobalCommonDateUtils.getDate(nursingOrderMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nursingOrderMaster.setUpdatedBy(nursingOrderMasterDto.getUpdatedBy());
			nursingOrderMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingOrderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(nursingOrderMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveNursingOrderMaster failed", he);
		} catch (Exception e) {
		logger.error("saveNursingOrderMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		try {
			NursingOrderMaster nursingOrderMaster=findById(nursingOrderMasterDto.getNursingOrderId());
			nursingOrderMaster.setNursingOrderCode(nursingOrderMasterDto.getNursingOrderCode());
			nursingOrderMaster.setNursingOrderDesc(nursingOrderMasterDto.getNursingOrderDesc());
			nursingOrderMaster.setServiceId(nursingOrderMasterDto.getServiceId());
			nursingOrderMaster.setSpecialityId(nursingOrderMasterDto.getSpecialityId());
			nursingOrderMaster.setSubSpecialityId(nursingOrderMasterDto.getSubSpecialityId());
			nursingOrderMaster.setOrganizationId(nursingOrderMasterDto.getOrganizationId());
			nursingOrderMaster.setUnitId(nursingOrderMasterDto.getUnitId());
			nursingOrderMaster.setUpdatedBy(nursingOrderMasterDto.getUpdatedBy());
			nursingOrderMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingOrderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(nursingOrderMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateNursingOrderMaster failed", he);
	} catch (Exception e) {
	logger.error("updateNursingOrderMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateNursingOrderMasterStatus(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {
			NursingOrderMaster nursingOrderMaster=findById(nursingOrderMasterDto.getNursingOrderId());
			
			nursingOrderMaster.setStatus(nursingOrderMasterDto.getStatus());
			nursingOrderMaster.setUpdatedBy(nursingOrderMasterDto.getUpdatedBy());
			nursingOrderMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingOrderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(nursingOrderMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateNursingOrderMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateNursingOrderMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getNursingOrderMasterById(Integer nursingOrderId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_ORDER_MASTER_BY_ID")
					.setParameter("nursingOrderId", nursingOrderId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterById failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingOrderMasterList(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		try {
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_ORDER_MASTER_LIST")
					.setParameter("organizationId",  nursingOrderMasterDto.getOrganizationId())
					.setParameter("unitId",  nursingOrderMasterDto.getUnitId())
					.setFirstResult(nursingOrderMasterDto.getOffset() != null ? nursingOrderMasterDto.getOffset()  : 0)
					.setMaxResults(nursingOrderMasterDto.getNoOfRecordsPerPage() != null ? nursingOrderMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterList failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingOrderMasterCount(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(NursingOrderMaster.class);
			crit.add(Restrictions.eq("organizationId", nursingOrderMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", nursingOrderMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveNursingOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_NURSING_ORDER_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveNursingOrderMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveNursingOrderMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingOrderMasterByName(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {
			
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_ORDER_MASTER_BY_NAME")
					.setParameter("organizationId", nursingOrderMasterDto.getOrganizationId())
					.setParameter("unitId", nursingOrderMasterDto.getUnitId())
					.setParameter("nursingOrderDesc", nursingOrderMasterDto.getNursingOrderDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingOrderMasterByNameNotId(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_ORDER_MASTER_BY_NAME_NOT_ID")
					.setParameter("organizationId", nursingOrderMasterDto.getOrganizationId())
					.setParameter("unitId", nursingOrderMasterDto.getUnitId())
					.setParameter("nursingOrderId", nursingOrderMasterDto.getNursingOrderId())
					.setParameter("nursingOrderDesc", nursingOrderMasterDto.getNursingOrderDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingOrderMasterListSearch(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {
			List<NursingOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_ORDER_MASTER_LIST_SERARCH")
					.setParameter("organizationId",  nursingOrderMasterDto.getOrganizationId())
					.setParameter("unitId",  nursingOrderMasterDto.getUnitId())
					.setParameter("nursingOrderCode", "%"+nursingOrderMasterDto.getNursingOrderCode().toLowerCase()+"%")
//					.setFirstResult(nursingOrderMasterDto.getOffset() != null ? nursingOrderMasterDto.getOffset()  : 0)
//					.setMaxResults(nursingOrderMasterDto.getNoOfRecordsPerPage() != null ? nursingOrderMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingOrderMasterListSearch failed", he);
		} catch (Exception e) {
		logger.error("getNursingOrderMasterListSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
