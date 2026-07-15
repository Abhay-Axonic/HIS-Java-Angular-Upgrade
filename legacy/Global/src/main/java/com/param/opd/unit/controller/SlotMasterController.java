package com.param.opd.unit.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.SlotBlockUnblockRequestDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.unit.service.ISlotMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("api/opd")
@SuppressWarnings({"rawtypes","unchecked"})
public class SlotMasterController implements ICommonConstants
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SlotMasterController.class);

	@Autowired
	ISlotMasterService iSlotMasterService;

	@Autowired
	SessionService sessionService;

	@RequestMapping(value="/generateSlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateSlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			if(slotMasterDto.getSlotTypeId()!=3)
				return iSlotMasterService.generateSlots(slotMasterDto);
			else
				return iSlotMasterService.generateSlotsForECH(slotMasterDto);
		}catch (Exception e) {
			logger.error("generateSlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/generateSlotsDoctorSheduling", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateSlotsDoctorSheduling(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iSlotMasterService.generateSlotsDoctorSheduling(slotMasterDto);
		}catch (Exception e) {
			logger.error("generateSlotsDoctorSheduling failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/generateSlotsForHemodialysis", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateSlotsForHemodialysis(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.generateSlotsForHemodialysis(slotMasterDto);
		}catch (Exception e) {
			logger.error("generateSlotsForHemodialysis failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlots(slotMasterDto);
		}catch (Exception e) {
			logger.error("getSlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getHemoSlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getHemoSlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getHemoSlots(slotMasterDto);
		}catch (Exception e) {
			logger.error("getHemoSlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getSlots/blockUnblock", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotsForBlockUnblock(@RequestBody SlotBlockUnblockRequestDto slotReqDto) {
		try {
			return iSlotMasterService.getSlotsForBlockUnblock(slotReqDto);
		}catch (Exception e) {
			logger.error("getSlotsForBlockUnblock failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSlotDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotDetails(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlotDetails(slotMasterDto);
		}catch (Exception e) {
			logger.error("getSlotDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSlotCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotCount(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlotCount(slotMasterDto);
		}catch (Exception e) {
			logger.error("getSlotCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getSlotHemoCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotHemoCount(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlotHemoCount(slotMasterDto);
		}catch (Exception e) {
			logger.error("getSlotHemoCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateSlotStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSlotStatus(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.updateSlotStatus(slotMasterDto);
		}catch (Exception e) {
			logger.error("updateSlotStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/deactiveSlotsDateAndDoctorWise", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deactiveSlotsDateAndDoctorWise(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.deactiveSlotsDateAndDoctorWise(slotMasterDto);
		}catch (Exception e) {
			logger.error("deactiveSlotsDateAndDoctorWise failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/deactiveSlotsWithInDateRange", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deactiveSlotsWithInDateRange(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.deactiveSlotsWithInDateRange(slotMasterDto);
		}catch (Exception e) {
			logger.error("deactiveSlotsWithInDateRange failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getYearlySlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getYearlySlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iSlotMasterService.getYearlySlots(slotMasterDto);

		}catch (Exception e) {
			logger.error("getYearlySlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getDailySlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDailySlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
				return iSlotMasterService.getDailySlots(slotMasterDto);

		}catch (Exception e) {
			logger.error("getDailySlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getMonthlySlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMonthlySlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getMonthlySlots(slotMasterDto);
		}catch (Exception e) {
			logger.error("getMonthlySlots failed", e);
		}
		return null;
	}

	@RequestMapping(value="/getWeeklySlots", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getWeeklySlots(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getWeeklySlots(slotMasterDto);
		}catch (Exception e) {
			logger.error("getWeeklySlots failed", e);
		}
		return null;
	}

	@RequestMapping(value="/blockUnblockslot", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response blockUnblockSlots(@RequestHeader Integer organisationId, @RequestHeader Integer unitId, @RequestBody BlockSlotReqDto blockSlotReqDto){
		try{
			blockSlotReqDto.setOrganisationId(organisationId);
			blockSlotReqDto.setUnitId(unitId);
			return iSlotMasterService.blockUnblockDoctorSlots(blockSlotReqDto);
		}catch(Exception e){
			logger.error("blockUnblockSlots failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getBlockedUnblockedSlotDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBlockedUnblockedSlotDetails(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getBlockedUnblockedSlotDetails(slotMasterDto);
		}catch (Exception e) {
			logger.error("getBlockedUnblockedSlotDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/isOverBookingAllowedOrNot", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response isOverBookingAllowedOrNot(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.isOverBookingAllowedOrNot(slotMasterDto);
		}catch (Exception e) {
			logger.error("isOverBookingAllowedOrNot failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/isEHCSlotAvailable", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response isEHCSlotAvailable(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.isEHCSlotAvailable(slotMasterDto);
		}catch (Exception e) {
			logger.error("isEHCSlotAvailable failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/block/{slotId}", method=RequestMethod.GET)
	public @ResponseBody Response blockSlot(@PathVariable("slotId") Integer slotId) {
		try {
				return iSlotMasterService.blockSlot(slotId);
		}catch (Exception e) {
			logger.error("blockSlot failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getAsset", method=RequestMethod.GET)
	public @ResponseBody Response getAsset(HttpServletRequest request) {
		try {
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}

			return iSlotMasterService.getAsset(sessionService.getOrganizationId());
		}catch (Exception e) {
			logger.error("getAsset failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSlotDetailsBySlotId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotDetailsBySlotId(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlotDetailsBySlotId(slotMasterDto);
		}catch (Exception e) {
			logger.error("getSlotDetailsBySlotId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSlotsNew", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSlotsNew(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterService.getSlotsNew(slotMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}

}
