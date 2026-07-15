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
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.service.IPatientTreatmentHistoryDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientTreatmentHistoryDetailsController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientTreatmentHistoryDetailsController.class);


	@Autowired
	private IPatientTreatmentHistoryDetailsService iPatientTreatmentHistoryDetailsService;
	
	
	@RequestMapping(value="/savePatientTreatmentHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientTreatmentHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		try
		{
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.savePatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updatePatientTreatmentHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientTreatmentHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		try
		{
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.updatePatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getOldPatientTreatmentHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response getOldPatientTreatmentHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			patientTreatmentHistoryDetailsDto.setCreatedDate(createdDate);
//			patientTreatmentHistoryDetailsDto.setRoleId(roleId);
//			patientTreatmentHistoryDetailsDto.setEncounterId(encounterId);
//			patientTreatmentHistoryDetailsDto.setAdmissionId(admissionId);
			return iPatientTreatmentHistoryDetailsService.getOldListOfPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentPatientTreatmentHistoryDetails", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientTreatmentHistoryDetails(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
//			patientTreatmentHistoryDetailsDto.setRoleId(roleId);
//			patientTreatmentHistoryDetailsDto.setEncounterId(encounterId);
//			patientTreatmentHistoryDetailsDto.setAdmissionId(admissionId);
			return iPatientTreatmentHistoryDetailsService.getCurrentListOfPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEnterErrorStatusPatientTreatmentHistoryDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEnterErrorStatusPatientTreatmentHistoryDetails(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		try
		{
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.updateEnterErrorStatusPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCurrentPatientTreatmentHistoryDetailsForCoversheet", method=RequestMethod.GET)
	public @ResponseBody Response getCurrentPatientTreatmentHistoryDetailsForCoversheet(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			patientTreatmentHistoryDetailsDto.setCreatedDate(createdDate);
//			patientTreatmentHistoryDetailsDto.setRoleId(roleId);
//			patientTreatmentHistoryDetailsDto.setEncounterId(encounterId);
//			patientTreatmentHistoryDetailsDto.setAdmissionId(admissionId);
			return iPatientTreatmentHistoryDetailsService.getCurrentPatientTreatmentHistoryDetailsForCoversheet(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientTreatmentHistoryForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getPatientTreatmentHistoryForBookView(@RequestParam(value="patientId") int patientId,
			@RequestParam(value="fromDate") String fromDate,@RequestParam(value="toDate") String toDate,
			@RequestHeader(value="organization_id") int organizationId) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setFromDate(fromDate);
			patientTreatmentHistoryDetailsDto.setToDate(toDate);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.getPatientTreatmentHistoryForBookView(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCompletedDiagnosticsProcedures", method=RequestMethod.GET)
	public @ResponseBody Response getCompletedDiagnosticsProcedures(@RequestParam(value="patientId") int patientId,
			@RequestHeader(value="organizationId") int organizationId) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.getCompletedDiagnosticsProcedures(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCompletedMedicalProcedures", method=RequestMethod.GET)
	public @ResponseBody Response getCompletedMedicalProcedures(@RequestParam(value="patientId") int patientId,
			@RequestHeader(value="organizationId") int organizationId) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			return iPatientTreatmentHistoryDetailsService.getCompletedMedicalProcedures(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/updateStatusRemoveTreatmentHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusRemoveTreatmentHistory(@RequestBody PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto){
		
		try {
			return iPatientTreatmentHistoryDetailsService.updateStatusRemoveTreatmentHistory(patientTreatmentHistoryDetailsDto);
			
		} catch (Exception e) {
		logger.error("updateStatusRemoveTreatmentHistory failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/getPatientTreatmentHistoryForDischargeSummary", method=RequestMethod.GET)
	public @ResponseBody Response getPatientTreatmentHistoryForDischargeSummary(@RequestParam(value="patientId") int patientId,@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam(value="created_date") String createdDate) {
		try
		{
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto= new PatientTreatmentHistoryDetailsDto();
			patientTreatmentHistoryDetailsDto.setPatientId(patientId);
			patientTreatmentHistoryDetailsDto.setUnitId(unitId);
			patientTreatmentHistoryDetailsDto.setOrganizationId(organizationId);
			patientTreatmentHistoryDetailsDto.setCreatedDate(createdDate);
			return iPatientTreatmentHistoryDetailsService.getPatientTreatmentHistoryForDischargeSummary(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}

