package com.param.procurement.purchase.service.purchaseOrder;

import org.springframework.util.MultiValueMap;

import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;


public interface IPurchaseOrderService {
	
	public PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);
	
	public Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto);
	
	public Object savePurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) throws CustomException;
	
	public Object updatePurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto);
	
	public Object getPucrhaseOrder(Integer id, Integer unitId, Integer orgId);

	Object approvePurchaseOrder(PurchaseOrderStatusDto purchaseOrderStatusDto);

	Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto);

	Object getLastPurchaseOrder(Integer itemid, Integer vendorid,Integer poType);

	Object forcefulyClosed(PurchaseOrderMasterDto purchaseOrderDto);
	
	/*public Response saveMultipleLocPurchaseOrder(PurchaseOrderMasterDto PurchaseOrderDto) throws Exception;
	
	public Response updateCancelReason(PurchaseOrderMasterDto PurchaseOrderDto);

	public Response updateApproveStatus(PurchaseOrderStatusDto purchaseOrderStatusDto);
	
	public Response updatePurchaseOrder(PurchaseOrderMasterDto PurchaseOrderDto);
	
	public Response updateApprovalStatus(PurchaseOrderStatusDto purchaseOrderStatusDto);
	
	public PurchaseOrderMasterDetailsDto retrievePuchaseOrderDetails(Integer id);
	
	public Response updatePurchaseOrderDetailsApproval(List<PurchaseOrderDetailDto> purchaseOrderDetailsDtoList);
*/
}
