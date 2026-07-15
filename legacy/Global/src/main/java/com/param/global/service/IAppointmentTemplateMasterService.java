package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IAppointmentTemplateMasterService {

	Response saveAppointmentTemplate(AppointmentTemplateMasterDto dto)throws ApplicationException;

	Response getAppointmentTemplate(AppointmentTemplateMasterDto dto)throws ApplicationException;

	Response getAppointmentTemplateMapperData(AppointmentTemplateMasterDto dto)throws ApplicationException;

	Response updateStatus(AppointmentTemplateMasterDto dto)throws ApplicationException;

}
