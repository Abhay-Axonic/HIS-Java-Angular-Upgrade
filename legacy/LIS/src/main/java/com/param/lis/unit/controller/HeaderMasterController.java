package com.param.lis.unit.controller;

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
import com.param.lis.unit.dto.HeaderMasterDto;
import com.param.lis.unit.service.IHeaderMasterService;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/global")
public class HeaderMasterController implements ICommonConstants, IError
{
	@Autowired
	private IHeaderMasterService iHeaderMasterService;

	@RequestMapping(value = "/addHeader", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addHeader(@RequestBody HeaderMasterDto headerMasterDto)
	{
		Response<HeaderMasterDto, Integer> response = null;
		try
		{
			response = iHeaderMasterService.addHeaderMaster(headerMasterDto);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@RequestMapping(value = "/getHeaderbyId/{orgId}/{unitId}/{headerId}", method = RequestMethod.GET)
	public @ResponseBody Response getHeaderbyId(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "headerId") Integer headerId)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iHeaderMasterService.getHeaderMasterById(orgId,unitId,headerId);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}	
	}

	@RequestMapping(value = "/updateHeader", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSample(@RequestBody HeaderMasterDto headerMasterDto)
	{
		Response<HeaderMasterDto, Integer> response = null;
		try
		{
			response = iHeaderMasterService.updateHeaderMaster(headerMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/activeInactiveHeader/{orgId}/{headerId}/{headerStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveHeader(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "headerId") Integer headerId,
			@PathVariable(value = "headerStatus") Character headerStatus)
	{
		Response<HeaderMasterDto, Integer> response = null;
		try
		{
			response = iHeaderMasterService.ActivateInactivateHeaderMaster(orgId, headerId, headerStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/listHeaderMaster/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listHeaderMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iHeaderMasterService.listHeaderMaster(orgId,unitId, offset, recordPerPage);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalHeaderMasterRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalHeaderMasterRecord(@PathVariable(value= "orgId") Integer orgId,
			@PathVariable(value= "unitId") Integer unitId)
		{
			Response<HeaderMasterDto, Integer> response = null;
			try
				{
					response = iHeaderMasterService.getToTalHeaderMasterRecord(orgId,unitId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}

	@RequestMapping(value = "/getHeaderParameterList/{orgId}/{unitId}/{headerId}", method = RequestMethod.GET)
	public @ResponseBody Response getHeaderParameterList(@PathVariable(value= "orgId") Integer orgId,
			@PathVariable(value= "unitId") Integer unitId,@PathVariable(value= "headerId") Integer headerId)
		{
			Response<HeaderMasterDto, Integer> response = null;
			try
				{
					response = iHeaderMasterService.getHeaderParameterList(orgId,unitId,headerId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}

}
