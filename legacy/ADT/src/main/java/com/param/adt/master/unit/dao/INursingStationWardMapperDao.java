package com.param.adt.master.unit.dao;

import java.util.List;

import com.param.adt.master.unit.dto.NursingStationWardMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INursingStationWardMapperDao {
	
	Response saveNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;
	
	Response getNursingStationWardMapperList(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response updateNursingStationWardMapperStatus(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response updateNursingStationWardMapper(List<NursingStationWardMapperDto> listUserNursingStationMapperDto, NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response getNursingStationWardMapperById(Integer id, Integer orgId, Integer unitId) throws ApplicationException;

	Response getNursingStationWardMapperCount(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response getWardListByUserId(Integer userId,Integer orgId, Integer unitId)throws ApplicationException;

	Response checkIsNursingStationAlreadyAssigned(NursingStationWardMapperDto nursingStationWardMapperDto)throws ApplicationException;

	Response getWardListByNursingStationId(Integer nursingStationId, Integer orgId, Integer unitId)throws ApplicationException; 
	
	Response checkIsNursingStationAlreadyAssignedOrNot(NursingStationWardMapperDto nursingStationWardMapperDto)throws ApplicationException;

	Response getNursingStationListByWardId(Integer wardId, Integer orgId, Integer unitId)throws ApplicationException;
}
