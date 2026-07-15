package com.param.opd.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.SlotBlockUnblockRequestDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.unit.model.SlotMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISlotMasterDao extends IGenericDao<SlotMaster, Integer>{


	Response generateSlots(List<SlotMasterDto> listForSaveCall, Integer slotType)throws ApplicationException;
	
	Response generateSlotsDoctorSheduling(List<SlotMasterDto> listForSaveCall, Integer slotType)throws ApplicationException;

	Response getSlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	Response getSlotsForBlockUnblock(SlotBlockUnblockRequestDto slotReqDto)throws ApplicationException;

	Response getSlotCount(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getSlotDetails(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response updateSlotStatus(SlotMasterDto slotMasterDto)throws ApplicationException;

	List<SlotMasterDto> getAppointmentsForMonth(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getYearlySlots(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getDailySlots(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getWeeklySlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	Response blockUnblockSlots(BlockSlotReqDto blockSlotReqDto) throws ApplicationException;

	Response getBlockedUnblockedSlotDetails(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response updateStatusOfSlot(SlotMasterDto slotMasterDto)throws ApplicationException;

	boolean isOverBookingAllowedOrNot(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response deactiveSlotsDateAndDoctorWise(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response isSlotAlreadyExist(SlotMasterDto slotMasterDtoNew)throws ApplicationException;

	Response isEHCSlotAvailable(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response blockSlot(Integer slotId)throws ApplicationException;

	Response checkSlotAlreadyBookedOrNot(BlockSlotReqDto blockSlotReqDto)throws ApplicationException;

	Response getAsset(Integer orgId)throws ApplicationException;

	Response getHemoSlots(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response getSlotHemoCount(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response deactiveSlotsWithInDateRange(SlotMasterDto slotMasterDto);

	Response getOldSlotDetails(SlotMasterDto slotMasterDtosObj, List<DoctorMasterDto> doctorArrayList, Integer unitId, Integer slotTypeId, char override);
	
	Response getOldSlotDetailsDoctor(SlotMasterDto slotMasterDtosObj, List<DoctorMasterDto> doctorArrayList, Integer unitId, Integer slotTypeId, char override);

	Response deleteUnusedSlots(List<Integer> unitIds);

	Response getSlotDetailsBySlotId(SlotMasterDto slotMasterDto);

	List<SlotMasterDto> getSlotsNew(SlotMasterDto slotMasterDto)throws ApplicationException;

	Response copyBackdatedSlots(SlotMasterDto dto)throws ApplicationException;

	Response saveBackDatedSlotsToSlotLogs(List<SlotMasterDto> dto)throws ApplicationException;

	Response deleteBackdatedSlotsFromSlotMaster(List<Integer> slotIdList)throws ApplicationException;

	Response getFlagCheckMyLife(SlotMasterDto slotMasterDto)throws ApplicationException;

	List<SlotMasterDto> getLastAppointmentDate(List<SlotMasterDto> slotMasterDtoList, Integer orgnizationId)throws ApplicationException;
}
