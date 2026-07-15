package com.param.pharmacy.master.unit.service.productRestriction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemRestriction;
import com.param.entity.model.master.StoreProductCategoryMapper;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.productRestriction.IProductRestrictionDao;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionDetailsResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemResDetailsResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionPCListResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ProductRestrictionService")
public class ProductRestrictionService implements IProductRestrictionService {

	@Autowired
	IProductRestrictionDao ProductRestrictionDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<ProductRestrictionPCListResponse> getAll(Integer storeId,Integer orgId) {

		List<ProductRestrictionPCListResponse> list = new ArrayList<>();

		Integer id = ProductRestrictionDao.checkingStoreIdPresence(storeId);
		if (id <= 0) {
			list = ProductRestrictionDao.getAllWithoutStoreId();
		} else {
			for (ProductRestrictionPCListResponse storePc : ProductRestrictionDao.getAll(storeId,orgId)) {
				list.add(storePc);
			}
		}
		return list;
	}

	@Override
	@Transactional
	public List<ProductRestrictionPCListResponse> getBystoreId(Integer storeId) {
		/// List<ProductRestrictionPCListResponse> responseList = new
		/// ArrayList<ProductRestrictionPCListResponse>();

		// for (StoreProductCategoryMapper categoryMapper :
		// ProductRestrictionDao.getBystoreId())
		List<ProductRestrictionPCListResponse> categoryMapper = ProductRestrictionDao.getBystoreId(storeId);

		/*
		 * if (storepc != null) {
		 * 
		 * ProductRestrictionPCListResponse pcResponse = new
		 * ProductRestrictionPCListResponse(storepc.getId(),
		 * storepc.getStore().getStoreId(), storepc.getStore().getStoreName()
		 * ,storepc.getProductCategory().getId(),storepc.getProductCategory().
		 * getCategory(),storepc.getProductCategory().getAssetCategory().getId(),
		 * storepc.getProductCategory().getAssetCategory().getCategory()
		 * ,storepc.getProductCategory().getAssetCategory().getAssetType().getId(),
		 * storepc.getProductCategory().getAssetCategory().getAssetType().getId(),
		 * storepc.getIsActive()); }
		 * 
		 * 
		 * return responseList; }
		 */

		/*
		 * if (categoryMapper != null) {
		 * 
		 * ProductRestrictionPCListResponse proRestPCResponse= new
		 * ProductRestrictionPCListResponse(); proRestPCResponse.setId((BigInteger)
		 * categoryMapper.getId()); proRestPCResponse.setStoreId((Integer)
		 * categoryMapper.getStore().getStoreId());
		 * proRestPCResponse.setStoreName((String)
		 * categoryMapper.getStore().getStoreName());
		 * proRestPCResponse.setProductCategoryId((Integer)
		 * categoryMapper.getProductCategory().getId());
		 * proRestPCResponse.setProductcategory((String)
		 * categoryMapper.getProductCategory().getCategory());
		 * proRestPCResponse.setAssetCategoryId((Integer)
		 * categoryMapper.getProductCategory().getAssetCategory().getId());
		 * proRestPCResponse.setAssetCategory((String)
		 * categoryMapper.getProductCategory().getAssetCategory().getCategory());
		 * proRestPCResponse.setAssetTypeId((Integer)
		 * categoryMapper.getProductCategory().getAssetCategory().getAssetType().getId()
		 * ); proRestPCResponse.setAssetType((String)
		 * categoryMapper.getProductCategory().getAssetCategory().getAssetType().getType
		 * ()); proRestPCResponse.setStatus((Boolean) categoryMapper.getIsActive());
		 * responseList.add(proRestPCResponse); }else {
		 * 
		 * }
		 */
		return categoryMapper;
	}

	@Override
	@Transactional
	public List<ProductRestrictionItemRestrictionResponse> getItemRestrictionsBystoreId(Integer storeId) {

		List<ProductRestrictionItemRestrictionResponse> itemRestriction = ProductRestrictionDao
				.getItemRestrictionsBystoreId(storeId);
		return itemRestriction;
	}

