package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.dto.FormulationTypeToDosageMapperDto;
import com.param.global.service.IFormulationTypeToDosageMapperService;

@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FormulationTypeToDosageMapperController  implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(FormulationTypeToDosageMapperController.class);

	@Autowired
	private IFormulationTypeToDosageMapperService iFormulationTypeToDosageMapperService;

	@RequestMapping(value = "saveFormulationTypeToDosageMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveFormulationTypeToDosageMapper(@RequestBody FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
		try {
			return iFormulationTypeToDosageMapperService.saveFormulationTypeToDosageMapper(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
			logger.error("Error while saving the formulation type to dosage mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "updateFormulationTypeToDosageMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateFormulationTypeToDosageMapper(@RequestBody FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
		try {
			return iFormulationTypeToDosageMapperService.updateFormulationTypeToDosageMapper(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
			logger.error("Error while updating the formulation type to dosage mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "updateFormulationTypeToDosageMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateFormulationTypeToDosageMapperStatus(@RequestBody FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
		try {
			return iFormulationTypeToDosageMapperService.updateFormulationTypeToDosageMapperStatus(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
			logger.error("Error while updating the formulation type to dosage mapper status", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "getFormulationTypeToDosageMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFormulationTypeToDosageMapperList(@RequestBody FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
		try {
			return iFormulationTypeToDosageMapperService.getFormulationTypeToDosageMapperList(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
			logger.error("Error while fetching the formulation type to dosage mapper list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "getFormulationTypeToDosageMapperListByFormulationTypeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFormTypeToDosMappListByFormTypeId(@RequestBody FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
		try {
			return iFormulationTypeToDosageMapperService.getFormTypeToDosMappListByFormTypeId(formulationTypeToDosageMapperDto);
		} catch (Exception e) {
			logger.error("Error while fetching the formulation type to dosage mapper list by formulation type id", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
