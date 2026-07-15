package com.param.procurement.quotation.service.quotationRequest;

import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDto;

public interface IQuotationRequestService {

	QuotationReqDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getQuotationRequestList(QuotationRequestDto quotationRequestDto);

	Object saveQuotationRequest(QuotationRequestDto quotationRequestDto);

	Object updateQuotationRequest(QuotationRequestDto quotationRequestDto);

	Object approveQuotationRequest(QuotationRequestDto quotationRequestDto);

	Object cancelQuotationRequest(QuotationRequestDto quotationRequestDto);

	Object getQuotationRequest(Integer id);

}
