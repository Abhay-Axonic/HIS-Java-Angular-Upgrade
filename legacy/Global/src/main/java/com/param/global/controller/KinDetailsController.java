package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.MortuaryDto;
import com.param.global.service.IKinDetailsService;

@RestController
@RequestMapping("api/global")
@SuppressWarnings({"rawtypes","unchecked"})
public class KinDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(KinDetailsController.class);

	@Autowired
	private IKinDetailsService iKinDetailsService;
	
	@RequestMapping(value="kinDetails/patient/{patientId}/unit/{unitId}/org/{orgId}", method=RequestMethod.GET)
	public Response getKinDetailsByPatientId(@PathVariable("patientId") Integer patientId,
											 @PathVariable("unitId") Integer unitId,
											 @PathVariable("orgId") Integer orgId) {
		try {
				return iKinDetailsService.getKinDetailsByPatientId(patientId,unitId,orgId);
		}catch (Exception e) {
			logger.error("Error while fetching kin details by patient id", e);
		}
		return new Response<>(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="kinDetails/{kinDetailsId}", method=RequestMethod.GET)
	public Response getKinDetailsByPatientId(@PathVariable("kinDetailsId") Integer kinDetailsId){
		try {
				return iKinDetailsService.getKinDetailsById(kinDetailsId);
		}catch (Exception e) {
			logger.error("Error while fetching kin details by id", e);
		}
		return new Response<>(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="kinDetails/{kinDetailsId}/status/{status}", method=RequestMethod.PUT)
	public Response changeKinDetailsStatus(@PathVariable("kinDetailsId") Integer kinDetailsId,
											 @PathVariable("status") Character status){
		try {
				return iKinDetailsService.changeKinDetailsStatus(kinDetailsId, status);
		}catch (Exception e) {
			logger.error("Error while changing kin details status", e);
		}
		return new Response<>(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/saveKinDeatilsForMortuaryAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveKinDeatilsForMortuaryAllocation(@RequestBody AdmissionDto admissionDto) {
		try {
				return iKinDetailsService.saveKinDetails(admissionDto);
		}catch (Exception e) {
			logger.error("Error while saving kin details for mortuary allocation", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getKinDeatilsForMortuaryAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getkinDeatilsForMortuaryAllocation(@RequestBody MortuaryDto mortuaryDto) {
		try {
				return iKinDetailsService.getkinDeatilsForMortuaryAllocation(mortuaryDto);
		}catch (Exception e) {
			logger.error("Error while fetching kin details for mortuary allocation", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateKinDeatilsForMortuaryAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKinDeatilsForMortuaryAllocation(@RequestBody AdmissionDto admissionDto) {
		try {
			return iKinDetailsService.updateKinDeatilsForMortuaryAllocation(admissionDto);
		}catch (Exception e) {
			logger.error("Error while updating kin details for mortuary allocation", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
