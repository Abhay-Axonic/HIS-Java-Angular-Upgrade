package com.param.pharmacy.master.global.service.routeOfAdministration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.routeOfAdministration.IRouteOfAdministrationDao;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationDetailsResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationListResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationRequest;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.service.global.SessionService;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

@Service("RouteOfAdministrationService")
public class RouteOfAdministrationService implements IRouteOfAdministrationService {

	@Autowired
	IRouteOfAdministrationDao RouteOfAdministrationDao;

	@Autowired
	IEmrService EmrService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	@Transactional
	public List<RouteOfAdministrationListResponse> getAll(Integer orgId) {
		// orgId param intentionally ignored -- BR-pending (batch 35 Step 0/1):
		// this previously trusted a client-supplied/path orgId directly, a
		// confirmed live cross-tenant IDOR. Always use the JWT-verified org.
		List<RouteOfAdministrationListResponse> list = new ArrayList<RouteOfAdministrationListResponse>();

		for (RouteOfAdministration routeOfAdministration : RouteOfAdministrationDao.getAll(verifiedIdentityService.getVerifiedOrganizationId())) {
			RouteOfAdministrationListResponse routOfAdminResponse = new RouteOfAdministrationListResponse();
			routOfAdminResponse.setId((Integer) routeOfAdministration.getId());
			routOfAdminResponse.setCode((String) routeOfAdministration.getCode());
			routOfAdminResponse.setDescription((String) routeOfAdministration.getDescription());
			routOfAdminResponse.setStatus((Boolean) routeOfAdministration.getIsActive());
			list.add(routOfAdminResponse);
		}
		return list;
	}

