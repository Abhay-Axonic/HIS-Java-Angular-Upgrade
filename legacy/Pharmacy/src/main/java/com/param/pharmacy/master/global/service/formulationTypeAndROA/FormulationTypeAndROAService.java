package com.param.pharmacy.master.global.service.formulationTypeAndROA;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FormulationAndRoa;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.formulationTypeAndROA.IFormulationTypeAndROADao;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationRoaRequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAList;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROARequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponse;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponseByID;
import com.param.service.global.SessionService;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings("rawtypes")
public class FormulationTypeAndROAService implements IFormulationTypeAndROAService {

	@Autowired
	IFormulationTypeAndROADao FormulationTypeAndROADao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	@Transactional
	public List<FormulationTypeAndROAResponse> getAll(Integer orgId) {
		// orgId param intentionally ignored -- BR-pending (batch 35 Step 0/1):
		// this previously trusted a client-supplied/path orgId directly, a
		// confirmed live cross-tenant IDOR, same class as routeAdministration.
		List<FormulationTypeAndROAResponse> responseList = new ArrayList<FormulationTypeAndROAResponse>();

		for (FormulationAndRoa response : FormulationTypeAndROADao.getAll(verifiedIdentityService.getVerifiedOrganizationId())) {
			FormulationTypeAndROAResponse formulationTypeAndROAResponse = new FormulationTypeAndROAResponse(
					response.getId(), response.getFormulationType().getType(),
					response.getRouteOfAdministration().getDescription(), response.getIsActive());
			responseList.add(formulationTypeAndROAResponse);
		}
		return responseList;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public CommonResponse create(FormulationTypeAndROARequest
	 * formulationTypeAndROARequest) {
	 * 
	 * FormulationType formulationType = new FormulationType();
	 * formulationType.setId(formulationTypeAndROARequest.getFormulationtypeid());
	 * 
	 * RouteOfAdministration administration = new RouteOfAdministration();
	 * administration.setId(formulationTypeAndROARequest.getRouteOfAdministrationId(
	 * ));
	 * 
	 * FormulationAndRoa formulationAndRoa = new FormulationAndRoa();
	 * formulationAndRoa.setFormulationType(formulationType);
	 * formulationAndRoa.setRouteOfAdministration(administration);
	 * formulationAndRoa.setIsActive(formulationTypeAndROARequest.getStatus());
	 * 
	 * CommonResponse response = new CommonResponse();
	 * response.setId(FormulationTypeAndROADao.create(formulationAndRoa).longValue()
	 * ); response.setMessage("FormulationAndRoa Inserted Successfully");
	 * 
	 * return response; }
	 */

	
	@Override
	@Transactional
	public CommonResponse saveFormulationTypeAndROA(FormulationRoaRequest request) {

		FormulationAndRoa formulationTypeAndROA = null;
//		formulationTypeAndROA.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		for (Integer routeOfAdministrationId : request.getRouteOfAdministrationId()) {
			formulationTypeAndROA = new FormulationAndRoa(new FormulationType(request.getFormulationtypeid()),
					new RouteOfAdministration(routeOfAdministrationId));
			// formulationTypeAndROA.setIsDeleted(false);
			formulationTypeAndROA.setIsActive(request.getStatus());
			formulationTypeAndROA.setOrganizationId(request.getOrganizationId());
			formulationTypeAndROA.setUnitId(request.getUnitId());
			formulationTypeAndROA.setUpdatedByUser(request.getUserId());
			// organizationId/unitId always taken from the JWT-verified identity now
			// -- BR-pending (batch 35 Step 0/1): the prior ternary trusted a
			// client-supplied organizationId/unitId directly from the request body,
			// a confirmed live cross-tenant write IDOR (the real frontend already
			// sends these fields in the body). userId left on sessionService, same
			// precedent as BR-127.
			formulationTypeAndROA.setCreateDefaults(verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId(), request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			FormulationTypeAndROADao.saveFormulationTypeAndROA(formulationTypeAndROA);
		}

		CommonResponse response = new CommonResponse();
		//sessionService.setEntityDefaults(formulationTypeAndROA);
		formulationTypeAndROA.setCreateDefaults(verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId(), request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		response.setId(formulationTypeAndROA.getId().longValue());
		response.setMessage("Formulation And Roa Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(FormulationTypeAndROARequest formulationTypeAndROARequest) throws CustomeException {

		// BR-pending (batch 35 Step 0/1): getById() previously had zero
		// organization scoping -- any authenticated user could update/deactivate
		// any organization's record purely by id. Now scoped to the JWT-verified
		// org; a record belonging to a different org resolves to null here
		// exactly like a genuinely nonexistent id would.
		FormulationAndRoa formulationAndRoa = FormulationTypeAndROADao.getById(formulationTypeAndROARequest.getId(), verifiedIdentityService.getVerifiedOrganizationId());

		if (formulationAndRoa == null) {
			throw new CustomeException(
					formulationTypeAndROARequest.getId() != null ? formulationTypeAndROARequest.getId().longValue() : null,
					"Formulation And Roa not found", false);
		}

		if (formulationTypeAndROARequest.getFormulationtypeid() != null
				&& formulationTypeAndROARequest.getFormulationtypeid() > 0) {
			FormulationType formulationType = FormulationTypeAndROADao
					.getFormulationType(formulationTypeAndROARequest.getFormulationtypeid());
			formulationAndRoa.setFormulationType(formulationType);
		}

		if (formulationTypeAndROARequest.getRouteOfAdministrationId() != null
				&& formulationTypeAndROARequest.getRouteOfAdministrationId() > 0) {
			RouteOfAdministration routeOfAdministration = FormulationTypeAndROADao
					.getRouteOfAdministration(formulationTypeAndROARequest.getRouteOfAdministrationId());// getFormulationtypeid());
			formulationAndRoa.setRouteOfAdministration(routeOfAdministration);
		}

		if (formulationTypeAndROARequest.getStatus() != null) {
			formulationAndRoa.setIsActive(formulationTypeAndROARequest.getStatus());
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Formulation And Roa Updated Successfully");

		Long countFromFormulationType = Long.valueOf(0);
		Long countFromFormulaROA = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (formulationAndRoa.getIsActive()) {
			countFromFormulationType = FormulationTypeAndROADao
					.getFormulationROACountFromFormulationType(formulationTypeAndROARequest.getId());
			countFromFormulaROA = FormulationTypeAndROADao
					.getFormulationROACountFromROA(formulationTypeAndROARequest.getId());
		}
		if (countFromFormulationType <= 0 && countFromFormulaROA <= 0) {
			UnableToUpdateStatus = false;
			if (formulationTypeAndROARequest.getStatus() != null) {
				formulationAndRoa.setUpdatedDate(TimeZoneComponent.getDateByzone(formulationAndRoa.getUnitId()));
				formulationAndRoa.setUpdatedByUser(formulationTypeAndROARequest.getUserId()!=null ? formulationTypeAndROARequest.getUserId() : sessionService.getUserId());
				formulationAndRoa.setIsActive(formulationTypeAndROARequest.getStatus());
			}
		} else if (countFromFormulationType > 0) {
			UnableToUpdateStatus = true;
			if (formulationTypeAndROARequest.getStatus() != null) {
				formulationAndRoa.setIsActive(false);
			}
			response.setMessage("Formulation Type is not Active");
		} else if (countFromFormulaROA > 0) {
			if (formulationTypeAndROARequest.getStatus() != null) {
				formulationAndRoa.setIsActive(false);
			}
			response.setMessage("Rout Of Admission is not Active");
		}
		response.setId(FormulationTypeAndROADao.update(formulationAndRoa).longValue());
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					formulationAndRoa.getIsActive());
			throw customeException;
		}
		return response;
	}

	@Override
	@Transactional
	public List<FormulationTypeAndROAList> getDropdownList(Integer orgId) {
		// orgId param intentionally ignored, same basis as getAll() above.
		List<FormulationTypeAndROAList> response = new ArrayList<FormulationTypeAndROAList>();

		for (RouteOfAdministration Route : FormulationTypeAndROADao.getDropdownList(verifiedIdentityService.getVerifiedOrganizationId())) {
			response.add(new FormulationTypeAndROAList(Route.getId(), Route.getCode(), Route.getDescription()));
		}
		return response;
	}

	@Override
	@Transactional
	public FormulationTypeAndROAResponseByID getById(Integer id,Integer orgId) {
		// orgId param intentionally ignored, same basis as getAll() above.
		FormulationTypeAndROAResponseByID responseList = new FormulationTypeAndROAResponseByID();
		FormulationAndRoa andRoa = FormulationTypeAndROADao.getById(id, verifiedIdentityService.getVerifiedOrganizationId());
		if (andRoa != null) {
			responseList = new FormulationTypeAndROAResponseByID(andRoa.getId(), andRoa.getFormulationType().getType(),
					andRoa.getRouteOfAdministration().getDescription(), andRoa.getFormulationType().getId(),
					andRoa.getRouteOfAdministration().getId(), andRoa.getIsActive());
		} else {
		}
		return responseList;
	}

	@Override
	@Transactional
	public Long getCount(Integer orgId) {
		// orgId param intentionally ignored, same basis as getAll() above.
		return FormulationTypeAndROADao.getCount(verifiedIdentityService.getVerifiedOrganizationId());
	}

	@Override
	@Transactional
	public List<FormulationTypeAndROAResponse> getFormulationTypeAndROA(Integer offset, Integer noOfRecordsPerPage,Integer orgId) {
		// orgId param intentionally ignored, same basis as getAll() above.
		List<FormulationTypeAndROAResponse> responseList = new ArrayList<FormulationTypeAndROAResponse>();

		for (FormulationAndRoa response : FormulationTypeAndROADao.getFormulationTypeAndROA(offset,noOfRecordsPerPage,verifiedIdentityService.getVerifiedOrganizationId())) {
			FormulationTypeAndROAResponse formulationTypeAndROAResponse = new FormulationTypeAndROAResponse(
					response.getId(), response.getFormulationType().getType(),
					response.getRouteOfAdministration().getDescription(), response.getIsActive());
			responseList.add(formulationTypeAndROAResponse);
		}
		return responseList;
	}
}
