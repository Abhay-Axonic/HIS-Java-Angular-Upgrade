package com.param.pharmacy.master.global.service.consumableBarcodeConfig;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigRequest;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigResponse;

public interface IConsumableBarcodeConfigService {

	CommonResponse saveConsumableBarcodeConfig(ConsumableBarcodeConfigRequest consumableBarcodeConfigRequest);

	CommonResponse update(ConsumableBarcodeConfigRequest consumableBarcodeConfigRequest);

	List<ConsumableBarcodeConfigResponse> getAll(Integer unitId);

}
