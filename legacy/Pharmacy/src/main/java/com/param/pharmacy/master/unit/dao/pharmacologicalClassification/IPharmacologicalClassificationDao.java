package com.param.pharmacy.master.unit.dao.pharmacologicalClassification;

import java.util.List;

import com.param.entity.model.master.PharmacologicalClassification;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;

public interface IPharmacologicalClassificationDao {

	List<PharmacologicalClassification> getAll(Integer orgId);

	PharmacologicalClassification getById(Integer id);

	Integer create(PharmacologicalClassification pharmaClassification);

	Integer update(PharmacologicalClassification pharmacologicalClassification);

	List<PharmacologicalClassificationListResponse> getAllPharmacology(Integer organizationId);

}
