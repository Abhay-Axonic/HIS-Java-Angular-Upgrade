package com.param.pharmacy.master.global.service.productCategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.StoreProductCategoryMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.Store;
import com.param.global.model.ServiceMaster;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.productCategory.IProductCategoryDao;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryRequest;
import com.param.pharmacy.master.unit.dao.productRestriction.IProductRestrictionDao;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ProductCategoryService")
public class ProductCategoryService implements IProductCategoryService {

	@Autowired
	IProductCategoryDao ProductCategoryDao;

	@Autowired
	IEmrService EmrService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IProductRestrictionDao ProductRestrictionDao;

	@Override
	@Transactional
	public List<ProductCategoryListResponse> getAll(Integer orgId) {
		return ProductCategoryDao.getAll(orgId);
	}

	@Override
	@Transactional
	public ProductCategoryDetailResponse getById(Integer id, Integer orgId) {
		ProductCategoryDetailResponse response = new ProductCategoryDetailResponse();

		ProductCategory productCategory = ProductCategoryDao.getById(id, orgId);

		if (productCategory != null) {
			response.setId((Integer) productCategory.getId());
			response.setAssetTypeId((Integer) productCategory.getAssetCategory().getAssetType().getId());
			response.setAssetType((String) productCategory.getAssetCategory().getAssetType().getType());
			response.setAssetCategoryId((Integer) productCategory.getAssetCategory().getId());
			response.setAssetCategory((String) productCategory.getAssetCategory().getCategory());
			response.setGeneralLedgerId(productCategory.getGeneralLedgerId());
			if (productCategory.getServiceMaster() != null
					&& productCategory.getServiceMaster().getServiceStandardName() != null) {
				response.setServiceName((String) productCategory.getServiceMaster().getServiceStandardName());
			}
			response.setCode((String) productCategory.getCode());
			response.setCategory((String) productCategory.getCategory());
			response.setStatus((Boolean) productCategory.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(ProductCategoryRequest productCategoryRequest) {

		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(productCategoryRequest.getAssetCategoryId());

		// Add new Employee object
		ProductCategory productCategory = new ProductCategory();
		productCategory.setCode(productCategoryRequest.getCode());
		productCategory.setCategory(productCategoryRequest.getCategory());
		productCategory.setIsActive(productCategoryRequest.getStatus());
		productCategory.setAssetCategory(assetCategory);
		/*
		 * Organization organization = new Organization();
		 * organization.setOrganizationId(sessionService.getOrganizationId());
		 * productCategory.setOrganization(organization);
		 */

		if (productCategoryRequest.getServiceId() != null) {
			ServiceMaster sm = new ServiceMaster();
			sm.setServiceMasterId(productCategoryRequest.getServiceId());
			productCategory.setServiceMaster(sm);
		}

		productCategory.setCreateDefaults(productCategoryRequest.getOrganizationId(),
				productCategoryRequest.getUnitId(), productCategoryRequest.getUserId());

		CommonResponse response = new CommonResponse();
		productCategory.setGeneralLedgerId(productCategoryRequest.getGeneralLedgerId());
		// sessionService.setEntityDefaults(productCategory);
		Integer proId = ProductCategoryDao.create(productCategory);
		response.setId(proId.longValue());
		response.setMessage("Product Category Inserted Successfully");

		List<Store> Stores = GlobalDao.getStoreListBP(productCategoryRequest.getUnitId());

		for (Store s : Stores) {
			StoreProductCategoryMapper storeProductCategoryMapper = new StoreProductCategoryMapper();
			// sessionService.setEntityDefaults(storeProductCategoryMapper);
			storeProductCategoryMapper.setCreateDefaults(productCategoryRequest.getOrganizationId(),
					productCategoryRequest.getUnitId(), productCategoryRequest.getUserId());

			storeProductCategoryMapper.setIsActive(true);
			storeProductCategoryMapper.setProductCategory(new ProductCategory(proId));
			storeProductCategoryMapper.setStore(new com.param.entity.model.master.Store(s.getId()));
			ProductRestrictionDao.create(storeProductCategoryMapper);
		}

		if (productCategoryRequest.getServiceId() != null && productCategoryRequest.getServiceId() > 0) {
			ProductCategoryDao.updateProductCategoryByService(productCategoryRequest.getServiceId(),
					response.getId().intValue(), productCategoryRequest.getUnitId());
		}

		productCategory.setId(response.getId().intValue());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(productCategory);
		request.setKey("ProductCategoryMasterHIS");
		request.setPayload(payload);
		// EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(ProductCategoryRequest productCategoryRequest) throws CustomeException {

		ProductCategory productCategory = ProductCategoryDao.getById(productCategoryRequest.getId(), null);

		if (productCategoryRequest.getAssetCategoryId() != null && productCategoryRequest.getAssetCategoryId() > 0) {
			AssetCategory assetCategory = ProductCategoryDao
					.getAssetCategory(productCategoryRequest.getAssetCategoryId());
			productCategory.setAssetCategory(assetCategory);
		}

		if (productCategoryRequest.getCode() != null && productCategoryRequest.getCode() != "") {
			productCategory.setCode(productCategoryRequest.getCode());
		}

		if (productCategoryRequest.getCategory() != null && productCategoryRequest.getCategory() != "") {
			productCategory.setCategory(productCategoryRequest.getCategory());
		}
		if (productCategoryRequest.getStatus() != null) {
			productCategory.setIsActive(productCategoryRequest.getStatus());
		}
		if (productCategoryRequest.getServiceId() != null) {
			ServiceMaster sm = new ServiceMaster();
			sm.setServiceMasterId(productCategoryRequest.getServiceId());
			productCategory.setServiceMaster(sm);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Product Category Updated Successfully");

		Long productCategoryCount = Long.valueOf(0);
		Long genericCount = Long.valueOf(0);
		Long productRestrictionCount = Long.valueOf(0);
		Long AssetcategoryCount = Long.valueOf(0);
		if (!productCategory.getIsActive()) {
			productCategoryCount = ProductCategoryDao.getProductCategoryCountFromItem(productCategoryRequest.getId());
			genericCount = ProductCategoryDao.getProductCategoryCountFromGeneric(productCategoryRequest.getId());
			productRestrictionCount = ProductCategoryDao
					.getProductCategoryCountFromPoductRestriction(productCategoryRequest.getId());
		} else {
			AssetcategoryCount = ProductCategoryDao
					.getProductCategoryCountFromAssetcategory(productCategoryRequest.getId());
		}
		Boolean UnableToUpdateStatus = false;
		if (productCategoryCount <= 0 && genericCount <= 0 && AssetcategoryCount <= 0) {
			if (productCategoryRequest.getStatus() != null) {
				productCategory.setIsActive(productCategoryRequest.getStatus());
				productCategory.setUpdatedDate(TimeZoneComponent.getDateByzone(productCategory.getUnitId()));
				productCategory.setUpdatedByUser(
						productCategoryRequest.getUserId() != null ? productCategoryRequest.getUserId()
								: sessionService.getUserId());
			}
			response.setMessage("Product Category Updated Successfully");
			UnableToUpdateStatus = false;
		} else if (genericCount > 0) {
			if (productCategoryRequest.getStatus() != null) {
				productCategory.setIsActive(true);
			}
			response.setMessage("Product Category is assigned to generic");
			UnableToUpdateStatus = true;
		} else if (productCategoryCount > 0) {
			if (productCategoryRequest.getStatus() != null) {
				productCategory.setIsActive(true);
			}
			response.setMessage("Product Category is assigned to Item");
			UnableToUpdateStatus = true;
		} else if (productRestrictionCount > 0) {
			if (productCategoryRequest.getStatus() != null) {
				productCategory.setIsActive(true);
			}
			response.setMessage("Product Category is assigned to product Restriction");
			UnableToUpdateStatus = true;
		} else if (AssetcategoryCount > 0) {
			if (productCategoryRequest.getStatus() != null) {
				productCategory.setIsActive(false);
			}
			response.setMessage("Asset Category is not Active");
			UnableToUpdateStatus = true;
		}
		productCategory.setGeneralLedgerId(productCategoryRequest.getGeneralLedgerId());
		response.setId(ProductCategoryDao.update(productCategory).longValue());

		if (productCategoryRequest.getServiceId() != null && productCategoryRequest.getServiceId() > 0) {
			ProductCategoryDao.updateProductCategoryByService(productCategoryRequest.getServiceId(),
					response.getId().intValue(), sessionService.getUnitId());
		}
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					productCategory.getIsActive());
			throw customeException;
		}
		return response;
	}

}