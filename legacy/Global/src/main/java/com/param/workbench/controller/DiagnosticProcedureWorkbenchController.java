package com.param.workbench.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.service.IBillingService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CancelOrderDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.workbench.service.IDiagnosticProcedureWorkbenchService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/dignostic/")
@SuppressWarnings("rawtypes")
public class DiagnosticProcedureWorkbenchController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(DiagnosticProcedureWorkbenchController.class);

	
	@Autowired
	IDiagnosticProcedureWorkbenchService iDiagnosticProcedureWorkbenchService;
	
	@Autowired
	IBillingService globalBillingService;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@RequestMapping(value="/workbench",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureworkbench(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto ) {
		try {
			  return iDiagnosticProcedureWorkbenchService.getProcedureworkbench(radiologyOrdersListDto);
		}catch(Exception e) {
		logger.error("getProcedureworkbench failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/resultEntry",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureResultEntry(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto ) {
		try {
			  return iDiagnosticProcedureWorkbenchService.getProcedureResultEntry(radiologyOrdersListDto);
		}catch(Exception e) {
		logger.error("getProcedureResultEntry failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/resultValidation",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureResultValidation(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto ) {
		try {
			  return iDiagnosticProcedureWorkbenchService.getProcedureResultValidation(radiologyOrdersListDto);
		}catch(Exception e) {
		logger.error("getProcedureResultValidation failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getProcedureWorkListForPrinting",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureWorkListForPrinting(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto ) {
		try {
			  return iDiagnosticProcedureWorkbenchService.getProcedureWorkListForPrinting(radiologyOrdersListDto);
		}catch(Exception e) {
		logger.error("getProcedureWorkListForPrinting failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/acceptRequest",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response acceptRequest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto ) {
		try {
			  return iDiagnosticProcedureWorkbenchService.acceptRequest(radiologyOrdersListDto);
		}catch(Exception e) {
		logger.error("acceptRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/rejectRequest", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response rejectRequest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
				 CancelOrderDto cancelOrderDto =new CancelOrderDto();
				 cancelOrderDto.setAdmissionId(radiologyOrdersListDto.getAdmissionId());
				 cancelOrderDto.setEncounterId(radiologyOrdersListDto.getEncounterId());
				 cancelOrderDto.setOrdCancelReasonId(radiologyOrdersListDto.getReasonId());
				 cancelOrderDto.setOrderDetailsId(radiologyOrdersListDto.getOrderDetailsId());
				 cancelOrderDto.setPreOrPostBilling(radiologyOrdersListDto.getIsPreOrPostBilling());
				 cancelOrderDto.setSpecialityId(radiologyOrdersListDto.getSpecialityId());
				 cancelOrderDto.setUserId(radiologyOrdersListDto.getUserId());
				 cancelOrderDto.setVisitTypeId(radiologyOrdersListDto.getVisitTypeId());
				 Response res = globalBillingService.cancelPendingService(cancelOrderDto);
			if(res.getStatus().equals(SUCCESS))
				iDiagnosticProcedureWorkbenchService.saveDiagnosticProcedureMapper(radiologyOrdersListDto);
			
			return res;
			
		}catch (Exception e) {
		logger.error("rejectRequest failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/diagnosisProcedure/templates",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateByTemplateTypeId(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iDiagnosticProcedureWorkbenchService.getTemplateByTemplateTypeId(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplateByTemplateTypeId failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/printDiagnosisReport", method = RequestMethod.POST)
	public Response printDiagnosisReport(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) throws IOException {
		try {
			return iDiagnosticProcedureWorkbenchService.printDiagnosisReport(radiologyOrdersListDto);
			
			}catch(Exception e){
			logger.error("printDiagnosisReport failed", e);
			}
			return null;
	}
}
