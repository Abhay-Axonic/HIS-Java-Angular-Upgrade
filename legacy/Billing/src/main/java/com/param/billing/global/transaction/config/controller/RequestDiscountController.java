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

import in.param.exception.ApplicationException;

@Controller
@RequestMapping(value = "/api/billing")
@SuppressWarnings({"rawtypes"})
public class RequestDiscountController implements ICommonConstants{


	@Autowired
	IRequestDiscountService iRequestDiscountService;

	@Autowired
	ITDoctorShareService iTDoctorShareService;


	@RequestMapping(value = "/requestDiscount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestDiscount(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.requestDiscount(requestDiscountDto);
	}

	@RequestMapping(value = "/requestedDiscount/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestedDiscount(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.getRequestedDiscount(requestDiscountDto);
	}

	@RequestMapping(value = "/requestedDiscount/dashboard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRequestedDiscountsForDashboard(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.getRequestedDiscountsForDashboard(requestDiscountDto);
	}

	@RequestMapping(value = "/approveRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.approveRequest(requestDiscountDto);
	}

	@RequestMapping(value = "/cancelRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.cancelRequest(requestDiscountDto);
	}

	@RequestMapping(value = "/getDiscReqDtlsForOPBilling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDiscountRequestDetailsForOPBilling(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.getDiscountRequestDetailsForOPBilling(requestDiscountDto);
	}

	@RequestMapping(value = "/approveRequestForFinancialClearance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRequestForFinancialClearance(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.approveRequestForFinancialClearance(requestDiscountDto);
	}

	@RequestMapping(value = "/approveCnRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveCnRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		Response res = iRequestDiscountService.approveCnRequest(requestDiscountDto);

		/*if(res.getObject() != null) {
			CreditNoteMasterDto creditNoteMasterDto = (CreditNoteMasterDto)res.getObject();

			iTDoctorShareService.updateDocShareAfterCreditNote(creditNoteMasterDto);

			//iTDoctorShareService.getProcessDocShare(creditNoteMasterDto.getUnitId());
		}*/

		return res;
	}

	@RequestMapping(value = "/approveExpenseRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveRefundRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.approveExpenseRequest(requestDiscountDto);
	}

	@RequestMapping(value = "/getBadDebitRequestObject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBadDebitRequestObject(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.getBadDebitRequestObject(requestDiscountDto);
	}

	@RequestMapping(value = "/approveCompanyReceiptsExpenseRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveCompanyReceiptsExpenseRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.approveCompanyReceiptsExpenseRequest(requestDiscountDto);
	}


	@RequestMapping(value = "/getUHIDMergeReuestAcceptData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUserUHIDMergeList(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.getUHIDMergeReuestApprovalData(requestDiscountDto);
	}

	@RequestMapping(value = "/cancelRequestFinancialRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelRequestFinancialRequest(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.cancelRequestFinancialRequest(requestDiscountDto);
	}

	@RequestMapping(value = "/requestDiscountBillList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestDiscountBillList(@RequestBody RequestDiscountDto requestDiscountDto) throws ApplicationException {
		return iRequestDiscountService.requestDiscountBillList(requestDiscountDto);
	}


}
