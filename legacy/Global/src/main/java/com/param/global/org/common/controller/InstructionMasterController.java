package com.param.global.org.common.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.org.common.service.IInstructionMasterService;

import java.util.List;
import java.util.stream.Collectors;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value = "/api/common")
@RestController
public class InstructionMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(InstructionMasterController.class);


	@Autowired
	private IInstructionMasterService iInspectionMasterService;

	@RequestMapping(value = "/saveInspectionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveInspectionMaster(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.saveInspectionMaster(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getInspectionMasterListById/{inspectionId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getInspectionMasterListById(@PathVariable("inspectionId") int inspectionId,
			@PathVariable("orgId") int orgId) {
		try {
			return iInspectionMasterService.getInspectionMasterListById(inspectionId, orgId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getInspectionMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getInspectionMasterList(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getInspectionMasterList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateInspectionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateInspectionMaster(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.updateInspectionMaster(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	/*
	 * @RequestMapping(value = "/updateInspectionMaster", method =
	 * RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	 * public @ResponseBody Response addSample(@RequestBody String sampleMasterDto)
	 * {
	 * 
	 * 
	 * Response<InstructionMasterDto, Integer> response = null; try {
	 * 
	 * JsonCheck.mapJsonToObject(sampleMasterDto, InstructionMasterDto.class);
	 * 
	 * response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
	 * response;
	 * 
	 * return null; } catch (Exception e) {  return new
	 * Response(ERROR, null, null, null, null); } }
	 */
	@RequestMapping(value = "/updateInspectionMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateInspectionMasterStatus(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.updateInspectionMasterStatus(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getInspectionMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInspectionMasterCount(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getInspectionMasterCount(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveInspectionMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveInspectionMasterList(
			@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
				return iInspectionMasterService.getActiveInspectionMasterList(instructionMasterDto);

		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDeptSpecilityById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDeptSpecilityById(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getDeptSpecilityById(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getConvertedLanguageListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConvertedLanguageListById(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getConvertedLanguageListById(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getSpecialityByDept", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityByDept(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getSpecialityByDept(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/convertText", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response convertText() {
		try {
			return iInspectionMasterService.convertText();
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getSearchedInstructionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSearchedInstructionList(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getSearchedInstructionList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMostFrequentlyUsedInstructions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMostFrequentlyUsedInstructions(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getMostFrequentlyUsedInstructions(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getSearchedFreeTextInstructionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSearchedFreeTextInstructionList(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInspectionMasterService.getSearchedFreeTextInstructionList(instructionMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
