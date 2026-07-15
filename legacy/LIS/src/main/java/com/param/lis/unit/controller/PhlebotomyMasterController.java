package com.param.lis.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.unit.dto.PhlebotomyMasterDto;
import com.param.lis.unit.service.IPhlebotomyMasterService;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/LIS/global")
public class PhlebotomyMasterController implements ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(PhlebotomyMasterController.class);

	@Autowired
	private IPhlebotomyMasterService iPhlebotomyMasterService;

	@RequestMapping(value = "/addPhlebotomyMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addPhlebotomyMaster(@RequestBody PhlebotomyMasterDto phlebotomyMasterDto){
		try{
			return iPhlebotomyMasterService.addPhlebotomyMaster(phlebotomyMasterDto);
		}catch(Exception e){
			logger.error("Exception adding phlebotomy master", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getPhlebotomyById/{orgId}/{unitId}/{phlebotomyId}", method = RequestMethod.GET)
	public @ResponseBody Response getPhlebotomyMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,@PathVariable(value = "phlebotomyId") Integer phlebotomyId)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iPhlebotomyMasterService.getPhlebotomyMasterById(orgId, unitId,phlebotomyId);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}	
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updatePhlebotomy", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePhlebotomyMaster(@RequestBody PhlebotomyMasterDto phlebotomyMasterDto)
	{
		Response<PhlebotomyMasterDto, Integer> response = null;
		try
		{
			response = iPhlebotomyMasterService.updatePhlebotomyMaster(phlebotomyMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/activeInactivePhlebotomy/{orgId}/{phlebotomyId}/{phlebotomyStatus}", method = RequestMethod.GET)
	public @ResponseBody Response ActivateInactivatePhlebotomyMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "phlebotomyId") Integer phlebotomyId,
			@PathVariable(value = "phlebotomyStatus") Character phlebotomyStatus)
	{
		Response<PhlebotomyMasterDto, Integer> response = null;
		try
		{
			response = iPhlebotomyMasterService.ActivateInactivatePhlebotomyMaster(orgId, phlebotomyId, phlebotomyStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listPhlebotomyMaster/{orgId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listPhlebotomyMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iPhlebotomyMasterService.listPhlebotomyMaster(orgId, offset, recordPerPage);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTotalPhlebotomyMasterRecord/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalPhlebotomyMasterRecord(@PathVariable(value= "orgId") Integer orgId)
		{
			Response<PhlebotomyMasterDto, Integer> response = null;
			try
				{
					response = iPhlebotomyMasterService.getToTalPhlebotomyMasterRecord(orgId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}


}
