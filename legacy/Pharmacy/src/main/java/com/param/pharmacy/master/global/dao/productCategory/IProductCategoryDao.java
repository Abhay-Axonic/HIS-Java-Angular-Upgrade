package com.param.pharmacy.master.global.dao.productCategory;

import java.util.List;

import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.ProductCategory;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;

public interface IProductCategoryDao {

	List<ProductCategoryListResponse> getAll(Integer orgId);

	ProductCategory getById(Integer id, Integer orgId);
	
	Integer create(ProductCategory productCategory);

	Integer update(ProductCategory productCategory);

	AssetCategory getAssetCategory(Integer id);

	Long getProductCategoryCountFromItem(Integer i);

	Long getProductCategoryCountFromGeneric(Integer id);

	Long getProductCategoryCountFromPoductRestriction(Integer id);

	Long getProductCategoryCountFromAssetcategory(Integer id);
	
	Integer updateProductCategoryByService(Integer serviceId,Integer productCategoryId,Integer unitId);


}