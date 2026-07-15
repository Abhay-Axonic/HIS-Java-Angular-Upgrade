package com.param.global.dao;

import java.util.List;

import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PackageBBCategoryMpprDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.TPackageConsumptionMasterDto;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageConsumptionMasterDao extends IGenericDao<TPackageConsumptionMaster, Integer>{
	public Response saveTPackageConsumptionMaster(TPackageConsumptionMasterDto dto)throws BillingException;
	public Response getExistingActivePackageByPatientEncounter(Integer patientId, Integer encounterId,Integer visitTypeId, Integer organisationId, Integer unitId,boolean isPharmacy, Integer billingBedCategoryId) throws ServiceException;
	public Response getPackageConsumptionServiceDetailsByPackageIdPatientId(Integer packageId, Integer patientId) throws ServiceException;
	public Response getPackageConsumptionServiceDetailsByPackageIdPatientId(int packageConsumptionMasterId) throws ServiceException;
	public Response cancelPackageConsumption(int packageId,int patientId,Integer encounterId,Integer cancelReasonId,String cancelRemark,Integer admissionId,Integer visitTypeId,Integer orderDetailsId)throws ServiceException;
	public Response getOrderedExclusionServices(int packageConsumptionMasterId)throws ServiceException;
	public Response getActiveMEPackage(Integer patientId, Integer encounterId, Integer visitTypeId, Integer organisationId, Integer unitId) throws ServiceException;
	public Response updateNoOfVisitsME(PackageConsumptionMasterResDto obj) throws ServiceException;
	public Response expireMEPkgByVisit(Integer encounterId,Integer visitTypeId) throws ServiceException;
	public Response expireMEPkgByVisitByPatientId(Integer patientId,Integer visitTypeId) throws ServiceException;
	public Response inactivatePackageForPayee(Integer packageConsumptionMasterId) throws ServiceException;
	public Response cancelPackageConsumption(Integer orderDetailsId)throws ServiceException;
	public Response getBillingBedCategoryIdsByParentPackageId(TPackageConsumptionMasterDto dto)throws BillingException;
	public Response getBillingBedCategoryIdsByPackageConsumptionId(Integer packageConsumptionMasterId)throws BillingException;
	public Response savePackageBbcMppr(PackageBBCategoryMpprDto dto)throws BillingException;
	Response updatePackageApplicableDate(TPackageConsumptionMasterDto tPackageConsumptionMasterDto) throws BillingException;
	Response getExistingActiveMultivisitPackageForDeactivation(List<Integer> unitIds) throws ServiceException;
	Response getPackageServicesByOrderDetailsId(Integer packageId, Integer billId) throws ServiceException;
	
}
