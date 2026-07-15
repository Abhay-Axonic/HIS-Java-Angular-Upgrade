package com.param.pharmacy.master.global.service.unitOfMeasurement;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementDetailsResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementRequest;

public interface IUnitOfMeasurementService {


	UnitOfMeasurementDetailsResponse getById(Integer id, Integer orgId);
	
	List<UnitOfMeasurementListResponse> getUomByItemId(Integer id, Integer orgId);

	CommonResponse create(List<UnitOfMeasurementRequest> unitOfMeasurementRequests, Integer userId, Integer orgId);

	CommonResponse update(UnitOfMeasurementRequest unitOfMeasurementRequest, Integer itemId);

	 Response getAll(Integer orgId, Integer offset, Integer noOfRecordsPerPage, String itemName);

	List<UnitOfMeasurementListResponse> getByitemId(Integer itemId);

	CommonResponse update(UnitOfMeasurementRequest uomRequest);
	CommonResponse update1(UnitOfMeasurementRequest uomRequest, Item item);

}
