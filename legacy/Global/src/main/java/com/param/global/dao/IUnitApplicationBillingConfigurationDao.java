package com.param.global.dao;



import com.param.global.common.Response;
import com.param.global.dto.ApplicationConfigurationDto;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IUnitApplicationBillingConfigurationDao {

	Response getUnitApplicationBillingConfigByOrgUnit(UnitApplicationConfigurationDto unitApplicationConfigurationDto);
	
	Response saveUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response updateUnitApplicationBillingConfig(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
		
	Response updateUnitApplicationBillingConfigStatus(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response getUnitApplicationBillingConfigList(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;
	
	Response getUnitApplicationBillingConfigCount(Integer organizationId, Integer unitId)throws ApplicationException;

	Response getOPDBillingBedCatagory(Integer organizationId, Integer unitId)throws ApplicationException;

	Response getSelfPayeeList(Integer organizationId, Integer unitId)throws ApplicationException;

	String getURLForPdfGeneration(Integer organizationId,Integer unitId) throws ApplicationException;

	Response saveUnitApplicationRegistrationFieldConfig(
			RegistrationFieldConfigurationDto registrationFieldConfigurationDto)throws ApplicationException;

	Response getUnitApplicationRegistrationFieldConfig(Integer organizationId, Integer unitId)throws ApplicationException;

	Response saveDefaultEntry(Integer orgId, Integer unitId, Integer selfPayeeId, Integer roleId, Integer userId, Integer docRoleId, Integer nurseRoleId) throws ApplicationException;

	Integer getDefaultDiscountOwnership(Integer unitId) throws ApplicationException;

	String getURLForPdfGenerationForBI(Integer organizationId, Integer unitId) throws ApplicationException;

	Response getApplicationConfigurationByOrgUnit(ApplicationConfigurationDto applicationConfigurationDto) throws ApplicationException;

	String getPublicUrl(Integer orgUnitId) throws ApplicationException;

	String getURLForPdfGenerationMis(Integer organizationId, Integer unitId) throws ApplicationException;

	Response getQMSPrnFormat(UnitApplicationConfigurationDto unitApplicationConfigurationDto)throws ApplicationException;

}
