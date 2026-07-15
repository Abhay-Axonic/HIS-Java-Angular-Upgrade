package com.param.pharmacy.nursing.crashCart.service.crashCartMedication;

import java.util.List;

import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;

public interface ICrashCartMedicationAdministrationApprovalService {

	List<CrashCartResponse> getCrashCartMedicationList(CrashCartRequest request);

	Object crashCartMedicationDetails(Integer id, Integer unitId);

	Object updateCrashCartMedication(CrashCartRequest request);

}
