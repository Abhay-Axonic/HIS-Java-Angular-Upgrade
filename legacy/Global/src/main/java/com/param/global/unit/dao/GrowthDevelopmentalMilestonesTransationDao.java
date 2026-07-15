package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.model.GrowthDevelopmentalMilestonesTransaction;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesDto;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesReqDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface GrowthDevelopmentalMilestonesTransationDao  extends IGenericDao<GrowthDevelopmentalMilestonesTransaction, Integer> {
	
	Response saveGrowthDevelopmentalMilestonesTransation(GrowthDevelopmentalMilestonesDto growthDevelopmentalMilestonesDto) throws ApplicationException;
	Response getGrowthDevelopmentalMilestonesTransation(GrowthDevelopmentalMilestonesReqDto growthDevelopmentalMilestonesReqDto) throws ApplicationException;

}
