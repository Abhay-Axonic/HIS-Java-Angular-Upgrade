package com.param.bloodBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.bloodBank.dto.IntraTransfusionMonitoringMapper;
import com.param.bloodBank.dto.TransfusionDetailsMasterDto;
import com.param.bloodBank.service.IBloodBagIssueAcceptanceService;
import com.param.bloodBank.service.IIssueBloodBagService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank/issueAcceptance")
@SuppressWarnings({"rawtypes","unchecked"})
public class BloodBagIssueAcceptanceController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagIssueAcceptanceController.class);


	@Autowired
	private IBloodBagIssueAcceptanceService iBloodBagIssueAcceptanceService;
	
	@Autowired
	IIssueBloodBagService iIssueBloodBagService;
	
	@RequestMapping(value = "/saveBloodBagIssueAcceptance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBagIssueAcceptance(@RequestBody List<BloodBagIssueAcceptanceDto> bloodBagIssueAcceptanceDto) {
		try {
			//Response res =  iIssueBloodBagDao.updateIssuedStatusByIssueBloodBagDetailsId(bloodBagIssueAcceptanceDto);
			Response res =  iBloodBagIssueAcceptanceService.saveBloodBagIssueAcceptance(bloodBagIssueAcceptanceDto);
			if(res.getStatus().equals(SUCCESS)) {
				for (BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto2 : bloodBagIssueAcceptanceDto) {
					 iIssueBloodBagService.updateIssuedStatusByIssueBloodBagDetailsId(bloodBagIssueAcceptanceDto2);
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		} catch (Exception e) {
			logger.error("saveBloodBagIssueAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getListOnPendingTransfusion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOnPendingTransfusion(@RequestBody BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto) {
		try {
			return iBloodBagIssueAcceptanceService.getListOnPendingTransfusion(bloodBagIssueAcceptanceDto);

		} catch (Exception e) {
			logger.error("getListOnPendingTransfusion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/saveTransfusionDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTransfusionDetails(@RequestBody TransfusionDetailsMasterDto transfusionDetailsMasterDto) {
		try {
			return iBloodBagIssueAcceptanceService.saveTransfusionDetails(transfusionDetailsMasterDto);
		} catch (Exception e) {
		throw e;        	
		}
//		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}	

	
	@RequestMapping(value = "/getTransfusionDetailsList/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getTransfusionDetailsList(@PathVariable ("patientId") Integer patientId,@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return iBloodBagIssueAcceptanceService.getTransfusionDetailsList(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			logger.error("getTransfusionDetailsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
