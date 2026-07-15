package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })
public interface IMpesaAppConfigDao {

	Response saveMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto) throws ApplicationException;

	Response updateMpesaAppConfigStatus(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)  throws ApplicationException;

	Response getMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto) throws ApplicationException;

	Response getMpesaAppConfigList(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)throws ApplicationException;
}
