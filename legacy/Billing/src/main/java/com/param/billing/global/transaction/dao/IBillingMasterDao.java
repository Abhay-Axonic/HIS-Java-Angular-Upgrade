package com.param.billing.global.transaction.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.smartcard.dto.Claim;
import com.param.billing.global.smartcard.dto.SmartAdmitionReqDto;
import com.param.billing.global.smartcard.dto.SmartCardBillReqDto;
import com.param.billing.global.smartcard.dto.SmartCardIItemsReqDto;
import com.param.billing.global.smartcard.dto.SmartCardPreauthReqDto;
import com.param.billing.global.smartcard.dto.SmartDiagnosisReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;
import com.param.billing.global.transaction.dto.TDoctorReferralShareDto;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.DischargeDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.SmartcardClaimDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface  IBillingMasterDao extends IGenericDao<BillingMaster, Integer>{
	public Response saveBillingMaster(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response getBillDetailsForPayment(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response updateBillSummaryAmount(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response getBillMasterById(Integer billMasterId)throws ApplicationException;
	public Response getAutoRenderedServicesByPatient(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getBillsByFilterForBillList(BillListReqDto reqDto)throws BillingException;
	public Response getActiveDiscountCategories(Integer orgId,Integer unitId)throws ApplicationException;
	Response getBillDetailsByBillId(Integer billId) throws ApplicationException;
	Response getRenderedServicesByEncounterIdForBillSave(Integer encounterId,Integer visitTypeId,Boolean debitNoteService, Character isPreBilling) throws ApplicationException;
	public Response getOrderedServicesByEncounterId(ServiceMasterDto serviceMasterDto);
	public Response updateDischargeStatusIfFinalBill(int admissionId,int statusId)throws BillingException;
	public Response updateBillMstIsDoctorReferralPostedByBillId(TDoctorReferralShareDto tDocShareVoucher) throws ServiceException;
	public Response billListByPatientId(int orgId, int unitId,int patientId,char isDrNote)throws ApplicationException;
	public Response getBillsForCreditNote(int orgId, int unitId,int patientId)throws ApplicationException;
	public Response getIntermediateBillDetailsByAdmissionId(int admissionId)throws BillingException;
	public Response updateOutStandingByBillIdForCompanyInvoiceSett(int billId, double amount,Integer updatedBy) throws BillingException;
	public Response updatePharmacyOutStandingByBillIdForCompanyInvoiceSett(int saleId, double amount,Integer updatedBy) throws BillingException ;//Ganesh
	public boolean checkOutstandingIsClearedForCompanyInvoice(int billId, double reciveAmount) throws BillingException;
	public Response getDebitNoteFilterList(BillListReqDto reqDto)throws BillingException;
	public Response getPatientServiceOrderList(AdmissionDto admissionDto)throws BillingException;
	public Response cancelUnRenderedServices(CancelOrderedServicesDto cancelOrderedServicesDto)throws BillingException;
	public Response updateBillStatusByBillId(Integer statusId, int billId)throws BillingException;
	
	public Response updateAddendumBillByBillId(BillingMasterDto billingMasterDto)throws BillingException;
	public Long getNextValGetBillId()throws BillingException;
	public Long generateRequestNumber()throws BillingException;
	Response updateBillDetails(BillingDetailsDto billingDetailsDto) throws BillingException;
	public Response billlistForBookView(Integer patientId,Integer unitId, Integer orgId, String fromDate, String toDate)throws BillingException;
	public Response readSmartCard(Claim claim)throws ApplicationException, SQLException, Exception;
	Integer updateFinalBill(Integer admssionId) throws ApplicationException;
	public BillPayeeDetailsDto getSelfPayeeAmount(Integer encounterId, Integer unitId, boolean debitNoteService)throws BillingException;;
	public List<BillPayeeDetailsDto> getCreditPayeeAmount(Integer encounterId, Integer unitId,boolean debitNoteService)throws BillingException;;
	public Response updateBillMasterIsInvoiceCreationByBillId(Integer billId, Integer payeeId, Integer assoiciateComId)throws BillingException;;

	/*public BillPayeeDetailsDto getSelfPayeeAmountForAdmission(Integer encounterId, Integer unitId)throws BillingException;;
	public List<BillPayeeDetailsDto> getCreditPayeeAmountForAdmission(Integer encounterId, Integer unitId)throws BillingException;*/
	public Response submitSmartCard(SmartcardClaimDto smartcardClaimDto)throws ApplicationException, SQLException;

	public BillPayeeDetailsDto getSelfPayeeAmountForAdmission(Integer encounterId, Integer unitId,boolean debitNoteService, Set<Integer> orderDetailsIdsList)throws BillingException;;
	public List<BillPayeeDetailsDto> getCreditPayeeAmountForAdmission(Integer encounterId, Integer unitId,boolean debitNoteService, Set<Integer> orderDetailsIdsList)throws BillingException;
	BillListResDto getBillSumsAdmissionId(Integer admissionId);
	
	
	public BillingMaster getBillById(Integer billId)throws ApplicationException;
	
	//SmartCardData Front
	public String getDischargeSummeryNotes(Integer admissionId,boolean isPharamcy);
	public String getPreAuthRequestDataByVisit(Integer visitId,Integer visitTypeId,boolean isPharamcy);
	public String getDiagnosisByVisitId(Integer visitId, Integer visitTypeId,boolean isPharamcy);
	public String getReceiptDetailsByBillid(Integer billId,boolean isPharamcy);
	public Integer updateSmartCardStatus(Integer billId,boolean isPharamcy);
	
	
	//SmartCardData BackEnd
	public List<SmartCardBillReqDto> getBillListForSmartCard(Date date);
	public List<SmartDiagnosisReqDto> getDiagnosis(Integer visitTypeId,Integer visitId);
	public List<SmartCardPreauthReqDto> getPreAuthReuest(Integer visitTypeId,Integer visitId);
	public SmartAdmitionReqDto getAdmissionDetails(Integer admissionId);
	public List<SmartCardIItemsReqDto> getItemsDetailsByBill(Integer billId,Integer payeeId);
	public List<PaymentRecieptMasterDto> getReceiptDetailsbyBill(Integer billId);
	public String getDischargeSummery(Integer admissionId);
	public Integer updateSmartCardSubmitStatus(Integer billId,boolean isPharamcy);
	
	
	//cancelDischarge
	public Integer cancelFinalBillForCancelDischarge(Integer billDetailsId,Integer orderDetailsId);
	public Integer inactivateBillbyBillId(Integer admissionId,Integer billId);
	public Integer getFinalbillByAdmissionId(Integer admissionId);
	public Response updateDisallowedAmountToSelfAmount(CompanyReceiptDetailDto receiptDetailsDto, Integer selfPayeeId);
	
	//pharmacy
	public Response updatePharmacyDisallowedAmountToSelfAmount(CompanyReceiptDetailDto receiptDetailsDto, Integer selfPayeeId);
	
	//GANESH
    Response checkBillSettledForCancelDischarge(DischargeDto dischargeDto);
    Response updatePDD(DischargeDto dischargeDto);
	Response canceDischarge(DischargeDto dischargeDto);
	public Response updateBillsDisallowedAmount(Double disallowedAmount, Integer billingMasterId);
	Response activateUnRenderedServices(CancelOrderedServicesDto cancelOrderedServicesDto) throws BillingException;
	public BillingMaster getBillMasterObject(Integer billId);
	Response getOrderedServicesByEncounterIdForPreBilling(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response updateBillDtlsForDocShare(Integer billingDetailsId, Integer doctorId, Integer docSpecialityId);
	
	public Response getBadDebitListByFilterForBadDebitList(BillListReqDto reqDto)throws BillingException;
	public Response changePatientClassId(ServiceMasterDto serviceMasterDto)throws BillingException;
	public Response billListByPatientIdForConcessionManager(int orgId, int unitId, int patientId);
	public Response inactivateBillAndBillDetails(BillingMaster billObj);
	public Response cancelReciept(BillingMasterDto billingMasterDto);
	public Response getBillListByFilterForbillCancellationList(BillListReqDto reqDto)throws BillingException;
	public Response getBillsByFilterForCancelledBillList(BillListReqDto reqDto)throws BillingException;
	Response updateIsBillReopened(Integer billId) throws ApplicationException;
	public Response printLog(int type, int userId, int billId);
	public Response receiptPrintLog(int type, int userId, int recieptId);
	public Response updatelabSampleDetailsStatusId(Integer billingMasterId)throws ApplicationException;
	public Response getSmartCardBalance(SmartcardClaimDto smartcardClaimDto)throws ApplicationException, SQLException;
	public void insertBillDataJson(Integer billingMasterId) throws SQLException;
	public Response getBillReferenceData(BillListReqDto reqDto)throws ApplicationException, SQLException;
	public BillListResDto getBillSumsEncounterId(Integer encounterId)throws ApplicationException, SQLException;
	public boolean matchBillAmounts(BillingMasterDto billingMasterDto, Set<Integer> orderDetailsIdList);

	public Response checkDischargeOrNot(DischargeDto dischargeDto)throws ApplicationException, SQLException;

	public void updateSaleTableBillstatus(BillingMasterDto billUpdateDto);
	public void saveBillStatusId(Integer object);


	public Integer getdiscountTypeIdBySaleId(Integer saleId);
	public Integer getcheckScreenIdAvailable(Integer unitId, Integer tt);
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto dto)throws ApplicationException;
	public void updatePharmacy(OrderDetailsMasterDto detailsByEncounterIdDto, Integer orderdetailsId, Integer paymentEntitlementId, Integer payeeId);
	public Response getFreeTextOrderServices(BillingMasterDto dto)throws ApplicationException;
	public boolean checkOutstandingIsClearedForCompanyInvoice1(Integer billId)throws ApplicationException;
	public void updateReceivedFrom(Object resObject, List<PaymentReceiptDetailsDto> list);
	public String getTallyInterfaceDataInJsonFormat(CabinMasterDto dto)throws ApplicationException;
	public String updateTallyInterfaceDataById(CabinMasterDto dto)throws ApplicationException;
	public Response getDataForCheckBillStatusByBillNumber(BillingMasterDto dto)throws ApplicationException;
	public Response updateRenderedDate(BillingMasterDto dto);
	public String getTallyInterfaceDataInJsonFormatByMode(CabinMasterDto dto)throws ApplicationException;
	public String updateTallyInterfaceDataByIdByMode(CabinMasterDto dto)throws ApplicationException;
	public Integer checkAppoinmentStatussss(SlotMasterDto dtott);
	public Response patientCouponTemplate(Integer patientCouponTemplateMapperId)throws ApplicationException;
	public Response getDataForFinanacialRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	public Response getOrderSumNetAndSelfCreditAmount(List<Integer> orderDetailsIds)throws ApplicationException;
	public Response checkInvoiceCreatedOrNotByBillId(TCompanyInvoiceCreationReqDto reqDto)throws ApplicationException;
	public Response getDataOfPatientToCheckDueAmountOfPharmacyService(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response getBillIdByEncounterId(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response getCapWithPackageBalanceAmount(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response checkCapWithPackageAndOrderAmount(BillingMasterDto billingMasterDto,
			TPackageConsumptionCapDetailsDto tPackageConsumptionCapDetailsDto)throws ApplicationException;
	public Response checkCapWithPackageAndConsumeAmount(BillingMasterDto billingMasterDto,
			TPackageConsumptionCapDetailsDto tPackageConsumptionCapDetailsDto)throws ApplicationException;
	public Response checkCapWithPackageBalanceAmountAndOrderAmount(BillingMasterDto billingMasterDto, String applicableDate)throws ApplicationException;
	public Response adjustCapWithPackageAvailableAmount(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response resetCapWithPackage(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response storePackCapAmountLog(OrderDetailsMasterDto detailsMasterDto)throws ApplicationException;
	public Response checkPackageMinusAmount(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response checkBillCancelledOrNot(BillingMasterDto billingMasterDto)throws ApplicationException;
	public Response getBillNoByPatientForMyLife(BillListReqDto billListReqDto) throws ApplicationException;
	public Response updateDoctorIdAndSpecialityId(AdmissionDto dto)throws ApplicationException;

	public Response updateOrderdetailsPackageMapper(Integer encounterId, Integer orgId, Integer unitId,Integer visitTypeId)throws ApplicationException;

    public Response getBillDetailsByListOfBillIds(List<Integer> billMasterIds) throws ApplicationException;
}
