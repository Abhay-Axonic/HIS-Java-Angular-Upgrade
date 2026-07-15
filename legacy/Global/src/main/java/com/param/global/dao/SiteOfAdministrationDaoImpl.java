package com.param.global.dao;

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
import com.param.global.dto.SiteOfAdministrationMasterDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.SiteOfAdministrationMaster;
import com.param.global.model.VaccinationMaster;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class SiteOfAdministrationDaoImpl extends GenericDao<SiteOfAdministrationMaster, Integer> implements SiteOfAdministrationDao,ICommonConstants {
	final static Logger logger = Logger.getLogger(SiteOfAdministrationDaoImpl.class);


	public SiteOfAdministrationDaoImpl() {
		super(SiteOfAdministrationMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			SiteOfAdministrationMaster siteOfAdministrationMaster=new SiteOfAdministrationMaster();
			siteOfAdministrationMaster.setSiteCode(siteOfAdministrationMasterDto.getSiteCode());
			siteOfAdministrationMaster.setSiteName(siteOfAdministrationMasterDto.getSiteName());
			siteOfAdministrationMaster.setOrganizationId(siteOfAdministrationMasterDto.getOrganizationId());
			siteOfAdministrationMaster.setStatus(siteOfAdministrationMasterDto.getStatus());
			siteOfAdministrationMaster.setCreatedBy(siteOfAdministrationMasterDto.getCreatedBy());
			
			siteOfAdministrationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(siteOfAdministrationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			siteOfAdministrationMaster.setUpdatedBy(siteOfAdministrationMaster.getUpdatedBy());
			siteOfAdministrationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(siteOfAdministrationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			siteOfAdministrationMaster = save(siteOfAdministrationMaster);
		
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, siteOfAdministrationMaster);
		
		} catch (HibernateException he) {
		logger.error("saveSiteOfAdministrationMaster failed", he);
		} catch (Exception e) {
		logger.error("saveSiteOfAdministrationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSiteOfAdministrationMaster(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			SiteOfAdministrationMaster siteOfAdministrationMaster=findById(siteOfAdministrationMasterDto.getSiteId());
			siteOfAdministrationMaster.setSiteCode(siteOfAdministrationMasterDto.getSiteCode());
			siteOfAdministrationMaster.setSiteName(siteOfAdministrationMasterDto.getSiteName());
			siteOfAdministrationMaster.setOrganizationId(siteOfAdministrationMasterDto.getOrganizationId());
			siteOfAdministrationMaster.setUpdatedBy(siteOfAdministrationMasterDto.getUpdatedBy());
			siteOfAdministrationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(siteOfAdministrationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
			siteOfAdministrationMaster = update(siteOfAdministrationMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, siteOfAdministrationMaster);
	
	} catch (HibernateException he) {
	logger.error("updateSiteOfAdministrationMaster failed", he);
	} catch (Exception e) {
	logger.error("updateSiteOfAdministrationMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSiteOfAdministrationMasterStatus(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			SiteOfAdministrationMaster siteOfAdministrationMaster=findById(siteOfAdministrationMasterDto.getSiteId());
			siteOfAdministrationMaster.setStatus(siteOfAdministrationMasterDto.getStatus());
			siteOfAdministrationMaster.setUpdatedBy(siteOfAdministrationMasterDto.getUpdatedBy());
			siteOfAdministrationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(siteOfAdministrationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(siteOfAdministrationMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateSiteOfAdministrationMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateSiteOfAdministrationMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterById(Integer siteId, Integer organizationId)
			throws ApplicationException {
		try {
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SITE_OF_ADMININSTRATION_MASTER_BY_ID")
					.setParameter("siteId",siteId)
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterById failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto, Integer organizationId)
			throws ApplicationException {
		try {
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SITE_OF_ADMININSTRATION_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					.setFirstResult(siteOfAdministrationMasterDto.getOffset() != null ? siteOfAdministrationMasterDto.getOffset()  : 0)
					.setMaxResults(siteOfAdministrationMasterDto.getNoOfRecordsPerPage() != null ? siteOfAdministrationMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterList failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterCount(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(SiteOfAdministrationMaster.class);
			crit.add(Restrictions.eq("organizationId", siteOfAdministrationMasterDto.getOrganizationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveSiteOfAdministrationMaster(Integer organizationId) throws ApplicationException {
		try {
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SITE_OF_ADMININSTRATION_MASTER")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveSiteOfAdministrationMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveSiteOfAdministrationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterByName(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
try {
			
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SITE_OF_ADMININSTRATION_MASTER_BY_NAME")
					.setParameter("siteName", siteOfAdministrationMasterDto.getSiteName().toLowerCase())
					.setParameter("organizationId", siteOfAdministrationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterByNameNotId(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto) throws ApplicationException {
		try {
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SITE_OF_ADMININSTRATION_MASTER_BY_NAME_NOT_ID")
					.setParameter("siteId", siteOfAdministrationMasterDto.getSiteId())
					.setParameter("siteName", siteOfAdministrationMasterDto.getSiteName().toLowerCase())
					.setParameter("organizationId", siteOfAdministrationMasterDto.getOrganizationId())

					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSiteOfAdministrationMasterSearchList(SiteOfAdministrationMasterDto siteOfAdministrationMasterDto)
			throws ApplicationException {
		try {
			List<SiteOfAdministrationMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SITE_OF_ADMININSTRATION_MASTER_LIST_SEARCH")
					.setParameter("organizationId", siteOfAdministrationMasterDto.getOrganizationId())
					.setParameter("siteCode","%"+siteOfAdministrationMasterDto.getSiteCode().toLowerCase()+"%" )
//					.setFirstResult(siteOfAdministrationMasterDto.getOffset() != null ? siteOfAdministrationMasterDto.getOffset()  : 0)
//					.setMaxResults(siteOfAdministrationMasterDto.getNoOfRecordsPerPage() != null ? siteOfAdministrationMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(SiteOfAdministrationMasterDto.class))
					.list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getSiteOfAdministrationMasterSearchList failed", he);
		} catch (Exception e) {
		logger.error("getSiteOfAdministrationMasterSearchList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
