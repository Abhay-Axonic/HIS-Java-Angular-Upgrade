package com.param.global.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.transaction.Transactional;

import com.param.global.common.*;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.GetDoctorShareDto;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.INeuronPlusOrderDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.dto.global.SlotCheckDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.OrderMaster;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dao.IProcedureOrderComponentDetailsDao;
import com.param.global.otims.dto.ProcedureOrderComponentDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class OrderMasterServiceImpl implements IOrderMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(OrderMasterServiceImpl.class);

	public static final DecimalFormat df2 = new DecimalFormat("#.##");
	
	@Autowired
	private IOrderMasterDao iOrderMasterDao; 
	@Autowired
	private IOrderDetailsMasterService iOrderDetailsMasterService; 
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	private IProcedureOrderComponentDetailsDao iProcedureOrderComponentDetailsDao;
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private INeuronPlusOrderDao iNeuronPlusOrderDao; 
	
	@Autowired 
	IpdChargesDao ipdChargesDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private IMPackageMasterService iMPackageMasterService;

	@Autowired
	private BillingCommonMethods billingCommonMethods;
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response saveOrderMasterWithDiscount(OrderMasterDto orderMasterDto,Integer reqDisId) throws ApplicationException {
		try {
			List<Integer> orderDetailsIdsList = new LinkedList<>();
			Map<Integer,Integer> packageOrders =  new LinkedHashMap<>();

			Integer defaultDiscountOwnership = iUnitApplicationBillingConfigurationDao.getDefaultDiscountOwnership(orderMasterDto.getOrgUnitId());

			Integer orderId = billingCommonMethods.saveOrderMaster(orderMasterDto,getOrderNumber(orderMasterDto.getOrgUnitId()));
			Integer admissionId = orderMasterDto.getAdmissionId();

				List<OrderDetailsMasterDto> listForSaveCall = new LinkedList<>();
				
				for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) {
					orderDetailsMasterDto.setOrderId(orderId);
					orderDetailsMasterDto.setAdmissionId(admissionId);
					if (orderDetailsMasterDto.getFutureService()){
						orderMasterDto.setIsFutureService(orderDetailsMasterDto.getFutureService());
						orderMasterDto.setAdmissionId(null);
						orderMasterDto.setEncounterId(null);
						orderDetailsMasterDto.setOrderId(billingCommonMethods.saveOrderMaster(orderMasterDto, getOrderNumber(orderMasterDto.getOrgUnitId())));
						orderMasterDto.setIsFutureService(false);
					}
					orderDetailsMasterDto.setOrderQty(orderDetailsMasterDto.getOrderQty() != null ? orderDetailsMasterDto.getOrderQty() : 1);
					orderDetailsMasterDto.setStatus((orderDetailsMasterDto.getStatus() != null) ? orderDetailsMasterDto.getStatus() : 'A');
					orderDetailsMasterDto.setIsAppointableRadiology(orderDetailsMasterDto.getIsAppointableRadiology() != null ? orderDetailsMasterDto.getIsAppointableRadiology() : 'N');

					//<----Referal & Doctor Share logic
					Double totalServiceAmt = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
					Double serviceAmt = totalServiceAmt - orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue();
					orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership() != null ? orderDetailsMasterDto.getDiscountOwnership() : defaultDiscountOwnership);

					if (serviceAmt > 0 && orderDetailsMasterDto.getOrdDocId() != null && orderDetailsMasterDto.getOrdDocId() > 0 && orderDetailsMasterDto.getIsDocReq() != null && orderDetailsMasterDto.getIsDocReq().equals('Y'))
						orderDetailsMasterDto.setDocSharePer(BigDecimal.valueOf(getDoctorSharePercentage(orderDetailsMasterDto, orderMasterDto.getVisitTypeId(), orderMasterDto.getBillingBedCategoryId(), orderMasterDto.getPatientTypeId(), orderMasterDto.getPaymentEntitlementTypeId())));
					else
						orderDetailsMasterDto.setDocSharePer(null);

					if (serviceAmt > 0 && orderMasterDto.getReferralId() != null && orderDetailsMasterDto.getIsRefDoctorShare() != null && orderDetailsMasterDto.getIsRefDoctorShare().equals('Y'))
						orderDetailsMasterDto.setRefDocSharePer(BigDecimal.valueOf(getDoctorReferralPercentage(orderDetailsMasterDto, orderMasterDto.getReferralId(), orderMasterDto.getVisitTypeId(), orderMasterDto.getBillingBedCategoryId(), orderMasterDto.getPatientTypeId(), orderMasterDto.getPaymentEntitlementTypeId())));
					else
						orderDetailsMasterDto.setRefDocSharePer(null);

					if (serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer() != null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && (orderDetailsMasterDto.getDiscountOwnership() != null && orderDetailsMasterDto.getDiscountOwnership() != 0) && (orderDetailsMasterDto.getOrdDiscount() != null || orderDetailsMasterDto.getOrdConcession() != null)) {
						if (orderDetailsMasterDto.getDiscountOwnership() == 1) //Discount owned by doctor
						{
							orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
						}
					}

					if (serviceAmt > 0 && orderDetailsMasterDto.getRefDocSharePer() != null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0 && (orderDetailsMasterDto.getDiscountOwnership() != null && orderDetailsMasterDto.getDiscountOwnership() != 0) && (orderDetailsMasterDto.getOrdDiscount() != null || orderDetailsMasterDto.getOrdConcession() != null)) {
						if (orderDetailsMasterDto.getDiscountOwnership() == 3) //Discount owned by referral
						{
							orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
						}
					}

					if (serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer() != null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer() != null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0) {
						Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() + orderDetailsMasterDto.getRefDocSharePer().doubleValue();

						if (orderDetailsMasterDto.getDocSharePer().doubleValue() == serviceAmt) {
							orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						} else if (totalShare > serviceAmt) {
							orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt - orderDetailsMasterDto.getDocSharePer().doubleValue()));
						}

					}
					orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt - ((orderDetailsMasterDto.getRefDocSharePer() != null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00) + (orderDetailsMasterDto.getDocSharePer() != null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));

					if ((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId() != null) && totalServiceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue())&& orderDetailsMasterDto.getPackageId() == null)

						if(totalServiceAmt == 0) {
							
							if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList() != null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0) {
							
								Boolean checkFlag = false;
								for (OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()) {
									if(dto.getIsPackageService() != null && dto.getIsPackageService() == 'Y') {
										checkFlag = true;
									}
								}
								
								if(checkFlag) {
									orderDetailsMasterDto.setIsPackageService('Y');
								}else {
									orderDetailsMasterDto.setIsPackageService('N');
								}
							
							}else {
								orderDetailsMasterDto.setIsPackageService('N');
							}
							
						}else {
							orderDetailsMasterDto.setIsPackageService('Y');
						}
					else{
						if(totalServiceAmt > 0 && !totalServiceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) ) {
							orderDetailsMasterDto.setIsPackageService('N');
						}else {
							orderDetailsMasterDto.setIsPackageService(orderDetailsMasterDto.getIsPackageService());
						}
					}
					

					listForSaveCall.add(orderDetailsMasterDto);
					//Referal & Doctor Share logic---->
					//Response orderDetails=iOrderDetailsMasterService.saveOrderDetailsMaster(orderDetailsMasterDto);
				}	
				Response orderDtailsSaveCall = iOrderDetailsMasterDao.saveOrderDetailsMasterBetterPerformannce(listForSaveCall);
				listForSaveCall = orderDtailsSaveCall.getListObject();
				for(OrderDetailsMasterDto orderDetailsMasterDto : listForSaveCall) {
					Integer orderDetailsId = orderDetailsMasterDto.getOrderDetailsId();
					orderDetailsMasterDto.setOrderDetailsId(orderDetailsId);
					//iOrderDetailsMasterService.updateOrderDetailsForAccessionNumber(orderDetailsId);
					orderDetailsIdsList.add(orderDetailsId);
					if(orderDetailsMasterDto.getFreeTextOrderedServicesId() != null) {
						iOrderDetailsMasterDao.updateFreeOrderService(orderDetailsMasterDto);	
					}
					
					if(orderDetailsMasterDto.isPackage() && orderDetailsMasterDto.getPackageId() != null ){
						packageOrders.put(orderDetailsMasterDto.getPackageId(), orderDetailsId);
					}



					//save procedure order components
					if(orderDetailsMasterDto.getIsProcedure() != '\u0000' && orderDetailsMasterDto.getIsProcedure() == 'Y'){
						Map<Integer,List<ProcedureOrderComponentDetailsDto>> procedureOrderComponents = orderDetailsMasterDto.getProcedureOrderComponents();
						if(procedureOrderComponents != null && !procedureOrderComponents.isEmpty()){
							List<ProcedureOrderComponentDetailsDto> listProOrderComp = procedureOrderComponents.get(orderDetailsMasterDto.getProcedureOrderMstId());
							if(listProOrderComp != null && !listProOrderComp.isEmpty()){
								/**ListIterator<ProcedureOrderComponentDetailsDto> proOrdCompItr = listProOrderComp.listIterator();
								while(proOrdCompItr.hasNext()){
									ProcedureOrderComponentDetailsDto proOrderComp = proOrdCompItr.next();
									proOrderComp.setOrderDetailId(orderDetailsId);
									iProcedureOrderComponentDetailsDao.saveProcedureOrderComponentDetails(proOrderComp);
								}*/
								listProOrderComp.forEach(proOrderComp -> {
									proOrderComp.setOrderDetailId(orderDetailsId);
									iProcedureOrderComponentDetailsDao.saveProcedureOrderComponentDetails(proOrderComp);
								});
							}
						}
					}
					
					if((orderMasterDto.getLineLevelDiscountOrderList()!=null && orderMasterDto.getLineLevelDiscountOrderList().isEmpty())&&(reqDisId!=null && orderDetailsMasterDto.getReqDisAmt()!=null && orderDetailsMasterDto.getIsDiscountApplicable() == 'Y'))
					{
						
						if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && !orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().isEmpty())
						{
							for (OrderDetailsPackageMapperDto mpprDto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()) 
							{
								if(mpprDto.getIsPackageService()=='N')
								{
									RequestDiscountDetailsDto requestDiscountDetailsDto = new RequestDiscountDetailsDto();
										requestDiscountDetailsDto.setReqDiscId(reqDisId);
										requestDiscountDetailsDto.setOrderId(orderId);
										requestDiscountDetailsDto.setOrderDetailsId(orderDetailsId);
										requestDiscountDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
										
										if(orderDetailsMasterDto.getPackageConsumptionAmt()!=null && orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()>0)
											requestDiscountDetailsDto.setNetAmount(orderDetailsMasterDto.getServiceAmt());
										else
											requestDiscountDetailsDto.setNetAmount(orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdConcession()));
										
										requestDiscountDetailsDto.setReqDisAmt(orderDetailsMasterDto.getReqDisAmt());
										requestDiscountDetailsDto.setApprovedDiscAmt(new BigDecimal("0.00"));
										requestDiscountDetailsDto.setCreatedBy(orderMasterDto.getCreatedBy());
										requestDiscountDetailsDto.setUpdatedBy(orderMasterDto.getCreatedBy());
										requestDiscountDetailsDto.setCreatedDate(orderMasterDto.getCurrentDate());
										requestDiscountDetailsDto.setUpdatedDate(orderMasterDto.getCurrentDate());
										requestDiscountDetailsDto.setOrganizationId(orderMasterDto.getOrgId());
										requestDiscountDetailsDto.setUnitId(orderMasterDto.getOrgUnitId());
								
										iOrderDetailsMasterDao.requestDiscountDetails(requestDiscountDetailsDto);
								}
							}
						}
						
					}
					
					if(orderMasterDto.getLineLevelDiscountOrderList()!=null && orderMasterDto.getLineLevelDiscountOrderList().size()>0)
					{
						for(OrderDetailsMasterDto dto :orderMasterDto.getLineLevelDiscountOrderList())
						{
							if(dto.getOrderDetailsId() == null && dto.getServiceId().equals(orderDetailsMasterDto.getServiceId())&&dto.getOrdTotalAmt().equals(orderDetailsMasterDto.getOrdTotalAmt()) )
							{
								dto.setOrderDetailsId(orderDetailsId);
							}
						}
					}

					//modality scheduling for OP start
					if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getVisitTypeId()==OP && orderMasterDto.getSlotMasterDtoList()!=null && !orderMasterDto.getSlotMasterDtoList().isEmpty())
					{
						for (SlotMasterDto slotMasterDto : orderMasterDto.getSlotMasterDtoList()) {
							boolean res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
							if (!res) {
//								return new Response(ERROR, COMMON_ERROR_CODE, "Slot is already booked !!", null, null);
								//System.out.println("Slot is already booked");
							} else {
								if (slotMasterDto.getSpecialityId() != null && orderDetailsMasterDto.getDepartmentId() != null && slotMasterDto.getSubSpecialityId() != null && orderDetailsMasterDto.getSubDepartmentId() != null && slotMasterDto.getSpecialityId() == orderDetailsMasterDto.getDepartmentId() && slotMasterDto.getSubSpecialityId() == orderDetailsMasterDto.getSubDepartmentId()) {
									slotMasterDto.setOrderDetailsId(orderDetailsId);
									iAppointmentSchedulingDao.updateOrderDetailsIsAppointableServiceStatus(slotMasterDto.getOrderDetailsId(), slotMasterDto.getUpdatedBy());
									iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);

								}

							}
						}// while
					}//if
					
				}
				
		//	}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, orderDetailsIdsList,packageOrders, orderMasterDto.getLineLevelDiscountOrderList());
		}catch (Exception e) {
			logger.error("saveOrderMasterWithDiscount failed", e);
			throw new ServiceException(e.getMessage());
		}
