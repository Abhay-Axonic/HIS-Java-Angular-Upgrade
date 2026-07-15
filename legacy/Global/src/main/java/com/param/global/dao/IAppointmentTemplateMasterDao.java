package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAppointmentTemplateMasterDao {

	Response saveAppointmentTemplate(AppointmentTemplateMasterDto dto)throws ApplicationException;

	Response getAppointmentTemplate(AppointmentTemplateMasterDto dto)throws ApplicationException;

	Response updateStatus(AppointmentTemplateMasterDto dto)throws ApplicationException;

}
