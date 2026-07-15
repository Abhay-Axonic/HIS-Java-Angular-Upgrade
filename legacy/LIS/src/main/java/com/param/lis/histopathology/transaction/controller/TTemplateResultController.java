package com.param.lis.histopathology.transaction.controller;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ISmsUtilityConstants;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.histopathology.transaction.service.ITTemplateResultService;
import com.param.utility.TimeZoneComponent;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Slide Creation Controller", tags = "TSlideMaster")
@SuppressWarnings({"rawtypes", "unchecked"})
@RequestMapping(value = "/LIS/histopathology")
public class TTemplateResultController implements ICommonConstants, IError 
{
   final Logger logger = Logger.getLogger(TTemplateResultController.class);

  @Autowired
  private ITTemplateResultService iTTemplateResultService;

  @Autowired
	private ISmsUtilityConfigService smsUtilityService;

  @RequestMapping(value = "/saveTemplateResult", method = RequestMethod.POST)
  public @ResponseBody Response createSlides(
      @RequestBody TTemplateResultDto tTemplateResultDto) throws Exception
  {
    Response<TTemplateResultDto, Integer> response = null;
    try 
    {
      response = iTTemplateResultService.saveTemplateResult(tTemplateResultDto);
      
      if(tTemplateResultDto.getSampleStatusId() == 8) {
    //send mail
      if(tTemplateResultDto.getTriggerReleaseMail()!=null && tTemplateResultDto.getTriggerReleaseMail()) {
     	 String pattern = "yyyy-MM-dd HH:mm:ss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(tTemplateResultDto.getOrgUnitId()));
				//mail 
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setLabSampleDtlsId( tTemplateResultDto.getLabSampleDtlsId());
				emailinfo.setPatientId(tTemplateResultDto.getPatientId());
				if(tTemplateResultDto.getDoctorId()!=null) {
					emailinfo.setConsultingDoctorId(tTemplateResultDto.getDoctorId());
					emailinfo.setAdmitingDoctorId(tTemplateResultDto.getDoctorId());
					emailinfo.setReferringDoctorId(tTemplateResultDto.getDoctorId());
					emailinfo.setTreatingDoctorId(tTemplateResultDto.getDoctorId());
				}
				emailinfo.setUnitId(tTemplateResultDto.getOrgUnitId());
				emailinfo.setOrganizationId(tTemplateResultDto.getOrgId());
				emailinfo.setDateTime(date1);
				emailinfo.setTestDesc(tTemplateResultDto.getTestDesc());
				emailinfo.setRptFileName("GetHistopathologyReport_GroupPrint.rpt");
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PATHOLOGY_REPORT, emailinfo);
      	}
      }
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }
  
  @RequestMapping(value = "/getReport/{templateResId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
  public @ResponseBody Response getHistopathReport(@PathVariable(value = "templateResId") Integer templateResId,
      @PathVariable(value = "orgId") Integer orgId,
      @PathVariable(value = "orgUnitId") Integer orgUnitId)
  {
    Response<TTemplateResultDto, Integer> response = null;
    try 
    {
      response = iTTemplateResultService.getHistopathReport(templateResId,orgId,orgUnitId);
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }
  
  @RequestMapping(value = "/getReportDetails/{labSampleDtlsId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
  public @ResponseBody Response getHistopathReportDetails(@PathVariable(value = "labSampleDtlsId") Integer labSampleDtlsId,
		  @PathVariable(value = "orgId") Integer orgId,
	      @PathVariable(value = "orgUnitId") Integer orgUnitId)
  {
	  try{
	  Response response = iTTemplateResultService.getHistopathReportDetails(labSampleDtlsId,orgId,orgUnitId);
	  return response;
  }catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }
  
}
