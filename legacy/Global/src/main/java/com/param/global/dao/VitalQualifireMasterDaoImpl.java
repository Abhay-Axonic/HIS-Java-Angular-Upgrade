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
import com.param.global.dto.VitalQualifireMasterDto;
import com.param.opd.coversheet.model.VitalQualifireMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class VitalQualifireMasterDaoImpl extends GenericDao<VitalQualifireMaster, Integer> implements IVitalQualifireMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(VitalQualifireMasterDaoImpl.class);


	public VitalQualifireMasterDaoImpl() {
		super(VitalQualifireMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			VitalQualifireMaster vitalQualifireMaster=new VitalQualifireMaster();
			vitalQualifireMaster.setVitalQualifireCode(vitalQualifireMasterDto.getVitalQualifireCode());
			vitalQualifireMaster.setVitalQualifireDesc(vitalQualifireMasterDto.getVitalQualifireDesc());
			vitalQualifireMaster.setVitalCategoryId(vitalQualifireMasterDto.getVitalCategoryId());
			vitalQualifireMaster.setOrganizationId(vitalQualifireMasterDto.getOrganizationId());
			vitalQualifireMaster.setUnitId(vitalQualifireMasterDto.getUnitId());
			vitalQualifireMaster.setStatus(vitalQualifireMasterDto.getStatus());
			vitalQualifireMaster.setCreatedBy(vitalQualifireMasterDto.getCreatedBy());
			vitalQualifireMaster.setCreatedDate(GlobalCommonDateUtils.getDate(vitalQualifireMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			vitalQualifireMaster.setUpdatedBy(vitalQualifireMasterDto.getUpdatedBy());
			vitalQualifireMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vitalQualifireMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(vitalQualifireMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveVitalQualifireMaster failed", he);
		} catch (Exception e) {
		logger.error("saveVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
				VitalQualifireMaster vitalQualifireMaster=findById(vitalQualifireMasterDto.getVitalQualifireId());
				vitalQualifireMaster.setVitalCategoryId(vitalQualifireMasterDto.getVitalCategoryId());
				vitalQualifireMaster.setVitalQualifireCode(vitalQualifireMasterDto.getVitalQualifireCode());
				vitalQualifireMaster.setVitalQualifireDesc(vitalQualifireMasterDto.getVitalQualifireDesc());
				vitalQualifireMaster.setOrganizationId(vitalQualifireMasterDto.getOrganizationId());
				vitalQualifireMaster.setUnitId(vitalQualifireMasterDto.getUnitId());
				vitalQualifireMaster.setUpdatedBy(vitalQualifireMasterDto.getUpdatedBy());
				vitalQualifireMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vitalQualifireMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(vitalQualifireMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateVitalQualifireMaster failed", he);
		} catch (Exception e) {
		logger.error("updateVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMasterStatus(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
				VitalQualifireMaster vitalQualifireMaster=findById(vitalQualifireMasterDto.getVitalQualifireId());
				
				vitalQualifireMaster.setStatus(vitalQualifireMasterDto.getStatus());
				vitalQualifireMaster.setUpdatedBy(vitalQualifireMasterDto.getUpdatedBy());
				vitalQualifireMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(vitalQualifireMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 
				update(vitalQualifireMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateVitalQualifireMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateVitalQualifireMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}
	
	@Override
	public Response getVitalQualifireMasterById(Integer vitalQualifireId, Integer organizationId) throws ApplicationException{
		try {
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MASTER_BY_ID")
					.setParameter("vitalQualifireId", vitalQualifireId)
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMasterById failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getVitalQualifireMasterList(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MASTER_LIST")
					.setParameter("organizationId",  vitalQualifireMasterDto.getOrganizationId())
					.setFirstResult(vitalQualifireMasterDto.getOffset() != null ? vitalQualifireMasterDto.getOffset()  : 0)
					.setMaxResults(vitalQualifireMasterDto.getNoOfRecordsPerPage() != null ? vitalQualifireMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMasterList failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterCount(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(VitalQualifireMaster.class);
			crit.add(Restrictions.eq("organizationId", vitalQualifireMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", vitalQualifireMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVitalQualifireMaster(Integer organizationId) throws ApplicationException{
		try {
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_VITAL_QUALIFIRE_MASTER")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveVitalQualifireMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterByName(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MASTER_BY_NAME")
					.setParameter("vitalQualifireDesc", vitalQualifireMasterDto.getVitalQualifireDesc().toLowerCase())
					.setParameter("organizationId", vitalQualifireMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterByNameNotId(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MASTER_BY_NAME_NOT_ID")
					.setParameter("vitalQualifireId", vitalQualifireMasterDto.getVitalQualifireId())
					.setParameter("vitalQualifireDesc", vitalQualifireMasterDto.getVitalQualifireDesc().toLowerCase())
					.setParameter("organizationId", vitalQualifireMasterDto.getOrganizationId())

					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalCategoryList(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException {
		try {
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_CATEGORY_LIST")
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalCategoryList failed", he);
		} catch (Exception e) {
		logger.error("getVitalCategoryList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
