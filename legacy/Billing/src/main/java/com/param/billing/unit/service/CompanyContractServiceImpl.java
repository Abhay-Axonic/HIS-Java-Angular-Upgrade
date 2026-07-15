package com.param.billing.unit.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.config.dao.ITPatientContarctCapDetailsDao;
import com.param.billing.global.transaction.config.dto.TPatientContractCapDetailsDto;
import com.param.billing.unit.dao.ICompanyContractDao;
import com.param.billing.unit.dao.ITContractBedBillingCategoryDetailsDao;
import com.param.billing.unit.dao.ITContractCapDetailsDao;
import com.param.billing.unit.dao.ITContractGroupDetailsDao;
import com.param.billing.unit.dao.ITContractGroupPharmacyExclusionDetailsDto;
import com.param.billing.unit.dao.ITContractProcedureDetailsDao;
import com.param.billing.unit.dao.ITContractServiceDetailsDao;
import com.param.billing.unit.dto.ChargeConversionDto;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.ItemsForContractDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.service.dto.CompanyContractReqDto;
import com.param.service.dto.ContarctConsumptionResDto;
import com.param.service.dto.ContractServiceCheckReqDto;
import com.param.service.dto.MCompanyContractMasterDto;
import com.param.service.dto.ServiceForCompnayContarctReqDto;
import com.param.service.dto.TContractBedCategoryDetailDto;
import com.param.service.dto.TContractCapDetailsDto;
import com.param.service.dto.TContractGroupDetailsDto;
import com.param.service.dto.TContractGroupPharmacyDetailsDto;
import com.param.service.dto.TContractProcedureDetailsDto;
import com.param.service.global.model.MCompanyContractMaster;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompanyContractServiceImpl implements ICompanyContractService, ICommonConstants {

	@Autowired
	ICompanyContractDao iCompanyContractDao;
	@Autowired
	ITContractBedBillingCategoryDetailsDao iTContractBedBillingCategoryDetailsDao;
	@Autowired
	ITContractGroupPharmacyExclusionDetailsDto iTContractGroupPharmacyExclusionDetailsDto;
	@Autowired 
	ITContractCapDetailsDao iTContractCapDetailsDao;
	@Autowired
	ITContractProcedureDetailsDao iTContractProcedureDetailsDao;
	@Autowired
	ITContractServiceDetailsDao iTContractServiceDetailsDao;
	@Autowired 
	ITPatientContarctCapDetailsDao iTPatientContarctCapDetailsDao;
	@Autowired
	ITContractGroupDetailsDao iTContractGroupDetailsDao;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;

	@Override
	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyContractDao.getCompanyMasterList(companyMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAssociateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyContractDao.getAssociateCompanyMaster(companyMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGradeListByAssociateCompanyId(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyContractDao.getGradeListByAssociateCompanyId(companyMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVisitTypeList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		try {
			return iCompanyContractDao.getVisitTypeList(companyMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response createCompanyContarct(CompanyContractReqDto reqDto)
			throws ApplicationException {
		try{
			
			Response resByName=iCompanyContractDao.getCompanyContarctName(reqDto.getCompanyContractMasterDto());
            if(resByName.getListObject()!=null && resByName.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			MCompanyContractMasterDto contractMasterDto = reqDto.getCompanyContractMasterDto();
			List<TContractServiceDetailsDto> contractServiceList = new LinkedList<>();
			if(contractMasterDto != null){
				//save Company Contract master
				Character statusChange = 'I';
				contractMasterDto.setOrgId(reqDto.getOrgId());
				contractMasterDto.setUnitId(reqDto.getUnitId());
				contractMasterDto.setOldContractId(null);
				contractMasterDto.setParentContractId(null);
				
				if(reqDto.getParentContractId() != null && reqDto.getParentContractId() > 0) {
					contractMasterDto.setParentContractId(reqDto.getParentContractId());
				}
				
				if(reqDto.getOldContractId() != null && reqDto.getOldContractId() > 0) {
					Character isVersioning = 'Y';
					Integer parConId = reqDto.getParentContractId();
				   iCompanyContractDao.updateStatus(reqDto.getOldContractId(),statusChange,isVersioning,parConId);
				   contractMasterDto.setVersionNo(reqDto.getVersionNo() + 1);
				   contractMasterDto.setOldContractId(reqDto.getOldContractId());
				}
				else
					contractMasterDto.setVersionNo(1);
				Response contractRes = iCompanyContractDao.createCompanyContarct(contractMasterDto);
				if(contractRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && contractRes.getObject() != null){
					MCompanyContractMaster contractMaster = (MCompanyContractMaster)contractRes.getObject();
					Integer contractId = contractMaster.getContractId();
					
					/*if(contractMasterDto.getIsTariffRateApplicable()=='Y')
					{
						List<TContractServiceDetailsDto> list = iCompanyContractDao.getTariffRatesForContract(contractMasterDto);
						reqDto.setListTContractServiceDetailsDto(list);
					}*/
					
					//save Contract Group Exclusion
					if(reqDto.getListContractGroupExclusion() != null && reqDto.getListContractGroupExclusion().size() > 0)
					{
						
						ListIterator<TContractGroupDetailsDto> consumDtlsItr = reqDto.getListContractGroupExclusion().listIterator();
						TContractGroupDetailsDto contractGroupExclusionDto = null;
						while(consumDtlsItr.hasNext()){
								contractGroupExclusionDto = consumDtlsItr.next();
								contractGroupExclusionDto.setContractId(contractId);
								contractGroupExclusionDto.setOrgId(reqDto.getOrgId());
								contractGroupExclusionDto.setUnitId(reqDto.getUnitId());
							iTContractGroupDetailsDao.saveContractGroupExclusionDetails(contractGroupExclusionDto);
						}
					}
					
					
					//save Contract Group Details
					if(reqDto.getListContractGroupWiseDetails() != null && reqDto.getListContractGroupWiseDetails().size() > 0)
					{
						
						ListIterator<TContractGroupDetailsDto> consumDtlsItr = reqDto.getListContractGroupWiseDetails().listIterator();
						TContractGroupDetailsDto contractGroupExclusionDto = null;
						while(consumDtlsItr.hasNext())
						{
							contractGroupExclusionDto = consumDtlsItr.next();
								contractGroupExclusionDto.setContractId(contractId);
								contractGroupExclusionDto.setOrgId(reqDto.getOrgId());
								contractGroupExclusionDto.setUnitId(reqDto.getUnitId());
							iTContractGroupDetailsDao.saveContractGroupDetails(contractGroupExclusionDto);
						}
					}
					
					//save Contract Service details
					if(reqDto.getListTContractServiceDetailsDto() != null && reqDto.getListTContractServiceDetailsDto().size() > 0)
					{
						ListIterator<TContractServiceDetailsDto> consumDtlsItr = reqDto.getListTContractServiceDetailsDto().listIterator();
						TContractServiceDetailsDto servicesDetailsDto = null;
						while(consumDtlsItr.hasNext()){
								servicesDetailsDto = consumDtlsItr.next();
								servicesDetailsDto.setContractId(contractId);
								servicesDetailsDto.setOrgId(reqDto.getOrgId());
								servicesDetailsDto.setUnitId(reqDto.getUnitId());
								servicesDetailsDto.setCreatedBy(reqDto.getCompanyContractMasterDto().getCreatedBy());
							contractServiceList.add(servicesDetailsDto);
							
						}
						iTContractServiceDetailsDao.saveContractServiceDetails(contractServiceList);
					}
				
					//save contract group pharmacy exclusion details
					List<TContractGroupPharmacyDetailsDto> groupPharmacyExclusionDetailsDtoList = new LinkedList<>();
					if(reqDto.getContractGroupPharmacyIncusionDetailsDto() != null && reqDto.getContractGroupPharmacyIncusionDetailsDto().getProductCategoryList().size()>0)
					{
						for(Integer prodCatId : reqDto.getContractGroupPharmacyIncusionDetailsDto().getProductCategoryList())
						{
							TContractGroupPharmacyDetailsDto detailsDto = new TContractGroupPharmacyDetailsDto();
								detailsDto.setContractId(contractId);
								detailsDto.setOrgId(reqDto.getOrgId());
								detailsDto.setUnitId(reqDto.getUnitId());
								detailsDto.setProductCategoryId(prodCatId);
							groupPharmacyExclusionDetailsDtoList.add(detailsDto);
//							excludedProdCatIds.add(prodCatId);
						}
						iTContractGroupPharmacyExclusionDetailsDto.saveContractGroupPharmacyExclusionDetails(groupPharmacyExclusionDetailsDtoList);
					}
					
					//save contract group pharmacy inclusion details
					List<TContractGroupPharmacyDetailsDto> groupPharmacyInclusionDetailsDtoList = new LinkedList<>();
					List<Integer> productCatFromCapDetails = new LinkedList<>();
					if(reqDto.getListTContractCapDetailsDto() != null && reqDto.getListTContractCapDetailsDto().size()>0)
					{
							reqDto.getContractGroupPharmacyIncusionDetailsDto().setUnitId(reqDto.getUnitId());
							if(reqDto.getListTContractCapDetailsDto() != null && reqDto.getListTContractCapDetailsDto().size() > 0)
							{
								for(TContractCapDetailsDto dto : reqDto.getListTContractCapDetailsDto())
								{
									if(dto.getProductCategoryId()!=null && dto.getProductCategoryId()>0)
									{
										productCatFromCapDetails.add(dto.getProductCategoryId());
									}
								}
							}
							Set<Integer> excludeProdCatList=new HashSet<>();
							if(reqDto.getContractGroupPharmacyIncusionDetailsDto().getItemList().size()>0){
								ListIterator<TContractGroupPharmacyDetailsDto> iterator = reqDto.getContractGroupPharmacyIncusionDetailsDto().getItemList().listIterator();
								while(iterator.hasNext())
								{
									TContractGroupPharmacyDetailsDto dto = iterator.next();
									TContractGroupPharmacyDetailsDto masterDto = new TContractGroupPharmacyDetailsDto();
									masterDto.setContractId(contractId);
									masterDto.setItemId(dto.getItemId());
									masterDto.setProductCategoryId(dto.getProductCategoryId());
									masterDto.setIsEquipment('N');
									masterDto.setOrgId(contractMasterDto.getOrgId());
									masterDto.setUnitId(contractMasterDto.getUnitId());
									masterDto.setCreatedBy(contractMasterDto.getCreatedBy());
									groupPharmacyInclusionDetailsDtoList.add(masterDto);
									excludeProdCatList.add(dto.getProductCategoryId());
									
								}
							}
							
							reqDto.getContractGroupPharmacyIncusionDetailsDto().setProductCategoryList(productCatFromCapDetails);

							if(reqDto.getContractGroupPharmacyIncusionDetailsDto().getProductCategoryList().size()>0)
							{
							
								
									Response res = iCompanyContractDao.getInclusionItemList(reqDto.getContractGroupPharmacyIncusionDetailsDto(),excludeProdCatList);
									ListIterator<ItemsForContractDto> itemList = res.getListObject().listIterator();
									while(itemList.hasNext())
									{
										ItemsForContractDto dto = itemList.next();
										TContractGroupPharmacyDetailsDto masterDto = new TContractGroupPharmacyDetailsDto();
										masterDto.setContractId(contractId);
										masterDto.setItemId(dto.getItemId());
										masterDto.setProductCategoryId(dto.getProductCategoryId());
										masterDto.setIsEquipment('N');
										masterDto.setOrgId(contractMasterDto.getOrgId());
										masterDto.setUnitId(contractMasterDto.getUnitId());
										masterDto.setCreatedBy(contractMasterDto.getCreatedBy());
										groupPharmacyInclusionDetailsDtoList.add(masterDto);								
									}
								
								
							}
							
							
							
							if(reqDto.getContractGroupPharmacyIncusionDetailsDto().getEquipmentList()!=null && reqDto.getContractGroupPharmacyIncusionDetailsDto().getEquipmentList().size()>0 ){
								
								ListIterator<TContractGroupPharmacyDetailsDto> listIterator = reqDto.getContractGroupPharmacyIncusionDetailsDto().getEquipmentList().listIterator();
								while(listIterator.hasNext()){
									
									TContractGroupPharmacyDetailsDto dto = listIterator.next();
									TContractGroupPharmacyDetailsDto masterDto = new TContractGroupPharmacyDetailsDto();
									masterDto.setContractId(contractId);
									masterDto.setItemId(dto.getItemId());
									masterDto.setProductCategoryId(dto.getProductCategoryId());
									masterDto.setIsEquipment('Y');
									masterDto.setOrgId(contractMasterDto.getOrgId());
									
									
									masterDto.setUnitId(contractMasterDto.getUnitId());
									masterDto.setCreatedBy(contractMasterDto.getCreatedBy());
									groupPharmacyInclusionDetailsDtoList.add(masterDto);
								}
							}
							if(groupPharmacyInclusionDetailsDtoList.size()>0 && groupPharmacyInclusionDetailsDtoList!=null){
								iTContractGroupPharmacyExclusionDetailsDto.saveContractGroupPharmacyInclusionDetails(groupPharmacyInclusionDetailsDtoList);
							}
							
					}
					
					
					//save contarct bed category details
					if(reqDto.getListTContractBedCategoryDetailDto() != null && reqDto.getListTContractBedCategoryDetailDto().size() > 0){
						ListIterator<TContractBedCategoryDetailDto> contractBedCatItr = reqDto.getListTContractBedCategoryDetailDto().listIterator();
						TContractBedCategoryDetailDto bedCategoryDetailsDto = null;
						while(contractBedCatItr.hasNext()){
							bedCategoryDetailsDto = contractBedCatItr.next();
							bedCategoryDetailsDto.setContractId(contractId);
							bedCategoryDetailsDto.setOrgId(reqDto.getOrgId());
							bedCategoryDetailsDto.setOrgUnitId(reqDto.getUnitId());
							iTContractBedBillingCategoryDetailsDao.saveContractBedBillingCategoryDetails(bedCategoryDetailsDto);
						}
					}
					
					
					//save contract cap amount
					if(reqDto.getListTContractCapDetailsDto() != null && reqDto.getListTContractCapDetailsDto().size() > 0){
						ListIterator<TContractCapDetailsDto> contractCapAmtItr = reqDto.getListTContractCapDetailsDto().listIterator();
						TContractCapDetailsDto contractCapDetailsDto = null;
						while(contractCapAmtItr.hasNext()){
								contractCapDetailsDto = contractCapAmtItr.next();
								contractCapDetailsDto.setContractId(contractId);
								contractCapDetailsDto.setOrgId(reqDto.getOrgId());
								contractCapDetailsDto.setUnitId(reqDto.getUnitId());
							   if((contractCapDetailsDto.getDepartmentId()!=null && contractCapDetailsDto.getDepartmentCapAmount()>0)
								||(contractCapDetailsDto.getProductCategoryId()!=null && (contractCapDetailsDto.getQuantity()!=null||(contractCapDetailsDto.getAmount()!=null && contractCapDetailsDto.getAmount()>0)))
								||(contractCapDetailsDto.getServiceId()!=null && (contractCapDetailsDto.getQuantity()!=null||(contractCapDetailsDto.getAmount() !=null && contractCapDetailsDto.getAmount()>0))))
								
								iTContractCapDetailsDao.saveContractCapDetails(contractCapDetailsDto);
						}
					}
					
					if(reqDto.getProcedureDetailsDtosList() != null && reqDto.getProcedureDetailsDtosList().size() > 0){
						ListIterator<TContractProcedureDetailsDto> contractGroupDtlsItr = reqDto.getProcedureDetailsDtosList().listIterator();
						TContractProcedureDetailsDto contractGroupDetailsDto = null;
						while(contractGroupDtlsItr.hasNext()){
							contractGroupDetailsDto = contractGroupDtlsItr.next();
							contractGroupDetailsDto.setContractId(contractId);
							contractGroupDetailsDto.setOrgId(reqDto.getOrgId());
							contractGroupDetailsDto.setUnitId(reqDto.getUnitId());
							iTContractProcedureDetailsDao.saveContractProcedureDetails(contractGroupDetailsDto);
						}
					}
					
					return new Response<>(SUCCESS, SUCCESS_CODE, COMPANY_CONTRACT_CREATED, null, null);
				}
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.searchCompanyContract(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response CompanyContractListSearch(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.CompanyContractListSearch(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getContractByCompanyId(CompanyMasterDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.getContractByCompanyId(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceRateByCompanyId(CompanyMasterDto reqDto) throws ServiceException {
		try {
			
			ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
				consumptionDto.setOrganizationId(reqDto.getOrganizationId());
				consumptionDto.setUnitId(reqDto.getUnitId());
				consumptionDto.setVisitTypeId(reqDto.getVisitTypeId());
				consumptionDto.setBillingBedCategoryId(reqDto.getBillingBedCategoryId());
				consumptionDto.setServiceId(reqDto.getServiceId());
				consumptionDto.setCurrentDate(reqDto.getOrderDate());
				consumptionDto.setContractId(reqDto.getContractId());
				consumptionDto.setPatientTypeId(reqDto.getPatientTypeId());
			consumptionDto =iEncounterMasterDao.getServiceRateByCompanyId(consumptionDto);
			List<ContractConsumptionDto> list = new LinkedList<>();
			if(consumptionDto!=null)
				list.add(consumptionDto);
			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public com.param.billing.common.Response savePatientContarctCapDetails(TPatientContractCapDetailsDto tPatientContractCapDetailsDto)
			throws ApplicationException {
		try {
			return iTPatientContarctCapDetailsDao.savePatientContarctCapDetails(tPatientContractCapDetailsDto);
		} catch (Exception e) {
			
		}
		return new com.param.billing.common.Response(ERROR, SERVER_ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response checkIfServiceFromContarct(ContractServiceCheckReqDto contractServiceCheckReqDto)
			throws ServiceException {
		try{
			Integer encounterId = contractServiceCheckReqDto.getEncounterId();
			Integer visitTypeId = contractServiceCheckReqDto.getVisitType();
			Integer admissionId = contractServiceCheckReqDto.getAdmissionId();
			Integer organisationId = contractServiceCheckReqDto.getOrganisationId();
			Integer unitId = contractServiceCheckReqDto.getUnitId();
			Integer serviceId = contractServiceCheckReqDto.getServiceId();
			//deptId,  subDeptId, payementEntitlementType,
			
			List<ContarctConsumptionResDto> finalContractMasterDto =  new ArrayList<>();
			com.param.billing.common.Response contarctResponse = iTPatientContarctCapDetailsDao.getExistingContarctByPatientEncounter( encounterId, visitTypeId,  admissionId,  serviceId,organisationId, unitId);
				if(contarctResponse.getCode().equals(SUCCESS_CODE) && contarctResponse.getListObject() != null){
					List<ContarctConsumptionResDto> listResDto= contarctResponse.getListObject();
					for(ContarctConsumptionResDto resDto : listResDto){
						if(resDto.getContractId()!=null) {
							com.param.billing.common.Response responseDto=iTPatientContarctCapDetailsDao.getContacrtConsumptionServiceDetailsByContarctIdServiceId(resDto.getContractId(),serviceId);
							if(responseDto.getCode().equals(SUCCESS_CODE) && responseDto.getListObject() != null){
								List<ContarctConsumptionResDto> listResssDto= responseDto.getListObject();
								for(ContarctConsumptionResDto ressDto : listResssDto){
									resDto.setContractId(ressDto.getContractId());
									resDto.setFinalPrice(ressDto.getFinalPrice());
									resDto.setIsfromCap(ressDto.getIsfromCap());
								}
								
							}
						}	
						finalContractMasterDto.add(resDto);
				}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalContractMasterDto, null);
			 }
				return new Response<>(ERROR, NOT_FOUND_CODE, SERVICE_NOT_AVAILABLE_IN_ACTIVE_CONTACRT, null, null);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	public Response getChargeConversionListByCurrentPayer(ChargeConversionDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.getChargeConversionListByCurrentPayer(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getChargeConversionListByConvertPayer(ChargeConversionDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.getChargeConversionListByConvertPayer(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getItemListByAssetAndProductCategory(ItemsForContractDto itemsForContractDto)
			throws ServiceException {
		try {
			return iCompanyContractDao.getItemListByAssetAndProductCategory(itemsForContractDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProductAssetInclusionList(ItemsForContractDto itemsForContractDto) throws ServiceException {
		try {
			return iCompanyContractDao.getProductAssetInclusionList(itemsForContractDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getItemListByProductCategoryForEquipment(ItemsForContractDto itemsForContractDto)
			throws ServiceException {
		try {
			return iCompanyContractDao.getItemListByProductCategoryForEquipment(itemsForContractDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCompanyContractDetailByContractId(CompanyContractReqDto companyContractDto)
			throws ServiceException {
		try {
			return iCompanyContractDao.getCompanyContractDetailByContractId(companyContractDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatus(Integer contractId, Character status) throws ServiceException {
		try {
			 Character isVersioning = 'N';
			 Integer parConId  = null;
			return iCompanyContractDao.updateStatus(contractId,status,isVersioning,parConId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchCompanyContractForVersion(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.searchCompanyContractForVersion(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getContractDetailsBySubDept(TContractGroupDetailsDto reqDto) throws ServiceException {
		try {
			return iCompanyContractDao.getContractDetailsBySubDept(reqDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addServicesInContract(List<TContractServiceDetailsDto> masterDtoList) {
		try{
			return iTContractServiceDetailsDao.saveContractServiceDetails(masterDtoList);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAssociateCompanyMasterByMultipleCompanyIds(CompanyMasterDto companyMasterDto)
			throws ServiceException {
		try{
			return iCompanyContractDao.getAssociateCompanyMasterByMultipleCompanyIds(companyMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}

	
