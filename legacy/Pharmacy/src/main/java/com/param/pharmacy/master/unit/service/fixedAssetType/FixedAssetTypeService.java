package com.param.pharmacy.master.unit.service.fixedAssetType;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FixedAssetType;
import com.param.pharmacy.master.unit.dao.fixedAssetType.IFixedAssetTypeDao;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("FixedAssetTypeService")
public class FixedAssetTypeService implements IFixedAssetTypeService {

	@Autowired
	IFixedAssetTypeDao FixedAssetTypeDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<FixedAssetTypeListResponse> getAll() {
		List<FixedAssetTypeListResponse> list = new ArrayList<FixedAssetTypeListResponse>();

		for (FixedAssetType fixedAssetType : FixedAssetTypeDao.getAll()) {
			FixedAssetTypeListResponse fixedAssetTypeResponse = new FixedAssetTypeListResponse();
			fixedAssetTypeResponse.setId((Integer) fixedAssetType.getId());
			fixedAssetTypeResponse.setFixedAssetCode((String) fixedAssetType.getCode());
			fixedAssetTypeResponse.setFixedAssetType((String) fixedAssetType.getType());
			fixedAssetTypeResponse.setStatus((Boolean) fixedAssetType.getIsActive());
			list.add(fixedAssetTypeResponse);
		}

		return list;
	}

	@Override
	@Transactional
	public FixedAssetTypeDetailsResponse getById(Integer id) {
		FixedAssetTypeDetailsResponse response = new FixedAssetTypeDetailsResponse();

		FixedAssetType fixedAssetType = FixedAssetTypeDao.getById(id);

		if (fixedAssetType != null) {
			response.setId((Integer) fixedAssetType.getId());
			response.setFixedAssetCode((String) fixedAssetType.getCode());
			response.setFixedAssetType((String) fixedAssetType.getType());
			response.setStatus((Boolean) fixedAssetType.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(FixedAssetTypeRequest fixedAssetTypeRequest) {

		FixedAssetType fixedAssetType = new FixedAssetType();
	//	sessionService.setEntityDefaults(fixedAssetType);
		fixedAssetType.setCreateDefaults(fixedAssetTypeRequest.getOrganizationId()!=null ? fixedAssetTypeRequest.getOrganizationId() : sessionService.getOrganizationId(),fixedAssetTypeRequest.getUnitId() !=null ? fixedAssetTypeRequest.getUnitId() :sessionService.getUnitId() ,fixedAssetTypeRequest.getUserId()!=null ? fixedAssetTypeRequest.getUserId() : sessionService.getUserId());

		fixedAssetType.setCode(fixedAssetTypeRequest.getFixedAssetCode());
		fixedAssetType.setType(fixedAssetTypeRequest.getFixedAssetType());
		fixedAssetType.setIsActive(fixedAssetTypeRequest.getStatus());

		CommonResponse response = new CommonResponse();
		response.setId(FixedAssetTypeDao.create(fixedAssetType).longValue());
		response.setMessage("Fixed Asset Type Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(FixedAssetTypeRequest fixedAssetTypeRequest) {
		
		FixedAssetType fixedAssetType = FixedAssetTypeDao.getById(fixedAssetTypeRequest.getId());

		if (fixedAssetTypeRequest.getFixedAssetCode() != null && fixedAssetTypeRequest.getFixedAssetCode() != "") {
			fixedAssetType.setCode(fixedAssetTypeRequest.getFixedAssetCode());
		}

		if (fixedAssetTypeRequest.getFixedAssetType() != null && fixedAssetTypeRequest.getFixedAssetType() != ""){
			fixedAssetType.setType(fixedAssetTypeRequest.getFixedAssetType());
		}

		if (fixedAssetTypeRequest.getStatus() != null) {
			fixedAssetType.setIsActive(fixedAssetTypeRequest.getStatus());
		}

		fixedAssetType.setUpdatedDate(TimeZoneComponent.getDateByzone(fixedAssetType.getUnitId()));
		fixedAssetType.setUpdatedByUser(fixedAssetTypeRequest.getUserId()!=null ? fixedAssetTypeRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(FixedAssetTypeDao.update(fixedAssetType).longValue());
		response.setMessage("Fixed Asset Type Updated Successfully");

		return response;
	}

}
