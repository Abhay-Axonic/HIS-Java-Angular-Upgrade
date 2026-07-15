package com.param.procurement.purchase.dao.purchaseRequest;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;

public interface IPurchaseRequestDao {

	/*List<PurchaseRequest>  retrivePurchaseRequestData();

	boolean createPurchaseRequest(PurchaseRequestDto purchaseRequestDto);

	boolean purchaseRequestApproval(PurchaseRequestDto purchaseRequestDto);*/

	List<PurchaseRequest> purchaseRequestSearch(com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequest search, Integer screenId);
	List<PurchaseRequestDetail> purchaseRequestDetails(Long purchaseRequestId);
	PurchaseRequest savePurchaseRequest(PurchaseRequest purchaseRequest);
	PurchaseRequest getPurchaseRequest(Integer id);
	PurchaseRequest updatePurchaseRequest(PurchaseRequest purchaseRequest);
	Long getLeaseQunatity(Integer storeId, Integer itemId);	
	Integer deletePurchaseRequestDetails(Long id, List<Integer> item_id);
	PurchaseRequestDetail getPurchaseRequestDetail(Integer id, Integer pr_id) ;
}
