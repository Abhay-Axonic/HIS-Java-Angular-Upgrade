package com.param.lis.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TestCostingMasterDto;
import com.param.lis.global.service.ITestCostingMasterService;

@RestController
@RequestMapping(value = "/LIS/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestCostingMasterController implements ICommonConstants,IError{

@Autowired ITestCostingMasterService iTestCostingMasterService;

	@RequestMapping(value="/addTestHeadCosting" ,method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	 public @ResponseBody Response addTestHeadCosting(@RequestBody TestCostingMasterDto dto) {
			try {
				return iTestCostingMasterService.addTestHeadCosting(dto);
			} catch (Exception e) {
				
				throw e;
			}
			
		}
	@RequestMapping(value="/getTestHeadCostingById/{costingHeadId}/{orgId}/{unitId}" ,method=RequestMethod.GET)
	 public @ResponseBody Response getTestHeadCostingById(@PathVariable("costingHeadId") Integer costingHeadId ,@PathVariable ("orgId") Integer orgId, 
			 @PathVariable ("unitId") Integer unitId) {
			try {
					TestCostingMasterDto dto=new TestCostingMasterDto();
					dto.setTestCostingMasterId(costingHeadId);
					dto.setOrganizationId(orgId);
					dto.setUnitId(unitId);
					return iTestCostingMasterService.getTestHeadCostingById(dto);
			} catch (Exception e) {
				
				throw e;
			}
			
		}
	@RequestMapping(value="/getTestHeadCostingList//{orgId}/{unitId}" ,method=RequestMethod.GET)
	 public @ResponseBody Response getTestHeadCostingList(@PathVariable ("orgId") Integer orgId, 
			 @PathVariable ("unitId") Integer unitId) {
			try {
					TestCostingMasterDto dto=new TestCostingMasterDto();
					dto.setOrganizationId(orgId);
					dto.setUnitId(unitId);
					return iTestCostingMasterService.getTestHeadCostingList(dto);
			} catch (Exception e) {
				
				throw e;
			}
			
		}
	@RequestMapping(value="/getTestHeadCostByCostingMstrId/{id}" ,method=RequestMethod.GET)
	 public @ResponseBody Response getTestHeadCostingList(@PathVariable ("id") Integer id) {
			try {
					TestCostingMasterDto dto=new TestCostingMasterDto();
					dto.setTestCostingMasterId(id);				
					return iTestCostingMasterService.getTestHeadCostByCostingMstrId(dto);
			} catch (Exception e) {
				
				throw e;
			}
			
		}
}
