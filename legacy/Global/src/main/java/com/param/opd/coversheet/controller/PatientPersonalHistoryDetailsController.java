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
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.service.IPatientPersonalHistoryDetailsService;


/*FUNCTIONALITY : PERSONAL HISTORY
DATE:05/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientPersonalHistoryDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PatientPersonalHistoryDetailsController.class);

	@Autowired
	private IPatientPersonalHistoryDetailsService iPatientPersonalHistoryDetailsService;

	@RequestMapping(value="/getOldPatientPersonalHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response listOfPatientPersonalHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto= new PatientPersonalHistoryDetailsDto();
			patientPersonalHistoryDetailsDto.setPatientId(patientId);
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			patientPersonalHistoryDetailsDto.setCreatedDate(createdDate);

//			patientPersonalHistoryDetailsDto.setEncounterId(encounterId);
//			patientPersonalHistoryDetailsDto.setAdmissionId(admissionId);
//			patientPersonalHistoryDetailsDto.setRoleId(roleId);
			return iPatientPersonalHistoryDetailsService.getListOfOldPatientPersonalHistoryDetails(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("listOfPatientPersonalHistoryDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/getCurrentPatientPersonalHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientPersonalHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="encounterId" , required = false) Integer encounterId,@RequestParam(value="roleId") int roleId,@RequestParam(value="admissionId" , required = false) Integer admissionId) {
		try
		{
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto= new PatientPersonalHistoryDetailsDto();
			patientPersonalHistoryDetailsDto.setPatientId(patientId);
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			patientPersonalHistoryDetailsDto.setEncounterId(encounterId);
			patientPersonalHistoryDetailsDto.setAdmissionId(admissionId);
			patientPersonalHistoryDetailsDto.setRoleId(roleId);

			return iPatientPersonalHistoryDetailsService.getListOfCurrentPatientPersonalHistoryDetails(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientPersonalHistoryDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/savePatientPersonalHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientPersonalHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) {
		try
		{
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientPersonalHistoryDetailsService.savePatientPersonalHistoryDetails(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("savePatientPersonalHistoryDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updatePatientPersonalHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientPersonalHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) {
		try
		{
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientPersonalHistoryDetailsService.updatePatientPersonalHistoryDetails(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("updatePatientPersonalHistoryDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateStatusEnterInError", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusEnterInError(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) {
		try
		{
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientPersonalHistoryDetailsService.updateStatusEnterInError(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("updateStatusEnterInError failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentPatientPersonalHistoryDetailsForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientPersonalHistoryDetailsForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto= new PatientPersonalHistoryDetailsDto();
			patientPersonalHistoryDetailsDto.setPatientId(patientId);
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			patientPersonalHistoryDetailsDto.setCreatedDate(createdDate);

//			patientPersonalHistoryDetailsDto.setEncounterId(encounterId);
//			patientPersonalHistoryDetailsDto.setAdmissionId(admissionId);
//			patientPersonalHistoryDetailsDto.setRoleId(roleId);
//
			return iPatientPersonalHistoryDetailsService.getCurrentPatientPersonalHistoryDetailsForCoversheet(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientPersonalHistoryDetailsForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getPatientPersonalHistoryForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientPersonalHistoryForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organizationId") int organizationId) {
		try
		{
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto= new PatientPersonalHistoryDetailsDto();
			patientPersonalHistoryDetailsDto.setPatientId(patientId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			patientPersonalHistoryDetailsDto.setFromDate(fromDate);
			patientPersonalHistoryDetailsDto.setToDate(toDate);
			return iPatientPersonalHistoryDetailsService.getPatientPersonalHistoryForBookView(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getPatientPersonalHistoryForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/saveResolvedPersonalHistoryDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResolvedPersonalHistoryDate(@RequestBody PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) {
		try {
			return iPatientPersonalHistoryDetailsService.saveResolvedPersonalHistoryDate(patientPersonalHistoryDetailsDto);
		} catch (Exception e) {
			logger.error("saveResolvedPersonalHistoryDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	//sneha
	@RequestMapping(value="/getResolvedPatientPersonalHistoryDetailsForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getResolvedPatientPersonalHistoryDetailsForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto= new PatientPersonalHistoryDetailsDto();
			patientPersonalHistoryDetailsDto.setPatientId(patientId);
			patientPersonalHistoryDetailsDto.setUnitId(unitId);
			patientPersonalHistoryDetailsDto.setOrganizationId(organizationId);
			patientPersonalHistoryDetailsDto.setCreatedDate(createdDate);

//			patientPersonalHistoryDetailsDto.setEncounterId(encounterId);
//			patientPersonalHistoryDetailsDto.setAdmissionId(admissionId);
//			patientPersonalHistoryDetailsDto.setRoleId(roleId);
//
			return iPatientPersonalHistoryDetailsService.getResolvedPatientPersonalHistoryDetailsForCoversheet(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getResolvedPatientPersonalHistoryDetailsForCoversheet failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
