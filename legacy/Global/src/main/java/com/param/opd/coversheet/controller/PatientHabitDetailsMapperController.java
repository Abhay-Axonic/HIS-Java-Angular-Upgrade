package com.param.opd.coversheet.controller;

import org.apache.log4j.Logger;
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
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dto.ImmunizationHistoryMapperDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.service.IPatientHabitDetailsMapperService;

/*FUNCTIONALITY : PATIENT HABIT DETAILS MAPPER
DATE:09/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientHabitDetailsMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PatientHabitDetailsMapperController.class);

	@Autowired
	private IPatientHabitDetailsMapperService iPatientHabitDetailsMapperService;

	@RequestMapping(value="/savePatientHabitDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientHabitDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			return iPatientHabitDetailsMapperService.savePatientHabitDetailsMapper(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("savePatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updatePatientHabitDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientHabitDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			return iPatientHabitDetailsMapperService.updatePatientHabitDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("updatePatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateEnterErrorStatusPatientHabitDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEnterErrorStatusPatientHabitDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			return iPatientHabitDetailsMapperService.updateStatusEnterInError(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("updateEnterErrorStatusPatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getPatientHabitDetails", method=RequestMethod.GET)
	public @ResponseBody Response listOfPatientHabitDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientHabitDetailsDto patientHabitDetailsDto= new PatientHabitDetailsDto();
			patientHabitDetailsDto.setPatientId(patientId);
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			return iPatientHabitDetailsMapperService.getListOfPatientHabitDetailsMapper(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("listOfPatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getOldPatientHabitDetails", method=RequestMethod.GET)
	public @ResponseBody Response getOldPatientHabitDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId",required=false) Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try
		{
			PatientHabitDetailsDto patientHabitDetailsDto= new PatientHabitDetailsDto();
			patientHabitDetailsDto.setPatientId(patientId);
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			patientHabitDetailsDto.setRoleId(roleId);
			patientHabitDetailsDto.setEncounterId(encounterId);
			patientHabitDetailsDto.setAdmissionId(admissionId);
			return iPatientHabitDetailsMapperService.getOldPatientHabitDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("getOldPatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentPatientHabitDetails", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientHabitDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId",required=false) Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try
		{
			PatientHabitDetailsDto patientHabitDetailsDto= new PatientHabitDetailsDto();
			patientHabitDetailsDto.setPatientId(patientId);
			patientHabitDetailsDto.setUnitId(unitId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			patientHabitDetailsDto.setRoleId(roleId);
			patientHabitDetailsDto.setEncounterId(encounterId);
			patientHabitDetailsDto.setAdmissionId(admissionId);
			return iPatientHabitDetailsMapperService.getCurrentPatientHabitDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientHabitDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getPatientHabitsForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientHabitsForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientHabitDetailsDto patientHabitDetailsDto= new PatientHabitDetailsDto();
			patientHabitDetailsDto.setPatientId(patientId);
			patientHabitDetailsDto.setOrganizationId(organizationId);
			patientHabitDetailsDto.setFromDate(fromDate);
			patientHabitDetailsDto.setToDate(toDate);
			return iPatientHabitDetailsMapperService.getPatientHabitsForBookView(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("getPatientHabitsForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getPatientHabitsForNewCov", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientHabitsForNewCov(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.getPatientHabitsForNewCov(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("getPatientHabitsForNewCov failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getSearchedHabits", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSearchedHabits(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.getSearchedHabits(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("getSearchedHabits failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/savePatientHabitsDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientHabitsDetails(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.savePatientHabitsDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("savePatientHabitsDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/removePatientHabitsDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response removePatientHabitsDetails(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.removePatientHabitsDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("removePatientHabitsDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updatePatientHabitRemarkForNewCov", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updatePatientHabitRemarkForNewCov(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.updatePatientHabitRemarkForNewCov(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("updatePatientHabitRemarkForNewCov failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
