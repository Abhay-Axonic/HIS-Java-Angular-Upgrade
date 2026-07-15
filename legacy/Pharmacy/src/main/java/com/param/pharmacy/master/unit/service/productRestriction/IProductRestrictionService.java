package com.param.pharmacy.master.unit.service.productRestriction;

import java.math.BigInteger;
import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionDetailsResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemResDetailsResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionPCListResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionRequest;

public interface IProductRestrictionService {

	List<ProductRestrictionPCListResponse> getAll(Integer storeId, Integer orgId);

	ProductRestrictionDetailsResponse getById(BigInteger id);
	
	CommonResponse create(ProductRestrictionRequest productRestrictionRequest);
	
	CommonResponse update(ProductRestrictionRequest productRestrictionRequest) throws CustomeException;
	
	
	List<ProductRestrictionItemRestrictionResponse> getAllItems();

	ProductRestrictionItemResDetailsResponse getItemRestrictionById(BigInteger id);

	CommonResponse createItemRestriction(
			ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest);

	CommonResponse updateItemRestriction(
			ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest);


	List<ProductRestrictionPCListResponse> getBystoreId(Integer storeId);

	CommonResponse updateProductRestrictionMapper(BigInteger id,Boolean status);

	List<ProductRestrictionItemRestrictionResponse> getItemRestrictionsBystoreId(Integer storeId);
	
}
