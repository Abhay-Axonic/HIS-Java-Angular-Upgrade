package com.param.pharmacy.master.global.service.formulationTypeAndROA;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationRoaRequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAList;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROARequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponse;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponseByID;

public interface IFormulationTypeAndROAService {

	List<FormulationTypeAndROAResponse> getAll(Integer orgId);

//	CommonResponse create(FormulationTypeAndROARequest formulationTypeAndROARequest);

	CommonResponse update(FormulationTypeAndROARequest formulationTypeAndROARequest) throws CustomeException;

	List<FormulationTypeAndROAList> getDropdownList(Integer orgId);

	CommonResponse saveFormulationTypeAndROA(FormulationRoaRequest request);
	
	FormulationTypeAndROAResponseByID getById(Integer id, Integer orgId);

	Long getCount(Integer orgId);

	List<FormulationTypeAndROAResponse> getFormulationTypeAndROA(Integer offset, Integer noOfRecordsPerPage, Integer orgId);


}