//		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveOrderMaster(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
			Integer orderDetailsIdForAccession = null;
			List<Integer> orderDetailsIds = new LinkedList<Integer>();
			List<Integer> orderIdLs = new ArrayList<>();
			Integer defaultDeiscountOwnership = orderMasterDto.getDefaultDiscountOwnership()!=null ? orderMasterDto.getDefaultDiscountOwnership() : iUnitApplicationBillingConfigurationDao.getDefaultDiscountOwnership(orderMasterDto.getOrgUnitId());
			Map<Integer, Integer> packageMap = new HashMap<Integer, Integer>();
			Integer orderId = 0;
			
			if(orderMasterDto.getOrderId()!=null && orderMasterDto.getOrderId() >0)
				orderId = orderMasterDto.getOrderId();
			else{
				orderMasterDto.setOrderNo(getOrderNumber(orderMasterDto.getOrgUnitId()));
				Response orderMasterResponse = iOrderMasterDao.saveOrderMaster(orderMasterDto);
				orderId = (Integer) orderMasterResponse.getObject();
			}
				
			if(orderId > 0 && orderMasterDto.getListOrderDetailsMasterDto() != null) 
			{
				for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) 
				{
					orderDetailsMasterDto.setOrderId(orderId);
					if(orderDetailsMasterDto.getStatus()!=null) {
						orderDetailsMasterDto.setStatus(orderDetailsMasterDto.getStatus());
					}else {
					  orderDetailsMasterDto.setStatus('A');
					}
					
					if(orderDetailsMasterDto.getIsOrderVisibleDepartment()!=null) {
                        orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
                    }
					
					orderDetailsMasterDto.setIsAppointableRadiology('N');
					
					//<----Referal & Doctor Share logic
					Double serviceAmt = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
					orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()!=null ? orderDetailsMasterDto.getDiscountOwnership() : defaultDeiscountOwnership !=null ? defaultDeiscountOwnership : 0);
					
					if(serviceAmt > 0 && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId() > 0 && orderDetailsMasterDto.getIsDocReq()!=null && orderDetailsMasterDto.getIsDocReq().equals('Y'))
						orderDetailsMasterDto.setDocSharePer(new BigDecimal(getDoctorSharePercentage(orderDetailsMasterDto,orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
					else
						orderDetailsMasterDto.setDocSharePer(null);
					
					if(serviceAmt > 0 && orderMasterDto.getReferralId()!=null && orderDetailsMasterDto.getIsRefDoctorShare()!=null && orderDetailsMasterDto.getIsRefDoctorShare().equals('Y'))
						orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(getDoctorReferralPercentage(orderDetailsMasterDto,orderMasterDto.getReferralId(),orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
					else
						orderDetailsMasterDto.setRefDocSharePer(null);
					
					if(serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
					{
						if(orderDetailsMasterDto.getDiscountOwnership()==1) //Discount owned by doctor
						{
							orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
						}
					}
					if(serviceAmt > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
					{
						if(orderDetailsMasterDto.getDiscountOwnership()==3) //Discount owned by referral
						{
							orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
						}
					}
					
					if(serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
					{
						Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
						
						if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmt)
						{
							orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						}else if(totalShare > serviceAmt)
						{
							orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt-orderDetailsMasterDto.getDocSharePer().doubleValue()));
						}
						
					}
					orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
					//Referal & Doctor Share logic---->
					orderDetailsMasterDto.setOrdRemarks(orderDetailsMasterDto.getOrdRemarks());
					
					if((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId()!=null) && serviceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) && orderDetailsMasterDto.getPackageId() == null)
						{

						if(serviceAmt == 0) {
							
							if(orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList() != null && orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0) {
								
							
								Boolean checkFlag = false;
								for (OrderDetailsPackageMapperDto dto : orderDetailsMasterDto.getOrderDetailsPackageMapperDtosList()) {
									if(dto.getIsPackageService() != null && dto.getIsPackageService() == 'Y') {
										checkFlag = true;
									}
								}
								
								if(checkFlag) {
									orderDetailsMasterDto.setIsPackageService('Y');
								}else {
									orderDetailsMasterDto.setIsPackageService('N');
								}
							
							}else {
								orderDetailsMasterDto.setIsPackageService('N');
							}
							
							
						}else {
							orderDetailsMasterDto.setIsPackageService('Y');
						}
						
					
						}
					else {
						if(serviceAmt > 0 && !serviceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) ) {
							orderDetailsMasterDto.setIsPackageService('N');
						}else {
							orderDetailsMasterDto.setIsPackageService(orderDetailsMasterDto.getIsPackageService());
						}
					}
						
					
					Response orderDetails =  iOrderDetailsMasterService.saveOrderDetailsMaster(orderDetailsMasterDto);
					orderDetailsIdForAccession = (Integer) orderDetails.getObject();
					//iOrderDetailsMasterService.updateOrderDetailsForAccessionNumber(orderDetailsIdForAccession);
					if(orderDetailsMasterDto.getNetAmount()!=null && orderDetailsMasterDto.getNetAmount().compareTo(BigDecimal.ZERO)==1)
					{
						orderDetailsIds.add(orderDetailsIdForAccession);
					}
					if(orderDetailsMasterDto.getPackageId()!=null && orderDetailsMasterDto.getPackageId()>0 && orderDetailsMasterDto.isPackage())
					{
						Integer orderDetailsId = (Integer) orderDetails.getObject();
						packageMap.put(orderDetailsMasterDto.getPackageId(),orderDetailsId);
					}
					//modality scheduling for OP start
					if(orderMasterDto.getEncounterId()!=null && orderMasterDto.getVisitTypeId()==OP && orderMasterDto.getSlotMasterDtoList()!=null && orderMasterDto.getSlotMasterDtoList().size()>0)
					{						
					
					ListIterator<SlotMasterDto> itr= orderMasterDto.getSlotMasterDtoList().listIterator();
						while(itr.hasNext()){						
							SlotMasterDto slotMasterDto=itr.next();
							boolean res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
							if(res==false){
//								return new Response(ERROR, COMMON_ERROR_CODE, "Slot is already booked !!", null, null);
								//System.out.println("Slot is already booked");
							}else{
									if(slotMasterDto.getSpecialityId()!=null && orderDetailsMasterDto.getDepartmentId()!=null && slotMasterDto.getSubSpecialityId()!=null &&orderDetailsMasterDto.getSubDepartmentId()!=null && slotMasterDto.getSpecialityId()==orderDetailsMasterDto.getDepartmentId() && slotMasterDto.getSubSpecialityId()==orderDetailsMasterDto.getSubDepartmentId()){
										slotMasterDto.setOrderDetailsId((Integer)orderDetails.getObject());
										iAppointmentSchedulingDao.updateOrderDetailsIsAppointableServiceStatus(slotMasterDto.getOrderDetailsId(),slotMasterDto.getUpdatedBy());
									     iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
									
								}
				
							}
						}// while
					}//if
				//modality scheduling for OP end
				}
				orderIdLs.add(orderId);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, orderIdLs, packageMap,orderDetailsIdForAccession,orderDetailsIds);
		}catch (Exception e) {
			logger.error("saveOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	public String getOrderNumber(Integer unitId) {
		try {
			/*Long incrementedId = iOrderMasterDao.getNextValOrderMasterId();
			String billPrefix="ORD-18-";
			
			StringBuilder bill=new StringBuilder(billPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				bill = length == 1 ? (bill.append("000000").append(incrementedId))
						: length == 2 ? (bill.append("00000").append(incrementedId))
								: length == 3 ? (bill.append("0000").append(incrementedId))
										: length == 4 ? (bill.append("000").append(incrementedId))
												: length == 5 ? (bill.append("00").append(incrementedId))
														: length == 6 ? (bill.append("0").append(incrementedId))
																: length == 7 ? (bill.append(incrementedId)) : bill.append("");*/
				return iGlobalUnitMasterDao.getGeneratedNo(0, unitId, IBillingCommonConstants.ORDER_NUMBER);
			} catch (Exception e) {
				logger.error("getOrderNumber failed", e);
				throw new ServiceException(e.getMessage());
			}

  }


	@Override
	@Transactional
	public Response creditNoteCancelSerivces(OrderMasterDto orderMasterDto ) throws ApplicationException 
	{
		try 
		{
		 		OrderMaster orderMaster =  (OrderMaster) sessionFactory.getCurrentSession().get(OrderMaster.class, orderMasterDto.getOrderId());
		 		
		 		OrderMasterDto newOrderMstDto = new OrderMasterDto();
		 		
		 		
		 		newOrderMstDto.setOrgId(orderMasterDto.getOrgId());
		 		newOrderMstDto.setOrgUnitId(orderMasterDto.getOrgUnitId());
		 		newOrderMstDto.setOrderNo(getOrderNumber(orderMasterDto.getOrgUnitId()));
		 		newOrderMstDto.setAdmissionId(orderMaster.getAdmissionId());
		 		newOrderMstDto.setOrderDate(orderMasterDto.getOrderDate());
		 		newOrderMstDto.setVisitTypeId(orderMaster.getVisitTypeId());
		 		newOrderMstDto.setEncounterId(orderMaster.getEncounterId());
		 		newOrderMstDto.setPatientId(orderMaster.getPatientId());
		 		newOrderMstDto.setOrdDoctorId(orderMaster.getDoctorId());
		 		newOrderMstDto.setPriorityId(orderMaster.getPriorityId());
		 		newOrderMstDto.setDrCrId(orderMaster.getDrCrId());
		 		newOrderMstDto.setOrdRemarks(orderMaster.getOrdRemarks());
		 		newOrderMstDto.setCreatedBy(orderMasterDto.getCreatedBy());
		 		newOrderMstDto.setCreatedDate(orderMasterDto.getCreatedDate());
		 		newOrderMstDto.setUpdatedBy(orderMasterDto.getUpdatedBy());
		 		newOrderMstDto.setUpdatedDate(orderMasterDto.getCreatedDate());
		 		newOrderMstDto.setOrderStatusId(orderMaster.getOrderStatusId());
		 		newOrderMstDto.setOrderStatus(orderMaster.getOrderStatus());
		 		return iOrderMasterDao.saveOrderMaster(newOrderMstDto);
		}catch (Exception e)
		{
			logger.error("creditNoteCancelSerivces failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getPreviousOrdersByPatient(int orgId, int orgUnitId, int patientId, int visitTypeId)
			throws ApplicationException {
		try {	
		
			List<OrderMasterDto> listOrderMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORDERED_PACKAGE_SERVICES_FOR_CANCELLATION_BY_PACKAGE_ENCOUNTER_ID")
				.setInteger("orgId", orgId)
				.setInteger("orgUnitId", orgUnitId)
				.setInteger("patientId", patientId )
				.setInteger("visitTypeId", visitTypeId )
				.setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOrderMasterDto, null);
		}catch(Exception e) {
			
			throw e;
		}
	}
	@Override
	public Double getDoctorSharePercentage(OrderDetailsMasterDto orderDetailsMasterDto,Integer visitTypeId,Integer billingBedCategoryId,Integer patientTypeId,Integer paymentEntitlementId)
	{
		try{
			Double shareAmt =0.00;
			Double amount = 0.00;
			orderDetailsMasterDto.setOrderQty(orderDetailsMasterDto.getOrderQty() != null ? orderDetailsMasterDto.getOrderQty() : 1);
			
			if(orderDetailsMasterDto.getDiscountOwnership()==0) //Discount ownership taken by both doctor and hospital
			{
				if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
					amount = (orderDetailsMasterDto.getOrdTotalAmt()).doubleValue();	
				}else{
					amount = (orderDetailsMasterDto.getOrdTotalAmt().subtract((orderDetailsMasterDto.getOrdDiscount()).add(new BigDecimal(orderDetailsMasterDto.getCrNetAmount())).add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
				}
			}
			
			if(orderDetailsMasterDto.getDiscountOwnership()!=0){
				if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
					amount = (orderDetailsMasterDto.getOrdTotalAmt().add(orderDetailsMasterDto.getOrdDiscount())).doubleValue();	
				}else{
					amount = (orderDetailsMasterDto.getOrdTotalAmt()).doubleValue();	
				}
			}		
			
			List<Long> dayCountList = new LinkedList<>();
			GetDoctorShareDto lastObj = new GetDoctorShareDto();
			List<GetDoctorShareDto> docShareType1 = new LinkedList();
			List<GetDoctorShareDto> docShareType2 = new LinkedList();
			List<GetDoctorShareDto> docShareType3 = new LinkedList();
			List<GetDoctorShareDto> docShareType4 = new LinkedList();
			List<GetDoctorShareDto> docShareType5 = new LinkedList();
			Response res = iOrderMasterDao.getDoctorSharePercentage(orderDetailsMasterDto,visitTypeId,billingBedCategoryId,patientTypeId,paymentEntitlementId);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
				ListIterator<GetDoctorShareDto> iterator = (ListIterator<GetDoctorShareDto>) res.getListObject().listIterator();
				while(iterator.hasNext())
				{
					GetDoctorShareDto getDoctorShareDto = iterator.next();
					
					if(getDoctorShareDto.getDocShareType()==1)
						docShareType1.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==2)
						docShareType2.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==3)
						docShareType3.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==4)
						docShareType4.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==5)
						docShareType5.add(getDoctorShareDto);
				}
				
				if(docShareType1.size()>0)
				{
					if(docShareType1.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType1)
						{
							if(dto.getDocShare()!=null && dto.getDocShare()>0) 
							{
								shareAmt = amount * (dto.getDocShare()/100);
								
							}else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0){
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
									if(orderDetailsMasterDto.getOrdDiscount().doubleValue() == amount){
										shareAmt = dto.getDocShareAmount() ; 
									}else{
										shareAmt = dto.getDocShareAmount() ; 
									}
								}else{
									shareAmt = dto.getDocShareAmount() ; 

								}
							}
							else
								shareAmt = 0.00;
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType1)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType1.get(minIndex);
							
							
							 if(lastObj.getDocShare()!=null && lastObj.getDocShare()>0){
									shareAmt = amount * (lastObj.getDocShare()/100);
								}
							 else if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0){
								 lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								 
								if(orderDetailsMasterDto.getOrdDiscount().doubleValue() == amount){
									
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; /*orderDetailsMasterDto.getOrdDiscount().doubleValue() > lastObj.getDocShareAmount()  ? (lastObj.getDocShareAmount() - (orderDetailsMasterDto.getOrdDiscount().doubleValue() - lastObj.getDocShareAmount())) : lastObj.getDocShareAmount(); */
									}else{
										shareAmt = lastObj.getDocShareAmount() ; 
									}
									
								}else{
									shareAmt = lastObj.getDocShareAmount() ; 
								}
								
							}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType2.size()>0)
				{
					if(docShareType2.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType2)
						{
							
							
							if(dto.getDocShare()!=null && dto.getDocShare()>0){
								shareAmt = amount * (dto.getDocShare()/100);
							}
							else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0)
							{
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
									shareAmt = lastObj.getDocShareAmount() ; 

								}else{
									shareAmt = dto.getDocShareAmount() ; 
								}
							} 
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType2)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType2.get(minIndex);
							
							if(lastObj.getDocShare()!=null && lastObj.getDocShare()>0){
								shareAmt = amount * (lastObj.getDocShare()/100);
							}
							else if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0)
								{
								lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; 
	
									}else{
										shareAmt = lastObj.getDocShareAmount() ; 
									}
								}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType3.size()>0)
				{
					if(docShareType3.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType3)
						{
							 if(dto.getDocShare()!=null && dto.getDocShare()>0){
								shareAmt = amount * (dto.getDocShare()/100);
							 }
							 else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0)
								{
								 	dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; 
	
									}else{
										shareAmt = dto.getDocShareAmount() ; 
									}
								}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType3)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType3.get(minIndex); //docShareType2 chnages to docShareType3 code typping error -HIS-28366 IPD Coversheet- Showing Network Error After Save  Surgical Order

							if(lastObj.getDocShare()!=null && lastObj.getDocShare()>0){
								shareAmt = amount * (lastObj.getDocShare()/100);
								}
							else
								if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0 )
								{
									lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; 
	
									}else{
										shareAmt = lastObj.getDocShareAmount() ; 
									}
								}
							else 
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType4.size()>0)
				{
					if(docShareType4.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType4)
						{
								if(dto.getDocShare()!=null && dto.getDocShare()>0){
									shareAmt = amount * (dto.getDocShare()/100);
								}
								else
								if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0 ){
									
									dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; 
	
									}else{
										shareAmt = dto.getDocShareAmount() ; 
									}
								}
							else 
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType4)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType4.get(minIndex);
							
								if(lastObj.getDocShare()!=null && lastObj.getDocShare()>0){
									shareAmt = amount * (lastObj.getDocShare()/100);
								}
								else 
								if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0)
								{
									lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
									if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
										shareAmt = lastObj.getDocShareAmount() ; 
	
									}else{
										shareAmt = lastObj.getDocShareAmount() ; 
									}
								}
							else 
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType5.size()>0)
				{
					if(docShareType5.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType5)
						{
							if(dto.getDocShare()!=null && dto.getDocShare()>0)
								shareAmt = amount * (dto.getDocShare()/100);
							else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0){
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = dto.getDocShareAmount()>amount ? 0 : dto.getDocShareAmount(); 
							}else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}
				}else{
					return 0.00;
				}
				
			}else{
				return 0.00;
			}

		}catch(Exception e){
			logger.error("getDoctorSharePercentage failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}


	@Override
	public Double getDoctorReferralPercentage(OrderDetailsMasterDto orderDetailsMasterDto,Integer referralId,Integer visitTypeId,Integer billingBedCategoryId,Integer patientTypeId,Integer paymentEntitlementId)
	{
		try{
			Double shareAmt =0.00;
			Double amount = 0.00;
			orderDetailsMasterDto.setOrderQty(orderDetailsMasterDto.getOrderQty() != null ? orderDetailsMasterDto.getOrderQty() : 1);
			
			if(orderDetailsMasterDto.getDiscountOwnership()==0) //Discount ownership taken by both doctor and hospital
			{
				if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
					amount = (orderDetailsMasterDto.getOrdTotalAmt()).doubleValue();	
				}else{
					amount = (orderDetailsMasterDto.getOrdTotalAmt().subtract((orderDetailsMasterDto.getOrdDiscount()).add(new BigDecimal(orderDetailsMasterDto.getCrNetAmount())).add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
				}
			}
			if(orderDetailsMasterDto.getDiscountOwnership()!=0){
				if(orderDetailsMasterDto.getCreditNoteService() != null && orderDetailsMasterDto.getCreditNoteService() == true){
					amount = (orderDetailsMasterDto.getOrdTotalAmt().add(orderDetailsMasterDto.getOrdDiscount())).doubleValue();	
				}else{
					amount = (orderDetailsMasterDto.getOrdTotalAmt()).doubleValue();	
				}
			}
			
			
			List<Long> dayCountList = new LinkedList<>();
			GetDoctorShareDto lastObj = new GetDoctorShareDto();
			List<GetDoctorShareDto> docShareType1 = new LinkedList();
			List<GetDoctorShareDto> docShareType2 = new LinkedList();
			List<GetDoctorShareDto> docShareType3 = new LinkedList();
			Response res = iOrderMasterDao.getDoctorReferralPercentage(orderDetailsMasterDto,referralId,visitTypeId,billingBedCategoryId,patientTypeId,paymentEntitlementId);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				ListIterator<GetDoctorShareDto> iterator = (ListIterator<GetDoctorShareDto>) res.getListObject().listIterator();
				while(iterator.hasNext())
				{
					GetDoctorShareDto getDoctorShareDto = iterator.next();
					
					if(getDoctorShareDto.getDocShareType()==1)
						docShareType1.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==2)
						docShareType2.add(getDoctorShareDto);
					if(getDoctorShareDto.getDocShareType()==3)
						docShareType3.add(getDoctorShareDto);
				}
				
				if(docShareType1.size()>0)
				{
					if(docShareType1.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType1)
						{
							if(dto.getDocShare()!=null && dto.getDocShare()>0)
								shareAmt = amount * (dto.getDocShare()/100); 
							else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0){
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = dto.getDocShareAmount()>amount ? 0 : dto.getDocShareAmount(); 
							}
							else
								shareAmt = 0.00;
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType1)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType1.get(minIndex);
							
							if(lastObj.getDocShare()!=null)
								shareAmt = amount * (lastObj.getDocShare()/100); 
							else if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0){
								lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = lastObj.getDocShareAmount();
							}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType2.size()>0)
				{
					if(docShareType2.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType2)
						{
							
							if(dto.getDocShare()!=null && dto.getDocShare()>0)
								shareAmt = amount * (dto.getDocShare()/100);
							else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0){
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = dto.getDocShareAmount();
							}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}else{
						for(GetDoctorShareDto dto : docShareType2)
						{
							Date effectiveDate=GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd");
							//in milliseconds
							long diff = TimeZoneComponent.getDateByzone(orderDetailsMasterDto.getOrgUnitId()).getTime() - effectiveDate.getTime();
							Long dayDiffDay =  diff / (24 * 60 * 60 * 1000);
							dayCountList.add(dayDiffDay);
						
						}
						if(dayCountList.size()>0){
							int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
							lastObj=docShareType2.get(minIndex);
							
							if(lastObj.getDocShare()!=null && lastObj.getDocShare()>0)
								shareAmt = amount * (lastObj.getDocShare()/100);
							else  if(lastObj.getDocShareAmount()!=null && lastObj.getDocShareAmount()>0){
								lastObj.setDocShareAmount((lastObj.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = lastObj.getDocShareAmount();
							}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
						
					}
						
				}else if(docShareType3.size()>0)
				{
					if(docShareType3.size()==1)
					{
						for(GetDoctorShareDto dto : docShareType3)
						{
							if(dto.getDocShare()!=null && dto.getDocShare()>0 )
								shareAmt = amount * (dto.getDocShare()/100);
							else if(dto.getDocShareAmount()!=null && dto.getDocShareAmount()>0){
								dto.setDocShareAmount((dto.getDocShareAmount()*orderDetailsMasterDto.getOrderQty().doubleValue()));
								shareAmt = dto.getDocShareAmount()>amount ? 0 : dto.getDocShareAmount(); 
							}
							else
								shareAmt = 0.00;
							
							return new Double (df2.format(shareAmt));
						}
					}
				}else{
					return 0.00;
				}
			}else{
			    return 0.00;
			}

		}catch(Exception e){
			logger.error("getDoctorReferralPercentage failed", e);
			throw new ServiceException(e.getMessage());
		}
	  return null;
	}


	@Override
	@Transactional
	public Integer getOrderNumberPharmacyByAdmission(Integer admissionId) throws ApplicationException {
		try {	
			return iOrderMasterDao.getOrderNumberPharmacyByAdmission(admissionId);
		}catch(Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Integer getOrderDetailsIdByOrderId(int orderId, int orgId, int unitId) throws ApplicationException {
		try {	
			return iOrderMasterDao.getOrderDetailsIdByOrderId(orderId, orgId, unitId);
		}catch(Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Integer updateOrderDetails(OrderDetailsMasterDto dto) throws ApplicationException {
		try {	
			return iOrderMasterDao.updateOrderDetails(dto);
		}catch(Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public UnitApplicationConfigurationDto getPharmacyServiceId(int orgId, int unitId) throws ApplicationException {
		try {	
			return iOrderMasterDao.getPharmacyServiceId(orgId,unitId);
		}catch(Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response saveOrderMasterPharmacy(OrderMasterDto orderMasterDto, OrderDetailsMasterDto dto,
			 boolean flag) throws ApplicationException {
		try {
			
			 Integer pharamcyServiceOrderDetailId = null;
			 if(flag)
			 {
			  orderMasterDto.setOrderNo(getOrderNumber(orderMasterDto.getOrgUnitId()));
				Response orderMasterResponse = iOrderMasterDao.saveOrderMaster(orderMasterDto);
				if(orderMasterResponse.getStatus().equals(SUCCESS) && orderMasterResponse.getObject() != null && orderMasterDto.getListOrderDetailsMasterDto() != null) {
					Integer orderId = (Integer) orderMasterResponse.getObject();
					for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) {
						orderDetailsMasterDto.setOrderId(orderId);
						orderDetailsMasterDto.setStatus('A');
						orderDetailsMasterDto.setIsAppointableRadiology('N');
						Response orderDetaResponseRes =   iOrderDetailsMasterService.saveOrderDetailsMaster(orderDetailsMasterDto);
						if(orderDetaResponseRes.getObject()!=null && orderDetailsMasterDto.getSaleDetailId()==null)
						 {
							pharamcyServiceOrderDetailId = (Integer) orderDetaResponseRes.getObject();
						 }
						
					}
				}
			 }
			 else{
				 for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) {
						orderDetailsMasterDto.setStatus('A');
						orderDetailsMasterDto.setIsAppointableRadiology('N');
						iOrderDetailsMasterService.saveOrderDetailsMaster(orderDetailsMasterDto);
						
					}
				 
				 iOrderMasterDao.updateOrderDetails(dto);
				 
			   }
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, pharamcyServiceOrderDetailId);
			}catch (Exception e) {
				logger.error("saveOrderMasterPharmacy failed", e);
				throw new ServiceException(e.getMessage());
			}

	}





	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getOtConfiguredServiced(int orgId, int unitId) throws ApplicationException {
		try {	
			return iOrderMasterDao.getOtConfiguredServiced(orgId,unitId);
		}catch(Exception e) {
			
			throw e;
		}
	}


	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response saveOrderMasterOt(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
			orderMasterDto.setOrderNo(getOrderNumber(orderMasterDto.getOrgUnitId()));
			Response orderMasterResponse = iOrderMasterDao.saveOrderMaster(orderMasterDto);
			if (orderMasterResponse.getStatus().equals(SUCCESS) && orderMasterResponse.getObject() != null
					&& orderMasterDto.getListOrderDetailsMasterDto() != null) {
				Integer orderId = (Integer) orderMasterResponse.getObject();
				for (OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto()) {
					orderDetailsMasterDto.setOrderId(orderId);
					orderDetailsMasterDto.setStatus('A');
					orderDetailsMasterDto.setIsAppointableRadiology('N');
					 iOrderDetailsMasterService.saveOrderDetailsMaster(orderDetailsMasterDto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveOrderMasterOt failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getMedicationsByOrderId(int orderId) throws ApplicationException 
	{
		try{
			 return iOrderMasterDao.getMedicationsByOrderId(orderId);
		} catch (Exception e) {
			logger.error("getMedicationsByOrderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response assignReviewApplicableDate(Integer orderDetailsId) throws ApplicationException 
	{
		try{
			String appointmentDate = null;
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto = iOrderMasterDao.checkIfReviewApplicableForService(orderDetailsId);
			if(unitServiceMasterDetailsDto!=null && unitServiceMasterDetailsDto.getIsReviewApplicable()!=null && unitServiceMasterDetailsDto.getIsReviewApplicable()=='Y' && (unitServiceMasterDetailsDto.getDoctorId()!=null || unitServiceMasterDetailsDto.getSpecialityId()!=null))
			{
				
                List<SlotCheckDto> listDto = iOrderMasterDao.getSlotsDataForReview(unitServiceMasterDetailsDto);
                if(listDto!=null && listDto.size() > 0) {
                	 long hours = unitServiceMasterDetailsDto.getReviewTime();
             		long totalHrs = 24;
                     for(SlotCheckDto dto : listDto)
                     {
                     	if(hours<=totalHrs)
             	    	{
             	    		if(hours<=24)
             	    			appointmentDate = dto.getAppointmentDate().concat(" "+(hours)+":00:00");
             	    		else
             	    			appointmentDate = dto.getAppointmentDate().concat(" "+(hours-24)+":00:00");
             	    		
             	    		break;
             	    	}
                     	totalHrs=totalHrs+24;
                     }
                     iOrderMasterDao.assignReviewApplicableDate(orderDetailsId,appointmentDate);
                }else {
                	return new Response<>(SUCCESS, null, "Review not applicable...!!", null, null);
                }
               
			}
			return new Response<>(SUCCESS, null, "Review not applicable...!!", null, null);
		} catch (Exception e) {
			logger.error("assignReviewApplicableDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public Response getDocShareForLineItem(OrderMasterDto orderMasterDto) 
	{
		try{
			
			for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto())
			{
				
				OrderDetailsMaster toBeUpdated = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsMasterDto.getOrderDetailsId());
				orderDetailsMasterDto.setOrderQty(toBeUpdated.getOrderQty()!= null ? toBeUpdated.getOrderQty() :1);
				
				Double serviceAmt = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
				
				if(serviceAmt > 0 && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 && orderDetailsMasterDto.getIsDocReq()!=null && orderDetailsMasterDto.getIsDocReq().equals('Y'))
				{	
					orderDetailsMasterDto.setDocSharePer(new BigDecimal(getDoctorSharePercentage(orderDetailsMasterDto,orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
				}else{
					
					if(orderDetailsMasterDto.getDiscountOwnership()!=0){
						
						orderDetailsMasterDto.setDocSharePer(new BigDecimal(getDoctorSharePercentage(orderDetailsMasterDto,orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));

					}else{
						
						orderDetailsMasterDto.setDocSharePer(null);
					}
					
					
				}
				if(serviceAmt > 0 && orderMasterDto.getReferralId()!=null && orderMasterDto.getReferralId()>0 && orderDetailsMasterDto.getIsRefDoctorShare()!=null && orderDetailsMasterDto.getIsRefDoctorShare().equals('Y'))
				{
					orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(getDoctorReferralPercentage(orderDetailsMasterDto,orderMasterDto.getReferralId(),orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
				}else{
					
					if(orderDetailsMasterDto.getDiscountOwnership()!=0){
						
						orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(getDoctorReferralPercentage(orderDetailsMasterDto,orderMasterDto.getReferralId(),orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
						
					}else{
						
						orderDetailsMasterDto.setRefDocSharePer(null);
					}
					
				}
				if( orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
				{
					if(orderDetailsMasterDto.getDiscountOwnership()==1) //Discount owned by doctor
					{
						orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}
				if(orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
				{
					if(orderDetailsMasterDto.getDiscountOwnership()==3) //Discount owned by referral
					{
						orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}
				if(serviceAmt > 0 &&  orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
				{
					Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
					
					if(orderDetailsMasterDto.getDiscountOwnership()!=2) //Discount owned by referral
					{
						if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmt)
						{
							orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						}else if(totalShare > serviceAmt && orderDetailsMasterDto.getDiscountOwnership() == 3)
						{
							orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt-orderDetailsMasterDto.getDocSharePer().doubleValue()));
						}else if(totalShare > serviceAmt && orderDetailsMasterDto.getDiscountOwnership() != 3)
						{
							orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						}
					}
					
					
				}
				orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
				toBeUpdated.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
				toBeUpdated.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());
				toBeUpdated.setDocSharePer(orderDetailsMasterDto.getDocSharePer()!=null ? new BigDecimal(df2.format(orderDetailsMasterDto.getDocSharePer())):BigDecimal.ZERO);
				toBeUpdated.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer()!=null ? new BigDecimal(df2.format(orderDetailsMasterDto.getRefDocSharePer())):BigDecimal.ZERO);
				toBeUpdated.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));
				toBeUpdated.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()); 
				orderDetailsMasterDto.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));
				
				iOrderDetailsMasterDao.saveOrderDetailsMaster(toBeUpdated,null,null);
				
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, orderMasterDto);
		}catch(Exception e){
			logger.error("getDocShareForLineItem failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	//@Transactional
	public OrderDetailsMasterDto calculateRefShareForPharmacyService(OrderMasterDto orderMasterDto) 
	{
		try{
			
			OrderDetailsMasterDto orderDetailsMasterDto = orderMasterDto.getOrderDetailsMasterDto();
			orderDetailsMasterDto.setOrderQty(orderDetailsMasterDto.getOrderQty()!= null ? orderDetailsMasterDto.getOrderQty() :1);	
			Double serviceAmt = (orderDetailsMasterDto.getOrdTotalAmt().doubleValue()-(orderDetailsMasterDto.getOrdDiscount().doubleValue()+orderDetailsMasterDto.getOrdConcession().doubleValue()));
			
			if(serviceAmt > 0 && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 && orderDetailsMasterDto.getIsDocReq()!=null && orderDetailsMasterDto.getIsDocReq().equals('Y'))
			{	
				orderDetailsMasterDto.setDocSharePer(new BigDecimal(getDoctorSharePercentage(orderDetailsMasterDto,orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
			}else{
				orderDetailsMasterDto.setDocSharePer(null);
			}
			if(serviceAmt > 0 && orderMasterDto.getReferralId()!=null && orderMasterDto.getReferralId()>0 && orderDetailsMasterDto.getIsRefDoctorShare()!=null && orderDetailsMasterDto.getIsRefDoctorShare()!=null && orderDetailsMasterDto.getIsRefDoctorShare().equals('Y'))
			{
				orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(getDoctorReferralPercentage(orderDetailsMasterDto,orderMasterDto.getReferralId(),orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
			}else{
				orderDetailsMasterDto.setRefDocSharePer(null);
			}
			if(serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer()!=null && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
			{
				if(orderDetailsMasterDto.getDiscountOwnership()==1) //Discount owned by doctor
				{
					orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					orderDetailsMasterDto.setDocSharePer(new BigDecimal(df2.format(orderDetailsMasterDto.getDocSharePer())));
				}
			}
			if(serviceAmt > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
			{
				if(orderDetailsMasterDto.getDiscountOwnership()==3) //Discount owned by referral
				{
					orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(df2.format(orderDetailsMasterDto.getRefDocSharePer())));
				}
			}
			if(serviceAmt > 0 &&  orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue()>0 && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
			{
				Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
				
				if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmt)
				{
					orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
				}else if(totalShare > serviceAmt)
				{
					orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt-orderDetailsMasterDto.getDocSharePer().doubleValue()));
					orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(df2.format(orderDetailsMasterDto.getRefDocSharePer())));
				}
			}
			orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
			
			return orderDetailsMasterDto;
		}catch(Exception e){
			logger.error("calculateRefShareForPharmacyService failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateIsHomeCollection(List<Integer> orderIds, boolean homeCollection) {
		try{
			iOrderMasterDao.updateIsHomeCollection(orderIds,homeCollection);
		}catch(Exception e){
			logger.error("updateIsHomeCollection failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}


	@Override
	@Transactional
	public Response updateOrderMasterForWebApp(OrderMasterDto orderMasterDto) throws ApplicationException {
		try{
			iOrderMasterDao.updateOrderMasterForWebApp(orderMasterDto);
		}catch(Exception e){
			logger.error("updateOrderMasterForWebApp failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;

	}


	@Override
	@Transactional
	public Response cashPaidAmount(OrderMasterDto orderMasterDto) throws ApplicationException {
		try{
			return iOrderMasterDao.cashPaidAmount(orderMasterDto);
		}catch(Exception e){
			logger.error("cashPaidAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response saveNeuronPlusorderedServices(OrderMasterDto orderMasterDto) throws ApplicationException {
		try{
			
			Response res= iOrderMasterDao.saveNeuronPlusorderedServices(orderMasterDto);
			if(res.getObject() != null) {
				OrderMasterDto dto=(OrderMasterDto)res.getObject();
				return iNeuronPlusOrderDao.saveNeuronPlusorderedData(dto);
			}
		}catch(Exception e){
			logger.error("saveNeuronPlusorderedServices failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;

	}


	@Override
	@Transactional
	public Response updateCouponConsumptionId(Integer tariffId,Integer patientCouponTemplateMapperId, Integer orderDetailsId)
			throws ApplicationException {
		try{
			iOrderMasterDao.updateCouponConsumptionId(tariffId,patientCouponTemplateMapperId,orderDetailsId);
		}catch(Exception e){
			logger.error("updateCouponConsumptionId failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}


	@Override
	@Transactional
	public Response getOrderedMedication(BillingMasterDto dto) throws ApplicationException {
		try{
			return iOrderMasterDao.getOrderedMedication(dto);
		}catch(Exception e){
			logger.error("getOrderedMedication failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getVirtualStoreId(BillingMasterDto dto) throws ApplicationException {
		try{
			return iOrderMasterDao.getVirtualStoreId(dto);
		}catch(Exception e){
			logger.error("getVirtualStoreId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response orderMedicationService(List<OrderDetailsMasterDto> list) throws ApplicationException {

		try {
				Boolean checkOrder = false;
				OrderMasterDto orderMst = null;
				Integer  patientTypeId = null;
				Integer  patientClassId = null;
				Integer  billingBedCategoryId = null;
				Integer  paymentEntitlementId = null;
				Integer  contractId = null;
				Integer  payeeId = null;
				Integer  wardId = null;
				Integer  roomId = null;
				Integer  bedId = null;
				Integer visitTypeId =null;
				
				UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
				uAppConfDto.setOrganizationId(list.get(0).getOrgId());
				uAppConfDto.setUnitId(list.get(0).getOrgUnitId());
				
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
				if (appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null) 
				{
					uAppConfDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
				}
				if(list.get(0).getAdmissionId()!=null && list.get(0).getAdmissionId() > 0 ) 
				{
					
					Response admissionDto = ipdChargesDao.getAdmissionsById(list.get(0).getAdmissionId());
					AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
						patientTypeId = adminDto.getPatientCategoryId();
						billingBedCategoryId = adminDto.getBedBillingCategoryId();
						paymentEntitlementId = adminDto.getPaymentEntitlementId();
						contractId = adminDto.getContractId();
						payeeId = adminDto.getPayeeId();
						visitTypeId = adminDto.getVisitTypeId();
						patientClassId = adminDto.getPatientClassId();
						wardId = adminDto.getWardId();
						roomId = adminDto.getRoomId();
						bedId =  adminDto.getBedId();
				}
				
				if(list.get(0).getEncounterId()!=null && list.get(0).getEncounterId() >0) 
				{
					
					Response encounterRes = iEncounterMasterService.getEncounterDetailsById(list.get(0).getEncounterId());
					EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encounterRes.getObject();
						patientTypeId = encounterMasterDto.getPatientCategoryId();
						paymentEntitlementId = encounterMasterDto.getPaymentEntitlementId();
						visitTypeId = 1;
						contractId = encounterMasterDto.getContractId();
						payeeId = encounterMasterDto.getPayeeId();
						patientClassId = encounterMasterDto.getPatientClassId();
						billingBedCategoryId = uAppConfDto.getOpdBillingCatId();
				}
				
				
				UnitApplicationConfigurationDto appConfigDto = this.getPharmacyServiceId(list.get(0).getOrgId() ,list.get(0).getOrgUnitId() );
				
			for (OrderDetailsMasterDto orderDetailsMasterDto : list) {
				
					checkOrder = false;
					
					SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
					Date today=formatter2.parse(orderDetailsMasterDto.getOrderDateString());  
					
					// insert order master
					orderMst = new OrderMasterDto();
					orderMst.setAdmissionId(orderDetailsMasterDto.getAdmissionId());
					orderMst.setEncounterId(orderDetailsMasterDto.getEncounterId());
					orderMst.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
					orderMst.setCreatedDate(today.getTime());
					orderMst.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "dd-M-yyyy"));
					orderMst.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
					orderMst.setDiscountPercentage(0);
					orderMst.setOrderDate(today.getTime());
					orderMst.setOrderStatus(ACTIVE);
					orderMst.setOrderStatusId(1);
					orderMst.setOrdRemarks("Pharmacy Service");
					orderMst.setOrgId(orderDetailsMasterDto.getOrgId());
					orderMst.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
					orderMst.setPatientId(orderDetailsMasterDto.getPatientId());
					orderMst.setPaymentEntitlementTypeId(paymentEntitlementId);
					orderMst.setPriorityId(1);
					orderMst.setIsPharmacy('Y');
					orderMst.setUpdatedBy(orderDetailsMasterDto.getCreatedBy());
					orderMst.setUpdatedDate(today.getTime());
					orderMst.setVisitTypeId(visitTypeId);
					
							List<OrderDetailsMasterDto> listOrderDetails = new ArrayList<OrderDetailsMasterDto>();
							
							// for Pharmacy Service order Details 
							if(checkOrder == false){
									OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
										orderDetails.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
										orderDetails.setCreatedDate(today.getTime());
										orderDetails.setFromMedicationCoBilling(1);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrderQty(1);
										orderDetails.setOrdTotalAmt(orderDetailsMasterDto.getServiceAmt());
										orderDetails.setOrgId(orderDetailsMasterDto.getOrgId());
										orderDetails.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
										orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
										orderDetails.setServiceAmt(orderDetailsMasterDto.getServiceAmt());
										orderDetails.setServiceId(appConfigDto.getPharmacyServiceId());
										orderDetails.setDepartmentId(appConfigDto.getServiceSpecilaityId());
										orderDetails.setIsRefDoctorShare(appConfigDto.getIsRefDoctorShare());
										orderDetails.setServiceIsBilled(0);
										orderDetails.setServiceRendered(0);
										orderDetails.setStatus(ACTIVE);
										orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
										orderDetails.setUpdatedBy(orderDetailsMasterDto.getCreatedBy());
										orderDetails.setUpdatedDate(today.getTime());
										orderDetails.setPriorityId(2);
										orderDetails.setIsOutsourced('N');
										orderDetails.setOrderSchDate(today.getTime());
										orderDetails.setOrdConcession(BigDecimal.valueOf(0));
										orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
										orderDetails.setServiceRenderingDeptId(appConfigDto.getServiceSpecilaityId());
										orderDetails.setOrdCancelled('N');
										orderDetails.setIsDrug('N');
										orderDetails.setIsProcedure(NO);
										orderDetails.setIsProcedureService(NO);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrdRenderDatetime(today.getTime());
										orderDetails.setIsInvestigationOrder('N');
										orderDetails.setWardId(wardId);
										orderDetails.setRoomId(roomId);
										orderDetails.setBedId(bedId);
										orderDetails.setPackageConsumptionAmt(new BigDecimal(0));

										Double taxperAmount = 0.0;
										if(orderDetailsMasterDto.getTaxPer() != null && orderDetailsMasterDto.getTaxPer().doubleValue()>0){
											 taxperAmount = (orderDetailsMasterDto.getServiceAmt().doubleValue()/100)*orderDetailsMasterDto.getTaxPer().doubleValue();
										}
										Double total = orderDetails.getServiceAmt().doubleValue() + taxperAmount; 
										
										orderDetails.setOrdConcession(new BigDecimal(0.0));
										orderDetails.setOrdDiscount(new BigDecimal(0.00));
										orderDetails.setTaxAmount(new BigDecimal(taxperAmount));
										orderDetails.setTaxPer(orderDetailsMasterDto.getTaxPer());
										orderDetails.setTaxId(orderDetailsMasterDto.getTaxId());
										orderDetails.setPayeeId(payeeId);
										orderDetails.setContractId(contractId);
										if(paymentEntitlementId ==1 ){
											orderDetails.setCoPayPer(new BigDecimal(100));
											orderDetails.setSelfPayable(new BigDecimal(df2.format( total)));
											orderDetails.setCreditPayable(new BigDecimal(0.0)); 
										}else{
											orderDetails.setSelfPayable(new BigDecimal(0.0));
											orderDetails.setCreditPayable(new BigDecimal(df2.format( total))); 
											orderDetails.setCoPayPer(new BigDecimal(0.0));
										}
										
										orderDetails.setNetAmount(new BigDecimal(df2.format( total)));
										

										ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
											consumptionDto.setOrganizationId(orderDetailsMasterDto.getOrgId());
											consumptionDto.setUnitId(orderDetails.getOrgUnitId());
											consumptionDto.setVisitTypeId(visitTypeId);
											consumptionDto.setPatientTypeId(patientTypeId);
											consumptionDto.setBillingBedCategoryId(billingBedCategoryId);
											consumptionDto.setPaymentEntitlementId(paymentEntitlementId);
											consumptionDto.setServiceId(orderDetails.getServiceId());
											consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(today, "yyyy-MM-dd"));
											consumptionDto.setEncounterId(orderDetailsMasterDto.getEncounterId());
											consumptionDto.setAdmissionId(orderDetailsMasterDto.getAdmissionId());
											consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
											consumptionDto.setSelfPayeeId(payeeId);
											consumptionDto.setPatientId(orderDetailsMasterDto.getPatientId());
											consumptionDto.setContractId(contractId);
											consumptionDto.setPatientCategoryId(patientTypeId);
											consumptionDto.setPatientClassId(patientClassId);
										Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
										consumptionDto = (ContractConsumptionDto) res.getObject();

									if (res.getObject() != null) 
									{
										
										consumptionDto.setSelfPayable(orderDetails.getSelfPayable().doubleValue());
										consumptionDto.setCreaditPayable(orderDetails.getCreditPayable().doubleValue());
										consumptionDto.setServiceRate(orderDetails.getServiceAmt().doubleValue());
										consumptionDto.setNetAmount(orderDetails.getNetAmount().doubleValue());
										consumptionDto.setOrdTotalAmount(orderDetails.getServiceAmt().doubleValue());
										orderDetails.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
										
										if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
											orderDetails.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
										
									}
										listOrderDetails.add(orderDetails);
										checkOrder = true;
									
								}
							
									if(checkOrder == true){
							
										OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();
										orderDetails.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
										orderDetails.setCreatedDate(today.getTime());
										orderDetails.setFromMedicationCoBilling(1);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrderQty(orderDetailsMasterDto.getOrderQty());
										orderDetails.setOrdTotalAmt(orderDetailsMasterDto.getServiceAmt());
										orderDetails.setOrgId(orderDetailsMasterDto.getOrgId());
										orderDetails.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
										orderDetails.setPackageConsumptionAmt(BigDecimal.valueOf(0));
										orderDetails.setServiceAmt(orderDetailsMasterDto.getServiceAmt());
										//orderDetails.setServiceId(appConfigDto.getPharmacyServiceId());
										orderDetails.setServiceIsBilled(0);
										orderDetails.setServiceRendered(0);
										orderDetails.setStatus(ACTIVE);
										orderDetails.setTariffId(uAppConfDto.getDefaultSelfTariffId());
										orderDetails.setUpdatedBy(orderDetailsMasterDto.getCreatedBy());
										orderDetails.setUpdatedDate(today.getTime());
										orderDetails.setPriorityId(2);
										orderDetails.setIsOutsourced('N');
										orderDetails.setOrderSchDate(today.getTime());
										orderDetails.setOrdConcession(BigDecimal.valueOf(0));
										orderDetails.setOrdDiscount(BigDecimal.valueOf(0));
										//orderDetails.setServiceRenderingDeptId(appConfigDto.getServiceSpecilaityId());
										orderDetails.setOrdCancelled('N');
										orderDetails.setBatchId(orderDetailsMasterDto.getBatchId());
										orderDetails.setDrugId(orderDetailsMasterDto.getDrugId());
										orderDetails.setIsDrug('Y');
										orderDetails.setIsProcedure(NO);
										orderDetails.setIsProcedureService(NO);
										orderDetails.setOrderDate(today.getTime());
										orderDetails.setOrdRenderDatetime(today.getTime());
										orderDetails.setIsInvestigationOrder('N');
										orderDetails.setWardId(wardId);
										orderDetails.setRoomId(roomId);
										orderDetails.setBedId(bedId);
										orderDetails.setPackageConsumptionAmt(new BigDecimal(0));
										
										Double taxperAmount = 0.0;
										if(orderDetailsMasterDto.getTaxPer() != null && orderDetailsMasterDto.getTaxPer().doubleValue()>0){
											 taxperAmount = (orderDetailsMasterDto.getServiceAmt().doubleValue()/100)*orderDetailsMasterDto.getTaxPer().doubleValue();
										}
									
										Double total = orderDetails.getServiceAmt().doubleValue() + taxperAmount; 
										
										orderDetails.setOrdConcession(new BigDecimal(0.0));
										orderDetails.setOrdDiscount(new BigDecimal(0.00));
										orderDetails.setTaxAmount(new BigDecimal(taxperAmount));
										orderDetails.setTaxPer(orderDetailsMasterDto.getTaxPer());
										orderDetails.setTaxId(orderDetailsMasterDto.getTaxId());
										orderDetails.setPayeeId(payeeId);
										orderDetails.setContractId(contractId);
										if(paymentEntitlementId ==1 ){
											orderDetails.setCoPayPer(new BigDecimal(100));
											orderDetails.setSelfPayable(new BigDecimal(df2.format( total)));
											orderDetails.setCreditPayable(new BigDecimal(0.0)); 
										}else{
											orderDetails.setSelfPayable(new BigDecimal(0.0));
											orderDetails.setCreditPayable(new BigDecimal(df2.format( total))); 
											orderDetails.setCoPayPer(new BigDecimal(0.0));
										}
										
										orderDetails.setNetAmount(new BigDecimal(df2.format( total)));
										
										
										listOrderDetails.add(orderDetails);
				
							}		
							if(listOrderDetails != null && listOrderDetails.size()>1){
								orderMst.setListOrderDetailsMasterDto(listOrderDetails);
								this.saveOrderMasterWithDiscount(orderMst, null);
							}	
							
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			logger.error("orderMedicationService failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getDocShareForLineItemForCreditNote(OrderMasterDto orderMasterDto) throws ApplicationException {

		try{
			
			Boolean checkFlag = false;
			
			for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto())
			{
				Integer doctorShareVac = 0;
				
				Integer referShareVac = 0;
				
				OrderDetailsMaster toBeUpdated = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsMasterDto.getOrderDetailsId());
				orderDetailsMasterDto.setOrderQty(toBeUpdated.getOrderQty()!= null ? toBeUpdated.getOrderQty() :1);
				Double serviceAmt = orderDetailsMasterDto.getOrdTotalAmt().doubleValue();
				
				if(orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 && orderDetailsMasterDto.getIsDocReq()!=null && orderDetailsMasterDto.getIsDocReq().equals('Y'))
				{	
					doctorShareVac = iOrderDetailsMasterDao.getDoctorShareVac(orderDetailsMasterDto.getOrderDetailsId());
					
					if(doctorShareVac == 1){
						orderDetailsMasterDto.setDiscountOwnership(2);
						checkFlag = true;
						orderDetailsMasterDto.setDocSharePer(toBeUpdated.getDocSharePer());
					}else{
						
						orderDetailsMasterDto.setCreditNoteService(true);
						orderDetailsMasterDto.setDocSharePer(new BigDecimal(getDoctorSharePercentage(orderDetailsMasterDto,orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
					}
					
				}
				if( orderMasterDto.getReferralId()!=null && orderMasterDto.getReferralId()>0 && orderDetailsMasterDto.getIsRefDoctorShare()!=null && orderDetailsMasterDto.getIsRefDoctorShare().equals('Y'))
				{
					
					 referShareVac = iOrderDetailsMasterDao.getReferShareVac(orderDetailsMasterDto.getOrderDetailsId());
						
						if(referShareVac == 1){
							orderDetailsMasterDto.setDiscountOwnership(2);
							checkFlag = true;
							orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
						}else{
							orderDetailsMasterDto.setCreditNoteService(true);
							orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(getDoctorReferralPercentage(orderDetailsMasterDto,orderMasterDto.getReferralId(),orderMasterDto.getVisitTypeId(),orderMasterDto.getBillingBedCategoryId(),orderMasterDto.getPatientTypeId(),orderMasterDto.getPaymentEntitlementTypeId())));
						}
				}
				if( orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue()>0  && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
				{
					
					if(orderDetailsMasterDto.getDiscountOwnership()==1) //Discount owned by doctor
					{
						orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}
				if(orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
				{
					
					if(orderDetailsMasterDto.getDiscountOwnership()==3) //Discount owned by referral
					{
						orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}
				if(serviceAmt > 0 &&  orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
				{
					Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
					
					if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmt)
					{
						orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
					}else if(totalShare > serviceAmt)
					{
						orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt-orderDetailsMasterDto.getDocSharePer().doubleValue()));
					}
				}
				orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
				/*toBeUpdated.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
				toBeUpdated.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());*/
				
				if(checkFlag == true){
					
					orderDetailsMasterDto.setDocSharePer(toBeUpdated.getDocSharePer());
					orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
					
				}
				
			/*	if(checkFlag == true){
					
					orderDetailsMasterDto.setDocSharePer(toBeUpdated.getDocSharePer());
					orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
					
					toBeUpdated.setDocSharePer(orderDetailsMasterDto.getDocSharePer()!=null ? new BigDecimal(df2.format(orderDetailsMasterDto.getDocSharePer())):BigDecimal.ZERO);
					toBeUpdated.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer()!=null ? new BigDecimal(df2.format(orderDetailsMasterDto.getRefDocSharePer())):BigDecimal.ZERO);
				}else{
					orderDetailsMasterDto.setDocSharePer(toBeUpdated.getDocSharePer());
					orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
				}*/
				
			/*	toBeUpdated.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));
				toBeUpdated.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()); */
				orderDetailsMasterDto.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));
				
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, orderMasterDto,checkFlag);
		}catch(Exception e){
			logger.error("getDocShareForLineItemForCreditNote failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response clearDoctor(OrderMasterDto orderMasterDto) {
		for(OrderDetailsMasterDto orderDetailsMasterDto : orderMasterDto.getListOrderDetailsMasterDto())
		{
			iOrderDetailsMasterDao.clearDoctor(orderDetailsMasterDto);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, orderMasterDto,null);
	}


	@Override
	@Transactional
	public Response getPackageConsumeAmount(Integer orderDetailsId) throws ApplicationException {
		
		try{
			return iOrderMasterDao.getPackageConsumeAmount(orderDetailsId);
		}catch(Exception e){
			logger.error("getPackageConsumeAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
