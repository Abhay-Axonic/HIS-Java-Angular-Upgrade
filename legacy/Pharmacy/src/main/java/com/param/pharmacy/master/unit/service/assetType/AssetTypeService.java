package com.param.pharmacy.master.unit.service.assetType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.unit.dao.assetType.IAssetTypeDao;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("AssetTypeService")
public class AssetTypeService implements IAssetTypeService {

	@Autowired
	IAssetTypeDao AssetTypeDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<AssetTypeListResponse> getAll() {
		
		 /*
		   List<AssetTypeListResponse> list = new ArrayList<AssetTypeListResponse>();
	
		
		 * for (AssetType assetType : AssetTypeDao.getAll()) { AssetTypeListResponse
		 * assetTypeRes = new AssetTypeListResponse(); assetTypeRes.setId((Integer)
		 * assetType.getId()); assetTypeRes.setCode((String) assetType.getCode());
		 * assetTypeRes.setType((String) assetType.getType());
		 * assetTypeRes.setStatus((Boolean) assetType.getIsActive());
		 * list.add(assetTypeRes); }
		 * 
		 * return list;
		 */
		
		return AssetTypeDao.getAllAssetTypesBetterPerformance();
	}

	@Override
	@Transactional
	public AssetTypeDetailsResponse getById(Integer id) {
		AssetTypeDetailsResponse response = new AssetTypeDetailsResponse();

		AssetType assetType = AssetTypeDao.getById(id);

		if (assetType != null) {
			response.setId((Integer) assetType.getId());
			response.setCode((String) assetType.getCode());
			response.setType((String) assetType.getType());
			response.setStatus((Boolean) assetType.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(AssetTypeRequest assetTypeRequest) {

		AssetType assetType = new AssetType(assetTypeRequest.getCode(),assetTypeRequest.getType(),assetTypeRequest.getStatus());
	//	sessionService.setEntityDefaults(assetType);
		assetType.setCreateDefaults(assetTypeRequest.getOrganizationId()!=null ? assetTypeRequest.getOrganizationId() : sessionService.getOrganizationId(),assetTypeRequest.getUnitId() !=null ? assetTypeRequest.getUnitId() :sessionService.getUnitId() ,assetTypeRequest.getUserId()!=null ? assetTypeRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(AssetTypeDao.create(assetType).longValue());
		response.setMessage("Asset Type Inserted Successfully");
		assetType.setId(response.getId().intValue());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(assetType);
		request.setKey(MasterKeyConstants.AssetCategoryMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(AssetTypeRequest assetTypeRequest) {

		AssetType assetType = AssetTypeDao.getById(assetTypeRequest.getId());
		if (assetTypeRequest.getCode() != null && assetTypeRequest.getCode() != "") {
			assetType.setCode(assetTypeRequest.getCode());
		}
		if (assetTypeRequest.getType() != null && assetTypeRequest.getType() != "") {
			assetType.setType(assetTypeRequest.getType());
		}
		if (assetTypeRequest.getStatus() != null) {
			assetType.setIsActive(assetTypeRequest.getStatus());
		}
		assetType.setUpdatedDate(TimeZoneComponent.getDateByzone(assetType.getUnitId()));
		assetType.setUpdatedByUser(assetTypeRequest.getUserId()!=null ? assetTypeRequest.getUserId() : sessionService.getUserId());
		CommonResponse response = new CommonResponse();
		response.setId(AssetTypeDao.update(assetType).longValue());
		response.setMessage("Asset Type Updated Successfully");
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(assetType);
		request.setKey(MasterKeyConstants.AssetCategoryMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		return response;
	}

}
