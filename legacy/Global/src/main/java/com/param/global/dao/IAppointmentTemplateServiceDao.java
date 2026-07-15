package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterMapperDto;
import com.param.global.model.AppointmentTemplateServiceDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAppointmentTemplateServiceDao {

	Response saveAppointmentTemplateService(AppointmentTemplateServiceDto serviceDto)throws ApplicationException;

	Response getServiceList(AppointmentTemplateMasterMapperDto appointmentTemplateMasterMapperDto)throws ApplicationException;

}
