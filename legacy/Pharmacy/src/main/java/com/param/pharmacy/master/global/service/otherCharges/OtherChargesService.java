package com.param.pharmacy.master.global.service.otherCharges;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
//import com.param.entity.dto.CommonResponse;
import com.param.entity.model.master.OtherCharge;
import com.param.pharmacy.master.global.dao.otherCharges.IOtherChargesDao;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesDetailsResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesListResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("OtherChargesService")
public class OtherChargesService implements IOtherChargesService {

	@Autowired
	IOtherChargesDao OtherChargesDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<OtherChargesListResponse> getAll(Integer orgId) {
		List<OtherChargesListResponse> list = new ArrayList<OtherChargesListResponse>();

		for (OtherCharge otherCharger : OtherChargesDao.getAll(orgId)) {
			OtherChargesListResponse otherChargerRes = new OtherChargesListResponse();
			otherChargerRes.setId((Integer) otherCharger.getId());
			otherChargerRes.setCode((String) otherCharger.getCode());
			otherChargerRes.setDescription((String) otherCharger.getDescription());
			otherChargerRes.setStatus((Boolean) otherCharger.getIsActive());
			list.add(otherChargerRes);
		}
		return list;
	}

	@Override
	@Transactional
	public OtherChargesDetailsResponse getById(Integer id) {
		OtherChargesDetailsResponse response = new OtherChargesDetailsResponse();

		OtherCharge otherCharge = OtherChargesDao.getById(id);

		if (otherCharge != null) {
			response.setId((Integer) otherCharge.getId());
			response.setCode((String) otherCharge.getCode());
			response.setDescription((String) otherCharge.getDescription());
			response.setStatus((Boolean) otherCharge.getIsActive());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(OtherChargesRequest otherChargesRequest) {

		OtherCharge otherCharge = new OtherCharge();
		otherCharge.setCode(otherChargesRequest.getCode());
		otherCharge.setDescription(otherChargesRequest.getDescription());
		otherCharge.setIsActive(otherChargesRequest.getStatus());

		CommonResponse response = new CommonResponse();
		//sessionService.setEntityDefaults(otherCharge);
		otherCharge.setCreateDefaults(otherChargesRequest.getOrganizationId()!=null ? otherChargesRequest.getOrganizationId() : sessionService.getOrganizationId(),otherChargesRequest.getUnitId() !=null ? otherChargesRequest.getUnitId() :sessionService.getUnitId() ,otherChargesRequest.getUserId()!=null ? otherChargesRequest.getUserId() : sessionService.getUserId());
		response.setId(OtherChargesDao.create(otherCharge).longValue());
		response.setMessage("Other Charges Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(OtherChargesRequest otherChargesRequest) {

		OtherCharge otherCharge = OtherChargesDao.getById(otherChargesRequest.getId());

		if (otherChargesRequest.getCode() != null && otherChargesRequest.getCode() != "") {
			otherCharge.setCode(otherChargesRequest.getCode());
		}

		if (otherChargesRequest.getDescription() != null && otherChargesRequest.getDescription() != "") {
			otherCharge.setDescription(otherChargesRequest.getDescription());
		}

		if (otherChargesRequest.getStatus() != null) {
			otherCharge.setIsActive(otherChargesRequest.getStatus());
		}

		otherCharge.setUpdatedDate(TimeZoneComponent.getDateByzone(otherCharge.getUnitId()));
		otherCharge.setUpdatedByUser(otherChargesRequest.getUserId()!=null ? otherChargesRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(OtherChargesDao.update(otherCharge).longValue());
		response.setMessage("Other Charges Updated Successfully");

		return response;
	}

}
