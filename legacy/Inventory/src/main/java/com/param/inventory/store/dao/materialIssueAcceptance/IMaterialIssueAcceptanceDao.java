package com.param.inventory.store.dao.materialIssueAcceptance;

import java.util.List;

import com.param.entity.model.inventory.MaterialIssue;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;

public interface IMaterialIssueAcceptanceDao {

//	List<MaterialIssue> searchMaterialIssueAcceptanceList(MaterialPickerRequest request, Integer screenId);

	MaterialIssue updateStoreIndent(MaterialIssue materialIssue);

	Reject saveReject(Reject reject);

	StoreStock save(StoreStock storeStock);

	StockTransaction saveStockTransaction(StockTransaction stockTransaction);
	
	
	List<MaterialIssueResponse> searchMaterialIssueAcceptanceList(MaterialPickerRequest request, Integer screenId);
	
	List<MaterialIssue> searchAllMaterialIssueAcceptanceList(MaterialPickerRequest request, Integer screenId);

	MaterialIssueBatchMapper save(MaterialIssueBatchMapper batchMapper);

	StoreStock saveStoreStock(StoreStock storeStock);

	MaterialIssue updateMaterialIssueStoreTransaction(Integer integer, Integer screenId);

	void updateMaterialIssueIndendStoreTransaction(Integer id,Integer screenId, Integer indentQty, Integer batchId);

}
