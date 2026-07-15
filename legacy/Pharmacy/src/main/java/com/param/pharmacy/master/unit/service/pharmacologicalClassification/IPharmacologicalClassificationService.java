package com.param.pharmacy.master.unit.service.pharmacologicalClassification;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationDetailsResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationRequest;

public interface IPharmacologicalClassificationService {

	List<PharmacologicalClassificationListResponse> getAll(Integer orgId);

	PharmacologicalClassificationDetailsResponse getById(Integer id);

	CommonResponse create(PharmacologicalClassificationRequest pharmacologicalClassificationRequest);

	CommonResponse update(PharmacologicalClassificationRequest pharmacologicalClassificationRequest);

}
