package com.param.pharmacy.master.global.service.generic;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.generic.GenericDetailsResponse;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.pharmacy.master.global.dto.generic.GenericRequest;
import com.param.pharmacy.master.global.dto.generic.GenericRequestItem;

public interface IGenericService {

	List<GenericListResponse> getAll(Integer orgId);
	
	List<GenericListResponse> getAllIsCombinationGeneric(Boolean isCombination);

	GenericDetailsResponse getById(Integer id,Integer orgId);
	
	List<GenericRequestItem> getCombinationDetailsById(Integer id);

	CommonResponse create(GenericRequest genericRequest);
	
	CommonResponse update(GenericRequest genericRequest) throws CustomeException;

}
