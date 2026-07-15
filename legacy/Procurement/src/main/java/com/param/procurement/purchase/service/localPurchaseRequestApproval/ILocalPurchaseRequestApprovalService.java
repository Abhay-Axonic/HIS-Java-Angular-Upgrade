package com.param.procurement.purchase.service.localPurchaseRequestApproval;

import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;

public interface ILocalPurchaseRequestApprovalService {
	
	public LocalPurchaseRequestDropDownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);
	
	public Object getLocalPuchaseRequestList(LocPurchaseRequestDto locPurchaseReqDto);
	
	public Object getLocalPurchaseRequest(Long id, Integer unitId, Integer orgId);
	
	public Object approvePurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto);
	
	public Object cancelLocalPurchaseRequest(LocPurchaseRequestDto locPurchaseReqDto);

}
