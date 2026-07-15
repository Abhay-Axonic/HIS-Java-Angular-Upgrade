package com.param.pharmacy.master.unit.service.mainStore;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreDetailsResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreListResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequest;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequestNew;

public interface IMainStoreService {

	List<MainStoreListResponse> getAll(Integer unitId, Integer orgId);

	MainStoreDetailsResponse getById(Integer id);

	CommonResponse create(MainStoreRequest mainStoreRequest);

	CommonResponse update(MainStoreRequest mainStoreRequest);

	CommonResponse updateStatus(Integer storeId,Boolean status , Integer orgId , Integer unitId ,Integer userId);

	CommonResponse updateNew(MainStoreRequestNew mainStoreRequest);

}
