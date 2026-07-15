package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.EquipmentSchedulingDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IEquipmentSchedulingDao {

	Response saveEquipmentScheduling(List<EquipmentSchedulingDto> list) throws ApplicationException;

	Response checkOtScheduling(EquipmentSchedulingDto dto)throws ApplicationException;

	Response getEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response deActivateEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response checkEquipmentavailableOrNot(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

}
