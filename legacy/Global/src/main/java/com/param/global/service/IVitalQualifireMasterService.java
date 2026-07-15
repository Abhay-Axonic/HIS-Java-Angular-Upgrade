package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.VitalQualifireMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface IVitalQualifireMasterService {

	Response saveVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response updateVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response updateVitalQualifireMasterStatus(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response getVitalQualifireMasterById(Integer vitalQualifireId, Integer organizationId) throws ApplicationException;

	Response getVitalQualifireMasterList(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response getVitalQualifireMasterCount(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response getActiveVitalQualifireMaster(Integer organizationId) throws ApplicationException;

	Response getVitalQualifireMasterByName(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException;

	Response getVitalQualifireMasterByNameNotId(VitalQualifireMasterDto vitalQualifireMasterDto)
			throws ApplicationException;

	Response getVitalCategoryList(VitalQualifireMasterDto vitalQualifireMasterDto)throws ApplicationException;

}
