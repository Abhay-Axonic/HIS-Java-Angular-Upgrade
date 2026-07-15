package com.param.CSSDMaster.dao;

import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IKitMasterDao {

	Response updateKit(KitMasterDto masterDto);
	
	Response updateKitStatus(KitMasterDto masterDto);

	Response getAllKitList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getAllSurgeonList(Integer organizationId,Integer unitId);
	
	Response getActiveKitList(Integer organizationId,Integer unitId,Integer storeId);

	Response createKit(KitMasterDto masterDto);

	Response getKitWithEquipments(Integer organizationId,Integer unitId,Integer storeId,Integer kitId);

	Response searchKitByKitName(Integer organizationId,Integer unitId,String name);
	
	Response searchInstrumentByKitName(Integer organizationId,Integer unitId,Integer storeId,String name);
	
	Response searchInstrumentAsKit(Integer organizationId,Integer unitId,String name);

	Response searchInstrumentByName(Integer organizationId,Integer unitId,Integer storeId,String name);
	
	Response getAllInstrumentList(Integer organizationId,Integer unitId);
	
	Response searchInstrumentByMultipleFields(String code,String name,Integer instrumentNo,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getKitByNameAndCode(String name, String code, Integer orgId,Integer unitId,Integer storeId);

	Response getKitListcount(KitMasterDto kitMasterDto);

	Response getEquipmentList(KitMasterDto kitMasterDto);
}
