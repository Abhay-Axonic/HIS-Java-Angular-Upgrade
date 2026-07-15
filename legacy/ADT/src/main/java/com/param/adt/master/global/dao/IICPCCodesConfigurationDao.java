package com.param.adt.master.global.dao;

import com.param.adt.master.global.dto.ICPCCodesConfigurationDto;
import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IICPCCodesConfigurationDao {


	public Response saveICPCCodesConfiguration(ICPCCodesConfigurationDto iCPCCodesConfigurationDto) throws ApplicationException;

	public Response updateICPCCodesConfigurationStatus(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto)throws ApplicationException;
}
