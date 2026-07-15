package com.param.procurement.quotation.dao.quotationRequest;

import java.util.List;

import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDetailsResponse;
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDto;

public interface IQuotationRequestDao {

	List<QuotationRequest> searchQuotationRequestList(QuotationRequestDto quotationRequestDto, Integer screenId);

	QuotationRequest getQuotationRequest(Integer id);

	List<QuotationRequestDetail> getQuotationRequestDetail(Integer id);

	QuotationRequest saveQuotationRequest(QuotationRequest quotationRequest);

	QuotationRequest updateQuotationRequest(QuotationRequest quotationRequest);

	List<PurchaseRequestDetailsResponse> getPurchaseRequestDetail(Integer qrId);

	List<PurchaseRequestDetail> getPurchaseRequestDetails(List<Integer> purchaseRequestDetailIds);

	PurchaseRequestDetail getPurchaseRequestDetails(Integer purchaseRequestDetailId);

	Long getLeaseQunatity(Integer storeId, Integer itemId);

	QuotationRequestDetail getQuotationRequestPendingQuantity(Integer qrId , Integer itemId);

}
