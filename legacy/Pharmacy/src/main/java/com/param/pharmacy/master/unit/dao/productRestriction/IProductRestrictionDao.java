package com.param.pharmacy.master.unit.dao.productRestriction;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemRestriction;
import com.param.entity.model.master.StoreProductCategoryMapper;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionPCListResponse;

public interface IProductRestrictionDao {

	List<ProductRestrictionPCListResponse> getAll(Integer storeId, Integer orgId);

	StoreProductCategoryMapper getById(BigInteger id);
	
	BigInteger create(StoreProductCategoryMapper storeProductCategoryMapper);
	
	BigInteger update(StoreProductCategoryMapper storeProductCategoryMapper);

	Store getStore(Integer storeId);

	ProductCategory getProductCategory(Integer productCategoryId);
	
	List<StoreItemRestriction> getAllItems();

	StoreItemRestriction getItemRestrictionById(BigInteger id);

	BigInteger createItemRestriction(StoreItemRestriction storeItemRestriction);
	
	BigInteger update(StoreItemRestriction storeItemRestriction);

	Item getItem(Integer itemId);

	List<StoreProductCategoryMapper> getBystoreId();

	List<ProductRestrictionPCListResponse> getBystoreId(Integer storeId);

	Long updateProductRestrictionMapper(BigInteger id,Boolean status);

	Integer checkingStoreIdPresence(Integer storeId);

	List<ProductRestrictionPCListResponse> getAllWithoutStoreId();

	List<ProductRestrictionItemRestrictionResponse> getItemRestrictionsBystoreId(Integer storeId);

	Long getProductRestrictionFromProductCategory(Integer productCategoryId);

}
