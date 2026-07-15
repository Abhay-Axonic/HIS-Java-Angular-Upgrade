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
import com.param.lis.unit.dto.LanguageMasterDto;
import com.param.lis.unit.service.ILanguageMasterService;

@RestController
@RequestMapping(value = "/LIS/global")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LanguageMasterController implements ICommonConstants, IError
{
	@Autowired
	private ILanguageMasterService iLanguageMasterService;

	@RequestMapping(value = "/addLanguage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addLanguage(@RequestBody LanguageMasterDto languageMasterDto)
	{
		Response<LanguageMasterDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.addLanguage(languageMasterDto);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@RequestMapping(value = "/getLanguagebyId/{orgId}/{unitId}/{languageId}", method = RequestMethod.GET)
	public @ResponseBody Response getLanguagebyId(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "languageId") Integer languageId)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.getLanguagebyId(orgId,unitId,languageId);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}	
	}

	
	@RequestMapping(value = "/updateLanguage", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateLanguageMaster(@RequestBody LanguageMasterDto languageMasterDto)
	{
		Response<LanguageMasterDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.updateLanguageMaster(languageMasterDto);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/activeInactiveLanguage/{orgId}/{LanguageId}/{languageStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveLanguage(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "LanguageId") Integer LanguageId,
			@PathVariable(value = "languageStatus") Character languageStatus)
	{
		Response<LanguageMasterDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.activeInactiveLanguage(orgId, LanguageId, languageStatus);
		} catch (Exception e)
		{
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/listLanguageMaster/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listLanguageMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.listLanguageMaster(orgId,unitId, offset, recordPerPage);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@RequestMapping(value = "/listLanguageMaster/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response listLanguageMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId)
	{
		Response<GlobalCommonDto, Integer> response = null;
		try
		{
			response = iLanguageMasterService.listAllLanguageMaster(orgId,unitId);
			return response;
		} catch (Exception e)
		{

			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalLanguageMasterRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalLanguageMasterRecord(@PathVariable(value= "orgId") Integer orgId,
			@PathVariable(value= "unitId") Integer unitId)
		{
			Response<LanguageMasterDto, Integer> response = null;
			try
				{
					response = iLanguageMasterService.getTotalLanguageMasterRecord(orgId,unitId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}

	
 	@RequestMapping(value = "/getTotalLanguageMasterRecordActive/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalLanguageMasterRecordActive(@PathVariable(value= "orgId") Integer orgId,
			@PathVariable(value= "unitId") Integer unitId)
		{
			Response<LanguageMasterDto, Integer> response = null;
			try
				{
					response = iLanguageMasterService.getTotalLanguageMasterRecordActive(orgId,unitId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}

}
