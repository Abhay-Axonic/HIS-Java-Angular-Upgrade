package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.service.IHemodialysisService;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/hemodialysis")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HemodialysisController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(HemodialysisController.class);

	
	@Autowired
	IHemodialysisService iHemodialysisService;
	
	@RequestMapping(value="/scheduleDialysisAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response scheduleDialysisAppointment(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iHemodialysisService.scheduleDialysisAppointment(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientListForDialysisWorkload", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getPatientListForDialysisWorkload(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			return iHemodialysisService.getPatientListForDialysisWorkload(hemodialysisAppointmentMapperDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/processDialysisRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response processDialysisRequest(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			return iHemodialysisService.saveDialysisRequest(hemodialysisAppointmentMapperDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getDialysisMachines", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getDialysisMachines(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			return iHemodialysisService.getDialysisMachines(hemodialysisAppointmentMapperDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/savePatientDialysisRecord", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response savePatientDialysisRecord(@RequestBody PatientDialysisRecordDto patientDialysisRecordDto) {
		try {
			return iHemodialysisService.savePatientDialysisRecord(patientDialysisRecordDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/savePatientDialysisRecord1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		Response<PatientDialysisRecordDto, Integer> response = null;
		try {
			JsonCheck.mapJsonToObject(sampleMasterDto, PatientDialysisRecordDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/uploadDialysisDocument", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	public @ResponseBody Response uploadDialysisDocument(@ModelAttribute  PatientDialysisRecordDto patientDialysisRecordDto) {
		try {
			return iHemodialysisService.uploadDialysisDocument(patientDialysisRecordDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value="/getPatientDialysisRecordDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getPatientDialysisRecordDetails(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			return iHemodialysisService.getPatientDialysisRecordDetails(hemodialysisAppointmentMapperDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/searchPatientForDialysis", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response searchPatientForDialysis(@RequestBody HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			return iHemodialysisService.searchPatientForDialysis(hemodialysisAppointmentMapperDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateDialysisStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response updateDialysisStatus(@RequestBody PatientDialysisRecordDto patientDialysisRecordDto) {
		try {
			return iHemodialysisService.updateDialysisStatus(patientDialysisRecordDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
