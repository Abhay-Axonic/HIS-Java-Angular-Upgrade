package com.param.inventory.store.dao.materialReturnAcceptance;

import java.util.List;

import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;
import com.param.entity.model.inventory.StoreStock;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;

public interface IMaterialReturnAcceptanceDao {

	List<MaterialResponse> searchMaterialReturnAcceptanceList(MaterialReurnRequest request, Integer screenId);

	Return getReturn(Integer id);

	List<ReturnDetail> getReturnDetail(Integer id);

	Return updateReturn(Return returnData);

	Reject save(Reject reject);

	StoreStock saveStoreStock(StoreStock storeStockNew);

	Integer updateIntransitQty(Integer id, Integer screenId);

	void updateIntransitByBatchIdQty(Integer j, Integer totalRejectedQty, Integer i);
	
}
