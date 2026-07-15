package com.param.global.opd.service;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAppointmentMasterService {
	public Response getAppointmentDetailsToSyncById(int appointmentId)throws ApplicationException;

	public Response syncPatientPathalogyReport();

	public Response syncPatientRadiologyReport(Integer id);

}
