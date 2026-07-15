package com.param.lis.microbiology.transaction.controller;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ISmsUtilityConstants;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.MicrobioResultDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.MicrobioResultEntryMasterDto;
import com.param.lis.microbiology.transaction.dto.TMicroIncubationMasterDto;
import com.param.lis.microbiology.transaction.service.IincubationObservationService;
import com.param.utility.TimeZoneComponent;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/Microbiology")
public class IncubationObservationController  implements ICommonConstants, IError {
	
	@Autowired
	private IincubationObservationService iIncubationObservationService;

	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	final static Logger logger = Logger.getLogger(IncubationObservationController.class);
	
	@RequestMapping(value = "/incubation/observation/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIncubationObservationList(
			@RequestBody MicrobioParamDto microbioParamDto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iIncubationObservationService.getIncubationObservationList(microbioParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/incubation/observation/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIncubationObservationListCount(@RequestBody MicrobioParamDto microbioParamDto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iIncubationObservationService.getIncubationObservationListCount(microbioParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/incubation/observation/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIncubationObservationListSearch(@RequestBody SearchDto dto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iIncubationObservationService.getIncubationObservationListSearch(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	
	
	  @RequestMapping(value = "/start/incubation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody Response saveIncubationTrasaction(
	            @RequestBody TMicroIncubationMasterDto tMicroIncubationMasterDto) throws Exception
	    {
	        Response<MicrobioResultDetailsMasterDto, Integer> response = null;
	        try
	        {
	            response = iIncubationObservationService.saveIncubationTrasaction(tMicroIncubationMasterDto);
	            
	            if(tMicroIncubationMasterDto.getIsCompleted().equals('Y') && tMicroIncubationMasterDto.getCultureTest() != null && tMicroIncubationMasterDto.getCultureTest()== true) { // report Release
	    	        //send mail
	                if(tMicroIncubationMasterDto.getTriggerReleaseMail()!=null && tMicroIncubationMasterDto.getTriggerReleaseMail()) {
	               	 String pattern = "yyyy-MM-dd HH:mm:ss";
	    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    					String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(tMicroIncubationMasterDto.getOrgUnitId()));
	    					//mail 
	    					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
	    					emailinfo.setLabSampleDtlsId( tMicroIncubationMasterDto.getLabSampleDtlsId());
	    					emailinfo.setPatientId(tMicroIncubationMasterDto.getPatientId());
	    					if(tMicroIncubationMasterDto.getDoctorId()!=null) {
	    						emailinfo.setConsultingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setAdmitingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setReferringDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setTreatingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    					}	
	    					emailinfo.setUnitId(tMicroIncubationMasterDto.getOrgUnitId());
	    					emailinfo.setOrganizationId(tMicroIncubationMasterDto.getOrgId());
	    					emailinfo.setDateTime(date1);
	    					emailinfo.setTestDesc(tMicroIncubationMasterDto.getTestDesc());
	    					emailinfo.setRptFileName("pathologyReport_Sensitivity_GroupPrint.rpt");
	    					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
	                }
	            } else if(tMicroIncubationMasterDto.getPartially().equals('Y') && tMicroIncubationMasterDto.getDoctorId()!= null){
	            	
	            	   if(tMicroIncubationMasterDto.getTriggerReleaseMail()!=null && tMicroIncubationMasterDto.getTriggerReleaseMail()) {
	  	               	 String pattern = "yyyy-MM-dd HH:mm:ss";
	  	    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	  	    					String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(tMicroIncubationMasterDto.getOrgUnitId()));
	  	    					//mail 
	  	    					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
	  	    					emailinfo.setLabSampleDtlsId( tMicroIncubationMasterDto.getLabSampleDtlsId());
	  	    					emailinfo.setPatientId(tMicroIncubationMasterDto.getPatientId());
	  	    					if(tMicroIncubationMasterDto.getDoctorId()!=null) {
	  	    						emailinfo.setConsultingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setAdmitingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setReferringDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setTreatingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    					}	
	  	    					emailinfo.setUnitId(tMicroIncubationMasterDto.getOrgUnitId());
	  	    					emailinfo.setOrganizationId(tMicroIncubationMasterDto.getOrgId());
	  	    					emailinfo.setDateTime(date1);
	  	    					emailinfo.setTestDesc(tMicroIncubationMasterDto.getTestDesc());
	  	    					emailinfo.setRptFileName("pathologyReport_Sensitivity_GroupPrint.rpt");
	  	    					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
	  	                }
	            }
	            
	            return response;
	        } catch (Exception e)
	        {
	            logger.error("Exception", e);
	            throw e;
	        }
	    }
	  
	  @RequestMapping(value = "/restart/incubation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
      public @ResponseBody Response updateIncubationTransaction(
              @RequestBody TMicroIncubationMasterDto tMicroIncubationMasterDto) throws Exception
      {
          Response<MicrobioResultDetailsMasterDto, Integer> response = null;
          try
          {
             
        	  response = iIncubationObservationService.updateIncubationTransaction(tMicroIncubationMasterDto);
        	  
        	  
	            if(tMicroIncubationMasterDto.getIsCompleted().equals('Y') && tMicroIncubationMasterDto.getCultureTest() != null && tMicroIncubationMasterDto.getCultureTest()== true) { // report Release
	    	        //send mail
	                if(tMicroIncubationMasterDto.getTriggerReleaseMail()!=null && tMicroIncubationMasterDto.getTriggerReleaseMail()) {
	               	 String pattern = "yyyy-MM-dd HH:mm:ss";
	    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    					String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(tMicroIncubationMasterDto.getOrgUnitId()));
	    					//mail 
	    					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
	    					emailinfo.setLabSampleDtlsId( tMicroIncubationMasterDto.getLabSampleDtlsId());
	    					emailinfo.setPatientId(tMicroIncubationMasterDto.getPatientId());
	    					if(tMicroIncubationMasterDto.getDoctorId()!=null) {
	    						emailinfo.setConsultingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setAdmitingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setReferringDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    						emailinfo.setTreatingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	    					}	
	    					emailinfo.setUnitId(tMicroIncubationMasterDto.getOrgUnitId());
	    					emailinfo.setOrganizationId(tMicroIncubationMasterDto.getOrgId());
	    					emailinfo.setDateTime(date1);
	    					emailinfo.setTestDesc(tMicroIncubationMasterDto.getTestDesc());
	    					emailinfo.setRptFileName("pathologyReport_Sensitivity_GroupPrint.rpt");
	    					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
	                }
	            }else if(tMicroIncubationMasterDto.getPartially().equals('Y') && tMicroIncubationMasterDto.getDoctorId()!= null){
	            	
	            	   if(tMicroIncubationMasterDto.getTriggerReleaseMail()!=null && tMicroIncubationMasterDto.getTriggerReleaseMail()) {
	  	               	 String pattern = "yyyy-MM-dd HH:mm:ss";
	  	    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	  	    					String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(tMicroIncubationMasterDto.getOrgUnitId()));
	  	    					//mail 
	  	    					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
	  	    					emailinfo.setLabSampleDtlsId( tMicroIncubationMasterDto.getLabSampleDtlsId());
	  	    					emailinfo.setPatientId(tMicroIncubationMasterDto.getPatientId());
	  	    					if(tMicroIncubationMasterDto.getDoctorId()!=null) {
	  	    						emailinfo.setConsultingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setAdmitingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setReferringDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    						emailinfo.setTreatingDoctorId(tMicroIncubationMasterDto.getDoctorId());
	  	    					}	
	  	    					emailinfo.setUnitId(tMicroIncubationMasterDto.getOrgUnitId());
	  	    					emailinfo.setOrganizationId(tMicroIncubationMasterDto.getOrgId());
	  	    					emailinfo.setDateTime(date1);
	  	    					emailinfo.setTestDesc(tMicroIncubationMasterDto.getTestDesc());
	  	    					emailinfo.setRptFileName("pathologyReport_Sensitivity_GroupPrint.rpt");
	  	    					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
	  	                }
	            }
        	  
              return response;
          } catch (Exception e)
          {
              logger.error("Exception", e);
              throw e;
          }
      }
	  
	  
	
	@RequestMapping(value = "/incubation/details/{tIncubationId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public @ResponseBody Response getIncubationDetails(@PathVariable(value="tIncubationId") Integer tIncubationId,
	    @PathVariable(value="orgId") Integer orgId,
	    @PathVariable(value="orgUnitId") Integer orgUnitId)
	{
		Response<MicrobioResultDetailsMasterDto, Integer> response = null;
		try
		{
			response = iIncubationObservationService.getIncubationDetails(tIncubationId,orgId,orgUnitId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

}
