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
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.service.IPatientTravelHistoryDetailsService;


/*FUNCTIONALITY : Travel HISTORY
DATE:27/04/2018
CREATED BY: Vivek*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientTravelHistoryDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PatientTravelHistoryDetailsController.class);

	@Autowired
	private IPatientTravelHistoryDetailsService iPatientTravelHistoryDetailsService;


	@RequestMapping(value="/getOldPatientTravelHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response getOldPatientTravelHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto= new PatientTravelHistoryDetailsDto();
			patientTravelHistoryDetailsDto.setPatientId(patientId);
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			patientTravelHistoryDetailsDto.setCreatedDate(createdDate);
//			patientTravelHistoryDetailsDto.setEncounterId(encounterId);
//			patientTravelHistoryDetailsDto.setAdmissionId(admissionId);
//			patientTravelHistoryDetailsDto.setRoleId(roleId);
			return iPatientTravelHistoryDetailsService.getOldPatientTravelHistory(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getOldPatientTravelHistoryDetails failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentPatientTravelHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientTravelHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="roleId") int roleId,@RequestParam(value="encounterId",required=false) Integer encounterId,@RequestParam(value="admissionId",required=false) Integer admissionId) {
		try
		{
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto= new PatientTravelHistoryDetailsDto();
			patientTravelHistoryDetailsDto.setPatientId(patientId);
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			patientTravelHistoryDetailsDto.setEncounterId(encounterId);
			patientTravelHistoryDetailsDto.setAdmissionId(admissionId);
			patientTravelHistoryDetailsDto.setRoleId(roleId);
			return iPatientTravelHistoryDetailsService.getCurrentPatientTravelHistory(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientTravelHistoryDetails failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/savePatientTravelHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientTravelHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) {
		try
		{
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTravelHistoryDetailsService.savePatientTravelHistoryDetails(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("savePatientTravelHistoryDetails failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updatePatientTravelHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientTravelHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) {
		try
		{
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTravelHistoryDetailsService.updatePatientTravelHistory(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("updatePatientTravelHistoryDetails failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateEnterErrorStatusPatientTravelHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEnterErrorStatusPatientTravelHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) {
		try
		{
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTravelHistoryDetailsService.updateStatusEnterInError(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("updateEnterErrorStatusPatientTravelHistoryDetails failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCurrentPatientTravelHistoryDetailsForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientTravelHistoryDetailsForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto= new PatientTravelHistoryDetailsDto();
			patientTravelHistoryDetailsDto.setPatientId(patientId);
			patientTravelHistoryDetailsDto.setUnitId(unitId);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			patientTravelHistoryDetailsDto.setCreatedDate(createdDate);
//			patientTravelHistoryDetailsDto.setEncounterId(encounterId);
//			patientTravelHistoryDetailsDto.setAdmissionId(admissionId);
//			patientTravelHistoryDetailsDto.setRoleId(roleId);
			return iPatientTravelHistoryDetailsService.getCurrentPatientTravelHistoryDetailsForCoversheet(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getCurrentPatientTravelHistoryDetailsForCoversheet failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getPatientTravelHistoryForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientTravelHistoryForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto= new PatientTravelHistoryDetailsDto();
			patientTravelHistoryDetailsDto.setPatientId(patientId);
			patientTravelHistoryDetailsDto.setFromDate(fromDate);
			patientTravelHistoryDetailsDto.setToDate(toDate);
			patientTravelHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTravelHistoryDetailsService.getPatientTravelHistoryForBookView(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("getPatientTravelHistoryForBookView failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateStatusRemoveTravelHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusRemoveTravelHistory(@RequestBody PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto){

		try {
			return iPatientTravelHistoryDetailsService.updateStatusRemoveTravelHistory(patientTravelHistoryDetailsDto);

		} catch (Exception e) {
			logger.error("updateStatusRemoveTravelHistory failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
