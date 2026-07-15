package com.param.procurement.purchase.dao.purchaseRequestApproval;

import com.param.entity.model.procurement.PurchaseRequest;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApproval;

import java.util.List;

public interface IPurchaseRequestApprovalDao {

    List<PurchaseRequest> purchaseRequestApprovalSearch(PurchaseRequestApproval search, Integer purchaseRequestApprovalScreenId, Integer purchaseRequestScreenId);

}
