package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IMpesaAppConfigService {

	Response saveMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto) throws ApplicationException;

	Response updateMpesaAppConfigStatus(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto) throws ApplicationException;

	Response getMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)throws ApplicationException;

	Response getMpesaAppConfigList(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)throws ApplicationException;

}
