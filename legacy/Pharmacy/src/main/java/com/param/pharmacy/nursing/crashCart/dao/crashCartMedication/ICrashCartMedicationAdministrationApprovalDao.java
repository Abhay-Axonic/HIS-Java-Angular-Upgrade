package com.param.pharmacy.nursing.crashCart.dao.crashCartMedication;

import java.util.List;

import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;

public interface ICrashCartMedicationAdministrationApprovalDao {

	List<com.param.entity.model.pharmacy.CrashCartRequest> crashCartMedicationSearch(CrashCartRequest request, Integer screenId);

	com.param.entity.model.pharmacy.CrashCartRequest updateCrashCasrtRequest(com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest);

}
