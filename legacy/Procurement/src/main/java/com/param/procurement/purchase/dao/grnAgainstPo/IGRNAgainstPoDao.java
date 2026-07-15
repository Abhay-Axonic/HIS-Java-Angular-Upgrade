package com.param.procurement.purchase.dao.grnAgainstPo;

import java.util.List;

import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;

public interface IGRNAgainstPoDao {

	List<PurchaseOrder> searchPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto, Integer screenId,
			List<Integer> statusId);

	List<GoodReceiptNote> getGoodReceiptNote(Integer id);

	Double getGoodReceiptNoteAmount(Integer id);

}
