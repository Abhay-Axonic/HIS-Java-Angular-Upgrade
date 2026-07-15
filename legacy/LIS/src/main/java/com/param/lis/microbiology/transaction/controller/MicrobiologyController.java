package com.param.lis.microbiology.transaction.controller;



import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dto.TMicroSampleMediaDto;
import com.param.lis.microbiology.transaction.service.IMicrobiologyService;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/Microbiology")
public class MicrobiologyController implements ICommonConstants, IError
{
	@Autowired
	private IMicrobiologyService iMicrobiologyService;

	final static Logger logger = Logger.getLogger(MicrobiologyController.class);
	
	@RequestMapping(value = "/Aacceptance/Pending", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response microAcceptancePending(
			@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iMicrobiologyService.microAcceptancePending(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/Aacceptance/Pending/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response microAcceptancePendingCount(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<SampleAcceptancePendingDto, Integer> response = null;
		try
		{
			response = iMicrobiologyService.microAcceptancePendingCount(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	 @RequestMapping(value = "/Aacceptance/Pending/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response microAcceptancePendingSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iMicrobiologyService.microAcceptancePendingSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	
	
	@RequestMapping(value = "/Microbiology/Pending/Sample", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response microChemistryAcceptRejectSample(@RequestBody List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iMicrobiologyService.microChemistryAcceptRejectSample(listlabSampleDetailsMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	
	@RequestMapping(value = "/MicroWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMicroChemistryWorklist(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iMicrobiologyService.getMicroChemistryWorklist(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/MicroWorklist/Count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMicroChemistryWorklistCount(@RequestBody BioChemParamDto bioChemParamDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iMicrobiologyService.getMicroChemistryWorklistCount(bioChemParamDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	 @RequestMapping(value = "/MicroWorklist/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getMicroChemistryWorklistSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iMicrobiologyService.getMicroChemistryWorklistSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	
	
	   @RequestMapping(value = "/incubation/media", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody Response addMediaForIncuabation(@RequestBody List<TMicroSampleMediaDto> listTMicroSampleMediaDto)
	    {
	        Response<LabSampleDetailsMasterDto, Integer> response = null;
	        try
	        {
	            response = iMicrobiologyService.addMediaForIncuabation(listTMicroSampleMediaDto);
	            return response;
	        } catch (Exception e)
	        {
	            logger.error("Exception", e);
	            throw e;
	        }
	    }
	   
	    @RequestMapping(value = "/incubation/media/{labSampleDtlsId}/{orgId}/{orgUniId}", method = RequestMethod.GET)
	    public @ResponseBody Response getAddedMediaForIncuabation(@PathVariable(value = "labSampleDtlsId") Integer labSampleDtlsId,
	        @PathVariable(value = "orgId") Integer orgId,
	        @PathVariable(value = "orgUniId") Integer orgUniId)
	    {
	        Response<LabSampleDetailsMasterDto, Integer> response = null;
	        try
	        {
	            response = iMicrobiologyService.getAddedMediaForIncuabation(labSampleDtlsId,orgId,orgUniId);
	            return response;
	        } catch (Exception e)
	        {
	            logger.error("Exception", e);
	            throw e;
	        }
	    }
	
	
	@RequestMapping(value = "/Worklist/ReportEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendForIncubationObjservation(@RequestBody List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response =iMicrobiologyService.sendForIncubationObjservation(listlabSampleDetailsMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	 @RequestMapping(value = "/MicroTempletWorkbeanch/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getMicroTempletWorkbeanchSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iMicrobiologyService.getMicroTempletWorkbeanchSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }

	 @RequestMapping(value = "/updateLabDetailsStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response updateLabDetailsStatus(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iMicrobiologyService.updateLabDetailsStatus(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }

	 @RequestMapping(value = "/getDetailsBySampleDetailId/{labSampDetId}", method = RequestMethod.GET)
	  public @ResponseBody Response getDetailsBySampleDetailId(@PathVariable("labSampDetId") Integer labSampleDetailId)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iMicrobiologyService.getDetailsBySampleDetailId(labSampleDetailId);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	
}
