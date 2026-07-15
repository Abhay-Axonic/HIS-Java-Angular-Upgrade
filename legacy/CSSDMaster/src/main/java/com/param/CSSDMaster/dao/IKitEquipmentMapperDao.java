package com.param.CSSDMaster.dao;

import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.global.common.Response;

public interface IKitEquipmentMapperDao {

	@SuppressWarnings("rawtypes")
	Response saveMapper(KitInstrumentMapperDto kitEquipmentMapperDto);

	@SuppressWarnings("rawtypes")
	Response updateMapper(KitInstrumentMapperDto kitEquipmentMapperDto);

}
