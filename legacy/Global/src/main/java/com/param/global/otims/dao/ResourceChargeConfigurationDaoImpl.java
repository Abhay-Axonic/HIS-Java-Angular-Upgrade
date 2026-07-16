package com.param.global.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ResourceChargeConfigurationDto;
import com.param.otims.model.ResourceChargeConfiguration;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class ResourceChargeConfigurationDaoImpl extends GenericDao<ResourceChargeConfiguration, Integer> implements IResourceChargeConfigurationDao,ICommonConstants {

	final static Logger logger = Logger.getLogger(ResourceChargeConfigurationDaoImpl.class);

	public ResourceChargeConfigurationDaoImpl() {
		super(ResourceChargeConfiguration.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory; 
	
	
	@Override
	public Character isResourceChargeConfigurationAlreadyExist(
			ResourceChargeConfigurationDto resourceChargeConfigurationDto) throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST")
					.setParameter("orgId", resourceChargeConfigurationDto.getOrgId())
					.setParameter("unitId", resourceChargeConfigurationDto.getUnitId())
					.setParameter("roleId", resourceChargeConfigurationDto.getRoleId())
					.setParameter("serviceId", resourceChargeConfigurationDto.getServiceId())
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class))		
			.list();
					
			if(resourceChargeConfigurationDtosList.size()>0)
				return 'Y';
			else
				return 'N';
		}catch(HibernateException e){
			logger.error("isResourceChargeConfigurationAlreadyExist failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveResourceChargeConfiguration(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			ResourceChargeConfiguration resourceChargeConfiguration = mapper.map(resourceChargeConfigurationDto, ResourceChargeConfiguration.class, "ResourceChargeConfigurationDto_to_ResourceChargeConfiguration");
				resourceChargeConfiguration.setCreatedDate(TimeZoneComponent.getDateByzone(resourceChargeConfigurationDto.getUnitId()));
				resourceChargeConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(resourceChargeConfigurationDto.getUnitId()));
				resourceChargeConfiguration.setStatus('A');
				resourceChargeConfiguration = save(resourceChargeConfiguration);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException e) {
			logger.error("saveResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Character isResourceChargeConfigurationAlreadyExistNotById(
			ResourceChargeConfigurationDto resourceChargeConfigurationDto) throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST_NOT_BY_ID")
					.setParameter("orgId", resourceChargeConfigurationDto.getOrgId())
					.setParameter("unitId", resourceChargeConfigurationDto.getUnitId())
					.setParameter("roleId", resourceChargeConfigurationDto.getRoleId())
					.setParameter("serviceId", resourceChargeConfigurationDto.getServiceId())
					.setParameter("amount",resourceChargeConfigurationDto.getAmount())
					.setParameter("resourceChargeConfigurationId", resourceChargeConfigurationDto.getResourceChargeConfigurationId())
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class))		
			.list();
			if(resourceChargeConfigurationDtosList.size()>0)
				return 'Y';
			else
				return 'N';
		}catch(HibernateException e){
			logger.error("isResourceChargeConfigurationAlreadyExistNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateResourceChargeConfiguration(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			ResourceChargeConfiguration resourceChargeConfiguration = mapper.map(resourceChargeConfigurationDto, ResourceChargeConfiguration.class, "ResourceChargeConfigurationDto_to_ResourceChargeConfiguration");
				resourceChargeConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(resourceChargeConfigurationDto.getUnitId()));
				resourceChargeConfiguration = update(resourceChargeConfiguration);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		}catch (HibernateException e) {
			logger.error("updateResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChargeConfigurationById(Integer resourceChargeConfigurationId)
			throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_CHARGE_CONFIGURATION_BY_ID")
					.setParameter("resourceChargeConfigurationId", resourceChargeConfigurationId)
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class)).list();	
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, resourceChargeConfigurationDtosList, null);
		}catch(HibernateException e){
			logger.error("getResourceChargeConfigurationById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChargeConfigurationList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_CHARGE_CONFIGURATION_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, resourceChargeConfigurationDtosList, null);
		}catch(HibernateException e){
			logger.error("getResourceChargeConfigurationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateResourceChargeConfigurationStatus(
			ResourceChargeConfigurationDto resourceChargeConfigurationDto) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE ResourceChargeConfiguration set status=:status where resourceChargeConfigurationId=:resourceChargeConfigurationId")
				.setParameter("resourceChargeConfigurationId", resourceChargeConfigurationDto.getResourceChargeConfigurationId())
				.setCharacter("status", resourceChargeConfigurationDto.getStatus()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException e){
			logger.error("updateResourceChargeConfigurationStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChareges(ResourceChargeConfigurationDto resourceChargeConfigurationDto) {
		try{
			List<ResourceChargeConfigurationDto> resourceChargeConfigurationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_CHAREGES")
					.setParameter("orgId", resourceChargeConfigurationDto.getOrgId())
					.setParameter("unitId", resourceChargeConfigurationDto.getUnitId())
					.setParameter("specialityId", resourceChargeConfigurationDto.getSpecialityId())
					.setParameter("roleId", resourceChargeConfigurationDto.getRoleId())
					.setParameter("serviceId", resourceChargeConfigurationDto.getServiceId())
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, resourceChargeConfigurationDtosList, null);
		}catch(HibernateException e){
			logger.error("getResourceChareges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceCharegesByDoctor(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			List<ResourceChargeConfigurationDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_RESOURCE_CHAREGES_BY_DOCTOR")
					.setParameter("orgId", resourceChargeConfigurationDto.getOrgId())
					.setParameter("unitId", resourceChargeConfigurationDto.getUnitId())
					.setParameter("roleId", resourceChargeConfigurationDto.getRoleId())
					.setParameter("doctorId", resourceChargeConfigurationDto.getDoctorId())
					.setParameter("serviceId", resourceChargeConfigurationDto.getServiceId())
			.setResultTransformer(Transformers.aliasToBean(ResourceChargeConfigurationDto.class)).list();
			if(list.size()>0 && list!=null)
			{
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, list.getFirst());
			}
			else {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, 0);
			}

		}catch(HibernateException e){
			logger.error("getResourceCharegesByDoctor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
