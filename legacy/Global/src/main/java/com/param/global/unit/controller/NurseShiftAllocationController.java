package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.NurseShiftAllocationDetailsDto;
import com.param.global.unit.dto.NurseShiftAllocationDto;
import com.param.global.unit.service.INurseShiftAllocationService;

@RestController
@RequestMapping("api/global/ ")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NurseShiftAllocationController implements ICommonConstants,IError {
	private static final Logger logger = Logger.getLogger(NurseShiftAllocationController.class);

	@Autowired
	private INurseShiftAllocationService iNurseShiftAllocationService;

	@RequestMapping(value = "/saveNurseShiftAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNurseShiftAllocation(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		//System.out.println("Save Data Nurse shift Allocation ");
		try {
			
			 return  iNurseShiftAllocationService.saveNurseShiftAllocation(nurseShiftAllocationDto);

		} catch (Exception e) {
			logger.error("saveNurseShiftAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	/*@RequestMapping(value = "/updateNurseShiftAllocationDashboardStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {

		
		
		Response<NurseShiftAllocationDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, NurseShiftAllocationDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/
	

	@RequestMapping(value = "/updateNurseShiftAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNurseShiftAllocation(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationService.updateNurseShiftAllocation(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("updateNurseShiftAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateNurseShiftAllocationDashboardStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNurseShiftAllocationDashboardStatus(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationService.updateNurseShiftAllocationDashboardStatus(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("updateNurseShiftAllocationDashboardStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/activeInactiveNurseAlloction/{orgId}/{unitId}/{nurseShiftAllocationDetailsId}/{shiftStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveNurseShiftAllocation(
			@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "nurseShiftAllocationDetailsId") Integer nurseShiftAllocationDetailsId,
			@PathVariable(value = "shiftStatus") Character shiftStatus) 
	       {

		Response<NurseShiftAllocationDetailsDto, Integer> response = null;
		try {
			response = iNurseShiftAllocationService.activeInactiveNurseShiftAllocation(orgId,unitId, nurseShiftAllocationDetailsId,
					shiftStatus);
		} catch (Exception e) {
			logger.error("activeInactiveNurseShiftAllocation failed", e);
		}
		return response;
	}
	
	@RequestMapping(value = "/getNurseShiftDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseShiftDetailsById(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationService.getNurseShiftDetailsById(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getNurseShiftDetailsById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getActiveNurseAllocationList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveNurseAllocationList(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto){
		try{
			
			return iNurseShiftAllocationService.getActiveNurseAllocationList(nurseShiftAllocationDto);
		}catch(Exception e){
			logger.error("getActiveNurseAllocationList failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	

	@RequestMapping(value = "/listNurseAllocation", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response listNurseAllocation(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		Response<NurseShiftAllocationDto, Integer> response = null;
		try {
			response = iNurseShiftAllocationService.listNurseAllocation(nurseShiftAllocationDto);
			return response;
		} catch (Exception e) {
			logger.error("listNurseAllocation failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getNurseShiftByDetailsId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseShiftByDetailsId(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		try {
			return iNurseShiftAllocationService.getNurseShiftByDetailsId(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getNurseShiftByDetailsId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getNurseShiftWithBedAllocated", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseShiftWithBedAllocated(@RequestBody NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto) {
		try {
			return iNurseShiftAllocationService.getNurseShiftWithBedAllocated(nurseShiftAllocationDetailsDto);
		} catch (Exception e) {
			logger.error("getNurseShiftWithBedAllocated failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	/*-------check count----*/
	@RequestMapping(value="/getTotalRecordNurseShiftAllocation/{orgId}/{unitId}", method=RequestMethod.GET)
	public Response getTotalRecordNurseShiftAllocation(@PathVariable (value = "orgId") Integer orgId,
			@PathVariable (value="unitid") Integer unitId){
		try {
			return iNurseShiftAllocationService.getTotalRecordNurseShiftAllocation(orgId,unitId);
		} catch (Exception e) {
			logger.error("getTotalRecordNurseShiftAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
       /*-------check duplicate bedId----*/
	@RequestMapping(value="/checkbedIdbyshiftdate", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkbedIdbyshiftdate(@RequestBody NurseShiftAllocationDetailsDto nurseShiftAllocationDetailsDto){
		try {
			return iNurseShiftAllocationService.checkbedIdbyshiftdate(nurseShiftAllocationDetailsDto);
		} catch (Exception e) {
			logger.error("checkbedIdbyshiftdate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getbedlistbywardId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getbedlistbywardId(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		
		try {
			return iNurseShiftAllocationService.getbedlistbywardId(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getbedlistbywardId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getbedlistbyNurseId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getbedlistbyNurseId(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDto) {
		
		try {
			return iNurseShiftAllocationService.getbedlistbyNurseId(nurseShiftAllocationDto);
		} catch (Exception e) {
			logger.error("getbedlistbyNurseId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getAssignedBedList", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAssignedBedList(@RequestBody NurseShiftAllocationDto nurseShiftAllocationDetailsDto){
		try {
			return iNurseShiftAllocationService.getAssignedBedList(nurseShiftAllocationDetailsDto);
		} catch (Exception e) {
			logger.error("getAssignedBedList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
