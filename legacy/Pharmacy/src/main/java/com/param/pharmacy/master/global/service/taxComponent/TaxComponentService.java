package com.param.pharmacy.master.global.service.taxComponent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.TaxComponent;
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

		TaxComponent taxComponent = TaxComponentDao.getById(id);

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

		TaxComponent taxComponent = new TaxComponent();
	//	sessionService.setEntityDefaults(taxComponent);

		taxComponent.setCreateDefaults(taxComponentRequest.getOrganizationId()!=null ? taxComponentRequest.getOrganizationId() : sessionService.getOrganizationId(),taxComponentRequest.getUnitId() !=null ? taxComponentRequest.getUnitId() :sessionService.getUnitId() ,taxComponentRequest.getUserId()!=null ? taxComponentRequest.getUserId() : sessionService.getUserId());
 
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

		TaxComponent taxComponent = TaxComponentDao.getById(taxComponentRequest.getId());

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
			count = TaxComponentDao.getTaxComponentFromTax(taxComponentRequest.getId(),taxComponentRequest.getOrganizationId());
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
