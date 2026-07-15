package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.RoomModalityMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IRoomModalityMasterDao {

	
	Response saveRoomModalityMasterMaster(RoomModalityMasterDto dto) throws ApplicationException;

	Response getRoomModalityById(int roomModalityId, int orgId, int unitId)throws ApplicationException;

	Response getRoomModalityList(RoomModalityMasterDto dto)throws ApplicationException;

	Response updateRoomModalityMaster(RoomModalityMasterDto dto)throws ApplicationException;

	Response updateRoomModalityMasterStatus(RoomModalityMasterDto dto)throws ApplicationException;

	Response updateRoomModalityMasterCount(RoomModalityMasterDto dto)throws ApplicationException;

	Response checkAlredyExistOrNot(RoomModalityMasterDto dto)throws ApplicationException;

	Response checkNotById(RoomModalityMasterDto dto)throws ApplicationException;
	
	Response getModalityDetailsByModalityId(RoomModalityMasterDto dto)throws ApplicationException;

}
