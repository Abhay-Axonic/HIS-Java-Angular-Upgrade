package com.param.global.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class OrderDetailsMasterServiceImpl implements IOrderDetailsMasterService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(OrderDetailsMasterServiceImpl.class);

	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IContractConsumptionDao iContractConsumptionDao; 
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IBillingService iBillingMasterService;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	
	@Autowired
	IMPackageMasterDao 	iMPackageMasterDao;
	
	public static final DecimalFormat df2 = new DecimalFormat("#.##");
	
	@Override
	//@Transactional
	public Response updateIsBilledStatus(Set<Integer> orderDetailsList, Integer billMasterId, List<Integer> pharmacyOrderIdList) throws ApplicationException {
		try {
				return iOrderDetailsMasterDao.updateIsBilledStatus(orderDetailsList, billMasterId,pharmacyOrderIdList);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response updateOrderDetails(List<OrderDetailsMasterDto> orderDetailsList,Integer reqDisId,Integer length,OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
				return updateOrderDetailsWithDiscount(orderDetailsList,reqDisId,length,orderMasterDto,'N');
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	public Response updateOrderDetailsWithDiscount(List<OrderDetailsMasterDto> orderDetailsList, Integer reqDisId,
			Integer length, OrderMasterDto orderMasterDto, Character updateSevices) throws ApplicationException {
		try {
			List<Integer> orderIds = new LinkedList<Integer>();
			List<OrderDetailsMasterDto> listForBatchInsert = new ArrayList<>();
			Integer packageId = null;
			for (OrderDetailsMasterDto detailsMasterDto : orderDetailsList) {
				if(detailsMasterDto.isPackage() == true && detailsMasterDto.getStatus()=='I'&& detailsMasterDto.getPackageId() != null) {
					packageId = detailsMasterDto.getPackageId();
				}
			}
			
			
			for (OrderDetailsMasterDto detailsMasterDto : orderDetailsList) {
			

				if (length != null && length == 0 && reqDisId != null && detailsMasterDto.getReqDisAmt() != null
						&& detailsMasterDto.getReqDisAmt().compareTo(new BigDecimal("0")) == 1
						&& detailsMasterDto.getIsDiscountApplicable() == 'Y') {

					RequestDiscountDetailsDto requestDiscountDetailsDto = new RequestDiscountDetailsDto();
					requestDiscountDetailsDto.setReqDiscId(reqDisId);
					requestDiscountDetailsDto.setOrderId(detailsMasterDto.getOrderId());
					requestDiscountDetailsDto.setOrderDetailsId(detailsMasterDto.getOrderDetailsId());
					requestDiscountDetailsDto.setServiceId(detailsMasterDto.getServiceId());
					requestDiscountDetailsDto.setNetAmount(
							detailsMasterDto.getOrdTotalAmt().subtract(detailsMasterDto.getOrdConcession()));
					requestDiscountDetailsDto.setReqDisAmt(detailsMasterDto.getReqDisAmt());
					requestDiscountDetailsDto.setApprovedDiscAmt(new BigDecimal("0.00"));
					requestDiscountDetailsDto.setCreatedBy(detailsMasterDto.getUpdatedBy());
					requestDiscountDetailsDto.setUpdatedBy(detailsMasterDto.getUpdatedBy());
					requestDiscountDetailsDto.setCreatedDate(GlobalCommonDateUtils
							.getStringDate(detailsMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDetailsDto.setUpdatedDate(GlobalCommonDateUtils
							.getStringDate(detailsMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ssss"));
					requestDiscountDetailsDto.setOrganizationId(detailsMasterDto.getOrgId());
					requestDiscountDetailsDto.setUnitId(detailsMasterDto.getOrgUnitId());
					iOrderDetailsMasterDao.requestDiscountDetails(requestDiscountDetailsDto);

				}
				if (updateSevices == 'N') {
					// <----Referal & Doctor Share logic
					Double totalPayableAmt = (detailsMasterDto.getOrdTotalAmt().subtract(detailsMasterDto.getOrdDiscount().add(detailsMasterDto.getOrdConcession()))).doubleValue();
					Double serviceAmt = totalPayableAmt - detailsMasterDto.getPackageConsumptionAmt().doubleValue();
					detailsMasterDto.setDiscountOwnership(detailsMasterDto.getDiscountOwnership() != null? detailsMasterDto.getDiscountOwnership() : 0);
					detailsMasterDto.setOrderQty(detailsMasterDto.getOrderQty() != null ? detailsMasterDto.getOrderQty() : 1);
					
					if (serviceAmt > 0 && detailsMasterDto.getOrdDocId() != null && detailsMasterDto.getOrdDocId() > 0
							&& detailsMasterDto.getIsDocReq() != null && detailsMasterDto.getIsDocReq().equals('Y'))
						detailsMasterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(
								detailsMasterDto, orderMasterDto.getVisitTypeId(),
								orderMasterDto.getBillingBedCategoryId(), orderMasterDto.getPatientTypeId(),
								orderMasterDto.getPaymentEntitlementTypeId())));
					else
						detailsMasterDto.setDocSharePer(null);

					if (serviceAmt > 0 && orderMasterDto.getReferralId() != null
							&& detailsMasterDto.getIsRefDoctorShare() != null
							&& detailsMasterDto.getIsRefDoctorShare().equals('Y'))
						detailsMasterDto.setRefDocSharePer(
								new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(detailsMasterDto,
										orderMasterDto.getReferralId(), orderMasterDto.getVisitTypeId(),
										orderMasterDto.getBillingBedCategoryId(), orderMasterDto.getPatientTypeId(),
										orderMasterDto.getPaymentEntitlementTypeId())));
					else
						detailsMasterDto.setRefDocSharePer(null);

					if (serviceAmt > 0 && detailsMasterDto.getDocSharePer() != null
							&& detailsMasterDto.getDocSharePer().doubleValue() > 0
							&& (detailsMasterDto.getDiscountOwnership() != null
									&& detailsMasterDto.getDiscountOwnership() != 0)
							&& (detailsMasterDto.getOrdDiscount() != null
									|| detailsMasterDto.getOrdConcession() != null)) {
						if (detailsMasterDto.getDiscountOwnership() == 1) // Discount
																			// owned
																			// by
																			// doctor
						{
							detailsMasterDto.setDocSharePer(detailsMasterDto.getDocSharePer().subtract(
									detailsMasterDto.getOrdDiscount().add(detailsMasterDto.getOrdConcession())));
						}
					}
					if (serviceAmt > 0 && detailsMasterDto.getRefDocSharePer() != null
							&& detailsMasterDto.getRefDocSharePer().doubleValue() > 0
							&& (detailsMasterDto.getDiscountOwnership() != null
									&& detailsMasterDto.getDiscountOwnership() != 0)
							&& (detailsMasterDto.getOrdDiscount() != null
									|| detailsMasterDto.getOrdConcession() != null)) {
						if (detailsMasterDto.getDiscountOwnership() == 3) // Discount
																			// owned
																			// by
																			// referral
						{
							detailsMasterDto.setRefDocSharePer(detailsMasterDto.getRefDocSharePer().subtract(
									detailsMasterDto.getOrdDiscount().add(detailsMasterDto.getOrdConcession())));
						}
					}

					if (serviceAmt > 0 && detailsMasterDto.getDocSharePer() != null
							&& detailsMasterDto.getDocSharePer().doubleValue() > 0
							&& detailsMasterDto.getRefDocSharePer() != null
							&& detailsMasterDto.getRefDocSharePer().doubleValue() > 0) {
						Double totalShare = detailsMasterDto.getDocSharePer().doubleValue()
								+ detailsMasterDto.getRefDocSharePer().doubleValue();

						if (detailsMasterDto.getDocSharePer().doubleValue() == serviceAmt) {
							detailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						} else if (totalShare > serviceAmt) {
							detailsMasterDto.setRefDocSharePer(
									new BigDecimal(serviceAmt - detailsMasterDto.getDocSharePer().doubleValue()));
						}
					}
					detailsMasterDto
							.setHospitalShare(new BigDecimal(serviceAmt - ((detailsMasterDto.getRefDocSharePer() != null
									? detailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)
									+ (detailsMasterDto.getDocSharePer() != null
											? detailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
					// Referal & Doctor Share logic---->

					if ((detailsMasterDto.getOrdDiscountPer() != null && detailsMasterDto.getOrdDiscountPer().doubleValue() != 100 ) && totalPayableAmt.equals(detailsMasterDto.getPackageConsumptionAmt().doubleValue()) && detailsMasterDto.getPackageId() == null)
						{
							if(totalPayableAmt == 0) {
								
								if(detailsMasterDto.getOrderDetailsPackageMapperDtosList() != null && detailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0) {
									Boolean checkFlag = false;
									for (OrderDetailsPackageMapperDto dto : detailsMasterDto.getOrderDetailsPackageMapperDtosList()) {
										if(dto.getIsPackageService() != null && dto.getIsPackageService() == 'Y') {
											
											if(packageId != null  && packageId.equals(dto.getPackageId())) {
												checkFlag = false;
												dto.setIsPackageService('N');
											}else {
												checkFlag = true;
											}
											
										}
									}
									
									if(checkFlag) {
										detailsMasterDto.setIsPackageService('Y');
									}else {
										detailsMasterDto.setIsPackageService('N');
									}
								}else {
									detailsMasterDto.setIsPackageService('N');
								}
								
							}else {
								detailsMasterDto.setIsPackageService('Y');
							}
							
						}
					else {
						if(totalPayableAmt > 0 && !totalPayableAmt.equals(detailsMasterDto.getPackageConsumptionAmt().doubleValue()) ) {
							detailsMasterDto.setIsPackageService('N');
						}else {
							detailsMasterDto.setIsPackageService(detailsMasterDto.getIsPackageService());
						}
						
					}
						

					listForBatchInsert.add(detailsMasterDto);
				}
				orderIds.add(detailsMasterDto.getOrderId());

			}
			if (listForBatchInsert.size() > 0)
				iOrderDetailsMasterDao.updateOrderDetailsBetterPerformance(listForBatchInsert,
						orderMasterDto.getSelfPayeeId());

			if (orderMasterDto.getEncounterId() != null && orderIds.size() > 0)
				iOrderMasterService.updateIsHomeCollection(orderIds, orderMasterDto.isHomeCollection());

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error("updateOrderDetailsWithDiscount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response saveOrderDetailsMaster(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {
			return iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto);
	}catch (Exception e) {
		logger.error("saveOrderDetailsMaster failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelOrder(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try {
			 	//updating previous record
				iOrderDetailsMasterDao.updateOrderDetails(orderDetailsMasterDto,'N',0);
			 
				//adding new record
			 	OrderDetailsMasterDto saveDetailsMasterDto = new OrderDetailsMasterDto();
			 	saveDetailsMasterDto=orderDetailsMasterDto;
				 	saveDetailsMasterDto.setOrderDetailsId(null);
				 	saveDetailsMasterDto.setOrderQty(-orderDetailsMasterDto.getOrderQty());
				 	saveDetailsMasterDto.setOldOrdDtlId(orderDetailsMasterDto.getOrderDetailsId());
				 	saveDetailsMasterDto.setCreatedDate(orderDetailsMasterDto.getUpdatedDate());
				 	saveDetailsMasterDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
			 	iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto);
			 	
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}catch (Exception e) {
		logger.error("cancelOrder failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response cancelServices(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException {
		try
		{
			Response orderDetailsRes = iOrderDetailsMasterDao.updateOrderDtlsAsCancelledById(orderDetailsMasterDto.getOrderDetailsId());
			if(orderDetailsRes.getCode().equalsIgnoreCase(SUCCESS_CODE))
			{
				EncounterMasterDto dto = iOrderDetailsMasterDao.getOrderAppointmentDetails(orderDetailsMasterDto.getOrderDetailsId());

				if(dto!=null && dto.getAppointmentId()!=null)
				{
					SlotMasterDto slotMasterDto = new SlotMasterDto();
						slotMasterDto.setEncounterId(dto.getEncounterId());
						slotMasterDto.setAppointmentId(dto.getAppointmentId());
						slotMasterDto.setSlotId(dto.getSlotId());
						slotMasterDto.setCancelledBy('C');
						slotMasterDto.setCancelReasonId(0);
						slotMasterDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
					iAppointmentSchedulingDao.cancelScheduledAppointment(slotMasterDto);
				}

		 		//iOrderDetailsMasterDao.incativeOrderPayeeDetails(orderDetailsMasterDto.getOrderDetailsId());

				return orderDetailsRes;
			}

		}catch (Exception e)
		{
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientServiceOrderListBySaleDetails(OrderDetailsMasterDto dto) throws BillingException {
		try {
			// updating previous record
			return iOrderDetailsMasterDao.getPatientServiceOrderListBySaleDetails(dto);

		} catch (Exception e) {
			logger.error("getPatientServiceOrderListBySaleDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateOrderDetailsForAccessionNumber(Integer orderDetailsIdForAccession) throws BillingException {
		try {
			// updating previous record
			return iOrderDetailsMasterDao.updateOrderDetailsForAccessionNumber(orderDetailsIdForAccession);

		} catch (Exception e) {
			logger.error("updateOrderDetailsForAccessionNumber failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response renderAndPostRates(List<Integer> listObject) throws BillingException {
		try{
			int count = 0;
			for (Integer orderDetailsId : listObject) 
			{

				OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao
						.getOrderDetailsByIdForPayeeUpdate(orderDetailsId);

				if (orderDetailsMasterDto.getServiceRendered() == 1) {
					return new Response(ERROR, ALREADY_EXIST_CODE, "Service is already rendered by another user...!!",
							null, null);
				} else if (orderDetailsMasterDto.getOrdCancelled() == 'Y') {
					return new Response(ERROR, ALREADY_EXIST_CODE, "Service is cancelled by another user...!!", null,
							null);
				} else {

					Response res = iOrderDetailsMasterDao.getVisitDetailsForRenderingService(orderDetailsId);
					AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();

					RadiologyOrdersListDto radiologyOrdersListDto = new RadiologyOrdersListDto();
					radiologyOrdersListDto.setOrderDetailsId(orderDetailsMasterDto.getOrderDetailsId());
					radiologyOrdersListDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy() != null && orderDetailsMasterDto.getUpdatedBy() > 0 ? orderDetailsMasterDto.getUpdatedBy() : orderDetailsMasterDto.getCreatedBy());

					// for render package
					Response packageMpprRes = iOrderDetailsMasterDao
							.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsId);
					List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
					for (OrderDetailsPackageMapperDto mpperDto : mpprList)
					{
						if (mpperDto.getIsPackageService() == 'Y') 
						{
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(mpperDto.getUnitId()).getTime());
							iMPackageMasterService.renderPackageServiceFrPostBillig(orderDetailsMasterDto,vistDetailsObj, mpperDto.getPackageId());
						}
					}
					Response succRes = iEncounterMasterDao.acceptRadiologyRequest(radiologyOrdersListDto);
					if (succRes.getStatus().equals(SUCCESS))
						count++;
				}
			}
			if(count==listObject.size())
				return new Response(SUCCESS, null, "Request accepted successfully...!!", null, null);
		}catch(Exception e)
		{
			logger.error("renderAndPostRates failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOrderDetailsForDeptWiseAppointment(Integer orderDetailsId,Integer doctorId,Integer deptId) throws BillingException {
		try {
			
			OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsId);		
			
			if(orderDetailsMasterDto.getServiceRendered()==1)
			{
				return new Response(ERROR, ALREADY_EXIST_CODE , "Service is already rendered by another user...!!", null, null);
			}else if(orderDetailsMasterDto.getOrdCancelled()=='Y')
			{
				return new Response(ERROR, ALREADY_EXIST_CODE, "Service is cancelled by another user...!!", null, null);
			}else {
				
				orderDetailsMasterDto.setOrdDocId(doctorId);
				Response res = iOrderDetailsMasterDao.getVisitDetailsForRenderingService(orderDetailsId);
				AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();
				
				if((vistDetailsObj.getIsDoctorReq()!=null && vistDetailsObj.getIsDoctorReq().equals('Y')) || (vistDetailsObj.getIsRefDoctorShare()!=null && vistDetailsObj.getIsRefDoctorShare().equals('Y')))
				{
					OrderDetailsMasterDto orderDetailsDto = new OrderDetailsMasterDto();
						orderDetailsDto.setOrdDocId(orderDetailsMasterDto.getOrdDocId() !=null  ? orderDetailsMasterDto.getOrdDocId() : doctorId);
						orderDetailsDto.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId() !=null ? orderDetailsMasterDto.getOrdDocSplId() : deptId);
						orderDetailsMasterDto.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId() !=null ? orderDetailsMasterDto.getOrdDocSplId() : deptId);
						orderDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
						orderDetailsDto.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
						orderDetailsDto.setTariffId(vistDetailsObj.getTariffId());
						orderDetailsDto.setOrdTotalAmt(orderDetailsMasterDto.getOrdTotalAmt());
						orderDetailsDto.setOrdDiscount(orderDetailsMasterDto.getOrdDiscount());
						orderDetailsDto.setOrdConcession(orderDetailsMasterDto.getOrdConcession());
						orderDetailsDto.setDiscountOwnership(vistDetailsObj.getDefaultDiscountOwnership()!=null ? vistDetailsObj.getDefaultDiscountOwnership() : 0);
						Integer serDeptId = iOrderDetailsMasterDao.getDepartmentIdByServiceId(orderDetailsMasterDto.getServiceId(),orderDetailsMasterDto.getOrgId());
						orderDetailsDto.setDepartmentId(serDeptId);
						orderDetailsDto.setOrderDate(orderDetailsMasterDto.getOrderDate());
						orderDetailsDto.setOrderQty(orderDetailsMasterDto.getOrderQty() !=null ? orderDetailsMasterDto.getOrderQty() : 1);
					Double serviceAmount = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();	
					
					if(serviceAmount >0 && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId() > 0 &&  vistDetailsObj.getIsDoctorReq()!=null  && vistDetailsObj.getIsDoctorReq().equals('Y') )
						orderDetailsMasterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(orderDetailsDto,vistDetailsObj.getVisitTypeId(),vistDetailsObj.getBillingBedCategoryId(),vistDetailsObj.getPatientTypeId(),vistDetailsObj.getPaymentEntitlementId())));
					else
						orderDetailsMasterDto.setDocSharePer(null);
					
					if(serviceAmount >0 && vistDetailsObj.getReferralId()!=null && vistDetailsObj.getIsRefDoctorShare()!=null && vistDetailsObj.getIsRefDoctorShare().equals('Y'))
						orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(orderDetailsDto,vistDetailsObj.getReferralId(),vistDetailsObj.getVisitTypeId(),vistDetailsObj.getBillingBedCategoryId(),vistDetailsObj.getPatientTypeId(),vistDetailsObj.getPaymentEntitlementId())));
					else
						orderDetailsMasterDto.setRefDocSharePer(null);
					
					if(serviceAmount >0 && orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
					{
						Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
						
						if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmount)
						{
							orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
						}else if(totalShare > serviceAmount)
						{
							orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmount-orderDetailsMasterDto.getDocSharePer().doubleValue()));
						}
						
					}
					orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmount-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
					orderDetailsMasterDto.setDiscountOwnership(vistDetailsObj.getDefaultDiscountOwnership()!=null ? vistDetailsObj.getDefaultDiscountOwnership() : 0);
					//Referal & Doctor Share logic---->
					if ((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId()!=null) && serviceAmount.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) && orderDetailsMasterDto.getPackageId() == null)
						orderDetailsMasterDto.setIsPackageService('Y');
					else
						orderDetailsMasterDto.setIsPackageService('N');
				}
				iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,null,null);
			}

		} catch (Exception e) {
			logger.error("updateOrderDetailsForDeptWiseAppointment failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	@Transactional
	public Response saveOrderDetailsMasterForPayee(OrderDetailsMaster masterDto,
			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,
			List<OrderDetailsPackageMapperDto> OrderDetailsPackageMapperDtoList) throws BillingException {

		try {
			return iOrderDetailsMasterDao.saveOrderDetailsMasterForPayee(masterDto, orderDetailsPayeeMapperDtosList, OrderDetailsPackageMapperDtoList);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Override
	@Transactional
	public OrderDetailsMaster getOrderDetailsByIdForPayeeUpdate(Integer orderDetailsMasterId) throws BillingException {
		try {
			return iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsMasterId);
		} catch (Exception e) {
			
			throw e;
		}
	}
	@Override
	public Response updateOrderDetailsObj(OrderDetailsMaster orderDetailsMasterDto, Double crNetAmt) {
		try {

			if (orderDetailsMasterDto.getDocSharePer() != null || orderDetailsMasterDto.getRefDocSharePer() != null) {
				Response res = iOrderDetailsMasterDao
						.getVisitDetailsForRenderingService(orderDetailsMasterDto.getOrderDetailsId());
				AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();

				OrderDetailsMasterDto detailsMasterDto = new OrderDetailsMasterDto();
				detailsMasterDto.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
				detailsMasterDto.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());
				detailsMasterDto.setServiceId(orderDetailsMasterDto.getServiceId());
				detailsMasterDto.setOrgUnitId(orderDetailsMasterDto.getOrgUnitId());
				detailsMasterDto.setTariffId(orderDetailsMasterDto.getTariffId());
				detailsMasterDto.setOrdTotalAmt(orderDetailsMasterDto.getOrdTotalAmt());
				detailsMasterDto.setOrdDiscount(orderDetailsMasterDto.getOrdDiscount());
				detailsMasterDto.setOrdConcession(orderDetailsMasterDto.getOrdConcession());
				detailsMasterDto.setDepartmentId(vistDetailsObj.getServiceSpecialityId());
				detailsMasterDto.setOrderDate(orderDetailsMasterDto.getOrderDate());
				detailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership() != null
						? orderDetailsMasterDto.getDiscountOwnership() : 0);
				detailsMasterDto.setCrNetAmount(crNetAmt);
				detailsMasterDto.setOrderQty(
						orderDetailsMasterDto.getOrderQty() != null ? orderDetailsMasterDto.getOrderQty() : 1);
				Double serviceAmt = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto
						.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()).add(new BigDecimal(crNetAmt))))
								.doubleValue();

				if (serviceAmt > 0 && orderDetailsMasterDto.getOrdDocId() != null
						&& orderDetailsMasterDto.getOrdDocId() > 0 && orderDetailsMasterDto.getDocSharePer() != null
						&& orderDetailsMasterDto.getDocSharePer().doubleValue() > 0.00)
					orderDetailsMasterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(
							detailsMasterDto, vistDetailsObj.getVisitTypeId(), vistDetailsObj.getBillingBedCategoryId(),
							vistDetailsObj.getPatientTypeId(), vistDetailsObj.getPaymentEntitlementId())));
				else
					orderDetailsMasterDto.setDocSharePer(null);

				if (serviceAmt > 0 && orderDetailsMasterDto.getRefDocSharePer() != null
						&& orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0.00)
					orderDetailsMasterDto.setRefDocSharePer(
							new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(detailsMasterDto,
									vistDetailsObj.getReferralId(), vistDetailsObj.getVisitTypeId(),
									vistDetailsObj.getBillingBedCategoryId(), vistDetailsObj.getPatientTypeId(),
									vistDetailsObj.getPaymentEntitlementId())));
				else
					orderDetailsMasterDto.setRefDocSharePer(null);

				if (serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer() != null
						&& orderDetailsMasterDto.getDocSharePer().doubleValue() > 0
						&& (orderDetailsMasterDto.getDiscountOwnership() != null
								&& orderDetailsMasterDto.getDiscountOwnership() != 0)
						&& (orderDetailsMasterDto.getOrdDiscount() != null
								|| orderDetailsMasterDto.getOrdConcession() != null)) {
					if (orderDetailsMasterDto.getDiscountOwnership() == 1) // Discount
																			// owned
																			// by
																			// doctor
					{
						orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(
								orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}
				if (serviceAmt > 0 && orderDetailsMasterDto.getRefDocSharePer() != null
						&& orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0
						&& (orderDetailsMasterDto.getDiscountOwnership() != null
								&& orderDetailsMasterDto.getDiscountOwnership() != 0)
						&& (orderDetailsMasterDto.getOrdDiscount() != null
								|| orderDetailsMasterDto.getOrdConcession() != null)) {
					if (orderDetailsMasterDto.getDiscountOwnership() == 3) // Discount
																			// owned
																			// by
																			// referral
					{
						orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(
								orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
					}
				}

				if (serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer() != null
						&& orderDetailsMasterDto.getDocSharePer().doubleValue() > 0
						&& orderDetailsMasterDto.getRefDocSharePer() != null
						&& orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0) {
					Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue()
							+ orderDetailsMasterDto.getRefDocSharePer().doubleValue();

					if (orderDetailsMasterDto.getDocSharePer().doubleValue() == serviceAmt) {
						orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
					} else if (totalShare > serviceAmt) {
						orderDetailsMasterDto.setRefDocSharePer(
								new BigDecimal(serviceAmt - orderDetailsMasterDto.getDocSharePer().doubleValue()));
					}
				}
				orderDetailsMasterDto.setHospitalShare(
						new BigDecimal(serviceAmt - ((orderDetailsMasterDto.getRefDocSharePer() != null
								? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)
								+ (orderDetailsMasterDto.getDocSharePer() != null
										? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
				orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership() != null
						? orderDetailsMasterDto.getDiscountOwnership() : 0);
				// Referal & Doctor Share logic---->
				if ((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId()!=null) && serviceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) && orderDetailsMasterDto.getPackageId() == null )
					orderDetailsMasterDto.setIsPackageService('Y');
				else
					orderDetailsMasterDto.setIsPackageService('N');
				iOrderDetailsMasterDao.updateOrderDetailsObj(orderDetailsMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateOrderDetailsObj failed", e);
		}
		return null;
	}
	@Override
	public List<OrderDetailsMasterDto> getItemDetailsFromOrderDetailsForUpdateReturnCalculation(Integer orderId) {
		try{
			return iOrderDetailsMasterDao.getItemDetailsFromOrderDetailsForUpdateReturnCalculation(orderId);
		}catch(Exception e){
			logger.error("getItemDetailsFromOrderDetailsForUpdateReturnCalculation failed", e);
		}
		return null;
	}
	@Override
	public Response inactiveOldOrderDetails(Integer orderId) {
		try{
			return iOrderDetailsMasterDao.inactiveOldOrderDetails(orderId);
		}catch(Exception e){
			logger.error("inactiveOldOrderDetails failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response saveOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster,List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList, List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr) {
		return iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMaster,orderDetailsPayeeList,ordDtlsPkgMppr);
	}

	@Override
	@Transactional
	public Integer checkIsBilledServices(Set<Integer> orderDetailsIdList) {
		return iOrderDetailsMasterDao.checkIsBilledServices(orderDetailsIdList);
	}

	@Override
	@Transactional
	public boolean getDummyServiceFlagForPackageService(List<Integer> packageConsumptionMasterId) {
		return iOrderDetailsMasterDao.getDummyServiceFlagForPackageService(packageConsumptionMasterId);
	}

	@Override
	@Transactional
	public Response updateDummyHiddenServiceFlag(String flag, int orderDetailsId, boolean value, boolean isPackage,int packageId, int admissionId) {
		return iOrderDetailsMasterDao.updateDummyHiddenServiceFlag(flag,orderDetailsId,value,isPackage,packageId,admissionId);
	}

	@Override
	@Transactional
	public Response opdateOrderDetailsAndDoctorShare(RequestDiscountDto dto) throws ApplicationException {

		try {

			Boolean checkFlag = false;

			Integer doctorShareVac = 0;

			Integer referShareVac = 0;
			Response res = iOrderDetailsMasterDao.getVisitDetailsForRenderingService(dto.getOrderDetailsId());
			AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();

			OrderDetailsMaster toBeUpdated = iOrderDetailsMasterDao
					.getOrderDetailsByIdForPayeeUpdate(dto.getOrderDetailsId());

			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
			orderDetailsMasterDto.setOrdDocId(toBeUpdated.getOrdDocId());
			orderDetailsMasterDto.setOrdDocSplId(toBeUpdated.getOrdDocSplId());
			orderDetailsMasterDto.setServiceId(toBeUpdated.getServiceId());
			orderDetailsMasterDto.setOrgUnitId(toBeUpdated.getOrgUnitId());
			orderDetailsMasterDto.setTariffId(toBeUpdated.getTariffId());
			orderDetailsMasterDto.setOrdTotalAmt(toBeUpdated.getOrdTotalAmt());
			orderDetailsMasterDto.setOrdDiscount(toBeUpdated.getOrdDiscount());
			orderDetailsMasterDto.setOrdConcession(toBeUpdated.getOrdConcession());
			orderDetailsMasterDto.setDepartmentId(vistDetailsObj.getServiceSpecialityId());
			orderDetailsMasterDto.setOrderDate(toBeUpdated.getOrderDate());
			orderDetailsMasterDto.setDiscountOwnership(toBeUpdated.getDiscountOwnership() != null ? toBeUpdated.getDiscountOwnership() : 0);
			orderDetailsMasterDto.setDocSharePer(BigDecimal.ZERO);
			orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
			orderDetailsMasterDto.setOrderDetailsId(dto.getOrderDetailsId());
			orderDetailsMasterDto.setOrderQty(toBeUpdated.getOrderQty() != null ? toBeUpdated.getOrderQty() : 1);
			Double serviceAmt =  (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto
					.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())))
					.doubleValue();

			if (toBeUpdated.getOrdDocId() != null && toBeUpdated.getOrdDocId() > 0) {
				doctorShareVac = iOrderDetailsMasterDao.getDoctorShareVac(toBeUpdated.getOrderDetailsId());

				if (doctorShareVac == 1) {
					toBeUpdated.setDiscountOwnership(2);
					checkFlag = true;
					toBeUpdated.setDocSharePer(toBeUpdated.getDocSharePer());
				} else {

					orderDetailsMasterDto.setCreditNoteService(true);
					orderDetailsMasterDto.setDocSharePer(
							new BigDecimal(iOrderMasterService.getDoctorSharePercentage(orderDetailsMasterDto,
									vistDetailsObj.getVisitTypeId(), vistDetailsObj.getBillingBedCategoryId(),
									vistDetailsObj.getPatientTypeId(), vistDetailsObj.getPaymentEntitlementId())));
				}

			}
			if (vistDetailsObj.getReferralId() != null && vistDetailsObj.getReferralId() > 0) {

				referShareVac = iOrderDetailsMasterDao.getReferShareVac(orderDetailsMasterDto.getOrderDetailsId());

				if (referShareVac == 1) {
					orderDetailsMasterDto.setDiscountOwnership(2);
					checkFlag = true;
					orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
				} else {
					orderDetailsMasterDto.setCreditNoteService(true);
					orderDetailsMasterDto.setRefDocSharePer(
							new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(orderDetailsMasterDto,
									vistDetailsObj.getReferralId(), vistDetailsObj.getVisitTypeId(),
									vistDetailsObj.getBillingBedCategoryId(), vistDetailsObj.getPatientTypeId(),
									vistDetailsObj.getPaymentEntitlementId())));
				}
			}
			if (orderDetailsMasterDto.getDocSharePer() != null
					&& orderDetailsMasterDto.getDocSharePer().doubleValue() > 0
					&& (orderDetailsMasterDto.getOrdDiscount() != null
							|| orderDetailsMasterDto.getOrdConcession() != null)) {

				if (orderDetailsMasterDto.getDiscountOwnership() == 1) // Discount
																		// owned
																		// by
																		// doctor
				{
					orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(
							orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
				}
			}
			if (orderDetailsMasterDto.getRefDocSharePer() != null
					&& orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0
					&& (orderDetailsMasterDto.getOrdDiscount() != null
							|| orderDetailsMasterDto.getOrdConcession() != null)) {

				if (orderDetailsMasterDto.getDiscountOwnership() == 3) // Discount
																		// owned
																		// by
																		// referral
				{
					orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(
							orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
				}
			}
			if (serviceAmt > 0 && orderDetailsMasterDto.getDocSharePer() != null
					&& orderDetailsMasterDto.getDocSharePer().doubleValue() > 0
					&& orderDetailsMasterDto.getRefDocSharePer() != null
					&& orderDetailsMasterDto.getRefDocSharePer().doubleValue() > 0) {
				Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue()
						+ orderDetailsMasterDto.getRefDocSharePer().doubleValue();

				if (orderDetailsMasterDto.getDocSharePer().doubleValue() == serviceAmt) {
					orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
				} else if (totalShare > serviceAmt) {
					orderDetailsMasterDto.setRefDocSharePer(
							new BigDecimal(serviceAmt - orderDetailsMasterDto.getDocSharePer().doubleValue()));
				}
			}
			orderDetailsMasterDto
					.setHospitalShare(new BigDecimal(serviceAmt - ((orderDetailsMasterDto.getRefDocSharePer() != null
							? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)
							+ (orderDetailsMasterDto.getDocSharePer() != null
									? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
			toBeUpdated.setOrdDocId(orderDetailsMasterDto.getOrdDocId());
			toBeUpdated.setOrdDocSplId(orderDetailsMasterDto.getOrdDocSplId());

			if (checkFlag == false) {
				toBeUpdated.setDocSharePer(orderDetailsMasterDto.getDocSharePer() != null
						? new BigDecimal(df2.format(orderDetailsMasterDto.getDocSharePer())) : BigDecimal.ZERO);
				toBeUpdated.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer() != null
						? new BigDecimal(df2.format(orderDetailsMasterDto.getRefDocSharePer())) : BigDecimal.ZERO);
			} else {
				orderDetailsMasterDto.setDocSharePer(toBeUpdated.getDocSharePer());
				orderDetailsMasterDto.setRefDocSharePer(toBeUpdated.getRefDocSharePer());
			}

			toBeUpdated.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));
			toBeUpdated.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership());
			orderDetailsMasterDto
					.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDto.getHospitalShare())));

			if ((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId()!=null) && serviceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) && orderDetailsMasterDto.getPackageId() == null)
				orderDetailsMasterDto.setIsPackageService('Y');
			else
				orderDetailsMasterDto.setIsPackageService('N');

			iOrderDetailsMasterDao.saveOrderDetailsMaster(toBeUpdated, null, null);

			if (doctorShareVac == 0) {
				Integer doctorShare = iOrderDetailsMasterDao.getDoctorShare(orderDetailsMasterDto.getOrderDetailsId());

				if (doctorShare == 1) {
					iOrderDetailsMasterDao.updateDoctorShare(orderDetailsMasterDto.getOrderDetailsId(), toBeUpdated);
				}

			}

			if (referShareVac == 0) {
				Integer referShare = iOrderDetailsMasterDao.getReferShare(orderDetailsMasterDto.getOrderDetailsId());

				if (referShare == 1) {
					iOrderDetailsMasterDao.updateReferShare(orderDetailsMasterDto.getOrderDetailsId(), toBeUpdated);
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Override
	@Transactional
	public Response updateDoctorShareRisByChangingDoctor(OrderDetailsMasterDto orderDetailsMasterDtos)
			throws ApplicationException {

		try {

			Boolean checkFlag = false;

			Integer doctorShareVac = 0;

			Integer referShareVac = 0;
			Response res = iOrderDetailsMasterDao
					.getVisitDetailsForRenderingService(orderDetailsMasterDtos.getOrderDetailsId());
			AdmissionsForBatchProcessDto vistDetailsObj = (AdmissionsForBatchProcessDto) res.getObject();

			OrderDetailsMaster toBeUpdated = iOrderDetailsMasterDao
					.getOrderDetailsByIdForPayeeUpdate(orderDetailsMasterDtos.getOrderDetailsId());
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
			orderDetailsMasterDto.setOrdDocId(orderDetailsMasterDtos.getOrdDocId());
			orderDetailsMasterDto.setOrdDocSplId(orderDetailsMasterDtos.getOrdDocSplId());
			orderDetailsMasterDto.setServiceId(orderDetailsMasterDtos.getServiceId());
			orderDetailsMasterDto.setOrgUnitId(orderDetailsMasterDtos.getOrgUnitId());
			orderDetailsMasterDto.setTariffId(orderDetailsMasterDtos.getTariffId());
			orderDetailsMasterDto.setOrdTotalAmt(orderDetailsMasterDtos.getOrdTotalAmt());
			orderDetailsMasterDto.setOrdDiscount(orderDetailsMasterDtos.getOrdDiscount());
			orderDetailsMasterDto.setOrdConcession(orderDetailsMasterDtos.getOrdConcession());
			orderDetailsMasterDto.setDepartmentId(vistDetailsObj.getServiceSpecialityId());
			orderDetailsMasterDto.setOrderDate(orderDetailsMasterDtos.getOrderDate());
			orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDtos.getDiscountOwnership() != null
					? orderDetailsMasterDtos.getDiscountOwnership() : 0);
			orderDetailsMasterDto.setDocSharePer(BigDecimal.ZERO);
			orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
			orderDetailsMasterDto.setOrderDetailsId(orderDetailsMasterDtos.getOrderDetailsId());

			Double serviceAmt =  (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto
					.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())))
					.doubleValue();

			if (orderDetailsMasterDtos.getOrdDocId() != null && orderDetailsMasterDtos.getOrdDocId() > 0) {
				doctorShareVac = iOrderDetailsMasterDao.getDoctorShareVac(orderDetailsMasterDtos.getOrderDetailsId());

				orderDetailsMasterDto.setDiscountOwnership(2);
				checkFlag = true;
				orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDtos.getDocSharePer());

			}
			if (vistDetailsObj.getReferralId() != null && vistDetailsObj.getReferralId() > 0
					&& orderDetailsMasterDtos.getIsRefDoctorShare() != null
					&& orderDetailsMasterDtos.getIsRefDoctorShare().equals('Y')) {

				referShareVac = iOrderDetailsMasterDao.getReferShareVac(orderDetailsMasterDtos.getOrderDetailsId());

				orderDetailsMasterDto.setDiscountOwnership(2);
				checkFlag = true;
				orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDtos.getRefDocSharePer());
			}
			if (orderDetailsMasterDtos.getDocSharePer() != null
					&& orderDetailsMasterDtos.getDocSharePer().doubleValue() > 0
					&& (orderDetailsMasterDtos.getOrdDiscount() != null
							|| orderDetailsMasterDtos.getOrdConcession() != null)) {

				if (orderDetailsMasterDto.getDiscountOwnership() == 1) // Discount
																		// owned
																		// by
																		// doctor
				{
					orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDtos.getDocSharePer().subtract(
							orderDetailsMasterDtos.getOrdDiscount().add(orderDetailsMasterDtos.getOrdConcession())));
				}
			}
			if (orderDetailsMasterDtos.getRefDocSharePer() != null
					&& orderDetailsMasterDtos.getRefDocSharePer().doubleValue() > 0
					&& (orderDetailsMasterDtos.getOrdDiscount() != null
							|| orderDetailsMasterDtos.getOrdConcession() != null)) {

				if (orderDetailsMasterDtos.getDiscountOwnership() == 3) // Discount
																		// owned
																		// by
																		// referral
				{
					orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDtos.getRefDocSharePer().subtract(
							orderDetailsMasterDtos.getOrdDiscount().add(orderDetailsMasterDtos.getOrdConcession())));
				}
			}
			if (serviceAmt > 0 && orderDetailsMasterDtos.getDocSharePer() != null
					&& orderDetailsMasterDtos.getDocSharePer().doubleValue() > 0
					&& orderDetailsMasterDtos.getRefDocSharePer() != null
					&& orderDetailsMasterDtos.getRefDocSharePer().doubleValue() > 0) {
				Double totalShare = orderDetailsMasterDtos.getDocSharePer().doubleValue()
						+ orderDetailsMasterDtos.getRefDocSharePer().doubleValue();

				if (orderDetailsMasterDtos.getDocSharePer().doubleValue() == serviceAmt) {
					orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
				} else if (totalShare > serviceAmt) {
					orderDetailsMasterDto.setRefDocSharePer(
							new BigDecimal(serviceAmt - orderDetailsMasterDtos.getDocSharePer().doubleValue()));
				}
			}
			orderDetailsMasterDto
					.setHospitalShare(new BigDecimal(serviceAmt - ((orderDetailsMasterDtos.getRefDocSharePer() != null
							? orderDetailsMasterDtos.getRefDocSharePer().doubleValue() : 0.00)
							+ (orderDetailsMasterDtos.getDocSharePer() != null
									? orderDetailsMasterDtos.getDocSharePer().doubleValue() : 0.00))));
			toBeUpdated.setOrdDocId(orderDetailsMasterDtos.getOrdDocId());
			toBeUpdated.setOrdDocSplId(orderDetailsMasterDtos.getOrdDocSplId());

			orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDtos.getDocSharePer() != null
					? new BigDecimal(df2.format(orderDetailsMasterDtos.getDocSharePer())) : BigDecimal.ZERO);
			orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDtos.getRefDocSharePer() != null
					? new BigDecimal(df2.format(orderDetailsMasterDtos.getRefDocSharePer())) : BigDecimal.ZERO);

			toBeUpdated.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDtos.getHospitalShare())));
			toBeUpdated.setOrdDocId(orderDetailsMasterDtos.getOrdDocId());
			toBeUpdated.setOrdDocSplId(orderDetailsMasterDtos.getOrdDocSplId());
			orderDetailsMasterDto
					.setHospitalShare(new BigDecimal(df2.format(orderDetailsMasterDtos.getHospitalShare())));

			if ((orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue() != 0.00 || orderDetailsMasterDto.getPackageId()!=null) && serviceAmt.equals(orderDetailsMasterDto.getPackageConsumptionAmt().doubleValue()) && orderDetailsMasterDto.getPackageId() == null )
				orderDetailsMasterDto.setIsPackageService('Y');
			else
				orderDetailsMasterDto.setIsPackageService('N');
			
			iOrderDetailsMasterDao.saveOrderDetailsMaster(toBeUpdated, null, null);

			Integer doctorShare = iOrderDetailsMasterDao.getDoctorShare(orderDetailsMasterDto.getOrderDetailsId());

			iOrderDetailsMasterDao.updateDoctorShareWithDoctorAndSpeciality(orderDetailsMasterDto.getOrderDetailsId(),
					toBeUpdated);

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}
}
