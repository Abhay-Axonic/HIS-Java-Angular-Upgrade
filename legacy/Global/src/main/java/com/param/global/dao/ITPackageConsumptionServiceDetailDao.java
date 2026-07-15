package com.param.global.dao;

import java.math.BigDecimal;

import com.param.billing.packages.model.TPackageConsumptionServiceDetail;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionServiceDetailDao extends IGenericDao<TPackageConsumptionServiceDetail, Integer>{
	public Response saveTPackageConsumptionServiceDetail(TPackageConsumptionServiceDetailDto dto)throws Exception;
	public Response getPackageServiceByPackageConsumItemId(int packageConsumptionId, int itemId)throws ServiceException;
	public Response getPackageServiceByPackageConsumServiceId(int packageConsumptionId, int serviceId)throws ServiceException;
	public Response updatePackageConsumptionServiceQty(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptionEOBalanceQty(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptionCapBalanceQty(Integer packageId, Integer patientId, Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount,Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptSevcQtyOnSrvCancl(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId, Integer admissionId, Integer visitTypeId,Integer createdBy)throws ServiceException;
	public Response updatePackageConsumptSevcQtyOnSrvCanclME(Integer packageId, Integer patientId, Integer encounterId, Integer serviceId, Integer admissionId, Integer visitTypeId,Integer createdBy)throws ServiceException;
	public Response getPackageServicesByEncounterPackageId(int packageId, Integer encounterId, Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response getPackageServicesByEncounterPackageIdWithMultyEnc(int patientId,int packageId, Integer encounterId, Integer admissionId, Integer visitTypeId)throws ServiceException;
	public Response updatePackageConsumptionServiceQtyMultiEnc(Integer packageId, Integer patientId,Integer serviceId,Integer admissionId, Integer visitTypeId,Integer createdBy)throws ServiceException;
	
	public Response updatePackageConsumptionItemeQtyMultiEnc(Integer packageId, Integer patientId,Integer itemId,Integer qty,Integer visitId, Integer visitTypeId,Integer createdBy)throws ServiceException;
	public Response updatePackageConsumptQtyITemCancelME(Integer packageId, Integer patientId, Integer encounterId, Integer itemId, Integer admissionId, Integer visitTypeId,Integer qty,Integer createdBy)throws ServiceException;
}