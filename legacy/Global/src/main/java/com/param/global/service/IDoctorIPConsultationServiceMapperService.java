package com.param.global.service;


import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.DoctorIPConsultationServiceMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
@Transactional
public interface IDoctorIPConsultationServiceMapperService {
	public Response saveDoctorIPConsultationServiceMapper(DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException;
	
	public Response updateDoctorIPConsultationServiceMapper(DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException;
		
	public Response getDoctorIPConsultationServiceMapperList(DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException;
	
	public Response getDoctorIPConsultationServiceMapperById(Integer doctorIPConsultationServiceMapperId, Integer organizationId, Integer unitId) throws ApplicationException;

	Response getDoctorIPConsultationByDoctorId(Integer doctorId, Integer organizationId, Integer unitId)
			throws ApplicationException;

	public Response getDoctorIPConsultationServiceMapperListCount(DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto)throws ApplicationException;
	


}
