package com.param.global.billing.dao;

import java.util.List;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IpdChargesDao {
	public Response getAdmissionsById(int admissionId)throws BillingException;

	public Response getAdmissionsByIdForUpdatePayee(int admissionId)throws BillingException;

	public Response getAdmissionsDetailsByAdmissionIdForCancelledBills(int admissionId);

	public List<Integer> getTariffIds(int admissionId);
}