package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.service.IComplaintAppointmentMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : COMPLAINT APPOINTMENT
DATE:03/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComplaintAppointmentMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ComplaintAppointmentMapperController.class);


	@Autowired
	private IComplaintAppointmentMapperService iComplaintAppointmentMapperService;
	
	@RequestMapping(value="/getComplaintAppointment", method=RequestMethod.GET)
	public @ResponseBody Response listOfComplaintAppointment(@RequestParam(value="unit_id") int unitId, @RequestParam(value="organization_id") int organizationId,
			@RequestParam(value="encounterId") int encounterId,@RequestParam(value="roleId") int roleId) {
		try
		{
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto= new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setUnitId(unitId);
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			complaintAppointmentMapperDto.setEncounterId(encounterId);
			complaintAppointmentMapperDto.setRoleId(roleId);
			return iComplaintAppointmentMapperService.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/saveComplaintAppointment/{unitId}/{organizationId}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveComplaintAppointment(@PathVariable("unitId") int unitId,
	   @PathVariable("organizationId") int organizationId,@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			complaintAppointmentMapperDto.setUnitId(unitId);
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			return iComplaintAppointmentMapperService.saveComplaintAppointmentMapper(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/editComplaintAppointment", method=RequestMethod.GET)
	public @ResponseBody Response editComplaintAppointment(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="complaintAppoId") int complaintAppoId) {
		try
		{
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto= new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setUnitId(unitId);
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			complaintAppointmentMapperDto.setComplaintAppoId(complaintAppoId);
			return iComplaintAppointmentMapperService.editListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateComplaintAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateComplaintAppointment(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			complaintAppointmentMapperDto.setUnitId(unitId);
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			return iComplaintAppointmentMapperService.updateComplaintAppointmentMapper(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateComplaintAppointmentStatusToRemoveComplaint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateComplaintAppointmentStatusToRemoveComplaint(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto){
		
		try {
			return iComplaintAppointmentMapperService.updateComplaintAppointmentStatusToRemoveComplaint(complaintAppointmentMapperDto);
			
		} catch (Exception e) {
		logger.error("updateComplaintAppointmentStatusToRemoveComplaint failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/getComplaintAppointmentByPatientIdForBookView", method=RequestMethod.GET)
	public @ResponseBody Response getComplaintAppointmentByPatientIdForBookView(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="patientId") int patientId) {
		try
		{
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto= new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setUnitId(unitId);
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			complaintAppointmentMapperDto.setPatientId(patientId);
			return iComplaintAppointmentMapperService.getComplaintAppointmentByPatientIdForBookView(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getComplaintAppointmentByPatientId", method=RequestMethod.GET)
	public @ResponseBody Response getComplaintAppointmentByPatientId(@RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="patientId") int patientId) {
		try
		{
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto= new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			complaintAppointmentMapperDto.setPatientId(patientId);
			return iComplaintAppointmentMapperService.getComplaintAppointmentByPatientId(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getPastComplaintAppointmentByPatientId", method=RequestMethod.GET)
	public @ResponseBody Response getPastComplaintAppointmentByPatientId(@RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="patientId") int patientId) {
		try
		{
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto= new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setOrganizationId(organizationId);
			complaintAppointmentMapperDto.setPatientId(patientId);
			return iComplaintAppointmentMapperService.getPastComplaintAppointmentByPatientId(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveResolvedComplaintAppointment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResolvedComplaintAppointment(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			return iComplaintAppointmentMapperService.saveResolvedComplaintAppointment(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getComplaintsByPatientIdForBookView",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getComplaintsByPatientIdForBookView(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			return iComplaintAppointmentMapperService.getComplaintsByPatientIdForBookView(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPastComplaintAppointmentForBookView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPastComplaintAppointmentForBookView(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			return iComplaintAppointmentMapperService.getPastComplaintAppointmentForBookView(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getMostSavedComplaint",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMostSavedComplaint(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			return iComplaintAppointmentMapperService.getMostSavedComplaint(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchFreeTextComplaint",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchFreeTextComplaint(@RequestBody ComplaintAppointmentMapperDto complaintAppointmentMapperDto) {
		try
		{
			return iComplaintAppointmentMapperService.searchFreeTextComplaint(complaintAppointmentMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
