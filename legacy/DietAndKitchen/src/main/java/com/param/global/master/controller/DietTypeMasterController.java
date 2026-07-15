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
import com.param.global.master.service.IDietTypeMasterService;
import com.param.global.unit.dto.CabinMasterDto;

@RestController
@RequestMapping(value = "/api/kitchen/master/dietTypeMaster")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietTypeMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTypeMasterController.class);

	@Autowired
	IDietTypeMasterService iDietTypeMasterService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDietTypeMaster(@RequestBody DietTypeMasterDto dietTypeMasterDto) {
		try {
			return iDietTypeMasterService.saveDietTypeMaster(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getByDietTypeId", method = RequestMethod.GET)
	public @ResponseBody Response getDietTypeMasterById(
			@RequestParam(value = "dietTypeId", required = true) Integer dietTypeId) {
		try {
			DietTypeMasterDto dietTypeMasterDto = new DietTypeMasterDto();
			dietTypeMasterDto.setDietTypeId(dietTypeId);
			return iDietTypeMasterService.getDietTypeMasterById(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getByOrganizationId", method = RequestMethod.GET)
	public @ResponseBody Response getDietTypeMasterList(@RequestParam(value = "orgId", required = true) Integer orgId) {
		try {
			DietTypeMasterDto dietTypeMasterDto = new DietTypeMasterDto();
			dietTypeMasterDto.setOrganizationId(orgId);
			return iDietTypeMasterService.getDietTypeMasterList(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public @ResponseBody Response getAllDietTypeMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iDietTypeMasterService.getAllDietTypeMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActive", method = RequestMethod.GET)
	public @ResponseBody Response getActiveDietTypeMasterList(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId ) {
		try {
			return iDietTypeMasterService.getActiveDietTypeMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDietTypeMaster(@RequestBody DietTypeMasterDto dietTypeMasterDto) {
		try {
			return iDietTypeMasterService.updateDietTypeMaster(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*
	 * @RequestMapping(method = RequestMethod.DELETE) public @ResponseBody
	 * Response deleteDietTypeMasterStatus(@RequestParam(value="dietTypeId")
	 * Integer dietTypeId) { try { DietTypeMasterDto dietTypeMasterDto=new
	 * DietTypeMasterDto(); dietTypeMasterDto.setDietTypeId(dietTypeId); return
	 * iDietTypeMasterService.updateDietTypeMasterStatus(dietTypeMasterDto); }
	 * catch (Exception e) {  } return new Response(ERROR,
	 * null, null, null, null); }
	 */

	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDietTypeMasterStatus(@RequestBody DietTypeMasterDto dietTypeMasterDto) {
		try {
			return iDietTypeMasterService.updateDietTypeMasterStatus(dietTypeMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/getDietTypeMasterPaginationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDietTypeMasterPaginationList(@RequestBody DietTypeMasterDto dietTypeMasterDto){

		try {
			return iDietTypeMasterService.getDietTypeMasterPaginationList(dietTypeMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDietTypeMasterPaginationListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDietTypeMasterPaginationListCount(@RequestBody DietTypeMasterDto dietTypeMasterDto){
		
		try {
			return iDietTypeMasterService.getDietTypeMasterPaginationListCount(dietTypeMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
