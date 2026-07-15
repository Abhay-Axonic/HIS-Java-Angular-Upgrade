package com.param.procurement.workOrder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.WorkOrderApprovalSettlementDto;
import com.param.global.pharmacy.dto.WorkOrderCreationApprovalDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDocumentDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;
import com.param.procurement.workOrder.service.workOrderCreation.INewWorkOrderCreationService;
import org.apache.log4j.Logger;
//Rohit.K
@RestController
@RequestMapping(value = "/api/procurements/workOrder")
@SuppressWarnings( {"rawtypes","unchecked"})
public class NewWorkOrderCreationController implements ICommonConstants,IError{

	final static Logger logger = Logger.getLogger(NewWorkOrderCreationController.class);
	
	@Autowired  
	private INewWorkOrderCreationService inewWorkOrderCreationService;
	
	@RequestMapping(value = "/saveCreationRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCreationRequest(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.saveCreationRequest(RequestDto);
		} catch (Exception e) {
			logger.error("saveCreationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/workorderRequestDocument", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	public @ResponseBody Response fundSanctionRequestDocument(@ModelAttribute  WorkOrderCreationRequestDocumentDto requestDocumentDto) {
		try {
			return inewWorkOrderCreationService.workorderRequestDocument(requestDocumentDto);
		} catch (Exception e) {
			logger.error("fundSanctionRequestDocument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCreationRequestList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationRequestList(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getCreationRequestList(RequestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getCreationRequestDetailsbyId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationRequestDetailsbyId(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getCreationRequestDetailsbyId(RequestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestDetailsbyId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	@RequestMapping(value = "/requestChangeToPending", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestChangeToPending(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.requestChangeToPending(RequestDto);
		} catch (Exception e) {
			logger.error("requestChangeToPending failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/requestChangeToCancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestChangeToCancel(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.requestChangeToCancel(RequestDto);
		} catch (Exception e) {
			logger.error("requestChangeToCancel failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/requestChangeToReject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestChangeToReject(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.requestChangeToReject(RequestDto);
		} catch (Exception e) {
			logger.error("requestChangeToReject failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getCreationApprovalDetailsbyId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationApprovalDetailsbyId(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return inewWorkOrderCreationService.getCreationApprovalDetailsbyId(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/saveCreationApprovalDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCreationApprovalDetails(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			Response res=  inewWorkOrderCreationService.saveCreationApprovalDetails(apptovalDto);
			if(res.getCode() != null && res.getCode().equals(SUCCESS)){
				inewWorkOrderCreationService.updateorder(apptovalDto);
			}
			return  res;
		} catch (Exception e) {
			logger.error("saveCreationApprovalDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/requestChangeToApproved", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestChangeToApproved(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.requestChangeToApproved(RequestDto);
		} catch (Exception e) {
			logger.error("requestChangeToApproved failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getRequestApprovalList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestApprovalList(@RequestBody  WorkOrderCreationApprovalDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getRequestApprovalList(RequestDto);
		} catch (Exception e) {
			logger.error("getRequestApprovalList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/saveWorkOrderSettlement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveWorkOrderSettle(@RequestBody WorkOrderApprovalSettlementDto RequestDto) {
		try {
			return inewWorkOrderCreationService.saveWorkOrderSettle(RequestDto);
		} catch (Exception e) {
			logger.error("saveWorkOrderSettle failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@RequestMapping(value = "/getRequestSettledList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestSettledList(@RequestBody WorkOrderApprovalSettlementDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getRequestSettledList(RequestDto);
		} catch (Exception e) {
			logger.error("getRequestSettledList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getRequestSettledListDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestSettledListDetails(@RequestBody WorkOrderApprovalSettlementDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getRequestSettledListDetails(RequestDto);
		} catch (Exception e) {
			logger.error("getRequestSettledListDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveCreationApprovalDetails/completionStages", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCreationCompletionDetails(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return inewWorkOrderCreationService.saveCreationCompletionDetails(apptovalDto);
		} catch (Exception e) {
			logger.error("saveCreationCompletionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCreationApprovalDetailsbyId/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationApprovalDetailsbyIdEdit(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return inewWorkOrderCreationService.getCreationApprovalDetailsbyIdEdit(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyIdEdit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCreationRequestList/completionSearch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationRequestListCompletion(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.getCreationRequestListCompletion(RequestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestListCompletion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/requestChangeToApproved/completionSvaeSubmit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestChangeToApprovedCompletion(@RequestBody  WorkOrderCreationRequestDto RequestDto) {
		try {
			return inewWorkOrderCreationService.requestChangeToApprovedCompletion(RequestDto);
		} catch (Exception e) {
			logger.error("requestChangeToApprovedCompletion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCreationApprovalDetailsbyId/getWOCompletedTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getWOCompletedTransaction(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return inewWorkOrderCreationService.getWOCompletedTransaction(apptovalDto);
		} catch (Exception e) {
			logger.error("getWOCompletedTransaction failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCreationApprovalDetailsbyId/settlementDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreationApprovalDetailsbyIdSettlement(@RequestBody  WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return inewWorkOrderCreationService.getCreationApprovalDetailsbyIdSettlement(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyIdSettlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
//	@RequestMapping(value = "/saveWorkOrderSettlement", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody Response saveWorkOrderSettle(@RequestBody String sampleMasterDto) {
//
//		Response<WorkOrderApprovalSettlementDto, Integer> response = null;
//		try {
//
//			JsonCheck.mapJsonToObject(sampleMasterDto, WorkOrderApprovalSettlementDto.class);
//
//			// response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
//			// response;
//
//			return null;
//		} catch (Exception e) {
//			
//			return new Response(ERROR, ERROR_CODE, null, null, null);
//		}
//	}
	
	

}
