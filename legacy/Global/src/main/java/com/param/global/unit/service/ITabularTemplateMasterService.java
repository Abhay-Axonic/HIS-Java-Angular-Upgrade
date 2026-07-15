package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.TabularTemplateMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ITabularTemplateMasterService {

	public Response saveTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto) throws ApplicationException;

	public Response updateTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto)throws ApplicationException;

	public Response updateTabularTemplateMasterStatus(TabularTemplateMasterDto tabularTemplateMasterDto)throws ApplicationException;

	public Response getTabularTemplateMasterById(Integer tabularTemplateId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getTabularTemplateMasterList(TabularTemplateMasterDto tabularTemplateMasterDto)throws ApplicationException;

	public Response getTabularTemplateMasterCount(TabularTemplateMasterDto tabularTemplateMasterDto)throws ApplicationException;

	public Response getActiveTabularTemplateMaster(Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getTabularTemplateMasterByName(TabularTemplateMasterDto tabularTemplateMasterDto) throws ApplicationException;

	public Response getTabularTemplateMasterByNameNotId(TabularTemplateMasterDto tabularTemplateMasterDto) throws ApplicationException;

}
