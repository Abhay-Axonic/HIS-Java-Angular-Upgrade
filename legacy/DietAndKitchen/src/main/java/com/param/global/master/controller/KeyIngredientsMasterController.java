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
import com.param.global.master.dto.KeyIngredientsMasterDto;
import com.param.global.master.service.IKeyIngredientsMasterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/kitchen/master/keyIngredientsMaster")
public class KeyIngredientsMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(KeyIngredientsMasterController.class);

	@Autowired
	IKeyIngredientsMasterService iKeyIngredientsMasterService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveKeyIngredientsMaster(
			@RequestBody KeyIngredientsMasterDto keyIngredientsMasterDto) {
		try {
			return iKeyIngredientsMasterService.saveKeyIngredientsMaster(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getAllKeyIngredientsMaster(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iKeyIngredientsMasterService.getAllKeyIngredientsMaster(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET)
	public @ResponseBody Response getActiveKeyIngredientsMaster(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iKeyIngredientsMasterService.getActiveKeyIngredientsMaster(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKeyIngredientsMaster(
			@RequestBody KeyIngredientsMasterDto keyIngredientsMasterDto) {
		try {
			return iKeyIngredientsMasterService.updateKeyIngredientsMaster(keyIngredientsMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
	public @ResponseBody Response updateKeyIngredientsMasterStatus(
			@RequestBody KeyIngredientsMasterDto keyIngredientsMasterDto) {

		try {
			return iKeyIngredientsMasterService.updateKeyIngredientsMasterStatus(keyIngredientsMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getByKeyIngredientIdAndOrganizationId", method = RequestMethod.GET)
	public @ResponseBody Response getKeyIngredientsMasterById(
			@RequestParam(value = "keyIngredientId") Integer keyIngredientId,
			@RequestParam(value = "orgId") Integer orgId) {
		try {
			KeyIngredientsMasterDto keyIngredientsMasterDto = new KeyIngredientsMasterDto();
			keyIngredientsMasterDto.setKeyIngredientId(keyIngredientId);
			keyIngredientsMasterDto.setOrganizationId(orgId);

			return iKeyIngredientsMasterService.getKeyIngredientsMasterById(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getUnitMasterDetails", method = RequestMethod.GET)
	public @ResponseBody Response getUnitMasterDetails(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iKeyIngredientsMasterService.getUnitMasterDetails(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getNutritionListByKeyIngredientId", method = RequestMethod.GET)
	public @ResponseBody Response getKeyIngredientsDetailsById(
			@RequestParam(value = "keyIngredientId") Integer keyIngredientId) {
		try {

			KeyIngredientsMasterDto keyIngredientsMasterDto = new KeyIngredientsMasterDto();
			keyIngredientsMasterDto.setKeyIngredientId(keyIngredientId);
			return iKeyIngredientsMasterService.getKeyIngredientsDetailsById(keyIngredientsMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
