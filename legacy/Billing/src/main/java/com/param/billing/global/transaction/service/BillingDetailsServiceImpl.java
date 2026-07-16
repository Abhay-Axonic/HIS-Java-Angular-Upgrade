package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.global.common.BillingCommonMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.ITPackageServicesDetailsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.TPackageServicesDetailsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UpdatePayeeDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class BillingDetailsServiceImpl implements IBillingDetailsService, ICommonConstants{

	@Autowired
	private IBillingDetailsDao iBillingDetailsDao;
	
	@Autowired
	private IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IBillingMasterService iBillingMasterService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	ITPackageServicesDetailsDao iTPackageServicesDetailsDao;

	@Autowired
	private BillingCommonMethods billingCommonMethods;
	
	@Override
	public Response saveBillingDetails(List<BillingDetailsDto> billingDetailsDtoList) throws ApplicationException {
		try {
				return iBillingDetailsDao.saveBillingDetails(billingDetailsDtoList);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getBillDetailsByBillId(int orgId, int unitId, int billId) throws ApplicationException {
		try {
			return iBillingDetailsDao.getBillDetailsByBillId(orgId, unitId, billId);
	}catch (Exception e) {
		
		throw e;
	}
	}

	@Override
	@Transactional
	public Response getTransactionLogs(BillingDetailsDto billingDetailsDto)throws ApplicationException {
		try{
			return iBillingDetailsDao.getTransactionLogs(billingDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
	}	
	
	@Override
	@Transactional
	public Response getBillServicesForAddendumBill(int billId) throws ApplicationException {
		try {
			return iBillingDetailsDao.getBillServicesForAddendumBill(billId);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@Override	
	@Transactional
	public Response updatePayeeForPackage(UpdatePayeeDto updatePayeeDto,List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList,List<OrderDetailsByEncounterIdDto> packageList, List<OrderDetailsMasterDto> orderDetailsList) throws Exception 
	{
		try {
			
			UnitApplicationConfigurationDto configDto = new UnitApplicationConfigurationDto();
			configDto.setOrganizationId(updatePayeeDto.getOrgId());
			configDto.setUnitId(updatePayeeDto.getUnitId());
			com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(configDto);
			if (configRes.getObject() != null) 
			{
				configDto = (UnitApplicationConfigurationDto) configRes.getObject();
			}
			List<Integer> packageNeedsToBeCancelled = new LinkedList<>();
			List<OrderDetailsMasterDto> listPackageUpdate = new ArrayList<>();
			if(packageList!=null && !packageList.isEmpty())
			{
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = new LinkedList<>();
				List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList = new LinkedList<>();
				
				
				OrderDetailsMasterDto ordDto = null;
				OrderDetailsMaster orderDetailsDto = null;
				
				Map<Integer,Integer> packageOrders = new LinkedHashMap<Integer,Integer>();
				for (OrderDetailsByEncounterIdDto dto : packageList) {
						 Integer oldPackageId = dto.getPackageId();	
						 BigDecimal serviceAmt = BigDecimal.ZERO;
						 BigDecimal taxAmt = BigDecimal.ZERO;
						 BigDecimal netAmt = BigDecimal.ZERO;
					   
					     orderDetailsDto = iOrderDetailsMasterService.getOrderDetailsByIdForPayeeUpdate(dto.getOrderDetailsId());
					 
						 ordDto  = new OrderDetailsMasterDto();
						 
						 String orderDate = orderDetailsDto.getOrderDate()!=null ? GlobalCommonDateUtils.getStringDate(dto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"): 
							 	GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()), "yyyy-MM-dd HH:mm:ss");
						 
						 ordDto.setPackageApplicableDate(dto.getPackageApplicableDate());
						 ordDto.setOrderDetailsId(dto.getOrderDetailsId());
						 ordDto.setOrgId(dto.getOrgId());
						 ordDto.setOrgUnitId(dto.getOrgUnitId());
						 ordDto.setOrderId(dto.getOrderId());
						 ordDto.setOrderQty(dto.getQuantity());
						 ordDto.setServiceId(dto.getServiceMasterId());
						 ordDto.setPriorityId(dto.getPriorityId());
						 ordDto.setIsOutsourced(dto.getIsOutsourced());
						 ordDto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(orderDate,"yyyy-MM-dd"));
						 ordDto.setOrderSchDate(GlobalCommonDateUtils.getLongDateFromStrigDate(orderDate,"yyyy-MM-dd"));
						 ordDto.setServiceAmt(dto.getBasePrice());
						 ordDto.setOrdConcession(dto.getConcession());
						 ordDto.setOrdDiscount(dto.getDiscount());
						 ordDto.setNetAmount(dto.getNetPay());
						 ordDto.setOrdDocSplId(dto.getDoctorSpecialityId());
						 ordDto.setOrdDocId(dto.getDoctorId());
						 ordDto.setDepartmentId(dto.getServiceSpecialityId());
						 ordDto.setSubDepartmentId(dto.getServiceSubSpecialityId());
						 ordDto.setTaxId(dto.getTaxId());
						 ordDto.setTaxPer(dto.getTaxPercentage());
						 ordDto.setTaxAmount(dto.getTaxAmt());
						 ordDto.setSelfPayable(dto.getSelfPayable());
						 ordDto.setCreditPayable(dto.getCreditPayable());
						 ordDto.setPayeeId(dto.getPayeeId());
						 ordDto.setPackageConsumptionAmt(dto.getPackageConsumptionAmt());
						 ordDto.setOrderSetId(dto.getOrderSetId());
						 ordDto.setContractId(dto.getContractId());
						 ordDto.setBedCategoryId(dto.getBedCategoryId());
						 ordDto.setCoPayPer(dto.getCoPayPer());
						 ordDto.setDocSharePer(dto.getDocSharePer());
						 ordDto.setRefDocSharePer(dto.getRefDocSharePer());
						 ordDto.setOrdTotalAmt(dto.getTotalAmt());
						 ordDto.setOrdCancelBy(dto.getOrdCancelBy());
						 ordDto.setOrdRemarks(dto.getOrdRemarks());
						 ordDto.setTariffId(dto.getTariffId());
						 ordDto.setBillingClassId(dto.getBillingClassId());
						 ordDto.setBedId(dto.getBedId());
						 ordDto.setRoomId(dto.getRoomId());
						 ordDto.setWardId(dto.getWardId());
						 ordDto.setOrderEmergencyFlag(dto.getOrderEmergencyFlag());
						 ordDto.setPackageId(dto.getPackageId());
						 ordDto.setDrugId(dto.getDrugId());
						 ordDto.setBatchId(dto.getBatchId());
						 ordDto.setOldOrdDtlId(dto.getOldOrdDtlId());
						 ordDto.setServiceRendered(dto.getServiceRendered());
						 ordDto.setServiceChargeable(dto.getServiceChargeable());
						 ordDto.setServiceRenderingDeptId(dto.getServiceRenderingDeptId());
						 ordDto.setOrdRenderDatetime(dto.getOrdRenderDatetime());
						 ordDto.setServiceIsBilled(dto.getServiceIsBilled());
						 ordDto.setCreatedDate(dto.getCreatedDate());
						 ordDto.setCreatedBy(dto.getCreatedBy());
						 ordDto.setOrdCancelled(dto.getOrdCancelled());
						 ordDto.setOrdCancelReasonId(dto.getOrdCancelReasonId());
						 ordDto.setOrdCancelDatetime(dto.getOrdCancelDatetime());
						 ordDto.setStatus(dto.getStatus());
						 ordDto.setIsDrug(dto.getIsDrug());
						 ordDto.setIsPackage(dto.getIsPackage());
						 ordDto.setSaleDetailId(dto.getSaleDetailId());
						 ordDto.setPatientClassPercentage(dto.getPatientClassPercentage());
						 ordDto.setMarkupStatus(dto.getMarkupStatus());
						 ordDto.setIsConsultationService(dto.getIsConsultationService());
						 ordDto.setDummyServiceAmt(dto.getDummyServiceAmt());
						 ordDto.setOldNetPay(dto.getNetPay());
						 ordDto.setOldSelfPayable(dto.getSelfPayable());
						 ordDto.setOldCreditPayable(dto.getCreditPayable());
						 ordDto.setIsInvestigationOrder(dto.getIsInvestigationOrder());
						 ordDto.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						
						 
						//For getting package rate
						/** ServiceForPackageReqDto reqDto=new ServiceForPackageReqDto();
						  reqDto.setPaymentEntitlementId(updatePayeeDto.getPaymentEntitlementId());
						  reqDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
						  reqDto.setPatientTypeId(updatePayeeDto.getPatientCategoryId());
						  reqDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
						  reqDto.setServiceId(dto.getServiceMasterId());
						  reqDto.setUnitId(updatePayeeDto.getUnitId());
						  reqDto.setOrgId(updatePayeeDto.getOrgId());*/
			           com.param.global.common.Response  responseRate =	iMPackageMasterService.getPackageRate(billingCommonMethods.createPackageRateFromServiceForPackageDto(updatePayeeDto,dto));
			           if(responseRate.getListObject()!=null && !responseRate.getListObject().isEmpty())
			           {
			        	   ServiceSearchResDto serviceSearchResDto = (ServiceSearchResDto) responseRate.getListObject().getFirst();
			        	   serviceSearchResDto.setContractId(updatePayeeDto.getContractId());
			        	   serviceSearchResDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
			        	   serviceSearchResDto.setPatientCategoryId(updatePayeeDto.getPatientCategoryId());
			        	   serviceSearchResDto.setDefaultSelfTariffId(dto.getTariffId());
						   serviceSearchResDto.setSelfPayeeId(updatePayeeDto.getSelfPayeeId());
						   serviceSearchResDto.setBasePrice(BigDecimal.valueOf(serviceSearchResDto.getPackagePrice()));
						   serviceSearchResDto.setOrganizationId(updatePayeeDto.getOrgId());
						   serviceSearchResDto.setUnitId(updatePayeeDto.getUnitId());
						   serviceSearchResDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
						   serviceSearchResDto.setDefaultSelfTariffId(updatePayeeDto.getDefaultSelfTariffId());
						   serviceSearchResDto.setOrderDate(orderDate);
							if (updatePayeeDto.getAdmissionId()!=null && updatePayeeDto.getAdmissionId() > 0)
								serviceSearchResDto.setAdmissionId(updatePayeeDto.getAdmissionId());
							if (updatePayeeDto.getEncounterId()!=null && updatePayeeDto.getEncounterId() > 0)
								serviceSearchResDto.setEncounterId(updatePayeeDto.getEncounterId());
							
							ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
								consumptionDto.setOrganizationId(dto.getOrgId());
								consumptionDto.setUnitId(dto.getOrgUnitId());
								consumptionDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
								consumptionDto.setPatientTypeId(updatePayeeDto.getPatientCategoryId());
								consumptionDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
								consumptionDto.setPaymentEntitlementId(updatePayeeDto.getPaymentEntitlementId());
								consumptionDto.setServiceId(dto.getServiceMasterId());
								consumptionDto.setCurrentDate(orderDate);
								consumptionDto.setContractId(updatePayeeDto.getContractId());
								consumptionDto.setTariffId(updatePayeeDto.getDefaultSelfTariffId());
								consumptionDto.setOrderDate(orderDate);
								if (updatePayeeDto.getAdmissionId()!=null && updatePayeeDto.getAdmissionId() > 0)
									consumptionDto.setAdmissionId(updatePayeeDto.getAdmissionId());
								if (updatePayeeDto.getEncounterId()!=null &&  updatePayeeDto.getEncounterId() > 0)
									consumptionDto.setEncounterId(updatePayeeDto.getEncounterId());
								
								consumptionDto.setDefaultSelfTariffId(updatePayeeDto.getDefaultSelfTariffId());
								consumptionDto.setSelfPayeeId(updatePayeeDto.getSelfPayeeId());

							com.param.global.common.Response searchRes = iContractConsumptionService.consumePackageFromContract(serviceSearchResDto);
							consumptionDto = (ContractConsumptionDto) searchRes.getObject();
							if (consumptionDto != null)
							{
								
								 serviceAmt = BigDecimal.valueOf(consumptionDto.getServiceRate());
								 taxAmt = BigDecimal.valueOf(consumptionDto.getTaxAmt());
								 netAmt = BigDecimal.valueOf(consumptionDto.getNetAmount());
								 
								 orderDetailsDto.setPackageId(serviceSearchResDto.getPackageMasterId());
								 ordDto.setPackageId(serviceSearchResDto.getPackageMasterId());
								 ordDto.setOldPackageId(oldPackageId);
								 ordDto.setOrdTotalAmt(serviceAmt);
								 ordDto.setServiceAmt(serviceAmt);
								 ordDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
								 ordDto.setTaxAmount(taxAmt);
								 ordDto.setNetAmount(netAmt);
								 ordDto.setHospitalShare(netAmt);
								 ordDto.setCreditPayable(BigDecimal.valueOf(consumptionDto.getCreaditPayable()));
								 ordDto.setSelfPayable(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
								 orderDetailsDto.setContractId(consumptionDto.getContractId());
								 orderDetailsDto.setOrdTotalAmt(serviceAmt);
								 orderDetailsDto.setServiceAmt(serviceAmt);
								 orderDetailsDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
								 orderDetailsDto.setTaxAmount(taxAmt);
								 orderDetailsDto.setNetAmount(netAmt);
								 orderDetailsDto.setHospitalShare(netAmt);
								 orderDetailsDto.setCreditPayable(BigDecimal.valueOf(consumptionDto.getCreaditPayable()));
								 orderDetailsDto.setSelfPayable(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
								 orderDetailsDto.setPayeeId(consumptionDto.getPayeeId());
								 orderDetailsDto.setOrdRenderDatetime(dto.getOrdRenderDatetime());
								 orderDetailsDto.setServiceRendered(dto.getServiceRendered());
								 orderDetailsDto.setOrdDiscount(BigDecimal.ZERO);
								if (consumptionDto.getPayeeId() == updatePayeeDto.getSelfPayeeId()
										&& consumptionDto.getContractId() == null) 
								{
									orderDetailsPayeeMapperDtosList = new LinkedList<>();
									OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(netAmt);
									detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
									orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
								} else
									orderDetailsPayeeMapperDtosList = consumptionDto.getOrderDetailsPayeeMapperDtosList();
								
								if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty())
									orderDetailsPackageMapperDtoList = consumptionDto.getOrderDetailsPackageMapperDtosList();
								
								
								if(dto.getPackageTypeId()!=null && dto.getPackageTypeId()==1)
								{
									com.param.global.common.Response packServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(serviceSearchResDto.getPackageMasterId());
									List<TPackageServicesDetailsDto> packServiceList = packServRes.getListObject();
									
									List<OrderDetailsMasterDto> ehcPackageServicesList = orderDetailsList.stream().filter(packService -> packService.getPackageId()!=null && packService.getPackageId().equals(oldPackageId) && packService.getIsPackageService()=='Y').collect(Collectors.toList());
									if(ehcPackageServicesList!=null && !ehcPackageServicesList.isEmpty())
									{
										for(OrderDetailsMasterDto detailsMasterDto : ehcPackageServicesList)
										{
											TPackageServicesDetailsDto packageServicesDetailsDto =  packServiceList.stream().filter(ehcPack -> ehcPack.getServiceId().equals(detailsMasterDto.getServiceId())).findAny().orElse(null);
											
											if(packageServicesDetailsDto !=null){
												detailsMasterDto.setPackageId(serviceSearchResDto.getPackageMasterId());
												detailsMasterDto.setServiceAmt(BigDecimal.valueOf(packageServicesDetailsDto.getServiceCap()));
												detailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(packageServicesDetailsDto.getServiceCap()));
												detailsMasterDto.setNetAmount(BigDecimal.valueOf(packageServicesDetailsDto.getServiceCap()));
												detailsMasterDto.setPackageConsumptionAmt(BigDecimal.valueOf(packageServicesDetailsDto.getServiceCap()));
												detailsMasterDto.setDummyServiceAmt(BigDecimal.valueOf(packageServicesDetailsDto.getServiceCap()));

												LinkedList<OrderDetailsPackageMapperDto> orderDetailsPackageMapperList = new LinkedList<>();

												OrderDetailsPackageMapperDto detailsPackageMapper = new OrderDetailsPackageMapperDto();
													detailsPackageMapper.setOrderDetailsId(detailsMasterDto.getOrderDetailsId());
													detailsPackageMapper.setPackageId(detailsMasterDto.getPackageId());
													detailsPackageMapper.setIsPackageService(detailsMasterDto.getIsPackageService());
													detailsPackageMapper.setIsCapService(detailsMasterDto.getIsCapService());
													detailsPackageMapper.setPackageAmount(detailsMasterDto.getOrdTotalAmt());
													detailsPackageMapper.setServiceId(detailsMasterDto.getServiceId());
													detailsPackageMapper.setServiceAmount(detailsMasterDto.getOrdTotalAmt());
													detailsPackageMapper.setOrganizationId(detailsMasterDto.getOrgId());
													detailsPackageMapper.setUnitId(detailsMasterDto.getOrgUnitId());
													detailsPackageMapper.setStatus('A');
												orderDetailsPackageMapperList.add(detailsPackageMapper);

												detailsMasterDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageMapperList);
											}
											
										}
										
									}
									
								}
							}
							iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsDto, orderDetailsPayeeMapperDtosList,orderDetailsPackageMapperDtoList);
							packageOrders.put(orderDetailsDto.getPackageId(), orderDetailsDto.getOrderDetailsId());
							listPackageUpdate.add(ordDto);
			           }else if((updatePayeeDto.getContractId()==null || updatePayeeDto.getContractId()==0) && updatePayeeDto.getPaymentEntitlementId()==SELF){
			        	   packageNeedsToBeCancelled.add(dto.getOrderDetailsId());
			           }else{
			        	   listPackageUpdate.add(ordDto);
			           }
			       
				}
				if(!listPackageUpdate.isEmpty())
				{
					/**BillingMasterDto billDto  = new BillingMasterDto();
						billDto.setOrganisationId(updatePayeeDto.getOrgId());
						billDto.setUnitId(updatePayeeDto.getUnitId());
						billDto.setCreatedBy(updatePayeeDto.getCreatedBy());
						billDto.setUpdatedBy(updatePayeeDto.getCreatedBy());
						billDto.setPatientId(updatePayeeDto.getPatientId());
						billDto.setVisitTypeId(updatePayeeDto.getVisitTypeId());
						billDto.setEncounterId(updatePayeeDto.getEncounterId());
						billDto.setAdmissionId(updatePayeeDto.getAdmissionId());
						billDto.setPaymentEntitlementId(updatePayeeDto.getPaymentEntitlementId());
						billDto.setPatientTypeId(updatePayeeDto.getPatientCategoryId());
						billDto.setBillingBedCategoryId(updatePayeeDto.getBillingBedCategoryId());
						billDto.setContractId(updatePayeeDto.getContractId());
						billDto.setTariffId(updatePayeeDto.getDefaultSelfTariffId());*/
					iBillingMasterService.packageConsumption(billingCommonMethods.createBillingMasterDto(updatePayeeDto), listPackageUpdate, new com.param.global.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,packageOrders, null));
				}
			}
			return new Response<>(SUCCESS, null, SUCCESS_MESSAGE, listPackageUpdate, configDto,packageNeedsToBeCancelled);
		} catch (Exception e) {
			
			throw e;
		}
	}



	/*public static void main(String[] args) {
		
		String date = "01/07/2022 17:40:30";
		
		System.out.println(GlobalCommonDateUtils.getLongDateFromStrigDate(date,"yyyy-MM-dd"));
		
	}*/
	

}
