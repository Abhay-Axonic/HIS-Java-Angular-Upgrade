package com.param.otims.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.EquipmentSchedulingDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IEquipmentSchedulingService {

	Response saveEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto) throws ApplicationException;

	Response getEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto) throws ApplicationException;

	Response deActivateEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response checkEquipmentavailableOrNot(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response checkEquipmentListAvailableOrNot(List<EquipmentSchedulingDto> equipmentSchedulingDto)throws ApplicationException;

}
