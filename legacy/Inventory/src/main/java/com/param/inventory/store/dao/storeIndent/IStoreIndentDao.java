package com.param.inventory.store.dao.storeIndent;

import java.util.List;

import com.param.entity.model.inventory.MaterialPicker;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalRequest;

public interface IStoreIndentDao {
	
	List<StoreIndent> searchStoreIndent(StoreIndentRequest storeIndentRequest, Integer screenId);

	StoreIndent saveStoreIndent(StoreIndent storeIndent);
	
	StoreIndent getStoreIndent(Integer id);
	
	StoreIndent updateStoreIndent(StoreIndent storeIndent);

	List<StoreIndentDetail> getStoreIndentDetail(Integer storeIndentId);

	MaterialPicker saveMaterialPicker(MaterialPicker materialPicker);

	List<StoreIndent> searchStoreIndentPending(StoreIndentRequest request, Integer screenId);

	List<StoreIndent>  searchStoreIndentScreen(StoreIndentRequest request, Integer screenId);

	StoreIndentDetail getStoreIndentDetailById(Integer id);

	void updateStoreIndentD(Integer qty , Integer id);

	StoreIndentDetail saveStoreIndentDetail(StoreIndentDetail storeIndentSubDetail);

	Integer StoreIndentStatus(Integer id);

	Integer updateStoreIndentForForceClose(StoreIndentApprovalRequest request);
}