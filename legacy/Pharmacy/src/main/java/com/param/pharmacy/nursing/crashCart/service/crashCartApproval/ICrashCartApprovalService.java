package com.param.pharmacy.nursing.crashCart.service.crashCartApproval;

import java.util.List;

import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;

public interface ICrashCartApprovalService {

	List<CrashCartResponse> getCrashCartApprovalList(CrashCartRequest request);

	Object saveCrashCartApproval(CrashCartRequest request) throws Exception;
	Object saveNewCrashCartApproval(CrashCartRequest request) throws Exception;

	Object crashCartApprovalDetails(Integer id, Integer unitId, Integer orgId) throws Exception;

}