	@Override
	@Transactional
	public ProductRestrictionDetailsResponse getById(BigInteger id) {
		ProductRestrictionDetailsResponse response = new ProductRestrictionDetailsResponse();

		StoreProductCategoryMapper categoryMapper = ProductRestrictionDao.getById(id);

		if (categoryMapper != null) {
			response.setId((BigInteger) categoryMapper.getId());
			response.setStoreId((Integer) categoryMapper.getStore().getStoreId());
			response.setStoreName((String) categoryMapper.getStore().getStoreName());
			response.setProductCategoryId((Integer) categoryMapper.getProductCategory().getId());
			response.setProductCategory((String) categoryMapper.getProductCategory().getCategory());
			response.setAssetCategoryId((Integer) categoryMapper.getProductCategory().getAssetCategory().getId());
			response.setAssetCategory((String) categoryMapper.getProductCategory().getAssetCategory().getCategory());
			response.setAssetTypeId(
					(Integer) categoryMapper.getProductCategory().getAssetCategory().getAssetType().getId());
			response.setAssetType(
					(String) categoryMapper.getProductCategory().getAssetCategory().getAssetType().getType());
			response.setStatus((Boolean) categoryMapper.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(ProductRestrictionRequest productRestrictionRequest) {

		ProductCategory productCategory = new ProductCategory();
		//sessionService.setEntityDefaults(productCategory);	
		productCategory.setCreateDefaults(productRestrictionRequest.getOrganizationId()!=null ? productRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionRequest.getUnitId() !=null ? productRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionRequest.getUserId()!=null ? productRestrictionRequest.getUserId() : sessionService.getUserId());
		
		productCategory.setId(productRestrictionRequest.getProductCategoryId());
		Store store = new Store();
	//	sessionService.setEntityDefaults(store);	
		store.setCreateDefaults(productRestrictionRequest.getOrganizationId()!=null ? productRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionRequest.getUnitId() !=null ? productRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionRequest.getUserId()!=null ? productRestrictionRequest.getUserId() : sessionService.getUserId());
		
		store.setStoreId(productRestrictionRequest.getStoreId());

		StoreProductCategoryMapper storeProductCategoryMapper = new StoreProductCategoryMapper();
		//sessionService.setEntityDefaults(storeProductCategoryMapper);
		storeProductCategoryMapper.setCreateDefaults(productRestrictionRequest.getOrganizationId()!=null ? productRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionRequest.getUnitId() !=null ? productRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionRequest.getUserId()!=null ? productRestrictionRequest.getUserId() : sessionService.getUserId());
		
		storeProductCategoryMapper.setIsActive(productRestrictionRequest.getStatus());
		storeProductCategoryMapper.setProductCategory(productCategory);
		storeProductCategoryMapper.setStore(store);

		CommonResponse response = new CommonResponse();
		response.setId((ProductRestrictionDao.create(storeProductCategoryMapper)).longValue());
		response.setMessage("Product Category Mapper Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(ProductRestrictionRequest productRestrictionRequest) throws CustomeException {

		StoreProductCategoryMapper storeProductCategoryMapper = ProductRestrictionDao
				.getById(productRestrictionRequest.getId());

		if (productRestrictionRequest.getStoreId() != null && productRestrictionRequest.getStoreId() > 0) {
			Store store = ProductRestrictionDao.getStore(productRestrictionRequest.getStoreId());
			storeProductCategoryMapper.setStore(store);
		}
		CommonResponse response = new CommonResponse();
		Boolean UnableToUpdateStatus = false;
		response.setMessage("Product Category Mapper Updated Successfully");
		if (productRestrictionRequest.getProductCategoryId() != null
				&& productRestrictionRequest.getProductCategoryId() > 0) {
			ProductCategory productCategory = ProductRestrictionDao
					.getProductCategory(productRestrictionRequest.getProductCategoryId());
			Long countFromProductCategory = new Long(0);

			if (!productRestrictionRequest.getStatus()) {
				UnableToUpdateStatus = false;
				storeProductCategoryMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(storeProductCategoryMapper.getUnitId()));
				storeProductCategoryMapper.setUpdatedByUser(productRestrictionRequest.getUserId()!=null ? productRestrictionRequest.getUserId() : sessionService.getUserId());
				if (productRestrictionRequest.getStatus() != null) {
					productCategory.setIsActive(productRestrictionRequest.getStatus());
				}
			} else {
				countFromProductCategory = ProductRestrictionDao
						.getProductRestrictionFromProductCategory(productRestrictionRequest.getProductCategoryId());
			}
			if (countFromProductCategory <= 0) {

			} else {
				UnableToUpdateStatus = true;
				if (productRestrictionRequest.getStatus() != null) {
					productCategory.setIsActive(false);
				}
				response.setMessage("Product Category is not Active");
			}
			storeProductCategoryMapper.setProductCategory(productCategory);
			response.setId((ProductRestrictionDao.update(storeProductCategoryMapper)).longValue());
			if (UnableToUpdateStatus) {
				CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
						productCategory.getIsActive());
				throw customeException;
			}
		}

		return response;
	}

	@Override
	@Transactional
	public List<ProductRestrictionItemRestrictionResponse> getAllItems() {

		List<ProductRestrictionItemRestrictionResponse> list = new ArrayList<ProductRestrictionItemRestrictionResponse>();

		for (StoreItemRestriction itemRestriction : ProductRestrictionDao.getAllItems()) {
			ProductRestrictionItemRestrictionResponse itemRestResponse = new ProductRestrictionItemRestrictionResponse();
			itemRestResponse.setId((BigInteger) itemRestriction.getId());
			itemRestResponse.setStoreId((Integer) itemRestriction.getStore().getStoreId());
			itemRestResponse.setStoreName((String) itemRestriction.getStore().getStoreName());
			itemRestResponse.setProductCategoryId(
					(Integer) itemRestriction.getItem().getGeneric().getProductCategory().getId());
			itemRestResponse.setProductCategory(
					(String) itemRestriction.getItem().getGeneric().getProductCategory().getCategory());
			itemRestResponse.setAssetCategoryId(
					(Integer) itemRestriction.getItem().getGeneric().getProductCategory().getAssetCategory().getId());
			itemRestResponse.setAssetCategory((String) itemRestriction.getItem().getGeneric().getProductCategory()
					.getAssetCategory().getCategory());
			itemRestResponse.setItemId((Integer) itemRestriction.getItem().getId());
			itemRestResponse.setItemCode((String) itemRestriction.getItem().getCode());
			itemRestResponse.setItemName((String) itemRestriction.getItem().getName());
			itemRestResponse.setStatus((Boolean) itemRestriction.getIsActive());
			list.add(itemRestResponse);
		}
		return list;
	}

	@Override
	@Transactional
	public ProductRestrictionItemResDetailsResponse getItemRestrictionById(BigInteger id) {

		ProductRestrictionItemResDetailsResponse response = new ProductRestrictionItemResDetailsResponse();

		StoreItemRestriction storeItemRestriction = ProductRestrictionDao.getItemRestrictionById(id);

		if (storeItemRestriction != null) {
			response.setId((BigInteger) storeItemRestriction.getId());
			response.setStoreId((Integer) storeItemRestriction.getStore().getStoreId());
			response.setStoreName((String) storeItemRestriction.getStore().getStoreName());
			response.setProductCategoryId(
					(Integer) storeItemRestriction.getItem().getGeneric().getProductCategory().getId());
			response.setProductCategory(
					(String) storeItemRestriction.getItem().getGeneric().getProductCategory().getCategory());
			response.setAssetCategoryId((Integer) storeItemRestriction.getItem().getGeneric().getProductCategory()
					.getAssetCategory().getId());
			response.setAssetCategory((String) storeItemRestriction.getItem().getGeneric().getProductCategory()
					.getAssetCategory().getCategory());
			response.setItemCode((String) storeItemRestriction.getStore().getStoreCode());
			response.setItemName((String) storeItemRestriction.getStore().getStoreName());
			response.setStatus((Boolean) storeItemRestriction.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse createItemRestriction(
			ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest) {

		Item item = new Item();
		//sessionService.setEntityDefaults(item);	

		item.setCreateDefaults(productRestrictionItemRestrictionRequest.getOrganizationId()!=null ? productRestrictionItemRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionItemRestrictionRequest.getUnitId() !=null ? productRestrictionItemRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionItemRestrictionRequest.getUserId()!=null ? productRestrictionItemRestrictionRequest.getUserId() : sessionService.getUserId());

		item.setId(productRestrictionItemRestrictionRequest.getItemId());
		item.setName(productRestrictionItemRestrictionRequest.getItemName());
		item.setCode(productRestrictionItemRestrictionRequest.getItemCode());

		Store store = new Store();
//		sessionService.setEntityDefaults(store);	
		store.setCreateDefaults(productRestrictionItemRestrictionRequest.getOrganizationId()!=null ? productRestrictionItemRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionItemRestrictionRequest.getUnitId() !=null ? productRestrictionItemRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionItemRestrictionRequest.getUserId()!=null ? productRestrictionItemRestrictionRequest.getUserId() : sessionService.getUserId());

		store.setStoreId(productRestrictionItemRestrictionRequest.getStoreId());

		StoreItemRestriction storeItemRestriction = new StoreItemRestriction();
		//sessionService.setEntityDefaults(storeItemRestriction);
		storeItemRestriction.setCreateDefaults(productRestrictionItemRestrictionRequest.getOrganizationId()!=null ? productRestrictionItemRestrictionRequest.getOrganizationId() : sessionService.getOrganizationId(),productRestrictionItemRestrictionRequest.getUnitId() !=null ? productRestrictionItemRestrictionRequest.getUnitId() :sessionService.getUnitId() ,productRestrictionItemRestrictionRequest.getUserId()!=null ? productRestrictionItemRestrictionRequest.getUserId() : sessionService.getUserId());

		storeItemRestriction.setIsActive(productRestrictionItemRestrictionRequest.getStatus());
		storeItemRestriction.setItem(item);
		storeItemRestriction.setStore(store);

		CommonResponse response = new CommonResponse();
		response.setId((ProductRestrictionDao.createItemRestriction(storeItemRestriction)).longValue());
		response.setMessage("Item Restriction Of Product Category Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse updateItemRestriction(
			ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest) {

		StoreItemRestriction storeItemRestriction = ProductRestrictionDao
				.getItemRestrictionById(productRestrictionItemRestrictionRequest.getId());

		if (productRestrictionItemRestrictionRequest.getStoreId() != null
				&& productRestrictionItemRestrictionRequest.getStoreId() > 0) {
			Store store = ProductRestrictionDao.getStore(productRestrictionItemRestrictionRequest.getStoreId());
			storeItemRestriction.setStore(store);
		}

		if (productRestrictionItemRestrictionRequest.getItemId() != null
				&& productRestrictionItemRestrictionRequest.getItemId() > 0) {
			Item item = ProductRestrictionDao.getItem(productRestrictionItemRestrictionRequest.getItemId());
			storeItemRestriction.setItem(item);
		}
		if (productRestrictionItemRestrictionRequest.getStatus() == null)
			storeItemRestriction.setIsActive(true);// setIsDeleted
		else
			storeItemRestriction.setIsActive(productRestrictionItemRestrictionRequest.getStatus());// true);

		storeItemRestriction.setUpdatedDate(TimeZoneComponent.getDateByzone(storeItemRestriction.getUnitId()));
		storeItemRestriction.setUpdatedByUser(productRestrictionItemRestrictionRequest.getUserId()!=null ? productRestrictionItemRestrictionRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId((ProductRestrictionDao.update(storeItemRestriction)).longValue());
		response.setMessage("Item Restriction Of Product Category Updated Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse updateProductRestrictionMapper(BigInteger id, Boolean status) {
		CommonResponse response = new CommonResponse();
		response.setId(ProductRestrictionDao.updateProductRestrictionMapper(id, status));
		response.setMessage("Product Category Mapper Updated Successfully");
		return response;
	}

}
