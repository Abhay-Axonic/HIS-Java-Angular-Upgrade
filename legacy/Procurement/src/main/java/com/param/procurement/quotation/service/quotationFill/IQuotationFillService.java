package com.param.procurement.quotation.service.quotationFill;

import java.util.List;

import com.param.procurement.quotation.dto.quotationFill.QuotationFillDropDownDto;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillRequest;

public interface IQuotationFillService {

	QuotationFillDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getQuotationFill(Integer id);

	Object updateQuotationFill(QuotationFillRequest quotationFillRequest);

	Object saveQuotationFill(QuotationFillRequest quotationFillRequest);

	Object cancelQuotationFill(QuotationFillRequest quotationFillRequest);

	Object approveQuotationFill(QuotationFillRequest quotationFillRequest);

	Object getQuotationVendorSearchList(QuotationFillRequest quotationFillRequest);

	Object getQuotationVendorDetails(List<QuotationFillRequest> quotationFillRequestList);

	Object getQuotationFillList(QuotationFillRequest quotationFillRequest);

}
