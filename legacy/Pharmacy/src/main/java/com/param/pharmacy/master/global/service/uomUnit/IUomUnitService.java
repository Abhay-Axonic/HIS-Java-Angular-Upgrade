package com.param.pharmacy.master.global.service.uomUnit;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitListResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitRequest;

public interface IUomUnitService {

	List<UomUnitListResponse> getAll(Integer orgId);

	UomUnitDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(UomUnitRequest uomUnitRequest);

	CommonResponse update(UomUnitRequest uomUnitRequest) throws CustomeException;

}
