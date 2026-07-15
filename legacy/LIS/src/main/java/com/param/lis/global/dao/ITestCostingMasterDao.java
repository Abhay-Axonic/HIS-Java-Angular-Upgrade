package com.param.lis.global.dao;

import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TestCostingMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITestCostingMasterDao {


	Response addTestHeadCosting(TestCostingMasterDto dto) throws ApplicationException;

	Response saveTestCodeMapperDetails(TestCostingMasterDto testCostingMasterDto)throws ApplicationException;

	Response getTestHeadCostingById(TestCostingMasterDto dto) throws ApplicationException;

	Response getTestHeadCostingList(TestCostingMasterDto dto)throws ApplicationException;

	Response getTestHeadCostByCostingMstrId(TestCostingMasterDto dto)throws ApplicationException;

}
