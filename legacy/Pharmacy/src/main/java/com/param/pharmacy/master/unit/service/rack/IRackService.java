package com.param.pharmacy.master.unit.service.rack;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.rack.RackDetailsResponse;
import com.param.pharmacy.master.unit.dto.rack.RackListResponse;
import com.param.pharmacy.master.unit.dto.rack.RackRequest;

public interface IRackService {

	List<RackListResponse> getAll(Integer unitId, Integer orgId);

	RackDetailsResponse getById(Integer id);

	CommonResponse create(RackRequest rackRequest);

	Object updateRack(RackRequest request);

}
