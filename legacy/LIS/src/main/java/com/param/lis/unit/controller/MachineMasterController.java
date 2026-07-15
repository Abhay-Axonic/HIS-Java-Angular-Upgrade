package com.param.lis.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.dto.DistrictMasterDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MachineMasterDto;
import com.param.lis.unit.service.IMachineMasterService;


@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/global")
public class MachineMasterController implements ICommonConstants,IError {

	@Autowired
	private IMachineMasterService  iMachineMasterService;
	
	
	@RequestMapping(value = "/addMachine", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMachineMaster(@RequestBody MachineMasterDto machineMasterDto) {
		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.saveMachineMaster(machineMasterDto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getMachinebyId/{organisationId}/{unitId}/{machineId}", method = RequestMethod.GET)
	public @ResponseBody Response getMachineMasterById(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "machineId") Integer machineId) {
		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.getMachineMasterById(organisationId, unitId, machineId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateMachineMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMachineMaster(@RequestBody MachineMasterDto machineMasterDto) {
		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.updateMachineMaster(machineMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value = "/activeInactiveMachine/{organisationId}/{machineId}/{machineStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveMachine(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "machineId") Integer machineId,
			@PathVariable(value = "machineStatus") Character machineStatus) {

		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.activateInactivateMachineMaster(organisationId, machineId,
					machineStatus);
		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value = "/listMachineMaster/{organisationId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listMachineMaster(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.listMachineMaster(organisationId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalMachineMasterRecord/{organisationId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalMachineMasterRecord(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<MachineMasterDto, Integer> response = null;
		try {
			response = iMachineMasterService.getTotalMachineMasterRecord(organisationId, unitId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/getActiveMachineList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveMachineList(@RequestBody MachineMasterDto  machineMasterDto){
		try{
			
			return iMachineMasterService.getActiveMachineList(machineMasterDto);
		}catch(Exception e){
			throw e;
		}
	}

	
}
