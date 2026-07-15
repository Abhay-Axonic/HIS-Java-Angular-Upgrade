package com.param.global.dao;

import com.param.billing.packages.model.TPackageConsumptionEoDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dto.TPackageConsumptionEoDetailsDto;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionEoDetailsDao extends IGenericDao<TPackageConsumptionEoDetails, Integer>{
	public Response saveTPackageConsumptionEoDetails(TPackageConsumptionEoDetailsDto dto)throws BillingException;
	public Response getTPackageConsumptionEoDetailsByGrpIdConsumptionId(Integer packageEoGroupId, Integer packageConsumptionId)throws BillingException;
	public Response updateTPackageConsumptionEoQtyCancel(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId)throws BillingException;
}
