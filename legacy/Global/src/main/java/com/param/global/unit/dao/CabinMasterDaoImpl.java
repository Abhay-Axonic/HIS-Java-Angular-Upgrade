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
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CabinMasterDaoImpl extends GenericDao<CabinMaster, Integer> implements ICabinMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(CabinMasterDaoImpl.class);


	public CabinMasterDaoImpl() {
		super(CabinMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			CabinMaster cabinMaster=new CabinMaster();
			cabinMaster.setCabinCode(cabinMasterDto.getCabinCode());
			cabinMaster.setCabinDesc(cabinMasterDto.getCabinDesc());
			cabinMaster.setOrganizationId(cabinMasterDto.getOrganizationId());
			cabinMaster.setUnitId(cabinMasterDto.getUnitId());
			cabinMaster.setStatus(cabinMasterDto.getStatus());
			cabinMaster.setCreatedBy(cabinMasterDto.getCreatedBy());
			cabinMaster.setCreatedDate(GlobalCommonDateUtils.getDate(cabinMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			cabinMaster.setUpdatedBy(cabinMasterDto.getUpdatedBy());
			cabinMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cabinMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(cabinMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveCabinMaster failed", he);
		} catch (Exception e) {
		logger.error("saveCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
				CabinMaster cabinMaster=findById(cabinMasterDto.getCabinId());
				cabinMaster.setCabinCode(cabinMasterDto.getCabinCode());
				cabinMaster.setCabinDesc(cabinMasterDto.getCabinDesc());
				cabinMaster.setOrganizationId(cabinMasterDto.getOrganizationId());
				cabinMaster.setUnitId(cabinMasterDto.getUnitId());
				cabinMaster.setUpdatedBy(cabinMasterDto.getUpdatedBy());
				cabinMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cabinMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(cabinMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateCabinMaster failed", he);
		} catch (Exception e) {
		logger.error("updateCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCabinMasterStatus(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
				CabinMaster cabinMaster=findById(cabinMasterDto.getCabinId());
				
				cabinMaster.setStatus(cabinMasterDto.getStatus());
				cabinMaster.setUpdatedBy(cabinMasterDto.getUpdatedBy());
				cabinMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cabinMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 
				update(cabinMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateCabinMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateCabinMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}
	
	@Override
	public Response getCabinMasterById(Integer cabinId, Integer organizationId, Integer unitId) throws ApplicationException{
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CABIN_MASTER_BY_ID")
					.setParameter("cabinId", cabinId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCabinMasterById failed", he);
		} catch (Exception e) {
		logger.error("getCabinMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getCabinMasterList(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CABIN_MASTER_LIST")
					.setParameter("organizationId",  cabinMasterDto.getOrganizationId())
					.setParameter("unitId",  cabinMasterDto.getUnitId())
					.setFirstResult(cabinMasterDto.getOffset() != null ? cabinMasterDto.getOffset()  : 0)
					.setMaxResults(cabinMasterDto.getNoOfRecordsPerPage() != null ? cabinMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCabinMasterList failed", he);
		} catch (Exception e) {
		logger.error("getCabinMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterCount(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(CabinMaster.class);
			crit.add(Restrictions.eq("organizationId", cabinMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", cabinMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getCabinMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getCabinMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveCabinMaster(Integer organizationId, Integer unitId) throws ApplicationException{
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CABIN_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveCabinMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterByName(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CABIN_MASTER_BY_NAME")
					.setParameter("cabinDesc", cabinMasterDto.getCabinDesc().toLowerCase())
					.setParameter("unitId", cabinMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCabinMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getCabinMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterByNameNotId(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CABIN_MASTER_BY_NAME_NOT_ID")
					.setParameter("cabinId", cabinMasterDto.getCabinId())
					.setParameter("unitId", cabinMasterDto.getUnitId())
					.setParameter("cabinDesc", cabinMasterDto.getCabinDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CabinMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getCabinMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getCabinMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCoversheetAccessMaster(CoversheetAccessMasterDto coversheetAccessMasterDto)
			throws ApplicationException {
		
		String query = "select cam.coversheet_id as \"coversheetId\" "+ 
				"  from patient.m_coversheet_access_master cam" + 
				"   where cam.organization_id=:organizationId" + 
				"   and cam.unit_id=:unitId";
		
		coversheetAccessMasterDto = (CoversheetAccessMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("unitId", coversheetAccessMasterDto.getUnitId())
				.setParameter("organizationId", coversheetAccessMasterDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(CoversheetAccessMasterDto.class)).uniqueResult();
		if(coversheetAccessMasterDto!=null && coversheetAccessMasterDto.getCoversheetId()!=null) {
		   return new Response(SUCCESS, SUCCESS_CODE, null, null, coversheetAccessMasterDto.getCoversheetId());
		}else {
			return new Response(ERROR, SUCCESS_CODE, null, null,null);
		}
	}
	
}
