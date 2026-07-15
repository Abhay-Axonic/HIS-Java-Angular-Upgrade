package com.param.procurement.quotation.dao.enqSendToVendor;

import java.util.List;

import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultipleVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorReqDto;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDetailsResponse;

public interface IEnqSendToVendorDao {

	List<QuotationRequest> getQuotationRequestSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto,List<Integer> statusIds);

	QuotationVendor saveQuotationVendor(QuotationVendor quotationVendor);

	List<QuotationVendor> getQuotationVendorSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto, Integer screenId);

	List<QuotationVendorDetail> getQuotationVendorDetail(Integer id);

	QuotationVendor getQuotationVendor(Integer id);

	QuotationVendor updateQuotationVendor(QuotationVendor quotationVendor);

	List<PurchaseRequestDetailsResponse> getPurchaseRequestDetail(Integer qrId);

	QuotationRequestDetail getQuotationRequestDetailsList(Integer itemId,Integer qrId);

	QuotationRequest getQuotationRequestList(Integer qrId);

	List<QuotationRequest> getQuotationRequestMultipleSearchList(EnqSendToMultipleVendorReqDto enqSendToVendorReqDto,
			List<Integer> statusIds);

}
