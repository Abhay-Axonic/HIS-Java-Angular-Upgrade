package com.param.global.dao;

import java.util.Set;

import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.MPackageMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageListReqDto;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.ParentPackageMasterDto;
import com.param.global.dto.ServiceForPackageReqDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.opd.encounter.dto.EncounterEhcConsultationMasterDto;
import com.param.opd.encounter.dto.EncounterEhcConsultationMpprDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.model.MPackageMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMPackageMasterDao extends IGenericDao<MPackageMaster, Integer>
{
	
	public Response saveParentPackageMaster(ParentPackageMasterDto parentPackageMasterDto)throws ServiceException;
	public Response savePackageMaster(MPackageMasterDto masterDto)throws ServiceException;
	
	public Response searchServiceForPackage(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response searchServicesForPackage(ServiceForPackageReqDto reqDto)throws ServiceException;
	
	
	public Response searchServiceForPackageByMultipleCategory(ServiceForPackageReqDto reqDto,Set<Integer> serviceIds)throws ServiceException;
	public Response searchItemsForPackageByMultipleCategory(ServiceForPackageReqDto reqDto,Set<Integer> itemIds)throws ServiceException;
	
	public Response searchServiceForOrderSet(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response getEHCPackagesList(ServiceForPackageReqDto reqDto)throws ApplicationException;
	public Response searchServicesForBilling(ServiceSearchReqDto reqDto)throws ServiceException;
	public Response getPackageById(int packageId)throws ServiceException;
	public Response getPackageListByFilter(PackageListReqDto reqDto)throws ServiceException;
	public Response getPackageLog(Integer packageConsumptionServiceDetailId)throws ServiceException;
	public Response checkServiceAssignedOrNot(Integer orgId,Integer unitId,Integer serviceId)throws ServiceException;
	public Response getPackageListForIPDPackageOrder(Integer admissionId,Integer orgId,Integer unitId, Integer visitTypeId, Integer visitTypeId2)throws ServiceException;
	public Response renderPackageService(OrderDetailsMaster dto,AdmissionsForBatchProcessDto vistDetailsObj,Integer pacakgeId)throws ApplicationException;
	public Response renderPackageServiceFrPostBillig(OrderDetailsMaster dto,AdmissionsForBatchProcessDto  vistDetailsObj,Integer packageId)throws ApplicationException;
	public Response searchPackageCommonFilter(PackageSearchRequestDto searchDto)throws ApplicationException;
	public Response getPackageServicesByPackageId(PackageSearchRequestDto searchDto)throws ApplicationException;
	
	
	public Response cancelEhcPackageServiceCR(OrderDetailsMasterDto dto)throws ApplicationException;
	
	

	//ehc consultation
	public Response saveEhcEncounterEhcConsultationMaster(EncounterEhcConsultationMasterDto dto) throws Exception;
	public Response saveEhcEncounterEhcConsultation(EncounterEhcConsultationMpprDto dto) throws Exception;
	public Response getEhcConsultationServices(EncounterMasterDto dto) throws Exception;
	public Response getParentPacakgeById(Integer parentPacakgeId)throws ServiceException;
	public Response getPacakgeByParentId(Integer parentPacakgeId)throws ServiceException;
	public Response inactivePacakge(Integer parentPackageId,Integer packageTypeId, boolean flag, int i)throws ServiceException;
	
	//package search
	public Response packageSearchForBilling(ServiceSearchReqDto reqDto)throws ServiceException;
	public Response parentPackage(ServiceSearchReqDto reqDto)throws ServiceException;
	
	public Response getPackageRate(ServiceForPackageReqDto reqDto)throws ServiceException;
	public Response updatePackageService(SlotMasterDto slotMasterDto)throws ServiceException;
	
	public Integer cancelPackageService(ContractConsumptionDto consumptionDto) throws ServiceException;
	public Integer cancelPackageItem(CommonBillingItemsDto item,boolean isRecalcualtion, boolean pharmacyReturnServiceCal) throws ServiceException;
	public Integer consumePackageItem(CommonBillingItemsDto item,boolean isRecalcualtion) throws ServiceException;
	public Response getPkgOrderDetailsIdByServiceId(ServiceMasterDto dto) throws ServiceException;
	public Response forceCancelPackage(PackageConsumptionMasterResDto reqDto)throws ServiceException;
	public Response inactiveParentPackage(Integer parentPkgId, boolean flag)throws ServiceException;
	public Response getPackageListForIPDPackageOrderForBookView(Integer patientId, String fromDate, String toDate)throws ServiceException;
	
	public Integer getPackageVersionByParentPacakgeId(Integer parentPackageId)throws ServiceException;
	public Integer addPackageItemForReturn(CommonBillingItemsDto item) throws ServiceException;
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto bdto) throws ServiceException;
	public Response getAllInclusivePackageAmount(BillingMasterDto bdto)throws ServiceException;
	public Response getPackageListOnCreditNote(Integer patientId, Integer admissionId, Integer orgId, Integer unitId,
			Integer visitTypeId);

    Response packageListForWeb(PackageListReqDto reqDto)throws ServiceException;
}
