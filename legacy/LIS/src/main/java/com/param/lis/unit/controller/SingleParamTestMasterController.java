package com.param.lis.unit.controller;

import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestMasterForOnHoldDto;
import com.param.lis.unit.service.ISingleParamTestMasterService;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/unit")
public class SingleParamTestMasterController implements ICommonConstants, IError
{
	@Autowired
	private ISingleParamTestMasterService iTestMasterService;

	final static Logger logger = Logger.getLogger(SingleParamTestMasterController.class);

	@RequestMapping(value = "/addSingleParameterTest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSingleParameterTest(@RequestBody TestMasterDto  testMasterDto)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			 
			response = iTestMasterService.addSingleParameterTest(testMasterDto);
			return response;
			// return null;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	

	@RequestMapping(value = "/getSingleParameterTest/{orgId}/{orgUnitId}/{testId}", method = RequestMethod.GET)
	public @ResponseBody Response getSingleParameterTest(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, @PathVariable(value = "testId") Integer testId)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.getSingleParameterTest(orgId, orgUnitId, testId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/updateSingleParameterTest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSingleParameterTest(@RequestBody TestMasterDto testMasterDto)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.updateSingleParameterTest(testMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
		return response;
	}
	/*@RequestMapping(value = "/updateSingleParameterTest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


	Response<TestMasterDto, Integer> response = null;
	try {

	JsonCheck.mapJsonToObject(sampleMasterDto, TestMasterDto.class);

	return null;
	} catch (Exception e) {
	
	throw e;
	}
	}*/

	@RequestMapping(value = "/activeInactiveTest/{orgId}/{orgUnitId}/{testId}/{parameterId}/{testStatus}/{serviceId}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveTest(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, @PathVariable(value = "testId") Integer testId
			,@PathVariable(value = "parameterId") Integer parameterId,
			@PathVariable(value = "testStatus") Character testStatus,
			@PathVariable(value = "serviceId") Integer serviceId)  
		
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.activeInactiveTest(orgId, orgUnitId, testId,parameterId, testStatus,serviceId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
		return response;
	}

	@RequestMapping(value = "/listTestMaster/{orgId}/{isHistoCyto}/{orgUnitId}/{offset}/{recordPerPage}/{searchKeyword}", method = RequestMethod.GET)
	public @ResponseBody Response listTestMaster(@PathVariable(value = "orgId") Integer orgId,@PathVariable(value = "isHistoCyto") Character isHistoCyto,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,
			@PathVariable(value = "searchKeyword") String searchKeyword)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			searchKeyword = (searchKeyword == null || searchKeyword.equalsIgnoreCase("null")) ? "" : searchKeyword;

			response = iTestMasterService.listTestMaster(orgId, isHistoCyto, orgUnitId, offset, recordPerPage, searchKeyword);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalTestRecord/{orgId}/{isHistoCyto}/{orgUnitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalTestRecord(@PathVariable(value = "orgId") Integer orgId, @PathVariable(value = "isHistoCyto") Character isHistoCyto,
			@PathVariable(value = "orgUnitId") Integer orgUnitId)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.getTotalTestRecord(orgId,isHistoCyto, orgUnitId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}	

	@RequestMapping(value = "/testDetails/{orgId}/{unitId}/{testId}", method = RequestMethod.GET)
	public @ResponseBody Response getTestDetails(@PathVariable(value = "orgId") Integer orgId, @PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "testId") Integer testId)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.getTestDetails(orgId,unitId, testId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}	
	
	
	@RequestMapping(value = "/getDeltaDetails/{orgId}/{unitId}/{testId}", method = RequestMethod.GET)
	public @ResponseBody Response getDeltaDetails(@PathVariable(value = "orgId") Integer orgId, @PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "testId") Integer testId)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.getDeltaDetails(orgId,unitId, testId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/checkServiceAvaiable/{addOrUpdate}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response checkServiceAvaiable(@RequestBody TestMasterDto  testMasterDto,@PathVariable(value = "addOrUpdate") boolean addOrUpdate)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.checkServiceAvaiable(testMasterDto,addOrUpdate);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getEquipmentList/{testId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getEquipmentList(@PathVariable(value = "testId") Integer testId,
			@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId)
			
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.getEquipmentList(testId,orgId,unitId );
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getParameterTestOnReportEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getParameterTestOnReportEntry(@RequestBody Map<Integer,Integer> labAndTestIds)
	{
		try
		{
			return iTestMasterService.getParameterTestOnReportEntry(labAndTestIds);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/cytologyTestMasterUniversalSearch/{orgId}/{orgUnitId}/{offset}/{recordPerPage}/{testDesc}/{isHistoCyto}",method = RequestMethod.GET)
	public @ResponseBody Response cytologyTestMasterUniversalSearch(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,
			@PathVariable(value = "testDesc") String testDesc,
			@PathVariable(value = "isHistoCyto") Character isHistoCyto)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.cytologyTestMasterUniversalSearch(orgId,orgUnitId,offset,recordPerPage,testDesc,isHistoCyto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/testMasterUniversalSearch/{orgId}/{isHistoCyto}/{orgUnitId}/{offset}/{recordPerPage}/{testDesc}", method = RequestMethod.GET)
	public @ResponseBody Response testMasterUniversalSearch(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "isHistoCyto") Character isHistoCyto,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, 
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,
			@PathVariable(value = "testDesc") String testDesc)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			response = iTestMasterService.testMasterUniversalSearch(orgId, isHistoCyto, orgUnitId, offset, recordPerPage,testDesc);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getTotalSerologyTestRecord/{orgId}/{isHistoCyto}/{orgUnitId}//{searchKeyword}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalSerologyTestRecord(@PathVariable(value = "orgId") Integer orgId, @PathVariable(value = "isHistoCyto") Character isHistoCyto,
			@PathVariable(value = "orgUnitId") Integer orgUnitId, @PathVariable(value = "searchKeyword") String searchKeyword)
	{
		Response<TestMasterDto, Integer> response = null;
		try
		{
			searchKeyword = (searchKeyword == null || searchKeyword.equalsIgnoreCase("null")) ? "" : searchKeyword;
			response = iTestMasterService.getTotalSerologyTestRecord(orgId,isHistoCyto, orgUnitId, searchKeyword);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/getOnHoldReportEntryDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOnHoldReportEntryDetail(@RequestBody TestMasterForOnHoldDto dto)
	{
		try
		{
			return iTestMasterService.getOnHoldReportEntryDetail(dto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}
}
