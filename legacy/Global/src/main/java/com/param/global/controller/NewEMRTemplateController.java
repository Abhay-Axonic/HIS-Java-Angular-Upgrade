package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.NewEMRTemplateDto;
import com.param.global.service.INewEMRTemplateService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class NewEMRTemplateController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NewEMRTemplateController.class);

	@Autowired
	private INewEMRTemplateService iNewEMRTemplateService;
	
	@RequestMapping(value = "/saveNewEMRTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNewEMRTemplate(@RequestBody CoversheetTemplateMasterDto coversheetTemplateMasterDto){
		
		try {
			return iNewEMRTemplateService.saveNewEMRTemplate(coversheetTemplateMasterDto);
			
		} catch (Exception e) {
		logger.error("saveNewEMRTemplate failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
