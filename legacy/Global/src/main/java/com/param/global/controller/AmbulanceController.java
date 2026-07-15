package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AmbulanceChargesDto;
import com.param.global.dto.AmbulanceFacilitiesDto;
import com.param.global.dto.AmbulanceMasterDto;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.dto.ResourceAllocationDto;
import com.param.global.dto.ServiceProviderDto;
import com.param.global.service.IAmbulanceChargesService;
import com.param.global.service.IAmbulanceFacilitiesService;
import com.param.global.service.IAmbulanceMasterService;
import com.param.global.service.IAmbulanceTarrifService;
import com.param.global.service.IAmbulanceTypeService;
import com.param.global.service.IResourceAllocationService;
import com.param.global.service.IServiceProviderService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class AmbulanceController implements ICommonConstants
{
	final static Logger logger = Logger.getLogger(AmbulanceController.class);


  @Autowired
  private IAmbulanceTypeService iAmbulanceService;
	
  @Autowired
  private IAmbulanceTarrifService iAmbulanceTarrifService;
  
  @Autowired
  private IResourceAllocationService iResourceAllocationService;
  
  @Autowired
  private IAmbulanceFacilitiesService iAmbulanceFacilitiesService;
  
  @Autowired
  private IServiceProviderService iServiceProviderService;
  
  @Autowired
  private IAmbulanceMasterService iAmbulanceMasterService;  
  
  @Autowired
  private IAmbulanceChargesService iAmbulanceChargesService;
	
	@RequestMapping(value="/saveAmbulanceType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAmbulanceType(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			
			return iAmbulanceService.addAmbulanceType(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceTypeList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceTypeList(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			return iAmbulanceService.getAmbulanceTypeList(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceTypeActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceTypeActiveList(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			return iAmbulanceService.getAmbulanceTypeActiveList(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleAmbulanceType", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleAmbulanceType(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			return iAmbulanceService.getSingleAmbulanceType(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateAmbulanceType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAmbulanceType(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			return iAmbulanceService.updateAmbulanceType(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeAmbulanceStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeAmbulanceStatus(@RequestBody AmbulanceTypeDto ambulanceTypeDto){
		try{
			
			return iAmbulanceService.changeAmbulanceStatus(ambulanceTypeDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveAmbulanceTarrif" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAmbulanceTarrif(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			
			return iAmbulanceTarrifService.saveAmbulanceTarrif(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceTarrifList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceTarrifList(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			return iAmbulanceTarrifService.getAmbulanceTarrifList(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceTarrifActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceTarrifActiveList(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			return iAmbulanceTarrifService.getAmbulanceTarrifActiveList(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleAmbulanceTarrif", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleAmbulanceTarrif(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			return iAmbulanceTarrifService.getSingleAmbulanceTarrif(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateAmbulanceTarrif" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAmbulanceTarrif(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			return iAmbulanceTarrifService.updateAmbulanceTarrif(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeAmbulanceTarrifStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeAmbulanceTarrifStatus(@RequestBody AmbulanceTarrifDto ambulanceTarrifDto){
		try{
			
			return iAmbulanceTarrifService.changeAmbulanceTarrifStatus(ambulanceTarrifDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveResourceAllocation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResourceAllocation(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			
			return iResourceAllocationService.saveResourceAllocation(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getResourceAllocationList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResourceAllocationList(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			return iResourceAllocationService.getResourceAllocationList(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/getResourceAllocationListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResourceAllocationListCount(@RequestBody ResourceAllocationDto resourceAllocationDto){
		
		try {
			return iResourceAllocationService.getResourceAllocationListCount(resourceAllocationDto);
			
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/getResourceAllocationActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getResourceAllocationActiveList(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			return iResourceAllocationService.getResourceAllocationActiveList(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleResourceAllocation", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleResourceAllocation(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			return iResourceAllocationService.getSingleResourceAllocation(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateResourceAllocation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResourceAllocation(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			return iResourceAllocationService.updateResourceAllocation(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeResourceAllocationStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeResourceAllocationStatus(@RequestBody ResourceAllocationDto resourceAllocationDto){
		try{
			
			return iResourceAllocationService.changeResourceAllocationStatus(resourceAllocationDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	//////////////////////////////////
	@RequestMapping(value="/saveAmbulanceFacilities" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAmbulanceFacilities(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			
			return iAmbulanceFacilitiesService.saveAmbulanceFacilities(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceFacilitiesList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceFacilitiesList(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			return iAmbulanceFacilitiesService.getAmbulanceFacilitiesList(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceFacilitiesActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceFacilitiesActiveList(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			return iAmbulanceFacilitiesService.getAmbulanceFacilitiesActiveList(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleAmbulanceFacilities", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleAmbulanceFacilities(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			return iAmbulanceFacilitiesService.getSingleAmbulanceFacilities(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateAmbulanceFacilities" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAmbulanceFacilities(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			return iAmbulanceFacilitiesService.updateAmbulanceFacilities(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeAmbulanceFacilitiesStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeAmbulanceFacilitiesStatus(@RequestBody AmbulanceFacilitiesDto ambulanceFacilitiesDto){
		try{
			
			return iAmbulanceFacilitiesService.changeAmbulanceFacilitiesStatus(ambulanceFacilitiesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/saveServiceProvider" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveServiceProvider(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			
			return iServiceProviderService.saveServiceProvider(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getServiceProviderList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceProviderList(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			return iServiceProviderService.getServiceProviderList(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getServiceProviderActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceProviderActiveList(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			return iServiceProviderService.getServiceProviderActiveList(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleServiceProvider", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleServiceProvider(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			return iServiceProviderService.getSingleServiceProvider(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateServiceProvider" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateServiceProvider(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			return iServiceProviderService.updateServiceProvider(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeServiceProviderStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeServiceProviderStatus(@RequestBody ServiceProviderDto serviceProviderDto){
		try{
			
			return iServiceProviderService.changeServiceProviderStatus(serviceProviderDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	///////////////////////////////////
	@RequestMapping(value="/saveAmbulanceMasterData" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAmbulanceMasterData(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			return iAmbulanceMasterService.saveAmbulanceMasterData(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getAmbulanceMasterDataList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceMasterDataList(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			return iAmbulanceMasterService.getAmbulanceMasterDataList(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceMasterDataActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceMasterDataActiveList(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			return iAmbulanceMasterService.getAmbulanceMasterDataActiveList(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleAmbulanceMasterData", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleAmbulanceMasterData(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			return iAmbulanceMasterService.getSingleAmbulanceMasterData(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateAmbulanceMasterData" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAmbulanceMasterData(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			return iAmbulanceMasterService.updateAmbulanceMasterData(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeAmbulanceMasterDataStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeAmbulanceMasterDataStatus(@RequestBody AmbulanceMasterDto ambulanceMasterDto){
		try{
			
			return iAmbulanceMasterService.changeAmbulanceMasterDataStatus(ambulanceMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	///////////////////////////////////
	@RequestMapping(value="/saveAmbulanceCharges" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAmbulanceCharges(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			return iAmbulanceChargesService.saveAmbulanceCharges(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceChargesList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceChargesList(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			return iAmbulanceChargesService.getAmbulanceChargesList(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAmbulanceChargesActiveList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAmbulanceChargesActiveList(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			return iAmbulanceChargesService.getAmbulanceChargesActiveList(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleAmbulanceCharges", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleAmbulanceCharges(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			return iAmbulanceChargesService.getSingleAmbulanceCharges(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateAmbulanceCharges" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAmbulanceCharges(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			return iAmbulanceChargesService.updateAmbulanceCharges(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/changeAmbulanceChargesStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response changeAmbulanceChargesStatus(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			
			return iAmbulanceChargesService.changeAmbulanceChargesStatus(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/cancelOrder" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelOrder(@RequestBody AmbulanceChargesDto ambulanceChargesDto){
		try{
			
			return iAmbulanceChargesService.cancelOrder(ambulanceChargesDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
}
