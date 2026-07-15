package com.param.lis.global.controller;

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
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.MicrobiologyStainMasterDto;
import com.param.lis.global.dto.StainigMasterDto;
import com.param.lis.global.service.IMicrobiologyStainigMasterService;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/LIS/global")
public class MicrobiologyStainigMasterController implements ICommonConstants, IError{

	@Autowired
	private IMicrobiologyStainigMasterService imicrobiologyStainigMasterService;
	
	@RequestMapping(value = "/addMicrobStainigMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addMicrobStainigMaster(@RequestBody MicrobiologyStainMasterDto microbstainigMasterDto){
		try{
			return imicrobiologyStainigMasterService.addMicrobStainigMaster(microbstainigMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getaddMicrobStainigMasterById/{orgId}/{microStainId}", method = RequestMethod.GET)
	public @ResponseBody Response getMicrobStainigMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "microStainId") Integer microStainId)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = imicrobiologyStainigMasterService.getMicrobStainigMasterMasterById(orgId, microStainId);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}	
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateMicrobStainigMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMicrobStainigMaster(@RequestBody MicrobiologyStainMasterDto microbstainigMasterDto)
	{
		Response<MicrobiologyStainMasterDto, Integer> response = null;
		try
		{
			response = imicrobiologyStainigMasterService.updateMicrobStainigMaster(microbstainigMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/activeInactiveMicrobStainigMaster/{orgId}/{microStainId}/{organismStatus}", method = RequestMethod.GET)
	public @ResponseBody Response ActivateInactivateMicrobStainigMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "microStainId") Integer microStainId,
			@PathVariable(value = "organismStatus") Character organismStatus)
	{
		Response<MicrobiologyStainMasterDto, Integer> response = null;
		try
		{
			response = imicrobiologyStainigMasterService.ActivateInactivateMicrobStainigMaster(orgId, microStainId, organismStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listMicrobStainigMaster/{orgId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listMicrobStainigMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = imicrobiologyStainigMasterService.listMicrobStainigMaster(orgId, offset, recordPerPage);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getToTallistMicrobStainigMaster/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getToTalMicrobStainigMaster(@PathVariable(value= "orgId") Integer orgId)
		{
			Response<MicrobiologyStainMasterDto, Integer> response = null;
			try
				{
					response = imicrobiologyStainigMasterService.getToTalMicrobStainigMaster(orgId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}


}
