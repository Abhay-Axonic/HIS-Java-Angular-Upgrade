package com.param.global.dao;

import java.util.List;
import com.param.global.common.Response;
import com.param.module.service.ServiceItemMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IServiceItemMapperDao {

	public Response saveServiceItemMapper(List<ServiceItemMapperDto> serviceItemMapperList, Integer serviceId) throws ApplicationException;

	public Response  updateServiceItemMapper(List<ServiceItemMapperDto> serviceItemMapperList, Integer serviceMasterId) throws ApplicationException;

	public Response getServiceItemMapperDetails(ServiceItemMapperDto serviceItemMapperDto)throws ApplicationException;

}
