package com.param.ipd.controller;

import com.param.ipd.dto.SmartDocumentSummary;
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
import com.param.ipd.dto.ComplaintAdmissionDto;
import com.param.ipd.service.IComplaintAdmissionService;

@RestController
@RequestMapping(value="/api/ipd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComplaintAdmissionController implements ICommonConstants{
	
	@Autowired
	private IComplaintAdmissionService iComplaintAdmissionService;
	
	
	@RequestMapping(value="/getComplaintAdmission", method=RequestMethod.GET)
	public @ResponseBody Response getComplaintAdmission(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="admissionId") int admissionId,@RequestParam(value="roleId") int roleId) {
		try
		{
			ComplaintAdmissionDto complaintAdmissionDto= new ComplaintAdmissionDto();
			complaintAdmissionDto.setUnitId(unitId);
			complaintAdmissionDto.setOrganizationId(organizationId);
			complaintAdmissionDto.setAdmissionId(admissionId);
			complaintAdmissionDto.setRoleId(roleId);
			return iComplaintAdmissionService.getComplaintAdmission(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveComplaintAdmission", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveComplaintAdmission(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody ComplaintAdmissionDto complaintAdmissionDto) {
		try
		{
			complaintAdmissionDto.setUnitId(unitId);
			complaintAdmissionDto.setOrganizationId(organizationId);
			return iComplaintAdmissionService.saveComplaintAdmission(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/editComplaintAdmission", method=RequestMethod.GET)
	public @ResponseBody Response editComplaintAppointment(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="complaintAdmissionId") int complaintAdmissionId) {
		try
		{
			ComplaintAdmissionDto complaintAdmissionDto= new ComplaintAdmissionDto();
			complaintAdmissionDto.setUnitId(unitId);
			complaintAdmissionDto.setOrganizationId(organizationId);
			complaintAdmissionDto.setComplaintAdmissionId(complaintAdmissionId);
			return iComplaintAdmissionService.editComplaintAppointment(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateComplaintAdmission", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateComplaintAdmission(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody ComplaintAdmissionDto complaintAdmissionDto) {
		try
		{
			complaintAdmissionDto.setUnitId(unitId);
			complaintAdmissionDto.setOrganizationId(organizationId);
			return iComplaintAdmissionService.updateComplaintAdmission(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/updateComplaintAdmissionStatusToRemoveComplaint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateComplaintAdmissionStatusToRemoveComplaint(@RequestBody ComplaintAdmissionDto complaintAdmissionDto){
		
		try {
			return iComplaintAdmissionService.updateComplaintAdmissionStatusToRemoveComplaint(complaintAdmissionDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	/*@RequestMapping(value="/getComplaintAdmissionByPatientId", method=RequestMethod.GET)
	public @ResponseBody Response getComplaintAdmissionByPatientId(@RequestHeader(value="organization_id") int organizationId,
			@RequestParam(value="patientId") int patientId) {
		try
		{
			ComplaintAdmissionDto complaintAdmissionDto= new ComplaintAdmissionDto();
			complaintAdmissionDto.setOrganizationId(organizationId);
			complaintAdmissionDto.setPatientId(patientId);
			return iComplaintAdmissionService.getComplaintAppointmentByPatientId(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	@RequestMapping(value="/saveResolvedComplaintAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveResolvedComplaintAdmission(@RequestBody ComplaintAdmissionDto complaintAdmissionDto) {
		try
		{
			return iComplaintAdmissionService.saveResolvedComplaintAdmission(complaintAdmissionDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getComplaintAdmission/getDataForIPDCoversheetByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody ComplaintAdmissionDto dto) {
		try
		{
			return iComplaintAdmissionService.getActiveComplaintsForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getComplaintAdmission/getUniqueDateFromComplaintAdm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody ComplaintAdmissionDto dto) {
		try
		{
			return iComplaintAdmissionService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/generateSummaryDocument", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response generateSummaryDocument(@RequestBody ComplaintAdmissionDto dto) {
		try
		{
			return iComplaintAdmissionService.generateSummaryDocument(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/saveMyLatestMedicalSummaryCallBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response saveMyLatestMedicalSummaryCallBack(@RequestBody SmartDocumentSummary dto) {
		try
		{
			System.out.println(" @@@@@@@  saveMyLatestMedicalSummaryCallBack controller called");

			return iComplaintAdmissionService.saveMyLatestMedicalSummaryCallBack(dto);



		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/getAISummaryData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getAISummaryData(@RequestBody ComplaintAdmissionDto dto) {
		try
		{

			return iComplaintAdmissionService.getAISummaryData(dto);



		}catch(Exception e)
		{

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
