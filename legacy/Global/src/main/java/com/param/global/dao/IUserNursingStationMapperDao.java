package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.UserNursingStationMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IUserNursingStationMapperDao {
	
	public Response saveUserNursingStationMapper(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException;
	
	public Response updateUserNursingStationMapper(List<UserNursingStationMapperDto> listUserNursingStationMapperDto, UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException;
	
	public Response updateUserNursingStationMapperStatus(Integer userId, Integer organisationId, Integer unitId, Character status) throws ApplicationException;
	
	public Response getUserNursingStationMapperlist(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException;
	
	public Response getUserNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveUserNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getUserNursingStationMapperById(Integer userId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response checkUserNursingStationMapperAvailable(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException;
	
	public Response getUserList(Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getDetailedNursingStationList(Integer userId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getNursingStationList(Integer organisationId, Integer unitId) throws ApplicationException;

	public Response getUserListForStore(Integer organisationId, Integer unitId)throws ApplicationException;

	
	
}
