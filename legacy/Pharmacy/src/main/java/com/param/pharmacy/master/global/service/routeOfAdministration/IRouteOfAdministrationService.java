package com.param.pharmacy.master.global.service.routeOfAdministration;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationDetailsResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationListResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationRequest;

public interface IRouteOfAdministrationService {

	List<RouteOfAdministrationListResponse> getAll(Integer orgId);

	RouteOfAdministrationDetailsResponse getById(Integer id, Integer orgId);

	CommonResponse create(RouteOfAdministrationRequest routeOfAdministrationRequest);

	CommonResponse update(RouteOfAdministrationRequest routeOfAdministrationRequest) throws CustomeException;

}
