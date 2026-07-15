package com.param.global.otims.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.AdditionalOtTimeDto;
import com.param.global.otims.dto.ResourceChargeConfigurationDto;
import com.param.otims.model.AdditionalOtTime;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings("unchecked")
public class AdditionalOtTimeDaoImpl extends GenericDao<AdditionalOtTime, Integer>implements IAdditionalOtTimeDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(AdditionalOtTimeDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	public AdditionalOtTimeDaoImpl() {
		super(AdditionalOtTime.class);
	}

	@Override
	public Character isAdditionalOtTimeExist(AdditionalOtTimeDto additionalOtTimeDto) throws ApplicationException {
		try{
			List<AdditionalOtTimeDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("IS_ADDITIONAL_OT_TIME_EXIST")
					.setParameter("orgId", additionalOtTimeDto.getOrgId())
					.setParameter("unitId", additionalOtTimeDto.getUnitId())
					.setParameter("procedureTypeId", additionalOtTimeDto.getProcedureTypeId())
					//.setParameter("serviceId", resourceChargeConfigurationDto.getServiceId())
			.setResultTransformer(Transformers.aliasToBean(AdditionalOtTimeDto.class))		
			.list();
					
			if(resourceChargeConfigurationDtosList.size()>0)
				return 'Y';
			else
				return 'N';
		}catch(HibernateException e){
		logger.error("isAdditionalOtTimeExist failed", e);
		}
		return 'N';
	}

	@Override
	public Response saveAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto) throws ApplicationException {
		try{
			AdditionalOtTime additionalOtTime = mapper.map(additionalOtTimeDto, AdditionalOtTime.class, "AdditionalOtTimeDto_to_AdditionalOtTime");
				additionalOtTime.setCreatedDate(TimeZoneComponent.getDateByzone(additionalOtTimeDto.getUnitId()));
				additionalOtTime.setUpdatedDate(TimeZoneComponent.getDateByzone(additionalOtTimeDto.getUnitId()));
				additionalOtTime.setStatus('A');
			additionalOtTime = save(additionalOtTime);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException e){
		logger.error("saveAdditionalOtTime failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Character isAdditionalOtTimeExistNotById(AdditionalOtTimeDto additionalOtTimeDto)
			throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("IS_ADDITIONAL_OT_TIME_EXIST_NOT_BY_ID")
					.setParameter("orgId", additionalOtTimeDto.getOrgId())
					.setParameter("unitId", additionalOtTimeDto.getUnitId())
					.setParameter("procedureTypeId", additionalOtTimeDto.getProcedureTypeId())
					.setParameter("timeMin", additionalOtTimeDto.getTimeMin())
					.setParameter("rate", additionalOtTimeDto.getRate())
					.setParameter("additionalOtTimeId", additionalOtTimeDto.getAdditionalOtTimeId())
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class))		
			.list();
					
			if(resourceChargeConfigurationDtosList.size()>0)
				return 'Y';
			else
				return 'N';
		}catch(HibernateException e){
		logger.error("isAdditionalOtTimeExistNotById failed", e);
		}
		return 'N';
	}

	@Override
	public Response updateAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto) throws ApplicationException {
		try{
			AdditionalOtTime additionalOtTime = mapper.map(additionalOtTimeDto, AdditionalOtTime.class, "AdditionalOtTimeDto_to_AdditionalOtTime");
				additionalOtTime.setUpdatedDate(TimeZoneComponent.getDateByzone(additionalOtTimeDto.getUnitId()));
				additionalOtTime.setStatus('A');
			additionalOtTime = update(additionalOtTime);
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException e){
		logger.error("updateAdditionalOtTime failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdditionalOtTimeList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<AdditionalOtTimeDto> additionalOtTimeDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("ADDITIONAL_OT_TIME_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(AdditionalOtTimeDto.class))		
			.list();
					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, additionalOtTimeDtosList, null);
		}catch(HibernateException e){
		logger.error("getAdditionalOtTimeList failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatusAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto) {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE AdditionalOtTime set status=:status where additionalOtTimeId=:additionalOtTimeId ")
				.setParameter("additionalOtTimeId", additionalOtTimeDto.getAdditionalOtTimeId())
				.setParameter("status", additionalOtTimeDto.getStatus()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException e){
		logger.error("updateStatusAdditionalOtTime failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAddtionalRate(AdditionalOtTimeDto additionalOtTimeDto) {
		try{
			List<AdditionalOtTimeDto> additionalOtTimeDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADDTIONAL_RATE")
					.setParameter("orgId", additionalOtTimeDto.getOrgId())
					.setParameter("unitId", additionalOtTimeDto.getUnitId())
					.setParameter("procedureTypeId", additionalOtTimeDto.getProcedureTypeId())
			.setResultTransformer(Transformers.aliasToBean(AdditionalOtTimeDto.class))		
			.list();
			double rate = 0;
			if(additionalOtTimeDtosList!=null && additionalOtTimeDtosList.size()>0)
			{
				 rate = additionalOtTimeDtosList.get(0).getRate().doubleValue();
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, rate);
		}catch(HibernateException e){
		logger.error("getAddtionalRate failed", e);
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
