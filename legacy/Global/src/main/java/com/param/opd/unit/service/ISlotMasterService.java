package com.param.opd.unit.service;

import in.param.exception.ApplicationException;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.SlotBlockUnblockRequestDto;
import com.param.global.dto.SlotMasterDto;
@SuppressWarnings("rawtypes")
public interface ISlotMasterService {

	@Transactional
	Response generateSlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response generateSlotsDoctorSheduling(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response getSlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response getSlotsForBlockUnblock(SlotBlockUnblockRequestDto slotReqDto)throws ApplicationException;

	@Transactional
	Response getSlotCount(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response getSlotDetails(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response generateSlotsForECH(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response updateSlotStatus(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response getMonthlySlots(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response getYearlySlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response getDailySlots(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response getWeeklySlots(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response blockUnblockDoctorSlots(BlockSlotReqDto blockSlotReqDto)throws ApplicationException;

	@Transactional
	Response getBlockedUnblockedSlotDetails(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response updateStatusOfSlot(SlotMasterDto slotMasterDto) throws ApplicationException;

	@Transactional
	Response isOverBookingAllowedOrNot(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response deactiveSlotsDateAndDoctorWise(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	@Transactional
	Response isEHCSlotAvailable(SlotMasterDto slotMasterDto)throws ApplicationException;

	@Transactional
	Response blockSlot(Integer slotId)throws ApplicationException;
	
	@Transactional
	Response getAsset(Integer orgId)throws ApplicationException;

    @Transactional
	Response generateSlotsForHemodialysis(SlotMasterDto slotMasterDto)throws ApplicationException;
    
    @Transactional
	Response getHemoSlots(SlotMasterDto slotMasterDto)throws ApplicationException;
    
    @Transactional
	Response getSlotHemoCount(SlotMasterDto slotMasterDto)throws ApplicationException;
    
    @Transactional
	Response deactiveSlotsWithInDateRange(SlotMasterDto slotMasterDto);

    @Transactional
    Response deleteUnusedSlots(List<Integer> unitIds);

    @Transactional
	Response getSlotDetailsBySlotId(SlotMasterDto slotMasterDto)throws ApplicationException;

    @Transactional
	Response getSlotsNew(SlotMasterDto slotMasterDto)throws ApplicationException;

    @Transactional
	Response copyBackdatedSlots(SlotMasterDto dto)throws ApplicationException;

	 @Transactional
	Response saveBackDatedSlotsToSlotLogs(List<SlotMasterDto> list)throws ApplicationException;

	 @Transactional
	Response deleteBackdatedSlotsFromSlotMaster(List<SlotMasterDto> list)throws ApplicationException;


}
