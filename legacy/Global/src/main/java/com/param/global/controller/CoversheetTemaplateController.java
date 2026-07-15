package com.param.global.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.opd.coversheet.service.IAllergyPatientMapperService;
import com.param.opd.coversheet.service.ICoversheetTemaplateService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CoversheetTemaplateController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(CoversheetTemaplateController.class);

	@Autowired
	private ICoversheetTemaplateService iCoversheetTemaplateService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@RequestMapping(value="/saveCoversheetAsTemaplate", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCoversheetAsTemaplate(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.saveCoversheetAsTemaplate(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getCoversheetAsTemaplateList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCoversheetAsTemaplateList(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.getCoversheetAsTemaplateList(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getAllCoversheetListsByTemplateId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllCoversheetListsByTemplateId(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.getAllCoversheetListsByTemplateId(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateCoversheetAsTemaplate", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCoversheetAsTemaplate(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.updateCoversheetAsTemaplate(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveTemplateListDataForNewEMR", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTemplateListDataForNewEMR(@RequestBody CoversheetTemplateMasterDto dto) {
		try
		{
			CoversheetTemplateMasterDto CoversheetTemplateMasterDto = dto;
			return iCoversheetTemaplateService.saveTemplateListDataForNewEMR(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getTemplateDataByCovTempId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateDataByCovTempId(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.getTemplateDataByCovTempId(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCompleteConsultationReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCompleteConsultationReport(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(CoversheetTemplateMasterDto.getOrganizationId(),CoversheetTemplateMasterDto.getUnitId());
	       
	       if(API_URL==null)
	    	   return null;
	       
	       String reportParam=" {"
	               +"'appName': 'HIS',"
	               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	               +"'reportDesignFilePath': 'NewCoverSheetEasyEMR.rpt',"
	               +"'reportFormat': 'PDF',"
	               +"'exportFileName': 'Coversheet',"
	               +"'reportParams': ["
	                 +"{"
		               +"'paramName': 'Patientid',"
				       +"'paramType': 'Integer',"
				       +"'paramValue':'"+CoversheetTemplateMasterDto.getPatientId()+"'"
		              +"}," 
		              +"{"
		               +"'paramName': 'Unitid',"
				       +"'paramType': 'Integer',"
				       +"'paramValue':'"+CoversheetTemplateMasterDto.getUnitId()+"'"
		              +"}," 
		              +"{"
		               +"'paramName': 'Encounter_id',"
				       +"'paramType': 'Integer',"
				       +"'paramValue':"+CoversheetTemplateMasterDto.getEncounterId()
		              +"}" 
		              
	               +"]"
	               +"}" ;
	       

	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
	   logger.error("method failed", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	  }
	
	@RequestMapping(value="/saveCovTemplateDataForPatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCovTemplateDataForPatient(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.saveCovTemplateDataForPatient(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateCovTemplateDataForPatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCovTemplateDataForPatient(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.updateCovTemplateDataForPatient(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCoversheetListData", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCoversheetListData(@RequestBody CoversheetTemplateMasterDto CoversheetTemplateMasterDto) {
		try
		{
			return iCoversheetTemaplateService.getCoversheetListData(CoversheetTemplateMasterDto);

		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
