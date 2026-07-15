package com.param.global.opd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.service.IDoctorMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.IPrefixMasterService;
import com.param.opd.appointment.dto.BookAppointmentDto;

@RestController
@RequestMapping("api/webappointment")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WebAppointmentController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(WebAppointmentController.class);

	@Autowired
	private IDoctorMasterService iDoctorMasterService;
	
	@Autowired
	private IPrefixMasterService iPrefixMasterService;
	
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	
    @RequestMapping(value="/getDetailsForBookAppointment",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getDetailsForBookAppointment(@RequestBody BookAppointmentDto bookAppointmentDto) {
		try {
			return iDoctorMasterService.getDetailsForBookAppointment(bookAppointmentDto);
		}catch (Exception e) {
			logger.error("getDetailsForBookAppointment failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

    @RequestMapping(value="/getActivePrefixList/{organizationId}",  method=RequestMethod.GET)
   	public  @ResponseBody Response getActivePrefixList(@PathVariable ("organizationId") Integer organizationId ) {
   		try {
   			return iPrefixMasterService.getActivePrefixList(organizationId);
   		}catch (Exception e) {
   			logger.error("getActivePrefixList failed: " + e.getMessage(), e);
   		}
   		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
   	}
    
    @RequestMapping(value="/getPatientDetailsByUhid",  method=RequestMethod.GET)
	public  @ResponseBody Response getPatientDetailsByUhid(@RequestParam(value="uhidNumber") String uhidNumber,@RequestParam(value="orgId") Integer orgId) {
		try {
			PatientRegistrationDto dto=new PatientRegistrationDto();
			dto.setUhidNumber(uhidNumber);
			dto.setOrganizationId(orgId);
			//dto.setOrganizationId(orgId);
			return iPatientRegistrationService.getPatientDetailsByUhid(dto);
		}catch (Exception e) {
			logger.error("getPatientDetailsByUhid failed: " + e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
    
    
}
