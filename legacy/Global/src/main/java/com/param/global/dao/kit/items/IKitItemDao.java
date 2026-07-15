package com.param.global.dao.kit.items;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.param.entity.model.master.Item;
import com.param.global.dto.kit.Items.KitItemsDetailsListResponse;

@Repository
public interface IKitItemDao {
	
	List<Item> productCategoryCount(List<Integer> idList);
	
	List<KitItemsDetailsListResponse> kitItemsDetails(List<Integer> idList, Integer storeId);
	
	List<KitItemsDetailsListResponse> kitItemsDetailsFixedAsset(List<Integer> idList, Integer storeId);
	
	Long getLeaseQunatity(Integer storeId,Integer itemId, Integer unitId);
	
}
