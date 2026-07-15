package com.param.adt.master.unit.service;

import javax.transaction.Transactional;

import com.param.adt.master.unit.dto.NursingStationWardMapperDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface INursingStationWardMapperService {

	
	Response saveNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response getNursingStationWardMapperList(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response updateNursingStationWardMapperStatus(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response updateNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response getNursingStationWardMapperById(Integer id, Integer orgId, Integer unitId) throws ApplicationException;

	Response getNursingStationWardMapperCount(NursingStationWardMapperDto nursingStationWardMapperDto) throws ApplicationException;

	Response getWardListByUserId(Integer userId, Integer orgId, Integer unitId)throws ApplicationException;

	Response getWardListByNursingStationId(Integer nursingStationId, Integer orgId, Integer unitId)throws ApplicationException;

	Response getNursingStationListByWardId(Integer wardId, Integer orgId, Integer unitId)throws ApplicationException;

}
