package com.param.billing.ipd.service;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IpdChargesService {
	public Response getAdmissionsById(int admissionId)throws BillingException;

	public Response getAdmissionsByIdForUpdatePayee(int admissionId)throws BillingException;

	public Response getAdmissionsDetailsByAdmissionIdForCancelledBills(int admissionId);
}
