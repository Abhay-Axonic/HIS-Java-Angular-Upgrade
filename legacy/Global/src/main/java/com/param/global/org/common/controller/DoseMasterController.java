package com.param.global.org.common.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.common.dto.DoseMasterDto;
import com.param.global.org.common.service.IDoseMasterService;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value="/api/common")
@RestController
public class DoseMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoseMasterController.class);

	@Autowired
	private IDoseMasterService iIDoseMasterService;

	
	@RequestMapping(value = "/saveDoseMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoseMaster(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.saveDoseMaster(doseMasterDto);
		} catch (Exception e) {
			logger.error("saveDoseMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDoseMasterListById/{doseId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getDoseMasterListById(@PathVariable("doseId") int doseId,
			@PathVariable("orgId") int orgId) {
		try {
			return iIDoseMasterService.getDoseMasterListById(doseId, orgId);
		} catch (Exception e) {
			logger.error("getDoseMasterListById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDoseMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getDoseMasterList(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.getDoseMasterList(doseMasterDto);
		} catch (Exception e) {
			logger.error("getDoseMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateDoseMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoseMaster(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.updateDoseMaster(doseMasterDto);
		} catch (Exception e) {
			logger.error("updateDoseMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateDoseMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoseMasterStatus(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.updateDoseMasterStatus(doseMasterDto);
		} catch (Exception e) {
			logger.error("updateDoseMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDoseMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoseMasterCount(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.getDoseMasterCount(doseMasterDto);
		} catch (Exception e) {
			logger.error("getDoseMasterCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getActiveDoseMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveDoseMasterList(@RequestBody DoseMasterDto doseMasterDto) {
		try {
			return iIDoseMasterService.getActiveDoseMasterList(doseMasterDto);
		} catch (Exception e) {
			logger.error("getActiveDoseMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
