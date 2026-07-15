package com.param.procurement.purchase.dao.localPurchaseRequest;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;


public interface ILocalPurchaseRequestDao {

	public List<PurchaseRequest> searchLocalPuchaseRequest(LocPurchaseRequestDto locPurchaseReqDto,Integer screenId,Integer localPurchaseReqId) ;

	public PurchaseRequest getPurchaseRequest(Long id);
	
	public List<PurchaseRequestDetail> getPurchaseRequestDetail(Long purchaseRequestId);
	
	public PurchaseRequest saveLocalPurchaseRequest(PurchaseRequest purchaseRequest);
	
	public PurchaseRequest updateLocalPurchaseRequest(PurchaseRequest purchaseRequest);
	
	public List<StoreIndentDetail> getIndentIdList(Integer purchaseReqDetailsId);
	
	public Integer updateStoreIndentDetailPrId(Integer indentId, Integer prId);

	Long getLeaseQunatity(Integer storeId, Integer itemId);

	public PurchaseRequestDetail saveLocalPurchaseRequestDetail(PurchaseRequestDetail purchaseRequestDetail);

}
