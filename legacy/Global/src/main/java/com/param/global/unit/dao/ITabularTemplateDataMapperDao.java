package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ITabularTemplateDataMapperDao {

	public Response saveTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)throws ApplicationException;

	public Response updateTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)throws ApplicationException;

	public Response getTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId, Integer unitId)throws ApplicationException;


}
