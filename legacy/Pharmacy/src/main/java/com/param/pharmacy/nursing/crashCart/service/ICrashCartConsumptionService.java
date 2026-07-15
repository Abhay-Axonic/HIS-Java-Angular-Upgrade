package com.param.pharmacy.nursing.crashCart.service;

import java.util.List;

import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;

public interface ICrashCartConsumptionService {

	List<CrashCartResponse> getCrashCartConsumptionList(CrashCartRequest request);

	Object saveCrashCart(CrashCartRequest request) throws Exception;

	Object crashCartDetails(Integer id, Integer unitId);

	Object cancelCrashCart(CrashCartRequest request);

}
