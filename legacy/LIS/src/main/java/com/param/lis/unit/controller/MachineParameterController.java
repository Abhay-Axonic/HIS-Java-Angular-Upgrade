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
import com.param.lis.unit.dto.MachineMasterDto;
import com.param.lis.unit.dto.MachineParameterDto;
import com.param.lis.unit.service.IMachineParameterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/global")
public class MachineParameterController implements ICommonConstants,IError {

	@Autowired
	private IMachineParameterService  iMachineParameterService;
	
	
	@RequestMapping(value = "/addMachineParameter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMachineParameterMaster(@RequestBody MachineParameterDto machineParameterDto) {
		Response<MachineParameterDto, Integer> response = null;
		try {
			//System.out.println("Save ApI CHECK Inside Controlletr");
			//System.out.println("==========____++++"+machineParameterDto.toString());
			response = iMachineParameterService.saveMachineParameterMaster(machineParameterDto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getMachineParabyId/{organisationId}/{unitId}/{machinParameterId}", method = RequestMethod.GET)
	public @ResponseBody Response getMachineParameterMasterById(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "machinParameterId") Integer machinParameterId) {
		Response<MachineParameterDto, Integer> response = null;
		try {
			response = iMachineParameterService.getMachineParameterMasterById(organisationId, unitId, machinParameterId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateMachineParaMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMachineParaMaster(@RequestBody MachineParameterDto machineParameterDto) {
		Response<MachineParameterDto, Integer> response = null;
		try {
			response = iMachineParameterService.updateMachineParaMaster(machineParameterDto);
		} catch (Exception e) {
			
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value = "/activeInactiveMachinePara/{organisationId}/{machinParameterId}/{machineStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveMachinePara(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "machinParameterId") Integer machinParameterId,
			@PathVariable(value = "machineStatus") Character machineStatus) {
//System.out.println(machineStatus);
		Response<MachineParameterDto, Integer> response = null;
		try {
			response = iMachineParameterService.activateInactivateMachineParaMaster(organisationId, machinParameterId,
					machineStatus);
  } catch (Exception e) {
			
			throw e;
		}
		return response;
	}
	
	@RequestMapping(value = "/listMachineParaMaster/{organisationId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listMachineMaster(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<MachineParameterDto, Integer> response = null;
		try {
			response = iMachineParameterService.listMachineParaMaster(organisationId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalMachinePatraMasterRecord/{organisationId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalMachineMasterRecord(@PathVariable(value = "organisationId") Integer organisationId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<MachineParameterDto, Integer> response = null;
		//System.out.println(organisationId);
		//System.out.println(organisationId);
		try {
			response = iMachineParameterService.getTotalMachineParaMasterRecord(organisationId, unitId);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/activeParameterMachineList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveParaMachineList(@RequestBody MachineParameterDto  machineParameterDto){
		try{
			
			return iMachineParameterService.getActiveParaMachineList(machineParameterDto);
		}catch(Exception e){
			throw e;
		}
	}
	

}



