package com.param.procurement.purchase.purchaseOrder.dao;

import java.util.List;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.purchaseOrder.model.LocPurchaseOrderDetail;

public interface PurchaseOrderDetailsDao {
	
	public Integer savePurchaseOrderDetails(LocPurchaseOrderDetail locPurchaseOrderDetails);
	
	public Integer updatePurchaseOrderDetails(PurchaseOrderMasterDto PurchaseOrderDto);
	
	public Integer updatePurchaseOrderDetailStatus(PurchaseOrderStatusDto purchaseOrderStatusDto);
	
	public List<Object[]> retrievePuchaseOrderDetails(Integer id);
	
	public Integer updatePurchaseOrderDetailsApproval(PurchaseOrderDetailDto purchaseOrderDeatilsDto);

}
