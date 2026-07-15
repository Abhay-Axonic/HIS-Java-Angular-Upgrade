package com.param.inventory.store.dao.returnableGatepass;

import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;

public interface IReturnableBonusBatchExpiryDao {

	void updateBonusBatchExpiry(Integer id);

	void saveBonusBatchExpiry(returnableBonusBatchExpiry grnBatchExpiry1);

	void updateReturnQty(returnableBonusBatchExpiry grnBatchExpiry1);

	void updateReturnedQty(com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry bonusBatches);

}
