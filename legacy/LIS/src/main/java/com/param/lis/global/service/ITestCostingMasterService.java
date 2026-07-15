package com.param.lis.global.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TestCostingMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ITestCostingMasterService {


	Response addTestHeadCosting(TestCostingMasterDto dto) throws ApplicationException;

	Response getTestHeadCostingById(TestCostingMasterDto dto)throws ApplicationException;

	Response getTestHeadCostingList(TestCostingMasterDto dto)throws ApplicationException;

	Response getTestHeadCostByCostingMstrId(TestCostingMasterDto dto)throws ApplicationException;

}
