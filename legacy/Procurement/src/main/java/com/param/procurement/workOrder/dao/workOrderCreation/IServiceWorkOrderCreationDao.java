package com.param.procurement.workOrder.dao.workOrderCreation;

import java.util.List;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
@SuppressWarnings("rawtypes")
public interface IServiceWorkOrderCreationDao {

	Response getPendingDataForWorkOrderCreation(PendingServicesForWorkOrderCreationDto requestDto)
			throws ApplicationException;

	Response getDetails(PendingServicesForWorkOrderCreationDto requestDto);

	List<PendingServicesForWorkOrderCreationDto> forWorkOrderStatus(PendingServicesForWorkOrderCreationDto requestDto);

}
