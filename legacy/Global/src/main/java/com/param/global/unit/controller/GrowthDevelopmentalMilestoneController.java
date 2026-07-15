package com.param.global.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesDto;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesReqDto;
import com.param.global.unit.service.GrowthDevelopmentalMilestoneService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class GrowthDevelopmentalMilestoneController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(GrowthDevelopmentalMilestoneController.class);

	
	@Autowired
	private GrowthDevelopmentalMilestoneService growthDevelopmentalMilestoneService;
		
	@RequestMapping(value = "/growthDevelopmentalMilestones/patient" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveGrowthDevelopmentalMilestoneTransaction(@RequestBody List<GrowthDevelopmentalMilestonesDto> growthDevelopmentMilestoneList) {
		try {
			return growthDevelopmentalMilestoneService.saveGrowthDevelopmentalMilestonesTransaction(growthDevelopmentMilestoneList);
		} catch (Exception e) {
		logger.error("saveGrowthDevelopmentalMilestoneTransaction failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}

	@RequestMapping(value = "/growthDevelopmentalMilestones/patient/detail" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getGrowthDevelopmentalMilestonesByPatient(@RequestBody GrowthDevelopmentalMilestonesReqDto reqDto) {
		try {
			return growthDevelopmentalMilestoneService.getGrowthDevelopmentalMilestonesTransaction(reqDto);
		} catch (Exception e) {
		logger.error("getGrowthDevelopmentalMilestonesByPatient failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}

}
