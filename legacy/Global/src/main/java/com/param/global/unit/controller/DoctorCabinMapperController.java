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
import com.param.global.unit.dto.DoctorCabinMapperDto;
import com.param.global.unit.service.IDoctorCabinMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class DoctorCabinMapperController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(DoctorCabinMapperController.class);


	
	@Autowired
	private IDoctorCabinMapperService iDoctorCabinMapperService;
	
	
	@RequestMapping(value = "/saveDoctorCabinMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorCabinMapper(@RequestBody DoctorCabinMapperDto doctorCabinMapperDto){
		try{
			return iDoctorCabinMapperService.saveDoctorCabinMapper(doctorCabinMapperDto);
			
		}catch(Exception e){
		logger.error("saveDoctorCabinMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateDoctorCabinMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDoctorCabinMapper(@RequestBody DoctorCabinMapperDto doctorCabinMapperDto){
		try{
			
			return iDoctorCabinMapperService.updateDoctorCabinMapper(doctorCabinMapperDto);
			
		}catch(Exception e){
		logger.error("updateDoctorCabinMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateDoctorCabinMapperStatus/{doctorId}/{organisationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateDoctorCabinMapperStatus(@PathVariable ("doctorId") Integer doctorId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("status") Character status){
		try{
			return iDoctorCabinMapperService.updateDoctorCabinMapperStatus(doctorId, organisationId, unitId, status);
		}catch(Exception e){
		logger.error("updateDoctorCabinMapperStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getDoctorCabinMapperlist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getDoctorCabinMapperlist(@RequestBody DoctorCabinMapperDto doctorCabinMapperDto){
		try{
			return iDoctorCabinMapperService.getDoctorCabinMapperlist(doctorCabinMapperDto);
		}catch(Exception e){
		logger.error("getDoctorCabinMapperlist failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getDoctorCabinMapperCount/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getDoctorCabinMapperCount(@PathVariable ("organisationId") Integer organisationId,
											  @PathVariable ("unitId") Integer unitId){
		try{
			return iDoctorCabinMapperService.getDoctorCabinMapperCount(organisationId, unitId);
		}catch(Exception e){
		logger.error("getDoctorCabinMapperCount failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveDoctorCabinMapper/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getActiveDoctorCabinMapper(@PathVariable ("organisationId") Integer organisationId,
												@PathVariable ("unitId") Integer unitId){
		try{
				return iDoctorCabinMapperService.getActiveDoctorCabinMapper(organisationId, unitId);
		}catch(Exception e){
		logger.error("getActiveDoctorCabinMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorCabinMapperById/{doctorId}/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getDoctorCabinMapperById(@PathVariable ("doctorId") Integer doctorId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDoctorCabinMapperService.getDoctorCabinMapperById(doctorId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getDoctorCabinMapperById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDoctorList/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getDoctorList(@PathVariable ("organisationId") Integer organisationId,
									@PathVariable ("unitId")Integer unitId){
		try{
			return iDoctorCabinMapperService.getDoctorList(organisationId, unitId);
			
		}catch(Exception e){
		logger.error("getDoctorList failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getDetailedCabinList/{doctorId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getDetailedCabinList(@PathVariable ("doctorId") Integer doctorId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDoctorCabinMapperService.getDetailedCabinList(doctorId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getDetailedCabinList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);

	}
}
	
	

