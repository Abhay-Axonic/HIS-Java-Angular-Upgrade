package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.model.GrowthChartMaster;
import com.param.global.unit.dto.GrowthChartMasterDto;
import com.param.global.unit.dto.GrowthChartReqDto;
import com.param.global.unit.dto.TGrowthChartDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IGrowthChartDao  extends IGenericDao<GrowthChartMaster, Integer> {
	
	Response saveGrowthMaster(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException;
	Response upadateGrowthMaster(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException;
	Response checkAgeExistOrNotAdd(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException;
	Response checkAgeExistOrNotUpdate(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException;
	Response deleteGrowthChartMaster(int orgId,int unitId,int growthChartId) throws ApplicationException;
	Response getGrowthMaster(int orgId,int unitId,int Offset,int recordPerPage) throws ApplicationException;
	Response getGrowthMasterById(int orgId,int unitId,int growthChartId) throws ApplicationException;
	Response getHeightUnit(int orgId,int unitId) throws ApplicationException;
	Response getWeightUnit(int orgId,int unitId) throws ApplicationException;
	Response getCircumferenceUnit(int orgId,int unitId) throws ApplicationException;
	
	Response saveGrowthChartTransaction(TGrowthChartDto tGrowthChartDto) throws ApplicationException;
	Response getGrowthChartTransaction(GrowthChartReqDto growthChartReqDto) throws ApplicationException;

}
