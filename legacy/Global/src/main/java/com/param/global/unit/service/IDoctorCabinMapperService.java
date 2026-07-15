package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.DoctorCabinMapperDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDoctorCabinMapperService {
	
	public Response saveDoctorCabinMapper(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException;
	
	public Response updateDoctorCabinMapper(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException;
	
	public Response updateDoctorCabinMapperStatus(Integer doctorId, Integer organisationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getDoctorCabinMapperlist(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException;
	
	public Response getDoctorCabinMapperCount(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveDoctorCabinMapper(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getDoctorCabinMapperById(Integer doctorId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response checkDoctorCabinMapperAvailable(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException;
	
	public Response getDoctorList(Integer organisationId, Integer unitId) throws ApplicationException;

	public Response getDetailedCabinList(Integer doctorId, Integer organisationId, Integer unitId) throws ApplicationException;

}
