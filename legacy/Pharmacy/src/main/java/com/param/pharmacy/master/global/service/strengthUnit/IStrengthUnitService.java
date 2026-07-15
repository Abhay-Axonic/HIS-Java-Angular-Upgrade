package com.param.pharmacy.master.global.service.strengthUnit;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitRequest;

public interface IStrengthUnitService {

	List<StrengthUnitListResponse> getAll(Integer orgId);

	StrengthUnitDetailsResponse getById(Integer id);

	CommonResponse create(StrengthUnitRequest strengthUnitRequest);

	CommonResponse update(StrengthUnitRequest strengthUnitRequest) throws CustomeException;

}
