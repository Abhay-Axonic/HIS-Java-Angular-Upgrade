package com.param.lis.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.SerologyTestFindingMasterDto;
import com.param.lis.unit.service.ISerologyTestFindingMasterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/global")
public class SerologyTestFindingMasterController implements ICommonConstants, IError {

	@Autowired
	private ISerologyTestFindingMasterService iSerologyTestFindingMasterService;

	@RequestMapping(value = "/getserologyTest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addHeader(@RequestBody SerologyTestFindingMasterDto serologyTestMasterDto) {
		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.saveSerologyTestMaster(serologyTestMasterDto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value = "/getSerologybyId/{orgId}/{unitId}/{serologyId}", method = RequestMethod.GET)
	public @ResponseBody Response getSerologyTestMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "serologyId") Integer serologyId) {
		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.getSerologyTestMasterById(orgId, unitId, serologyId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value = "/updateSerologyTest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSerologyMaster(@RequestBody SerologyTestFindingMasterDto serologyTestMasterDto) {
		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.updateSerologyMaster(serologyTestMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/activeInactiveSerology/{orgId}/{serologyId}/{serologyStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveSerology(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "serologyId") Integer serologyId,
			@PathVariable(value = "serologyStatus") Character serologyStatus) {

		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.ActivateInactivateSerologyTestMaster(orgId, serologyId,
					serologyStatus);
		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/listSerologyMaster/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listSerologyMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.listSerologyMaster(orgId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value = "/getTotalSerologyMasterRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalSerologyMasterRecord(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<SerologyTestFindingMasterDto, Integer> response = null;
		try {
			response = iSerologyTestFindingMasterService.getToTalSerologyMasterRecord(orgId, unitId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
}
