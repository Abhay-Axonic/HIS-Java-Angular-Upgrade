package com.param.lis.unit.service;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.TestMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMultiParamTestMasterService
{
	public Response addMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException;

	public Response getMultyParameterTest(Integer orgId, Integer orgUnitId, Integer testId) throws ApplicationException;

	public Response updateMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException;

	public Response activeInactiveMultiParamTest(Integer orgId, Integer orgUnitId, Integer testId,
			Character testStatus,Integer serviceId) throws ApplicationException;

	public Response listMultiParamTestMaster(Integer orgId, Character isHistoCyto,Integer orgUnitId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getTotalMultiTestRecord(Integer orgId,Character isHistoCyto, Integer orgUnitId) throws ApplicationException;
	
	public Response getParametersByTest(Integer testId)throws ApplicationException;

	public Response<TestMasterDto, Integer> multiParamTestMasterUniversalSearch(Integer orgId, Character isHistoCyto,
			Integer orgUnitId, Integer offset, Integer recordPerPage, String testDesc)  throws ApplicationException;

}
