package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;
import com.param.opd.coversheet.dto.EmergencyDiagnosisMasterDto;
import com.param.opd.coversheet.service.IDiagnosisMapperService;
import com.param.opd.coversheet.service.IEmergencyDiagnosisMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/opd")
public class EmergencyDiagnosisMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(EmergencyDiagnosisMasterController.class);

	@Autowired
	IEmergencyDiagnosisMasterService iEmergencyDiagnosisMasterService;
	IDiagnosisMapperService iDiagnosisMapper;

	@RequestMapping(value = "/saveEmergencyDiagnosisMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveEmergencyDiagnosisMaster(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.saveEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getListOfEmergencyDiagnosisMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfEmergencyDiagnosisMaster(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.getListOfEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateEmergencyDiagnosisMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmergencyDiagnosisMasterStatus(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.updateEmergencyDiagnosisMasterStatus(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getEmergencyDiagnosisMasterById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEmergencyDiagnosisMasterById(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.getEmergencyDiagnosisMasterById(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateEmergencyDiagnosisMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmergencyDiagnosisMaster(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.updateEmergencyDiagnosisMaster(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getEmergencyDiagnosisMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEmergencyDiagnosisMasterCount(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.getEmergencyDiagnosisMasterCount(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveEmergencyDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveEmergencyDiagnosis(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.getActiveEmergencyDiagnosis(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/doorToNiddle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataForDoorToNiddle(@RequestBody EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			return iEmergencyDiagnosisMasterService.getDataForDoorToNiddle(emergencyDiagnosisMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
