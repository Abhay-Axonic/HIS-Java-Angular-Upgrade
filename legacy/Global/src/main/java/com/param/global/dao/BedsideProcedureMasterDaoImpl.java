package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BedsideProcedureMasterDto;
import com.param.global.dto.BedsideProcedureMasterDto;
import com.param.global.model.BedsideProcedureMaster;
import com.param.global.service.IBedsideProcedureMasterService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class BedsideProcedureMasterDaoImpl   extends GenericDao<BedsideProcedureMaster, Integer> implements IBedsideProcedureMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(BedsideProcedureMasterDaoImpl.class);


	public BedsideProcedureMasterDaoImpl() {
		super(BedsideProcedureMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			BedsideProcedureMaster bedsideProcedureMaster=new BedsideProcedureMaster();
			bedsideProcedureMaster.setBedsideProcedureCode(bedsideProcedureMasterDto.getBedsideProcedureCode());
			bedsideProcedureMaster.setBedsideProcedureDesc(bedsideProcedureMasterDto.getBedsideProcedureDesc());
			bedsideProcedureMaster.setIsAnaesthesia(bedsideProcedureMasterDto.getIsAnaesthesia());
			bedsideProcedureMaster.setDuration(bedsideProcedureMasterDto.getDuration());
			bedsideProcedureMaster.setOrganizationId(bedsideProcedureMasterDto.getOrganizationId());
			bedsideProcedureMaster.setUnitId(bedsideProcedureMasterDto.getUnitId());
			bedsideProcedureMaster.setStatus(bedsideProcedureMasterDto.getStatus());
			bedsideProcedureMaster.setCreatedBy(bedsideProcedureMasterDto.getCreatedBy());
			bedsideProcedureMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bedsideProcedureMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bedsideProcedureMaster.setUpdatedBy(bedsideProcedureMasterDto.getUpdatedBy());
			bedsideProcedureMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideProcedureMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(bedsideProcedureMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveBedsideProcedureMaster failed", he);
		} catch (Exception e) {
		logger.error("saveBedsideProcedureMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response updateBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			BedsideProcedureMaster bedsideProcedureMaster=findById(bedsideProcedureMasterDto.getBedsideProcedureId());
			bedsideProcedureMaster.setBedsideProcedureCode(bedsideProcedureMasterDto.getBedsideProcedureCode());
			bedsideProcedureMaster.setBedsideProcedureDesc(bedsideProcedureMasterDto.getBedsideProcedureDesc());
			bedsideProcedureMaster.setIsAnaesthesia(bedsideProcedureMasterDto.getIsAnaesthesia());
			bedsideProcedureMaster.setDuration(bedsideProcedureMasterDto.getDuration());
			bedsideProcedureMaster.setOrganizationId(bedsideProcedureMasterDto.getOrganizationId());
			bedsideProcedureMaster.setUnitId(bedsideProcedureMasterDto.getUnitId());
			bedsideProcedureMaster.setUpdatedBy(bedsideProcedureMasterDto.getUpdatedBy());
			bedsideProcedureMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideProcedureMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(bedsideProcedureMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateBedsideProcedureMaster failed", he);
	} catch (Exception e) {
	logger.error("updateBedsideProcedureMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateBedsideProcedureMasterStatus(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			BedsideProcedureMaster bedsideProcedureMaster=findById(bedsideProcedureMasterDto.getBedsideProcedureId());
			bedsideProcedureMaster.setStatus(bedsideProcedureMasterDto.getStatus());
			bedsideProcedureMaster.setOrganizationId(bedsideProcedureMasterDto.getOrganizationId());
			bedsideProcedureMaster.setUnitId(bedsideProcedureMasterDto.getUnitId());
			bedsideProcedureMaster.setUpdatedBy(bedsideProcedureMasterDto.getUpdatedBy());
			bedsideProcedureMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideProcedureMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(bedsideProcedureMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateBedsideProcedureMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateBedsideProcedureMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response getBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			List<BedsideProcedureMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_PROCEDURE_MASTER_LIST")
					.setParameter("organizationId",  bedsideProcedureMasterDto.getOrganizationId())
					.setParameter("unitId",  bedsideProcedureMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedsideProcedureMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideProcedureMasterList failed", he);
		} catch (Exception e) {
		logger.error("getBedsideProcedureMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			List<BedsideProcedureMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BEDSIDE_PROCEDURE_MASTER")
					.setParameter("organizationId",  bedsideProcedureMasterDto.getOrganizationId())
					.setParameter("unitId",  bedsideProcedureMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedsideProcedureMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveBedsideProcedureMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveBedsideProcedureMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideProcedureMasterByName(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
	try {
			
			List<BedsideProcedureMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME")
					.setParameter("bedsideProcedureDesc", bedsideProcedureMasterDto.getBedsideProcedureDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideProcedureMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideProcedureMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getBedsideProcedureMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideProcedureMasterByNameNotId(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			List<BedsideProcedureMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME_NOT_ID")
					.setParameter("bedsideProcedureId", bedsideProcedureMasterDto.getBedsideProcedureId())
					.setParameter("bedsideProcedureDesc", bedsideProcedureMasterDto.getBedsideProcedureDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideProcedureMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideProcedureMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getBedsideProcedureMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
