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
import com.param.global.master.dto.DietTemplateDto;
import com.param.global.master.dto.DietTemplateMapperDto;
import com.param.global.master.service.IDietTemplateMapperService;

@RestController
@RequestMapping("/api/kitchen/master/dietTemplate")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTemplateMapperController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTemplateMapperController.class);

	@Autowired
	IDietTemplateMapperService iDietTemplateMapperService;

	/**
	 * This method creates new Diet Template.
	 * 
	 * @param dietTemplateDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDietTemplateMapper(@RequestBody DietTemplateDto dietTemplateDto) {
		try {
			return iDietTemplateMapperService.saveDietTemplateMapper(dietTemplateDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns list of All Diet Templates.
	 * 
	 * @return Response
	 */

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllDietTemplateMapper(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iDietTemplateMapperService.getAllDietTemplateMapper(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns brief Diet Template List.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getDietTemplateList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietTemplateList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iDietTemplateMapperService.getDietTemplateList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns particular Diet Template by Id.
	 * 
	 * @param dietTemplateId
	 * @return Response
	 */

	@RequestMapping(value = "/getDietTemplateById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietTemplateById(@RequestParam("dietTemplateId") Integer dietTemplateId,
			@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iDietTemplateMapperService.getDietTemplateById(dietTemplateId,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDietTemplateByDietPrefIdAndDietTypeId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietTemplateByDietPrefIdsAndDietTypeId(@RequestParam("dietPrefId") Integer dietPrefId,@RequestParam("dietTypeId") Integer dietTypeId,
			@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId) {
		try {
			return iDietTemplateMapperService.getDietTemplateByDietPrefIdsAndDietTypeId(dietPrefId,dietTypeId,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method updates the particular Diet Template by Id.
	 * 
	 * @param dietTemplateDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDietTemplateMapper(@RequestBody DietTemplateDto dietTemplateDto) {
		try {
			return iDietTemplateMapperService.updateDietTemplateMapper(dietTemplateDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns day wise nutrition value list by Diet Template Id.
	 * 
	 * @param dietTemplateId
	 * @return Response
	 */
	@RequestMapping(value = "/getDayWiseNutritionList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDayWiseNutritionList(@RequestParam("dietTemplateId") Integer dietTemplateId) {
		try {
			return iDietTemplateMapperService.getDayWiseNutritionList(dietTemplateId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*-----------------------------Older APIs-----------------------------------------------*/

	/*
	 * @RequestMapping(method = RequestMethod.POST, consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public @ResponseBody Response
	 * saveDietTemplateMapper(@RequestBody DietTemplateMapperDto
	 * dietTemplateMapperDto) { try { return
	 * iDietTemplateMapperService.saveDietTemplateMapper(dietTemplateMapperDto);
	 * } catch (Exception e) {  } return new Response(ERROR,
	 * null, null, null, null); }
	 */

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietTemplateMapperByDishAndFoodType(
			@RequestParam(value = "dietTypeMasterId", required = true) Integer dietTypeMasterId,
			@RequestParam(value = "dietOption", required = true) String dietOption) {
		try {

			DietTemplateMapperDto dietTemplateMapperDto = new DietTemplateMapperDto();
			dietTemplateMapperDto.setDietTypeMasterId(dietTypeMasterId);
			dietTemplateMapperDto.setDietOption(dietOption);
			return iDietTemplateMapperService.getDietTemplateMapperByDishAndFoodType(dietTemplateMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveDietTemplateMapper() {
		try {
			return iDietTemplateMapperService.getActiveDietTemplateMapper();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody DietTemplateMapperDto dietTemplateMapperDto) {
		try {
			return iDietTemplateMapperService.updateStatus(dietTemplateMapperDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
