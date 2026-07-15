package com.param.adt.master.global.service;

import javax.transaction.Transactional;

import com.param.adt.master.global.dto.ICPCCodesConfigurationMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IICPCCodesConfigurationMapperService {

	public Response saveICPCCodesConfigurationMapper(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) throws ApplicationException;

	public Response getICPCCodesConfigurationMapperList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) throws ApplicationException;

	public Response getICPCSystemList(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) throws ApplicationException;

	public Response updateICPCCodesConfigurationStatus(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) throws ApplicationException;

	public Response getSpecialityListByIcpcSystemId(ICPCCodesConfigurationMapperDto iCPCCodesConfigurationMapperDto) throws ApplicationException;


}
