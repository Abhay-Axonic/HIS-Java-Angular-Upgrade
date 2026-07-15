package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.RoomModalityMasterDto;
import com.param.global.service.IRoomModalityMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/global")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RoomModalityMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(RoomModalityMasterController.class);

	
	@Autowired
	private IRoomModalityMasterService iRoomModalityMasterService;

	
	@RequestMapping(value = "/ saveRoomModalityMasterMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveRoomModalityMasterMaster(@RequestBody RoomModalityMasterDto dto) {
		try {
			return iRoomModalityMasterService.saveRoomModalityMasterMaster(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getRoomModalityById/{roomModalityId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getRoomModalityById(@PathVariable("roomModalityId") int roomModalityId,
			@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId) {
		try {
			return iRoomModalityMasterService.getRoomModalityById(roomModalityId, orgId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getRoomModalityList", method = RequestMethod.POST)
	public @ResponseBody Response getRoomModalityList(@RequestBody RoomModalityMasterDto dto) {
		try {
			return iRoomModalityMasterService.getRoomModalityList(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/updateRoomModalityMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRoomModalityMaster(@RequestBody RoomModalityMasterDto dto) {
		try {
			return iRoomModalityMasterService.updateRoomModalityMaster(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateRoomModalityMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRoomModalityMasterStatus(@RequestBody RoomModalityMasterDto dto) {
		try {
			return iRoomModalityMasterService.updateRoomModalityMasterStatus(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateRoomModalityMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRoomModalityMasterCount(@RequestBody RoomModalityMasterDto dto) {
		try {
			return iRoomModalityMasterService.updateRoomModalityMasterCount(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
