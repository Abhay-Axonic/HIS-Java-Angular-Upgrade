package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BedsideLabInvestigationDto;
import com.param.global.model.BedsideLabInvestigationMaster;
import com.param.global.model.CabinMaster;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public class BedsideLabInvestigationDaoImpl extends GenericDao<BedsideLabInvestigationMaster, Integer> implements IBedsideLabInvestigationDao , ICommonConstants{
	final static Logger logger = Logger.getLogger(BedsideLabInvestigationDaoImpl.class);


	public BedsideLabInvestigationDaoImpl() {
		super(BedsideLabInvestigationMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		try {
			BedsideLabInvestigationMaster bedsideLabInvestigationMaster=new BedsideLabInvestigationMaster();
			bedsideLabInvestigationMaster.setBedsideLabId(bedsideLabInvestigationDto.getBedsideLabId());
			bedsideLabInvestigationMaster.setBedsideLabCode(bedsideLabInvestigationDto.getBedsideLabCode());
			bedsideLabInvestigationMaster.setBedsideLabDesc(bedsideLabInvestigationDto.getBedsideLabDesc());
			bedsideLabInvestigationMaster.setMaxReferenceRange(bedsideLabInvestigationDto.getMaxReferenceRange());
			bedsideLabInvestigationMaster.setMinReferenceRange(bedsideLabInvestigationDto.getMinReferenceRange());
			bedsideLabInvestigationMaster.setOrganizationId(bedsideLabInvestigationDto.getOrganizationId());
			bedsideLabInvestigationMaster.setUnitId(bedsideLabInvestigationDto.getUnitId());
			bedsideLabInvestigationMaster.setBedsideUnitId(bedsideLabInvestigationDto.getBedsideUnitId());
			bedsideLabInvestigationMaster.setStatus(bedsideLabInvestigationDto.getStatus());
			bedsideLabInvestigationMaster.setCreatedBy(bedsideLabInvestigationDto.getCreatedBy());
			bedsideLabInvestigationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bedsideLabInvestigationDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bedsideLabInvestigationMaster.setUpdatedBy(bedsideLabInvestigationDto.getUpdatedBy());
			bedsideLabInvestigationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideLabInvestigationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(bedsideLabInvestigationMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveBedsideLabInvestigation failed", he);
		} catch (Exception e) {
		logger.error("saveBedsideLabInvestigation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);


	}

	@Override
	public Response getBedsideLabInvestigationByName(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		try {
			
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BED_SIDE_LAB_INVESTIGATION_BY_NAME")
					.setParameter("bedsideLabDesc", bedsideLabInvestigationDto.getBedsideLabDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideLabInvestigationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideLabInvestigationByName failed", he);
		} catch (Exception e) {
		logger.error("getBedsideLabInvestigationByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideLabInvestigationNotId(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		try {
			List<BedsideLabInvestigationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_LAB_INVESTIGATION_BY_NAME_NOT_ID")
					.setParameter("bedsideLabId", bedsideLabInvestigationDto.getBedsideLabId())
					.setParameter("bedsideLabDesc", bedsideLabInvestigationDto.getBedsideLabDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(BedsideLabInvestigationDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideLabInvestigationNotId failed", he);
		} catch (Exception e) {
		logger.error("getBedsideLabInvestigationNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		try {
			BedsideLabInvestigationMaster bedsideLabInvestigationMaster=findById(bedsideLabInvestigationDto.getBedsideLabId());
			bedsideLabInvestigationMaster.setBedsideLabCode(bedsideLabInvestigationDto.getBedsideLabCode());
			bedsideLabInvestigationMaster.setBedsideLabDesc(bedsideLabInvestigationDto.getBedsideLabDesc());
			bedsideLabInvestigationMaster.setBedsideUnitId(bedsideLabInvestigationDto.getBedsideUnitId());
			bedsideLabInvestigationMaster.setMaxReferenceRange(bedsideLabInvestigationDto.getMaxReferenceRange());
			bedsideLabInvestigationMaster.setMinReferenceRange(bedsideLabInvestigationDto.getMinReferenceRange());
			bedsideLabInvestigationMaster.setOrganizationId(bedsideLabInvestigationDto.getOrganizationId());
			bedsideLabInvestigationMaster.setUnitId(bedsideLabInvestigationDto.getUnitId());
			bedsideLabInvestigationMaster.setUpdatedBy(bedsideLabInvestigationDto.getUpdatedBy());
			bedsideLabInvestigationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideLabInvestigationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(bedsideLabInvestigationMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateBedsideLabInvestigation failed", he);
	} catch (Exception e) {
	logger.error("updateBedsideLabInvestigation failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateBedsideLabInvestigationStatus(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException {
		try {
			BedsideLabInvestigationMaster bedsideLabInvestigationMaster=findById(bedsideLabInvestigationDto.getBedsideLabId());
			
			bedsideLabInvestigationMaster.setStatus(bedsideLabInvestigationDto.getStatus());
			bedsideLabInvestigationMaster.setUpdatedBy(bedsideLabInvestigationDto.getUpdatedBy());
			bedsideLabInvestigationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bedsideLabInvestigationDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(bedsideLabInvestigationMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateBedsideLabInvestigationStatus failed", he);
	} catch (Exception e) {
	logger.error("updateBedsideLabInvestigationStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getBedsideLabInvestigationById(Integer bedsideLabId, Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BedsideLabInvestigationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_LAB_INVESTIGATION_BY_ID")
					.setParameter("bedsideLabId", bedsideLabId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BedsideLabInvestigationDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideLabInvestigationById failed", he);
		} catch (Exception e) {
		logger.error("getBedsideLabInvestigationById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	
	@Override
	public Response getBedsideLabInvestigationCount(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getActiveBedsideLabInvestigation(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BedsideLabInvestigationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_BEDSIDE_LAB_INVESTIGATION")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BedsideLabInvestigationDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveBedsideLabInvestigation failed", he);
		} catch (Exception e) {
		logger.error("getActiveBedsideLabInvestigation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideLabInvestigationList(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		try {
			List<BedsideLabInvestigationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BEDSIDE_LAB_INVESTIGATION_LIST")
					.setParameter("organizationId",  bedsideLabInvestigationDto.getOrganizationId())
					.setParameter("unitId",  bedsideLabInvestigationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BedsideLabInvestigationDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getBedsideLabInvestigationList failed", he);
		} catch (Exception e) {
		logger.error("getBedsideLabInvestigationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterCount(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
 