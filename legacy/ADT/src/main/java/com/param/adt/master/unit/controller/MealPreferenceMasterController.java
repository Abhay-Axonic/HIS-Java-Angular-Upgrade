package com.param.adt.master.unit.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.adt.master.unit.service.IMealPreferenceMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/adt/unit")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MealPreferenceMasterController implements ICommonConstants{

	@Autowired
	private IMealPreferenceMasterService iMealPreferenceMasterService;
	
	
	@RequestMapping(value = "/saveMealPreferenceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMealPreferenceMaster(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iMealPreferenceMasterService.saveMealPreferenceMaster(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getMealPreferenceMasterById/{mealPrefernceId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getMealPreferenceMasterById(@PathVariable("mealPrefernceId") int mealPrefernceId,
			@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId) {
		try {
			return iMealPreferenceMasterService.getMealPreferenceMasterById(mealPrefernceId, orgId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getMealPreferenceMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getMealPreferenceMasterList(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iMealPreferenceMasterService.getMealPreferenceMasterList(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	

	@RequestMapping(value = "/updateMealPreferenceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNursingStationMaster(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iMealPreferenceMasterService.updateNursingStationMaster(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateMealPreferenceMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMealPreferenceMasterStatus(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iMealPreferenceMasterService.updateMealPreferenceMasterStatus(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getMealPreferenceMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMealPreferenceMasterCount(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {
			return iMealPreferenceMasterService.getMealPreferenceMasterCount(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveMealPreferenceMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getaCTIVEMealPreferenceMasterList(@RequestBody MealPreferenceMasterDto mealPreferenceMasterDto) {
		try {

			return iMealPreferenceMasterService.getActiveMealPreferenceMasterList(mealPreferenceMasterDto);

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}
}