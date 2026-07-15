package com.param.pharmacy.master.global.service.formulationType;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeDetailsResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeRequest;

public interface IFormulationTypeService {

	List<FormulationTypeListResponse> getAll(Integer orgId);

	FormulationTypeDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(FormulationTypeRequest formulationTypeRequest);

	CommonResponse update(FormulationTypeRequest formulationTypeRequest) throws CustomeException;
	
//	List<FormulationTypeResponseList> getDropdownList();

}
