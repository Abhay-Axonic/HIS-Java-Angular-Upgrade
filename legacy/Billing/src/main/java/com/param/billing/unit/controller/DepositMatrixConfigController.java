
/**@author Ganesh Chaudhari*/

package com.param.billing.unit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.dto.DepositMatrixConfigMasterDto;
import com.param.billing.unit.dto.DepositMatrixReqDto;
import com.param.billing.unit.service.IDepositMatrixConfigService;

@RestController
@RequestMapping("/api/billing/unit")
@SuppressWarnings({"rawtypes","unchecked"})
public class DepositMatrixConfigController  implements ICommonConstants {
	
	@Autowired
	private IDepositMatrixConfigService iDepositMatrixConfigService;
	
	@RequestMapping(value="/depositMatrix/{orgId}/{unitId}/{offset}/{recordPerPage}",method=RequestMethod.GET)
	public Response getDepositMatrixList(@PathVariable Map<String,String> params)
	{
		try {
			
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int offset = Integer.parseInt(params.get("offset").trim());
			int recordPerPage = Integer.parseInt(params.get("recordPerPage").trim());
			return iDepositMatrixConfigService.getDepositMatrixList(orgId,unitId,offset,recordPerPage);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/depositMatrix", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDepositMatrixConfig(
			@RequestBody DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) {
		try {
			return iDepositMatrixConfigService.saveDepositMatrixConfig(depositMatrixConfigMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/depositMatrix/{mDepositMatrixId}", method = RequestMethod.GET)
	public @ResponseBody Response getDepositMatrixConfig(@PathVariable("mDepositMatrixId") int mDepositMatrixId) {
		try {
			return iDepositMatrixConfigService.getDepositMatrixConfig(mDepositMatrixId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/depositMatrix", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDepositMatrixConfig(
			@RequestBody DepositMatrixConfigMasterDto depositMatrixConfigMasterDto) {
		try {
			return iDepositMatrixConfigService.updateDepositMatrixConfig(depositMatrixConfigMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/depositMatrix/{mDepositMatrixId}/{status}", method = RequestMethod.GET)
	public @ResponseBody Response updateDepositMatrixConfigStatus(@PathVariable Map<String,String> params) {
		try {
			int mDepositMatrixId = Integer.parseInt(params.get("mDepositMatrixId").trim());
			char status = params.get("status").trim().charAt(0);
			return iDepositMatrixConfigService.updateDepositMatrixConfigStatus(mDepositMatrixId,status);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/depositMatrix/count/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getDepositMatrixConfigCount(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iDepositMatrixConfigService.getDepositMatrixConfigCount(orgId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/depositMatrixConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDepositMatrixConfigList(@RequestBody DepositMatrixReqDto depositMatrixReqDto) {
		try {
			return iDepositMatrixConfigService.getDepositMatrixConfigList(depositMatrixReqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
