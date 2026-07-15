package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionCapDetailsService {
	public Response updateTPackageConsumptCapAmtByDeptSubDept(Integer packageId, Integer patientId,Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount, Integer admissionId, Integer visitTypeId)throws BillingException;
}
