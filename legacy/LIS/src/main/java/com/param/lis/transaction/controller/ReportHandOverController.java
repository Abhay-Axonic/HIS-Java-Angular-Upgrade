package com.param.lis.transaction.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ServiceException;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.service.IReportHandOverService;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/ReportHandOver")
public class ReportHandOverController implements ICommonConstants, IError 
{
	@Autowired
	private IReportHandOverService iReportHandOverService;

	final static Logger logger = Logger.getLogger(BiochemValidationWorklistController.class);
	
	@RequestMapping(value = "/ReportHandOverList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReportHandList(
			@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<LabResultMasterDto, Integer> response = null;
		try
		{
			response = iReportHandOverService.getReportHandList(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/ReportHandOverList/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getReportHandListCount(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<LabResultMasterDto, Integer> response = null;
		try
		{
			response = iReportHandOverService.getReportHandListCount( bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	 @RequestMapping(value = "/ReportHandOverList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response reportHandOverListSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	    	  
	          response = iReportHandOverService.reportHandOverListSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/updateLabsampDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateLabsampDetails(@RequestBody List<LabResultMasterDto> list)
		{
			try
			{
				return iReportHandOverService.updateLabsampDetails(list);
				
			} catch (Exception e)
			{
				logger.error("Exection", e);
				throw e;
			}
		}
	 
	 

		
	 @RequestMapping(value = "/saveReportHandOverImage", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveIntraVideo(
    		 @RequestParam("labSampleDtlsId") Integer labSampleDtlsId,
    		 @RequestParam("orderDetailsId") Integer orderDetailsId,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("organizationId") Integer organizationId,
    		 @RequestParam("file") MultipartFile file
    		 )
         {
	      try
	      {
	    	  LabResultMasterDto dto = new LabResultMasterDto();
	    	  dto.setLabSampleDtlsId(labSampleDtlsId);
	    	  dto.setOrgUnitId(unitId);
	    	  dto.setOrgId(organizationId);
	    	  dto.setFile(file);
	    	  dto.setOrderDetailsId(orderDetailsId);
	    	  dto.setFileName(file.getOriginalFilename());
	         return iReportHandOverService.saveReportHandOverImage(dto);
	       }catch(Exception e)
	      {
	           logger.error("Exection", e);
	           throw new ServiceException(e.getMessage());
	      }
       }
	
	 @RequestMapping(value = "/sendReportOnMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response sendReportOnMail(@RequestBody List<LabResultMasterDto> list , HttpServletRequest request ) throws Exception
		{
			try
			{
				return iReportHandOverService.sendReportOnMail(list ,request);
				
			} catch (Exception e)
			{
				logger.error("Exection", e);
				throw e;
			}
		}
	 
	 @RequestMapping(value = "/ReportHandOverList/Type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response reportHandOverListTypeSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iReportHandOverService.reportHandOverListTypeSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/bulkEmailWorklistPatientSearch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response bulkEmailWorklistPatientSearch(@RequestBody SearchDto searchDto)
	  {
	      try
	      {
	    	  return iReportHandOverService.bulkEmailWorklistPatientSearch(searchDto);
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/sendBulkEmailToCompanyOrInsurance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response sendBulkEmailToCompanyOrInsurance(@RequestBody List<SmsUtilityConfigInfoDto> dtoList) throws Exception
	  {
	      try
	      {
	    	  return iReportHandOverService.sendBulkEmailToCompanyOrInsurance(dtoList);
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
}
