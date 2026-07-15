package com.param.pharmacy.master.global.service.storageUnit;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitListResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitRequest;

public interface IStorageUnitService {

	List<StorageUnitListResponse> getAll(Integer orgId);

	StorageUnitDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(StorageUnitRequest storageUnitRequest);

	CommonResponse update(StorageUnitRequest storageUnitRequest) throws CustomeException;

}
