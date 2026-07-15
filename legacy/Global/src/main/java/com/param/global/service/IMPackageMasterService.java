package com.param.global.service;

import java.util.List;
import java.util.Map;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.*;
import com.param.global.model.OrderDetailsMaster;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMPackageMasterService {
	public Response createEhcPackage(ParentPackageMasterDto parentPackageMasterDto)throws ServiceException;
	public Response SaveAllInclusivePackage(ParentPackageMasterDto parentPackageMasterDto)throws ServiceException;
	public Response createMultiencouterPackage(ParentPackageMasterDto parentPackage)throws ServiceException;
	
	
	public Response searchServiceForPackage(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response searchServiceForOrderSet(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response createEitherOrPackage(ParentPackageMasterDto packageDto)throws ServiceException;
	public Response getEHCPackagesList(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response createPackageWithCap(ParentPackageMasterDto packageDto)throws ServiceException;
	
	public Response searchServicesForBilling(ServiceSearchReqDto reqDto)throws ServiceException;
	public Response searchServicesForPackage(ServiceForPackageReqDto reqDto)throws ServiceException;
	
	
	public Response getPackageDetailsByIdForBilling(List<Integer> packageIds)throws ServiceException;
	public Response getExistingActivePackageByPatientEncounter(Integer patientId, Integer encounterId,Integer visitTypeId,Integer organisationId, Integer unitId,Integer packageId) throws ServiceException;
	public Response checkIfServiceFromPackage(PackageServiceCheckReqDto packageServiceCheckReqDto) throws ServiceException;
	public Response getPackageListByFilter(PackageListReqDto reqDto)throws ServiceException;
	public void savePackageConsumption(BillingMasterDto billingMasterDto,List<OrderDetailsMasterDto> listToBeAdded, Response orderRes) throws Exception;
	public Response getPackageLog(Integer packageConsumptionServiceDetailId)throws ApplicationException;
	public Response packageConsumtionOfItem(BillingMasterDto dto,OrderMasterDto orderDto)throws BillingException;
	public Response getPackageListForIPDPackageOrder(Integer admissionId, Integer orgId, Integer unitId,Integer visitTypeId, Integer visitTypeId2)throws ApplicationException;
	public Response renderPackageService(OrderDetailsMaster dto,AdmissionsForBatchProcessDto  vistDetailsObj,Integer packageId)throws ApplicationException;
	public Response renderPackageServiceFrPostBillig(OrderDetailsMaster dto,AdmissionsForBatchProcessDto  vistDetailsObj,Integer packageId)throws ApplicationException;
	//
	public Response searchPackageCommonFilter(PackageSearchRequestDto searchDto)throws ApplicationException;
	public Response getPackageServicesByPackageId(PackageSearchRequestDto searchDto)throws ApplicationException;


	//edit Package
	public Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> mergeStrem(Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map1,
																			  Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map2);
	public Response editPackage(ParentPackageMasterDto packageDto,Map<Integer,Integer> packageIds)throws ApplicationException;


	public MPackageMasterDto searchMap(Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map1,MPackageMasterDto dto);

	//EHC encounter order
	public Response getEhcByPackageId(EncounterMasterDto encounterMasterDto) throws Exception;



	/*
	 * public Response getEhcByPackageIdOPCoversheet(EncounterMasterDto
	 * encounterMasterDto) throws Exception;
	 */
	public Response saveEhcEncounterEhcConsultation(EncounterMasterDto encounterMasterDto) throws Exception;
	
	
	//PACKAGE CHANGE
	public Response searchServiceForPackageByMultipleCategory(ServiceForPackageReqDto reqDto,PackageMultyServiceSearchReqDto searchDto)throws ServiceException;
	public Response getPackgeByIds(Map<Integer,Integer> packageIds)throws ServiceException;
	
	public Response packageSearchForBilling(ServiceSearchReqDto reqDto)throws ServiceException;
	public Response parentPackage(ServiceSearchReqDto reqDto)throws ServiceException;
	
	
	//Check Package Contract
	public Response checkIfServiceFromPackage(ContractConsumptionDto consumptionDto) throws ServiceException;
	// get package rate
	public Response getPackageRate(ServiceForPackageReqDto reqDto) throws ServiceException;
	//for updating package services in appointment master
	public Response updatePackageService(SlotMasterDto slotMasterDto) throws ServiceException;
	
	//Check Package Contract
	public Response checkServiceAvailableInPackageContract(ContractConsumptionDto consumptionDto) throws ServiceException;
	public Response updatePackage(ContractConsumptionDto contractConsumptionDto) throws ServiceException;
	
	
	//pharamcy
	Response consumeItemFromPackageContract(PharmacyBillCommonDto dto, Integer pharmacyRec) throws Exception;
	Response consumeItemFromPackage(PharmacyBillCommonDto dto) throws Exception;
	public Response getPackageDetailsByIdForBillingTransaction(Map<Integer, Integer> consumtionIdPackageIds)throws Exception;
	public Response forceCancelPackage(PackageConsumptionMasterResDto reqDto)throws Exception;
	public Response inactivePackage(Integer parentPkgId, Integer packageTypeId, boolean flag)throws Exception;
	public Response inactiveParentPackage(Integer parentPkgId, boolean flag)throws Exception;
	public Response getPackageListForIPDPackageOrderForBookView(Integer patientId, String fromDate, String toDate)throws Exception;
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto bdto)throws Exception;
	public Response getAllInclusivePackageAmount(BillingMasterDto bdto)throws Exception;


    Response getMPackageForIPDCoversheetByDate(PackageConsumptionMasterResDto dto) throws Exception;

	Response getUniqueDateForIPDCoversheet(PackageConsumptionMasterResDto dto) throws Exception;
	public Response getPackageListOnCreditNote(Integer patientId, Integer admissionId, Integer orgId, Integer unitId,
			Integer visitTypeId);

	Response packageListForWeb(PackageListReqDto reqDto)throws Exception;
}
