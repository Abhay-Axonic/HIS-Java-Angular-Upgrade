package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.DepartmentNursingStationMapperDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDepartmentNursingStationMapperService {
	
	public Response saveDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response updateDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response updateDepartmentNursingStationMapperStatus(Integer specialityId, Integer organisationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperlist(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveDepartmentNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperById(Integer specialityId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response checkDepartmentNursingStationMapperAvailable(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response getDetailedListNursingStation(Integer nursingStationId, Integer organisationId, Integer unitId) throws ApplicationException;

	public Response getDeptListByNursingStationId(Integer nursingStationId, Integer organisationId, Integer unitId)throws ApplicationException;

	public Response getWardListForER(Integer organisationId, Integer unitId) throws ApplicationException;

}
