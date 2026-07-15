package com.param.procurement.purchase.service.purchaseRequestApproval;


import java.util.List;

import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApproval;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDetail;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDropDown;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalResponse;

public interface IPurchaseRequestApprovalService {

    PurchaseRequestApprovalDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId);
    List<PurchaseRequestApprovalResponse> purchaseRequestApprovalSearch(PurchaseRequestApproval search);
    PurchaseRequestApprovalDetail purchaseRequestDetails(Long purchaseRequestId, Integer unitId, Integer orgId);
    Object cancelPurchaseRequestApproval(PurchaseRequestApproval request);
    Object approvePurchaseRequestApproval(PurchaseRequestApproval request);
    Object updatePurchaseRequestApproval(PurchaseRequestApproval request);
}
