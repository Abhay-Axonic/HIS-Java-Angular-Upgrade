package com.param.lis.global.dao;

import com.param.global.dto.TestCodeHeadDto;
import com.param.lis.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITestCostHeadDao {
	
	Response addTestCodeHead(TestCodeHeadDto testCodeHeadDto) throws ApplicationException;
	
	Response updateTestCodeHead(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;
	
	Response getTestCodeHeadList(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response updateTestCodeHeadStatus(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;
	
	Response countOfTestCodeHeadList(TestCodeHeadDto companyMasterDto)throws ApplicationException;
	
	Response getTestCodeHeadListById(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;
	
	Response getActiveTestCodeHeadList(TestCodeHeadDto dto) throws ApplicationException;

	Response checkForUniqueCode(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response checkForUniqueDesc(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response checkUpdateForUniqueCode(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response checkUpdateForUniqueDesc(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;
}
