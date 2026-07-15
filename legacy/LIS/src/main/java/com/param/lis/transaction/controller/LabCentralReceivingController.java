package com.param.lis.transaction.controller;

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

import com.param.global.dto.OrderDetailsMasterDto;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SampleForInOutPatient;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.transaction.service.ILabCentralReceivingService;
//LabCentralReceivingController
@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/centralReceiving")
public class LabCentralReceivingController implements ICommonConstants, IError
{
	@Autowired
	private ILabCentralReceivingService iILabCentralReceivingService;

	final static Logger logger = Logger.getLogger(LabCentralReceivingController.class);

	@RequestMapping(value = "/getSampleListForInOutPatient/{orgId}/{orgUnitId}/{visitTypeId}/{offset}/{recordPerPage}/{isSendToCrApplicable}", method = RequestMethod.GET)
	public @ResponseBody Response getSampleListForInOutPatient(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,
			@PathVariable(value = "visitTypeId") Integer visitTypeId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,
			@PathVariable(value = "isSendToCrApplicable") Character isSendToCrApplicable)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getSampleListForInOutPatient(orgId, orgUnitId, visitTypeId, offset,
					recordPerPage,isSendToCrApplicable);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	@RequestMapping(value = "/getTotalRecordOutPatient", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalRecordOutPatient(@RequestBody SearchDto searchDto)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getTotalRecordOutPatient(searchDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@RequestMapping(value = "/acceptOrRejectSample", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptOrRejectSample(
			@RequestBody List<LabSampleDetailsMasterDto> listlabLabSampleDetailsMasterDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.acceptOrRejectSample(listlabLabSampleDetailsMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getCentrifugationWorklist/{orgId}/{orgUnitId}/{deptId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response getCentrifugationWorklist(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, 
			@PathVariable(value = "deptId") Integer deptId,@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getCentrifugationWorklist(orgId, orgUnitId,deptId ,offset, recordPerPage);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}


	

	@RequestMapping(value = "/getTotalRecordINPatient", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalRecordINPatient(@RequestBody SearchDto searchDto)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getTotalRecordINPatient(searchDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	
	
	
	@RequestMapping(value = "/getTotalcentrifugationWorkListRecord/{orgId}/{orgUnitId}/{deptId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalcentrifugationWorkList(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "deptId") Integer deptId)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getTotalcentrifugationWorkList(orgId, orgUnitId,deptId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@RequestMapping(value = "/getRejectedSampleList/{orgId}/{orgUnitId}/{deptId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response getRejectedSampleList(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, @PathVariable(value = "deptId") Integer deptId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage
	)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getRejectedSampleList(orgId, orgUnitId,deptId,offset,recordPerPage);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalRecordRejectedSample", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalRecordRejectedSample(@RequestBody SearchDto searchDto)
	{
		Response<SampleForInOutPatient, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.getTotalRecordRejectedSample(searchDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/sampleRecollect", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sampleRecollect(@RequestBody List<LabSampleMasterDto> listLabSampleMasterDto)
	{
		Response<OrderDetailsMasterDto, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.sampleRecollect(listLabSampleMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/Centrifugation/Department", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendToDepartMent(@RequestBody List<LabSampleDetailsMasterDto> listlabLabSampleDetailsMasterDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iILabCentralReceivingService.sendToDepartMent(listlabLabSampleDetailsMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	 /*==========================SEARCH GENRAL LAB FILTER===============================*/
	
	
	 @RequestMapping(value = "/outPatientList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getFilteredOutPatientList(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	    	  
	    	 response = iILabCentralReceivingService.getFilteredOutPatientList(searchDto);
			 return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/CentrifugationWorkList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getFilteredCentrifugationWorkList(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iILabCentralReceivingService.getFilteredCentrifugationWorkList(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/getRejectedSampleList/Patient/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getRejectedSampleListPatientSearch(@RequestBody SearchCommonDto searchDto)
	  {
	      Response<CommonAutoCompleteDto, Integer> response = null;
	      try
	      {
	          response = iILabCentralReceivingService.getRejectedSampleListPatientSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 @RequestMapping(value = "/getRejectedSampleList/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getRejectedSampleListSearch(@RequestBody SearchDto searchDto)
	  {
	      Response<CommonAutoCompleteDto, Integer> response = null;
	      try
	      {
	          response = iILabCentralReceivingService.getRejectedSampleListSearch(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	 
	 @RequestMapping(value = "/getTotalcentrifugationWorkListCount",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getTotalcentrifugationWorkListCount(@RequestBody SearchDto searchDto)
		{
			Response<SampleForInOutPatient, Integer> response = null;
			try
			{
				response = iILabCentralReceivingService.getTotalcentrifugationWorkListCount(searchDto);
				return response;
			} catch (Exception e)
			{
				logger.error("Exection", e);
				throw e;
			}
		}
	
}
