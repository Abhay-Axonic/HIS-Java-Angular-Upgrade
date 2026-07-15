package com.param.pharmacy.master.global.dao.consumableBarcodeConfig;

import java.util.List;

import com.param.entity.model.master.ConsumableBarcodeConfig;

public interface IConsumableBarcodeConfigDao {

	List<ConsumableBarcodeConfig> getAll(Integer unitId);

	Integer update(ConsumableBarcodeConfig consumableBarcodeConfig);

	ConsumableBarcodeConfig getConsumableBarcodeConfig(Integer id);

	ConsumableBarcodeConfig saveConsumableBarcodeConfig(ConsumableBarcodeConfig equipment);

}
