package com.param.global.procedure.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientCategoryMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUnitServiceTariffMasterDao;
import com.param.global.dto.CommonDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.dto.VisitTypeMasterDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.dao.IBillOfMaterialDao;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BillOfMaterialServiceImpl implements IBillOfMaterialService, ICommonConstants{

	@Autowired
	IBillOfMaterialDao iBillOfMaterialDao;
	
	@Autowired
	IPatientCategoryMasterDao iPatientCategoryMasterDao;
	
	@Autowired
	IUnitServiceTariffMasterDao iUnitServiceTariffMasterDao;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	private IOrderMasterService iOrderMasterService;
	
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Override
	public Response saveBillOfMaterial(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try{
			Response res = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = simpleDateFormat.format(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(billOfMaterialDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));

			for (BillOfMaterialDto dto : billOfMaterialDto.getBillOfMaterialList()) {
				
				 res = iBillOfMaterialDao.saveBillOfMaterial(dto);
			}
			List<CommonDto> patientCatList = new ArrayList<>();
			Response resPatientCat  = iPatientCategoryMasterDao.getPatientCategoryByOrg(billOfMaterialDto.getBillOfMaterialList().get(0).getOrganizationId());
			if(resPatientCat.getListObject() != null && resPatientCat.getListObject().size()>0) {
				patientCatList.addAll(resPatientCat.getListObject());
				List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtoList = new ArrayList<>();
				for (BillOfMaterialDto dto : billOfMaterialDto.getBillOfMaterialList()) {
					
					 res = iBillOfMaterialDao.getVisitTypeByBillingBedCat(dto.getBillingBedCatId(),dto.getUnitId());
					 
					 List<VisitTypeMasterDto> visitList =  new ArrayList<>();
					 if(res.getListObject()!= null && res.getListObject().size()>0) {
						 visitList.addAll(res.getListObject());
						 
						 Integer totalAmount = 0;
						
						 for (BillOfMaterialDto amount : billOfMaterialDto.getTotalAmount()) {
								
								 if(amount.getBillingBedCatId().equals(dto.getBillingBedCatId())) {
									 totalAmount = amount.getTotalAmt();
								 }
							}
							
						 for (VisitTypeMasterDto visitDto : visitList) {
							 
							 
							for (CommonDto commonDto : patientCatList) {
								
								UnitServiceTariffMasterDto uinttariff = new UnitServiceTariffMasterDto();
								
								uinttariff.setOrganizationId(dto.getOrganizationId());
								uinttariff.setUnitId(dto.getUnitId());
								uinttariff.setServiceId(dto.getProcedureId());
								uinttariff.setTariffId(dto.getTariffId());
								uinttariff.setPaymentEntitlementId(SELF);
								uinttariff.setVisitTypeId(visitDto.getVisitTypeId());
								uinttariff.setPatientTypeId(commonDto.getId());
								uinttariff.setBillingBedCatId(dto.getBillingBedCatId());
								uinttariff.setStatPer("0.00");
								uinttariff.setEffectiveDate(date);
								uinttariff.setRate(totalAmount.doubleValue());
								unitServiceTariffMasterDtoList.add(uinttariff);
							}
						}
					 }
				}
				
				iUnitServiceTariffMasterDao.saveUnitServiceTariffMaster(unitServiceTariffMasterDtoList);
			}
			
			
			
			return  res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialServices(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.getBillOfMaterialServices(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getIsProcedure(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.getIsProcedure(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialListByProcedureId(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.getBillOfMaterialListByProcedureId(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialServiceAmount(List<BillOfMaterialDto> billOfMaterialDto) throws ApplicationException {
		try{
			
			List<BillOfMaterialDto> list = new ArrayList<>();
			
			for (BillOfMaterialDto dto : billOfMaterialDto) {
				
				Response res=  iBillOfMaterialDao.getBillOfMaterialServiceAmount(dto);
					
				if(res.getListObject().size()>0) {
					list.addAll(res.getListObject());
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@SuppressWarnings("null")
	@Override
	public Response updateReservationAgainstAdmissionEncounter(OTReservationDto oTReservationDto)
			throws ApplicationException {
		try{
			
			Response res =  iBillOfMaterialDao.updateReservationAgainstAdmissionEncounter(oTReservationDto);
			if(res.getStatus().equals(SUCCESS)) {
				Response serRes =  iBillOfMaterialDao.getCongServicesByProReqId(oTReservationDto);
				if(serRes.getListObject()!=null && serRes.getListObject().size()>0) {
					List<OTReservationDto> serviceList = new ArrayList<>(); 
					serviceList.addAll(serRes.getListObject());
					
					
					Integer  patientTypeId = null;
					Integer  visitTypeId = null;
					Integer  patientClassId = null;
					Integer  billingBedCategoryId = null;
					Integer  paymentEntitlementId = null;
					Integer  contractId = null;
					Integer  payeeId = null;

					UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
					uAppConfDto.setOrganizationId(serviceList.get(0).getOrganizationId());
					uAppConfDto.setUnitId(serviceList.get(0).getUnitId());
					Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
					if (appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null) 
					{
						uAppConfDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
					}
					if(oTReservationDto.getAdmissionId()!=null && oTReservationDto.getAdmissionId() > 0 ) 
					{
						
						Response admissionDto = iBillOfMaterialDao.getAdmissionsById(oTReservationDto.getAdmissionId());
						AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
							patientTypeId = adminDto.getPatientCategoryId();
							billingBedCategoryId = adminDto.getBedBillingCategoryId();
							paymentEntitlementId = adminDto.getPaymentEntitlementId();
							contractId = adminDto.getContractId();
							payeeId = adminDto.getPayeeId();
							visitTypeId = adminDto.getVisitTypeId();
							patientClassId = adminDto.getPatientClassId();
					}
					
					if(oTReservationDto.getEncounterId()!=null && oTReservationDto.getEncounterId() >0) 
					{
						
						Response encounterRes = iEncounterMasterService.getEncounterDetailsById(oTReservationDto.getEncounterId());
						EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
							patientTypeId = encounterMasterDto.getPatientCategoryId();
							paymentEntitlementId = encounterMasterDto.getPaymentEntitlementId();
							visitTypeId = 1;
							contractId = encounterMasterDto.getContractId();
							payeeId = encounterMasterDto.getPayeeId();
							patientClassId = encounterMasterDto.getPatientClassId();
							billingBedCategoryId = uAppConfDto.getOpdBillingCatId();
					}
					
				
						Date today = TimeZoneComponent.getDateByzone(oTReservationDto.getUnitId());

						OrderMasterDto orderMst = new OrderMasterDto();
						orderMst = new OrderMasterDto();
						orderMst.setAdmissionId(oTReservationDto.getAdmissionId()!=null?oTReservationDto.getAdmissionId() :null);
						orderMst.setEncounterId(oTReservationDto.getEncounterId()!=null?oTReservationDto.getEncounterId() :null);
						orderMst.setCreatedBy(oTReservationDto.getCreatedBy());
						orderMst.setCreatedDate(today.getTime());
						orderMst.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
						orderMst.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
						orderMst.setDiscountPercentage(0);
						orderMst.setOrderDate(today.getTime());
						orderMst.setOrderStatus(ACTIVE);
						orderMst.setOrderStatusId(1);
						orderMst.setOrdRemarks("Procedure Order");
						orderMst.setOrgId(serviceList.get(0).getOrganizationId());
						orderMst.setOrgUnitId(serviceList.get(0).getUnitId());
						orderMst.setPatientId(oTReservationDto.getPatientId());
						orderMst.setPaymentEntitlementTypeId(paymentEntitlementId);
						orderMst.setPriorityId(1);
						orderMst.setUpdatedBy(serviceList.get(0).getCreatedBy());
						orderMst.setUpdatedDate(today.getTime());
						orderMst.setVisitTypeId(oTReservationDto.getVisitTypeId());

						List<OrderDetailsMasterDto> listOrderDetails = new ArrayList();;
					for (OTReservationDto otReservationDto2 : serviceList) {
					

									BigDecimal serviceAmt = BigDecimal.ZERO;
									BigDecimal netAmt = BigDecimal.ZERO;

									OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
										orderDetails.setCreatedBy(otReservationDto2.getCreatedBy());
										orderDetails.setCreatedDate(today.getTime());
								//		orderDetails.setNetAmount(serviceAmt);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrderQty(1);
									//	orderDetails.setOrdTotalAmt(serviceAmt);
										orderDetails.setOrgId(otReservationDto2.getOrganizationId());
										orderDetails.setOrgUnitId(otReservationDto2.getUnitId());
										orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
										//orderDetails.setServiceAmt(serviceAmt);
										orderDetails.setServiceId(otReservationDto2.getServiceId());
										orderDetails.setServiceIsBilled(0);
										orderDetails.setServiceRendered(0);
										orderDetails.setStatus(ACTIVE);
										orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
										orderDetails.setUpdatedBy(otReservationDto2.getCreatedBy());
										orderDetails.setUpdatedDate(today.getTime());
										orderDetails.setPriorityId(2);
										orderDetails.setIsOutsourced('N');
										orderDetails.setOrderSchDate(today.getTime());
										orderDetails.setOrdConcession(BigDecimal.valueOf(0));
										orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
										orderDetails.setServiceRenderingDeptId(otReservationDto2.getSubSpecialityId());
										orderDetails.setOrdCancelled('N');
										orderDetails.setIsDrug('N');
										orderDetails.setIsProcedure(NO);
										orderDetails.setIsProcedureService(NO);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrdRenderDatetime(today.getTime());
										orderDetails.setIsInvestigationOrder('N');
										orderDetails.setProcedureOrderMstId(oTReservationDto.getProReqId());
			
										ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
											consumptionDto.setOrganizationId(serviceList.get(0).getOrganizationId() != null ? serviceList.get(0).getOrganizationId() :sessionService.getOrganizationId());
											consumptionDto.setUnitId(serviceList.get(0).getUnitId() != null ? serviceList.get(0).getUnitId() :sessionService.getUnitId());
											consumptionDto.setVisitTypeId(visitTypeId);
											consumptionDto.setPatientTypeId(patientTypeId);
											consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
											consumptionDto.setPaymentEntitlementId(paymentEntitlementId);
											consumptionDto.setServiceId(otReservationDto2.getServiceId());
											consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "yyyy-MM-dd"));
											consumptionDto.setEncounterId(oTReservationDto.getEncounterId()!=null?oTReservationDto.getEncounterId() :null);
											consumptionDto.setAdmissionId(oTReservationDto.getAdmissionId()!=null?oTReservationDto.getAdmissionId() :null);
											consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
											consumptionDto.setSelfPayeeId(payeeId);
											consumptionDto.setPatientId(oTReservationDto.getPatientId());
											consumptionDto.setContractId(contractId);
											consumptionDto.setPatientCategoryId(patientTypeId);
											consumptionDto.setPatientClassId(patientClassId);
										
										Response res23 = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
										consumptionDto = (ContractConsumptionDto) res23.getObject();

										
										ContractConsumptionDto consumptionDtoForRate = new ContractConsumptionDto();
										
										consumptionDtoForRate.setOrganizationId(serviceList.get(0).getOrganizationId() != null ? serviceList.get(0).getOrganizationId() :sessionService.getOrganizationId());
										consumptionDtoForRate.setUnitId(serviceList.get(0).getUnitId() != null ? serviceList.get(0).getUnitId() :sessionService.getUnitId());
										consumptionDtoForRate.setVisitTypeId(visitTypeId);
										consumptionDtoForRate.setBillingBedCategoryId(billingBedCategoryId);
										consumptionDtoForRate.setServiceId(otReservationDto2.getServiceId());
										consumptionDtoForRate.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "yyyy-MM-dd"));
										consumptionDtoForRate.setEncounterId(oTReservationDto.getEncounterId()!=null?oTReservationDto.getEncounterId() :null);
										consumptionDtoForRate.setAdmissionId(oTReservationDto.getAdmissionId()!=null?oTReservationDto.getAdmissionId() :null);
										consumptionDtoForRate.setPatientCategoryId(patientTypeId);
										consumptionDtoForRate.setTariffId(uAppConfDto.getDefaultSelfTariffId());
										consumptionDtoForRate.setProcedureId(null);
										Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(consumptionDtoForRate, false, false);
										
										if(serviceRateRes.getObject()!=null) {
											
											ContractConsumptionDto 	consumptionDto1 = (ContractConsumptionDto) serviceRateRes.getObject();
											orderDetails.setRate(consumptionDto1.getFinalPrice());
											orderDetails.setOrdConcession(BigDecimal.valueOf(consumptionDto1.getConcession()));
											orderDetails.setNetAmount(BigDecimal.valueOf(consumptionDto1.getNetAmount()));
											orderDetails.setOrdDiscount(BigDecimal.valueOf(consumptionDto1.getOrdDiscount()));
											orderDetails.setOrdDiscountPer(BigDecimal.valueOf(consumptionDto1.getOrdDiscountPer()));
											orderDetails.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto1.getOrdTotalAmount()));
											orderDetails.setPatientClassPercentage(BigDecimal.valueOf(consumptionDto1.getPatientClassPercentage()));
											orderDetails.setTaxAmount(BigDecimal.valueOf(consumptionDto1.getTaxAmt()));
											orderDetails.setTaxPer(BigDecimal.valueOf(consumptionDto1.getTaxPercentage()));

											orderDetails.setServiceAmt(BigDecimal.valueOf(consumptionDto1.getServiceRate()));
										}
										
										
									if (res23.getObject() != null) 
									{
										orderDetails.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetails.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
										orderDetails.setOrdDiscount(new BigDecimal(0.00));
										orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
										orderDetails.setBillingClassId(billingBedCategoryId);
										orderDetails.setOrderEmergencyFlag('N');
										orderDetails.setServiceRendered(0);
										orderDetails.setOrdRenderDatetime(today.getTime());
										orderDetails.setServiceChargeable(1);
										orderDetails.setServiceIsBilled(0);
										orderDetails.setIsDrug('N');
										orderDetails.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetails.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
										orderDetails.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
										orderDetails.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
										orderDetails.setPayeeId(consumptionDto.getPayeeId());
										orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
										orderDetails.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
										orderDetails.setContractId(consumptionDto.getContractId());
										orderDetails.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
										if (consumptionDto.getPayeeId() == payeeId && consumptionDto.getContractId() == null) 
										{
											List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
											OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
												detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
												detailsPayeeMapperDto.setPayeeAmount(netAmt);
												detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
												list.add(detailsPayeeMapperDto);
											orderDetails.setOrderDetailsPayeeMapperDtosList(list);
										} else
											orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
										
										
										if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
											orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
										
										listOrderDetails.add(orderDetails);
									}
					}
					orderMst.setListOrderDetailsMasterDto(listOrderDetails);
					 iOrderMasterService.saveOrderMasterWithDiscount(orderMst, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
		
	}

	@Override
	public Response reAssignOtReservationProcedureAdmissionEncounter(OTReservationDto oTReservationDto)
			throws ApplicationException {
	try{
			
			return iBillOfMaterialDao.reAssignOtReservationProcedureAdmissionEncounter(oTReservationDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response convertOtdepositeToActiveDeposite(OTReservationDto oTReservationDto) throws ApplicationException {
		try{
			return iBillOfMaterialDao.convertOtdepositeToActiveDeposite(oTReservationDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getCongServicesByProReqId(OTReservationDto oTReservationDto) throws ApplicationException {
		try{
			return iBillOfMaterialDao.getCongServicesByProReqId(oTReservationDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialListByProcedureList(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.getBillOfMaterialListByProcedureList(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateStatusProcedure(BillOfMaterialDto billOfMaterialDto) throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.updateStatusProcedure(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getBillOfMaterialListByProcedureIdAll(BillOfMaterialDto billOfMaterialDto)
			throws ApplicationException {
		try{
			
			return iBillOfMaterialDao.getBillOfMaterialListByProcedureIdAll(billOfMaterialDto);
		
		}catch(Exception e){
			
			throw e;
		}
	}

}
