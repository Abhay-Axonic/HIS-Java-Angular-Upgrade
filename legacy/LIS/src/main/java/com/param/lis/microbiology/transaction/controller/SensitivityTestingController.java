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
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.MicrobioResultEntryMasterDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultMasterDto;
import com.param.lis.microbiology.transaction.service.ISensitivityTestingService;
import com.param.utility.TimeZoneComponent;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/Microbiology")
public class SensitivityTestingController  implements ICommonConstants, IError {
	
	@Autowired
	private ISensitivityTestingService iSensitivityTestingService;

	 @Autowired
		private ISmsUtilityConfigService smsUtilityService;
	  
	final static Logger logger = Logger.getLogger(SensitivityTestingController.class);
	
	@RequestMapping(value = "/SensitivityTesting/observation/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSensitivityTestingList(
			@RequestBody MicrobioParamDto microbioParamDto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iSensitivityTestingService.getSensitivityTestingList(microbioParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/SensitivityTesting/observation/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSensitivityTestingListCount(@RequestBody MicrobioParamDto microbioParamDto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iSensitivityTestingService.getSensitivityTestingListCount(microbioParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/SensitivityTesting/observation/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sensitivityTestingListListSearch(@RequestBody SearchDto dto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iSensitivityTestingService.sensitivityTestingListListSearch(dto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/SensitivityTesting/observation/details", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSensitivityTestingDetails(@RequestBody MicrobioParamDto microbioParamDto)
	{
		Response<MicrobioResultEntryMasterDto, Integer> response = null;
		try
		{
			response = iSensitivityTestingService.getSensitivityTestingDetails(microbioParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/sensitivityTesting/observation/{labSampleStausId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSensitivityTestingDuringObservation(@RequestBody SensitivityTestResultMasterDto   listSensitivityTestResultMasterDto,@PathVariable(value = "labSampleStausId") Integer labSampleStausId) throws Exception
	{
		Response<SensitivityTestResultMasterDto, Integer> response = null;
		try
		{
			if(listSensitivityTestResultMasterDto.getAmend() != null && listSensitivityTestResultMasterDto.getAmend() == true){
				 iSensitivityTestingService.updateAmendLogs(listSensitivityTestResultMasterDto);
			}
			 
	        response = iSensitivityTestingService.saveSensitivityTestingObservation(listSensitivityTestResultMasterDto,labSampleStausId);
	        
	        if(labSampleStausId== 8) { // report Release
	        //send mail
            if(listSensitivityTestResultMasterDto.getTriggerReleaseMail()!=null && listSensitivityTestResultMasterDto.getTriggerReleaseMail()) {
           	 String pattern = "yyyy-MM-dd HH:mm:ss";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(listSensitivityTestResultMasterDto.getOrgUnitId()));
					//mail 
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					emailinfo.setLabSampleDtlsId( listSensitivityTestResultMasterDto.getLabSampleDtlsId());
					emailinfo.setPatientId(listSensitivityTestResultMasterDto.getPatientId());
					if(listSensitivityTestResultMasterDto.getDoctorId()!=null) {
						emailinfo.setConsultingDoctorId(listSensitivityTestResultMasterDto.getDoctorId());
						emailinfo.setAdmitingDoctorId(listSensitivityTestResultMasterDto.getDoctorId());
						emailinfo.setReferringDoctorId(listSensitivityTestResultMasterDto.getDoctorId());
						emailinfo.setTreatingDoctorId(listSensitivityTestResultMasterDto.getDoctorId());
					}	
					emailinfo.setUnitId(listSensitivityTestResultMasterDto.getOrgUnitId());
					emailinfo.setOrganizationId(listSensitivityTestResultMasterDto.getOrgId());
					emailinfo.setDateTime(date1);
					emailinfo.setTestDesc(listSensitivityTestResultMasterDto.getTestDesc());
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
	
	@RequestMapping(value = "/sensitivityTesting/getSensitivityDetails/{labSampleDtlsId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public @ResponseBody Response getSensitivityDetails(@PathVariable(value = "labSampleDtlsId") Integer labSampleDtlsId,
	    @PathVariable(value = "orgId") Integer orgId,
	    @PathVariable(value = "orgUnitId") Integer orgUnitId)
	{
		Response<SensitivityTestResultMasterDto, Integer> response = null;
		try
		{
			response = iSensitivityTestingService.getSensitivityDetails(labSampleDtlsId,orgId,orgUnitId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/sensitivityTesting/removeSensitivityTestResultSubDetails/{sesitivityResultSubDetailsId}/{orgId}/{orgUnitId}", method = RequestMethod.DELETE)
	public @ResponseBody Response removeSensitivityTestResultSubDetails (@PathVariable(value = "sesitivityResultSubDetailsId") Integer sesitivityResultSubDetailsId,
			@PathVariable (value = "orgId") Integer orgId,
			@PathVariable (value = "orgUnitId") Integer orgUnitId) 	
	{
		Response response = null;
		try {
			
			response = iSensitivityTestingService.removeSensitivityTestResultSubDetails(sesitivityResultSubDetailsId,orgId,orgUnitId);
			
		} catch (Exception e) {
			 logger.error("Exception", e);
			 throw e;
		} 
		
	  	return response;
	}
	
	@RequestMapping(value = "/sensitivityTesting/removeSensitivityTestResultDetails/{sesitivityResultDetailsId}/{sensitivityResultId}/{orgId}/{orgUnitId}", method = RequestMethod.DELETE)
	public @ResponseBody Response removeSensitivityTestResultDetails (
			@PathVariable Integer sesitivityResultDetailsId, 
			@PathVariable Integer sensitivityResultId,
			@PathVariable Integer orgId,
			@PathVariable   Integer orgUnitId) {
		try {
			
			 return iSensitivityTestingService.removeSensitivityTestResultDetails(sesitivityResultDetailsId,sensitivityResultId,orgId,orgUnitId);
			
		} catch (Exception e){
            logger.error("Exception in removeSensitivityTestResultDetails", e);
            throw new ServiceException(e.getMessage());
		}
	}
	

}
