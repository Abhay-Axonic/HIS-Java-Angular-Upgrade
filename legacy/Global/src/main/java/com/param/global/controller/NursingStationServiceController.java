package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.NursingStationMasterDto;
import com.param.global.service.INursingStationService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : NURSING STATION
DATE:08/05/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingStationServiceController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingStationServiceController.class);


	@Autowired
	private INursingStationService iNursingStationService;
	
	@RequestMapping(value="/getListOfNursingStation", method=RequestMethod.GET)
	public @ResponseBody Response getListOfNursingStation(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			NursingStationMasterDto nursingStationMasterDto= new NursingStationMasterDto();
			nursingStationMasterDto.setUnitId(unitId);
			nursingStationMasterDto.setOrganizationId(organizationId);
			return iNursingStationService.getListOfNursingStationMaster(nursingStationMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getListOfNursingStationByClinicId", method=RequestMethod.GET)
	public @ResponseBody Response getListOfNursingStationByClinicId(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam(value="clinic_id") int clinicId) {
		try
		{
			NursingStationMasterDto nursingStationMasterDto= new NursingStationMasterDto();
			nursingStationMasterDto.setUnitId(unitId);
			nursingStationMasterDto.setOrganizationId(organizationId);
			nursingStationMasterDto.setClinicId(clinicId);
			return iNursingStationService.getListOfNursingStationMasterByClinicId(nursingStationMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/saveNursingStationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNursingStationMaster(@RequestBody NursingStationMasterDto nursingStationMasterDto) {
		try {
			return iNursingStationService.saveNursingStationMaster(nursingStationMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getNursingStationMasterById/{nursingId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getNursingStationMasterById(@PathVariable("nursingId") int nursingId,
			@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId) {
		try {
			return iNursingStationService.getNursingStationMasterById(nursingId, orgId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getNursingStationMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getNursingStationMasterList(@RequestBody NursingStationMasterDto nursingStationMasterDto) {
		try {
			return iNursingStationService.getNursingStationMasterList(nursingStationMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateNursingStationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNursingStationMaster(@RequestBody NursingStationMasterDto nursingStationMasterDto) {
		try {
			return iNursingStationService.updateNursingStationMaster(nursingStationMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateNursingStationMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNursingStationMasterStatus(@RequestBody NursingStationMasterDto nursingStationMasterDto) {
		try {
			return iNursingStationService.updateNursingStationMasterStatus(nursingStationMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getNursingStationMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNursingStationMasterCount(@RequestBody NursingStationMasterDto nursingStationMasterDto) {
		try {
			return iNursingStationService.getNursingStationMasterCount(nursingStationMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
