package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.service.IpatientFamilyHistoryService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : FAMILY HISTORY
DATE:06/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientFamilyHistoryController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientFamilyHistoryController.class);


	@Autowired
	private IpatientFamilyHistoryService iPatientFamilyHistoryService;
	

	@RequestMapping(value="/getOldPatientFamilyHistory", method=RequestMethod.GET)
	public @ResponseBody Response getOldPatientFamilyHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientFamilyHistoryDto patientFamilyHistoryDto= new PatientFamilyHistoryDto();
			patientFamilyHistoryDto.setPatientId(patientId);
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			patientFamilyHistoryDto.setCreatedDate(createdDate);
//			patientFamilyHistoryDto.setRoleId(roleId);
//			patientFamilyHistoryDto.setEncounterId(encounterId);
//			patientFamilyHistoryDto.setAdmissionId(admissionId);
			return iPatientFamilyHistoryService.getOldPatientFamilyHistory(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentPatientFamilyHistory", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientFamilyHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId",required=false) Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try
		{
			PatientFamilyHistoryDto patientFamilyHistoryDto= new PatientFamilyHistoryDto();
			patientFamilyHistoryDto.setPatientId(patientId);
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			patientFamilyHistoryDto.setRoleId(roleId);
			patientFamilyHistoryDto.setEncounterId(encounterId);
			patientFamilyHistoryDto.setAdmissionId(admissionId);
			return iPatientFamilyHistoryService.getCurrentPatientFamilyHistory(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/savePatientFamilyHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientPersonalHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientFamilyHistoryDto patientFamilyHistoryDto) {
		try
		{
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			return iPatientFamilyHistoryService.savePatientFamilyHistory(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updatePatientFamilyHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientFamilyHistory(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientFamilyHistoryDto patientFamilyHistoryDto) {
		try
		{
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			return iPatientFamilyHistoryService.updatePatientFamilyHistory(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateFamilyStatusEnterInError", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateFamilyStatusEnterInError(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientFamilyHistoryDto patientFamilyHistoryDto) {
		try
		{
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			return iPatientFamilyHistoryService.updateStatusEnterInError(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getCurrentPatientFamilyHistoryForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientFamilyHistoryForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientFamilyHistoryDto patientFamilyHistoryDto= new PatientFamilyHistoryDto();
			patientFamilyHistoryDto.setPatientId(patientId);
			patientFamilyHistoryDto.setUnitId(unitId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			patientFamilyHistoryDto.setCreatedDate(createdDate);
//			patientFamilyHistoryDto.setRoleId(roleId);
//			patientFamilyHistoryDto.setEncounterId(encounterId);
//			patientFamilyHistoryDto.setAdmissionId(admissionId);
			return iPatientFamilyHistoryService.getCurrentPatientFamilyHistoryForCoversheet(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientFamilyHistoryForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientFamilyHistoryForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organizationId") int organizationId) {
		try
		{
			PatientFamilyHistoryDto patientFamilyHistoryDto= new PatientFamilyHistoryDto();
			patientFamilyHistoryDto.setPatientId(patientId);
			patientFamilyHistoryDto.setOrganizationId(organizationId);
			patientFamilyHistoryDto.setFromDate(fromDate);
			patientFamilyHistoryDto.setToDate(toDate);
			return iPatientFamilyHistoryService.getPatientFamilyHistoryForBookView(patientFamilyHistoryDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateStatusRemoveFamilyHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusRemoveFamilyHistory(@RequestBody PatientFamilyHistoryDto patientFamilyHistoryDto){
		
		try {
			return iPatientFamilyHistoryService.updateStatusRemoveFamilyHistory(patientFamilyHistoryDto);
			
		} catch (Exception e) {
		logger.error("updateStatusRemoveFamilyHistory failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
