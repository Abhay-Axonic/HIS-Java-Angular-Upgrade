package com.param.inventory.nursing.service.consumableAcceptance;

import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;

public interface IConsumableAcceptanceService {

	Object getConsumableAcceptanceList(ConsumptionRequest request);

	Object getConsumableAcceptanceDetail(Integer id);

	Object approveConsumableAcceptance(ConsumptionRequest request);

	Object approveConsumableAcceptanceDetails(ConsumptionRequest request);

	Object cancelConsumableAcceptance(ConsumptionRequest request);

	Object getDropdowns(Integer unitId, Integer orgId, Integer userId);

}
