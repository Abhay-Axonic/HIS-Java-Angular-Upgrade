package com.param.pharmacy.master.unit.service.assetCategory;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.assetCategory.IAssetCategoryDao;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AssetCategoryService")
public class AssetCategoryService implements IAssetCategoryService {

	@Autowired
	IAssetCategoryDao AssetCategoryDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<AssetCategoryListResponse> getAll(Integer orgId) {
		List<AssetCategoryListResponse> list = new ArrayList<AssetCategoryListResponse>();

		for (AssetCategory assetCategory : AssetCategoryDao.getAll(orgId)) {
			AssetCategoryListResponse assetCategoryRes = new AssetCategoryListResponse();
			assetCategoryRes.setId((Integer) assetCategory.getId());
			assetCategoryRes.setAssetType((String) assetCategory.getAssetType().getType());
			assetCategoryRes.setCode((String) assetCategory.getCode());
			assetCategoryRes.setCategory((String) assetCategory.getCategory());
			assetCategoryRes.setStatus((Boolean) assetCategory.getIsActive());
			list.add(assetCategoryRes);
		}

		return list;
	}

	@Override
	@Transactional
	public AssetCategoryDetailsResponse getById(Integer id) {
		AssetCategoryDetailsResponse response = new AssetCategoryDetailsResponse();

		AssetCategory assetCategory = AssetCategoryDao.getById(id);

		if (assetCategory != null) {
			response.setId((Integer) assetCategory.getId());
			response.setAssetTypeId((Integer) assetCategory.getAssetType().getId());
			response.setAssetType((String) assetCategory.getAssetType().getType());
			response.setCode((String) assetCategory.getCode());
			response.setCategory((String) assetCategory.getCategory());
			response.setStatus((Boolean) assetCategory.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(AssetCategoryRequest assetCategoryRequest) {

		AssetCategory assetCategory = new AssetCategory(assetCategoryRequest.getCode(),assetCategoryRequest.getCategory(),
									  assetCategoryRequest.getStatus(),new AssetType(assetCategoryRequest.getAssetTypeId()));
		//sessionService.setEntityDefaults(assetCategory);
		assetCategory.setCreateDefaults(assetCategoryRequest.getOrganizationId()!=null ? assetCategoryRequest.getOrganizationId() : sessionService.getOrganizationId(),assetCategoryRequest.getUnitId() !=null ? assetCategoryRequest.getUnitId() :sessionService.getUnitId() ,assetCategoryRequest.getUserId()!=null ? assetCategoryRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(AssetCategoryDao.create(assetCategory).longValue());
		response.setMessage("Asset Category Inserted Successfully");
		assetCategory.setId(response.getId().intValue());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(assetCategory);
		request.setKey(MasterKeyConstants.AssetCategoryMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(AssetCategoryRequest assetCategoryRequest) throws CustomeException {

		AssetCategory assetCategory = AssetCategoryDao.getById(assetCategoryRequest.getId());

		if (assetCategoryRequest.getAssetTypeId() != null && assetCategoryRequest.getAssetTypeId() > 0) {
			AssetType assetType = AssetCategoryDao.getAssetType(assetCategoryRequest.getAssetTypeId());
			assetCategory.setAssetType(assetType);
		}

		if (assetCategoryRequest.getCode() != null && assetCategoryRequest.getCode() != "") {
			assetCategory.setCode(assetCategoryRequest.getCode());
		}

		if (assetCategoryRequest.getCategory() != null && assetCategoryRequest.getCategory() != "") {
			assetCategory.setCategory(assetCategoryRequest.getCategory());
		}
		if (assetCategoryRequest.getStatus() != null) {
			assetCategory.setIsActive(assetCategoryRequest.getStatus());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Category Updated Successfully");
		Long count = Long.valueOf(0);
		if (!assetCategory.getIsActive()) {
			count = AssetCategoryDao.getProductCategory(assetCategoryRequest.getId());
		}
		if (count <= 0) {
			assetCategory.setUpdatedDate(TimeZoneComponent.getDateByzone(assetCategory.getUnitId()));
			assetCategory.setUpdatedByUser(assetCategoryRequest.getUserId()!=null ? assetCategoryRequest.getUserId() : sessionService.getUserId());
			response.setId(AssetCategoryDao.update(assetCategory).longValue());
		} else {
			if (assetCategoryRequest.getStatus() != null) {
				assetCategory.setIsActive(true);
				CustomeException customeException = new CustomeException(
						AssetCategoryDao.update(assetCategory).longValue(),
						"Asset category is assigned to product category", assetCategory.getIsActive());
				response.setId(AssetCategoryDao.update(assetCategory).longValue());
				throw customeException;
			}
		}
		return response;
	}

}
