package com.param.procurement.quotation.service.consignmentPOCreation;

import java.util.List;

import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOHistoryDto;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;

public interface IConsignmentStockPOCreationService {
	
	Object getConsignmentStockPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto);
	
	List<ConsignmentStockReturnDetailSaveRequest> getAllCRGrnDetails(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	
	//Object savePurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) throws CustomException;
	Object savePurchaseOrder(ConsignmentStockPOMasterDto purchaseOrderDto) throws CustomException;
	
	Object updatePurchaseOrder(ConsignmentStockPOMasterDto purchaseOrderDto);
	
	Object getPucrhaseOrder(Integer id, Integer unitId, Integer orgId);

	List<ConsignmentStockPOHistoryDto> getConsignmentPOHistoryByGRNDetailId(Integer grnDetailId , Integer unitId);

}
