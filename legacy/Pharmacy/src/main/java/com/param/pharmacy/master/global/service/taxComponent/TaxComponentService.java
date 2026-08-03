package com.param.pharmacy.master.global.service.taxComponent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.TaxComponent;
import com.param.global.service.VerifiedIdentityService;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.taxComponent.ITaxComponentDao;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentDetailsResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("TaxComponentService")
public class TaxComponentService implements ITaxComponentService {

	@Autowired
	ITaxComponentDao TaxComponentDao;
	

	@Autowired
	private SessionService sessionService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	@Transactional
	public List<TaxComponentListResponse> getAll(Integer orgId) {
		List<TaxComponentListResponse> list = new ArrayList<TaxComponentListResponse>();

		for (TaxComponent taxComponent : TaxComponentDao.getAll(orgId)) {
			TaxComponentListResponse taxComponentRes = new TaxComponentListResponse();
			taxComponentRes.setId((Integer) taxComponent.getId());
			taxComponentRes.setCode((String) taxComponent.getCode());
			taxComponentRes.setComponent((String) taxComponent.getComponent());
			taxComponentRes.setStatus((Boolean) taxComponent.getIsActive());
			list.add(taxComponentRes);
		}
		return list;
	}

	@Override
	@Transactional
	public TaxComponentDetailsResponse getById(Integer id, Integer orgId) {
		TaxComponentDetailsResponse response = new TaxComponentDetailsResponse();

		TaxComponent taxComponent = TaxComponentDao.getById(id, orgId);

		if (taxComponent != null) {
			response.setId(taxComponent.getId());
			response.setCode(taxComponent.getCode());
			response.setComponent(taxComponent.getComponent());
			response.setStatus(taxComponent.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(TaxComponentRequest taxComponentRequest) {

		// BR-158: cross-tenant IDOR fix -- org/unit must come from the JWT-verified
		// identity, never from the request body (the old code accepted a client-supplied
		// organizationId with session as mere fallback, which live-confirmed let an
		// org-1 session create a row in org 999).
		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();

		TaxComponent taxComponent = new TaxComponent();

		taxComponent.setCreateDefaults(verifiedOrgId, verifiedUnitId, taxComponentRequest.getUserId()!=null ? taxComponentRequest.getUserId() : sessionService.getUserId());
 
		taxComponent.setCode(taxComponentRequest.getCode());
		taxComponent.setComponent(taxComponentRequest.getComponent());
		taxComponent.setIsActive(taxComponentRequest.getStatus());

		CommonResponse response = new CommonResponse();
		response.setId(TaxComponentDao.create(taxComponent).longValue());
		response.setMessage("Tax Component Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(TaxComponentRequest taxComponentRequest) throws CustomeException {

		// BR-158: cross-tenant IDOR fix -- load the row scoped to the JWT-verified org
		// (the old code passed the client-supplied organizationId to getById, so a forged
		// org could target another tenant's row).
		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();

		TaxComponent taxComponent = TaxComponentDao.getById(taxComponentRequest.getId(), verifiedOrgId);

		if (taxComponent == null) {
			throw new CustomeException(null, "Tax Component Not Found", null);
		}

		if (taxComponentRequest.getCode() != null && taxComponentRequest.getCode() != "") {
			taxComponent.setCode(taxComponentRequest.getCode());
		}

		if (taxComponentRequest.getComponent() != null && taxComponentRequest.getComponent() != "") {
			taxComponent.setComponent(taxComponentRequest.getComponent());
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Tax Component Updated Successfully");
		Long count = Long.valueOf(0);
		Boolean UnableToUpdateStatus = false;
		if (taxComponentRequest.getStatus() != null && !taxComponentRequest.getStatus()) {
			count = TaxComponentDao.getTaxComponentFromTax(taxComponentRequest.getId(), verifiedOrgId);
		}
		if (count <= 0) {
			UnableToUpdateStatus = false;
			taxComponent.setUpdatedDate(TimeZoneComponent.getDateByzone(taxComponent.getUnitId()));
			taxComponent.setUpdatedByUser(taxComponentRequest.getUserId()!=null ? taxComponentRequest.getUserId() : sessionService.getUserId());

			if (taxComponentRequest.getStatus() != null) {
				taxComponent.setIsActive(taxComponentRequest.getStatus());
			}
		} else {
			UnableToUpdateStatus = true;
			if (taxComponentRequest.getStatus() != null) {
				taxComponent.setIsActive(true);
			}
			response.setMessage("Tax Component is assigned to Tax");
		}

		response.setId(TaxComponentDao.update(taxComponent).longValue());
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					taxComponent.getIsActive());
			throw customeException;
		}
		return response;
	}

	@Override
	@Transactional
	public List<com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto> getTaxComponentList() {
		List<com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto> response = new ArrayList<com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto>();

		for (TaxComponent taxComponent : TaxComponentDao.getTaxComponentList()) {
			if (!taxComponent.getIsDeleted()) {
				response.add(new com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto(
						taxComponent.getId(), taxComponent.getComponent()));
			}
		}
		return response;
	}

}
