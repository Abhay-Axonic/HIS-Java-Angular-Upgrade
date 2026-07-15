package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.transaction.config.model.RegistrationFieldConfiguration;
import com.param.entity.model.master.DifferentTypesScreenImageDTO;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.param.billing.global.transaction.config.model.UnitApplicationBillingConfiguration;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IDifferentTypesScreenImageDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;
import scala.collection.generic.BitOperations.Int;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class UnitApplicationBillingConfigurationServiceImpl implements IUnitApplicationBillingConfigurationService,ICommonConstants{

	final static Logger logger = Logger.getLogger(UnitApplicationBillingConfigurationServiceImpl.class);

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IDifferentTypesScreenImageDao iDifferentTypesLogoImageDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<RegistrationFieldConfigurationDto>> unitApplicationRegistrationFieldConfigMasterCache;
	private Cache<Integer, List<UnitApplicationConfigurationDto>> unitApplicationBillingConfigListCache;

	@PostConstruct
	public void initCache() {
		unitApplicationRegistrationFieldConfigMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();

		unitApplicationBillingConfigListCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	
	@Override
	public Response getUnitApplicationBillingConfigByOrgUnit(
			UnitApplicationConfigurationDto unitApplicationConfigurationDto) throws ApplicationException {
		try {
			//return iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			Response response = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)response.getObject();
			
			ApplicationConfiguration appconfig = GlobalDao.getApplicationConfigurationByUnitId(unitApplicationConfigurationDto.getUnitId());
			RegistrationFieldConfiguration registrationFieldConfiguration = GlobalDao.getRegistrationFieldConfigurationByUnitId(unitApplicationConfigurationDto.getUnitId());
			if(appconfig!=null){
			unitApplicationConfigurationDtos.setTaxInclusive(appconfig.getTaxInclusive()!=null ? appconfig.getTaxInclusive() : null);
			unitApplicationConfigurationDtos.setAssignPickerRequired(appconfig.getAssignPickerRequired()!=null ? appconfig.getAssignPickerRequired() : null);
			unitApplicationConfigurationDtos.setIssueMaterialPickerRequired(appconfig.getIssueMaterialPickerRequired()!=null ? appconfig.getIssueMaterialPickerRequired() : null);
			unitApplicationConfigurationDtos.setIsMarkup(appconfig.getIsMarkup()!=null ? appconfig.getIsMarkup() : null);
			unitApplicationConfigurationDtos.setIsPharmacologicalValidationSkipIp(appconfig.getIsPharmacologicalValidationSkipIp());
			unitApplicationConfigurationDtos.setOpStatNotRequired(appconfig.getOpStatNotRequired());
			unitApplicationConfigurationDtos.setIpStatNotRequired(appconfig.getIpStatNotRequired());
			}
			if(registrationFieldConfiguration != null){
				unitApplicationConfigurationDtos.setMobileDigit(registrationFieldConfiguration.getMobileDigit() != null ? registrationFieldConfiguration.getMobileDigit() : 10);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, unitApplicationConfigurationDtos);

		}catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigByOrgUnit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response saveUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {
				
//				iUnitApplicationBillingConfigurationDao.updateUnitApplicationBillingConfigStatus(unitApplicationConfigurationDto);
			Response res = iUnitApplicationBillingConfigurationDao.saveUnitApplicationBillingConfig(unitApplicationConfigurationDto);
				
				if(res.getObject()!=null ) {
					unitApplicationBillingConfigListCache.invalidate(unitApplicationConfigurationDto.getUnitId());

				UnitApplicationBillingConfiguration master  = (UnitApplicationBillingConfiguration) res.getObject();

				Request request = new Request();
				Payload payload = new Payload();
				if(unitApplicationConfigurationDto.getUnitApplicationBillingConfigurationId()!=null) {
					payload.setActionType("update");
				}else {
					payload.setActionType("create");
				}
				payload.setData(master);
				request.setKey(MasterKeyConstants.UnitApplicationBillingConfiguration.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return res;
			}


		} catch (Exception e) {
			logger.error("saveUnitApplicationBillingConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {

			Response updateRes =  iUnitApplicationBillingConfigurationDao.updateUnitApplicationBillingConfig(unitApplicationConfigurationDto);
			if(updateRes.getStatus().equals(SUCCESS)){
				unitApplicationBillingConfigListCache.invalidate(unitApplicationConfigurationDto.getUnitId());
			}
			return updateRes;
		} catch (Exception e) {
			logger.error("updateUnitApplicationBillingConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateUnitApplicationBillingConfigStatus(UnitApplicationConfigurationDto unitApplicationConfigurationDto) throws ApplicationException {
		try {
			Response res = iUnitApplicationBillingConfigurationDao.updateUnitApplicationBillingConfigStatus(unitApplicationConfigurationDto);

			if(res.getStatus().equals(SUCCESS)) {
				unitApplicationBillingConfigListCache.invalidate(unitApplicationConfigurationDto.getUnitId());
			}
			return res;
		} catch (Exception e) {
			logger.error("updateUnitApplicationBillingConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitApplicationBillingConfigList(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {
			List<UnitApplicationConfigurationDto> responseCache = unitApplicationBillingConfigListCache.getIfPresent(unitApplicationConfigurationDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigList(unitApplicationConfigurationDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				unitApplicationBillingConfigListCache.put(unitApplicationConfigurationDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitApplicationBillingConfigCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigCount(organizationId, unitId);

		} catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigCount failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	@Override
	public Response getOPDBillingBedCatagory(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getOPDBillingBedCatagory(organizationId, unitId);

		} catch (Exception e) {
			logger.error("getOPDBillingBedCatagory failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getSelfPayeeList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getSelfPayeeList(organizationId, unitId);

		} catch (Exception e) {
			logger.error("getSelfPayeeList failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response saveUnitApplicationRegistrationFieldConfig(
			RegistrationFieldConfigurationDto registrationFieldConfigurationDto) throws ApplicationException {
		try {
			Response res = iUnitApplicationBillingConfigurationDao.saveUnitApplicationRegistrationFieldConfig(registrationFieldConfigurationDto);
			if(res.getStatus().equals(SUCCESS)){
				unitApplicationRegistrationFieldConfigMasterCache.invalidate(registrationFieldConfigurationDto.getUnitId());
			}
			return res;
		} catch (Exception e) {
			logger.error("saveUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitApplicationRegistrationFieldConfig(Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<RegistrationFieldConfigurationDto> responseCache = unitApplicationRegistrationFieldConfigMasterCache.getIfPresent(unitId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iUnitApplicationBillingConfigurationDao.getUnitApplicationRegistrationFieldConfig(organizationId,unitId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				unitApplicationRegistrationFieldConfigMasterCache.put(unitId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
			logger.error("getUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getApplicationConfigurationByOrgUnit(ApplicationConfigurationDto applicationConfigurationDto)
			throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getApplicationConfigurationByOrgUnit(applicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getApplicationConfigurationByOrgUnit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public String getPublicUrl(Integer orgUnitId) throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getPublicUrl(orgUnitId);

		} catch (Exception e) {
			logger.error("getPublicUrl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSPrnFormat(UnitApplicationConfigurationDto unitApplicationConfigurationDto)
			throws ApplicationException {
		try {
			return iUnitApplicationBillingConfigurationDao.getQMSPrnFormat(unitApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getQMSPrnFormat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveScreenImage(DifferentTypesScreenImageDTO differentTypesScreenImageDTO, MultipartFile inputFile)
			throws Exception {
		
		try {
			return iDifferentTypesLogoImageDao.saveScreenImage(differentTypesScreenImageDTO,inputFile);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response saveScreenApplicationName(DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws Exception {
		try {
			return iDifferentTypesLogoImageDao.saveScreenApplicationName(differentTypesScreenImageDTO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getScreenImageById(Integer organisationId, Integer imageTypeId) throws Exception {

		try {
			return iDifferentTypesLogoImageDao.getScreenImageById(organisationId,imageTypeId);
		} catch (Exception e) {
			throw e;
		}
	}


}
