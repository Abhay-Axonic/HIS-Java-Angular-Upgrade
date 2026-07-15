package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;
import com.param.global.model.AppointmentTemplateMasterMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAppointmentTemplateMasterMapperDao {

	Response saveAppointmentTemplateMapper(AppointmentTemplateMasterMapperDto mapper)throws ApplicationException;

	Response getAppointmentTemplateMapperData(AppointmentTemplateMasterDto dto)throws ApplicationException;

}
