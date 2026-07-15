package com.param.lis.microbiology.transaction.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.microbiology.transaction.service.IMicrobiologyTemplateService;
import com.param.lis.transaction.dto.MicrobiologyTemplateDto;

@RestController
@SuppressWarnings({ "unchecked", "rawtypes"})
@RequestMapping(value = "/LIS/Microbiology/Template")
public class MicrobiologyTemplateController implements ICommonConstants, IError{

	
	@Autowired
	private IMicrobiologyTemplateService iMicrobiologyTemplateService;

	final static Logger logger = Logger.getLogger(MicrobiologyController.class);
	
	
	
	@RequestMapping(value = "/saveTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTemplate( @RequestBody MicrobiologyTemplateDto microbiologyTemplateDto)
	{
		Response<MicrobiologyTemplateDto, Integer> response = null;
		try
		{
			response = iMicrobiologyTemplateService.saveTemplate(microbiologyTemplateDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplate( @RequestBody MicrobiologyTemplateDto microbiologyTemplateDto)
	{
		Response<MicrobiologyTemplateDto, Integer> response = null;
		try
		{
			response = iMicrobiologyTemplateService.getTemplate(microbiologyTemplateDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
}
