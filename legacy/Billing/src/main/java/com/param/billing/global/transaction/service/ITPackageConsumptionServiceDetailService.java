package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionServiceDetailService {
	public Response updatePackageConsumptionServiceQty(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptionEOBalanceQty(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptionCapBalanceQty(Integer packageId, Integer patientId, Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptSevcQtyOnSrvCancl(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId, Integer admissionId, Integer visitTypeId,Integer createdBy)throws ServiceException;
}
