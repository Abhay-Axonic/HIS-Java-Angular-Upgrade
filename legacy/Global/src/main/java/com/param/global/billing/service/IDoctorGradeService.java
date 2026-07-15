package com.param.global.billing.service;

import javax.transaction.Transactional;

import com.param.global.billing.dto.DoctorGradeDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface IDoctorGradeService {
	
	public Response saveDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response updateDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response updateDoctorGradeStatus(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response getDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response getDoctorGradeById(Integer doctorGradeId, Integer orgId, Integer orgUnitId) throws ApplicationException;
	
	public Response getDoctorGradeCount(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response getActiveDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response getDoctorGradeByName(DoctorGradeDto doctorGradeDto) throws ApplicationException;
	
	public Response getDoctorGradeByNameNotId(DoctorGradeDto doctorGradeDto) throws ApplicationException;
}
