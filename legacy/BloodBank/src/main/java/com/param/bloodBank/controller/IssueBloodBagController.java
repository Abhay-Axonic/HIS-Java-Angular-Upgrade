package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.bloodBank.service.IIssueBloodBagService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/bloodBank")
@SuppressWarnings({ "rawtypes" })
public class IssueBloodBagController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(IssueBloodBagController.class);


	@Autowired
	IIssueBloodBagService iIssueBloodBagService;

	@RequestMapping(value = "/saveIssueBloodBag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIssueBloodBag(@RequestBody IssueBloodBagDto issueBloodBagDto) {
		try {
			return iIssueBloodBagService.saveIssueBloodBag(issueBloodBagDto);
		} catch (Exception e) {
			logger.error("saveIssueBloodBag failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getIssueBloodBagOnIssueAcceptanceScreen", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIssueBloodBagOnIssueAcceptanceScreen(@RequestBody BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto) {
		try {
			return iIssueBloodBagService.getIssueBloodBagOnIssueAcceptanceScreen(bloodBagIssueAcceptanceDto);
		} catch (Exception e) {
			logger.error("getIssueBloodBagOnIssueAcceptanceScreen failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/updateIssuedStatusByIssueBloodBagDetailsId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIssuedStatusByIssueBloodBagDetailsId(@RequestBody BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto) {
		try {
			return iIssueBloodBagService.updateIssuedStatusByIssueBloodBagDetailsId(bloodBagIssueAcceptanceDto);
		} catch (Exception e) {
			logger.error("updateIssuedStatusByIssueBloodBagDetailsId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	/*	@RequestMapping(value = "/saveIssueBloodBag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<IssueBloodBagDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, IssueBloodBagDto.class);
			
			 * response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 * response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		}
	}*/
}
