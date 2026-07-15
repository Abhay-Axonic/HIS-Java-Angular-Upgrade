package com.param.opd.coversheet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.ActionMasterDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.service.IConsultDoctorService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : Consult Order
DATE:16/04/2018
CREATED BY: TUSHAR*/
@Controller
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConsultDoctorController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ConsultDoctorController.class);

	
	@Autowired 
	private IConsultDoctorService iConsultDoctorService;
	
	@RequestMapping(value="/consultDoctor", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveConsultDoctor(@RequestBody List<ConsultDoctorDto> ConsultDoctorDto) {
		try {
			return iConsultDoctorService.saveConsultDoctor(ConsultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/consultDoctorListByEncounter", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consultDoctorListByEncounter(@RequestBody ConsultDoctorDto ConsultDoctorDto) {
		try {
			return iConsultDoctorService.consultDoctorListByEncounter(ConsultDoctorDto.getEncounterId());
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/inactiveConsultDoctor", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response inactiveConsultDoctor(@RequestBody ConsultDoctorDto ConsultDoctorDto) {
		try {
			return iConsultDoctorService.inactiveConsultDoctor(ConsultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getConsultationPatientListForDocDesk", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsultationPatientListForDocDesk(@RequestBody ConsultDoctorDto consultDoctorDto) {
		try {
			return iConsultDoctorService.getConsultationPatientListForDocDesk(consultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/consultDoctorListByAdmission", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consultDoctorListByAdmission(@RequestBody ConsultDoctorDto consultDoctorDto) {
		try {
			return iConsultDoctorService.consultDoctorListByAdmission(consultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateReferredStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateReferredStatus(@RequestBody ConsultDoctorDto consultDoctorDto){
		
		try {
			return iConsultDoctorService.updateReferredStatus(consultDoctorDto);
			
		} catch (Exception e) {
		logger.error("method failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/consultDoctorListForBookView", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consultDoctorListForBookView(@RequestBody ConsultDoctorDto ConsultDoctorDto) {
		try {
			return iConsultDoctorService.consultDoctorListForBookView(ConsultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateConsultDoctorNotes", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateConsultDoctorNotes(@RequestBody ConsultDoctorDto ConsultDoctorDto) {
		try {
			return iConsultDoctorService.updateConsultDoctorNotes(ConsultDoctorDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
