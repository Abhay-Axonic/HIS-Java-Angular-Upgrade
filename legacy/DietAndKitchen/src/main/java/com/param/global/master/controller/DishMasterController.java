package com.param.global.master.controller;

import java.util.List;

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
import com.param.global.master.dto.DishMasterDto;
import com.param.global.master.dto.MealPreferenceMasterDto;
import com.param.global.master.service.IDishMasterService;

@RestController
@RequestMapping("/api/kitchen/master/dishMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DishMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DishMasterController.class);

	@Autowired
	IDishMasterService iDishMasterService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDishMaster(@RequestBody DishMasterDto dishMasterDto) {
		try {
			return iDishMasterService.saveDishMaster(dishMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getDishMasterList(@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId ) {

		try {
			return iDishMasterService.getDishMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET)
	public @ResponseBody Response getActiveDishMasterList(@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId) {

		try {
			return iDishMasterService.getActiveDishMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getDishMasterDropDown", method = RequestMethod.GET)
	public @ResponseBody Response getDishMasterDropDown(@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId) {

		try {
			return iDishMasterService.getDishMasterDropDown(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getDishListByDietPrefId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDishListByDietPrefId(@RequestBody List<MealPreferenceMasterDto> mealPrefDtos,
			@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId ) {

		try {
			return iDishMasterService.getDishListByDietPrefId(mealPrefDtos,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody DishMasterDto dishMasterDto) {
		try {
			return iDishMasterService.updateStatus(dishMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMaster(@RequestBody DishMasterDto dishMasterDto) {
		try {
			return iDishMasterService.updateMaster(dishMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getNutritionalValues", method = RequestMethod.GET)
	public @ResponseBody Response getNutritionalValues(@RequestParam("dishId") Integer dishId) {

		try {
			return iDishMasterService.getNutritionalValues(dishId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getNutritionalValuesByDishList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNutritionalValuesByDishList(@RequestBody List<DishMasterDto> dishList) {

		try {
			return iDishMasterService.getNutritionalValuesByDishList(dishList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
