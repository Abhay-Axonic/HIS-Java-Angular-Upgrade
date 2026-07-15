package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.DoctorInCabinDto;
import com.param.global.unit.service.ICabinMasterService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class CabinMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(CabinMasterController.class);

	@Autowired
	private ICabinMasterService iCabinMasterService;
	
	@RequestMapping(value = "/saveCabinMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCabinMaster(@RequestBody CabinMasterDto cabinMasterDto){
		
		try {
			return iCabinMasterService.saveCabinMaster(cabinMasterDto);
			
		} catch (Exception e) {
		logger.error("saveCabinMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateCabinMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCabinMaster(@RequestBody CabinMasterDto cabinMasterDto){
		
		try {
			return iCabinMasterService.updateCabinMaster(cabinMasterDto);
			
		} catch (Exception e) {
		logger.error("updateCabinMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateCabinMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCabinMasterStatus(@RequestBody CabinMasterDto cabinMasterDto){
		
		try {
			return iCabinMasterService.updateCabinMasterStatus(cabinMasterDto);
			
		} catch (Exception e) {
		logger.error("updateCabinMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCabinMasterById/{cabinId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getCabinMasterById(@PathVariable ("cabinId") Integer cabinId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iCabinMasterService.getCabinMasterById(cabinId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getCabinMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getCabinMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCabinMasterList(@RequestBody CabinMasterDto cabinMasterDto){

		try {
			return iCabinMasterService.getCabinMasterList(cabinMasterDto);

		} catch (Exception e) {
		logger.error("getCabinMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCabinMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCabinMasterCount(@RequestBody CabinMasterDto cabinMasterDto){
		
		try {
			return iCabinMasterService.getCabinMasterCount(cabinMasterDto);
			
		} catch (Exception e) {
		logger.error("getCabinMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveCabinMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveCabinMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return iCabinMasterService.getActiveCabinMaster(organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getActiveCabinMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCoversheetAccessMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCoversheetAccessMaster(@RequestBody CoversheetAccessMasterDto coversheetAccessMasterDto){

		try {
			return iCabinMasterService.getCoversheetAccessMaster(coversheetAccessMasterDto);

		} catch (Exception e) {
		logger.error("getCoversheetAccessMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveDoctorInCabin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorInCabin(@RequestBody DoctorInCabinDto dto){

		try {
			return iCabinMasterService.saveDoctorInCabin(dto);

		} catch (Exception e) {
		logger.error("saveDoctorInCabin failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/inactivePreviousDoctorInCabin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response inactivePreviousDoctorInCabin(@RequestBody DoctorInCabinDto dto){

		try {
			return iCabinMasterService.inactivePreviousDoctorInCabin(dto);

		} catch (Exception e) {
		logger.error("inactivePreviousDoctorInCabin failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorInCabin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDoctorInCabin(@RequestBody DoctorInCabinDto dto){

		try {
			return iCabinMasterService.getDoctorInCabin(dto);

		} catch (Exception e) {
		logger.error("getDoctorInCabin failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
