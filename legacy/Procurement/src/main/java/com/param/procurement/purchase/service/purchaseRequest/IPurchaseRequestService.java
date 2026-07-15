package com.param.procurement.purchase.service.purchaseRequest;

import java.util.List;

import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequest;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDetailResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDropdownResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestSearchResponse;

public interface IPurchaseRequestService {

	/*List<com.param.procurement.purchase.model.PurchaseRequest> retrivePurchaseRequestData();
	boolean createPurchaseRequest(PurchaseRequestDto purchaseRequestDto);
	boolean purchaseRequestApproval(PurchaseRequestDto purchaseRequestDto);*/

	PurchaseRequestDropdownResponse getDropdowns(Integer unitId, Integer userId);
	List<PurchaseRequestSearchResponse> purchaseRequestSearch(PurchaseRequest purchaseRequestSearchDto);
	PurchaseRequestDetailResponse purchaseRequestDetails(Long purchaseRequestId, Integer unitId, Integer orgId);
	Object savePurchaseRequest(PurchaseRequest request) throws Exception;
	Object updatePurchaseRequest(PurchaseRequest request);
	Object approvePurchaseRequest(PurchaseRequest request);
	Object cancelPurchaseRequest(PurchaseRequest request);
}
