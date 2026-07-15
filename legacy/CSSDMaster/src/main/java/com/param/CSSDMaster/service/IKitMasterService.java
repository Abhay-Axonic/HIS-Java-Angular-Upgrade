package com.param.CSSDMaster.service;

import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitMasterService {

	Response updateMaster(KitMasterDto masterDto);
	
	Response updateKitStatus(KitMasterDto masterDto);
	
	Response createMaster(KitMasterDto masterDto);

	Response getAllMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getAllSurgeonList(Integer organizationId,Integer unitId);
	
	Response getActiveMasterList(Integer organizationId,Integer unitId,Integer storeId);

	Response getKitWithEquipments(Integer organizationId,Integer unitId,Integer storeId,Integer kitId);

	Response searchKitByKitName(Integer organizationId,Integer unitId,String name);
	
	Response searchInstrumentByKitName(Integer organizationId,Integer unitId,Integer storeId,String name);
	
	Response searchInstrumentAsKit(Integer organizationId,Integer unitId,String name);
	
	Response searchInstrumentByName(Integer organizationId,Integer unitId,Integer storeId,String name);
	
	Response getAllInstrumentList(Integer organizationId,Integer unitId);
	
	Response searchInstrumentByMultipleFields(String code,String name,Integer instrumentNo,Integer organizationId,Integer unitId,Integer storeId);

	Response getKitListcount(KitMasterDto kitMasterDto);

	Response getEquipmentList(KitMasterDto kitMasterDto);
	
}
