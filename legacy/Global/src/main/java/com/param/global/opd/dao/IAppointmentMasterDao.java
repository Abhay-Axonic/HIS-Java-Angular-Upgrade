package com.param.global.opd.dao;

import com.param.global.common.Response;
import com.param.opd.appointment.dto.AppointmentMasterDto;
import com.param.opd.appointment.model.AppointmentMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAppointmentMasterDao extends IGenericDao<AppointmentMaster, Integer>{
	public Response getDoctorAppointmentToSync(int appointmentId)throws ApplicationException;
	public Response getBasicAppointmentDetaistoSync(int appointmentId)throws ApplicationException;
	public Response getAppointmentServiceDtlsToSync(int appointmentId)throws ApplicationException;
	public Response getAppointmentVitalsToSync(int appointmentId)throws ApplicationException;
	public Response getMedicationDetailsToSync(int appointmentId)throws ApplicationException;
	public Response getDiagnosisDtlsToSync(int appointmentId)throws ApplicationException;
	public Response geAppointmentStatus(int appointmentId);
	public Response getIPPatientDetailsToSync(Integer recordId) throws ApplicationException;
	public Response getAppointmentDetails(int appointmentId);
	public Response syncPatientPathalogyReport() throws ApplicationException;
	public Response syncPatientRadiologyReport(Integer id)throws ApplicationException;
}