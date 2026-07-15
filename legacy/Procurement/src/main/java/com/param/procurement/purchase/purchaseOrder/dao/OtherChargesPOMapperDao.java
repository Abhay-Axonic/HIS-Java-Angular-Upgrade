package com.param.procurement.purchase.purchaseOrder.dao;

import java.util.List;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.model.PurchaseOrderOtherChargesMapper;

public interface OtherChargesPOMapperDao {
	
	public Integer saveOtherCharges(PurchaseOrderOtherChargesMapper poOtherChrages);
	
	public Integer updatePoOtherCharges(PurchaseOrderMasterDto purchaseOrderDto);
	
	public List<Object[]> retrieveOtherChargesDetailsByPoId(Integer purchaseOrderId);

}
