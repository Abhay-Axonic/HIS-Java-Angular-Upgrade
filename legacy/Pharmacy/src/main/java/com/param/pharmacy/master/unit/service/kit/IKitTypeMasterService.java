package com.param.pharmacy.master.unit.service.kit;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.GetItemResponse;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;

public interface IKitTypeMasterService {
	
	CommonResponse saveKitMasterType(KitTypeMasterRequest request);
	
	CommonResponse update(KitTypeMasterRequest request);
	
	List<KitTypeMasterResponse> getAllKitType();
	
	Object getById(Integer id);
	
	/* List<KitTypeMasterResponse> getKitTypeById(); */
	
	/* KitTypeMasterResponse getById(Integer id); */
	

}
