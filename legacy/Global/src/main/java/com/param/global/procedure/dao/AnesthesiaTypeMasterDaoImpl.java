package com.param.global.procedure.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.AnesthesiaTypeMasterDto;
import com.param.otims.model.AnesthesiaTypeMaster;


import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AnesthesiaTypeMasterDaoImpl extends GenericDao<AnesthesiaTypeMaster, Integer> implements IAnesthesiaTypeMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(AnesthesiaTypeMasterDaoImpl.class);


	public AnesthesiaTypeMasterDaoImpl() {
		super(AnesthesiaTypeMaster.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getAnesthesiaTypeList(int orgId, int unitId) throws ApplicationException {
		try{
			List<AnesthesiaTypeMasterDto> listAnesthesiaTypeMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ANESTHESIA_TYPE_LIST").setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(AnesthesiaTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listAnesthesiaTypeMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			AnesthesiaTypeMaster anesthesiaTypeMaster=new AnesthesiaTypeMaster();
			anesthesiaTypeMaster.setAnesthesiaTypeCode(anesthesiaTypeMasterDto.getAnesthesiaTypeCode());
			anesthesiaTypeMaster.setAnesthesiaTypeDesc(anesthesiaTypeMasterDto.getAnesthesiaTypeDesc());
			anesthesiaTypeMaster.setAnesthesiaTypeName(anesthesiaTypeMasterDto.getAnesthesiaTypeName());
			anesthesiaTypeMaster.setOrgId(anesthesiaTypeMasterDto.getOrgId());
			anesthesiaTypeMaster.setUnitId(anesthesiaTypeMasterDto.getUnitId());
			anesthesiaTypeMaster.setCreatedBy(anesthesiaTypeMasterDto.getCreatedBy());
			anesthesiaTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(anesthesiaTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			anesthesiaTypeMaster.setUpdatedBy(anesthesiaTypeMasterDto.getUpdatedBy());
			anesthesiaTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(anesthesiaTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			anesthesiaTypeMaster.setStatus(anesthesiaTypeMasterDto.getStatus());
			anesthesiaTypeMaster=save(anesthesiaTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, anesthesiaTypeMaster);
		} catch (HibernateException he) {
		logger.error("saveAnesthesiaTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("saveAnesthesiaTypeMaster failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	public Response updateAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			AnesthesiaTypeMaster anesthesiaTypeMaster=findById(anesthesiaTypeMasterDto.getAnesthesiaTypeId());
			anesthesiaTypeMaster.setAnesthesiaTypeCode(anesthesiaTypeMasterDto.getAnesthesiaTypeCode());
			anesthesiaTypeMaster.setAnesthesiaTypeDesc(anesthesiaTypeMasterDto.getAnesthesiaTypeDesc());
			anesthesiaTypeMaster.setAnesthesiaTypeName(anesthesiaTypeMasterDto.getAnesthesiaTypeName());
			anesthesiaTypeMaster.setOrgId(anesthesiaTypeMasterDto.getOrgId());
			anesthesiaTypeMaster.setUnitId(anesthesiaTypeMasterDto.getUnitId());
			anesthesiaTypeMaster.setUpdatedBy(anesthesiaTypeMasterDto.getUpdatedBy());
			anesthesiaTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(anesthesiaTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		
			update(anesthesiaTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateAnesthesiaTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("updateAnesthesiaTypeMaster failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAnesthesiaTypeMasterStatus(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			AnesthesiaTypeMaster anesthesiaTypeMaster=findById(anesthesiaTypeMasterDto.getAnesthesiaTypeId());
			anesthesiaTypeMaster.setUpdatedBy(anesthesiaTypeMasterDto.getUpdatedBy());
			anesthesiaTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(anesthesiaTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			anesthesiaTypeMaster.setStatus(anesthesiaTypeMasterDto.getStatus());
			
			update(anesthesiaTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateAnesthesiaTypeMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateAnesthesiaTypeMasterStatus failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getListAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			List<AnesthesiaTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_ANESTHESIA_TYPE_MASTER")
					.setParameter("orgId", anesthesiaTypeMasterDto.getOrgId())
					.setParameter("unitId", anesthesiaTypeMasterDto.getUnitId())
					.setFirstResult(anesthesiaTypeMasterDto.getOffset() != null ? anesthesiaTypeMasterDto.getOffset() : 0)
					.setMaxResults(anesthesiaTypeMasterDto.getNoOfRecordsPerPage() != null ? anesthesiaTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AnesthesiaTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getListAnesthesiaTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("getListAnesthesiaTypeMaster failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAnesthesiaTypeMasterById(Integer anesthesiaTypeId, Integer orgId, Integer unitId)throws ApplicationException {
			
		try {
			List<AnesthesiaTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANESTHESIA_TYPE_MASTER_BY_ID")
					.setParameter("anesthesiaTypeId", anesthesiaTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(AnesthesiaTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getAnesthesiaTypeMasterById failed", he);
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterById failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAnesthesiaTypeMasterCount(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
	try{
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(AnesthesiaTypeMaster.class);
		crit.add(Restrictions.eq("orgId", anesthesiaTypeMasterDto.getOrgId()));
		crit.add(Restrictions.eq("unitId", anesthesiaTypeMasterDto.getUnitId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		} catch (HibernateException he) {
		logger.error("getAnesthesiaTypeMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAnesthesiaTypeMasterByName(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			List<AnesthesiaTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANESTHESIA_TYPE_MASTER_BY_NAME")
					.setParameter("orgId", anesthesiaTypeMasterDto.getOrgId()) //dilip
					.setParameter("unitId", anesthesiaTypeMasterDto.getUnitId()) //dilip
					.setParameter("anesthesiaTypeName", anesthesiaTypeMasterDto.getAnesthesiaTypeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AnesthesiaTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getAnesthesiaTypeMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterByName failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAnesthesiaTypeMasterByNameNotId(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)throws ApplicationException {
			
		try {
			List<AnesthesiaTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANESTHESIA_TYPE_MASTER_BY_NAME_NOT_ID")
					.setParameter("orgId", anesthesiaTypeMasterDto.getOrgId()) //dilip
					.setParameter("unitId", anesthesiaTypeMasterDto.getUnitId()) //dilip
					.setParameter("anesthesiaTypeId", anesthesiaTypeMasterDto.getAnesthesiaTypeId())
					.setParameter("anesthesiaTypeName", anesthesiaTypeMasterDto.getAnesthesiaTypeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AnesthesiaTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getAnesthesiaTypeMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getAnesthesiaTypeMasterByNameNotId failed", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
