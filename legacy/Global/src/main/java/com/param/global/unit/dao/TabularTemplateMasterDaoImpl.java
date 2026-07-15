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
import com.param.global.model.CabinMaster;
import com.param.global.model.TabularTemplateMaster;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.TabularTemplateMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class TabularTemplateMasterDaoImpl extends GenericDao<TabularTemplateMaster, Integer> implements ITabularTemplateMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(TabularTemplateMasterDaoImpl.class);

	
	public TabularTemplateMasterDaoImpl() {
		super(TabularTemplateMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;


	@Override
	public Response saveTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			TabularTemplateMaster tabularTemplateMaster=new TabularTemplateMaster();
			tabularTemplateMaster.setTabularTemplateCode(tabularTemplateMasterDto.getTabularTemplateCode());
			tabularTemplateMaster.setTabularTemplateDesc(tabularTemplateMasterDto.getTabularTemplateDesc());
			tabularTemplateMaster.setOrganizationId(tabularTemplateMasterDto.getOrganizationId());
			tabularTemplateMaster.setUnitId(tabularTemplateMasterDto.getUnitId());
			tabularTemplateMaster.setStatus(tabularTemplateMasterDto.getStatus());
			tabularTemplateMaster.setCreatedBy(tabularTemplateMasterDto.getCreatedBy());
			tabularTemplateMaster.setCreatedDate(GlobalCommonDateUtils.getDate(tabularTemplateMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			tabularTemplateMaster.setUpdatedBy(tabularTemplateMasterDto.getUpdatedBy());
			tabularTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tabularTemplateMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(tabularTemplateMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveTabularTemplateMaster failed", he);
		} catch (Exception e) {
		logger.error("saveTabularTemplateMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			TabularTemplateMaster tabularTemplateMaster=findById(tabularTemplateMasterDto.getTabularTemplateId());
			tabularTemplateMaster.setTabularTemplateCode(tabularTemplateMasterDto.getTabularTemplateCode());
			tabularTemplateMaster.setTabularTemplateDesc(tabularTemplateMasterDto.getTabularTemplateDesc());
			tabularTemplateMaster.setOrganizationId(tabularTemplateMasterDto.getOrganizationId());
			tabularTemplateMaster.setUnitId(tabularTemplateMasterDto.getUnitId());
			tabularTemplateMaster.setUpdatedBy(tabularTemplateMasterDto.getUpdatedBy());
			tabularTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tabularTemplateMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(tabularTemplateMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateTabularTemplateMaster failed", he);
	} catch (Exception e) {
	logger.error("updateTabularTemplateMaster failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateTabularTemplateMasterStatus(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			TabularTemplateMaster tabularTemplateMaster=findById(tabularTemplateMasterDto.getTabularTemplateId());
			
			tabularTemplateMaster.setStatus(tabularTemplateMasterDto.getStatus());
			tabularTemplateMaster.setUpdatedBy(tabularTemplateMasterDto.getUpdatedBy());
			tabularTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(tabularTemplateMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(tabularTemplateMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateTabularTemplateMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateTabularTemplateMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getTabularTemplateMasterById(Integer tabularTemplateId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<TabularTemplateMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_MASTER_BY_ID")
					.setParameter("tabularTemplateId", tabularTemplateId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TabularTemplateMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTabularTemplateMasterById failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTabularTemplateMasterList(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			List<TabularTemplateMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_MASTER_LIST")
					.setParameter("organizationId",  tabularTemplateMasterDto.getOrganizationId())
					.setParameter("unitId",  tabularTemplateMasterDto.getUnitId())
					.setFirstResult(tabularTemplateMasterDto.getOffset() != null ? tabularTemplateMasterDto.getOffset()  : 0)
					.setMaxResults(tabularTemplateMasterDto.getNoOfRecordsPerPage() != null ? tabularTemplateMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(TabularTemplateMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTabularTemplateMasterList failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTabularTemplateMasterCount(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(TabularTemplateMaster.class);
			crit.add(Restrictions.eq("organizationId", tabularTemplateMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", tabularTemplateMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getTabularTemplateMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveTabularTemplateMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<TabularTemplateMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TABULAR_TEMPLATE_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TabularTemplateMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveTabularTemplateMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveTabularTemplateMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getTabularTemplateMasterByName(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_MASTER_BY_NAME")
					.setParameter("tabularTemplateDesc", tabularTemplateMasterDto.getTabularTemplateDesc().toLowerCase())
					.setParameter("unitId", tabularTemplateMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTabularTemplateMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterByName failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getTabularTemplateMasterByNameNotId(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		try {
			List<TabularTemplateMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_MASTER_BY_NAME_NOT_ID")
					.setParameter("tabularTemplateId", tabularTemplateMasterDto.getTabularTemplateId())
					.setParameter("unitId", tabularTemplateMasterDto.getUnitId())
					.setParameter("tabularTemplateDesc", tabularTemplateMasterDto.getTabularTemplateDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(TabularTemplateMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTabularTemplateMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterByNameNotId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
