package com.param.global.master.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.NutritionalMasterDto;
import com.param.global.master.service.INutritionalMasterService;

@RestController
@RequestMapping("/api/kitchen/master/nutritionalMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NutritionalMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(NutritionalMasterController.class);

	@Autowired
	INutritionalMasterService iNutritionalMasterService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNutritionalMaster(@RequestBody NutritionalMasterDto nutritionalMasterDto) {
		try {
			return iNutritionalMasterService.saveNutritionalMaster(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getByOrganizationId", method = RequestMethod.GET)
	public @ResponseBody Response getNutritionalMasterList(@RequestParam("orgId") Integer orgId) {
		try {
			NutritionalMasterDto nutritionalMasterDto = new NutritionalMasterDto();
			nutritionalMasterDto.setOrganizationId(orgId);

			return iNutritionalMasterService.getNutritionalMasterList(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNutritionalMaster(@RequestBody NutritionalMasterDto nutritionalMasterDto) {
		try {
			return iNutritionalMasterService.updateNutritionalMaster(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
	public @ResponseBody Response updateNutritionMasterStatus(@RequestBody NutritionalMasterDto nutritionalMasterDto) {
		try {
			return iNutritionalMasterService.updateNutritionMasterStatus(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getByNutritionIdAndOrganizationId", method = RequestMethod.GET)
	public @ResponseBody Response getNutritionalMasterById(@RequestParam("nutritionId") Integer nutritionId,
			@RequestParam("orgId") Integer orgId) {
		try {
			NutritionalMasterDto nutritionalMasterDto = new NutritionalMasterDto();
			nutritionalMasterDto.setNutritionId(nutritionId);
			nutritionalMasterDto.setOrganizationId(orgId);

			return iNutritionalMasterService.getNutritionalMasterById(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllNutritionalMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId,@RequestParam("offset") Integer offset ,@RequestParam("noOfRecordsPerPage") Integer noOfRecordsPerPage) {
		try {
			return iNutritionalMasterService.getAllNutritionalMasterList(organizationId, unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET)
	public @ResponseBody Response getActiveNutritionalMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iNutritionalMasterService.getActiveNutritionalMasterList(organizationId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getNutritionMasterCount", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNutritionMasterCount(@RequestBody NutritionalMasterDto nutritionalMasterDto ) {
		try {
			return iNutritionalMasterService.getNutritionMasterCount(nutritionalMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
