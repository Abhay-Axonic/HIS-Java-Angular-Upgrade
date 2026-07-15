package com.param.global.service;

import java.util.List;

import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOrderMasterService {
	public Response saveOrderMaster(OrderMasterDto orderMasterDto) throws ApplicationException;
	
	public Response saveOrderMasterPharmacy(OrderMasterDto orderMasterDto,OrderDetailsMasterDto dto,boolean flag) throws ApplicationException;
	
	public Response saveOrderMasterOt(OrderMasterDto orderMasterDto) throws ApplicationException;

	public Response saveOrderMasterWithDiscount(OrderMasterDto orderMasterDto, Integer reqDisId) throws ApplicationException;

	public Response creditNoteCancelSerivces(OrderMasterDto orderMasterDto) throws ApplicationException;

	public Response getPreviousOrdersByPatient(int orgId, int orgUnitId, int patientId, int visitTypeId)
			throws ApplicationException;

	public Double getDoctorSharePercentage(OrderDetailsMasterDto orderDetailsMasterDto, Integer visitTypeId,
			Integer billingBedCategoryId, Integer patientTypeId,Integer paymentEntitlementId) throws ApplicationException;
	
	public Integer getOrderNumberPharmacyByAdmission(Integer admissionId) throws ApplicationException;
	
	public Integer getOrderDetailsIdByOrderId(int orderId,int orgId,int unitId) throws ApplicationException;
	
	public UnitApplicationConfigurationDto getPharmacyServiceId(int orgId,int unitId) throws ApplicationException;
	
	public Integer updateOrderDetails(OrderDetailsMasterDto dto) throws ApplicationException;
	
	public Response getOtConfiguredServiced(int orgId,int unitId) throws ApplicationException;
	
	public Response getMedicationsByOrderId(int orderId) throws ApplicationException;

	Double getDoctorReferralPercentage(OrderDetailsMasterDto orderDetailsMasterDto,Integer referralId, Integer visitTypeId,
			Integer billingBedCategoryId, Integer patientTypeId, Integer paymentEntitlementId);

	Response assignReviewApplicableDate(Integer orderDetailsId) throws ApplicationException;

	public Response getDocShareForLineItem(OrderMasterDto orderMasterDto);

	OrderDetailsMasterDto calculateRefShareForPharmacyService(OrderMasterDto orderMasterDto);

	public Response updateIsHomeCollection(List<Integer> orderIds, boolean homeCollection);

	public Response updateOrderMasterForWebApp(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response cashPaidAmount(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response saveNeuronPlusorderedServices(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response updateCouponConsumptionId(Integer tariffId,Integer patientCouponTemplateMapperId, Integer orderDetailsId)throws ApplicationException;

	public Response getOrderedMedication(BillingMasterDto dto)throws ApplicationException;

	public Response getVirtualStoreId(BillingMasterDto dto)throws ApplicationException;

	public Response orderMedicationService(List<OrderDetailsMasterDto> list)throws ApplicationException;

	public Response getDocShareForLineItemForCreditNote(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response clearDoctor(OrderMasterDto orderMasterDto);

	public Response getPackageConsumeAmount(Integer orderDetailsId)throws ApplicationException;


}
