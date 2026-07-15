package com.param.global.dao;

import java.util.Date;
import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.dto.global.SlotCheckDto;
import com.param.global.model.OrderMaster;
import com.param.global.otims.dto.ProcedureOrderMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOrderMasterDao extends IGenericDao<OrderMaster, Integer>{
	public Response saveOrderMaster(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response getDoctorSharePercentage(OrderDetailsMasterDto orderDetailsMasterDto, Integer visitTypeId,
			Integer billingBedCategoryId, Integer patientTypeId, Integer paymentEntitlementId);

	public Long getNextValOrderMasterId()throws ApplicationException;
	
	public Integer getOrderNumberPharmacyByAdmission(Integer admissionId) throws ApplicationException;
	
	public Integer getOrderDetailsIdByOrderId(int orderId,int orgId,int unitId) throws ApplicationException;
	
	public UnitApplicationConfigurationDto getPharmacyServiceId(int orgId,int unitId) throws ApplicationException;
	
	public Integer updateOrderDetails(OrderDetailsMasterDto dto) throws ApplicationException;
	
	public Response getOtConfiguredServiced(int orgId,int unitId) throws ApplicationException;
	
	public Response getAutoPostingOrderedByAdmittionAndDate(Integer admissionId,Date currentDate) throws ApplicationException;
	
	public Response getMedicationsByOrderId(int orderId) throws ApplicationException;

	Response getDoctorReferralPercentage(OrderDetailsMasterDto orderDetailsMasterDto, Integer referralId, Integer visitTypeId,
			Integer billingBedCategoryId, Integer patientTypeId, Integer paymentEntitlementId);
	
	public Response updateOrderDetailsByOdrDtlsId(Integer orderDetailsId) throws ApplicationException;

	public UnitServiceMasterDetailsDto checkIfReviewApplicableForService(Integer orderDetailsId);

	public List<SlotCheckDto> getSlotsDataForReview(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto);

	public Integer assignReviewApplicableDate(Integer orderDetailsId, String appointmentDate);

	public OrderDetailsMasterDto getOrderIdForLabService(Integer encounterId, Integer admissionId);

	public Response updateIsHomeCollection(List<Integer> orderIds, boolean homeCollection);

	public Response updateOrderMasterForWebApp(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response cashPaidAmount(OrderMasterDto orderMasterDto);

	public Response saveNeuronPlusorderedServices(OrderMasterDto orderMasterDto)throws ApplicationException;

	public Response updateCouponConsumptionId(Integer tariffId,Integer patientCouponTemplateMapperId, Integer orderDetailsId)throws ApplicationException;

	public Response getOrderedMedication(BillingMasterDto dto)throws ApplicationException;

	public Response getVirtualStoreId(BillingMasterDto dto)throws ApplicationException;

	public Response getPackageConsumeAmount(Integer orderDetailsId)throws ApplicationException;
	
}
