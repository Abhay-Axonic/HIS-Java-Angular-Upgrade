package com.param.lis.unit.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.transaction.service.IPathologyMachineParameterService;
import com.param.lis.unit.dto.MachineParameterMpperDto;


@RestController
@RequestMapping(value = "/LIS/global")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PathologyMachineParameterController implements ICommonConstants,IError {

	
	@Autowired
	private IPathologyMachineParameterService iPathomcnParaService;
	
	@RequestMapping(value = "/addPathalogyMachineParaMapping", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMachineParameterMapping(@RequestBody  MachineParameterMpperDto machineParameterMpperDto) {
		Response<MachineParameterMpperDto, Integer> response = null;
		try {
			//System.out.println(machineParameterMpperDto.toString()); 
			response = iPathomcnParaService.saveMachineParameterMapping(machineParameterMpperDto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updatePathalogyMachineParaMapping", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMachineParaMapping(@RequestBody  MachineParameterMpperDto machineParameterMpperDto) {
		try {
			return iPathomcnParaService.updateMachineParaMapping(machineParameterMpperDto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getPathalogyMachineParameterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPathalogyMachineList(@RequestBody  MachineParameterMpperDto machineParameterMpperDto) {
		try {
			return iPathomcnParaService.getPathalogyMachineParameterList(machineParameterMpperDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getPathalogyMachineListByMachineId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPathalogyMachineListByMachineId(@RequestBody  MachineParameterMpperDto machineParameterMpperDto) {
		try {
			return iPathomcnParaService.getPathalogyMachineParameterListByMachineId(machineParameterMpperDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updatePathalogyMachineParameterStatusByMachineId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePathalogyMachineParameterStatus(@RequestBody  MachineParameterMpperDto machineParameterMpperDto) {
		try {
			return iPathomcnParaService.updatePathalogyMachineParameterStatus(machineParameterMpperDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
}
