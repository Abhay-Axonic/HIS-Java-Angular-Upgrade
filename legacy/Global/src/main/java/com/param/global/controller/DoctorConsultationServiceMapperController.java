package com.param.global.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorConsChargesForMyLife;
import com.param.global.dto.DoctorConsultationServiceMapperDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IDoctorConsultationServiceMapperService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

/**
 * 
 * @author Kaustubh
 * Controller for DoctorConsultationServiceMapper
 */
@RestController
@RequestMapping(value="api/billing/unit")
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorConsultationServiceMapperController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoctorConsultationServiceMapperController.class);

	@Autowired
	IDoctorConsultationServiceMapperService iDoctorConsultationServiceMapperService;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@RequestMapping(value="/doctorConsultationServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorConsultationServiceMapperService(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.saveDoctorConsultationServiceMapperService(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while saving the doctor consultation service mapper", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDoctorConsultationServiceMapperList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorConsultationServiceMapperList(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.getDoctorConsultationServiceMapperList(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor consultation service mapper list", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateDoctorConsultationServiceMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorConsultationServiceMapper(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.updateDoctorConsultationServiceMapper(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while updating the doctor consultation service mapper", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDoctorConsultationServiceMapperById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorConsultationServiceMapperById(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.getDoctorConsultationServiceMapperById(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor consultation service mapper by id", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateDoctorConsultationServiceMapperStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorConsultationServiceMapperStatus(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.updateDoctorConsultationServiceMapperStatus(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while updating the doctor consultation service mapper status", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDoctorConsultationServiceBySpecialityAndDoctorId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorConsultationServiceBySpecialityAndDoctorId(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorIdTransactional(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor consultation service by speciality and doctor id", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDoctorConsultationServiceMapperListCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorConsultationServiceMapperListCount(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.getDoctorConsultationServiceMapperListCount(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor consultation service mapper list count", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/doctorCon/checkAlreadyExistOrNot" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response doctorConCheckAlreadyExistOrNot(@RequestBody DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto){
		try{
			return iDoctorConsultationServiceMapperService.doctorConCheckAlreadyExistOrNot(doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while checking if the doctor consultation service mapper already exists", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDoctorCharges" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCharges(@RequestBody DoctorConsChargesForMyLife doctorConsultationServiceMapperDto){
		try{
			Date currDate=TimeZoneComponent.getDateByzone(doctorConsultationServiceMapperDto.getUnitId());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    String strDate= formatter.format(currDate); 
			EncounterMasterDto encounterDto=new EncounterMasterDto();
			encounterDto.setDoctorId(doctorConsultationServiceMapperDto.getLocalDoctorId());
			encounterDto.setSpecialityId(doctorConsultationServiceMapperDto.getSpecialityId());
			encounterDto.setOrganizationId(doctorConsultationServiceMapperDto.getHospitalId());
			encounterDto.setUnitId(doctorConsultationServiceMapperDto.getUnitId());
			encounterDto.setPatientId(doctorConsultationServiceMapperDto.getPatientId());
			encounterDto.setDeptWiseAppointment('N');
			encounterDto.setCurrentDate(strDate);
			Response doctorChargesRes=iEncounterMasterService.billAmountForKiosk(encounterDto);
			ContractConsumptionDto consumptionDto=(ContractConsumptionDto)doctorChargesRes.getObject();
			doctorConsultationServiceMapperDto.setCharges(consumptionDto.getFinalPrice());
			doctorConsultationServiceMapperDto.setVisitTypeId(consumptionDto.getSystemDefiedVisitType());
			doctorConsultationServiceMapperDto.setPatientCategoryId(consumptionDto.getPatientCategoryId());
			//return iDoctorConsultationServiceMapperService.getDoctorCharges(doctorConsultationServiceMapperDto);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, doctorConsultationServiceMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the doctor charges", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

}
