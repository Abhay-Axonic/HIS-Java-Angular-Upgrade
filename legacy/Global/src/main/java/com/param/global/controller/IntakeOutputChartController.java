package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.IntakeOutputChartMasterDto;
import com.param.global.dto.OutputChartDetailsDto;
import com.param.global.service.IIntakeOutputChartService;
import com.param.global.unit.dto.CabinMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IntakeOutputChartController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(IntakeOutputChartController.class);

	@Autowired 
	private IIntakeOutputChartService iIntakeOutputChartService;
	
	@RequestMapping(value="/saveIntakeOutputChart", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveIntakeOutputChart(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.saveIntakeOutputChart(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("saveIntakeOutputChart failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getIntakeOutputChartListByPatientId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeOutputChartListByPatientId(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getIntakeOutputChartListByPatientId(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getIntakeOutputChartListByPatientId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getIntakeOutputChartHistoryListByDate", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeOutputChartHistoryListByDate(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getIntakeOutputChartHistoryListByDate(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getIntakeOutputChartHistoryListByDate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getIntakeHistoryList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeHistoryList(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getIntakeHistoryList(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getIntakeHistoryList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getOutputHistoryList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOutputHistoryList(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getOutputHistoryList(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getOutputHistoryList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getInputOutputChartHistoryDetailsList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInputOutputChartHistoryDetailsList(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getInputOutputChartHistoryDetailsList(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getInputOutputChartHistoryDetailsList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value="/getInputOutputChartHistoryByDate", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInputOutputChartHistoryByDate(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.getInputOutputChartHistoryByDate(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("getInputOutputChartHistoryByDate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	
	@RequestMapping(value = "/updateInputOutputStartStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateInputOutputStartStatus(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto){
		
		try {
			return iIntakeOutputChartService.updateInputOutputStartStatus(intakeOutputChartMasterDto);
			
		} catch (Exception e) {
		logger.error("updateInputOutputStartStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateStatusForStartIOChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusForStartIOChart(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto){
		
		try {
			return iIntakeOutputChartService.updateStatusForStartIOChart(intakeOutputChartMasterDto);
			
		} catch (Exception e) {
		logger.error("updateStatusForStartIOChart failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getIntakeAndOutputDateByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntakeAndOutputDateByPatientId(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto){
		
		try {
			return iIntakeOutputChartService.getIntakeAndOutputDateByPatientId(intakeOutputChartMasterDto);
			
		} catch (Exception e) {
		logger.error("getIntakeAndOutputDateByPatientId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/updateIntakeOutputChart", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateIntakeOutputChart(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto) {
		try {
			return iIntakeOutputChartService.updateIntakeOutputChart(intakeOutputChartMasterDto);
		} catch (Exception e) {
		logger.error("updateIntakeOutputChart failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	
	@RequestMapping(value = "/deleteStatusForStartIntakeChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteStatusForStartIOChart(@RequestBody IntakeOutputChartMasterDto intakeOutputChartMasterDto){
		
		try {
			return iIntakeOutputChartService.deleteStatusForStartIOChart(intakeOutputChartMasterDto);
			
		} catch (Exception e) {
		logger.error("deleteStatusForStartIOChart failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/deleteStatusForOutputChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteStatusForOutputChart(@RequestBody OutputChartDetailsDto outputChartDetailsDto){
		
		try {
			return iIntakeOutputChartService.deleteStatusForOutputChart(outputChartDetailsDto);
			
		} catch (Exception e) {
		logger.error("deleteStatusForOutputChart failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
