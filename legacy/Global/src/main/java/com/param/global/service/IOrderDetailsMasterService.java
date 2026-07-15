package com.param.global.service;

import java.util.List;
import java.util.Set;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.model.OrderDetailsMaster;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOrderDetailsMasterService {
	Response updateIsBilledStatus(Set<Integer> orderDetailsList, Integer billMasterId, List<Integer> pharmacyOrderIdList) throws ApplicationException;
	Response saveOrderDetailsMaster(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;
	Response updateOrderDetailsWithDiscount(List<OrderDetailsMasterDto> orderDetailsList, Integer reqDisId, Integer length,OrderMasterDto orderMasterDto, Character updateSevices) throws ApplicationException;
	Response cancelOrder(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException;
    Response cancelServices(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;
    public Response getPatientServiceOrderListBySaleDetails(OrderDetailsMasterDto dto) throws BillingException ;
    Response updateOrderDetailsForAccessionNumber(Integer orderDetailsIdForAccession)throws BillingException ;
	Response renderAndPostRates(List<Integer> listObject)throws BillingException;
	Response updateOrderDetailsForDeptWiseAppointment(Integer orderDetailsId,Integer doctorId,Integer deptId) throws BillingException;
	Response saveOrderDetailsMasterForPayee(OrderDetailsMaster masterDto, List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,List<OrderDetailsPackageMapperDto> OrderDetailsPackageMapperDtoList)throws BillingException;
	OrderDetailsMaster getOrderDetailsByIdForPayeeUpdate(Integer orderDetailsMasterId) throws BillingException;
	Response updateOrderDetailsObj(OrderDetailsMaster orderDetailsMasterDto, Double double1);
	List<OrderDetailsMasterDto> getItemDetailsFromOrderDetailsForUpdateReturnCalculation(Integer orderId);
	Response inactiveOldOrderDetails(Integer orderId);
	Response updateOrderDetails(List<OrderDetailsMasterDto> orderDetailsList, Integer reqDisId, Integer length,	OrderMasterDto orderMasterDto) throws ApplicationException;
	Response saveOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster,List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList, List<OrderDetailsPackageMapperDto> ordDtlsPkgMppr);
	Integer checkIsBilledServices(Set<Integer> orderDetailsIdList);
	boolean getDummyServiceFlagForPackageService(List<Integer> packageConsumptionMasterId);
	Response opdateOrderDetailsAndDoctorShare(RequestDiscountDto dto)throws ApplicationException;
	Response updateDummyHiddenServiceFlag(String flag, int orderDetailsId, boolean value, boolean isPackage, int packageId, int admissionId);
	Response updateDoctorShareRisByChangingDoctor(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;
	
}
