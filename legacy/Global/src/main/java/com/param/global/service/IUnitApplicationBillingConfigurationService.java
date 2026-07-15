package com.param.global.service;

import javax.transaction.Transactional;

import com.param.entity.model.master.DifferentTypesScreenImageDTO;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

import in.param.exception.ApplicationException;
import scala.collection.generic.BitOperations.Int;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IUnitApplicationBillingConfigurationService {

	Response getUnitApplicationBillingConfigByOrgUnit(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;

	Response saveUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;

	Response updateUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response updateUnitApplicationBillingConfigStatus(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response getUnitApplicationBillingConfigList(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response getUnitApplicationBillingConfigCount(Integer organizationId, Integer unitId)throws ApplicationException;
	
	Response getOPDBillingBedCatagory(Integer organizationId, Integer unitId)throws ApplicationException;

	Response getSelfPayeeList(Integer organizationId, Integer unitId)throws ApplicationException;

	Response saveUnitApplicationRegistrationFieldConfig(
			RegistrationFieldConfigurationDto registrationFieldConfigurationDto)throws ApplicationException;

	Response getUnitApplicationRegistrationFieldConfig(Integer organizationId, Integer unitId)throws ApplicationException;

	Response getApplicationConfigurationByOrgUnit(ApplicationConfigurationDto applicationConfigurationDto) throws ApplicationException;

	String getPublicUrl(Integer orgUnitId) throws ApplicationException;

	Response getQMSPrnFormat(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	public Response saveScreenImage(DifferentTypesScreenImageDTO differentTypesScreenImageDTO, MultipartFile inputFile) throws Exception;

	public Response saveScreenApplicationName(DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws Exception;

	public Response getScreenImageById(Integer organisationId, Integer imageTypeId) throws Exception;

}
