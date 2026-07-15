package com.param.lis.global.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.dto.TestCodeHeadDto;
import com.param.lis.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ITestCostHeadService {
	
	Response addTestCodeHead(TestCodeHeadDto testCodeHeadDto) throws ApplicationException;

	Response updateTestCodeHead(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response getTestCodeHeadList(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response getTestCodeHeadListById(TestCodeHeadDto testCodeHeadDto)throws ApplicationException;

	Response updateTestCodeHeadStatus(TestCodeHeadDto companyMasterDto)throws ApplicationException;

	Response countOfTestCodeHeadList(TestCodeHeadDto companyMasterDto)throws ApplicationException;

	Response getActiveTestCodeHeadList(TestCodeHeadDto dto) throws ApplicationException;

}