	@Override
	@Transactional
	public RouteOfAdministrationDetailsResponse getById(Integer id,Integer orgId) {
		// orgId param intentionally ignored, same basis as getAll() above.
		RouteOfAdministrationDetailsResponse response = new RouteOfAdministrationDetailsResponse();

		RouteOfAdministration routeOfAdministration = RouteOfAdministrationDao.getById(id, verifiedIdentityService.getVerifiedOrganizationId());

		if (routeOfAdministration != null) {
			response.setId((Integer) routeOfAdministration.getId());
			response.setCode((String) routeOfAdministration.getCode());
			response.setDescription((String) routeOfAdministration.getDescription());
			response.setStatus((Boolean) routeOfAdministration.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(RouteOfAdministrationRequest routeOfAdministrationRequest) {

		RouteOfAdministration routeOfAdministration = new RouteOfAdministration(null);
		routeOfAdministration.setCode(routeOfAdministrationRequest.getCode());
		routeOfAdministration.setDescription(routeOfAdministrationRequest.getDescription());
		routeOfAdministration.setIsActive(routeOfAdministrationRequest.getStatus());

		CommonResponse response = new CommonResponse();
		// organizationId/unitId always taken from the JWT-verified identity now --
		// BR-pending (batch 35 Step 0/1): the prior ternary trusted a
		// client-supplied organizationId/unitId directly whenever the request
		// body included one, a confirmed live cross-tenant write IDOR. userId is
		// left on sessionService, same precedent as BR-127: it's independently
		// JWT-bound via JwtTokenFilter's UserComponent lookup, a separate,
		// already-verified check.
		routeOfAdministration.setCreateDefaults(verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId(), routeOfAdministrationRequest.getUserId()!=null ? routeOfAdministrationRequest.getUserId() : sessionService.getUserId());
		response.setId(RouteOfAdministrationDao.create(routeOfAdministration).longValue());
		response.setMessage("Route Of Administration Added Successfully");
		routeOfAdministration.setId(response.getId().intValue());
		
		/*RouteOfAdministrationRabbitDto administrationRabbitDto = new RouteOfAdministrationRabbitDto(routeOfAdministration.getId(),routeOfAdministration.getCode(),
				routeOfAdministration.getDescription(),routeOfAdministration.getAddedBy().getUserId(),routeOfAdministration.getAddedDate(),
				routeOfAdministration.getUpdatedBy().getUserId(),routeOfAdministration.getUpdatedDate(),routeOfAdministration.getIsDeleted(),
				routeOfAdministration.getIsActive(),routeOfAdministration.getOrganization().getOrganizationId(),routeOfAdministration.getUnit().getUnitId());
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(administrationRabbitDto);
		request.setKey(MasterKeyConstants.RouteOfAdministrationMasterHISKey.toString());
		request.setPayload(payload);*/
		//EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(RouteOfAdministrationRequest routeOfAdministrationRequest) throws CustomeException {

		// BR-pending (batch 35 Step 0/1): getById() previously had zero
		// organization scoping -- any authenticated user could update/deactivate
		// any organization's record purely by id. Now scoped to the JWT-verified
		// org; a record belonging to a different org resolves to null here
		// exactly like a genuinely nonexistent id would.
		RouteOfAdministration routeOfAdministration = RouteOfAdministrationDao
				.getById(routeOfAdministrationRequest.getId(), verifiedIdentityService.getVerifiedOrganizationId());

		if (routeOfAdministration == null) {
			throw new CustomeException(
					routeOfAdministrationRequest.getId() != null ? routeOfAdministrationRequest.getId().longValue() : null,
					"Route Of Administration not found", false);
		}

		if (routeOfAdministrationRequest.getCode() != null && routeOfAdministrationRequest.getCode() != "") {
			routeOfAdministration.setCode(routeOfAdministrationRequest.getCode());
		}

		if (routeOfAdministrationRequest.getDescription() != null
				&& routeOfAdministrationRequest.getDescription() != "") {
			routeOfAdministration.setDescription(routeOfAdministrationRequest.getDescription());
		}

		if (routeOfAdministrationRequest.getStatus() != null) {
			routeOfAdministration.setIsActive(routeOfAdministrationRequest.getStatus());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Route Of Administration Updated Successfully");
		Long countFromItem = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (!routeOfAdministration.getIsActive()) {
			countFromItem = RouteOfAdministrationDao
					.getRouteOfAdministrationCountFromFormulationROA(routeOfAdministrationRequest.getId());
		}
		if (countFromItem <= 0) {
			UnableToUpdateStatus = false;
			if (routeOfAdministrationRequest.getStatus() != null) {
				routeOfAdministration.setUpdatedDate(TimeZoneComponent.getDateByzone(routeOfAdministration.getUnitId()));
				routeOfAdministration.setUpdatedByUser(routeOfAdministrationRequest.getUserId()!=null ? routeOfAdministrationRequest.getUserId() : sessionService.getUserId());
				routeOfAdministration.setIsActive(routeOfAdministrationRequest.getStatus());
				response.setMessage("Route Of Administration updated successfully");
			}
		} else {
			UnableToUpdateStatus = true;
			if (routeOfAdministrationRequest.getStatus() != null) {
				routeOfAdministration.setIsActive(true);
			}
			response.setMessage("Route Of Administration is assigned to Item");
		}

		response.setId(RouteOfAdministrationDao.update(routeOfAdministration).longValue());
		/*RouteOfAdministrationRabbitDto administrationRabbitDto = new RouteOfAdministrationRabbitDto(routeOfAdministration.getId(),routeOfAdministration.getCode(),
				routeOfAdministration.getDescription(),routeOfAdministration.getAddedBy().getUserId(),routeOfAdministration.getAddedDate(),
				routeOfAdministration.getUpdatedBy().getUserId(),routeOfAdministration.getUpdatedDate(),routeOfAdministration.getIsDeleted(),
				routeOfAdministration.getIsActive(),routeOfAdministration.getOrganization().getOrganizationId(),routeOfAdministration.getUnit().getUnitId());
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(administrationRabbitDto);
		request.setKey(MasterKeyConstants.RouteOfAdministrationMasterHISKey.toString());
		request.setPayload(payload);*/
		//EmrService.publish(request);

		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					routeOfAdministration.getIsActive());
			throw customeException;
		}
		return response;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public List<FormulationTypeAndROAList> getDropdownList() {
	 * List<FormulationTypeAndROAList> response = new
	 * ArrayList<FormulationTypeAndROAList>();
	 * 
	 * for (RouteOfAdministration Route :
	 * FormulationTypeAndROADao.getDropdownList()) { response.add(new
	 * FormulationTypeAndROAList(Route.getId(),
	 * Route.getCode(),Route.getDescription())); } return response; }
	 */
}
