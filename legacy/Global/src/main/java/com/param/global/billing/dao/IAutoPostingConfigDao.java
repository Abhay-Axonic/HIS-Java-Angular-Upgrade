package com.param.global.billing.dao;

import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAutoPostingConfigDao {

	Response saveAutoPostingConfig(AutoPostingServicesConfigDto postingServicesConfigDto)throws ApplicationException;

	Response getAutoPostingConfigList(Integer orgId, Integer unitId)throws ApplicationException;

	Response checkExsistance(AutoPostingServicesConfigDto postingServicesConfigDto)throws ApplicationException;

	Response updateServiceAutoPostingStatus(AutoPostingServicesConfigDto postingServicesConfigDto) throws ApplicationException;

	Response getAutoPostingConfigList(Integer organizationId, Integer unitId, Integer postingId) throws ApplicationException;
	
	Response getAutoPostingConfigListForRoomRent()throws ApplicationException;

}
