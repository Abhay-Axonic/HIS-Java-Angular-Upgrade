package com.param.pharmacy.master.unit.service.kit;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.param.pharmacy.master.unit.dto.kit.KitItemMasterResponse;
import com.param.pharmacy.master.unit.dto.kit.KitMasterDropdownResponse;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Kit;
import com.param.global.common.Response;
import com.param.global.service.items.CustomException;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;

import in.param.exception.ApplicationException;


public interface IKitMasterService {
	
	KitMasterDropdownResponse getDropdowns();
	
	Object saveKitMaster(KitTypeMasterRequest request);

	CommonResponse update(KitTypeMasterRequest kitTypeMasterRequest);
	
	List<KitItemMasterResponse> getAll(Integer orgId , Integer unitId);

	KitItemMasterResponse getByKitMasterId(Integer id);

	Object getKitMasterByCodeAndDecriptionSearch(String name);

	Object getMedicationKitByCodeAndDecriptionSearch(String name);

	Response getKitDetailsByIdAndType(Integer storeId, Integer kitId, Integer kitTypeId,
			Integer orgId, Integer unitId, Integer paymentEntitlementId, Integer admissionId, Integer encounterId, Integer patientTypeId)throws ApplicationException;

	Response searchItems(boolean isImplant, String search, Integer paymentEntitlementId, Integer admissionId, Integer encounterId, Integer patientTypeId)throws ApplicationException;

	Object updateStatus(KitTypeMasterRequest request);

	Response getKitDetails(Integer kitId, Integer storeId, Integer unitId);
	
	
}
