package com.param.adt.master.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.master.unit.dto.RmoAllocationDto;
import com.param.adt.master.unit.service.IRMOAllocationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/api/unit/clinical")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RMOAllocationController  implements ICommonConstants{

	@Autowired
	private IRMOAllocationService iRMOAllocationService;
	
	
	
	@RequestMapping(value = "/saveRmoAllocationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveRmoAllocationMaster(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.saveRmoAllocationMaster(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getRmoAllocationMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getRmoAllocationMasterList(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.getRmoAllocationMasterList(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateRmoAllocationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRmoAllocationMaster(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.updateRmoAllocationMaster(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateRmoAllocationMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRmoAllocationMasterStatus(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.updateRmoAllocationMasterStatus(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getRmoAllocationMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRmoAllocationMasterCount(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.getRmoAllocationMasterCount(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getRmoAllocationMasterById/{rmoAllocationId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getRmoAllocationMasterById(@PathVariable ("rmoAllocationId") Integer rmoAllocationId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iRMOAllocationService.getRmoAllocationMasterById(rmoAllocationId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveRmoAllocationMasterByWardId/{wardId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveRmoAllocationMasterByWardId(@PathVariable ("wardId") Integer wardId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iRMOAllocationService.getActiveRmoAllocationMasterByWardId(wardId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveRmoAllocationMasterByNurseId/{doctorId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveRmoAllocationMasterByDoctorId(@PathVariable ("doctorId") Integer doctorId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iRMOAllocationService.getActiveRmoAllocationMasterByDoctorId(doctorId,organizationId,unitId);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
   @RequestMapping(value = "/getDoctorListByRmoallocationId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorListByRmoallocationId(@RequestBody RmoAllocationDto rmoAllocationDto) {
		try {
			return iRMOAllocationService.getDoctorListByRmoallocationId(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
   
   @RequestMapping(value = "/getActiveRmoAllocationMasterByRMODoctorId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveRmoAllocationMasterByRMODoctorId(@RequestBody RmoAllocationDto rmoAllocationDto){
		try {
			return iRMOAllocationService.getActiveRmoAllocationMasterByRMODoctorId(rmoAllocationDto);
		}catch(Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
   
   @RequestMapping(value = "/getListOfDoctorMappedToRmo",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  	public Response getListOfDoctorMappedToRmo(@RequestBody RmoAllocationDto rmoAllocationDto){
  		try {
  			return iRMOAllocationService.getListOfDoctorMappedToRmo(rmoAllocationDto);
  		}catch(Exception e) {
  			
  		}
  		return new Response(ERROR, null, null, null, null);
  	}
}
