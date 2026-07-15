package com.param.global.unit.service;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesDto;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface GrowthDevelopmentalMilestoneService {

	Response saveGrowthDevelopmentalMilestonesTransaction(List<GrowthDevelopmentalMilestonesDto> listGrowthDevelopmentalMilestonesDto) throws ApplicationException;
	Response getGrowthDevelopmentalMilestonesTransaction(GrowthDevelopmentalMilestonesReqDto growthDevelopmentalMilestonesReqDto) throws ApplicationException;

}
