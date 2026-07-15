package com.param.global.dao;

import java.util.List;
import java.util.Set;

import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.common.Response;
import com.param.global.dto.*;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOrderDetailsMasterDao extends IGenericDao<OrderDetailsMaster, Integer>{
	public Response saveOrderDetailsMaster(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;
	public Response updateIsBilledStatus(Set<Integer> orderDetailsList, Integer billMasterId, List<Integer> pharmacyOrderIdList) throws ApplicationException;
	public Response updateOrderDetails(OrderDetailsMasterDto detailsMasterDto, Character isFinalBill, Integer selfPayeeId)throws ApplicationException;
	public Response getOrderedPackgeServForCancel(int packageId, Integer encounterId,Integer admissionId,Integer visitTypeId)throws ApplicationException;
	public Response getOrderDetailsById(int orderDetailsId)throws ApplicationException;
	public Response updateOrderDtlsAsCancelledById(int orderDetailsId)throws ApplicationException;
	public Response getPreviousOrderSerivice(int orgId,int orgUnitId,int orderId)throws ApplicationException;
	public Response updateOrderDtlsByOrderDtlsId(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException;
	public Response getPatientServiceOrderListBySaleDetails(OrderDetailsMasterDto dto) throws BillingException ;
	public OrderDetailsMaster getOrderDetailsByIdForPayeeUpdate(Integer orderDetailsMasterId) throws BillingException;
	public Response orderDetailsLogAfterUpdatePayee(OrderDetailsMaster masterDto)throws BillingException;
	public Response saveOrderDetailsMaster(OrderDetailsMaster masterDto, List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList)throws BillingException;
	public Response saveOrderDetailsMaster(OrderDetailsMaster masterDto, List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,List<OrderDetailsPackageMapperDto> OrderDetailsPackageMapperDtoList)throws BillingException;
	public Response saveOrderDetailsMasterForPayee(OrderDetailsMaster masterDto, List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,List<OrderDetailsPackageMapperDto> OrderDetailsPackageMapperDtoList)throws BillingException;
	public Response saveOrderDetailsPayeeDetails(OrderDetailsMasterDto orderDetailsMasterDto);
	public Response getOrderPayeeDetailsByOrderDetailsId(Integer orderDetailsId, Integer billId);
	public Response getOrderPayeeDetailsByOrderDetailsId(List<Integer> orderDetailsId, Integer billId);
	public Response getAvailableRefundAmountCreditNote(Integer orderDetailsId, Integer billId);
	public Response updateOrderDetailsForAccessionNumber(Integer orderDetailsIdForAccession)throws ApplicationException;
	Response getVisitDetailsForRenderingService(Integer orderDetailsId) throws ApplicationException;
	Response updateIsBilledFlag(Integer orderDetailsId) throws ApplicationException;
	public Response incativeOrderPayeeDetails(Integer orderDetailsId);
	public Response updateReviewedServiceStatus(Integer orderDetailsId);
	Response cancelUnRenderedServices(CancelOrderDto cancelOrderDto) throws BillingException;
	public Response updateOrderMasterData(List<Integer> orderMasterIds,ConvertServicesFromOptoIpDto convertServicesFromOptoIpDto);
	
	
	//Packge 
	public Response getOrderDetailsPackageMapperByOrderDetailsId(Integer orderDetailsId);
	public Response getOrderDetailsPackageMapperByOrderDetailsId(List<Integer> orderDetailsIds);
	public EncounterMasterDto getOrderAppointmentDetails(Integer orderDetailsId);
	public Response updateEncounterToOrderMaster(EncounterMasterDto dto,Integer orderId);
	public Integer getDepartmentIdByServiceId(Integer serviceId, Integer orgId) throws BillingException;
	OrderDetailsMaster getOrderDetailsForRewiewAppointment(Integer orderDetailsMasterId) throws BillingException;
	public Response updateOrderDetail(List<Integer> tempServiceIdList, Set<Integer> serviceIdList,Set<Integer> orderDetailsIdList, Integer unitId);
	public Response updateOrdDocIdAndSpecialityId(OrderDetailsMasterDto orderDetailsMasterDto);
	
	//for cancel bill pharmacy if consolidated
	public Response inactiveSaleInOrderDetails(Integer saleId);
	public Response updateOrderDetailsObj(OrderDetailsMaster orderDetailsMasterDto);
	Response updateOrderDetailsPayeeMapper(OrderDetailsPayeeMapperDto detailsPayeeMapperDto);
	public Response cancelProcedureSerivicesByProcedure(ProcedureOrderMasterDto order);
	public EncounterMasterDto checkConsultationService(Integer encounterId);
	public Integer checkIsBilledServices(Set<Integer> orderDetailsIdList);
	public Response getEhcOrderedServices(AdmissionsForBatchProcessDto dto) throws ApplicationException;
	public List<OrderDetailsMasterDto> getItemDetailsFromOrderDetailsForUpdateReturnCalculation(Integer orderId);
	public Response inactiveOldOrderDetails(Integer orderId);
	Response updateOrderDetailsBetterPerformance(List<OrderDetailsMasterDto> orderDetailsMasterDtoList,Integer selfPayeeId) throws ApplicationException;
	Response saveOrderDetailsMasterBetterPerformannce(List<OrderDetailsMasterDto> listForSaveCall)throws ApplicationException;
	Response requestDiscountDetails(RequestDiscountDetailsDto requestDiscountDetailsDto) throws ApplicationException;
	public Response updateOrderDetailsList(CoversheetTemplateMasterDto coversheetTemplateMasterDto);
	public Response saveOrderDetailsMasterList(OrderDetailsMasterDto ordDtlDto)throws ApplicationException;
	public Response updateFreeOrderService(OrderDetailsMasterDto orderDetailsMasterDto)throws ApplicationException;
	public Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServices)throws ApplicationException;
	public Response updateFreeTextInvestigations(CoversheetTemplateMasterDto coversheetTemplateMasterDto)throws ApplicationException;
	public Response getInvestigationsListForCoversheet(Integer patientId, boolean b, Integer unitId);
	public Response getInvestigationsListForNewEMR(Integer patientId, boolean b, Integer unitId);
	public Integer getDoctorShareVac(Integer orderDetailsId)throws ApplicationException;
	public Integer getReferShareVac(Integer orderDetailsId)throws ApplicationException;
	public Integer getDoctorShare(Integer orderDetailsId)throws ApplicationException;
	public Integer getReferShare(Integer orderDetailsId)throws ApplicationException;
	public Response updateDoctorShare(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)throws ApplicationException;
	public Response updateReferShare(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)throws ApplicationException;
	public void clearDoctor(OrderDetailsMasterDto orderDetailsMasterDto);
	public Response updateOrderDetailsForDoctorShare(OrderDetailsMaster toBeUpdated)throws ApplicationException;
	public Response updateDummyHiddenServiceFlag(String flag, int orderDetailsId, boolean value, boolean isPackage, int packageId, int admissionId);
	public boolean getDummyServiceFlagForPackageService(List<Integer> packageConsumptionMasterId);
	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperByOrderDetailsIdOptimise( List<Integer> orderDetailsIds)throws ApplicationException;
	public Response updateDoctorShareWithDoctorAndSpeciality(Integer orderDetailsId, OrderDetailsMaster toBeUpdated)throws ApplicationException;
	public Response updateOrderDetailsStatus(List<Integer> packIds, Integer billingMasterId);

    public List<AdmissionDto> getPatientsDetailsListByMobileNumber(AdmissionDto admissionDto) throws ApplicationException;
}
