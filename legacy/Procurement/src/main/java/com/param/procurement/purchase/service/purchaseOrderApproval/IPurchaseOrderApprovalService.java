package com.param.procurement.purchase.service.purchaseOrderApproval;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;

public interface IPurchaseOrderApprovalService {

	PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);
	
	Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto);

	Object getPucrhaseOrder(Integer id, Integer unitId, Integer orgId);

	Object approvePurchaseOrder(List<PurchaseOrderStatusDto> purchaseOrderStatusDtoList);

	Object approvePurchaseOrderDetail(PurchaseOrderMasterDto purchaseOrdDto);

	Object secondLevelApprovePurchaseOrderDetail(PurchaseOrderMasterDto purchaseOrdDto);

	Object getPurchaseOrderFinalList(PurchaseOrderMasterDto purchaseOrdDto);
	
	

	

}
