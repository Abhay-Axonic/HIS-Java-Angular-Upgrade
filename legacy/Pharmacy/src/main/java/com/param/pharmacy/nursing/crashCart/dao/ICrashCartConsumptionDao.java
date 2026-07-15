package com.param.pharmacy.nursing.crashCart.dao;

import java.util.List;

import com.param.entity.model.pharmacy.CrashCartRequest;
import com.param.entity.model.pharmacy.CrashCartRequestDetail;


public interface ICrashCartConsumptionDao {

	List<CrashCartRequest> crashCartSearch(com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest request, Integer screenId);

	CrashCartRequest saveCrashCart(CrashCartRequest crashCartRequest);

	CrashCartRequest getCrashCartRequest(Integer id, Integer unitId);

	List<CrashCartRequestDetail> getCrashCartRequestDetail(Integer id);

	CrashCartRequest updateCrashCartRequest(CrashCartRequest crashCartRequest);

	Integer getDoctorId(Integer id);
}
