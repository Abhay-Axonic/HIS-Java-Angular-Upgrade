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
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.service.IPatientSurgicalHistoryService;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;

/*FUNCTIONALITY : SURGICAL HISTORY
DATE:06/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientSurgicalHistoryController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PatientSurgicalHistoryController.class);

	@Autowired
	private IPatientSurgicalHistoryService iPatientSurgicalHistoryService;
	
	@RequestMapping(value="/getOldPatientSurgicalHistory", method=RequestMethod.GET)
	public @ResponseBody Response getOldPatientSurgicalHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientSurgicalHistoryDto patientSurgicalHistoryDto= new PatientSurgicalHistoryDto();
			patientSurgicalHistoryDto.setPatientId(patientId);
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			patientSurgicalHistoryDto.setCreatedDate(createdDate);
//			patientSurgicalHistoryDto.setRoleId(roleId);
//			patientSurgicalHistoryDto.setEncounterId(encounterId);
//			patientSurgicalHistoryDto.setAdmissionId(admissionId);
			return iPatientSurgicalHistoryService.getOldListOfPatientSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("getOldPatientSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentPatientSurgicalHistory", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientSurgicalHistory(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId, @RequestParam(value="encounterId",required=false) Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try
		{
			PatientSurgicalHistoryDto patientSurgicalHistoryDto= new PatientSurgicalHistoryDto();
			patientSurgicalHistoryDto.setPatientId(patientId);
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			patientSurgicalHistoryDto.setRoleId(roleId);
			patientSurgicalHistoryDto.setEncounterId(encounterId);
			patientSurgicalHistoryDto.setAdmissionId(admissionId);
			return iPatientSurgicalHistoryService.getCurrentListOfPatientSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/savePatientSurgicalHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientSurgicalHistory(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto) {
		try
		{
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			return iPatientSurgicalHistoryService.savePatientSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("savePatientSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updatePatientSurgicalHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientSurgicalHistory(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto) {
		try
		{
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			return iPatientSurgicalHistoryService.updatePatientSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("updatePatientSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEnterErrorStatusPatientSurgicalHistory", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEnterErrorStatusPatientSurgicalHistory(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto) {
		try
		{
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			return iPatientSurgicalHistoryService.updateStatusEnterInError(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("updateEnterErrorStatusPatientSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentPatientSurgicalHistoryForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientSurgicalHistoryForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientSurgicalHistoryDto patientSurgicalHistoryDto= new PatientSurgicalHistoryDto();
			patientSurgicalHistoryDto.setPatientId(patientId);
			patientSurgicalHistoryDto.setUnitId(unitId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			patientSurgicalHistoryDto.setCreatedDate(createdDate);
//			patientSurgicalHistoryDto.setRoleId(roleId);
//			patientSurgicalHistoryDto.setEncounterId(encounterId);
//			patientSurgicalHistoryDto.setAdmissionId(admissionId);
			return iPatientSurgicalHistoryService.getCurrentPatientSurgicalHistoryForCoversheet(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientSurgicalHistoryForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientSurgicalHistoryForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientSurgicalHistoryForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientSurgicalHistoryDto patientSurgicalHistoryDto= new PatientSurgicalHistoryDto();
			patientSurgicalHistoryDto.setPatientId(patientId);
			patientSurgicalHistoryDto.setFromDate(fromDate);
			patientSurgicalHistoryDto.setToDate(toDate);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			return iPatientSurgicalHistoryService.getPatientSurgicalHistoryForBookView(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("getPatientSurgicalHistoryForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCompletedProcedureListForSurgicalHistory", method=RequestMethod.GET)
	public @ResponseBody Response getCompletedProcedureListForSurgicalHistory(@RequestParam(value="patientId") int patientId,
			@RequestHeader(value="organizationId") int organizationId) {
		try
		{
			PatientSurgicalHistoryDto patientSurgicalHistoryDto= new PatientSurgicalHistoryDto();
			patientSurgicalHistoryDto.setPatientId(patientId);
			patientSurgicalHistoryDto.setOrganizationId(organizationId);
			return iPatientSurgicalHistoryService.getCompletedProcedureListForSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("getCompletedProcedureListForSurgicalHistory failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	@RequestMapping(value = "/updateStatusRemoveSurgicalHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusRemoveSurgicalHistory(@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto){
		
		try {
			return iPatientSurgicalHistoryService.updateStatusRemoveSurgicalHistory(patientSurgicalHistoryDto);

		} catch (Exception e) {
			logger.error("updateStatusRemoveSurgicalHistory failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getMostUsedSurgicalHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMostUsedSurgicalHistory(@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto){
		
		try {
			return iPatientSurgicalHistoryService.getMostUsedSurgicalHistory(patientSurgicalHistoryDto);

		} catch (Exception e) {
			logger.error("getMostUsedSurgicalHistory failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}
