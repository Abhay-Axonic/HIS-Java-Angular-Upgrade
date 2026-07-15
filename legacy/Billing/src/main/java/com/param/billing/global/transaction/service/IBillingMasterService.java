package com.param.billing.global.transaction.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.dto.EncounterPaymentDto;
import com.param.billing.global.smartcard.dto.Claim;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.CancelOrderDto;
import com.param.global.dto.DischargeDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.SmartcardClaimDto;
import com.param.global.dto.TPackageConsumptionMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UpdatePayeeDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IBillingMasterService {
	
	public Response updateBillSummaryAmount(BillingMasterDto billingMasterDto)throws ApplicationException;
	
	public Response getAutoRenderedServicesByPatient(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	
	public Response getBillsByFilterForBillList(BillListReqDto reqDto)throws BillingException;
	
	public Response getDebitNoteFilterList(BillListReqDto reqDto)throws BillingException;
	
	public Response getOrderedServicesByEncounterId(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	
	public Response saveBill(BillingMasterDto billingMasterDto)throws ApplicationException;
	
	public Response getActiveDiscountCategories(Integer orgId, Integer unitId)throws ApplicationException;

	public Response updatePackageConsumptionQty(BillingMasterDto billDto, List<OrderDetailsMasterDto> listToBeAdded)throws ApplicationException;
	
	public Response collectPayment(BillPaymentCollectionReqDto reqDto)throws ApplicationException;
	
	public Response billListByPatientId(int orgId, int unitId,int patientId,char isDrNote)throws ApplicationException;

	public Response getBillsForCreditNote(int orgId, int unitId,int patientId)throws ApplicationException;
	
	public Response getIntermediateBillDetailsByAdmissionId(int admissionId)throws BillingException;

	public Response getPatientServiceOrderList(AdmissionDto admissionDto)throws BillingException;

	public Response cancelUnRenderedServices(List<CancelOrderedServicesDto> cancelOrderedServicesDtoList)throws BillingException;
	
	public Response updateBillStatusByBillId(Integer statusId, int billId)throws BillingException;

	public Response saveBillFromKiosk(EncounterMasterDto encounterMasterDto)throws BillingException;

	Response saveBillService(BillingMasterDto billingMasterDto) throws ApplicationException, Exception;
	
//	public Response billlistForBookView(Integer patientId,Integer unitId, Integer orgId)throws BillingException;
	public Response billlistForBookView(Integer patientId,Integer unitId, Integer orgId, String fromDate, String toDate)throws BillingException;

	public Response readSmartCard(Claim claim)throws ApplicationException, Exception;

	public Response collectPaymentFromEncounter(EncounterPaymentDto encounterMasterDto, Character cashSettlement)throws ApplicationException, Exception;

	public Response submitSmartCard(SmartcardClaimDto smartcardClaimDto)throws ApplicationException, Exception;

	Response saveIntrimBill(BillingMasterDto billingMasterDto) throws ApplicationException;

	Response updatePayee(UpdatePayeeDto billingMasterDto) throws ApplicationException, Exception;

	Response updateOrderDetailsForPayee(UpdatePayeeDto billingMasterDto,List<OrderDetailsByEncounterIdDto> list,UnitApplicationConfigurationDto configDto) throws ApplicationException, Exception;

    Response cancelAutoPostingAndOrederServicesByAdmission(DischargeDto dto);
    
    
  //Ganesh 01-07-19
  	//public Response generateBackOfficeDebitNote(CompanyInvoiceSettlementReqDto reqDto);
  	
	Response cancelDischarge(DischargeDto dischargeDto);
	
	Response updateDischarge(DischargeDto dischargeDto);
	
	Response packageConsumption(BillingMasterDto billingMasterDto,List<OrderDetailsMasterDto> listToBeAdded, Response orderRes) throws ApplicationException, Exception;

	public Response postServiceToEncounterBill(OrderDetailsMaster orderDetailsMasterDto,AdmissionsForBatchProcessDto vistDetailsObj);

	Response getOrderedServicesByEncounterIdForPreBilling(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	

	Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId, Integer visitTypeId,
			boolean debitNoteService, Integer unitId, Set<Integer> orderDetailsIdsList, Character isPreBilling)
			throws ApplicationException;

	public Response saveBillFromKioskFutureDate(EncounterMasterDto encounterMasterDto);
	
	//Check Package Contract
	public Response orderPackageOnSelection(BillingMasterDto billDto) throws ApplicationException, Exception;
	public Response updatePackageCoverSheet(BillingMasterDto billDto,Character isPreOrPostBilling,boolean isUpdatePayee,boolean isForPreAuth) throws ApplicationException, Exception;

	public Response cancelPackage(BillingMasterDto billingMasterDto,List<OrderDetailsMasterDto> listToBeUpDated);

	
	
	public Response getBadDebitListByFilterForBadDebitList(BillListReqDto reqDto)throws BillingException;

	public Response requestForBadDebit(BillingMasterDto billingMasterDto)throws BillingException;
	
	public Response saveBillCycylicAppoinment(EncounterMasterDto encounterMasterDto,List<OrderDetailsMasterDto> orderDetailsMasterDtoList);
	
	Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId,Integer visitTypeId,Boolean debitNoteService, Character isPreBilling) throws ApplicationException;
	
	public Response changePatientClassId(ServiceMasterDto serviceMasterDto)throws BillingException;

	Response getOrderDetailsPayeeData(Integer orderDetailsId) throws ApplicationException;

	public Response generateCyclicServiceAppointment(List<SlotMasterDto> slotMasterDto);
	
	public Response updateBillFromConcessionManager(BillingMasterDto billingMasterDto);

	public Response billListByPatientIdForConcessionManager(int orgId, int unitId, int patientId);

	public Response cancelBill(BillingMasterDto billingMasterDto);

	public Response cancelReciept(BillingMasterDto billingMasterDto);
	public Response getBillListByFilterForbillCancellationList(BillListReqDto reqDto)throws BillingException;

	public Response getBillsByFilterForCancelledBillList(BillListReqDto reqDto)throws BillingException;
	
	public Response updatePdd(DischargeDto dischargeDto);
	
	public Response cancelPendingService(CancelOrderDto cancelOrderDto);
	
	public Response cancelPackageFromPendingService(BillingMasterDto billDto);

	String generateRequestNumber();

	public Response printLog(int type, int userId, int billId);

	public Response receiptPrintLog(int type, int userId, int recieptId);

	public Response getSmartCardBalance(SmartcardClaimDto smartcardClaimDto)throws ApplicationException, Exception;

	public Response getBillReferenceData(BillListReqDto reqDto)throws ApplicationException, Exception;

	public BillListResDto getBillSumsEncounterId(Integer encounterId)throws ApplicationException, Exception;

	public Response updatePackageEffectiveDate(TPackageConsumptionMasterDto tPackageConsumptionMasterDto);

	public Response checkDischargeOrNot(DischargeDto dischargeDto)throws ApplicationException, Exception;

	public void saveBillStatusId(Integer object);
	public Response printReceiptRPTReport(int orgId, int unitId, int billingMasterId, int receiptId, int depositConsumptionId)throws ApplicationException;

	public Response updateAdvisedBy(BillingMasterDto billingMasterDto)throws ApplicationException;

	public boolean matchBillAmounts(BillingMasterDto billingMasterDto, Set<Integer> orderDetailsIdList);

	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto dto)throws ApplicationException;

	public Response getFreeTextOrderServices(BillingMasterDto dto)throws ApplicationException;

	public void updateReceivedFrom(Object resObject, List<PaymentReceiptDetailsDto> list);

	public String getTallyInterfaceDataInJsonFormat(CabinMasterDto dto)throws ApplicationException;

	public String updateTallyInterfaceDataById(CabinMasterDto dto) throws ApplicationException;

	public Response getDataForCheckBillStatusByBillNumber(BillingMasterDto dto)throws ApplicationException;

	public Response updateRenderedDate(BillingMasterDto dto);

	public String getTallyInterfaceDataInJsonFormatByMode(CabinMasterDto dto)throws ApplicationException;

	public String updateTallyInterfaceDataByIdByMode(CabinMasterDto dto)throws ApplicationException;

	public Response patientCouponTemplate(Integer patientCouponTemplateMapperId)throws ApplicationException;

	public Response getDataForFinanacialRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	public Response getOrderSumNetAndSelfCreditAmount(List<Integer> orderDetailsIds)throws ApplicationException;

	public Response getDataOfPatientToCheckDueAmountOfPharmacyService(BillingMasterDto billingMasterDto)throws ApplicationException;
	
	public Response getBillIdByEncounterId(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response checkCapWithPackageBalanceAmount(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response checkCapWithPackageBalanceAmountAndOrderAmount(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response adjustCapWithPackageAvailableAmount(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response resetCapWithPackage(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response storePackCapAmountLog(OrderDetailsMasterDto detailsMasterDto)throws ApplicationException;

	public Response checkPackageMinusAmount(BillingMasterDto billingMasterDto)throws ApplicationException;

	public Response checkBillCancelledOrNot(BillingMasterDto billingMasterDto)throws ApplicationException;


   public Response getBillNoByPatientForMyLife(BillListReqDto billListReqDto) throws ApplicationException;

   public Response updateDoctorIdAndSpecialityId(AdmissionDto dto)throws ApplicationException;

	public Response updateOrderdetailsPackageMapper(Integer encounterId, Integer orgId, Integer unitId, Integer visitTypeId)throws ApplicationException;

	public Response getOrderDetailsPackageMapperDetails(List<Integer> orderdId)throws ApplicationException;

    public Response orderedServicesByMobileNoForWebUser(AdmissionDto admissionDto) throws ApplicationException;
}
