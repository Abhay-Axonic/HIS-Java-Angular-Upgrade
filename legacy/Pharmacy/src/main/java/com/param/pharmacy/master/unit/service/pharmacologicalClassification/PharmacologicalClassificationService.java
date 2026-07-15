package com.param.pharmacy.master.unit.service.pharmacologicalClassification;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.pharmacy.master.unit.dao.pharmacologicalClassification.IPharmacologicalClassificationDao;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationDetailsResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("PharmacologicalClassificationService")
public class PharmacologicalClassificationService implements IPharmacologicalClassificationService {

	@Autowired
	IPharmacologicalClassificationDao PharmacologicalClassificationDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<PharmacologicalClassificationListResponse> getAll(Integer orgId) {
		List<PharmacologicalClassificationListResponse> list = new ArrayList<PharmacologicalClassificationListResponse>();

		for (PharmacologicalClassification pharmacologicalClassification : PharmacologicalClassificationDao.getAll(orgId)) {
			PharmacologicalClassificationListResponse pharmaClassiRes = new PharmacologicalClassificationListResponse();
			pharmaClassiRes.setId((Integer) pharmacologicalClassification.getId());
			pharmaClassiRes.setCode((String) pharmacologicalClassification.getCode());
			pharmaClassiRes.setClassification((String) pharmacologicalClassification.getClassification());
			pharmaClassiRes.setStatus((Boolean) pharmacologicalClassification.getIsActive());
			pharmaClassiRes.setAntibioticTest(pharmacologicalClassification.getAntibioticTest());
			list.add(pharmaClassiRes);
		}

		return list;
	}

	@Override
	@Transactional
	public PharmacologicalClassificationDetailsResponse getById(Integer id) {
		PharmacologicalClassificationDetailsResponse response = new PharmacologicalClassificationDetailsResponse();

		PharmacologicalClassification pharmaclassification = PharmacologicalClassificationDao.getById(id);

		if (pharmaclassification != null) {
			response.setId((Integer) pharmaclassification.getId());
			response.setCode((String) pharmaclassification.getCode());
			response.setClassification((String) pharmaclassification.getClassification());
			response.setStatus((Boolean) pharmaclassification.getIsActive());
			response.setAntibioticTest(pharmaclassification.getAntibioticTest());
		} else {

		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(PharmacologicalClassificationRequest pharmacologicalClassificationRequest) {

		PharmacologicalClassification pharmaClassification = new PharmacologicalClassification();
	//	sessionService.setEntityDefaults(pharmaClassification);	
		pharmaClassification.setCreateDefaults(pharmacologicalClassificationRequest.getOrganizationId()!=null ? pharmacologicalClassificationRequest.getOrganizationId() : sessionService.getOrganizationId(),pharmacologicalClassificationRequest.getUnitId() !=null ? pharmacologicalClassificationRequest.getUnitId() :sessionService.getUnitId() ,pharmacologicalClassificationRequest.getUserId()!=null ? pharmacologicalClassificationRequest.getUserId() : sessionService.getUserId());

		pharmaClassification.setCode(pharmacologicalClassificationRequest.getCode());
		pharmaClassification.setClassification(pharmacologicalClassificationRequest.getClassification());
		pharmaClassification.setIsActive(pharmacologicalClassificationRequest.getStatus());
		pharmaClassification.setAntibioticTest(pharmacologicalClassificationRequest.getAntibioticTest());

		CommonResponse response = new CommonResponse();
		response.setId(PharmacologicalClassificationDao.create(pharmaClassification).longValue());
		response.setMessage("Pharmacological Classification Inserted Successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(PharmacologicalClassificationRequest pharmacologicalClassificationRequest) {

		PharmacologicalClassification pharmacologicalClassification = PharmacologicalClassificationDao
				.getById(pharmacologicalClassificationRequest.getId());

		if (pharmacologicalClassificationRequest.getCode() != null
				&& pharmacologicalClassificationRequest.getCode() != "") {
			pharmacologicalClassification.setCode(pharmacologicalClassificationRequest.getCode());
		}

		if (pharmacologicalClassificationRequest.getClassification() != null
				&& pharmacologicalClassificationRequest.getClassification() != "") {
			pharmacologicalClassification.setClassification(pharmacologicalClassificationRequest.getClassification());
		}

		if (pharmacologicalClassificationRequest.getStatus() != null) {
			pharmacologicalClassification.setIsActive(pharmacologicalClassificationRequest.getStatus());
		}
		
		pharmacologicalClassification.setAntibioticTest(pharmacologicalClassificationRequest.getAntibioticTest());

		pharmacologicalClassification.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacologicalClassification.getUnitId()));
		pharmacologicalClassification.setUpdatedByUser(pharmacologicalClassificationRequest.getUserId()!=null ? pharmacologicalClassificationRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(PharmacologicalClassificationDao.update(pharmacologicalClassification).longValue());
		response.setMessage("Pharmacological Classification Updated Successfully");

		return response;
	}

}
