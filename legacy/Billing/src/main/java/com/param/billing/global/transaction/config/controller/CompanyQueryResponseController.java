package com.param.billing.global.transaction.config.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceQueryReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceQueryDetailDto;
import com.param.billing.global.transaction.config.service.ICompanyQueryResponseService;
import com.param.global.common.JsonCheck;

@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings("rawtypes")
public class CompanyQueryResponseController implements ICommonConstants{
	
	@Autowired
	private ICompanyQueryResponseService iCompanyQueryResponseService;
	
	/**
	 * TO CREATE COMPANY QUERY AGAINST MULTIPLE INVOICE
	 * @param reqDtos
	 * @return
	 */
	@RequestMapping(value="/saveCompanyQueryResponse", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response saveCompanyQueryResponse(@RequestBody CompanyInvoiceQueryReqDto reqDtos) {
		try {
			return iCompanyQueryResponseService.saveCompanyQueryResponse(reqDtos);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	/**
	 * TO UPDATE REMARK QUERY RESPONSE 
	 * @param listReqDto
	 * @return
	 */
	@RequestMapping(value="/updateCompanyQueryResponseDetailsRemark", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(@RequestBody List<TCompanyInvoiceQueryDetailDto> listReqDto) {
		try {
			return iCompanyQueryResponseService.updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(listReqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * TO GET PENDING QUERIES & RESPONSES DETAILS
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value="/getPendingCompanyQueryResponseDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response getPendingCompanyQueryResponseDetails(@RequestBody TCompanyInvoiceQueryDetailDto reqDto) {
		try {
			return iCompanyQueryResponseService.getPendingCompanyQueryResponseDetails(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	

}
