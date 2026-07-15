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
import com.param.global.dto.AllergyMasterDto;
import com.param.global.service.IAllergyMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : ALLERGY MASTER
DATE:12/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(AllergyMasterController.class);


	@Autowired
	private IAllergyMasterService iAllergyMasterService;
	
	
	@RequestMapping(value="/getAllergyMaster", method=RequestMethod.GET)
	public @ResponseBody Response listOfAllergyTypeMaster(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="allergy_type_id") int allergyTypeId) {
		try
		{
			AllergyMasterDto allergyMasterDto= new AllergyMasterDto();
//			allergyMasterDto.setUnitId(unitId);
			allergyMasterDto.setOrganizationId(organizationId);
			allergyMasterDto.setAllergyTypeId(allergyTypeId);
			return iAllergyMasterService.getListOfAllergyMaster(allergyMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "saveAllergyMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAllergyMaster(@RequestBody AllergyMasterDto allergyMasterDto) {
		try {
			return iAllergyMasterService.saveAllergyMaster(allergyMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "updateAllergyMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAllergyMaster(@RequestBody AllergyMasterDto allergyMasterDto) {
		try {
			return iAllergyMasterService.updateAllergyMaster(allergyMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "updateAllergyMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAllergyMasterStatus(@RequestBody AllergyMasterDto allergyMasterDto) {
		try {
			return iAllergyMasterService.updateAllergyMasterStatus(allergyMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllergyMasterList(@RequestBody AllergyMasterDto allergyMasterDto) {
		try {
			return iAllergyMasterService.getAllergyMasterList(allergyMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllergyMasterListCount(@RequestBody AllergyMasterDto allergyMasterDto) {
		try {
			return iAllergyMasterService.getAllergyMasterListCount(allergyMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyMasterListById/{allergyId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getAllergyMasterListById(@PathVariable("allergyId") Integer allergyId,
			@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId) {
		try {
			return iAllergyMasterService.getAllergyMasterListById(allergyId,orgId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
