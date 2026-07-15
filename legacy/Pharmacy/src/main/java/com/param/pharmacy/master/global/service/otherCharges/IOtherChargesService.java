package com.param.pharmacy.master.global.service.otherCharges;

import java.util.List;

import com.param.common.dto.CommonResponse;
//import com.param.entity.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesDetailsResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesListResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesRequest;

public interface IOtherChargesService {

	List<OtherChargesListResponse> getAll(Integer orgId);

	OtherChargesDetailsResponse getById(Integer id);

	CommonResponse create(OtherChargesRequest otherChargesRequest);

	CommonResponse update(OtherChargesRequest otherChargesRequest);

}
