package com.param.pharmacy.master.global.service.taxComponent;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentDetailsResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentRequest;

public interface ITaxComponentService {

	List<TaxComponentListResponse> getAll(Integer orgId);

	TaxComponentDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(TaxComponentRequest taxComponentRequest);

	CommonResponse update(TaxComponentRequest taxComponentRequest) throws CustomeException;

	List<com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto> getTaxComponentList();

}
