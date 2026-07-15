package com.param.global.unit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.GrowthChartMasterDto;
import com.param.global.unit.dto.GrowthChartReqDto;
import com.param.global.unit.dto.TGrowthChartDto;
import com.param.global.unit.service.IGrowChartMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class GrowthChartController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(GrowthChartController.class);

	
	@Autowired
	private IGrowChartMasterService iGrowChartMasterService;
	
	
	@RequestMapping(value = "/growthChart" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveGrowthMaster(@RequestBody GrowthChartMasterDto growthChartMasterDto){
		try{
			
			return iGrowChartMasterService.saveGrowthMaster(growthChartMasterDto);
		}catch(Exception e){
		logger.error("saveGrowthMaster failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/growthChart" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response upadateGrowthMaster(@RequestBody GrowthChartMasterDto growthChartMasterDto){
		try{
			return iGrowChartMasterService.upadateGrowthMaster(growthChartMasterDto);
		}catch(Exception e){
		logger.error("upadateGrowthMaster failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/growthChartlist/{orgId}/{unitId}/{offset}/{recordPerPage}" , method = RequestMethod.GET )
	public Response getGrowthMaster(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int offset = Integer.parseInt(params.get("offset").trim());
			int recordPerPage = Integer.parseInt(params.get("recordPerPage").trim());
			return iGrowChartMasterService.getGrowthMaster(orgId, unitId, offset, recordPerPage);
		}catch(Exception e){
		logger.error("getGrowthMaster failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}

	@RequestMapping(value = "/growthChart/{orgId}/{unitId}/{growthChartId}" , method = RequestMethod.GET )
	public Response getGrowthMasterById(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int growthChartId = Integer.parseInt(params.get("growthChartId").trim());
			return iGrowChartMasterService.getGrowthMasterById(orgId, unitId, growthChartId);
		}catch(Exception e){
		logger.error("getGrowthMasterById failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/growthChart/{orgId}/{unitId}/{growthChartId}" , method = RequestMethod.DELETE)
	public Response deleteGrowthChartMaster(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int growthChartId = Integer.parseInt(params.get("growthChartId").trim());
			return iGrowChartMasterService.deleteGrowthChartMaster(orgId, unitId, growthChartId);
		}catch(Exception e){
		logger.error("deleteGrowthChartMaster failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/growthchart/heightunit/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getHeightUnit(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iGrowChartMasterService.getHeightUnit(orgId, unitId);
		}catch(Exception e){
		logger.error("getHeightUnit failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	@RequestMapping(value = "/growthchart/weightunit/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getWeightUnit(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iGrowChartMasterService.getWeightUnit(orgId, unitId);
		}catch(Exception e){
		logger.error("getWeightUnit failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	@RequestMapping(value = "/growthchart/circumferenceunit/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getCircumferenceUnit(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iGrowChartMasterService.getCircumferenceUnit(orgId, unitId);
		}catch(Exception e){
		logger.error("getCircumferenceUnit failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/growthChart/patient" ,  method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveGrowthChartTransaction(@RequestBody List<TGrowthChartDto> listTGrowthChartDto){
		try{
			return iGrowChartMasterService.saveGrowthChartTransaction(listTGrowthChartDto);
		}catch(Exception e){
		logger.error("saveGrowthChartTransaction failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/growthChart/patient/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getGrowthChartTransaction(@RequestBody GrowthChartReqDto growthChartReqDto){
		try{
			return iGrowChartMasterService.getGrowthChartTransaction(growthChartReqDto);
		}catch(Exception e){
		logger.error("getGrowthChartTransaction failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
}
