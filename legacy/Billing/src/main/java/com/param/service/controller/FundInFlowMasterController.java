package com.param.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.TrustMasterDto;
import com.param.service.services.IFundInFlowMasterService;

@RestController
@RequestMapping(value = "/api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FundInFlowMasterController implements ICommonConstants,IError  {
	
	@Autowired  
	private IFundInFlowMasterService iFundInFlowMasterService;

	@RequestMapping(value = "/getTrustList/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTrustList(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<TrustMasterDto, Integer> response = null;
		try {
			response = iFundInFlowMasterService.getTrustList(orgId, unitId);
			return response;
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getTrustDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTrustDetailsById(@RequestBody TrustMasterDto trustMasterDto) {
		try {

			 return iFundInFlowMasterService.getTrustDetailsById(trustMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/saveFundDeatils", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveFundDeatils(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto) {
		 
		Response<FundInFlowDetailsDto, Integer> response = null;
		try {
			 
			response = iFundInFlowMasterService.saveFundDeatils(fundInFlowDetailsDto);
					return response;
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getfundInflowDetailsByTrust", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getfundInflowDetailsByTrust(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto) {
		 
		try {
			 
			return iFundInFlowMasterService.getfundInflowDetailsByTrust(fundInFlowDetailsDto);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getCollectionList/{orgId}/{unitId}", method = RequestMethod.POST)
	public @ResponseBody Response getCollectionList(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto,
			@PathVariable(value = "orgId") Integer orgId,
       	@PathVariable(value = "unitId") Integer unitId) {
		Response<FundInFlowDetailsDto, Integer> response = null;
		try {
			response = iFundInFlowMasterService.getCollectionList(orgId, unitId,fundInFlowDetailsDto);
			return response;
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getFundCollectionListByTrust/{orgId}/{unitId}", method = RequestMethod.POST)
	public @ResponseBody Response getFundCollectionListByTrust(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto,
			@PathVariable(value = "orgId") Integer orgId,
       	@PathVariable(value = "unitId") Integer unitId) {
		Response<FundInFlowDetailsDto, Integer> response = null;
		try {
			response = iFundInFlowMasterService.getFundCollectionListByTrust(orgId, unitId,fundInFlowDetailsDto);
			return response;
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAllfundInflowDetailsByTrust", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllfundInflowDetailsByTrust(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto) {
		 
		try {
			 
			return iFundInFlowMasterService.getAllfundInflowDetailsByTrust(fundInFlowDetailsDto);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInflowDetailsByFundInflowId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInflowDetailsByFundInflowId(@RequestBody  FundInFlowDetailsDto fundInFlowDetailsDto) {
		 
		try {
			 
			return iFundInFlowMasterService.getInflowDetailsByFundInflowId(fundInFlowDetailsDto);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}


