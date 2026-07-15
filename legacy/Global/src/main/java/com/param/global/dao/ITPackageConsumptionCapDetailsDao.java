package com.param.global.dao;

import java.math.BigDecimal;

import com.param.billing.packages.model.TPackageConsumptionCapDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionCapDetailsDao extends IGenericDao<TPackageConsumptionCapDetails, Integer>{
	public Response saveTPackageConsumptionCapDetails(TPackageConsumptionCapDetailsDto dto)throws BillingException;
	public Response updateTPackageConsumptCapAmtByDeptSubDept(Integer packageId, Integer patientId,Integer encounterId, Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount, Integer admissionId, Integer visitTypeId)throws BillingException;
	public Response getTPackageConsumptionCapDtlsByPackageEncId(int packageId, Integer encounterId, Integer admissionId, Integer visitTypeId)throws BillingException;
	
	public Response updatePackageCap(Integer packageCapDetailsId ,Double capAmount)throws ServiceException;
	public Response updatePackageServiceCap(Integer pacakgeConsumptionServiceDetailId ,Double capAmount)throws ServiceException;
	public Response updatePackageQty(Integer pacakgeConsumptionServiceDetailId ,Integer qty)throws ServiceException;
	public Response updateEitherOrQuantity(Integer packageConsumptionEODetailId ,Integer qty, boolean isRecalcualtion)throws ServiceException;
	
	//for pharamcy
	public Response updatePackageCapItem(Integer pacakgeConsumptionServiceDetailId, Double capAmount, boolean isRecalcualtion)throws ServiceException;
	public Response updatePackageQuantityCapItem(Integer pacakgeConsumptionServiceDetailId ,Integer qty,Double capAmount, boolean isRecalcualtion)throws ServiceException;
	
	public Response updatePackageCapProductCategory(Integer packageCapDetailsId,Double capAmount, boolean isRecalcualtion, boolean pharmacyReturnServiceCal)throws ServiceException;
	public Response updatePackageQuantityCapProductCategory(Integer packageCapDetailsId ,Integer qty,Double capAmount, boolean isRecalcualtion, boolean pharmacyReturnServiceCal)throws ServiceException;
	
	public Response updateEitherCapItem(Integer packageConsumptionEODetailId ,Double capAmount)throws ServiceException;
	public Response updateEitherQuantityCapItem(Integer packageConsumptionEODetailId ,Integer qty,Double capAmount)throws ServiceException;
}
