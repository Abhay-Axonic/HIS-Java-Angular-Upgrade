package com.param.global.org.common.dao;

import com.param.global.common.Response;
import com.param.global.org.common.dto.ShiftMasterDto;

import in.param.exception.ApplicationException;


@SuppressWarnings("rawtypes")
public interface IShiftMasterDao {

	Response saveShiftMaster(ShiftMasterDto shiftMasterDto) throws ApplicationException;

	Response getByShiftName(ShiftMasterDto shiftMasterDto) throws ApplicationException;

	Response getShiftMasterList(ShiftMasterDto shiftMasterDto)throws ApplicationException;

	Response updateShiftMaster(ShiftMasterDto shiftMasterDto)throws ApplicationException;

	Response updateShiftMasterStatus(ShiftMasterDto shiftMasterDto)throws ApplicationException;

	Response getShiftMasterCount(ShiftMasterDto shiftMasterDto)throws ApplicationException;

	Response getShiftMasterByNameNotId(ShiftMasterDto shiftMasterDto)throws ApplicationException;

	Response getShiftMasterById(Integer shiftId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response getActiveShiftMasterList(Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getShiftlistByTime(ShiftMasterDto shiftMasterDto) throws ApplicationException;

}
