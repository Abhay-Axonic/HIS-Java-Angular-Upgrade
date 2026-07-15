package com.param.procurement.purchase.purchaseOrder.dao;

import java.util.List;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.model.BonusUomMapper;

public interface BonusUomMapperDao {
	
	public Integer saveBonusUom(BonusUomMapper bonusUomMapper);
	
	public Integer updateBonusUomMapper(PurchaseOrderMasterDto purchaseOrderDto);
	
	public List<Object[]> retrieveBonusUomDetails(Integer purchaseOrderDetailsId);

}
