package com.param.adt.discharge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.service.IMarkedForDischargeService;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DischargeDto;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MarkedForDischargeController implements ICommonConstants
{
	@Autowired
	IMarkedForDischargeService iMarkedForDischargeService;
	
	
	@RequestMapping(value = "/getDischargePatientListForNursing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischagePatientListForNursing(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getDischagePatientListForNursing(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updatePatientServiceOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientServiceOrder(@RequestBody AdmissionDto admissionDto) {
		try {
			
			return iMarkedForDischargeService.updatePatientServiceOrder(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientReadyForBillingList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientReadyForBillingList(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getPatientReadyForBillingList(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientFinalDischargeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientFinalDischargeList(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getPatientFinalDischargeList(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updatePatientReadyForBillingStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientReadyForBillingStatus(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.updatePatientReadyForBillingStatus(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/vacateBed", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response vacateBed(@RequestBody DischargeDto dischargeDto, HttpServletRequest request) {
		try {
			
			return iMarkedForDischargeService.vacateBed(dischargeDto, request);

		} catch (Exception e) {
			
		
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischagePatientListByDoctorId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischagePatientListByDoctorId(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getDischagePatientListByDoctorId(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getPatientDischargedList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDischargedList(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getPatientDischargedList(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientDischargedListForPrint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDischargedListForPrint(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getPatientDischargedListForPrint(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientFinalDischargeListNewForListAndCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientFinalDischargeListNewForListAndCount(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getPatientFinalDischargeListNewForListAndCount(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargePatientListForNursingByVisitTypeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischargePatientListForNursingByVisitTypeId(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iMarkedForDischargeService.getDischargePatientListForNursingByVisitTypeId(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargedPatientsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischargedPatientsList(@RequestBody DischargeDto dischargeDto) {
		try {
			return iMarkedForDischargeService.getDischargedPatientsList(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
