package com.param.pharmacy.master.global.service.productCategory;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryRequest;

public interface IProductCategoryService {

	List<ProductCategoryListResponse> getAll(Integer orgId);

	CommonResponse create(ProductCategoryRequest productCategoryRequest);

	CommonResponse update(ProductCategoryRequest productCategoryRequest) throws CustomeException;

	ProductCategoryDetailResponse getById(Integer id, Integer orgId);
	
}