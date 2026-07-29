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
import com.param.global.common.Response;
import com.param.procedure.service.ITrustMasterService;
import com.param.service.dto.TrustMasterDto;

import in.param.exception.ApplicationException;

@RestController
@RequestMapping(value = "/api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TrustMasterController implements ICommonConstants,IError {

	 @Autowired
	  private ITrustMasterService iTrustMasterService;

@RequestMapping(value = "/addTrust", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTrust(@RequestBody  TrustMasterDto trustMasterDto) throws ApplicationException {
		return iTrustMasterService.saveTrust(trustMasterDto);
	}
	
/*	@RequestMapping(value = "/updateTrustMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {

		Response<TrustMasterDto, Integer> response = null;
		try {

			JsonCheck.mapJsonToObject(sampleMasterDto, TrustMasterDto.class);
			
		     response = iSampleMasterService.addSampleMaster(sampleMasterDto);
			 return response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}*/
 @RequestMapping(value = "/getTrustbyId/{orgId}/{unitId}/{trustId}", method = RequestMethod.GET)
	public @ResponseBody Response getTrustMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "trustId") Integer trustId) throws ApplicationException {
		return iTrustMasterService.getTrustMasterById(orgId, unitId, trustId);
	}

 @RequestMapping(value = "/updateTrustMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTrustMaster(@RequestBody TrustMasterDto trustMasterDto) throws ApplicationException {
		return iTrustMasterService.updateTrustMaster(trustMasterDto);
	}

 @RequestMapping(value = "/activeInactiveTrust/{orgId}/{trustId}/{trustStatus}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveTrust(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "trustId") Integer trustId,
			@PathVariable(value = "trustStatus") Character trustStatus) throws ApplicationException {
		return iTrustMasterService.activeInactiveTrustMaster(orgId, trustId, trustStatus);
	}

	@RequestMapping(value = "/listTrustMaster", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response listTrustMaster(@RequestBody TrustMasterDto trustMasterDto) throws ApplicationException {
		return iTrustMasterService.listTrustMaster(trustMasterDto);
	}

	@RequestMapping(value = "/getTotalTrustMasterRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalTrustMasterRecord(@RequestBody TrustMasterDto trustMasterDto) throws ApplicationException {
		return iTrustMasterService.getTotalTrustMasterRecord(trustMasterDto);
	}

	@RequestMapping(value = "/getActiveTrustList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveTrustList(@RequestBody TrustMasterDto trustMasterDto) throws ApplicationException {
		return iTrustMasterService.getActiveTrustList(trustMasterDto);
	}


  @RequestMapping(value = "/getVisitType/{trustId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getVisitType(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "trustId") Integer trustId) throws ApplicationException {
		return iTrustMasterService.getVisitType(trustId,orgId, unitId );
	}


}
