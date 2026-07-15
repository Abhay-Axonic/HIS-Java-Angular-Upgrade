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
import com.param.adt.discharge.service.IDueForDischargeService;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DischargeDto;
import com.param.global.utility.ErrorCorrelationUtil;
import org.apache.log4j.Logger;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DueForDischargeController implements ICommonConstants
{
	final static Logger logger = Logger.getLogger(DueForDischargeController.class);

	@Autowired
	IDueForDischargeService iDueForDischargeService;

	@RequestMapping(value = "/saveDischargeRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDischargeRequest(@RequestBody DischargeDto dischargeDto) {
		try {

			return iDueForDischargeService.saveDischargeRequest(dischargeDto);

		} catch (Exception e) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "saveDischargeRequest failed"), e);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/getDischagePatientList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDischagePatientList(@RequestBody AdmissionDto admissionDto) {
		try {
			
			return iDueForDischargeService.getDischagePatientList(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargePdd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePDD(@RequestBody DischargeDto dischargeDto) {
		try {
			return iDueForDischargeService.updatePDD(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/cancelDischarge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelDischarge(@RequestBody DischargeDto dischargeDto) {
		try {
			return iDueForDischargeService.cancelDischarge(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getPatientDischargeOrNot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDischargeOrNot(@RequestBody DischargeDto dischargeDto) {
		try {
			
			return iDueForDischargeService.getPatientDischargeOrNot(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/markDischargeFromCasesheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response markDischargeFromCasesheet(@RequestBody DischargeDto dischargeDto) {
		try {
			return iDueForDischargeService.markDischargeFromCasesheet(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
}
