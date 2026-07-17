package com.param.pharmacy.master.global.service.storageUnit;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.StorageUnit;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.storageUnit.IStorageUnitDao;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitListResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StorageUnitService")
public class StorageUnitService implements IStorageUnitService {

	@Autowired
	IStorageUnitDao StorageUnitDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<StorageUnitListResponse> getAll(Integer orgId) {
		List<StorageUnitListResponse> list = new ArrayList<StorageUnitListResponse>();

		for (StorageUnit storageUnit : StorageUnitDao.getAll(orgId)) {
			StorageUnitListResponse storageUnitRes = new StorageUnitListResponse();
			storageUnitRes.setId((Integer) storageUnit.getId());
			storageUnitRes.setCode((String) storageUnit.getCode());
			storageUnitRes.setUnitName((String) storageUnit.getUnitName());
			storageUnitRes.setStatus((Boolean) storageUnit.getIsActive());
			list.add(storageUnitRes);
		}
		return list;
	}

	@Override
	@Transactional
	public StorageUnitDetailsResponse getById(Integer id, Integer orgId) {

		StorageUnitDetailsResponse response = new StorageUnitDetailsResponse();

		StorageUnit storageUnit = StorageUnitDao.getById(id);

		if (storageUnit != null) {
			response.setId((Integer) storageUnit.getId());
			response.setCode((String) storageUnit.getCode());
			response.setUnitName((String) storageUnit.getUnitName());
			response.setStatus((Boolean) storageUnit.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(StorageUnitRequest storageUnitRequest) {

		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setCode(storageUnitRequest.getCode());
		storageUnit.setUnitName(storageUnitRequest.getUnitName());
		storageUnit.setIsActive(storageUnitRequest.getStatus());

		CommonResponse response = new CommonResponse();
		//sessionService.setEntityDefaults(storageUnit);
		storageUnit.setCreateDefaults(storageUnitRequest.getOrganizationId()!=null ? storageUnitRequest.getOrganizationId() : sessionService.getOrganizationId(),storageUnitRequest.getUnitId() !=null ? storageUnitRequest.getUnitId() :sessionService.getUnitId() ,storageUnitRequest.getUserId()!=null ? storageUnitRequest.getUserId() : sessionService.getUserId());
		response.setId(StorageUnitDao.create(storageUnit).longValue());
		response.setMessage("Storage Unit Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(StorageUnitRequest storageUnitRequest) throws CustomeException {

		StorageUnit storageUnit = StorageUnitDao.getById(storageUnitRequest.getId());

		if (storageUnitRequest.getCode() != null && storageUnitRequest.getCode() != "") {
			storageUnit.setCode(storageUnitRequest.getCode());
		}

		if (storageUnitRequest.getUnitName() != null && storageUnitRequest.getUnitName() != "") {
			storageUnit.setUnitName(storageUnitRequest.getUnitName());
		}

		if (storageUnitRequest.getStatus() != null) {
			storageUnit.setIsActive(storageUnitRequest.getStatus());
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Storage Unit Updated Successfully");
		Long countFromItem = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!storageUnit.getIsActive()) {
			countFromItem = StorageUnitDao.getStorageUnitCountFromItem(storageUnitRequest.getId());
		}
		if (countFromItem <= 0) {
			UnableToUpdateStatus = false;
			storageUnit.setUpdatedDate(TimeZoneComponent.getDateByzone(storageUnit.getUnitId()));
			storageUnit.setUpdatedByUser(storageUnitRequest.getUserId()!=null ? storageUnitRequest.getUserId() : sessionService.getUserId());
			if (storageUnitRequest.getStatus() != null) {
				storageUnit.setIsActive(storageUnitRequest.getStatus());
			}
		} else {
			UnableToUpdateStatus = true;
			if (storageUnitRequest.getStatus() != null) {
				storageUnit.setIsActive(true);
			}
			response.setMessage("Storage Unit is assigned to Items");
		}

		response.setId(StorageUnitDao.update(storageUnit).longValue());

		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					storageUnit.getIsActive());
			throw customeException;
		}
		return response;
	}

}
