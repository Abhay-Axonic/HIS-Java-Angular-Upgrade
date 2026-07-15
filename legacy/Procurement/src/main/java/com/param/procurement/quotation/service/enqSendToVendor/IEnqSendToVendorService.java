package com.param.procurement.quotation.service.enqSendToVendor;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultiVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultipleVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorReqDto;
import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;

public interface IEnqSendToVendorService {

	QuotationReqDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getQuotationRequestSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto);

	Object saveEnquirySendToVendor(EnqSendToMultiVendorReqDto enqSendToVendorReqDto);

	Object getEnquirySendToVendorList(EnqSendToVendorReqDto enqSendToVendorReqDto);

	Object getQuotationVendor(Integer id, Integer orgId);

	Object updateEnquirySendToVendor(EnqSendToVendorReqDto enqSendToVendorReqDto);

	Object cancelQuotationVendor(EnqSendToVendorReqDto enqSendToVendorReqDto);

	Object approveQuotationVendor(EnqSendToVendorReqDto enqSendToVendorReqDto);

	Object getQuotationRequestDetails(List<EnqSendToVendorReqDto> enqSendToVendorReqDtoList);

	Object getQuotationRequesMultipleSearchtList(EnqSendToMultipleVendorReqDto enqSendToVendorReqDto);

}
