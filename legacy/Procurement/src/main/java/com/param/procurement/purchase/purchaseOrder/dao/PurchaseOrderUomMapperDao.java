package com.param.procurement.purchase.purchaseOrder.dao;

import java.util.List;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.model.PurchaseOrderUomMapper;

public interface PurchaseOrderUomMapperDao {
	
	public Integer savePurchaseOrderUom(PurchaseOrderUomMapper poUomMapper);
	
	public Integer updatePurchaseOrderUomMapper(PurchaseOrderMasterDto purchaseOrderDto);
	
	public List<Object[]> retrievePuchaseOrderUomDetails(Integer purchaseOrderId);

}
