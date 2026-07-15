package com.param.inventory.nursing.service.nonBillableConsumption;

import com.param.global.common.Response;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;

import in.param.exception.ApplicationException;

public interface IConsumptionService {

	Object saveConsumption(ConsumptionRequest request);

	Object getConsumptionList(ConsumptionRequest request);

	Object getConsumptionDetail(Integer id, Integer orgId, Integer unitId);

	Object updateConsumption(ConsumptionRequest request);

	Object submitConsumption(ConsumptionRequest request);

	Object cancelConsumption(ConsumptionRequest request);

	Object getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Response getConsumptionDetailsByTemplateTransactionId(ConsumptionRequest request)throws ApplicationException;

}
