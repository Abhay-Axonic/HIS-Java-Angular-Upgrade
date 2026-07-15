package com.param.pharmacy.nursing.crashCart.dao.crashCartApproval;

import java.util.List;

import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;

public interface ICrashCartApprovalDao {

	List<com.param.entity.model.pharmacy.CrashCartRequest> crashCartApprovalSearch(CrashCartRequest request, Integer screenId);

	com.param.entity.model.pharmacy.CrashCartRequest saveCrashCartRequestApproval(com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest);

}
