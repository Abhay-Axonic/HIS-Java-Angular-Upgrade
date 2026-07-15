package com.param.lis.unit.service;

import java.util.Map;
import java.util.Optional;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestMasterForOnHoldDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISingleParamTestMasterService
{
	public Response addSingleParameterTest(TestMasterDto testMasterDto) throws ApplicationException;

	public Response getSingleParameterTest(Integer orgId,
			 Integer orgUnitId,Integer testId) throws ApplicationException;

	public Response updateSingleParameterTest(TestMasterDto testMasterDto) throws ApplicationException;

	public Response activeInactiveTest(Integer orgId, Integer orgUnitId, Integer testId,Integer parameterId, Character testStatus,Integer serviceId)
			throws ApplicationException;

	public Response listTestMaster(Integer orgId,Character isHistoCyto, Integer orgUnitId, Integer offset, Integer recordPerPage,String searchKeyword)
			throws ApplicationException;

	public Response getTotalTestRecord(Integer orgId,Character isHistoCyto, Integer orgUnitId) throws ApplicationException;
	
	public Response checkServiceAvaiable(TestMasterDto testMasterDto,boolean addOrUpdate) throws ApplicationException;
	
	public Response getTestDetails(Integer orgId,Integer unitId, Integer testId) throws ApplicationException;
	public Response getDeltaDetails(Integer orgId,Integer unitId, Integer testId) throws ApplicationException;

	public Response getEquipmentList(Integer testId, Integer orgId, Integer unitId)
			throws ApplicationException;

	public Response getParameterTestOnReportEntry(Map<Integer,Integer> labAndTestIds)throws ApplicationException;

	public Response<TestMasterDto, Integer> cytologyTestMasterUniversalSearch(Integer orgId, Integer orgUnitId,
			Integer offset, Integer recordPerPage, String testDesc, Character isHistoCyto)throws ApplicationException;

	public Response<TestMasterDto, Integer> testMasterUniversalSearch(Integer orgId, Character isHistoCyto,
			Integer orgUnitId, Integer offset, Integer recordPerPage, String testDesc) throws ApplicationException;

	public Response<TestMasterDto, Integer> getTotalSerologyTestRecord(Integer orgId, Character isHistoCyto,
			Integer orgUnitId,  String searchKeyword)throws ApplicationException;

	public Response getOnHoldReportEntryDetail(TestMasterForOnHoldDto dto)throws ApplicationException;

}
