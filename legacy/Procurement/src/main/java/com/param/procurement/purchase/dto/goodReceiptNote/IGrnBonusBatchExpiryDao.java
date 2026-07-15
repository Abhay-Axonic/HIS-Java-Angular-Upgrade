package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.List;

import com.param.entity.model.procurement.GrnBonusBatchExpiry;

public interface IGrnBonusBatchExpiryDao {

	List<GrnBonusBatchExpiry> getGrnBonusBatchExpiryByGrnDetailId(Integer id);

	void saveBonusBatchExpiry(GrnBonusBatchExpiry grnBatchExpiry1);

	void updateBonusBatchExpiry(Integer grndetailId);

	void updateBonusBatchExpiryApproval(GrnBonusBatchExpiry grnBatchExpiry1);

	void updateReturnQuantity(GrnBonusBatchExpiry grnBatchExpiry1);

}
