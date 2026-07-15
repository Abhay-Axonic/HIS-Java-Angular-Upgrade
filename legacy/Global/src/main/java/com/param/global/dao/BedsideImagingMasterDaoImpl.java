package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BedsideImagingMasterDto;
import com.param.global.model.BedsideImagingMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class BedsideImagingMasterDaoImpl extends GenericDao<BedsideImagingMaster, Integer> implements IBedsideImagingMasterDao,ICommonConstants
{

	private static final Logger logger = Logger.getLogger(BedsideImagingMasterDaoImpl.class);

	public BedsideImagingMasterDaoImpl() {
		super(BedsideImagingMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			BedsideImagingMaster bedsideImagingMaster=new BedsideImagingMaster();
			bedsideImagingMaster.setBedsideImagingCode(bedsideImagingMasterDto.getBedsideImagingCode());
			bedsideImagingMaster.setBedsideImagingDesc(bedsideImagingMasterDto.getBedsideImagingDesc());
			bedsideImagingMaster.setSubSpecialityId(bedsideImagingMasterDto.getSubSpecialityId());
			bedsideImagingMaster.setOrganizationId(bedsideImagingMasterDto.getOrganizationId());
			bedsideImagingMaster.setUnitId(bedsideImagingMasterDto.getUnitId());
			bedsideImagingMaster.setStatus(bedsideImagingMasterDto.getStatus());
			bedsideImagingMaster.setCreatedBy(bedsideImagingMasterDto.getCreatedBy());
			bedsideImagingMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bedsideImagingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bedsideImagingMaster.setUpdatedBy(bedsideImagingMasterDto.getUpdatedBy());
			bedsideImagingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideImagingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(bedsideImagingMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			BedsideImagingMaster bedsideImagingMaster=findById(bedsideImagingMasterDto.getBedsideImagingId());
			bedsideImagingMaster.setBedsideImagingCode(bedsideImagingMasterDto.getBedsideImagingCode());
			bedsideImagingMaster.setBedsideImagingDesc(bedsideImagingMasterDto.getBedsideImagingDesc());
			bedsideImagingMaster.setSubSpecialityId(bedsideImagingMasterDto.getSubSpecialityId());
			bedsideImagingMaster.setOrganizationId(bedsideImagingMasterDto.getOrganizationId());
			bedsideImagingMaster.setUnitId(bedsideImagingMasterDto.getUnitId());
			bedsideImagingMaster.setUpdatedBy(bedsideImagingMasterDto.getUpdatedBy());
			bedsideImagingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideImagingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(bedsideImagingMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateBedsideImagingMasterStatus(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			BedsideImagingMaster bedsideImagingMaster=findById(bedsideImagingMasterDto.getBedsideImagingId());
			bedsideImagingMaster.setStatus(bedsideImagingMasterDto.getStatus());
			bedsideImagingMaster.setOrganizationId(bedsideImagingMasterDto.getOrganizationId());
			bedsideImagingMaster.setUnitId(bedsideImagingMasterDto.getUnitId());
			bedsideImagingMaster.setUpdatedBy(bedsideImagingMasterDto.getUpdatedBy());
			bedsideImagingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideImagingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(bedsideImagingMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error(he.getMessage(), he);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response getBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			List<BedsideImagingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_IMAGING_MASTER_LIST")
					.setParameter("organizationId",  bedsideImagingMasterDto.getOrganizationId())
					.setParameter("unitId",  bedsideImagingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedsideImagingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			List<BedsideImagingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_IMAGING_MASTER_LIST")
					.setParameter("organizationId",  bedsideImagingMasterDto.getOrganizationId())
					.setParameter("unitId",  bedsideImagingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedsideImagingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideImagingMasterByName(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
	try {

			List<BedsideImagingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_IMAGING_MASTER_BY_NAME")
					.setParameter("bedsideImagingDesc", bedsideImagingMasterDto.getBedsideImagingDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideImagingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideImagingMasterByNameNotId(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			List<BedsideImagingMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_IMAGING_MASTER_BY_NAME_NOT_ID")
					.setParameter("bedsideImagingId", bedsideImagingMasterDto.getBedsideImagingId())
					.setParameter("bedsideImagingDesc", bedsideImagingMasterDto.getBedsideImagingDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideImagingMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
