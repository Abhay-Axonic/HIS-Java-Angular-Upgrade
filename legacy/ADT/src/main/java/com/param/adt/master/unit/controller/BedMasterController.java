package com.param.adt.master.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.service.IBedMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Controller
@RequestMapping("api/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedMasterController implements ICommonConstants {
	
	@Autowired
	IBedMasterService iBedMasterService;
	
	@RequestMapping(value="/addBed" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addBed(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.addBed(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bedList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedMasterList(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getBedMasterList(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateBed" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBedMaster(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.updateBedMaster(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bed/{bedId}" , method = RequestMethod.GET)
	public @ResponseBody Response getbedById(@PathVariable Integer bedId){
		try{
			return iBedMasterService.getbedById(bedId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.updateStatus(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/bedListCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response bedListCount(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.bedListCount(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/bedCategoryAvailabilityCount/orgId/{orgId}/unitId/{unitId}" , method = RequestMethod.GET)
	public @ResponseBody Response getBedCategoryAvailabilityCount(@PathVariable("orgId") Integer organizationId,
																  @PathVariable("unitId") Integer unitId){
		try{
			return iBedMasterService.getBedCategoryAvailabilityCount(organizationId,unitId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bed/management" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataForBedManagementData(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getDataForBedManagementData(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bed/management/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataForBedManagementDataSearch(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getDataForBedManagementDataSearch(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bedMovingBedList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedMovingBedList(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getBedMovingBedList(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/movingBedListCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response movingBedListCount(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.movingBedListCount(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/activeMovingBedList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response activeMovingBedList(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.activeMovingBedList(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDialysisChair" , method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDialysisChair(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getDialysisChair(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/bedMasterUniversalSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response bedMasterUniversalSearch(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.bedMasterUniversalSearch(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getPaymentEntitlmentbyAdmissionId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPaymentEntitlmentbyAdmissionId(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getPaymentEntitlmentbyAdmissionId(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/bed/getIcuMonitorData" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIcuMonitorData(@RequestBody BedMasterDto bedMasterDto){
		try{
			return iBedMasterService.getIcuMonitorData(bedMasterDto);
		}catch(Exception e){

		}
		return new Response(ERROR, null, null, null,null);
	}
}
