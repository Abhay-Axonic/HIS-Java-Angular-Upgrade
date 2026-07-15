package com.param.service.controller;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;
import com.param.service.dto.BillFundSanctionDetailsDto;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.FundSanctionAcceptanceRequestDto;
import com.param.service.dto.FundSanctionRequestDto;
import com.param.service.dto.TrustMasterDto;
import com.param.service.services.IFundInFlowMasterService;
import com.param.service.services.IFundSanctionRequestService;

import in.param.exception.ApplicationException;

@RestController
@RequestMapping(value = " /api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FundSanctionRequestController implements ICommonConstants,IError  {

	final static Logger logger = Logger.getLogger(FundSanctionRequestController.class);

	@Autowired
	private IFundSanctionRequestService iFundSanctionRequestService;
	
	@RequestMapping(value = "/saveFundSanctionRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveFundSanctionRequest(@RequestBody  FundSanctionRequestDto fundSanctionRequestDto) {
		try {
			return iFundSanctionRequestService.saveFundSanctionRequest(fundSanctionRequestDto);
		} catch (Exception e) {
			logger.error("saveFundSanctionRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/fundSanctionRequestByPatientId", method = RequestMethod.POST)
	public @ResponseBody Response fundSanctionRequestByPatientId(@RequestBody  FundSanctionRequestDto fundSanctionRequestDto) {
		try {
			return iFundSanctionRequestService.fundSanctionRequestByPatientId(fundSanctionRequestDto);
		} catch (Exception e) {
			logger.error("fundSanctionRequestByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/fundSanctionRequestDocument", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	public @ResponseBody Response fundSanctionRequestDocument(@ModelAttribute  FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto) {
		try {
			return iFundSanctionRequestService.fundSanctionRequestDocument(fundSanctionRequestDocumentDto);
		} catch (Exception e) {
			logger.error("fundSanctionRequestDocument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDocumentsByFundSanctionRequestId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDocumentsByFundSanctionRequestId(@RequestBody  FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto) {
		try {
			return iFundSanctionRequestService.getDocumentsByFundSanctionRequestId(fundSanctionRequestDocumentDto);
		} catch (Exception e) {
			logger.error("getDocumentsByFundSanctionRequestId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/saveApprovalRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response saveApprovalRequest(@RequestBody  FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) {
		try {
			return iFundSanctionRequestService.saveApprovalRequest(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
			logger.error("saveApprovalRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getAllPendingVirtualAcceptance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getAllPendingVirtualAcceptance(@RequestBody  FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) {
		try {
			return iFundSanctionRequestService.getAllPendingVirtualAcceptance(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
			logger.error("getAllPendingVirtualAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getInflowAcceptedRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getInflowAcceptedRequest(@RequestBody  FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) {
		try {
			return iFundSanctionRequestService.getInflowAcceptedRequest(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
			logger.error("getInflowAcceptedRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/getVirtualRequestPayments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getVirtualRequestPayments(@RequestBody  FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) {
		try {
			return iFundSanctionRequestService.getVirtualRequestPayments(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
			logger.error("getVirtualRequestPayments failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllFundApprovalRequestByPatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getAllFundApprovalRequestByPatient(@RequestBody  FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) {
		try {
			return iFundSanctionRequestService.getAllFundApprovalRequestByPatient(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
			logger.error("getAllFundApprovalRequestByPatient failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveBillFundDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response saveBillFundDetails(@RequestBody  BillFundSanctionDetailsDto billFundSanctionDetailsDto) {
		try {
			return iFundSanctionRequestService.saveBillFundDetails(billFundSanctionDetailsDto);
		} catch (Exception e) {
			logger.error("saveBillFundDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillFundDetailsByBillMasterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getBillFundDetailsByBillMasterId(@RequestBody  BillFundSanctionDetailsDto billFundSanctionDetailsDto) {
		try {
			return iFundSanctionRequestService.getBillFundDetailsByBillMasterId(billFundSanctionDetailsDto);
		} catch (Exception e) {
			logger.error("getBillFundDetailsByBillMasterId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}


