package com.param.global.billing.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAutoPostingConfigService {

	@Transactional
	Response saveAutoPostingConfig(AutoPostingServicesConfigDto postingServicesConfigDto)throws ApplicationException;

	@Transactional
	Response getAutoPostingConfigList(Integer orgId, Integer unitId)throws ApplicationException;
	
	@Transactional
	Response updateServiceAutoPostingStatus(AutoPostingServicesConfigDto postingServicesConfigDto) throws ApplicationException;

}
