package com.param.billing.global.transaction.service;

import java.math.BigDecimal;
/*
 * 
 * Created by TUSHAR
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.dto.InvestigationDTO;
import com.param.billing.dto.InvestigationOrderDto;
import com.param.billing.global.transaction.dao.IFreeTextInvestigationsDAO;
import com.param.billing.global.transaction.dao.IinvestigationDAO;
import com.param.entity.model.master.Tax;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class InvestigationServiceImpl implements IinvestigationService,ICommonConstants {
	
	@Autowired
	private IinvestigationDAO iinvestigationDAO;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired 
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired 
	IBillingMasterService iBillingMasterService;
	
	@Autowired 
	IFreeTextInvestigationsDAO iFreeTextInvestigationsDAO;
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Override
	@Transactional
	public Response saveOrdersFromInvestigation(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
			
				Integer selfPayeeId =null ;
				Integer defaultTariffId = null;
				Date date  = TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId());
				Response orderRes = null;
				Character isPreBilling = null;
				Integer defaultDiscountOwnership = 0;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
					unitApplicationConfigurationDto.setOrganizationId(orderMasterDto.getOrgId());
					unitApplicationConfigurationDto.setUnitId(orderMasterDto.getOrgUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				
				if(appConfigRes.getObject()!=null)
				{
					UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
					
					if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
					else
						defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
						
					if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
					else
						selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
					
					if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
					else	
						isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
						
					defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
				}else{
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Unit Application Configuration is yet to set !!", null, null);
				}
		     
		   
				ListIterator<OrderMasterDto> iteratorOrderMaster = orderMasterDto.getListOrderMasterDto().listIterator();
				
				while(iteratorOrderMaster.hasNext())	
				{
					List orderDetailsList = new ArrayList();
					OrderMasterDto orderMasterDto2 = iteratorOrderMaster.next();
					orderMasterDto2 = iContractConsumptionService.getPatientClassByVisit(orderMasterDto2); //patientClassId,referralId
					orderMasterDto2.setDefaultDiscountOwnership(defaultDiscountOwnership);
					if(orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
					{
						if(orderMasterDto2.getEncounterId()==null && orderMasterDto2.getAdmissionId()==null)
						{    
							ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
							while(iterator.hasNext())
							{
								OrderMasterDto orderMasterDtoFutureOrder =  orderMasterDto2;
								OrderDetailsMasterDto detailsMasterDto = iterator.next();
								List orderDetailsListFuture = new LinkedList();
								OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
										orderDetailsMasterDto.setServiceId(detailsMasterDto.getServiceId());
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
										orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
										orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
										orderDetailsMasterDto.setStatus('A');
										orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
										orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
										orderDetailsMasterDto.setOrderQty(1);
										orderDetailsMasterDto.setOrdDocId(detailsMasterDto.getOrdDocId());
										orderDetailsMasterDto.setOrdDocSplId(detailsMasterDto.getOrdDocSplId());
										orderDetailsMasterDto.setServiceRenderingDeptId(detailsMasterDto.getServiceRenderingDeptId());
										
//										orderDetailsMasterDto.setPriorityId(2);
										orderDetailsMasterDto.setPriorityId(detailsMasterDto.getServicePriorityId());
										
										orderDetailsMasterDto.setIsOutsourced('N');
										orderDetailsMasterDto.setServiceRendered(0);
										orderDetailsMasterDto.setServiceChargeable(1);
										orderDetailsMasterDto.setOrdCancelled('N');
										orderDetailsMasterDto.setIsDrug('N');
										orderDetailsMasterDto.setIsInvestigationOrder('Y');
										orderDetailsMasterDto.setIsRefDoctorShare(detailsMasterDto.getIsRefDoctorShare());
										orderDetailsMasterDto.setDepartmentId(detailsMasterDto.getDepartmentId());
										orderDetailsMasterDto.setSubDepartmentId(detailsMasterDto.getSubDepartmentId());
										orderDetailsMasterDto.setOrderSetId(detailsMasterDto.getOrderSetId());
										orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
										orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
										orderDetailsMasterDto.setProcedureOrderMstId((detailsMasterDto.getProcedureOrderMstId()!=null && detailsMasterDto.getProcedureOrderMstId()!=0)?detailsMasterDto.getProcedureOrderMstId():null);
										orderDetailsMasterDto.setBedId(detailsMasterDto.getBedId());
										orderDetailsMasterDto.setWardId(detailsMasterDto.getWardId());
										orderDetailsMasterDto.setRoomId(detailsMasterDto.getRoomId());
										orderDetailsMasterDto.setOrdRemarks(detailsMasterDto.getOrdRemarks());
										if(orderDetailsMasterDto.getOrderSchDate()!=null) {
											orderDetailsMasterDto.setOrderSchDate(detailsMasterDto.getOrderSchDate());
										}
										else {
											orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId()).getTime());
										}
							
								orderDetailsListFuture.add(orderDetailsMasterDto);
								orderMasterDtoFutureOrder.setListOrderDetailsMasterDto(orderDetailsListFuture);
								orderRes = iOrderMasterService.saveOrderMaster(orderMasterDtoFutureOrder);
							}
							
						}
						else
						{
							ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
							Integer labOrderId = null;
							while(iterator.hasNext())
							{
								OrderDetailsMasterDto masterDto = iterator.next();
								
								if(masterDto.getIsLabService() != null && masterDto.getIsLabService().equals('Y') && labOrderId == null)
								{
									
									OrderDetailsMasterDto OrderObj = iOrderMasterDao.getOrderIdForLabService(orderMasterDto2.getEncounterId(),orderMasterDto2.getAdmissionId());
									
									if(OrderObj != null){
										
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
										String dateStr = simpleDateFormat.format(TimeZoneComponent.getDateByzone(orderMasterDto2.getOrgUnitId()));
										
										if(OrderObj.getOrderDateString() != null && dateStr.equals(OrderObj.getOrderDateString())){
											orderMasterDto2.setOrderId(OrderObj.getOrderId()!= null ? OrderObj.getOrderId() : null);
										}
									}
									
								}
								
									OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
										orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setOrderQty(1);
										orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
										orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
										orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
										orderDetailsMasterDto.setStatus('A');
										orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
										orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
										orderDetailsMasterDto.setOrdDocId(masterDto.getOrdDocId());
										orderDetailsMasterDto.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
										orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getServiceRenderingDeptId());
										orderDetailsMasterDto.setOrdCancelled('N');
										
//										orderDetailsMasterDto.setPriorityId(2);
										orderDetailsMasterDto.setPriorityId(masterDto.getServicePriorityId());
										orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
										orderDetailsMasterDto.setIsOutsourced('N');
										orderDetailsMasterDto.setIsInvestigationOrder('Y');
										orderDetailsMasterDto.setServiceRendered(0);
										orderDetailsMasterDto.setIsRefDoctorShare(masterDto.getIsRefDoctorShare());
										orderDetailsMasterDto.setDepartmentId(masterDto.getDepartmentId());
										orderDetailsMasterDto.setSubDepartmentId(masterDto.getSubDepartmentId());
										orderDetailsMasterDto.setOrderSetId(masterDto.getOrderSetId());
										orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
										orderDetailsMasterDto.setNursingServiceOrderFlag(masterDto.getNursingServiceOrderFlag());
										orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
										orderDetailsMasterDto.setProcedureOrderMstId((masterDto.getProcedureOrderMstId()!=null && masterDto.getProcedureOrderMstId()!=0)?masterDto.getProcedureOrderMstId():null);
										orderDetailsMasterDto.setBedId(masterDto.getBedId());
										orderDetailsMasterDto.setWardId(masterDto.getWardId());
										orderDetailsMasterDto.setRoomId(masterDto.getRoomId());
										orderDetailsMasterDto.setOrdRemarks(masterDto.getOrdRemarks());
									if(masterDto.isPackage())
									{
										    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
										    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
										    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
											Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(masterDto.getServiceId(),orderMasterDto2.getOrgUnitId());
											BigDecimal serviceAmt = BigDecimal.ZERO;
											BigDecimal netAmt= BigDecimal.ZERO;
											BigDecimal taxAmt= BigDecimal.ZERO;
											if(tax != null)
											{
												orderDetailsMasterDto.setTaxId(tax.getId());
												orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
												serviceAmt = masterDto.getServiceAmt()!=null?masterDto.getServiceAmt():new BigDecimal(0);
												taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
												netAmt = serviceAmt.add(taxAmt);
											}else
											{
												orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
												serviceAmt = masterDto.getServiceAmt();
												netAmt = serviceAmt;
											}
											orderDetailsMasterDto.setOrdTotalAmt(serviceAmt);
											orderDetailsMasterDto.setTaxAmount(taxAmt);
											orderDetailsMasterDto.setNetAmount(netAmt);
											orderDetailsMasterDto.setOrdConcession(masterDto.getOrdConcession());
											orderDetailsMasterDto.setOrdDiscount(masterDto.getOrdDiscount());
											orderDetailsMasterDto.setSelfPayable(masterDto.getSelfPayable());
											orderDetailsMasterDto.setCreditPayable(masterDto.getCreditPayable());
											orderDetailsMasterDto.setTariffId(defaultTariffId);
											orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
											orderDetailsMasterDto.setOrderEmergencyFlag('N');
											orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setPayeeId(selfPayeeId);
											orderDetailsMasterDto.setIsPackage(true);
											orderDetailsMasterDto.setPackage(true);
											orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
											orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
											orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
											orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
											//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
											orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
											orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setIsInvestigationOrder('N');
											orderDetailsMasterDto.setCoPayPer(masterDto.getCoPayPer());
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(masterDto.getOrderDetailsPayeeMapperDtosList());
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
									}
									else if(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId().intValue()==1 && !masterDto.isPackage())
									{
										orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
									    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
									    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
										orderDetailsMasterDto.setTariffId(defaultTariffId);
										orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
										orderDetailsMasterDto.setOrderEmergencyFlag('N');
										orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
										orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
										orderDetailsMasterDto.setServiceChargeable(1);
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setPayeeId(selfPayeeId);
										orderDetailsMasterDto.setIsPackage(false);
										orderDetailsMasterDto.setPackage(false);
										orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
										orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
										orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
										orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
										//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
										orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
										orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
										orderDetailsMasterDto.setOrdCancelled('N');
										orderDetailsMasterDto.setIsDrug('N');
										orderDetailsMasterDto.setServiceRendered(0);
										orderDetailsMasterDto.setStatus('A');
										orderDetailsMasterDto.setIsInvestigationOrder('N');
										orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
										List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
										OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
											detailsPayeeMapperDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
											detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
											detailsPayeeMapperDto.setServiceId(orderDetailsMasterDto.getServiceId());
										list.add(detailsPayeeMapperDto);	
										orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
										orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
										
									  }
									else{
										
										  ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
											consumptionDto.setOrganizationId(orderMasterDto2.getOrgId());
											consumptionDto.setUnitId(orderMasterDto2.getOrgUnitId());
											consumptionDto.setVisitTypeId(orderMasterDto2.getVisitTypeId());
											consumptionDto.setPatientTypeId(orderMasterDto2.getPatientTypeId());
											consumptionDto.setBillingBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
											consumptionDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
											consumptionDto.setServiceId(masterDto.getServiceId());
											consumptionDto.setCurrentDate(orderMasterDto2.getCurrentDate());
											consumptionDto.setContractId(orderMasterDto2.getContractId());
											consumptionDto.setEncounterId(orderMasterDto2.getEncounterId());
											consumptionDto.setAdmissionId(orderMasterDto2.getAdmissionId());
											consumptionDto.setDefaultSelfTariffId(defaultTariffId);
											consumptionDto.setSelfPayeeId(selfPayeeId);
											consumptionDto.setPatientId(orderMasterDto2.getPatientId());
											consumptionDto.setPatientClassId(orderMasterDto2.getPatientClassId());
										Response res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
											consumptionDto=(ContractConsumptionDto)res.getObject();
										
										if(res.getObject()!=null)
										{
											orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
											orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
											orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
											orderDetailsMasterDto.setTariffId(defaultTariffId);
											orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
											orderDetailsMasterDto.setOrderEmergencyFlag('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setPackage(masterDto.isPackage());
											orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
											orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
											orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
											orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
											orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
											orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
											orderDetailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
											orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
											orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
											orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
											orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()!=null?consumptionDto.getCreaditPayable():0.00));
											orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
											orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
											orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
											orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
											if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
												
										}
									 }
								
									if(orderDetailsMasterDto.getOrderDetailsId()!=null && orderDetailsMasterDto.getOrderDetailsId()>0) {								
										orderDetailsMasterDto.setIsInvestigationOrder('Y');
										if(masterDto.getOrderId()==null)
											iOrderMasterDao.updateOrderDetailsByOdrDtlsId(orderDetailsMasterDto.getOrderDetailsId());
										else {
											 orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
											iOrderDetailsMasterDao.updateOrderDetails(orderDetailsMasterDto,'N',selfPayeeId);
										}							
										
									}else{
										 orderDetailsList.add(orderDetailsMasterDto);
									}
									
									
									if(orderMasterDto2.getOrdDocSplId() == null) {
									   orderMasterDto2.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
									}
									   orderMasterDto2.setListOrderDetailsMasterDto(orderDetailsList);
									   
									   if(orderMasterDto.getSlotMasterDtoList()!=null && orderMasterDto.getSlotMasterDtoList().size()>0){
											orderMasterDto2.setSlotMasterDtoList(orderMasterDto.getSlotMasterDtoList());
										}
									 
							}
							  if(orderMasterDto2.getListOrderDetailsMasterDto()!=null && orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
							   {
									 orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto2);
									 labOrderId = (Integer) orderRes.getListObject().getFirst();
							   }
							
						}
					}
				}
			return new Response(SUCCESS, SUCCESS_CODE, "Order placed !!", null, selfPayeeId,orderRes,orderRes.getListObject2());
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);

	}
	
	/*@Override
	public Response getCurrentInvestigations(Integer encounter_id,boolean flag)throws ApplicationException {
		try {
			return iinvestigationDAO.getCurrentInvestigations(encounter_id,flag);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	@Override
	@Transactional
	public Response getCurrentInvestigations(Integer patient_id,boolean flag)throws ApplicationException {
		try {
			Response res= iinvestigationDAO.getCurrentInvestigations(patient_id,flag);
			List<InvestigationOrderDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			
		/*	for (InvestigationOrderDto investOrderDto : orderDetailsByEncounterIdDtosList) {
				// for package
				Response packageMpprRes = iOrderDetailsMasterDao
						.getOrderDetailsPackageMapperByOrderDetailsId(investOrderDto.getOrderDetailsId());
				List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
				  investOrderDto.setOrderDetailsPackageMapperDtosList(mpprList);
			}*/   // as per sidhu 
			
			return new Response(SUCCESS, SUCCESS_CODE, null, orderDetailsByEncounterIdDtosList, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCurrentInvestigationDetails(Integer panelId,Integer orderDetailsId)throws ApplicationException {
		try {
			return iinvestigationDAO.getCurrentInvestigationDetails(panelId,orderDetailsId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getInvestigations(String start_date,String end_date,Integer service_id)
			throws ApplicationException {
		
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = new SimpleDateFormat("dd/MM/yyyy").parse(start_date);
			endDate=new SimpleDateFormat("dd/MM/yyyy").parse(end_date); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}  
		
		return iinvestigationDAO.getInvestigations(startDate,endDate,service_id);	
	}


	@Override
	@Transactional
	public Response updateInvestigation(InvestigationDTO investigationDTO)
			throws ApplicationException {
		return iinvestigationDAO.updateInvestigation(investigationDTO);			
	}


	@Override
	@Transactional
	public Response deleteInvestigation(Integer investigation_id)
			throws ApplicationException {
		return iinvestigationDAO.deleteInvestigation(investigation_id);		
	}


	@Override
	public Response getServices(String serviceCode, String serviceName, Integer orgId) throws ApplicationException {
		return iinvestigationDAO.getServices(serviceCode, serviceName, orgId);
	}


	@Override
	public Response getServiceTypes() throws ApplicationException {
		return iinvestigationDAO.getServiceTypes();
	}


	@Override
	public Response getHistoryServices(Integer orgId)throws ApplicationException {
		return iinvestigationDAO.getHistoryServices(orgId);
	}


	@Override
	public Response getServicesByTest(Integer serviceId)
			throws ApplicationException {
		return iinvestigationDAO.getServicesByTest(serviceId);
	}

	@Override
	@Transactional
	public Response getPreviousInvestigation(int orgId, int orgUnitId, int patientId, int visitTypeId)
			throws ApplicationException {
		try {
			return iOrderMasterService.getPreviousOrdersByPatient(orgId, orgUnitId, patientId, visitTypeId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getPreviousInvestigationDetails(int orgId, int orgUnitId, int orderId) throws ApplicationException {
		try {
			return iOrderDetailsMasterDao.getPreviousOrderSerivice(orgId, orgUnitId, orderId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCurrentInvestigationsByPatientId(Integer patient_id)
			throws ApplicationException {
		try {
			return iinvestigationDAO.getCurrentInvestigationsByPatientId(patient_id);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getInvestigationsListForBookView(Integer patientId, Integer organizationId,String fromDate, String toDate)
			throws ApplicationException {
		try {
			return iinvestigationDAO.getInvestigationsListForBookView(patientId,organizationId,fromDate,toDate);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getInvestigationsListForCoversheet(int patient_id, boolean flag,Integer unitId)
			throws ApplicationException {
		try {
			Response res= iinvestigationDAO.getInvestigationsListForCoversheet(patient_id,flag,unitId);
			List<InvestigationOrderDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			
			
			List<Integer> orderdetailIds = orderDetailsByEncounterIdDtosList.stream().map(InvestigationOrderDto::getOrderDetailsId).collect(Collectors.toList());
			
			Response packageMpprRes = iOrderDetailsMasterDao
					.getOrderDetailsPackageMapperByOrderDetailsId(orderdetailIds);
			List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
			for (InvestigationOrderDto investOrderDto : orderDetailsByEncounterIdDtosList) {
			
				List<OrderDetailsPackageMapperDto> mpprListfillter = mpprList.stream()
		        .filter(order -> investOrderDto.getOrderDetailsId().equals(order.getOrderDetailsId()))
		        .collect(Collectors.toList());
				
				  investOrderDto.setOrderDetailsPackageMapperDtosList(mpprListfillter);
			}
			
			/*for (InvestigationOrderDto investOrderDto : orderDetailsByEncounterIdDtosList) {
				// for package
				Response packageMpprRes = iOrderDetailsMasterDao
						.getOrderDetailsPackageMapperByOrderDetailsId(investOrderDto.getOrderDetailsId());
				List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
				  investOrderDto.setOrderDetailsPackageMapperDtosList(mpprList);
			}*/
			
			return new Response(SUCCESS, SUCCESS_CODE, null, orderDetailsByEncounterIdDtosList, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getInvestigationsListForAnesthesia(int patient_id, boolean flag, Integer unitId) throws ApplicationException {
		try {
			Response res= iinvestigationDAO.getInvestigationsListForAnesthesia(patient_id,flag,unitId);
			List<InvestigationOrderDto> orderDetailsByEncounterIdDtosList = res.getListObject();

//			for (InvestigationOrderDto investOrderDto : orderDetailsByEncounterIdDtosList) {
//				// for package
//				Response packageMpprRes = iOrderDetailsMasterDao
//						.getOrderDetailsPackageMapperByOrderDetailsId(investOrderDto.getOrderDetailsId());
//				List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
//				investOrderDto.setOrderDetailsPackageMapperDtosList(mpprList);
//			}

			return new Response(SUCCESS, SUCCESS_CODE, null, orderDetailsByEncounterIdDtosList, null);
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getCurrentInvestigationsByProcedureIdAndVisitId(Integer visitId, Integer procedureId ,boolean flag)
			throws ApplicationException {
		try {
			Response res= iinvestigationDAO.getCurrentInvestigationsByProcedureIdAndVisitId(visitId,procedureId,flag);
			List<InvestigationOrderDto> orderDetailsByEncounterIdDtosList = res.getListObject();
			
			for (InvestigationOrderDto investOrderDto : orderDetailsByEncounterIdDtosList) {
				// for package
				Response packageMpprRes = iOrderDetailsMasterDao
						.getOrderDetailsPackageMapperByOrderDetailsId(investOrderDto.getOrderDetailsId());
				List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
				  investOrderDto.setOrderDetailsPackageMapperDtosList(mpprList);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, orderDetailsByEncounterIdDtosList, null);
		}catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getInvestigationsByPatientDetails(InvestigationOrderDto investigationOrderDto)
			throws ApplicationException {
		try {
			return iinvestigationDAO.getInvestigationsByPatientDetails(investigationOrderDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response updateDiagnosticsRemarks(InvestigationOrderDto investigationOrderDto) throws ApplicationException {
		try {
			return iinvestigationDAO.updateDiagnosticsRemarks(investigationOrderDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response cancelLabOrderForNewEMR(InvestigationOrderDto investigationOrderDto) throws ApplicationException {
		try {
			return iinvestigationDAO.cancelLabOrderForNewEMR(investigationOrderDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
			return iFreeTextInvestigationsDAO.saveFreeTextInvestigations(freeTextOrderedServicesDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
			return iFreeTextInvestigationsDAO.getFreeTextInvestigations(freeTextOrderedServicesDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
			return iFreeTextInvestigationsDAO.searchFreeTextInvestigations(freeTextOrderedServicesDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response cancelFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
			return iFreeTextInvestigationsDAO.cancelFreeTextInvestigations(freeTextOrderedServicesDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveInvestigationForAppointmentTemplate(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
				Integer selfPayeeId =null ;
				Integer defaultTariffId = null;
				Date date  = TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId());
				Response orderRes = null;
				Character isPreBilling = null;
				Integer defaultDiscountOwnership = 0;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
					unitApplicationConfigurationDto.setOrganizationId(orderMasterDto.getOrgId());
					unitApplicationConfigurationDto.setUnitId(orderMasterDto.getOrgUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				
				if(appConfigRes.getObject()!=null)
				{
					UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
					
					if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
					else
						defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
						
					if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
					else
						selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
					
					if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
					else	
						isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
						
					defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
				}else{
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Unit Application Configuration is yet to set !!", null, null);
				}
		   
				ListIterator<OrderMasterDto> iteratorOrderMaster = orderMasterDto.getListOrderMasterDto().listIterator();
				
				while(iteratorOrderMaster.hasNext())	
				{
					OrderMasterDto orderMasterDto2 = iteratorOrderMaster.next();
					orderMasterDto2 = iContractConsumptionService.getPatientClassByVisit(orderMasterDto2);
					orderMasterDto2.setDefaultDiscountOwnership(defaultDiscountOwnership);
					if(orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
					{
						if(orderMasterDto2.getEncounterId()==null && orderMasterDto2.getAdmissionId()==null)
						{    
							OrderMasterDto orderMasterDtoFutureOrder =  orderMasterDto2;
							ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
							List<OrderDetailsMasterDto> orderDetailsListFuture = new ArrayList<OrderDetailsMasterDto>();
							while(iterator.hasNext())
							{
								OrderDetailsMasterDto detailsMasterDto = iterator.next();
								OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
										orderDetailsMasterDto.setServiceId(detailsMasterDto.getServiceId());
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
										orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
										orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
										orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
										orderDetailsMasterDto.setStatus('A');
										orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
										orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
										orderDetailsMasterDto.setOrderQty(1);
										orderDetailsMasterDto.setOrdDocId(detailsMasterDto.getOrdDocId());
										orderDetailsMasterDto.setOrdDocSplId(detailsMasterDto.getOrdDocSplId());
										orderDetailsMasterDto.setServiceRenderingDeptId(detailsMasterDto.getServiceRenderingDeptId());
										orderDetailsMasterDto.setPriorityId(detailsMasterDto.getServicePriorityId());
										orderDetailsMasterDto.setIsOutsourced('N');
										orderDetailsMasterDto.setServiceRendered(0);
										orderDetailsMasterDto.setServiceChargeable(1);
										orderDetailsMasterDto.setOrdCancelled('N');
										orderDetailsMasterDto.setIsDrug('N');
										orderDetailsMasterDto.setIsInvestigationOrder('Y');
										orderDetailsMasterDto.setIsRefDoctorShare(detailsMasterDto.getIsRefDoctorShare());
										orderDetailsMasterDto.setDepartmentId(detailsMasterDto.getDepartmentId());
										orderDetailsMasterDto.setSubDepartmentId(detailsMasterDto.getSubDepartmentId());
										orderDetailsMasterDto.setOrderSetId(detailsMasterDto.getOrderSetId());
										orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
										orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
										orderDetailsMasterDto.setProcedureOrderMstId((detailsMasterDto.getProcedureOrderMstId()!=null && detailsMasterDto.getProcedureOrderMstId()!=0)?detailsMasterDto.getProcedureOrderMstId():null);
										orderDetailsMasterDto.setBedId(detailsMasterDto.getBedId());
										orderDetailsMasterDto.setWardId(detailsMasterDto.getWardId());
										orderDetailsMasterDto.setRoomId(detailsMasterDto.getRoomId());
										orderDetailsMasterDto.setOrdRemarks(detailsMasterDto.getOrdRemarks());
										if(orderDetailsMasterDto.getOrderSchDate()!=null) {
											orderDetailsMasterDto.setOrderSchDate(detailsMasterDto.getOrderSchDate());
										}
										else {
											orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(orderMasterDto.getOrgUnitId()).getTime());
										}
							
										orderDetailsListFuture.add(orderDetailsMasterDto);
								}
							orderMasterDtoFutureOrder.setListOrderDetailsMasterDto(orderDetailsListFuture);
							orderRes = iOrderMasterService.saveOrderMaster(orderMasterDtoFutureOrder);
						}
					}
				}
			return new Response(SUCCESS, null, "Order placed !!", null, selfPayeeId,orderRes,orderRes.getListObject2());
		} catch (Exception e) {
			
			throw e;
		}
	
	}

	@Override
	public Response getInvestigationForIPDCoversheetByDate(InvestigationOrderDto dto) throws ApplicationException {
		try{
			List<InvestigationOrderDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<InvestigationOrderDto> complaintsList = new ArrayList<>();
				InvestigationOrderDto resDto = new InvestigationOrderDto();

				resDto.setAddedDate(date);
				if(dto.getCurrentEncounterOrderLists() != null && dto.getCurrentEncounterOrderLists().size()>0){
					for (InvestigationOrderDto investigation : dto.getCurrentEncounterOrderLists()) {

						if (date.equals(investigation.getCreatedDateForFilter())) {
							investigation.setAddedDate(date);
							complaintsList.add(investigation);
						}
					}
					resDto.setCurrentEncounterOrderLists(complaintsList);
				}
				resDtoList.add(resDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){

		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(InvestigationOrderDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			InvestigationOrderDto resDto = dto;

			if(dto.getCurrentEncounterOrderLists() != null && dto.getCurrentEncounterOrderLists().size()>0){
				for (InvestigationOrderDto complaint : dto.getCurrentEncounterOrderLists()) {

					if (!distinctDates.contains(complaint.getCreatedDateForFilter())) {
						distinctDates.add(complaint.getCreatedDateForFilter());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getInvestigationForIPDCoversheetByDate(resDto);
				}

			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
