package com.param.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.PatientSponsorForCostEstimationDto;
import com.param.global.service.IPatientSponsorDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("api/opd")
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientSponsorDetailsController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientSponsorDetailsController.class);

	
	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;
	
	@RequestMapping(value = "/addPatientSponsorDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addPatientSponsorDetails(@RequestBody  List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList) {
		try {
			
			int encounterId=patientSponsorDetailsDtosList.getFirst().getEncounterId();
			return iPatientSponsorDetailsService.savePatientSponsorDetailsAndUpdatePatientSponsorUpdate(patientSponsorDetailsDtosList, encounterId);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getSponsoredDetails/{admissionId}/{paymentEntitlementId}", method = RequestMethod.GET)
	public @ResponseBody Response getSponsoredDetails(@PathVariable(value = "admissionId") Integer admissionId,@PathVariable(value = "paymentEntitlementId") Integer paymentEntitlementId) {
		try {
			
			return iPatientSponsorDetailsService.getSponsoredDetails(admissionId, paymentEntitlementId);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/sponsoredDetails/admission/{admissionId}/encounter/{encounterId}", method = RequestMethod.GET)
	public @ResponseBody Response getSponsoredDetailsForUpdatePayee(@PathVariable(value = "admissionId") Integer admissionId,@PathVariable(value = "encounterId") Integer encounterId) {
		try {
			return iPatientSponsorDetailsService.getSponsoredDetailsForUpdatePayee(admissionId,encounterId);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/addPatientSponsorDetails/costEstimation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addPatientSponsorDetailsForCostEstimation(@RequestBody PatientSponsorForCostEstimationDto dto) {
		try {
			Response res = iPatientSponsorDetailsService.savePatientSponsorDetailsForCostEstimation(dto);
			
			if(dto.getVisitTypeId()!=OP)
			{
				Response res2 = iPatientSponsorDetailsService.calculateAutoRenderForCostEstimation((PatientSponsorForCostEstimationDto)res.getObject());
				return new Response(res.getStatus(),res.getCode(),res.getMessage(),res2.getListObject(),null);
			}else{
				return new Response(res.getStatus(),res.getCode(),res.getMessage(),null,null);
			}

		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getAutRenderServices/costEstimation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAutRenderServices(@RequestBody PatientSponsorForCostEstimationDto dto) {
		try {
			return iPatientSponsorDetailsService.calculateAutoRenderForCostEstimation(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/lastSponsors/{patientId}/{visitTypeId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getLastSponsorsByPatientId(@PathVariable(value = "patientId") Integer patientId,@PathVariable(value = "visitTypeId") Integer visitTypeId,@PathVariable(value = "unitId") Integer unitId) {
		try {
			return iPatientSponsorDetailsService.getLastSponsorsByPatientId(patientId,visitTypeId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/updatePatientSponserDetailsClaimNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientSponserDetailsClaimNumber(@RequestBody PatientSponsorDetailsDto patientSponsorDetailsDto) {
		try {
			return iPatientSponsorDetailsService.updatePatientSponserDetailsClaimNumber(patientSponsorDetailsDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}
	
}
