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
import com.param.global.master.dto.DietTypeMasterDto;
import com.param.global.master.dto.MealMasterDto;
import com.param.global.master.service.IMealMasterService;

@RestController
@RequestMapping("/api/kitchen/master/mealMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MealMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(MealMasterController.class);

	@Autowired
	IMealMasterService iMealMasterService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMealMaster(@RequestBody MealMasterDto mealMasterDto) {
		try {
			return iMealMasterService.saveMealMaster(mealMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllMealMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {

		try {
			return iMealMasterService.getAllMealMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveMealMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {

		try {
			return iMealMasterService.getActiveMealMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getMealMasterDropDown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMealMasterDropDown(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {

		try {
			return iMealMasterService.getMealMasterDropDown(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody MealMasterDto mealMasterDto) {
		try {
			return iMealMasterService.updateStatus(mealMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response update(@RequestBody MealMasterDto mealMasterDto) {
		try {
			return iMealMasterService.update(mealMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllMealMasterListPaginationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllMealMasterListPaginationList(@RequestBody MealMasterDto mealMasterDto){

		try {
			return iMealMasterService.getAllMealMasterListPaginationList(mealMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllMealMasterListPaginationListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllMealMasterListPaginationListCount(@RequestBody MealMasterDto mealMasterDto){

		try {
			return iMealMasterService.getAllMealMasterListPaginationListCount(mealMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
