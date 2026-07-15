package com.param.pharmacy.master.unit.dao.Kit;

import java.util.List;

import com.param.entity.model.master.Kit;
import com.param.entity.model.master.KitType;
import com.param.global.common.Response;
import com.param.pharmacy.master.unit.dto.kit.KitItemMasterResponse;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;

import in.param.exception.ApplicationException;

public interface IKitTypeMasterDao {
	
	KitType kitTypeMasterSave(KitType KitTypeMaster);
	
	KitType getKitType(Integer id);
	
	Integer update(KitType kitType);
	
	List<KitTypeMasterResponse> getAllKitType();
	
	KitType getById(Integer id);

	Kit saveKitMaster(Kit kit);

	Kit getKitMasterById(Integer id, Integer unitId);

	Kit updateKitDetailMaster(Kit kit);
	
	List<Kit> getAll(Integer orgId , Integer unitId);
	
	Kit getByKitMasterId(Integer id);

	List<Kit> getKitMasterByCodeAndDecriptionSearch(String name);

	List<Kit> getMedicationKitByCodeAndDecriptionSearch(String name);

	Response getKitDetailsByIdAndType(Integer storeId, Integer kitId, Integer kitTypeId,
			Integer orgId, Integer unitId)throws ApplicationException;

	Response getBatchListByItemId(Integer storeId, Integer itemId, Integer orgId, Integer unitId) throws ApplicationException;

	Response getUOMListByBatchId(Integer batchId, Integer orgId, Integer unitId)throws ApplicationException;

	Object updateStatus(KitTypeMasterRequest request);

	Response getKitDetails(Integer kitId, Integer storeId, Integer unitId);

}
