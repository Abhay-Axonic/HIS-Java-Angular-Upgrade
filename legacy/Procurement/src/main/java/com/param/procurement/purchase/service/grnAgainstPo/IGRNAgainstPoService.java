package com.param.procurement.purchase.service.grnAgainstPo;

import com.param.procurement.purchase.dto.goodReceiptNote.GRNDropdownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;

public interface IGRNAgainstPoService {

	GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto);

	Object getGoodReceiptNoteList(Integer id);

	Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto);

}
