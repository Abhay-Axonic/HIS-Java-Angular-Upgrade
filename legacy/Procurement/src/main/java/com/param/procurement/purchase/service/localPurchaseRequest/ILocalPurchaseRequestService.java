package com.param.procurement.purchase.service.localPurchaseRequest;

import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseReqStatusDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;

public interface ILocalPurchaseRequestService {

	public LocalPurchaseRequestDropDownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	public Object getLocalPuchaseRequestList(LocPurchaseRequestDto locPurchaseReqDto);

	public Object saveLocalPurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto);
	
	public Object getLocalPurchaseRequest(Long id, Integer unitId, Integer orgId);
	
	public Object updateLocalPurchaseReq(LocPurchaseRequestDto locPurchaseReqDto);
	
	public Object approvePurchaseRequest(LocalPurchaseReqStatusDto locPurchaseReqStatusDto);
	
	public Object cancelLocalPurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto);

}

