package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;

import in.param.exception.ApplicationException;

public interface IVitalAppointmentQualifierMapperDao {

	public Response saveVitalAppointmentQualifierMapper(AppointmentVitalMapperDto vitalDto)throws ApplicationException;

}
