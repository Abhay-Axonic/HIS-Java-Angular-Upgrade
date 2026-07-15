package com.param.adt.master.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.adt.master.unit.service.INurseAllocationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/api/adt/unit")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NurseAllocationController  implements ICommonConstants{

	@Autowired
	private INurseAllocationService iNurseAllocationService;
	
	@RequestMapping(value = "/saveNurseAllocationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNurseAllocationMaster(@RequestBody NurseAllocationDto nurseAllocationDto) {
		try {
			return iNurseAllocationService.saveNurseAllocationMaster(nurseAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getNurseAllocationMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getNurseAllocationMasterList(@RequestBody NurseAllocationDto nurseAllocationDto) {
		try {
			return iNurseAllocationService.getNurseAllocationMasterList(nurseAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateNurseAllocationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNurseAllocationMaster(@RequestBody NurseAllocationDto nurseAllocationDto) {
		try {
			return iNurseAllocationService.updateNurseAllocationMaster(nurseAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateNurseAllocationMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNurseAllocationMasterStatus(@RequestBody NurseAllocationDto nurseAllocationDto) {
		try {
			return iNurseAllocationService.updateNurseAllocationMasterStatus(nurseAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getNurseAllocationMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNurseAllocationMasterCount(@RequestBody NurseAllocationDto nurseAllocationDto) {
		try {
			return iNurseAllocationService.getNurseAllocationMasterCount(nurseAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getNurseAllocationMasterById/{nurseAllocationId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getNurseAllocationMasterById(@PathVariable ("nurseAllocationId") Integer nurseAllocationId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iNurseAllocationService.getNurseAllocationMasterById(nurseAllocationId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveNurseAllocationMasterByWardId/{wardId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveNurseAllocationMasterByWardId(@PathVariable ("wardId") Integer wardId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iNurseAllocationService.getActiveNurseAllocationMasterByWardId(wardId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveNurseAllocationMasterByNurseId/{nurseId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveNurseAllocationMasterByNurseId(@PathVariable ("nurseId") Integer nurseId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iNurseAllocationService.getActiveNurseAllocationMasterByNurseId(nurseId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
}
