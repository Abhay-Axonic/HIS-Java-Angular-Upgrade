package com.param.billing.global.transaction.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.billing.global.service.IRequestDiscountService;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.service.ITDoctorShareService;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Controller
@RequestMapping(value = "/api/billing")
@SuppressWarnings({"rawtypes"})
public class RequestDiscountController implements ICommonConstants{

	
	@Autowired
	IRequestDiscountService iRequestDiscountService;
	
	@Autowired
	ITDoctorShareService iTDoctorShareService;
	
	
	@RequestMapping(value = "/requestDiscount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestDiscount(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			
			return iRequestDiscountService.requestDiscount(requestDiscountDto);
		} catch (Exception e) {
		    	
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/requestedDiscount/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestedDiscount(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.getRequestedDiscount(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/requestedDiscount/dashboard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestedDiscountsForDashboard(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.getRequestedDiscountsForDashboard(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/approveRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.approveRequest(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/cancelRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.cancelRequest(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDiscReqDtlsForOPBilling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDiscountRequestDetailsForOPBilling(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.getDiscountRequestDetailsForOPBilling(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/approveRequestForFinancialClearance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRequestForFinancialClearance(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.approveRequestForFinancialClearance(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/approveCnRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveCnRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			Response res = iRequestDiscountService.approveCnRequest(requestDiscountDto);
			
			/*if(res.getObject() != null) {
				CreditNoteMasterDto creditNoteMasterDto = (CreditNoteMasterDto)res.getObject();
				
				iTDoctorShareService.updateDocShareAfterCreditNote(creditNoteMasterDto);
				
				//iTDoctorShareService.getProcessDocShare(creditNoteMasterDto.getUnitId());	
			}*/
			
			
			return res;
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/approveExpenseRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRefundRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.approveExpenseRequest(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getBadDebitRequestObject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBadDebitRequestObject(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.getBadDebitRequestObject(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/approveCompanyReceiptsExpenseRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveCompanyReceiptsExpenseRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.approveCompanyReceiptsExpenseRequest(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getUHIDMergeReuestAcceptData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserUHIDMergeList(@RequestBody RequestDiscountDto requestDiscountDto){
		 try {
			return iRequestDiscountService.getUHIDMergeReuestApprovalData(requestDiscountDto);
		} catch (Exception e)  {
			
			 return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);  
		}
	}
	
	@RequestMapping(value = "/cancelRequestFinancialRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelRequestFinancialRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iRequestDiscountService.cancelRequestFinancialRequest(requestDiscountDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/requestDiscountBillList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestDiscountBillList(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			
			return iRequestDiscountService.requestDiscountBillList(requestDiscountDto);
		} catch (Exception e) {
		    	
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
}
