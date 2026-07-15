package com.param.procurement.workOrder.service.workOrderCreation;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;

public interface IServiceWorkOrderCreationService {

	Response getPendingDataForWorkOrderCreation(PendingServicesForWorkOrderCreationDto requestDto)
			throws ApplicationException;

	Response getDetails(PendingServicesForWorkOrderCreationDto requestDto);

	Response saveWorkOrderForServices(WorkOrderCreationRequestDto workOrderCreationRequestDto)
			throws ApplicationException;

}
