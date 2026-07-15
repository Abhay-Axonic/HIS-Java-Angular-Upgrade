package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.DepartmentNursingStationMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IDepartmentNursingStationMapperDao {
	
	public Response saveDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response updateDepartmentNursingStationMapper(List<DepartmentNursingStationMapperDto> listDepartmentNursingStationMapperDto) throws ApplicationException;
	
	public Response updateDepartmentNursingStationMapperStatus(Integer specialityId, Integer organisationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperlist(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveDepartmentNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getDepartmentNursingStationMapperById(Integer specialityId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response checkDepartmentNursingStationMapperAvailable(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException;

	public Response getDetailedListNursingStation(Integer specialityId, Integer organisationId, Integer unitId) throws ApplicationException;

	public Response getDeptListByNursingStationId(Integer nursingStationId, Integer organisationId, Integer unitId)throws ApplicationException;

	public Response getWardListForER(Integer organisationId, Integer unitId) throws ApplicationException;


	
	
}
