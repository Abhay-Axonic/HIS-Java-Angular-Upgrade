package com.param.billing.global.transaction.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.common.*;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.*;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.global.unit.service.IUserRoleMasterService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.JsonCheck;
import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.billing.global.CostEstimateDto;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.dto.BillListResDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.dto.EncounterPaymentDto;
import com.param.billing.global.smartcard.dto.Claim;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dto.BillRecieptDto;
import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.global.transaction.service.IBillPayeeDetailsService;
import com.param.billing.global.transaction.service.IBillingDetailsService;
import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.billing.ipd.service.IpdChargesService;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.billing.unit.service.ITPeriodicityServiceOrdersService;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.billing.service.IBillingService;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.billing.service.IPatientCouponTemplateMapperService;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.procedure.service.IBillOfMaterialService;
import com.param.global.service.IDoctorConsultationServiceMapperService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsDiscountService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.IRazorPayService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.service.sync.IAppointmentService;
import org.apache.log4j.Logger;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ILabConfigurationService;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class BillingMasterController implements ICommonConstants, IBillingStatusMasterConstant {

	// Added 2026-07-15 alongside the Razorpay payment-verification check below -- this class
	// had no logger of any kind before. See docs/REVIEW_DRAFT_payment-fixes-FINAL.md sec 3.
	final static Logger logger = Logger.getLogger(BillingMasterController.class);

	@Autowired
	private IRazorPayService razorPayService;

	@Autowired
	private IBillingMasterService iBillingMasterService;
	@Autowired
	private IBillPayeeDetailsService iBillPayeeDetailsService;
	@Autowired
	private IOrderDetailsDiscountService iOrderDetailsDiscountService;
	@Autowired
	IBillingService globalBillingService;
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IBillOfMaterialService iBillOfMaterialService;
	@Autowired
	private IpdChargesService ipdChargesService;
	@Autowired
	private IEncounterMasterService iEncounterMasterService;
	@Autowired
	private IBillingDetailsService iBillingDetailsService;
	@Autowired
	private IWalkInSalesService iWalkInSalesService;
	@Autowired
	private IAppointmentService iAppointmentService;
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService; 
	@Autowired
	IOrderMasterService iOrderMasterService;
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	@Autowired
	private IBillingMasterDao billingMasterDao;
	@Autowired
	IPatientRegistrationService iPatientRegistrationService;
	@Autowired
	IDoctorConsultationServiceMapperService iDoctorConsultationServiceMapperService;
	@Autowired
	ILabConfigurationService iLabConfigurationService;
	@Autowired
	private IDepositMasterService iDepositMasterService;
	@Autowired
	private IPatientCouponTemplateMapperService iPatientCouponTemplateMapperService;
	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	@Autowired
	ITPeriodicityServiceOrdersService iTPeriodicityServiceOrdersService;
	@Autowired
	private BillingCommonMethods billingCommonMethods;

	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private Gson gson;

	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	IUserRoleMasterService iUserRoleMasterService;

	@RequestMapping(value = "saveBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveBill(@RequestBody BillingMasterDto billingMasterDto,HttpServletRequest request) throws Exception {
		try {

			// JsonCheck.mapJsonToObject(billingMasterDto,BillingMasterDto.class);

			if (billingMasterDto.getPaymentReceiptDetailsDtosList() != null) 
			{
				for (PaymentReceiptDetailsDto dto : billingMasterDto.getPaymentReceiptDetailsDtosList()) 
				{
					if (dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2) 
					{
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if (resCcavenue.getObject() == null) 
						{
							return new Response<>(ERROR, ERROR_CODE,dto.getTransactionNo() + " This already used in other trancation", null, null);
						}
					}
				}
			}

			List<Integer> orderDetailsIds = new ArrayList<>();
			Double netAmount = 0d;
			Double selfPayable = 0d;
			Double creditPayable = 0d;
			for (OrderDetailsMasterDto dto : billingMasterDto.getOrderDetailsMasterDtosList()) {
				if(dto.getOrderDetailsId() != null) {
					orderDetailsIds.add(dto.getOrderDetailsId());
					
					netAmount = netAmount + (dto.getNetAmount() != null ? dto.getNetAmount().doubleValue(): 0d);
					
					selfPayable = selfPayable + (dto.getSelfPayable() != null ?  dto.getSelfPayable().doubleValue(): 0d);
					
					creditPayable = creditPayable + (dto.getCreditPayable() != null ?  dto.getCreditPayable().doubleValue(): 0d);
				}
				
			}
			
			netAmount = (double) Math.round(netAmount);
			
			selfPayable = (double) Math.round(selfPayable);
			
			creditPayable = (double) Math.round(creditPayable);
			
			Response orderDetailsRes = iBillingMasterService.getOrderSumNetAndSelfCreditAmount(orderDetailsIds);
			Boolean checkSameAmount = true;
			if(orderDetailsRes.getObject() != null){
				
				OrderDetailsMasterDto dto = (OrderDetailsMasterDto) orderDetailsRes.getObject();
				
				Double netAmountSave = 0d;
				Double selfPayableSave = 0d;
				Double creditPayableSave = 0d;
				
				netAmountSave = (double) Math.round(dto.getNetAmount().doubleValue());
				
				selfPayableSave = (double) Math.round(dto.getSelfPayable()!= null ? dto.getSelfPayable().doubleValue() : 0d);
				
				creditPayableSave = (double) Math.round(dto.getCreditPayable() != null ? dto.getCreditPayable().doubleValue() : 0d);
				
				if(billingMasterDto.isSaveFromSplitBill() == true){
					
					if( Integer.parseInt(String.valueOf(netAmountSave).split("\\.")[0])   == Integer.parseInt(String.valueOf(netAmount).split("\\.")[0]) ){
						
						checkSameAmount = false;
						
					}else{
						
						double diffNetamount = netAmountSave - netAmount;
						
						if(diffNetamount <=1 && diffNetamount >= -1 ){
							checkSameAmount = false;
						}
						
					}
					
					
				}else{
					
					if( (Integer.parseInt(String.valueOf(selfPayableSave).split("\\.")[0])  == Integer.parseInt(String.valueOf(selfPayable).split("\\.")[0]) )
							&& (Integer.parseInt(String.valueOf(creditPayableSave).split("\\.")[0])  == Integer.parseInt(String.valueOf(creditPayable).split("\\.")[0]))){
						checkSameAmount = false;
					}else{
						
						
						double diffselfPayable = selfPayableSave - selfPayable;
						double diffcreditPayable = creditPayableSave - creditPayable;
						
						if((diffselfPayable <=1 && diffselfPayable >= -1 ) && (diffcreditPayable <=1 && diffcreditPayable >= -1 )){
							checkSameAmount = false;
						}
						
					}
				}
				
			}
			
			if(orderDetailsRes.getObject() == null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, "Unable to verify this bill's current amount -- please retry, or contact support if this persists.", null, null);
			}

			if(checkSameAmount){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, "This bill is already in process by other user!", null, null);
			}
			
			
			BillRecieptDto obj = null;
			Response billRes = iBillingMasterService.saveBillService(billingMasterDto);
			
			if(billRes.getStatus().equalsIgnoreCase(SUCCESS) && billRes.getObject()!= null) {
				Integer billId = (Integer) billRes.getObject();
				

				 List<Integer> orderIdList = new ArrayList<>();
				for (OrderDetailsMasterDto orderDetailsId : billingMasterDto.getOrderDetailsMasterDtosList()) {
					orderIdList.add(orderDetailsId.getOrderId());
				}
				
				Response resCop = iPatientCouponTemplateMapperService.getpatientCouponMapperByOrderId(orderIdList);
				if(resCop.getListObject() != null && resCop.getListObject().size()>0 ) {
					
					List<CouponTemplateConsumeDetailsDto> list =  resCop.getListObject();
					
					for (CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto : list) {
						couponTemplateConsumeDetailsDto.setUnitId(billingMasterDto.getUnitId());
						couponTemplateConsumeDetailsDto.setOrganizationId(billingMasterDto.getOrganisationId());
						couponTemplateConsumeDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
						couponTemplateConsumeDetailsDto.setConsumeAmount(couponTemplateConsumeDetailsDto.getOrdDiscount().doubleValue());
					}
					
					iPatientCouponTemplateMapperService.savePatientConsumptionData(list,billId);
				}
				
			}
			
			if (billRes.getStatus().equalsIgnoreCase(SUCCESS) && billRes.getResObject() != null) 
			{
				BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) billRes.getResObject();
				reqDto.setIsPaidAppointment('N');
				reqDto.setBillNumber(billingMasterDto.getBillNumber() != null ? billingMasterDto.getBillNumber() : null);
				reqDto.setReceivedFrom(billingMasterDto.getReceivedFrom());
				Response recieptRes = iBillingMasterService.collectPayment(reqDto);
				
				Integer recieptId = null;
				Integer depositConsumptionId = null;
				if (recieptRes.getObject() != null) 
				{
					obj = (BillRecieptDto) recieptRes.getObject();
					recieptId = obj.getRecieptId();
					depositConsumptionId = obj.getDepositConsumptionId();
				}
				
				if (billRes.getStatus().equals(SUCCESS) 
						&& reqDto.getIsInterimOrFinalBill() != null
							&& reqDto.getIsInterimOrFinalBill() == 2 
								&& reqDto.getVisitTypeId() == OP) 
				{
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					BillListResDto dto1 = iBillingMasterService.getBillSumsEncounterId(billingMasterDto.getEncounterId());
					emailinfo.setRecieptId(recieptId);
					emailinfo.setRecieptNo(obj != null ? obj.getReceiptNo() : "");
					emailinfo.setBillId((Integer) billRes.getObject());
					emailinfo.setPatientId(reqDto.getPatientId());
					emailinfo.setDepositeConsumptionId(depositConsumptionId);
					emailinfo.setBillNo(billingMasterDto.getBillNumber());
					emailinfo.setUnitId(billingMasterDto.getUnitId());
					emailinfo.setOrganizationId(billingMasterDto.getOrganisationId());

					if (billingMasterDto.getPaymentEntitlementId() != null
							&& billingMasterDto.getPaymentEntitlementId() == INSURANCE) 
					{

						emailinfo.setCompanyId(null);
						emailinfo.setInsuranceId(billingMasterDto.getPayeeId() != null ? billingMasterDto.getPayeeId() : null);

					} else if (billingMasterDto.getPaymentEntitlementId() != null
							&& billingMasterDto.getPaymentEntitlementId() == COMPANY) 
					{
						emailinfo.setCompanyId(billingMasterDto.getPayeeId() != null ? billingMasterDto.getPayeeId() : null);
						emailinfo.setInsuranceId(null);

					} else {
						emailinfo.setCompanyId(null);
						emailinfo.setInsuranceId(null);
					}
					if (dto1 != null) {
						emailinfo.setBillAmount(new BigDecimal(dto1.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						emailinfo.setSelfAmount(new BigDecimal(dto1.getTotalSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						emailinfo.setCreditAmount(new BigDecimal(dto1.getTotalCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						emailinfo.setOutstandingAmount(new BigDecimal(dto1.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					} else {
						emailinfo.setBillAmount(new BigDecimal(0.00));
						emailinfo.setSelfAmount(new BigDecimal(0.00));
						emailinfo.setCreditAmount(new BigDecimal(0.00));
						emailinfo.setOutstandingAmount(new BigDecimal(0.00));
					}

					emailinfo.setDateTime(reqDto.getDateOfReciept());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.OPBILL, emailinfo);
				}

				BillListResDto dto = null;
				if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
						&& reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() != OP) 
				{
					dto = billingMasterDao.getBillSumsAdmissionId(reqDto.getAdmissionId());

					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(reqDto.getUnitId());
						smsInfo.setBillId((Integer) billRes.getObject());
						smsInfo.setPatientId(reqDto.getPatientId());
						smsInfo.setTreatingDoctorId(reqDto.getDoctorId());
						smsInfo.setBillAmount(new BigDecimal(dto.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						smsInfo.setSelfAmount(new BigDecimal(dto.getTotalSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						smsInfo.setCreditAmount(new BigDecimal(dto.getTotalCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						smsInfo.setOutstandingAmount(new BigDecimal(dto.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						smsInfo.setDateTime(reqDto.getDateOfReciept());
						smsInfo.setReferringDoctorId(reqDto.getReferalDocId());
						smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.IP_FINAL_BILL, smsInfo);
				}

				if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() == 1
						&& reqDto.getVisitTypeId() != OP) {

					BigDecimal roundOffNetAmount = new BigDecimal(reqDto.getAmount()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
					BigDecimal roundOffSelfAmount = new BigDecimal(reqDto.getTotalSelfPayable()).setScale(2,BigDecimal.ROUND_HALF_EVEN);
					BigDecimal roundOffCreditAmount = new BigDecimal(reqDto.getTotalCreditPayable()).setScale(2,BigDecimal.ROUND_HALF_EVEN);

					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(reqDto.getUnitId());
						smsInfo.setPatientId(reqDto.getPatientId());
						smsInfo.setConsultingDoctorId(reqDto.getDoctorId());
						smsInfo.setBillAmount(roundOffNetAmount);
						smsInfo.setSelfAmount(roundOffSelfAmount);
						smsInfo.setCreditAmount(roundOffCreditAmount);
						smsInfo.setDateTime(reqDto.getDateOfReciept());
						smsInfo.setReferringDoctorId(reqDto.getReferalDocId());
						smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.INTERMEDIATE_BILL, smsInfo);
				}

				if (billingMasterDto.getAdvisedById() != null || billingMasterDto.getAdvisedByName() != null) 
				{
					iBillingMasterService.updateAdvisedBy(billingMasterDto);
				}

				if (billRes.getStatus().equals(SUCCESS) &&  recieptRes.getStatus().equals(SUCCESS) && billRes.getObject()!=null) 
				{
					iBillingMasterService.updateReceivedFrom(billRes.getObject(),billingMasterDto.getPaymentReceiptDetailsDtosList());
					return new Response<>(SUCCESS, SUCCESS_CODE, billRes.getMessage(), null, billRes.getObject(),(BillRecieptDto) recieptRes.getObject());
				}
				iBillingMasterService.saveBillStatusId((Integer) billRes.getObject());
			}
			if(billRes.getStatus().equals(SUCCESS) && billRes.getObject()!=null)
			{
				iBillingMasterService.updateReceivedFrom(billRes.getObject(),billingMasterDto.getPaymentReceiptDetailsDtosList());
                //// Sneha 
				if (billingMasterDto.getVisitTypeId() == OP) {
				
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setBillId((Integer) billRes.getObject());
				smsInfo.setPatientId(billingMasterDto.getPatientId());
				smsInfo.setTreatingDoctorId(billingMasterDto.getOrdDoctorId());
				smsInfo.setBillAmount(new BigDecimal(billingMasterDto.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setSelfAmount(
						new BigDecimal(billingMasterDto.getSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setCreditAmount(
						new BigDecimal(billingMasterDto.getCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setOutstandingAmount(
						new BigDecimal(billingMasterDto.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				//smsInfo.setDateTime(reqDto.getDateOfReciept());
				smsInfo.setReferringDoctorId(billingMasterDto.getReferralId() != null ? billingMasterDto.getReferralId() : 0);
				smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
				smsInfo.setOrgId(billingMasterDto.getOrganisationId());
				smsInfo.setUnitId(billingMasterDto.getUnitId());
				
				String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(billingMasterDto.getUnitId());
 				
                   int lowerLimit = 97;
                   int upperLimit = 122;
                   Random random = new Random();
                   StringBuffer r = new StringBuffer(10);
                   for (int i = 0; i < 10; i++) {
                       int nextRandomChar = lowerLimit+ (int)(random.nextFloat() * (upperLimit - lowerLimit + 1));
                       r.append((char)nextRandomChar);
                   }
                   
                   String str= "";
                   if(resPublicUrl == null) {
                   	str= ServletUtils.getBaseUrl(request)+"/api/billing/BillList/"+smsInfo.getBillId()+"/"+smsInfo.getUnitId()
               		+"/"+smsInfo.getOrganizationId()+"/"+billingMasterDto.getVisitTypeId()+"/"+r.toString();  
                   }else {
                   	str= resPublicUrl+"/api/billing/BillList/"+smsInfo.getBillId()+"/"+smsInfo.getUnitId()
               		+"/"+smsInfo.getOrganizationId()+"/"+billingMasterDto.getVisitTypeId()+"/"+r.toString(); 
                   }
                   String urlStringMsg="";
   				    String urlString="";
   					String mailLink="<a href='"+str+"'>"+str+" <a></br>";
   					 urlString +=mailLink;
   					String sms=str;
   					urlStringMsg =sms;
   					smsInfo.setReportMsgUrl(urlStringMsg);
   					smsInfo.setReportUrl(urlString);
   					
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.BILLLISTOP, smsInfo);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.BILLLISTOP, smsInfo);
			}else {
				
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setBillId((Integer) billRes.getObject());
				smsInfo.setPatientId(billingMasterDto.getPatientId());
				smsInfo.setTreatingDoctorId(billingMasterDto.getOrdDoctorId());
				smsInfo.setBillAmount(new BigDecimal(billingMasterDto.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setSelfAmount(
						new BigDecimal(billingMasterDto.getSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setCreditAmount(
						new BigDecimal(billingMasterDto.getCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setOutstandingAmount(
						new BigDecimal(billingMasterDto.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				//smsInfo.setDateTime(reqDto.getDateOfReciept());
				smsInfo.setReferringDoctorId(billingMasterDto.getReferralId() != null ? billingMasterDto.getReferralId() : 0);
				smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
				smsInfo.setOrgId(billingMasterDto.getOrganisationId());
				smsInfo.setUnitId(billingMasterDto.getUnitId());
				
				String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(billingMasterDto.getUnitId());
 				
                   int lowerLimit = 97;
                   int upperLimit = 122;
                   Random random = new Random();
                   StringBuffer r = new StringBuffer(10);
                   for (int i = 0; i < 10; i++) {
                       int nextRandomChar = lowerLimit+ (int)(random.nextFloat() * (upperLimit - lowerLimit + 1));
                       r.append((char)nextRandomChar);
                   }
                   
                   String str= "";
                   if(resPublicUrl == null) {
                   	str= ServletUtils.getBaseUrl(request)+"/api/opd/BillList/"+smsInfo.getBillId()+"/"+smsInfo.getUnitId()
               		+"/"+smsInfo.getOrganizationId()+"/"+billingMasterDto.getVisitTypeId()+"/"+r.toString();  
                   }else {
                   	str= resPublicUrl+"/api/opd/BillList/"+smsInfo.getBillId()+"/"+smsInfo.getUnitId()
               		+"/"+smsInfo.getOrganizationId()+"/"+billingMasterDto.getVisitTypeId()+"/"+r.toString(); 
                   }
                   String urlStringMsg="";
   				    String urlString="";
   					String mailLink="<a href='"+str+"'>"+str+" <a></br>";
   					 urlString +=mailLink;
   					String sms=str;
   					urlStringMsg =sms;
   					smsInfo.setReportMsgUrl(urlStringMsg);
   					smsInfo.setReportUrl(urlString);
   					
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.BILLLISTIP, smsInfo);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.BILLLISTIP, smsInfo);
			}
				
				
				
			}
			//if(billRes.getStatus().equalsIgnoreCase(SUCCESS))
			//	iTDoctorShareService.getProcessDocShare(billingMasterDto.getUnitId());
			
			return billRes;

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/intrimBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveIntrimBill(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			
			Set<Integer> orderDetailsIdList = billingMasterDto.getOrderDetailsMasterDtosList()
					.stream().map(OrderDetailsMasterDto::getOrderDetailsId).collect(Collectors.toSet());
			orderDetailsIdList.remove(null);
			boolean isbillAmountsMatched = iBillingMasterService.matchBillAmounts(billingMasterDto,orderDetailsIdList);
			if(!isbillAmountsMatched)
				return new Response<>(ERROR, ALREADY_EXIST_CODE, "Dispute in orders count, refreshing orders !", null, (!isbillAmountsMatched));
			 
			return iBillingMasterService.saveIntrimBill(billingMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/**
	 * Save Bill Payee Details
	 * 
	 * @param billPayeeDetailsDto
	 * @return
	 */
	@RequestMapping(value = "billPayeeDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBillPayeeDetails(@RequestBody BillPayeeDetailsDto billPayeeDetailsDto) {
		try {
			return iBillPayeeDetailsService.saveBillPayeeDetails(billPayeeDetailsDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/**
	 * get Auto Rendered Services By Patient
	 * 
	 * @param serviceMasterDto
	 * @return
	 */
	@RequestMapping(value = "/serviceMaster/autoRendered", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAutoRenderedServicesByPatient(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iBillingMasterService.getAutoRenderedServicesByPatient(serviceMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/**
	 * get Ordered Services By EncounterId
	 * 
	 * @param serviceMasterDto
	 * @return
	 */

	@RequestMapping(value = "/serviceMaster/orderedServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderedServicesByEncounterId(@RequestBody ServiceMasterDto serviceMasterDto) throws Exception {
		try {
			Response res =  iBillingMasterService.getOrderedServicesByEncounterId(serviceMasterDto);
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = new ArrayList<>();
			if(res.getListObject()!= null && !res.getListObject().isEmpty()){
				orderDetailsByEncounterIdDtosList =res.getListObject();
				List<Integer> orderdId = new ArrayList<>();
				//List<OrderDetailsByEncounterIdDto> orderDetailsIdDtosList = new ArrayList<>();
				for (OrderDetailsByEncounterIdDto dto :orderDetailsByEncounterIdDtosList){
					if(dto.getOrderDetailsPackageMapperDtosList() == null || dto.getOrderDetailsPackageMapperDtosList().isEmpty()){
						orderdId.add(dto.getOrderDetailsId());
						//orderDetailsIdDtosList.add(dto);
					}
				}

				if(!orderdId.isEmpty()){
					iBillingMasterService.updateOrderdetailsPackageMapper(serviceMasterDto.getEncounterId(),serviceMasterDto.getOrgId(),serviceMasterDto.getUnitId(),serviceMasterDto.getVisitTypeId());
/*
					 BillingMasterDto billingMasterDto = new BillingMasterDto();
					billingMasterDto.setPatientId(orderDetailsIdDtosList.get(0));
					 updateService(billingMasterDto);*/

					Response packageMpprRes = iBillingMasterService.getOrderDetailsPackageMapperDetails(orderdId);
					List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();

					for (OrderDetailsByEncounterIdDto orderDetailsByEncounterIdDto : orderDetailsByEncounterIdDtosList)
					{

						Stream<OrderDetailsPackageMapperDto> orderDetailsPackageStream = mpprList.stream();
						List<OrderDetailsPackageMapperDto> orderDetailsPackageList = orderDetailsPackageStream
								.filter(str -> str.getOrderDetailsId().equals(orderDetailsByEncounterIdDto.getOrderDetailsId()))
								.collect(Collectors.toList());
						if(orderDetailsPackageList != null && !orderDetailsPackageList.isEmpty()){
							orderDetailsByEncounterIdDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageList);
							orderDetailsByEncounterIdDto.setServiceUpdateforPackage(1);
						}


					}

				}

			}
			return new Response<>(SUCCESS, null, null, orderDetailsByEncounterIdDtosList, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/service/preBilling/orderedServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderedServicesByEncounterIdForPreBilling(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iBillingMasterService.getOrderedServicesByEncounterIdForPreBilling(serviceMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/**
	 * get Bills By Filter For Bill List
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/billlist/filter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillsByFilterForBillList(@RequestBody BillListReqDto reqDto) {
		try {
			return iBillingMasterService.getBillsByFilterForBillList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * get Bill Payee Details By Bill Cr.
	 * 
	 * @param billId
	 * @return
	 */
	@RequestMapping(value = "cr/payee/bill/{billId}/{visitTypeId}/{admissionId}", method = RequestMethod.GET)
	public Response getBillPayeeDetailsByBillIdCr(@PathVariable("billId") int billId,
			@PathVariable("visitTypeId") int visitTypeId, @PathVariable("admissionId") int admissionId) {
		try {
			return iBillPayeeDetailsService.getBillPayeeDetailsByBillIdCr(billId, visitTypeId, admissionId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * get Bill Payee By Bill Id Credit Note
	 * 
	 * @param billId
	 * @return
	 */

	@RequestMapping(value = "/payee/bill/{billId}", method = RequestMethod.GET)
	public Response getBillPayeeByBillIdCreditNote(@PathVariable("billId") int billId) {
		try {
			return iBillPayeeDetailsService.getBillPayeeDetailsByBillId(billId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		//	throw new BillingException(e.getMessage());
		}
	}

	/**
	 * 
	 * Collect payment
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/payments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response collectPayment(@RequestBody BillPaymentCollectionReqDto reqDto) {
		try {
			
			if(reqDto.getPaymentReceiptDetailsDtosList()!= null) 
			{
				for (PaymentReceiptDetailsDto dto : reqDto.getPaymentReceiptDetailsDtosList()) 
				{
					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) 
					{
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) 
						{
							return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
				}
			}
			Response res = iBillingMasterService.collectPayment(reqDto);
			
			//if(res.getStatus().equalsIgnoreCase(SUCCESS))
				//iTDoctorShareService.getProcessDocShare(reqDto.getUnitId());
			
			return res;
			
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * get Active Discount Categories
	 * 
	 * @param orgId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/discountCategories/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public Response getActiveDiscountCategories(@PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId) {
		try {
			return iBillingMasterService.getActiveDiscountCategories(orgId, unitId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * Save Cancel Discount
	 * 
	 * @param billingMasterDto
	 * @return
	 */

	@RequestMapping(value = "/cancelDiscount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelDiscount(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iOrderDetailsDiscountService.saveCancelledDiscounts(billingMasterDto.getCancelledDiscountsList());
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * Cancel Un Rendered Services
	 * 
	 * @param cancelOrderedServicesDtoList
	 * @return
	 */
	@RequestMapping(value = "/cancelUnRenderedServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelBilledService(@RequestBody CancelOrderedServicesDto cancelOrderedServicesDtoList) {
		try {
			return iBillingMasterService.cancelUnRenderedServices(cancelOrderedServicesDtoList.getOrderedServicesDtosList());
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * get Bill List By Patient Id
	 * 
	 * @param params debitCreditNote
	 * @return
	 */
	@RequestMapping(value = "/billList/{orgId}/{unitId}/{patientId}/{isDrNote}", method = RequestMethod.GET)
	public Response billListByPatientId(@PathVariable Map<String, String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int patientId = Integer.parseInt(params.get("patientId").trim());
			char isDrNote = params.get("isDrNote").trim().charAt(0);
			return iBillingMasterService.billListByPatientId(orgId, unitId, patientId, isDrNote);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/creditNote/bills/{orgId}/{unitId}/{patientId}", method = RequestMethod.GET)
	public Response getBillsForCreditNote(@PathVariable Map<String, String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int patientId = Integer.parseInt(params.get("patientId").trim());
			return iBillingMasterService.getBillsForCreditNote(orgId, unitId, patientId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/**
	 * get Interim Bill Details By Admission Id
	 * 
	 * @param admissionId
	 * @return
	 */
	@RequestMapping(value = "/intermediate/admission/{admissionId}", method = RequestMethod.GET)
	public Response getIntermediateBillByAdmissionId(@PathVariable("admissionId") int admissionId) {
		try {
			return iBillingMasterService.getIntermediateBillDetailsByAdmissionId(admissionId);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR, e.getMessage(), null, null, null);
		}
	}

	/**
	 * get Patient Service Order List
	 * 
	 * @param admissionDto
	 * @return
	 */
	@RequestMapping(value = "/getPatientServiceOrderList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientServiceOrderList(@RequestBody AdmissionDto admissionDto) {
		try {
			return iBillingMasterService.getPatientServiceOrderList(admissionDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/kiosk/saveAndPay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveBillFromKiosk(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			// Verify any claimed Razorpay payment before saving the bill or collecting payment
			// below -- previously the full paymentReceiptDetailsDtosList (amount, transaction
			// number, payment mode) was trusted as-is from the client with zero Razorpay
			// involvement of any kind. Non-Razorpay modes (cash/card/cheque/CCAvenue) are
			// unchanged -- see docs/REVIEW_DRAFT_payment-fixes-FINAL.md sec 4 for why that's a
			// deliberate choice for this pass, not an oversight.
			if (encounterMasterDto.getPaymentReceiptDetailsDtosList() != null) {
				for (PaymentReceiptDetailsDto payDto : encounterMasterDto.getPaymentReceiptDetailsDtosList()) {
					if (payDto.getPaymentModeId() != null && payDto.getPaymentModeId() == 8) { // Razorpay
						Response verifyRes = razorPayService.verifyRazorpayPayment(
								payDto.getRazorpayOrderId(), payDto.getTransactionNo(), payDto.getRazorpaySignature(),
								encounterMasterDto.getUnitId(), encounterMasterDto.getOrganizationId());
						if (!SUCCESS.equals(verifyRes.getStatus())) {
							logger.error("PAYMENT_RECONCILIATION_GAP: saveBillFromKiosk -- Razorpay payment claim failed verification, bill NOT saved. " + verifyRes.getMessage());
							return new Response(ERROR, ERROR_CODE, "Payment could not be verified", null, null);
						}
					}
				}
			}

			int isPaid = 0;
			if (encounterMasterDto.getPatientId() == 0 || (encounterMasterDto.getRegistrationTypeId()!=null && encounterMasterDto.getRegistrationTypeId()==5))
				isPaid = 1;
			Response res = null;
			if(!encounterMasterDto.getPaymentCompleted()) {

				 res = iBillingMasterService.saveBillFromKiosk(encounterMasterDto);
			}
			ButtonMasterDto buttonMasterDto = new ButtonMasterDto();
			buttonMasterDto.setDoctorId(encounterMasterDto.getDoctorId());
			buttonMasterDto.setSpecialityId(encounterMasterDto.getSpecialityId());
			buttonMasterDto.setHospitalId(encounterMasterDto.getOrganizationId());
			List<ButtonMasterDto> result = (List<ButtonMasterDto>)iUserRoleMasterService.getSingleButtonById(buttonMasterDto).getListObject();
			if(encounterMasterDto.getPaymentCompleted()) {
				return new Response(SUCCESS, SUCCESS_CODE, "Payment already completed for this appointment", result, null);
			}

			res.setListObject(result);
			if (res.getStatus().equals(SUCCESS)) {
				BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) res.getResObject();
				if (isPaid == 1)
					reqDto.setIsPaidAppointment('Y');


				if (reqDto.getPatientId() == null) {

					reqDto.setPatientId(encounterMasterDto.getPatientId());
				}
				iBillingMasterService.collectPayment(reqDto);

			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}


	
	
	@RequestMapping(value = "/kiosk/saveAndPay/future", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveBillFromKioskFutureDate(
			@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			
			
			if(encounterMasterDto.getPaymentReceiptDetailsDtosList()!= null) {

				for (PaymentReceiptDetailsDto dto : encounterMasterDto.getPaymentReceiptDetailsDtosList()) {

					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) {
							return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
					// Added 2026-07-15: verify any claimed Razorpay payment against Razorpay's
					// own signature before this bill/payment is saved -- previously trusted as-is
					// from the client with zero Razorpay involvement. See
					// docs/REVIEW_DRAFT_payment-fixes-FINAL.md sec 3.
					if (dto.getPaymentModeId() != null && dto.getPaymentModeId() == 8) { // Razorpay
						Response verifyRes = razorPayService.verifyRazorpayPayment(
								dto.getRazorpayOrderId(), dto.getTransactionNo(), dto.getRazorpaySignature(),
								encounterMasterDto.getUnitId(), encounterMasterDto.getOrganizationId());
						if (!SUCCESS.equals(verifyRes.getStatus())) {
							logger.error("PAYMENT_RECONCILIATION_GAP: saveBillFromKioskFutureDate -- Razorpay payment claim failed verification, bill NOT saved. " + verifyRes.getMessage());
							return new Response(ERROR, ERROR_CODE, "Payment could not be verified", null, null);
						}
					}
				}
			}
			Character isReshedule = encounterMasterDto.getIsReshedule() != null ? encounterMasterDto.getIsReshedule() : 'N';

			if(isReshedule.equals('Y'))
			{
				encounterMasterDto.setAppointmentIdOld(encounterMasterDto.getAppointmentId());
			}

			Response res = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
			if (res.getStatus().equals(SUCCESS)) {
				BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) res.getResObject();
				reqDto.setIsPaidAppointment('Y');
				reqDto.setPatientCouponId(encounterMasterDto.getPatientCouponTemplateMapperId() != null ? encounterMasterDto.getPatientCouponTemplateMapperId() : null);
				iBillingMasterService.collectPayment(reqDto);
				if(encounterMasterDto.getPatientCouponTemplateMapperId() != null){
					iBillingMasterService.patientCouponTemplate(encounterMasterDto.getPatientCouponTemplateMapperId());
				}
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/billlistForBookView/{patientId}/{unitId}/{orgId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public Response billlistForBookView(@PathVariable("patientId") int patientId,
			@PathVariable("unitId") Integer unitId, @PathVariable("orgId") Integer orgId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		try {
			return iBillingMasterService.billlistForBookView(patientId, unitId, orgId, fromDate, toDate);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/readSmartCard", method = RequestMethod.POST,
	 * consumes = MediaType.APPLICATION_JSON_VALUE) public @ResponseBody Response
	 * addSample(@RequestBody String sampleMasterDto) {
	 * 
	 * 
	 * Response<Claim, Integer> response = null; try {
	 * 
	 * JsonCheck.mapJsonToObject(sampleMasterDto, Claim.class);
	 * 
	 * response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
	 * response;
	 * 
	 * return null; } catch (Exception e) {  return new
	 * Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null); } }
	 */

	@RequestMapping(value = "/readSmartCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response readSmartCard(@RequestBody Claim claim) throws Exception {
		try {
			return iBillingMasterService.readSmartCard(claim);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/encounter/collect/payment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response collectPaymentFromEncounter(@RequestBody EncounterPaymentDto encounterPaymentDto) throws Exception {
		try {
			
			if(encounterPaymentDto.getPaymentReceiptDetailsDtosList() != null) {
				
				for (PaymentReceiptDetailsDto dto : encounterPaymentDto.getPaymentReceiptDetailsDtosList()) {
					
					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) {
							return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
				}
				
			}
			
			if(encounterPaymentDto.getEncounterMasterDto().getPatientId()==null || encounterPaymentDto.getEncounterMasterDto().getPatientId()==0)
			{
				Response patRes = iPatientRegistrationService.registerPatient(encounterPaymentDto.getEncounterMasterDto().getPatientRegistrationDto());
				if(patRes.getStatus().equals(SUCCESS))
				{
					encounterPaymentDto.getEncounterMasterDto().setPatientId((Integer)patRes.getObject());
					if(encounterPaymentDto.getEncounterMasterDto().getAppointmentObject()!=null)
						encounterPaymentDto.getEncounterMasterDto().getAppointmentObject().setPatientId((Integer)patRes.getObject());
					
				}else if(patRes.getCode().equals(ALREADY_EXIST_CODE)){
					return patRes;
				}
				encounterPaymentDto.getEncounterMasterDto().setNewPatient(true);
			}else{
				encounterPaymentDto.getEncounterMasterDto().setNewPatient(false);
			}
			
			Response res = iBillingMasterService.collectPaymentFromEncounter(encounterPaymentDto,encounterPaymentDto.getCashSettlement());
			if (res.getStatus().equals(SUCCESS)) 
			{
				BillPaymentCollectionReqDto paymentCollectionDto = (BillPaymentCollectionReqDto) res.getResObject();
				
				if (paymentCollectionDto.getTotalSelfPayable() != null && paymentCollectionDto.getTotalSelfPayable().doubleValue() > 0) 
				{
					BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) res.getResObject();
					iBillingMasterService.collectPayment(reqDto);

					if (encounterPaymentDto.getOtReservationList() != null && encounterPaymentDto.getOtReservationList().size() > 0) 
					{
						for (OTReservationDto oTReservationDto : encounterPaymentDto.getOtReservationList()) 
						{
							oTReservationDto.setEncounterId(paymentCollectionDto.getEncounterId());
							oTReservationDto.setVisitTypeId(paymentCollectionDto.getVisitTypeId() == null ? OP : paymentCollectionDto.getVisitTypeId());
							
							iBillOfMaterialService.updateReservationAgainstAdmissionEncounter(oTReservationDto);
							
		                    iBillOfMaterialService.convertOtdepositeToActiveDeposite(oTReservationDto);

						}
					}
				}
				
				String appointmentDate;
				appointmentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(paymentCollectionDto.getUnitId()), "dd/MM/yyyy") ;
					
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(paymentCollectionDto.getUnitId());
					smsInfo.setPatientId(paymentCollectionDto.getPatientId());
				    smsInfo.setConsultingDoctorId(encounterPaymentDto.getEncounterMasterDto()!=null && encounterPaymentDto.getEncounterMasterDto().getDoctorId()!= null ? encounterPaymentDto.getEncounterMasterDto().getDoctorId() : null);
	                smsInfo.setReferringDoctorId(encounterPaymentDto.getEncounterMasterDto()!=null && encounterPaymentDto.getEncounterMasterDto().getReferralId() != null ? encounterPaymentDto.getEncounterMasterDto().getReferralId() : null);smsInfo.setDateTime(appointmentDate);
                    smsInfo.setEncounterTypeId(encounterPaymentDto.getEncounterMasterDto()!=null && encounterPaymentDto.getEncounterMasterDto().getEncounterTypeId()!=null ? encounterPaymentDto.getEncounterMasterDto().getEncounterTypeId() :null);
					smsInfo.setOrganizationId(paymentCollectionDto.getOrgnizationId());
					smsInfo.setEncounterId(paymentCollectionDto.getEncounterId());
					smsInfo.setBillAmount(new BigDecimal(paymentCollectionDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
					smsInfo.setSelfAmount(new BigDecimal(paymentCollectionDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER,smsInfo);
				
				//if(res.getStatus().equalsIgnoreCase(SUCCESS))
					//iTDoctorShareService.getProcessDocShare(paymentCollectionDto.getUnitId());
				
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/submitSmartCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response submitSmartCard(@RequestBody SmartcardClaimDto smartcardClaimDto) throws Exception {
		try {

			return iBillingMasterService.submitSmartCard(smartcardClaimDto);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/changePayee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response changePayee(@RequestBody UpdatePayeeDto masterDto) throws Exception {
		try {
			Response res = iBillingMasterService.updatePayee(masterDto);
			if (res.getStatus().equals(SUCCESS)) {
				
				Response serviceRes =iBillingMasterService.getRenderedServicesByEncounterIdForBillSave(
						masterDto.getEncounterId() != null ? masterDto.getEncounterId()
								: masterDto.getAdmissionId(),
								masterDto.getVisitTypeId(), false, masterDto.getIsPreBilling());
				
				List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = serviceRes.getListObject();
				List<OrderDetailsByEncounterIdDto> packageList = orderDetailsByEncounterIdDtosList.stream().filter(p->p.getIsPackage()==true).collect(Collectors.toList());
				
				for(OrderDetailsByEncounterIdDto dto: packageList)
				{
					ContractConsumptionDto addCapDto = new ContractConsumptionDto();
						addCapDto.setAdmissionId(masterDto.getAdmissionId());
						addCapDto.setEncounterId(masterDto.getEncounterId());
						addCapDto.setOrderDetailsPayeeMapperDtosList(dto.getOrderDetailsPayeeMapperDtosList());
						addCapDto.setOrderDetailsPackageMapperDtosList(dto.getOrderDetailsPackageMapperDtosList());
						addCapDto.setSpecialityId(dto.getServiceSpecialityId());
						addCapDto.setServiceId(dto.getServiceMasterId());
					iContractConsumptionService.addCapContract(addCapDto);
				}
				
				
				com.param.billing.common.Response packageRes = iBillingDetailsService.updatePayeeForPackage(masterDto,orderDetailsByEncounterIdDtosList,packageList,null);
				if (packageRes.getListObject() != null && packageRes.getObject() != null) 
				{
					// SAVE PACKAGE CONSUMPTION
					BillingMasterDto billingMasterDto = new BillingMasterDto();
						billingMasterDto.setOrganisationId(masterDto.getOrgId());
						billingMasterDto.setUnitId(masterDto.getUnitId());
						billingMasterDto.setCreatedBy(masterDto.getCreatedBy());
						billingMasterDto.setUpdatedBy(masterDto.getCreatedBy());
						billingMasterDto.setPatientId(masterDto.getPatientId());
						billingMasterDto.setVisitTypeId(masterDto.getVisitTypeId());
						billingMasterDto.setEncounterId(masterDto.getEncounterId());
						billingMasterDto.setAdmissionId(masterDto.getAdmissionId());
						billingMasterDto.setPaymentEntitlementId(masterDto.getPaymentEntitlementId());
						billingMasterDto.setPatientTypeId(masterDto.getPatientCategoryId());
						billingMasterDto.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
						billingMasterDto.setContractId(masterDto.getContractId());
						billingMasterDto.setTariffId(masterDto.getDefaultSelfTariffId());
						billingMasterDto.setPharmacyServiceId(masterDto.getPharmacyServiceId());
						billingMasterDto.setPatientClassId(masterDto.getPatientClassId());
						billingMasterDto.setContractId(masterDto.getContractId());
						billingMasterDto.setReferralId(masterDto.getReferralId());
						billingMasterDto.setSelfPayeeId(masterDto.getSelfPayeeId());
					Response updatePayeeRes = iBillingMasterService.updatePackageCoverSheet(billingMasterDto, 'N',true,false);
					if (updatePayeeRes.getStatus().equals(SUCCESS)) 
					{
						{
							return new Response<>(SUCCESS, null, "Payee Updated Successfully..!!", null, null);
						}
					}
				}
			}
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/cancelBillAndAutopostingServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response cancelAutoPostingAndOrederServicesByAdmission(@RequestBody DischargeDto dto) {
		try {

			return iBillingMasterService.cancelAutoPostingAndOrederServicesByAdmission(dto);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/cancelDischarge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response cancelDischarge(@RequestBody DischargeDto dischargeDto) throws Exception {
		try {
			Long dischCount = 0l;
			Response dischargeRes1 =  iBillingMasterService.checkDischargeOrNot(dischargeDto);
			
			if(dischargeRes1.getObject().equals(dischCount)) {
				return new Response(ERROR, null, "This patient discharge already cancelled by other user", null, null);
			}else {
				Response dischargeRes =  iBillingMasterService.cancelDischarge(dischargeDto);
				if (dischargeRes.getStatus().equals(SUCCESS)) {

					Response orderDetailsRes = iBillingMasterService.cancelAutoPostingAndOrederServicesByAdmission(dischargeDto);
					if (orderDetailsRes.getCode().equals(SUCCESS_CODE)) {
						if (orderDetailsRes.getListObject() != null && orderDetailsRes.getListObject().size() > 0) {
							List<OrderDetailsMasterDto> list = orderDetailsRes.getListObject();
							for (OrderDetailsMasterDto odto : list) 
							{
								CancelOrderDto cancelOrderDto =new CancelOrderDto();
									 cancelOrderDto.setAdmissionId(dischargeDto.getAdmissionId());
									 cancelOrderDto.setOrderDetailsId(odto.getOrderDetailsId());
									 cancelOrderDto.setSpecialityId(odto.getSpecialityId());
									 cancelOrderDto.setUserId(dischargeDto.getUpdatedBy());
									 cancelOrderDto.setVisitTypeId(dischargeDto.getVisitTypeId());
									 cancelOrderDto.setOrdCancelReasonId(dischargeDto.getReasonId());
								 globalBillingService.cancelPendingService(cancelOrderDto);
							}
						}
						iBillingMasterService.updateDischarge(dischargeDto);
						return new Response(SUCCESS, SUCCESS_CODE, "Discharge Cancelled Successfully.", null, null);
					} else {
						return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
					}
				}
		      return dischargeRes;
			}
			
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/post/bill/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response postServicesIntoExistingBill(@RequestBody List<Integer> orderDetailsIdsList) {
		try {
			return globalBillingService.postServicesIntoExistingBill(orderDetailsIdsList);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/bill/service/cancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelPendingService(@RequestBody CancelOrderDto cancelOrderDto) {
		try {
			//return globalBillingService.cancelPendingService(cancelOrderDto);
			return iBillingMasterService.cancelPendingService(cancelOrderDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/bill/estimate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveBillEstimate(@RequestBody CostEstimateDto costEstimationDto) throws Exception {
		try {

			Response res = globalBillingService.saveBillEstimate(costEstimationDto);
			Integer costEstimateId = (Integer) res.getObject();

			SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
			smsInfo.setOrganizationId(costEstimationDto.getOrgId());
			smsInfo.setUnitId(costEstimationDto.getUnitId());
			smsInfo.setPatientId(costEstimationDto.getPatientId());
			smsInfo.setCostEstimateId(costEstimateId);
			smsInfo.setToMobileNumber(costEstimationDto.getNewWhatsAppNo() != null ? costEstimationDto.getNewWhatsAppNo() : "");
			if(costEstimationDto.getIsManuallySend() != null && costEstimationDto.getIsManuallySend() == TRUE) {
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.COST_ESTIMATION, smsInfo);
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.COST_ESTIMATION, smsInfo);
			}

			return new Response(SUCCESS, SUCCESS_CODE, null, null, res);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/convert/services", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response convertServicesFromOPToIP(
			@RequestBody ConvertServicesFromOptoIpDto costEstimationDto) {
		try {

			return globalBillingService.convertServicesFromOPToIP(costEstimationDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/upDatePackage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response orderAndUpdatePackage(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {
			
			Set<Integer> orderDetailsIdList = billingMasterDto.getOrderDetailsMasterDtosList()
					.stream().map(OrderDetailsMasterDto::getOrderDetailsId).collect(Collectors.toSet());
			orderDetailsIdList.remove(null);
			
			if(billingMasterDto.getForceInOut() == null) {
				
				boolean isBillOrderCountMatched = iBillingMasterService.matchBillAmounts(billingMasterDto,orderDetailsIdList);
				if(!isBillOrderCountMatched)
					return new Response<>(ERROR, ALREADY_EXIST_CODE, "Dispute in orders count, refreshing orders !", null, (!isBillOrderCountMatched));
			}
			
			List<OrderDetailsMasterDto> sortedList = new LinkedList<>();
			
			List<OrderDetailsMasterDto> pkgList = billingMasterDto.getOrderDetailsMasterDtosList()
					.parallelStream()
					.filter(obj->obj.isPackage()==true)
					.collect(Collectors.toList());

			sortedList.addAll(pkgList);
			
			List<OrderDetailsMasterDto> newPkgList = billingMasterDto.getOrderDetailsMasterDtosList()
					.parallelStream()
					.filter(obj->obj.getOrderDetailsId()==null && obj.isPackage())
					.collect(Collectors.toList());
			
			if(!newPkgList.isEmpty())
			{
				Date minEffectiveDate = newPkgList.stream().map(pobj -> GlobalCommonDateUtils.getDate(pobj.getPackageApplicableDate(),"dd-MM-yyyy")).min(Date::compareTo).get();
				
				List<OrderDetailsMasterDto> sortedServiceList = billingMasterDto.getOrderDetailsMasterDtosList()
						.parallelStream()
						.filter(obj->(obj.isPackage()==false 
										&& minEffectiveDate.getTime()<=GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDateFromLongDate(obj.getOrderDate(), "dd-MM-yyyy"), "dd-MM-yyyy").getTime() 
											&& (obj.getServiceAmt().doubleValue()!=obj.getPackageConsumptionAmt().doubleValue() || obj.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId()))
								|| (obj.isPackage()==false && obj.getOrderDetailsId()==null) )
						.collect(Collectors.toList());
				sortedList.addAll(sortedServiceList);
				
			}else{
				List<OrderDetailsMasterDto> sortedServiceList = billingMasterDto.getOrderDetailsMasterDtosList()
						.parallelStream()
						.filter(obj->obj.isPackage()==false)
						.collect(Collectors.toList());
				sortedList.addAll(sortedServiceList);
			}
			
			billingMasterDto.setOrderDetailsMasterDtosList(sortedList);
			
			
			if(!pkgList.isEmpty() && billingMasterDto.isUpdatePayee()) {
				Response serviceRes =iBillingMasterService.getRenderedServicesByEncounterIdForBillSave(billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId()
						: billingMasterDto.getAdmissionId(),
						billingMasterDto.getVisitTypeId(), 
						false, 
						billingMasterDto.getIsPreBilling());
		
				List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = serviceRes.getListObject();
				List<OrderDetailsByEncounterIdDto> packageList = orderDetailsByEncounterIdDtosList.stream().filter(OrderDetailsByEncounterIdDto::getIsPackage).collect(Collectors.toList());
		

				com.param.billing.common.Response updatePayeeRes = iBillingDetailsService
						.updatePayeeForPackage(billingCommonMethods.createUpdatePayeeDto(billingMasterDto),null,packageList,billingMasterDto.getOrderDetailsMasterDtosList());
				if (updatePayeeRes != null && updatePayeeRes.getObject2() != null) {
					List<Integer> packageNeedsToBeCancelledOrdDtlsId = (List<Integer>) updatePayeeRes.getObject2();
					
					
					List<Integer> orderDetailIdsToBeRemoved = packageList.stream().map(od->od.getOrderDetailsId()).collect(Collectors.toList());
					
					billingMasterDto.getOrderDetailsMasterDtosList()
							.forEach(detailsMasterDto -> {
								if (packageNeedsToBeCancelledOrdDtlsId.contains(detailsMasterDto.getOrderDetailsId())) {
									detailsMasterDto.setStatus('I');
									detailsMasterDto.setOrdCancelled('Y');
								}
							});
					
					if(updatePayeeRes.getListObject()!= null && updatePayeeRes.getListObject().size()>0) {
						List<OrderDetailsMasterDto> listPackageUpdate = updatePayeeRes.getListObject();
						
						for (OrderDetailsMasterDto updatedPackDto : listPackageUpdate) {
							for (OrderDetailsMasterDto updatePackDto : billingMasterDto.getOrderDetailsMasterDtosList()) {
								if(updatedPackDto.getOrderDetailsId().equals(updatePackDto.getOrderDetailsId())) {
									
									updatePackDto.setPackageId(updatedPackDto.getPackageId());
									updatePackDto.setOrdTotalAmt(updatedPackDto.getOrdTotalAmt());
									updatePackDto.setServiceAmt(updatedPackDto.getServiceAmt());
									updatePackDto.setOrdConcession(updatedPackDto.getOrdConcession());
									updatePackDto.setTaxAmount(updatedPackDto.getTaxAmount());
	                                updatePackDto.setNetAmount(updatedPackDto.getNetAmount());
	                                updatePackDto.setHospitalShare(updatedPackDto.getHospitalShare());
	                                updatePackDto.setCreditPayable(updatedPackDto.getCreditPayable());
	                                updatePackDto.setSelfPayable(updatedPackDto.getSelfPayable());
								}
								
								 for (OrderDetailsPackageMapperDto packMappr : updatePackDto.getOrderDetailsPackageMapperDtosList()) {
									 
										if(packMappr.getPackageId() != null && packMappr.getPackageId().equals(updatedPackDto.getOldPackageId())) {
											packMappr.setPackageId(updatedPackDto.getPackageId());
											packMappr.setServiceAmount(updatedPackDto.getServiceAmt());
										}
									}
								}
					
							}
						}
				}
			}
			
				
				ListIterator<OrderDetailsMasterDto> ordrItr = billingMasterDto.getOrderDetailsMasterDtosList().listIterator();
				OrderDetailsMasterDto order = null;
				Integer ehcPackageId = null;
				Response packCanclRes = null;
				Integer pacakgeId = null;
				while (ordrItr.hasNext())
				{
					order = ordrItr.next();
					if (order.getOrdCancelled() != null && order.getOrdCancelled() == 'Y') 
					{
						if (order.isPackage()) 
						{
							if(order.getPackageTypeId()!=null && order.getPackageTypeId().intValue()==1)
								ehcPackageId = order.getPackageId();
							
							pacakgeId = order.getPackageId();
				         }
			          }
		           }
				
				for ( OrderDetailsMasterDto pkgDto : billingMasterDto.getOrderDetailsMasterDtosList() )
				{
					
					if(pkgDto.getPackageId()!=null && pkgDto.getPackageId().equals(ehcPackageId))
	        	    {
						pkgDto.setOrdCancelled('Y');
						pkgDto.setStatus('I');
	        	    }
					if(pkgDto.getForceAddRemoved()==1 && pacakgeId!=null) 
					{
					  Integer pkgId =pacakgeId;
					 Optional<OrderDetailsPackageMapperDto> result = pkgDto.getOrderDetailsPackageMapperDtosList()
						      .stream().parallel()
						      .filter(ord -> ord.getPackageId()!=null && ord.getPackageId().equals(pkgId)).findAny();
					 
						if(result.isPresent())
						{ 
							pkgDto.setForceAddRemoved(0);
						}
					}
				}
			
			Integer isPackageCancelled = 0;
			Response packCanRes = iBillingMasterService.cancelPackage(billingMasterDto, billingMasterDto.getOrderDetailsMasterDtosList());
			isPackageCancelled = packCanRes.getObject()!=null ? (Integer)packCanRes.getObject() : 0;
			
			boolean isPatienClassApplied = !(!pkgList.isEmpty() ||isPackageCancelled==1);
			
			if(!billingMasterDto.isUpdatePayee())
			{
				for(OrderDetailsMasterDto detailsMasterDto : billingMasterDto.getOrderDetailsMasterDtosList())
				{
					if(detailsMasterDto.getOrderDetailsId()!=null && (detailsMasterDto.getPackageTypeId() == null || detailsMasterDto.getPackageTypeId() != PACKAGE1))
					{
						
						ContractConsumptionDto addCapDto = billingCommonMethods.createContractConsumption(billingMasterDto,detailsMasterDto);
						if(detailsMasterDto.getServiceId().equals(billingMasterDto.getPharmacyServiceId()) && detailsMasterDto.getOrdTotalAmt().doubleValue()>0){
							iWalkInSalesService.addContractCapAmountPharmacy(addCapDto,detailsMasterDto.getSaleId());
						}else{
							iContractConsumptionService.addCapContract(addCapDto);
						}
					}
					billingMasterDto.setAddPreAuth(true);
				}
			}
			
			Response res = iBillingMasterService.orderPackageOnSelection(billingMasterDto);
			
			if((boolean)res.getResObject() || isPackageCancelled == 1 || billingMasterDto.isApplyAuth() || billingMasterDto.isNeedToUpdatePharmacyService() || billingMasterDto.isUpdatePayee())
				billingMasterDto.setNeedToUpdatePharmacyService(true);
				
			if (res.getListObject() != null && !res.getListObject().isEmpty())
			{
				List<OrderDetailsMasterDto> listToBeUpdated = res.getListObject();
				ContractConsumptionDto contractDto = billingCommonMethods.createContractConsumptionForClone(billingMasterDto,listToBeUpdated);
				ContractConsumptionDto contractDtoClone = contractDto;
					iBillingMasterService.resetCapWithPackage(billingMasterDto);
				
				for (OrderDetailsMasterDto ordDtls : contractDto.getListOrderDetailsMasterDto()) 
				{
					if (ordDtls.getPackageTypeId() == null || ordDtls.getPackageTypeId() != PACKAGE1 ) {
						if (ordDtls.getProcedureOrderMstId() != null && ordDtls.getProcedureOrderMstId() > 0 && ordDtls.getIsProcedureService() == 'Y') 
						{
							contractDtoClone.setServiceId(ordDtls.getProcedureOrderMstId());
							for (OrderDetailsPayeeMapperDto odPayeeDto : ordDtls.getOrderDetailsPayeeMapperDtosList())
								odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
							for (OrderDetailsPackageMapperDto odPayeeDto : ordDtls
									.getOrderDetailsPackageMapperDtosList())
								odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
						} else 
						{
							contractDtoClone.setServiceId(ordDtls.getServiceId());
						}
						contractDtoClone.setOrderDetailsPayeeMapperDtosList(ordDtls.getOrderDetailsPayeeMapperDtosList());
						contractDtoClone.setOrderDetailsPackageMapperDtosList(ordDtls.getOrderDetailsPackageMapperDtosList());
						contractDtoClone.setSpecialityId(ordDtls.getDepartmentId());
						contractDtoClone.setEncounterId(billingMasterDto.getEncounterId());
						contractDtoClone.setAdmissionId(billingMasterDto.getAdmissionId());
						contractDtoClone.setContractId(billingMasterDto.getContractId());
						contractDtoClone.setServiceChargeStatus(ordDtls.getIsChargedHalfDay()!=null && ordDtls.getIsChargedHalfDay()=='Y'?0:1);
							if(billingMasterDto.getPharmacyServiceId() != null &&billingMasterDto.getPharmacyServiceId()>0 && (ordDtls.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())) 
							{
								 contractDtoClone.setForceAddRemoved(ordDtls.getForceAddRemoved());
								 iContractConsumptionService.addCapPackage(contractDtoClone);
							}
							
						if (!ordDtls.isPackage() && ordDtls.getOrdTotalAmt().doubleValue()!=0) 
						{
							
							if ((isPackageCancelled == 1 || billingMasterDto.isUpdatePayee()) 
									&& ordDtls.getPackageConsumptionAmt() != null
									&& ordDtls.getPackageConsumptionAmt().doubleValue() > 0.00
									&& billingMasterDto.getPharmacyServiceId() != null
									&& (ordDtls.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())
									&& (ordDtls.getProcedureOrderMstId() == null)
									&& (ordDtls.getIsPeriodicityService() == null || ordDtls.getIsPeriodicityService() == 'N'))
							{
								if(ordDtls.getIsConsultationService() != null &&  ordDtls.getIsConsultationService()=='Y')
								{
									contractDtoClone.setIsConsultationService(ordDtls.getIsConsultationService());
								}else{
									contractDtoClone.setIsConsultationService('N');
									contractDtoClone.setConsultationAmount(null);
								}
								contractDtoClone.setPatientClassPercentage(ordDtls.getPatientClassPercentage().doubleValue());
								contractDtoClone.setCurrentDate(ordDtls.getOrderDate() == null ? GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyyy-MM-dd")  
										: GlobalCommonDateUtils.getStringDate(ordDtls.getOrderDate(), "yyyy-MM-dd"));
							    contractDtoClone.setBillingBedCategoryId(ordDtls.getBedCategoryId()!=null && ordDtls.getBedCategoryId()>0?ordDtls.getBedCategoryId():billingMasterDto.getBillingBedCategoryId());
								
							    Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contractDtoClone, false, false);
								ContractConsumptionDto globalRatesRes = (ContractConsumptionDto) serviceRateRes.getObject();
								
								ordDtls.setCoPayPer(BigDecimal.valueOf(globalRatesRes.getCoShare()));
								ordDtls.setSelfPayable(BigDecimal.valueOf(globalRatesRes.getSelfPayable() * ordDtls.getOrderQty()));
								ordDtls.setCreditPayable(BigDecimal.valueOf(globalRatesRes.getCreaditPayable() * ordDtls.getOrderQty()));
								ordDtls.setServiceAmt(BigDecimal.valueOf(globalRatesRes.getServiceRate()));
								ordDtls.setOrdConcession(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO: BigDecimal.valueOf(globalRatesRes.getConcession() * ordDtls.getOrderQty()));
								ordDtls.setOrdTotalAmt(BigDecimal.valueOf(globalRatesRes.getServiceRate() * ordDtls.getOrderQty()));
								ordDtls.setNetAmount(BigDecimal.valueOf(globalRatesRes.getFinalPrice() * ordDtls.getOrderQty()));
								ordDtls.setOrdDiscount(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO: BigDecimal.valueOf(globalRatesRes.getOrdDiscount() * ordDtls.getOrderQty()));
								ordDtls.setOrdConcessionPer(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO: BigDecimal.valueOf(globalRatesRes.getPatientClassPercentage()));
								ordDtls.setDummyService(FALSE);
							}
						
							ordDtls.setOrderDetailsPackageMapperDtosList(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrderDetailsPackageMapperDtosList():null);
							ordDtls.setOrderDetailsPayeeMapperDtosList(null);
							ordDtls.setOrderDate(ordDtls.getOrderDate() == null ? TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime() : ordDtls.getOrderDate());
							ordDtls.setSelfPayable(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getOrdTotalAmt());
							ordDtls.setNetAmount(ordDtls.getForceAddRemoved().equals(1)?BigDecimal.ZERO:ordDtls.getNetAmount());
							ordDtls.setPackageConsumptionAmt(ordDtls.getForceAddRemoved().equals(1)?ordDtls.getOrdTotalAmt():BigDecimal.ZERO);
							ordDtls.setCreditPayable(BigDecimal.ZERO);
						}
					}else if(ordDtls.getPackageTypeId() != null && ordDtls.getPackageTypeId() == PACKAGE1 && ordDtls.getForceAddRemoved().equals(-1)) 
					{
						ordDtls.setPackageConsumptionAmt(BigDecimal.ZERO);
						ordDtls.setPackageTypeId(null);
						ordDtls.setOrderDetailsPackageMapperDtosList(null);
						ordDtls.setOrderDetailsPayeeMapperDtosList(null);
						ordDtls.setOrderDate(ordDtls.getOrderDate() == null ? TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime() : ordDtls.getOrderDate());
						ordDtls.setSelfPayable(ordDtls.getOrdTotalAmt());
						ordDtls.setNetAmount(BigDecimal.ZERO);
						ordDtls.setCreditPayable(BigDecimal.ZERO);
					}else if(ordDtls.getPackageTypeId() != null && ordDtls.getPackageTypeId() == 1 && ordDtls.getForceAddRemoved().equals(0) && ordDtls.getIsPackageService()=='Y') 
					{
						ordDtls.setPackageConsumptionAmt(ordDtls.getOrdTotalAmt());
						ordDtls.setPackageTypeId(1);
						ordDtls.setSelfPayable(BigDecimal.ZERO);
						ordDtls.setNetAmount(BigDecimal.ZERO);
						ordDtls.setCreditPayable(BigDecimal.ZERO);
						ordDtls.setTaxId(null);
						ordDtls.setTaxPer(BigDecimal.ZERO);
						ordDtls.setTaxAmount(BigDecimal.ZERO);
					}
				}

				Response packageRes = iMPackageMasterService.updatePackage(contractDto);
				if (packageRes.getObject() != null) 
				{
					contractDto = (ContractConsumptionDto) packageRes.getObject();
					
					List<OrderDetailsMasterDto> pharmacyServiceRearrange = new ArrayList<>();
					
					   for (OrderDetailsMasterDto orderDetailsMasterDto : contractDto.getListOrderDetailsMasterDto()) {
						
						   if(orderDetailsMasterDto.getSaleId() != null && orderDetailsMasterDto.getOrderQty()>0) {
							   pharmacyServiceRearrange.add(orderDetailsMasterDto);
								
								   for (OrderDetailsMasterDto orderDetailretuern : contractDto.getListOrderDetailsMasterDto()) {
									
									   if(orderDetailretuern.getSaleId() != null  && orderDetailretuern.getSaleId().equals(orderDetailsMasterDto.getSaleId())&& orderDetailretuern.getOrderQty()<0) {
										   pharmacyServiceRearrange.add(orderDetailretuern);
									   }
								   }
							
						   }else if(orderDetailsMasterDto.getSaleId() == null){
							   pharmacyServiceRearrange.add(orderDetailsMasterDto); 
						   }
					   }
					   
					   contractDto.setListOrderDetailsMasterDto(pharmacyServiceRearrange);
					
					
					List<OrderDetailsMasterDto> listForSave = new ArrayList<>();
					for (OrderDetailsMasterDto ordDtlsDto : contractDto.getListOrderDetailsMasterDto()) 
					{
						if((ordDtlsDto.getStatus()==null || ordDtlsDto.getStatus().equals('A')) 
								&& ordDtlsDto.getOrdTotalAmt().doubleValue()>=0
									&& (billingMasterDto.getPharmacyServiceId() == null || (billingMasterDto.getPharmacyServiceId() != null &&billingMasterDto.getPharmacyServiceId()>0 
										&& (ordDtlsDto.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())))) {
							boolean addToList = true;
							contractDtoClone.setPatientClassApplied(isPatienClassApplied);
							contractDtoClone.setQuantity(ordDtlsDto.getOrderQty().doubleValue());
							contractDtoClone.setSpecialityId(ordDtlsDto.getDepartmentId());
							contractDtoClone.setServiceId(ordDtlsDto.getServiceId());
							contractDtoClone.setEncounterId(billingMasterDto.getEncounterId());
							contractDtoClone.setAdmissionId(billingMasterDto.getAdmissionId());
							contractDtoClone.setTariffId(billingMasterDto.getTariffId());
							contractDtoClone.setContractId(billingMasterDto.getContractId());
							contractDtoClone.setDefaultSelfTariffId(billingMasterDto.getTariffId());
							contractDtoClone.setCurrentDate(ordDtlsDto.getOrderDate()!=null ? GlobalCommonDateUtils.getStringDate(ordDtlsDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss") 
									:  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
							contractDtoClone.setServiceChargeStatus(ordDtlsDto.getIsChargedHalfDay()!=null && ordDtlsDto.getIsChargedHalfDay()=='Y'?0:1);
							if (ordDtlsDto.getOrderDetailsPackageMapperDtosList() != null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
							{
								if (ordDtlsDto.getSelfPayable().doubleValue() > 0 && !ordDtlsDto.isPackage()
										&& (ordDtlsDto.getPackageTypeId() == null || ordDtlsDto.getPackageTypeId() != PACKAGE1)) 
								{
									OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
									oDPmpprDto.setIsPackageService('N');
									oDPmpprDto.setIsCapService('N');
									oDPmpprDto.setPackageAmount(BigDecimal.ZERO);
									oDPmpprDto.setServiceAmount(ordDtlsDto.getSelfPayable());
									oDPmpprDto.setPackageQty(1);
									ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);
								}else {
									ordDtlsDto.setOrderDetailsPayeeMapperDtosList(billingCommonMethods.createOrderDetailsPayeeMapperDtos(null,null,billingMasterDto,ordDtlsDto));

									if(ordDtlsDto.getServiceAmt().equals(BigDecimal.ZERO) && (ordDtlsDto.getPackageTypeId() == null || ordDtlsDto.getPackageTypeId() != PACKAGE1)) {
										List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
										mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(null,null,ordDtlsDto,null));
										ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
									}
								}
								if(!ordDtlsDto.isPackage() && (ordDtlsDto.getPackageTypeId() == null || ordDtlsDto.getPackageTypeId() != PACKAGE1)){
									if (ordDtlsDto.getSelfPayable().doubleValue() > 0 ) 
									{
											contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
											contractDtoClone.setOrdDiscount(ordDtlsDto.getOrdDiscount().doubleValue());
											contractDtoClone.setUpdateCap(null);
											contractDtoClone.setPatientClassPercentage(ordDtlsDto.getPatientClassPercentage().doubleValue());
										Response resContract = iContractConsumptionService.consumeServiceFromContractPackage(contractDtoClone, null);
										ContractConsumptionDto contractResp = (ContractConsumptionDto) resContract.getObject();
										if(contractResp != null){
										ordDtlsDto.setTaxId(contractResp.getTaxId() != null ? contractResp.getTaxId() : null);
										ordDtlsDto.setTaxPer(contractResp.getTaxPercentage() != null ? BigDecimal.valueOf(contractResp.getTaxPercentage()) :new BigDecimal("0.00"));
										if(!ordDtlsDto.getPackageConsumptionAmt().equals(BigDecimal.ZERO)) {
											ordDtlsDto.setOrdDiscount(BigDecimal.ZERO);
											ordDtlsDto.setOrdDiscountPer(BigDecimal.ZERO);
										}
										ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
										ordDtlsDto.setTaxAmount(BigDecimal.valueOf(contractResp.getTaxAmt()));
										ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
										ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getFinalPrice()));
										ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession()));
										ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
										}
									}else{
										ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
										ordDtlsDto.setCreditPayable(BigDecimal.ZERO);
										ordDtlsDto.setTaxAmount(BigDecimal.ZERO);
										ordDtlsDto.setTaxPer(BigDecimal.ZERO);
										ordDtlsDto.setNetAmount(BigDecimal.ZERO);
									}
								}
								
								else if(ordDtlsDto.isPackage())
								{
											Response resContract = null;
										resContract =iContractConsumptionService.consumePackageFromContract(billingCommonMethods.createServiceSearchResDto(contractDtoClone,ordDtlsDto,null,billingMasterDto.isAddPreAuth()));
										ContractConsumptionDto contractResp = (ContractConsumptionDto) resContract.getObject();
											ordDtlsDto.setCoPayPer(BigDecimal.valueOf(contractResp.getCoShare()));
											ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
											ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
											ordDtlsDto.setOrdConcession(BigDecimal.valueOf(	contractResp.getConcession() != null ? contractResp.getConcession() : 0));
											ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getNetAmount()));
											ordDtlsDto.setOrdConcessionPer(	BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
											ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
											ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
											ordDtlsDto.setTariffId(ordDtlsDto.getTariffId());
									
								}
								
								if(billingMasterDto.getIsBillManipulationAllowed())
								{
									List<Integer> packageConsumptionMasterIds = ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty()
											? ordDtlsDto.getOrderDetailsPackageMapperDtosList().stream().map(OrderDetailsPackageMapperDto::getPackageConsumptionMasterId).filter(Objects::nonNull).collect(Collectors.toList()) : null;
									if(packageConsumptionMasterIds!=null && !packageConsumptionMasterIds.isEmpty())
										ordDtlsDto.setDummyService(iOrderDetailsMasterService.getDummyServiceFlagForPackageService(packageConsumptionMasterIds));
								}
								
							} else {
								Response resContract = null;

								if (ordDtlsDto.getSelfPayable()!=null && !ordDtlsDto.isPackage()) 
								{
									contractDtoClone.setSelfPayable(ordDtlsDto.getSelfPayable().doubleValue());
									contractDtoClone.setFinalPrice(ordDtlsDto.getNetAmount().doubleValue());
									contractDtoClone.setIsConsultationService(ordDtlsDto.getIsConsultationService());
									contractDtoClone.setIsPeriodicityService('N');
									contractDtoClone.setConsultationAmount(null);
									contractDtoClone.setIsBOM('N');
									
									if (ordDtlsDto.getIsConsultationService() != null && ordDtlsDto.getIsConsultationService().equals('Y')) 
									{
										if(billingMasterDto.isUpdatePayee())
										{
											Double consulServiceRate = null;
											EncounterMasterDto encounterMasterDto= iEncounterMasterService.getEncounterDetailsForUpdatePackage(contractDtoClone.getEncounterId());
											encounterMasterDto.setPaymentEntitlementId(billingMasterDto.getPaymentEntitlementId()); 
											DoctorConsultationServiceMapperDto dto = iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorIdForUpdatePackage(encounterMasterDto);
											
											if(dto!=null)
											{
												switch (encounterMasterDto.getUserDefinedVisitTypeId()){
													case 1:
														consulServiceRate = dto.getNewVisitRate();
														break;
													case 2:
														consulServiceRate = dto.getFollowupVisitRate();
														break;
													case 3:
														consulServiceRate = dto.getSecondaryVisitRate();
														break;
												}
												
												contractDtoClone.setConsultationAmount(consulServiceRate);
											}else{
												addToList = false;
											}
										}else{
											contractDtoClone.setConsultationAmount((ordDtlsDto.getServiceAmt().doubleValue()));
											contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										}
										contractDtoClone.setOrdDiscountPer(ordDtlsDto.getOrdDiscountPer().doubleValue());
										
									}else if (ordDtlsDto.getIsPeriodicityService() != null && ordDtlsDto.getIsPeriodicityService() == 'Y') 
									{
										contractDtoClone.setQuantity(ordDtlsDto.getOrderQty().doubleValue());
										contractDtoClone.setConsultationAmount(null);
										contractDtoClone.setIsPeriodicityService('Y');
										
										if(billingMasterDto.isUpdatePayee())
										{
											TPeriodicityServiceOrdersDto periodicityServiceOrdersDto = iTPeriodicityServiceOrdersService.getPeriodicityServiceDetails(ordDtlsDto.getOrderDetailsId());
											periodicityServiceOrdersDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
											periodicityServiceOrdersDto.setDefaultSelfTariffId(contractDtoClone.getDefaultSelfTariffId());
											periodicityServiceOrdersDto.setServiceId(contractDtoClone.getServiceId());
											periodicityServiceOrdersDto.setChargesResDto(billingCommonMethods.createAdmissionListIpdChargesResDto(contractDtoClone,periodicityServiceOrdersDto.getPatientId()));
											
											List<TPeriodicityServiceOrdersDto> periodicityServiceOrdersDtosList = new LinkedList<>();
											periodicityServiceOrdersDtosList.add(periodicityServiceOrdersDto);
											iTPeriodicityServiceOrdersService.orderTPeriodicityServices(periodicityServiceOrdersDtosList);
											addToList = false;
										}
										else
											contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
									}else 
									{
										contractDtoClone.setOrdDiscountPer(ordDtlsDto.getOrdDiscountPer().doubleValue());
										contractDtoClone.setConsultationAmount(null);

										contractDtoClone.setChangedPrice(billingMasterDto.isUpdatePayee()?null:ordDtlsDto.getServiceAmt().doubleValue());
										
										if (ordDtlsDto.getProcedureOrderMstId() != null	&& ordDtlsDto.getProcedureOrderMstId() > 0 && ordDtlsDto.getIsProcedureService() == 'Y') 
										{
											contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
											contractDtoClone.setIsBOM('Y');
											contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										}else if (ordDtlsDto.getProcedureOrderMstId() != null && ordDtlsDto.getProcedureOrderMstId() > 0 && ordDtlsDto.getIsProcedureService() == 'N')
										{
											contractDtoClone.setProcedureId(ordDtlsDto.getProcedureOrderMstId());
											contractDtoClone.setIsBOM('N');
											contractDtoClone.setChangedPrice(ordDtlsDto.getServiceAmt().doubleValue());
										}
										else {
											contractDtoClone.setProcedureId(null);
											contractDtoClone.setServiceRate(null);
											contractDtoClone.setIsBOM('N');
										}
									}
									if(addToList)
									{
										if (ordDtlsDto.isPackage()) {
											resContract = iContractConsumptionService.consumePackageFromContract(billingCommonMethods.createServiceSearchResDto(contractDtoClone,ordDtlsDto,contractDtoClone.isApplyAuth(),null));
										}else {
											
												contractDtoClone.setUpdateCap(null);
												contractDto.setApplyAuth(contractDtoClone.isApplyAuth());
												contractDtoClone.setPatientClassPercentage(ordDtlsDto.getPatientClassPercentage().doubleValue());
												contractDtoClone.setBillingBedCategoryId((ordDtlsDto.getBedCategoryId()!=null && ordDtlsDto.getBedCategoryId()>0) ?ordDtlsDto.getBedCategoryId():billingMasterDto.getBillingBedCategoryId());
												resContract = iContractConsumptionService.consumeServiceFromContract(contractDtoClone);
												
										}
										ContractConsumptionDto contractResp;
										contractResp = (ContractConsumptionDto) resContract.getObject();

										if (ordDtlsDto.getProcedureOrderMstId() != null
												&& ordDtlsDto.getProcedureOrderMstId() > 0
												&& ordDtlsDto.getIsProcedureService() == 'Y') 
										{
											contractResp.setServiceId(ordDtlsDto.getServiceId());

											if (!contractResp.getOrderDetailsPayeeMapperDtosList().isEmpty())
												for (OrderDetailsPayeeMapperDto odPayeeDto : contractResp.getOrderDetailsPayeeMapperDtosList()) {
													odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
												}

											if (contractResp.getOrderDetailsPackageMapperDtosList() != null
													&& !contractResp.getOrderDetailsPackageMapperDtosList().isEmpty())
												for (OrderDetailsPackageMapperDto odPayeeDto : contractResp.getOrderDetailsPackageMapperDtosList()) {
													odPayeeDto.setServiceId(ordDtlsDto.getServiceId());
												}

										}
										ordDtlsDto.setTaxPer(contractResp.getTaxPercentage()!=null ? BigDecimal.valueOf(contractResp.getTaxPercentage()):BigDecimal.ZERO);
										ordDtlsDto.setServiceAmt(BigDecimal.valueOf(contractResp.getServiceRate()));
										ordDtlsDto.setOrdTotalAmt(BigDecimal.valueOf(contractResp.getServiceRate()!=null ? (contractResp.getServiceRate()*(ordDtlsDto.getOrderQty()!=null ? ordDtlsDto.getOrderQty():1)):0.00));
										ordDtlsDto.setCoPayPer(BigDecimal.valueOf(contractResp.getCoShare()));
										ordDtlsDto.setSelfPayable(BigDecimal.valueOf(contractResp.getSelfPayable()));
										ordDtlsDto.setCreditPayable(BigDecimal.valueOf(contractResp.getCreaditPayable()));
										ordDtlsDto.setOrdConcession(BigDecimal.valueOf(contractResp.getConcession() != null ? contractResp.getConcession() : 0));
										ordDtlsDto.setNetAmount(BigDecimal.valueOf(contractResp.getNetAmount()));
										ordDtlsDto.setOrdConcessionPer(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setPatientClassPercentage(BigDecimal.valueOf(contractResp.getPatientClassPercentage()));
										ordDtlsDto.setOrdDiscount(BigDecimal.valueOf(contractResp.getOrdDiscount()));
										ordDtlsDto.setOrderDetailsPayeeMapperDtosList(contractResp.getOrderDetailsPayeeMapperDtosList());
										ordDtlsDto.setTariffId(ordDtlsDto.getTariffId());
									}
								}
							}
							if(addToList)
								listForSave.add(ordDtlsDto);
								
						}
						
						if(ordDtlsDto.getStatus()!=null && ordDtlsDto.getStatus().equals('I'))
						{	
							if(ordDtlsDto.getSaleId()!=null && ordDtlsDto.getSaleId()>0)
							{
								iWalkInSalesService.revertAllocatedQty(ordDtlsDto.getSaleId().longValue(), 9,ordDtlsDto.getOrgUnitId(),null,null,null);
								iWalkInSalesService.inactiveSaleBySaleId(ordDtlsDto.getSaleId());
							}
							listForSave.add(ordDtlsDto);
						}
						if (billingMasterDto.getPharmacyServiceId() != null
								&& billingMasterDto.getPharmacyServiceId() > 0
								&& (ordDtlsDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId())
								&& (billingMasterDto.isNeedToUpdatePharmacyService() || (ordDtlsDto.getForceAddRemoved().equals(1) || ordDtlsDto.getForceAddRemoved().equals(-1)))) {
							
							Response getPackageOrderCount =  iEncounterMasterService.getPackageOrderCount(ordDtlsDto);
							
							BigInteger packageExist= BigInteger.ZERO;
							if(getPackageOrderCount.getObject() !=null) {
							 packageExist = (BigInteger)getPackageOrderCount.getObject();
							}
							
							
							if (ordDtlsDto.getOrdTotalAmt().doubleValue() > 0 /*&& ( packageExist.intValue() ==0 || billingMasterDto.isUpdatePayee())*/) {
								ordDtlsDto.setOrdDiscountPer(new BigDecimal("-1")); // assigning discount percentage to -1 it means the discount will get reset
								ordDtlsDto.setConcessionPer(null);
								List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
								pharamcyList.add(ordDtlsDto);
								iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
							} else if(ordDtlsDto.getOrderQty()<0){
								List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
								pharamcyList.add(ordDtlsDto);
								iWalkInSalesService.updatePayeeRetunService(pharamcyList, billingMasterDto);
							}
						}

					}
					
					if(billingMasterDto.isRecalculation() == true) {
						Response orderList = iBillingMasterService.adjustCapWithPackageAvailableAmount(billingMasterDto);
						
					}
					
					Response orderList = iBillingMasterService.checkCapWithPackageBalanceAmountAndOrderAmount(billingMasterDto);
					
					if(orderList.getListObject() != null && orderList.getListObject().size() >0 && billingMasterDto.isRecalculation() == true) {
						
						this.pharmacyReturnServiceConsumtion(contractDto,billingMasterDto,orderList,5);
					
					}
					
					billingMasterDto.setOrderDetailsMasterDtosList(listForSave);
					billingMasterDto.setIsFinalBill('N');
					billingMasterDto.setIsIntermediateBill('N');
					iBillingMasterService.saveBillService(billingMasterDto);
					if (res.getObject() != null) {
						Map<Integer, Integer> ehcPackageIds = (Map) res.getObject();
						if (ehcPackageIds != null) {
							for (Map.Entry<Integer, Integer> entry : ehcPackageIds.entrySet()) {
								if(billingMasterDto.getEncounterId()!=null && billingMasterDto.getEncounterId().intValue()>0) {
									EncounterMasterDto dto = billingCommonMethods.createEncounterMasterDto(billingMasterDto, entry);
									iMPackageMasterService.saveEhcEncounterEhcConsultation(dto);
								}
							}
						}
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}




	private void pharmacyReturnServiceConsumtion(ContractConsumptionDto contractDto,
			BillingMasterDto billingMasterDto, Response orderList, int count) {
		
		try {
			
		List<OrderDetailsMasterDto> getPharList = new ArrayList<OrderDetailsMasterDto>();
		getPharList.addAll(orderList.getListObject());
		List<OrderDetailsMasterDto> pharmacyRecalculationList = new ArrayList<OrderDetailsMasterDto>();
		Boolean chackInLoop = false;
		for (OrderDetailsMasterDto ordDtlsDto : contractDto.getListOrderDetailsMasterDto()) 
		{
			if (billingMasterDto.getPharmacyServiceId() != null
					&& billingMasterDto.getPharmacyServiceId() > 0
					&& (ordDtlsDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId())
					&& (billingMasterDto.isNeedToUpdatePharmacyService() || (ordDtlsDto.getForceAddRemoved().equals(1) || ordDtlsDto.getForceAddRemoved().equals(-1))
					&& ordDtlsDto.getForceAddRemoved().equals(0))) {
				
				for (OrderDetailsMasterDto orderDetailsDto : getPharList) 
				{
					if ( orderDetailsDto.getOrderDetailsId().equals(ordDtlsDto.getOrderDetailsId()) && ordDtlsDto.getOrdTotalAmt().doubleValue() > 0) {
						chackInLoop = true;
						pharmacyRecalculationList.add(ordDtlsDto);
					} else if(chackInLoop  && ordDtlsDto.getOrderQty()<0) {
						pharmacyRecalculationList.add(ordDtlsDto);
					}
				}
			}
		}
		
		if(pharmacyRecalculationList != null && pharmacyRecalculationList.size()>0) {
			
			ContractConsumptionDto contractDto1 = billingCommonMethods.createContractConsumptionForClone(billingMasterDto,pharmacyRecalculationList);
			ContractConsumptionDto contractDtoClone1 = contractDto1;
			// set pkg mppr list null
			for (OrderDetailsMasterDto ordDtls : contractDto1.getListOrderDetailsMasterDto()) 
			{
				if (ordDtls.getPackageTypeId() == null || ordDtls.getPackageTypeId() != PACKAGE1 ) {
					if (ordDtls.getProcedureOrderMstId() != null && ordDtls.getProcedureOrderMstId() > 0 && ordDtls.getIsProcedureService() == 'Y') 
					{
						contractDtoClone1.setServiceId(ordDtls.getProcedureOrderMstId());
						for (OrderDetailsPayeeMapperDto odPayeeDto : ordDtls.getOrderDetailsPayeeMapperDtosList())
							odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
						for (OrderDetailsPackageMapperDto odPayeeDto : ordDtls
								.getOrderDetailsPackageMapperDtosList())
							odPayeeDto.setServiceId(ordDtls.getProcedureOrderMstId());
					} else 
					{
						contractDtoClone1.setServiceId(ordDtls.getServiceId());
					}
					contractDtoClone1.setOrderDetailsPayeeMapperDtosList(ordDtls.getOrderDetailsPayeeMapperDtosList());
					contractDtoClone1.setOrderDetailsPackageMapperDtosList(ordDtls.getOrderDetailsPackageMapperDtosList());
					contractDtoClone1.setSpecialityId(ordDtls.getDepartmentId());
					contractDtoClone1.setEncounterId(billingMasterDto.getEncounterId());
					contractDtoClone1.setAdmissionId(billingMasterDto.getAdmissionId());
					contractDtoClone1.setContractId(billingMasterDto.getContractId());
					contractDtoClone1.setServiceChargeStatus(ordDtls.getIsChargedHalfDay()!=null && ordDtls.getIsChargedHalfDay()=='Y'?0:1);
					if(!billingMasterDto.isUpdatePayee()){
						 if(ordDtls.getServiceId().equals(billingMasterDto.getPharmacyServiceId()) && ordDtls.getOrdTotalAmt().doubleValue()>0){
							 iWalkInSalesService.cancelPharmacyPackageCapping(contractDtoClone1,ordDtls.getSaleId(),billingMasterDto.isRecalculation(),true);
						}
					}
					
					
				}
			}//end loop
			
			for (OrderDetailsMasterDto ordDtls : contractDto1.getListOrderDetailsMasterDto()) 
			{
				if (billingMasterDto.getPharmacyServiceId() != null
						&& billingMasterDto.getPharmacyServiceId() > 0
						&& (ordDtls.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId())
						&& (billingMasterDto.isNeedToUpdatePharmacyService() || (ordDtls.getForceAddRemoved().equals(1) || ordDtls.getForceAddRemoved().equals(-1)))) {
					if (ordDtls.getOrdTotalAmt().doubleValue() > 0) {
						ordDtls.setOrdDiscountPer(new BigDecimal("-1")); // assigning discount percentage to -1 it means the discount will get reset
						ordDtls.setConcessionPer(null);
						List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
						pharamcyList.add(ordDtls);
						billingMasterDto.setPharmacyReturnServiceRecal(1);
						iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
					} else {
						List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
						pharamcyList.add(ordDtls);
						billingMasterDto.setPharmacyReturnServiceRecal(1);
						iWalkInSalesService.updatePayeeRetunService(pharamcyList, billingMasterDto);
					}
				}
			}
			
		}
		
		if(billingMasterDto.isRecalculation() == true) {
			Response orde = iBillingMasterService.adjustCapWithPackageAvailableAmount(billingMasterDto);
			
		}
		
		Response orderList1 = iBillingMasterService.checkCapWithPackageBalanceAmountAndOrderAmount(billingMasterDto);
		
		Response res = iBillingMasterService.checkPackageMinusAmount(billingMasterDto);
		
		Boolean checkPackMinusAmount = false;
		
		if(res.getListObject() != null && res.getListObject().size()>0) {
			
			checkPackMinusAmount = true;
		}
		
		if(orderList1.getListObject() != null && orderList1.getListObject().size() >0 && billingMasterDto.isRecalculation() == true && count >0 ) {
		
			count = count - 1;
			
			this.pharmacyReturnServiceConsumtion(contractDto,billingMasterDto,orderList1,count);
		
		}else if(checkPackMinusAmount == true && count >0) {
			
			count = count - 1;
			
			this.pharmacyReturnServiceConsumtion(contractDto,billingMasterDto,orderList,count);
			
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	@RequestMapping(value = "/update/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response addService(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {
			return updateService(billingMasterDto);

        } catch (Exception e) {

			//throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	Response updateService(BillingMasterDto billingMasterDto)throws Exception{

		try {
			Boolean isFutureService = false;
			List<Integer> orderDetailsIds = new LinkedList();
			List<OrderDetailsMasterDto> listToBeAdded = billingMasterDto.getOrderDetailsMasterDtosList().stream().filter(p -> p.getOrderDetailsId() == null).collect(Collectors.toList());
			List<OrderDetailsMasterDto> listToBeUpdated = billingMasterDto.getOrderDetailsMasterDtosList().stream().filter(p -> p.getOrderDetailsId() != null).collect(Collectors.toList());
			Response res = null;
			if (!listToBeUpdated.isEmpty() && (billingMasterDto.isCapExist() || billingMasterDto.isPackageExist())) {
				for (OrderDetailsMasterDto detailsMasterDto : listToBeUpdated) {

					ContractConsumptionDto addCapDto = billingCommonMethods.createContractConsumption(billingMasterDto, detailsMasterDto);
					if (billingMasterDto.isCapExist() && billingMasterDto.isPackageExist())
						iContractConsumptionService.addCapAmount(addCapDto);
					else if (billingMasterDto.isCapExist()) iContractConsumptionService.addCapContract(addCapDto);
					else if (billingMasterDto.isPackageExist()) iContractConsumptionService.addCapPackage(addCapDto);

				}
			}


			OrderMasterDto orderMasterDto = billingCommonMethods.createOrderMasterDto(billingMasterDto, billingMasterDto.getCreatedDate(), null, true);

			List orderDetailsList = new ArrayList();
			if (!listToBeAdded.isEmpty()) {
				for (OrderDetailsMasterDto detailsMasterDto : listToBeAdded) {

					String pattern = "dd-MM-yyyy";
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					String date1 = sdf.format(detailsMasterDto.getOrderDate());
					String date2 = sdf.format(detailsMasterDto.getOrderSchDate());
					isFutureService = date2.compareTo(date1) > 0;
					ContractConsumptionDto consumptionDto = billingCommonMethods.consumeServiceFromPackageOrContract(billingMasterDto, detailsMasterDto);
					if (billingMasterDto.isPackageExist())
						res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
					else
						res = iContractConsumptionService.consumeServiceFromContract(consumptionDto);
					consumptionDto = (ContractConsumptionDto) res.getObject();
					if (res.getObject() != null) {
						detailsMasterDto.setFutureService(isFutureService);
						detailsMasterDto.setAdmissionId(!isFutureService?detailsMasterDto.getAdmissionId():null);
						detailsMasterDto.setEncounterId(!isFutureService?detailsMasterDto.getEncounterId():null);
						detailsMasterDto.setPatientClassPercentage(BigDecimal.valueOf(consumptionDto.getPatientClassPercentage() != null ? consumptionDto.getPatientClassPercentage() : 0.00));
						detailsMasterDto.setServiceAmt(BigDecimal.valueOf(consumptionDto.getServiceRate() ));
						detailsMasterDto.setDummyServiceAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
						detailsMasterDto.setTariffId(detailsMasterDto.getTariffId());
						detailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf((consumptionDto.getServiceRate() != null ? consumptionDto.getServiceRate() : 0.00) * (detailsMasterDto.getOrderQty() != null ? detailsMasterDto.getOrderQty() : 1)));
						detailsMasterDto.setOrdConcession( BigDecimal.valueOf(consumptionDto.getConcession() != null ? consumptionDto.getConcession() : 0.00));
						detailsMasterDto.setTaxId(consumptionDto.getTaxId());
						detailsMasterDto.setTaxAmount( BigDecimal.valueOf(consumptionDto.getTaxAmt() != null ? consumptionDto.getTaxAmt() : 0.00));
						detailsMasterDto.setNetAmount( BigDecimal.valueOf(consumptionDto.getFinalPrice() != null ? consumptionDto.getFinalPrice() : 0.00));
						detailsMasterDto.setTaxPer( BigDecimal.valueOf(consumptionDto.getTaxPercentage() != null ? consumptionDto.getTaxPercentage() : 0.00));
						detailsMasterDto.setSelfPayable( BigDecimal.valueOf(consumptionDto.getSelfPayable() != null ? consumptionDto.getSelfPayable() : 0.00));
						detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
						detailsMasterDto.setCreditPayable(BigDecimal.valueOf(consumptionDto.getCreaditPayable()));
						detailsMasterDto.setContractId(consumptionDto.getContractId());
						detailsMasterDto.setPackageConsumptionAmt( consumptionDto.getPackageConsumptionAmt() != null ? consumptionDto.getPackageConsumptionAmt() : BigDecimal.ZERO);
						detailsMasterDto.setCoPayPer(BigDecimal.valueOf(consumptionDto.getCoShare() != null ? consumptionDto.getCoShare() : 0.00));
						detailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						detailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
						detailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
						if (billingMasterDto.getIsBillManipulationAllowed()) {
							List<Integer> packageConsumptionMasterIds = detailsMasterDto.getOrderDetailsPackageMapperDtosList() != null
									&& !detailsMasterDto.getOrderDetailsPackageMapperDtosList().isEmpty()
									? detailsMasterDto.getOrderDetailsPackageMapperDtosList()
									.stream().map(OrderDetailsPackageMapperDto::getPackageConsumptionMasterId)
									.filter(Objects::nonNull)
									.collect(Collectors.toList()) : null;
							if (packageConsumptionMasterIds != null && !packageConsumptionMasterIds.isEmpty())
								detailsMasterDto.setDummyService(iOrderDetailsMasterService.getDummyServiceFlagForPackageService(packageConsumptionMasterIds));

						}
						detailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
						if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty())
							detailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						else
							detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
					}
					if (detailsMasterDto.getPatientCouponTemplateMapperId() != null) {
						iOrderMasterService.updateCouponConsumptionId(detailsMasterDto.getTariffId(), detailsMasterDto.getPatientCouponTemplateMapperId(), detailsMasterDto.getOrderDetailsId());
					}
					orderDetailsList.add(detailsMasterDto);
				}
			}

			if (!listToBeUpdated.isEmpty()) {
				for (OrderDetailsMasterDto detailsMasterDto : listToBeUpdated) {
					if (detailsMasterDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId()) {
						List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
						pharamcyList.add(detailsMasterDto);
						if (detailsMasterDto.getOrderQty() > 0) {
							detailsMasterDto.setConcessionPer(detailsMasterDto.getPatientClassPercentage().doubleValue());
							iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
						} else {
							if (detailsMasterDto.getOldNetPay().doubleValue() == 0.00) {
								iWalkInSalesService.updatePayeeRetunService(pharamcyList, billingMasterDto);
							} else {
								iWalkInSalesService.applyDiscountOnPharamcyReturn(pharamcyList, billingMasterDto);
							}
						}
						if (detailsMasterDto.getDiscountCategoryId() != null) {
							iWalkInSalesService.updatediscountCategory(detailsMasterDto);
						}
					} else if (detailsMasterDto.getIsPackageService() == 'Y') {
						orderDetailsList.add(detailsMasterDto);
					} else {
						ContractConsumptionDto consumptionDto;
						if (detailsMasterDto.isPackage()) {
							res = iContractConsumptionService.consumePackageFromContract(billingCommonMethods.createServiceSearchResDto(billingMasterDto, detailsMasterDto, true));
						} else {

							consumptionDto = billingCommonMethods.consumeServiceFromPackageOrContract(billingMasterDto, detailsMasterDto);
							if (billingMasterDto.isPackageExist() && detailsMasterDto.getForceAddRemoved() != (-1))
								res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
							else
								res = iContractConsumptionService.consumeServiceFromContract(consumptionDto);
						}
						consumptionDto = (ContractConsumptionDto) res.getObject();
						if (res != null || res.getObject() != null) {
							detailsMasterDto.setPatientClassPercentage(BigDecimal.valueOf(consumptionDto.getPatientClassPercentage() != null ? consumptionDto.getPatientClassPercentage() : 0.00));
							detailsMasterDto.setServiceAmt(BigDecimal.valueOf(consumptionDto.getServiceRate() != null ? consumptionDto.getServiceRate() : 0.00));
							detailsMasterDto.setDummyServiceAmt(BigDecimal.valueOf(consumptionDto.getServiceRate() != null ? consumptionDto.getServiceRate() : 0.00));
							detailsMasterDto.setTariffId(detailsMasterDto.getTariffId());
							detailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf((consumptionDto.getServiceRate() != null ? consumptionDto.getServiceRate() : 0.00) * (detailsMasterDto.getOrderQty() != null ? detailsMasterDto.getOrderQty() : 1)));
							detailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession() != null ? consumptionDto.getConcession() : 0.00));
							detailsMasterDto.setOrdDiscount(BigDecimal.valueOf(consumptionDto.getOrdDiscount()));
							detailsMasterDto.setTaxId(consumptionDto.getTaxId());
							detailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt() != null ? consumptionDto.getTaxAmt() : 0.00));
							detailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getFinalPrice() != null ? consumptionDto.getFinalPrice() : 0.00));
							detailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage() != null ? consumptionDto.getTaxPercentage() : 0.00));
							detailsMasterDto.setSelfPayable(BigDecimal.valueOf(consumptionDto.getSelfPayable() != null ? consumptionDto.getSelfPayable() : 0.00));
							detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							detailsMasterDto.setCreditPayable(BigDecimal.valueOf(consumptionDto.getCreaditPayable() != null ? consumptionDto.getCreaditPayable() : 0.00));
							detailsMasterDto.setContractId(consumptionDto.getContractId());
							detailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt() != null ? consumptionDto.getPackageConsumptionAmt() : BigDecimal.ZERO);
							detailsMasterDto.setCoPayPer(BigDecimal.valueOf(consumptionDto.getCoShare() != null ? consumptionDto.getCoShare() : 0.00));
							detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
							if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty())
								detailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
							else
								detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
						}
						if (detailsMasterDto.getPatientCouponTemplateMapperId() != null) {
							iOrderMasterService.updateCouponConsumptionId(detailsMasterDto.getTariffId(), detailsMasterDto.getPatientCouponTemplateMapperId(), detailsMasterDto.getOrderDetailsId());
						}
						orderDetailsList.add(detailsMasterDto);
					}
					orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
				}
			}
			Response savedRes = null;
			if (!orderDetailsList.isEmpty() && !listToBeAdded.isEmpty()) {
				orderMasterDto.setSlotMasterDtoList(billingMasterDto.getSlotMasterDtoList());
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				savedRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto, null);
				System.out.println("savedRes  "+savedRes);
				orderDetailsIds = savedRes.getListObject();
				// }
			}
			if (!orderDetailsList.isEmpty() && !listToBeUpdated.isEmpty()) {
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				iOrderDetailsMasterService.updateOrderDetails(orderDetailsList, null, (billingMasterDto.getLineLevelDiscountOrderList() != null) ? billingMasterDto.getLineLevelDiscountOrderList().size() : null, orderMasterDto);
			}
			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();
			serviceMasterDto.setEncounterId(billingMasterDto.getVisitTypeId() == OP ? billingMasterDto.getEncounterId() : billingMasterDto.getAdmissionId());
			serviceMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
			serviceMasterDto.setOrderDetailsIds(orderDetailsIds);
			serviceMasterDto.setIsBillManipulationAllowed(billingMasterDto.getIsBillManipulationAllowed());
			Response getList = iBillingMasterService.getOrderedServicesByEncounterId(serviceMasterDto);


			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getList.getListObject(), null, null,orderDetailsIds);

		} catch (Exception e) {

			//throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/forceAddOrRemove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response forceAddOrRemove(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {
			
			List<OrderDetailsMasterDto> listToBeUpdated = billingMasterDto.getOrderDetailsMasterDtosList().stream().filter(p->p.getOrderDetailsId()!=null).collect(Collectors.toList());
			Response res = null;
			List<Integer> orderDetailsIds = new LinkedList();
			for(OrderDetailsMasterDto detailsMasterDto : listToBeUpdated)
			{
				if(detailsMasterDto.getServiceId().intValue() != billingMasterDto.getPharmacyServiceId())
				{
					ContractConsumptionDto addCapDto = new ContractConsumptionDto();
						addCapDto.setAdmissionId(billingMasterDto.getAdmissionId());
						addCapDto.setEncounterId(billingMasterDto.getEncounterId());
						addCapDto.setOrderDetailsPayeeMapperDtosList(detailsMasterDto.getOrderDetailsPayeeMapperDtosList());
						addCapDto.setOrderDetailsPackageMapperDtosList(detailsMasterDto.getOrderDetailsPackageMapperDtosList());
						addCapDto.setSpecialityId(detailsMasterDto.getDepartmentId());
						addCapDto.setApplyAuth(billingMasterDto.isApplyAuth());
						addCapDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
						if (detailsMasterDto.getProcedureOrderMstId() != null && detailsMasterDto.getProcedureOrderMstId() > 0 && detailsMasterDto.getIsProcedureService() == 'Y') 
						{
							addCapDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
							for (OrderDetailsPayeeMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPayeeMapperDtosList())
								odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
							for (OrderDetailsPackageMapperDto odPayeeDto : detailsMasterDto.getOrderDetailsPackageMapperDtosList())
								odPayeeDto.setServiceId(detailsMasterDto.getProcedureOrderMstId());
						} else 
						{
							addCapDto.setServiceId(detailsMasterDto.getServiceId());
						}
					
					/*if(detailsMasterDto.getForceAddRemoved()==1 && billingMasterDto.isCapExist())	
						iContractConsumptionService.addCapContract(addCapDto);
					else if(detailsMasterDto.getForceAddRemoved()==-1)	
						iContractConsumptionService.addCapPackage(addCapDto);*/
				}else if(detailsMasterDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId()) {
					
						iBillingMasterService.storePackCapAmountLog(detailsMasterDto);
				}
				
				
			}
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
				orderMasterDto.setAdmissionId(billingMasterDto.getAdmissionId());
				orderMasterDto.setPatientId(billingMasterDto.getPatientId());
				orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
				orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
				orderMasterDto.setCreatedDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
				orderMasterDto.setOrderDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
				orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
				orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
				orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
				orderMasterDto.setReferralId(billingMasterDto.getReferralId());
				orderMasterDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
			List orderDetailsList = new ArrayList();
			
			if(listToBeUpdated.size()>0)
			{
				for(OrderDetailsMasterDto detailsMasterDto : listToBeUpdated)
				{
					if(billingMasterDto.getPharmacyServiceId() != null && billingMasterDto.getPharmacyServiceId()>0 
							&& (detailsMasterDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId()))
					{
						List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
						detailsMasterDto.setConcessionPer(0.00);
						detailsMasterDto.setOrdDiscountPer(BigDecimal.ZERO);
						pharamcyList.add(detailsMasterDto);
						orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
						if(detailsMasterDto.getOrdTotalAmt().doubleValue()>=0)
						{
							iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
							orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
						}else{
							Response resRet = iWalkInSalesService.updatePayeeRetunService(pharamcyList, billingMasterDto);
							orderDetailsIds.add((Integer) resRet.getObject());
						}
					}else{
						if(detailsMasterDto.getForceAddRemoved()==1)
						{
							detailsMasterDto.setPackageConsumptionAmt(detailsMasterDto.getOrdTotalAmt());
							detailsMasterDto.setPatientClassPercentage(BigDecimal.ZERO);
							detailsMasterDto.setOrdConcession(BigDecimal.ZERO);
							detailsMasterDto.setTaxAmount(BigDecimal.ZERO);
							detailsMasterDto.setNetAmount(BigDecimal.ZERO);
							detailsMasterDto.setSelfPayable(BigDecimal.ZERO);
							detailsMasterDto.setCreditPayable(BigDecimal.ZERO);
							detailsMasterDto.setCoPayPer(BigDecimal.ZERO);
							detailsMasterDto.setOrdDiscountPer(BigDecimal.ZERO);
							detailsMasterDto.setOrdDiscount(BigDecimal.ZERO);
							detailsMasterDto.setIsPackageService('Y');
							if(billingMasterDto.getIsBillManipulationAllowed())
							{
								List<Integer> packageConsumptionMasterIds = detailsMasterDto.getOrderDetailsPackageMapperDtosList()!=null && detailsMasterDto.getOrderDetailsPackageMapperDtosList().size()>0 
										? detailsMasterDto.getOrderDetailsPackageMapperDtosList().stream().filter(obj->obj.getPackageConsumptionMasterId()!=null).map(obj->obj.getPackageConsumptionMasterId()).collect(Collectors.toList()) : null;
								if(packageConsumptionMasterIds!=null && packageConsumptionMasterIds.size()>0)
									detailsMasterDto.setDummyService(iOrderDetailsMasterService.getDummyServiceFlagForPackageService(packageConsumptionMasterIds));
							}else{
								detailsMasterDto.setDummyService(false);		
							}
							detailsMasterDto.setHiddenService(false);
							OrderDetailsPayeeMapperDto dto = new OrderDetailsPayeeMapperDto();
								dto.setOrderDetailsId(detailsMasterDto.getOrderDetailsId());
								dto.setPayeeAmount(detailsMasterDto.getSelfPayable());
								dto.setPayeeId(billingMasterDto.getSelfPayeeId());
								dto.setStatus('A');
								dto.setOrganizationId(billingMasterDto.getOrganisationId());
								dto.setUnitId(billingMasterDto.getUnitId());
							List<OrderDetailsPayeeMapperDto> list = new ArrayList<>();
								list.add(dto);
							detailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
							
						}else if(detailsMasterDto.getForceAddRemoved()==-1)
						{
							ContractConsumptionDto consumptionDto=new ContractConsumptionDto();
								consumptionDto.setOrganizationId(billingMasterDto.getOrganisationId());
								consumptionDto.setUnitId(billingMasterDto.getUnitId());
								consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
								consumptionDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
								consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
								consumptionDto.setPaymentEntitlementId(SELF);
								consumptionDto.setServiceId(detailsMasterDto.getServiceId());
								consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
								consumptionDto.setContractId(billingMasterDto.getContractId());
								consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
								consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
								consumptionDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
								consumptionDto.setPatientId(billingMasterDto.getPatientId());
								consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
							    consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());	
							    consumptionDto.setServiceRate(detailsMasterDto.getServiceAmt().doubleValue());
							    consumptionDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
							    consumptionDto.setSpecialityId(detailsMasterDto.getDepartmentId());
							    consumptionDto.setSubSpecialityId(detailsMasterDto.getSubDepartmentId());
							    consumptionDto.setOrdDiscountPer(detailsMasterDto.getOrdDiscountPer().doubleValue());
							    consumptionDto.setPatientClassPercentage(detailsMasterDto.getPatientClassPercentage().doubleValue());
							    consumptionDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
						
							res =iContractConsumptionService.consumeServiceFromContract(consumptionDto);
							
							consumptionDto=(ContractConsumptionDto)res.getObject();
							
							if(res.getObject()!=null)
							{
								detailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()!=null ? consumptionDto.getPatientClassPercentage():0.00));
								detailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate() : 0.00));
								detailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate():0.00));
								detailsMasterDto.setTariffId(billingMasterDto.getTariffId());
								detailsMasterDto.setOrdTotalAmt(new BigDecimal((consumptionDto.getServiceRate()!=null?consumptionDto.getServiceRate():0.00)*(detailsMasterDto.getOrderQty()!=null ? detailsMasterDto.getOrderQty():1)));
								detailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()!=null ? consumptionDto.getConcession():0.00));
								detailsMasterDto.setTaxId(consumptionDto.getTaxId());
								detailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()!=null ? consumptionDto.getTaxAmt():0.00));
								detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
								detailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()!=null ? consumptionDto.getTaxPercentage():0.00));
								detailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()!=null?consumptionDto.getSelfPayable():0.00));
								detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
								detailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()!=null?consumptionDto.getCreaditPayable():0.00));
								detailsMasterDto.setContractId(consumptionDto.getContractId());
								detailsMasterDto.setPackageConsumptionAmt(BigDecimal.ZERO);
								detailsMasterDto.setIsPackageService('N');
								detailsMasterDto.setDummyService(false);		
								detailsMasterDto.setHiddenService(false);
								detailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()!=null?consumptionDto.getCoShare():0.00));
								detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
								detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
							}
						}
						orderDetailsList.add(detailsMasterDto);
						orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
					}
					
				}
			}
			if(orderDetailsList.size()>0 && listToBeUpdated.size()>0)
			{
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				iOrderDetailsMasterService.updateOrderDetails(orderDetailsList, null,(billingMasterDto.getLineLevelDiscountOrderList()!=null)?billingMasterDto.getLineLevelDiscountOrderList().size():null, orderMasterDto);
			}
			
			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();
				serviceMasterDto.setEncounterId(billingMasterDto.getVisitTypeId()==OP ? billingMasterDto.getEncounterId():billingMasterDto.getAdmissionId());
				serviceMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				serviceMasterDto.setOrderDetailsIds(orderDetailsIds);
			Response getList = iBillingMasterService.getOrderedServicesByEncounterId(serviceMasterDto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, getList.getListObject(), null);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	/**
	 * get Bills By Filter For Bill List
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/badDebitList/filter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBadDebitListByFilterForBadDebitList(@RequestBody BillListReqDto reqDto) {
		try {
			return iBillingMasterService.getBadDebitListByFilterForBadDebitList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "requestForBadDebit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response requestForBadDebit(@RequestBody BillingMasterDto billingMasterDto) {
		try {

			Response billRes = iBillingMasterService.requestForBadDebit(billingMasterDto);
			return billRes;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/preAuth/approval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response reshuffleListAfterPreAuth(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {

			UpdatePayeeDto updatePayeeDto = new UpdatePayeeDto();
			UnitApplicationConfigurationDto configDto = new UnitApplicationConfigurationDto();
			configDto.setOrganizationId(billingMasterDto.getOrganisationId());
			configDto.setUnitId(billingMasterDto.getUnitId());
			com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(configDto);
			if (configRes.getObject() != null) {
				configDto = (UnitApplicationConfigurationDto) configRes.getObject();
			}
			if (billingMasterDto.getAdmissionId() != null) {
				Response res = ipdChargesService.getAdmissionsById(billingMasterDto.getAdmissionId());
				AdmissionListIpdChargesResDto masterDto = (AdmissionListIpdChargesResDto) res.getObject();

				billingMasterDto.setPatientId(masterDto.getPatientId());
				billingMasterDto.setVisitTypeId(masterDto.getVisitTypeId());
				billingMasterDto.setAdmissionId(masterDto.getAdmissionId());
				billingMasterDto.setPaymentEntitlementId(masterDto.getPaymentEntitlementId());
				billingMasterDto.setPatientTypeId(masterDto.getPatientCategoryId());
				billingMasterDto.setBillingBedCategoryId(masterDto.getBedBillingCategoryId());
				billingMasterDto.setContractId(masterDto.getContractId());
				
				
				updatePayeeDto.setContractId(masterDto.getContractId());
				updatePayeeDto.setPatientCategoryId(masterDto.getPatientCategoryId());
				updatePayeeDto.setVisitTypeId(masterDto.getVisitTypeId());
				updatePayeeDto.setSelfPayeeId(configDto.getSelfPayeeId());
				updatePayeeDto.setDefaultSelfTariffId(configDto.getDefaultSelfTariffId());
				updatePayeeDto.setAdmissionId(masterDto.getAdmissionId());
				updatePayeeDto.setBillingBedCategoryId(masterDto.getBedBillingCategoryId());
				updatePayeeDto.setIsPreBilling(configDto.getIsPreOrPostBilling());
				updatePayeeDto.setPatientId(masterDto.getPatientId());
				updatePayeeDto.setOrgId(billingMasterDto.getOrganisationId());
				updatePayeeDto.setUnitId(billingMasterDto.getUnitId());
				updatePayeeDto.setReferralId(masterDto.getReferralId());
				updatePayeeDto.setPatientClassId(masterDto.getPatientClassId());
				updatePayeeDto.setPharmacyServiceId(configDto.getPharmacyServiceId());
				
			}
			if (billingMasterDto.getEncounterId() != null) {
				Response res = iEncounterMasterService.getEncounterDetailsById(billingMasterDto.getEncounterId());
				EncounterMasterDto masterDto = (EncounterMasterDto) res.getObject();

				billingMasterDto.setPatientId(masterDto.getPatientId());
				billingMasterDto.setVisitTypeId(OP);
				billingMasterDto.setEncounterId(masterDto.getEncounterId());
				billingMasterDto.setPaymentEntitlementId(masterDto.getPaymentEntitlementTypeId());
				billingMasterDto.setPatientTypeId(masterDto.getPatientCategoryId());
				// billingMasterDto.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
				billingMasterDto.setContractId(masterDto.getContractId());
				
				updatePayeeDto.setContractId(masterDto.getContractId());
				updatePayeeDto.setPatientCategoryId(masterDto.getPatientCategoryId());
				updatePayeeDto.setVisitTypeId(OP);
				updatePayeeDto.setSelfPayeeId(configDto.getSelfPayeeId());
				updatePayeeDto.setDefaultSelfTariffId(configDto.getDefaultSelfTariffId());
				updatePayeeDto.setBillingBedCategoryId(configDto.getOpdBillingCatId());
				updatePayeeDto.setIsPreBilling(configDto.getIsPreOrPostBilling());
				updatePayeeDto.setPatientId(masterDto.getPatientId());
				updatePayeeDto.setOrgId(billingMasterDto.getOrganisationId());
				updatePayeeDto.setUnitId(billingMasterDto.getUnitId());
				updatePayeeDto.setReferralId(masterDto.getReferralId());
				updatePayeeDto.setPatientClassId(masterDto.getPatientClassId());
				updatePayeeDto.setPharmacyServiceId(configDto.getPharmacyServiceId());
			}
			
			Response serviceRes =iBillingMasterService.getRenderedServicesByEncounterIdForBillSave(
					billingMasterDto.getEncounterId() != null ? billingMasterDto.getEncounterId()
							: billingMasterDto.getAdmissionId(),
							billingMasterDto.getVisitTypeId(), false, billingMasterDto.getIsPreBilling());
			
			List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = serviceRes.getListObject();
			List<OrderDetailsByEncounterIdDto> packageList = orderDetailsByEncounterIdDtosList.stream().filter(p->p.getIsPackage()==true).collect(Collectors.toList());
			
			for(OrderDetailsByEncounterIdDto dto: orderDetailsByEncounterIdDtosList)
			{
				ContractConsumptionDto addCapDto = new ContractConsumptionDto();
					addCapDto.setAdmissionId(billingMasterDto.getAdmissionId());
					addCapDto.setEncounterId(billingMasterDto.getEncounterId());
					addCapDto.setOrderDetailsPayeeMapperDtosList(dto.getOrderDetailsPayeeMapperDtosList());
					addCapDto.setOrderDetailsPackageMapperDtosList(dto.getOrderDetailsPackageMapperDtosList());
					addCapDto.setSpecialityId(dto.getServiceSpecialityId());
					addCapDto.setServiceId(dto.getServiceMasterId());
				iContractConsumptionService.addCapContract(addCapDto);
			}
			iBillingMasterService.updatePackageCoverSheet(billingMasterDto, billingMasterDto.getIsPreBilling(),false,true);
			
			iBillingDetailsService.updatePayeeForPackage(updatePayeeDto,orderDetailsByEncounterIdDtosList,packageList,null);
			
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/changePatientClassId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response changePatientClassId(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iBillingMasterService.changePatientClassId(serviceMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
	

/*	 @RequestMapping(value = "/mylife/sync/appointment/test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String scheduleAppointmentReqDto) {
		
		
		Response<ScheduleAppointmentReqDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(scheduleAppointmentReqDto, ScheduleAppointmentReqDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		}
	}*/
 
 
	
	
	@RequestMapping(value = "/mylife/sync/appointment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response saveAppointmentFromMyLife(@RequestBody ScheduleAppointmentReqDto scheduleAppointmentReqDto) throws Exception {
		try {
			EncounterMasterDto encounterMasterDto=null;
			SlotMasterDto slotMasterDtoTest=null;
			EncounterMasterDto encounterMasterDtoTest=null;
			Response appRes =iAppointmentService.confirmAppointment(scheduleAppointmentReqDto);
			
			  
				if(scheduleAppointmentReqDto.getAppointmentId()!=null) 
				{
					
					Integer sjjs = (Integer) appRes.getObject();
					  if(appRes.getStatus().equals(SUCCESS)){
						  SlotMasterDto slotMasterDtoss = new SlotMasterDto();
						  Integer appointmentUniqueId  = null;
						  
						  slotMasterDtoss.setAppointmentId(sjjs);
						  Response appResapp =iAppointmentService.getAppointmentUniqueId(slotMasterDtoss);
						  
						   slotMasterDtoTest=(SlotMasterDto)appResapp.getObject();
						   
						   if(slotMasterDtoTest != null) {
						   appointmentUniqueId  = slotMasterDtoTest.getAppointmentUniqueId() != null ? slotMasterDtoTest.getAppointmentUniqueId() : null;
						   }
						  
		                  SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
	                      smsInfo.setOrganizationId(scheduleAppointmentReqDto.getHospitalId());
	                      smsInfo.setUnitId(scheduleAppointmentReqDto.getUnitId());
	                      smsInfo.setDateTime(scheduleAppointmentReqDto.getAppointmentDate() != null
	                		  ? (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
	                                  + scheduleAppointmentReqDto.getTimeSlot())
	                          : (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
	                                  + scheduleAppointmentReqDto.getTimeSlot()));
	                      smsInfo.setPatientId(scheduleAppointmentReqDto.getPatientId());
	                      smsInfo.setConsultingDoctorId(scheduleAppointmentReqDto.getDoctorId());
	                      smsInfo.setAppointmentId(sjjs);
	                      smsInfo.setAppointmentUniqueId(appointmentUniqueId != null ? appointmentUniqueId : null);
	                  smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
	                  smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
	                  
	              }
					  
					return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Confirmed", null, String.valueOf(appRes.getObject()));
				}else if(appRes.getResObject() != null)
				{
					
					Integer sjjs = (Integer) appRes.getResObject();
					  if(appRes.getStatus().equals(SUCCESS)){
						  SlotMasterDto slotMasterDtoss = new SlotMasterDto();
						  Integer appointmentUniqueId  = null;
						  
						  slotMasterDtoss.setAppointmentId(sjjs);
						  Response appResapp =iAppointmentService.getAppointmentUniqueId(slotMasterDtoss);
						  
						   slotMasterDtoTest=(SlotMasterDto)appResapp.getObject();
						   
						   if(slotMasterDtoTest != null) {
						   appointmentUniqueId  = slotMasterDtoTest.getAppointmentUniqueId() != null ? slotMasterDtoTest.getAppointmentUniqueId() : null;
						   }
		                  SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
	                      smsInfo.setOrganizationId(scheduleAppointmentReqDto.getHospitalId());
	                      smsInfo.setUnitId(scheduleAppointmentReqDto.getUnitId());
	                      smsInfo.setDateTime(scheduleAppointmentReqDto.getAppointmentDate() != null
	                		  ? (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
	                                  + scheduleAppointmentReqDto.getTimeSlot())
	                          : (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
	                                  + scheduleAppointmentReqDto.getTimeSlot()));
	                      smsInfo.setPatientId(scheduleAppointmentReqDto.getPatientId());
	                      smsInfo.setConsultingDoctorId(scheduleAppointmentReqDto.getDoctorId());
	                      smsInfo.setAppointmentId(sjjs);
	                      smsInfo.setAppointmentUniqueId(appointmentUniqueId != null ? appointmentUniqueId : null);
	                  smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
	                  smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
	                  
	              }
					return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Confirmed", null, String.valueOf(appRes.getResObject()));
				}else{
					encounterMasterDto=(EncounterMasterDto)appRes.getObject();
					if(encounterMasterDto != null) {
					String test = encounterMasterDto.getAppointmentObject().getAppointmentDate();
					String testtime = encounterMasterDto.getAppointmentObject().getAppointmentDate();
					Response res = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
					if (res.getStatus().equals(SUCCESS)) 
					{
						BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) res.getResObject();
						reqDto.setIsPaidAppointment('Y');
						if(scheduleAppointmentReqDto.getIsVideo()!=null && scheduleAppointmentReqDto.getIsVideo()==1) {
							reqDto.setTeleConsultApp(true);
						}
						
						iBillingMasterService.collectPayment(reqDto);
					}

					 if(appRes.getStatus().equals(SUCCESS) && encounterMasterDto.getDoctorId()!=null){
						  SlotMasterDto slotMasterDtoss = new SlotMasterDto();
						  Integer appointmentUniqueId  = null;
						  
						  slotMasterDtoss.setAppointmentId(encounterMasterDto.getAppointmentId());
						  Response appResapp =iAppointmentService.getAppointmentUniqueId(slotMasterDtoss);
						  
						   slotMasterDtoTest=(SlotMasterDto)appResapp.getObject();
						   
						   if(slotMasterDtoTest != null) {
						   appointmentUniqueId  = slotMasterDtoTest.getAppointmentUniqueId() != null ? slotMasterDtoTest.getAppointmentUniqueId() : null;
						   }
						   
		                  SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
		                  smsInfo.setOrganizationId(scheduleAppointmentReqDto.getHospitalId());
	                      smsInfo.setUnitId(encounterMasterDto.getUnitId());
	                      smsInfo.setDateTime(scheduleAppointmentReqDto.getAppointmentDate() != null
		                		  ? (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
		                                  + scheduleAppointmentReqDto.getTimeSlot())
		                          : (scheduleAppointmentReqDto.getAppointmentDate().substring(0, 10) + " at "
		                                  + scheduleAppointmentReqDto.getTimeSlot()));
		                      smsInfo.setPatientId(encounterMasterDto.getPatientId());
		                      smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId());
		                      smsInfo.setAppointmentId(encounterMasterDto.getAppointmentId());
		                      smsInfo.setAppointmentUniqueId(appointmentUniqueId != null ? appointmentUniqueId : null);
		                  smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
		                  smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
					 }
		                  
		              }
					return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Confirmed", null, String.valueOf(encounterMasterDto.getAppointmentId()));
				}
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getOrderDetailsPayeeData/{orderDetailsId}", method = RequestMethod.GET)
	public  @ResponseBody Response getOrderDetailsPayeeData(@PathVariable("orderDetailsId") int orderDetailsId) {
		try {
			return iBillingMasterService.getOrderDetailsPayeeData(orderDetailsId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/concessionManager/billDetails/{orgId}/{unitId}/{patientId}", method = RequestMethod.GET)
	public Response billListByPatientIdForConcessionManager(@PathVariable("orgId") int orgId, @PathVariable("unitId") int unitId,@PathVariable("patientId") int patientId) {
		try {
			return iBillingMasterService.billListByPatientIdForConcessionManager(orgId, unitId, patientId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/concessionManager/finalUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response updateBillFromConcessionManager(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iBillingMasterService.updateBillFromConcessionManager(billingMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}
	
	

	@RequestMapping(value="/generateCyclicServiceAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateCyclicServiceAppointment(@RequestBody List<SlotMasterDto> slotMasterDto) throws Exception {
		try {
			
			
			Response res= iBillingMasterService.generateCyclicServiceAppointment(slotMasterDto);
			
			if (res.getStatus().equals(SUCCESS)) {
				for (SlotMasterDto dto : slotMasterDto) {
					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(dto.getUnitId());
					smsInfo.setOrganizationId(dto.getOrganizationId());
					smsInfo.setPatientId(dto.getPatientId());
					smsInfo.setConsultingDoctorId(dto.getDoctorId());
					smsInfo.setTreatingDoctorId(dto.getDoctorId());
					smsInfo.setTestDesc(dto.getServiceStandardName());
					smsInfo.setCyclicOrderDate(dto.getCyclicOrdScheduledDate()+" "+dto.getAppointmentTime()+":00");
					smsInfo.setUniqueId((dto.getEncounterId()!=null ? dto.getEncounterId().toString() :dto.getAdmissionId()).toString());
					smsInfo.setReferringDoctorId(null);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER, smsInfo);

					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.CYCLIC_ORDER, smsInfo);

				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cancelBill", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelBill(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			

			if (billingMasterDto.getBillingMasterId() != null) 
			{
						Response resCcavenue = iBillingMasterService.checkBillCancelledOrNot(billingMasterDto);
						if (resCcavenue.getObject() != null) 
						{
							return new Response<>(ERROR, ERROR_CODE, " This already used in other trancation", null, null);
						}
			}
			
			return iBillingMasterService.cancelBill(billingMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/cancelReceipt", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelReciept(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iBillingMasterService.cancelReciept(billingMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/billCancellation/filter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillListByFilterForbillCancellationList(@RequestBody BillListReqDto reqDto) {
		try {
			return iBillingMasterService.getBillListByFilterForbillCancellationList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancelledBill/filter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillsByFilterForCancelledBillList(@RequestBody BillListReqDto reqDto) {
		try {
			return iBillingMasterService.getBillsByFilterForCancelledBillList(reqDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
//			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/order/docShare", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDocShareForLineItem(@RequestBody OrderMasterDto orderMasterDto) {
		try {
			/*JsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return null;*/
			return iOrderMasterService.getDocShareForLineItem(orderMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/printLog/{type}/{userId}/{billId}", method = RequestMethod.GET)
	public @ResponseBody Response printLog(@PathVariable int type, @PathVariable int userId,@PathVariable int billId) {
		try {
			return iBillingMasterService.printLog(type,userId,billId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/receipt/printLog/{type}/{userId}/{recieptId}", method = RequestMethod.GET)
	public @ResponseBody Response receiptPrintLog(@PathVariable int type, @PathVariable int userId,@PathVariable int recieptId) {
		try {
			return iBillingMasterService.receiptPrintLog(type,userId,recieptId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/triggerMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response triggerMail(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {
			
			
			if (billingMasterDto.getVisitTypeId() == OP) {
				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setBillId(billingMasterDto.getBillingMasterId());
				smsInfo.setPatientId(billingMasterDto.getPatientId());
				smsInfo.setTreatingDoctorId(billingMasterDto.getOrdDoctorId());
				smsInfo.setBillAmount(new BigDecimal(billingMasterDto.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setSelfAmount(
						new BigDecimal(billingMasterDto.getSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setCreditAmount(
						new BigDecimal(billingMasterDto.getCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setOutstandingAmount(
						new BigDecimal(billingMasterDto.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setDateTime(billingMasterDto.getOrderDate());
				smsInfo.setReferringDoctorId(billingMasterDto.getReferralId() != null ? billingMasterDto.getReferralId() : 0);
				smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
				smsInfo.setOrgId(billingMasterDto.getOrganisationId());
				smsInfo.setUnitId(billingMasterDto.getUnitId());
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.BILLLISTOP, smsInfo);
			}else {

				SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
				smsInfo.setUnitId(billingMasterDto.getUnitId());
				smsInfo.setBillId(billingMasterDto.getBillingMasterId());
				smsInfo.setPatientId(billingMasterDto.getPatientId());
				smsInfo.setTreatingDoctorId(billingMasterDto.getOrdDoctorId());
				smsInfo.setBillAmount(new BigDecimal(billingMasterDto.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setSelfAmount(
						new BigDecimal(billingMasterDto.getSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setCreditAmount(
						new BigDecimal(billingMasterDto.getCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setOutstandingAmount(
						new BigDecimal(billingMasterDto.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				smsInfo.setDateTime(billingMasterDto.getOrderDate());
				smsInfo.setReferringDoctorId(billingMasterDto.getReferralId() != null ? billingMasterDto.getReferralId() : 0);
				smsInfo.setOrganizationId(billingMasterDto.getOrganisationId());
				smsInfo.setOrgId(billingMasterDto.getOrganisationId());
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.BILLLISTIP, smsInfo);
			}
			
			
			return new Response(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getBillData/{encounterId}", method = RequestMethod.GET)
	public @ResponseBody AdmissionsForBatchProcessDto getBillDataByEncounterId(@PathVariable int encounterId) {
		try {
			return globalBillingService.getBillByEncounterId(encounterId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillData/admission/{admissionId}", method = RequestMethod.GET)
	public @ResponseBody AdmissionsForBatchProcessDto getBillDataByAdmissionId(@PathVariable int admissionId) {
		try {
			return globalBillingService.getBillDataByAdmissionId(admissionId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSmartCardBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response getSmartCardBalance(@RequestBody SmartcardClaimDto smartcardClaimDto) throws Exception {
		try {

			return iBillingMasterService.getSmartCardBalance(smartcardClaimDto);

		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updatePayee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response updatePayee(@RequestBody UpdatePayeeDto masterDto) throws Exception {
		try {
			 return iBillingMasterService.updatePayee(masterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/getBillAdditionalData/{billId}", method = RequestMethod.GET)
	public  Response getBillAdditionalData(@PathVariable("billId") int billId) throws Exception {
		try {
			BillListReqDto reqDto=new BillListReqDto();
			reqDto.setBillingMasterId(billId);
			return iBillingMasterService.getBillReferenceData(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updatePackageEffectiveDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response updatePackageEffectiveDate(@RequestBody TPackageConsumptionMasterDto tPackageConsumptionMasterDto) {
		try {
			return iBillingMasterService.updatePackageEffectiveDate(tPackageConsumptionMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/printReport/printReceiptRPTReport/{orgId}/{unitId}/{billingMasterId}/{receiptId}/{depositConsumptionId}", method = RequestMethod.GET)
	public @ResponseBody Response printReceiptRPTReport(@PathVariable int orgId,@PathVariable int unitId,
			@PathVariable int billingMasterId, @PathVariable int receiptId,@PathVariable int depositConsumptionId) {
		try {
			return iBillingMasterService.printReceiptRPTReport(orgId,unitId,billingMasterId,receiptId,depositConsumptionId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
}	
		
		@RequestMapping(value = "/cashPaidAmount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response cashPaidAmount(@RequestBody OrderMasterDto orderMasterDto) {
			try {
				return iOrderMasterService.cashPaidAmount(orderMasterDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
				//throw new BillingException(e.getMessage());
			}
		}
	/**
	 * @param billingMasterDto
	 * @return
	 */
	@RequestMapping(value = "/orderplaceFromMobileApp/service", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response orderplaceForService(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			
			List<Integer> orderDetailsIds = new LinkedList();
			List<OrderDetailsMasterDto> listToBeAdded =new ArrayList();
			List<OrderDetailsMasterDto> listToBeUpdated = new ArrayList();
			Response res = null;
			
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(billingMasterDto.getOrganisationId());
			unitApplicationConfigurationDto.setUnitId(billingMasterDto.getUnitId());
			
			Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

			
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
			billingMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
			billingMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
			billingMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setOrderDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setBillDateTime(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(unitApplicationConfigurationDto.getSelfPayeeId());// need to ask
			billingMasterDto.setPatientTypeId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			billingMasterDto.setContractId(null);
			billingMasterDto.setHomeCollection(false);
			billingMasterDto.setApplyAuth(false);
			billingMasterDto.setNeedToUpdatePharmacyService(false);
			billingMasterDto.setUpdatePayee(false);
			billingMasterDto.setIsPreBilling(unitApplicationConfigurationDto.getIsPreOrPostBilling());
			billingMasterDto.setCapExist(false);
			billingMasterDto.setPackageExist(false);
			billingMasterDto.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			billingMasterDto.setSurchargeServiceId(unitApplicationConfigurationDto.getSurchargeServiceId());
			billingMasterDto.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
			billingMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setSelfPayable(billingMasterDto.getTotalBillAmount());
			/*List<BillPayeeDetailsDto> billPayeeDetailsDtosList = new ArrayList<>();
		
			BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
			billPayeeDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			billPayeeDetailsDto.setBillAmount(billingMasterDto.getTotalBillAmount());
			billPayeeDetailsDto.setOrgId(billingMasterDto.getOrganisationId());
			billPayeeDetailsDto.setUnitId(billingMasterDto.getUnitId());
			billPayeeDetailsDtosList.add(billPayeeDetailsDto);
			billPayeeDetailsDto.setCreatedDate(billingMasterDto.getCreatedDate());
			billPayeeDetailsDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
			billPayeeDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
			billPayeeDetailsDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
			billPayeeDetailsDto.setStatus('A');
			billingMasterDto.setBillPayeeDetailsDtosList(billPayeeDetailsDtosList);*/
			
			
			
			//create orderDetailsMaster list
			for (OrderDetailsMasterDto orderMstrdtls : billingMasterDto.getOrderDetailsMasterDtosList()) {
				orderMstrdtls.setDiscountOwnership(0);
				ContractConsumptionDto contrcDto = new ContractConsumptionDto();
				
				contrcDto.setOrganizationId(billingMasterDto.getOrganisationId());
				contrcDto.setUnitId(billingMasterDto.getUnitId());
				contrcDto.setAdmissionId(null);
				contrcDto.setEncounterId(billingMasterDto.getEncounterId());
				contrcDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				contrcDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
				contrcDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				contrcDto.setServiceId(orderMstrdtls.getServiceId());
				contrcDto.setTariffId(billingMasterDto.getTariffId());
				contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
				Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
				
				if(serviceRateRes.getObject()!=null) {
					ContractConsumptionDto 	consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
					orderMstrdtls.setRate(consumptionDto.getFinalPrice());
					orderMstrdtls.setDiscountOwnership(0);
					orderMstrdtls.setMarkupStatus(consumptionDto.getMarkupStatus());
					orderMstrdtls.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
					orderMstrdtls.setOrgId(billingMasterDto.getOrganisationId());
					orderMstrdtls.setOrgUnitId(billingMasterDto.getUnitId());
					orderMstrdtls.setOrderQty(1);
					orderMstrdtls.setPriorityId(consumptionDto.getPriorityId());
					orderMstrdtls.setIsOutsourced('N');
					orderMstrdtls.setOrderDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setOrderSchDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setPayeeId(consumptionDto.getPayeeId());
					orderMstrdtls.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
					orderMstrdtls.setBillingClassId(1);
					orderMstrdtls.setServiceRendered((consumptionDto.getIsAutoRender()!=null && consumptionDto.getIsAutoRender()=='Y')? 1 :0);
					orderMstrdtls.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId());
					orderMstrdtls.setOrdRenderDatetime(billingMasterDto.getCreatedDate());
					orderMstrdtls.setServiceIsBilled(0);
					orderMstrdtls.setStatus('A');
					orderMstrdtls.setOldNetPay(new BigDecimal(0));
					orderMstrdtls.setOrdCancelled('N');
					orderMstrdtls.setOrgId(billingMasterDto.getOrganisationId());
					orderMstrdtls.setOrgUnitId(billingMasterDto.getUnitId());
					orderMstrdtls.setCreatedBy(billingMasterDto.getCreatedBy());
					orderMstrdtls.setUpdatedBy(billingMasterDto.getUpdatedBy());
					orderMstrdtls.setCreatedDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setUpdatedDate(billingMasterDto.getUpdatedDate());
					orderMstrdtls.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
					orderMstrdtls.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
					orderMstrdtls.setIsProcedureService('N');
					orderMstrdtls.setIsPostedWhileDischarge('N');
					orderMstrdtls.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
					orderMstrdtls.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
					orderMstrdtls.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
					orderMstrdtls.setOrdDiscount(new BigDecimal(consumptionDto.getOrdDiscount()));
					orderMstrdtls.setOrdDiscountPer(new BigDecimal(consumptionDto.getOrdDiscountPer()));
					orderMstrdtls.setIsFromAddservice('N');
					orderMstrdtls.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
					orderMstrdtls.setCreditPayable(new BigDecimal(0));
					orderMstrdtls.setOldCreditPayable(new BigDecimal(0));
					orderMstrdtls.setOldSelfPayable(new BigDecimal(0));
					orderMstrdtls.setTaxId(consumptionDto.getTaxId());
					orderMstrdtls.setIsDiscountApplicable('N');
					orderMstrdtls.setOrderEmergencyFlag('N');
					orderMstrdtls.setIsDrug('N');
					orderMstrdtls.setIsPackage(orderMstrdtls.isPackage());
					orderMstrdtls.setPackageId(orderMstrdtls.getPackageId());
					orderMstrdtls.setPackageTypeId(orderMstrdtls.getPackageTypeId());
					//orderMstrdtls.setIsPackageService(orderMstrdtls.getIsPackageService());
					orderMstrdtls.setApportionedPrice(new BigDecimal(0));
					orderMstrdtls.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
					orderMstrdtls.setDepartmentId(consumptionDto.getSpecialityId());
					orderMstrdtls.setSubDepartmentId(consumptionDto.getSubSpecialityId());
					orderMstrdtls.setIsRefDoctorShare(consumptionDto.getIsRefReq());
					orderMstrdtls.setIsDocReq(consumptionDto.getIsDocReq());
					orderMstrdtls.setEncounterId(billingMasterDto.getEncounterId());
					orderMstrdtls.setBedCategoryId(billingMasterDto.getBillingBedCategoryId());
					orderMstrdtls.setDebitNoteService(false);
					
					Response labRes=iLabConfigurationService.getLabConfigurationByOrgUnit(billingMasterDto.getOrganisationId(),billingMasterDto.getUnitId());
				      LabConfigurationDto labConfigDto=(LabConfigurationDto) labRes.getObject();
						
				      orderMstrdtls.setIsLabService(labConfigDto.getLabSpecialityId()==consumptionDto.getSpecialityId()? 'Y':'N');  
					if(consumptionDto.getPayeeId()==unitApplicationConfigurationDto.getSelfPayeeId() && consumptionDto.getContractId()!=null)
					{
						List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
						OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
							detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
							detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
						list.add(detailsPayeeMapperDto);	
						orderMstrdtls.setOrderDetailsPayeeMapperDtosList(list);
					}else
						orderMstrdtls.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
					
					if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0) {
						orderMstrdtls.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
					}
					
				
				
				}
			}
			
			 listToBeAdded = billingMasterDto.getOrderDetailsMasterDtosList().stream().filter(p->p.getOrderDetailsId()==null).collect(Collectors.toList());
			 listToBeUpdated = billingMasterDto.getOrderDetailsMasterDtosList().stream().filter(p->p.getOrderDetailsId()!=null).collect(Collectors.toList());
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setVisitTypeId(OP);
				orderMasterDto.setEncounterId(billingMasterDto.getEncounterId());
				orderMasterDto.setAdmissionId(null);
				orderMasterDto.setPatientId(billingMasterDto.getPatientId());
				orderMasterDto.setCreatedBy(billingMasterDto.getCreatedBy());
				orderMasterDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
				orderMasterDto.setCreatedDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
				orderMasterDto.setOrderDate(billingMasterDto.getCreatedDate());
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrgId(billingMasterDto.getOrganisationId());
				orderMasterDto.setOrgUnitId(billingMasterDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
				orderMasterDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
				orderMasterDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				orderMasterDto.setPaymentEntitlementTypeId(billingMasterDto.getPaymentEntitlementId());
				orderMasterDto.setReferralId(billingMasterDto.getReferralId());
				orderMasterDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
				List orderDetailsList = new ArrayList();
			if(listToBeAdded.size()>0)
			{
				for(OrderDetailsMasterDto detailsMasterDto : listToBeAdded)
				{
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(billingMasterDto.getOrganisationId());
						consumptionDto.setUnitId(billingMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
						consumptionDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
						consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(detailsMasterDto.getServiceId());
//						consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
						consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
						consumptionDto.setContractId(billingMasterDto.getContractId());
						consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
						consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
						consumptionDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
						consumptionDto.setPatientId(billingMasterDto.getPatientId());
						consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
					    consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());	
					    consumptionDto.setPatientClassPercentage(detailsMasterDto.getPatientClassPercentage().doubleValue());
					    consumptionDto.setServiceRate(detailsMasterDto.getServiceAmt().doubleValue());
					    consumptionDto.setSpecialityId(detailsMasterDto.getDepartmentId());
					    consumptionDto.setSubSpecialityId(detailsMasterDto.getSubDepartmentId());
					    consumptionDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
					    if(!detailsMasterDto.getDummyServiceAmt().equals(detailsMasterDto.getServiceAmt()))
					    	consumptionDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
					    
				    if(billingMasterDto.isPackageExist())		
						res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
					else
						res =iContractConsumptionService.consumeServiceFromContract(consumptionDto);
				
				    consumptionDto = (ContractConsumptionDto) res.getObject();
					if(res.getObject()!=null)
					{
						detailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()!=null ? consumptionDto.getPatientClassPercentage():0.00));
						detailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate() : 0.00));
						detailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate():0.00));
						detailsMasterDto.setTariffId(billingMasterDto.getTariffId());
						detailsMasterDto.setOrdTotalAmt(new BigDecimal((consumptionDto.getServiceRate()!=null?consumptionDto.getServiceRate():0.00)*(detailsMasterDto.getOrderQty()!=null ? detailsMasterDto.getOrderQty():1)));
						detailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()!=null ? consumptionDto.getConcession():0.00));
						detailsMasterDto.setTaxId(consumptionDto.getTaxId());
						detailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()!=null ? consumptionDto.getTaxAmt():0.00));
						detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
						detailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()!=null ? consumptionDto.getTaxPercentage():0.00));
						detailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()!=null?consumptionDto.getSelfPayable():0.00));
						detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
						detailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()!=null?consumptionDto.getCreaditPayable():0.00));
						detailsMasterDto.setContractId(consumptionDto.getContractId());
						detailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()!=null ? consumptionDto.getPackageConsumptionAmt():BigDecimal.ZERO);
						detailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()!=null?consumptionDto.getCoShare():0.00));
						detailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						detailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
						detailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
//						detailsMasterDto.setOrderDate(new Date().getTime());
						detailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
						if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							detailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						else 
							detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
					}
					orderDetailsList.add(detailsMasterDto);
				}
			}
			
			if(listToBeUpdated.size()>0)
			{
				for(OrderDetailsMasterDto detailsMasterDto : listToBeUpdated)
				{
                   // if(detailsMasterDto.getServiceId().intValue() == billingMasterDto.getPharmacyServiceId())
					//{
                   // 	List<OrderDetailsMasterDto> pharamcyList = new ArrayList<>();
                   // 		detailsMasterDto.setConcessionPer(detailsMasterDto.getPatientClassPercentage().doubleValue());
                  //  	pharamcyList.add(detailsMasterDto);
					//	iWalkInSalesService.updatePayeePharamcy(pharamcyList, billingMasterDto);
                    	//iOrderDetailsMasterService.updatePharmacyServiceWithDiscsount(orderMasterDto,detailsMasterDto);
				//	}else{
						ContractConsumptionDto consumptionDto;
						if(detailsMasterDto.isPackage())
						{
							ServiceSearchResDto searchResDto = new ServiceSearchResDto();
								searchResDto.setAdmissionId(billingMasterDto.getAdmissionId());
								searchResDto.setEncounterId(billingMasterDto.getEncounterId());
								searchResDto.setContractId(billingMasterDto.getContractId());
								searchResDto.setServiceMasterId(detailsMasterDto.getServiceId());
								searchResDto.setOrderDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
								searchResDto.setOrganizationId(billingMasterDto.getOrganisationId());
								searchResDto.setUnitId(billingMasterDto.getUnitId());
								searchResDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
								searchResDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
								searchResDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
								searchResDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
								searchResDto.setPackageMasterId(detailsMasterDto.getPackageId());
								searchResDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
								searchResDto.setOrdDiscAmt(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getOrdDiscount().doubleValue() : 0.00);
								searchResDto.setBasePrice(detailsMasterDto.getServiceAmt());
								searchResDto.setApplyAuth(billingMasterDto.isAddPreAuth());
								searchResDto.setPatientClassPer(detailsMasterDto.getPatientClassPercentage()!=null && detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getPatientClassPercentage().doubleValue() : 0.00);
							res =iContractConsumptionService.consumePackageFromContract(searchResDto);	
							consumptionDto = (ContractConsumptionDto) res.getObject();
						}else{
							consumptionDto=new ContractConsumptionDto();
								consumptionDto.setOrganizationId(billingMasterDto.getOrganisationId());
								consumptionDto.setUnitId(billingMasterDto.getUnitId());
								consumptionDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
								consumptionDto.setPatientTypeId(billingMasterDto.getPatientTypeId());
								consumptionDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
								consumptionDto.setPaymentEntitlementId(SELF);
								consumptionDto.setServiceId(detailsMasterDto.getServiceId());
								consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(detailsMasterDto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
								consumptionDto.setContractId(billingMasterDto.getContractId());
								consumptionDto.setEncounterId(billingMasterDto.getEncounterId());
								consumptionDto.setAdmissionId(billingMasterDto.getAdmissionId());
								consumptionDto.setDefaultSelfTariffId(billingMasterDto.getTariffId());
								consumptionDto.setPatientId(billingMasterDto.getPatientId());
								consumptionDto.setSelfPayeeId(billingMasterDto.getSelfPayeeId());
							    consumptionDto.setPatientClassId(billingMasterDto.getPatientClassId());	
							    consumptionDto.setServiceRate(detailsMasterDto.getServiceAmt().doubleValue());
							    consumptionDto.setChangedPrice(detailsMasterDto.getServiceAmt().doubleValue());
							    consumptionDto.setSpecialityId(detailsMasterDto.getDepartmentId());
							    consumptionDto.setSubSpecialityId(detailsMasterDto.getSubDepartmentId());
							    consumptionDto.setOrdDiscountPer(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getOrdDiscountPer().doubleValue():0.00);
							    consumptionDto.setPatientClassPercentage(detailsMasterDto.getServiceAmt().doubleValue()>0 ? detailsMasterDto.getPatientClassPercentage().doubleValue():0.00);
							    consumptionDto.setQuantity(detailsMasterDto.getOrderQty().doubleValue());
							
							if(billingMasterDto.isPackageExist() && detailsMasterDto.getForceAddRemoved()!=(-1))		
								res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
							else
								res =iContractConsumptionService.consumeServiceFromContract(consumptionDto);
							
							consumptionDto=(ContractConsumptionDto)res.getObject();
						}
						if(res!=null || res.getObject()!=null)
						{
							detailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()!=null ? consumptionDto.getPatientClassPercentage():0.00));
							detailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate() : 0.00));
							detailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()!=null ? consumptionDto.getServiceRate():0.00));
							detailsMasterDto.setTariffId(billingMasterDto.getTariffId());
							detailsMasterDto.setOrdTotalAmt(new BigDecimal((consumptionDto.getServiceRate()!=null?consumptionDto.getServiceRate():0.00)*(detailsMasterDto.getOrderQty()!=null ? detailsMasterDto.getOrderQty():1)));
							detailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()!=null ? consumptionDto.getConcession():0.00));
							detailsMasterDto.setTaxId(consumptionDto.getTaxId());
							detailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()!=null ? consumptionDto.getTaxAmt():0.00));
							detailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()!=null ? consumptionDto.getFinalPrice():0.00));
							detailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()!=null ? consumptionDto.getTaxPercentage():0.00));
							detailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()!=null?consumptionDto.getSelfPayable():0.00));
							detailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							detailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()!=null?consumptionDto.getCreaditPayable():0.00));
							detailsMasterDto.setContractId(consumptionDto.getContractId());
							detailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()!=null ? consumptionDto.getPackageConsumptionAmt():BigDecimal.ZERO);
							detailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()!=null?consumptionDto.getCoShare():0.00));
							detailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								detailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
							else
								detailsMasterDto.setOrderDetailsPackageMapperDtosList(null);
						}
						orderDetailsList.add(detailsMasterDto);
				//	}
                    orderDetailsIds.add(detailsMasterDto.getOrderDetailsId());
				}
			}
			
			if(orderDetailsList.size()>0 && listToBeAdded.size()>0)
			{
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				Response savedRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto,null);
				orderDetailsIds = savedRes.getListObject();
			}
			if(orderDetailsList.size()>0 && listToBeUpdated.size()>0)
			{
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				iOrderDetailsMasterService.updateOrderDetails(listToBeUpdated, null,(billingMasterDto.getLineLevelDiscountOrderList()!=null)?billingMasterDto.getLineLevelDiscountOrderList().size():null, orderMasterDto);
			}
			
			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();
				serviceMasterDto.setEncounterId(billingMasterDto.getVisitTypeId()==OP ? billingMasterDto.getEncounterId():billingMasterDto.getAdmissionId());
				serviceMasterDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				serviceMasterDto.setOrderDetailsIds(orderDetailsIds);
			Response getList = iBillingMasterService.getOrderedServicesByEncounterId(serviceMasterDto);
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, getList.getListObject(), null);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/saveBillFromMobileApp/saveBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveBillFromMobileApp(@RequestBody BillingMasterDto billingMasterDto) throws Exception {
		try {
			
			
			BillRecieptDto obj = null;
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(billingMasterDto.getOrganisationId());
			unitApplicationConfigurationDto.setUnitId(billingMasterDto.getUnitId());
			
			Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
			
			//get order Details by orderId
			Response orderDetailsRes = iOrderMasterService.getMedicationsByOrderId(billingMasterDto.getOrderId());
			if(orderDetailsRes!=null && orderDetailsRes.getListObject()!=null && orderDetailsRes.getListObject().size()>0) {
				List<OrderDetailsMaster> orderDetailsList = orderDetailsRes.getListObject();
				List<OrderDetailsMasterDto> orderDetailsMasterDtoList = new ArrayList<>();
				for (OrderDetailsMaster orderDetailsMaster :  orderDetailsList) {
					OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
					orderDtlsDto.setServiceId(orderDetailsMaster.getServiceId());
					orderDtlsDto.setOrderDetailsId(orderDetailsMaster.getOrderDetailsId());
					orderDtlsDto.setOrderId(billingMasterDto.getOrderId());
					orderDetailsMasterDtoList.add(orderDtlsDto);
				}
					billingMasterDto.setOrderDetailsMasterDtosList(orderDetailsMasterDtoList);
				
			}else {
				//order details not present against this order
				return new Response<>(ERROR, ERROR_CODE, "Active Orders not present against Current Order", null, null);
			}
				
			
				
			
			billingMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
			billingMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
			billingMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setOrderDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			billingMasterDto.setIsFinalBill('Y');
			billingMasterDto.setBillDateTime(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime());
			billingMasterDto.setRoundOff(0.00);
			billingMasterDto.setIsCreditBill('N');
			billingMasterDto.setBillCancelled('N');
			billingMasterDto.setIsFinancialClearance('N');
			billingMasterDto.setCreditPayable(0.00);
			billingMasterDto.setBillStatusId(1);
			billingMasterDto.setCancelledDiscountsList(null);
			billingMasterDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			billingMasterDto.setPaymentEntitlementId(unitApplicationConfigurationDto.getSelfPayeeId());// need to ask
			billingMasterDto.setPatientTypeId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			billingMasterDto.setIsInterimBill('N');
			billingMasterDto.setIsDrNote('N');
			billingMasterDto.setContractId(null);
			billingMasterDto.setHomeCollection(false);
			billingMasterDto.setApplyAuth(false);
			billingMasterDto.setNeedToUpdatePharmacyService(false);
			billingMasterDto.setUpdatePayee(false);
			billingMasterDto.setIsPreBilling(unitApplicationConfigurationDto.getIsPreOrPostBilling());
			billingMasterDto.setCapExist(false);
			billingMasterDto.setPackageExist(false);
			billingMasterDto.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			billingMasterDto.setSurchargeServiceId(unitApplicationConfigurationDto.getSurchargeServiceId());
			billingMasterDto.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
			billingMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
			billingMasterDto.setVisitTypeId(OP);
			billingMasterDto.setIsFinalBill('Y');
			double selfPayable = 0.00;
			//create orderDetailsMaster list
			for (OrderDetailsMasterDto orderMstrdtls : billingMasterDto.getOrderDetailsMasterDtosList()) {
				orderMstrdtls.setDiscountOwnership(0);
				ContractConsumptionDto contrcDto = new ContractConsumptionDto();
				
				contrcDto.setOrganizationId(billingMasterDto.getOrganisationId());
				contrcDto.setUnitId(billingMasterDto.getUnitId());
				contrcDto.setAdmissionId(null);
				contrcDto.setEncounterId(billingMasterDto.getEncounterId());
				contrcDto.setBillingBedCategoryId(billingMasterDto.getBillingBedCategoryId());
				contrcDto.setPatientCategoryId(billingMasterDto.getPatientTypeId());
				contrcDto.setVisitTypeId(billingMasterDto.getVisitTypeId());
				contrcDto.setServiceId(orderMstrdtls.getServiceId());
				contrcDto.setTariffId(billingMasterDto.getTariffId());
				contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
				Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
				
				if(serviceRateRes.getObject()!=null) {
					ContractConsumptionDto 	consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
					orderMstrdtls.setRate(consumptionDto.getFinalPrice());
					orderMstrdtls.setDiscountOwnership(0);
					orderMstrdtls.setMarkupStatus(consumptionDto.getMarkupStatus());
					orderMstrdtls.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
					orderMstrdtls.setOrgId(billingMasterDto.getOrganisationId());
					orderMstrdtls.setOrgUnitId(billingMasterDto.getUnitId());
					orderMstrdtls.setOrderQty(billingMasterDto.getBillingBedCategoryId());
					orderMstrdtls.setPriorityId(consumptionDto.getPriorityId());
					orderMstrdtls.setIsOutsourced('N');
					orderMstrdtls.setOrderDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setOrderSchDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setPayeeId(consumptionDto.getPayeeId());
					orderMstrdtls.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
					orderMstrdtls.setBillingClassId(1);
					orderMstrdtls.setServiceRendered((consumptionDto.getIsAutoRender()!=null && consumptionDto.getIsAutoRender()=='Y')? 1 :0);
					orderMstrdtls.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId());
					orderMstrdtls.setOrdRenderDatetime(billingMasterDto.getCreatedDate());
					orderMstrdtls.setServiceIsBilled(0);
					orderMstrdtls.setStatus('A');
					orderMstrdtls.setOldNetPay(new BigDecimal(0));
					orderMstrdtls.setOrdCancelled('N');
					orderMstrdtls.setOrgId(billingMasterDto.getOrganisationId());
					orderMstrdtls.setOrgUnitId(billingMasterDto.getUnitId());
					orderMstrdtls.setCreatedBy(billingMasterDto.getCreatedBy());
					orderMstrdtls.setUpdatedBy(billingMasterDto.getUpdatedBy());
					orderMstrdtls.setCreatedDate(billingMasterDto.getCreatedDate());
					orderMstrdtls.setUpdatedDate(billingMasterDto.getUpdatedDate());
					orderMstrdtls.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
					orderMstrdtls.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
					orderMstrdtls.setIsProcedureService('N');
					orderMstrdtls.setIsPostedWhileDischarge('N');
					orderMstrdtls.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
					orderMstrdtls.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
					orderMstrdtls.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderMstrdtls.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
					orderMstrdtls.setOrdDiscount(new BigDecimal(consumptionDto.getOrdDiscount()));
					orderMstrdtls.setOrdDiscountPer(new BigDecimal(consumptionDto.getOrdDiscountPer()));
					orderMstrdtls.setIsFromAddservice('N');
					orderMstrdtls.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
					orderMstrdtls.setCreditPayable(new BigDecimal(0));
					orderMstrdtls.setOldCreditPayable(new BigDecimal(0));
					orderMstrdtls.setOldSelfPayable(new BigDecimal(0));
					orderMstrdtls.setTaxId(consumptionDto.getTaxId());
					orderMstrdtls.setIsDiscountApplicable('N');
					orderMstrdtls.setOrderEmergencyFlag('N');
					orderMstrdtls.setIsDrug('N');
					orderMstrdtls.setIsPackage(orderMstrdtls.isPackage());
					orderMstrdtls.setPackageId(orderMstrdtls.getPackageId());
					orderMstrdtls.setPackageTypeId(orderMstrdtls.getPackageTypeId());
					//orderMstrdtls.setIsPackageService(orderMstrdtls.getIsPackageService());
					orderMstrdtls.setApportionedPrice(new BigDecimal(0));
					orderMstrdtls.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
					orderMstrdtls.setDepartmentId(consumptionDto.getSpecialityId());
					orderMstrdtls.setSubDepartmentId(consumptionDto.getSubSpecialityId());
					orderMstrdtls.setIsRefDoctorShare(consumptionDto.getIsRefReq());
					orderMstrdtls.setIsDocReq(consumptionDto.getIsDocReq());
					orderMstrdtls.setEncounterId(billingMasterDto.getEncounterId());
					orderMstrdtls.setBedCategoryId(billingMasterDto.getBillingBedCategoryId());
					orderMstrdtls.setDebitNoteService(false);
					Response labRes=iLabConfigurationService.getLabConfigurationByOrgUnit(billingMasterDto.getOrganisationId(),billingMasterDto.getUnitId());
				      LabConfigurationDto labConfigDto=(LabConfigurationDto) labRes.getObject();
						
				      orderMstrdtls.setIsLabService(labConfigDto.getLabSpecialityId()==consumptionDto.getSpecialityId()? 'Y':'N');  
					if(consumptionDto.getPayeeId()==unitApplicationConfigurationDto.getSelfPayeeId() && consumptionDto.getContractId()!=null)
					{
						List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
						OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
							detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
							detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
						list.add(detailsPayeeMapperDto);	
						orderMstrdtls.setOrderDetailsPayeeMapperDtosList(list);
					}else
						orderMstrdtls.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
					
					if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0) {
						orderMstrdtls.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
					}
					
					selfPayable = selfPayable + orderMstrdtls.getNetAmount().doubleValue();
				
				}
			}
			
			
			if(billingMasterDto.getTotalAmountFromApp()!=null && billingMasterDto.getTotalAmountFromApp()!=0) {
				 //iterate only for one time for self 
				List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtoList = new ArrayList<>();
				PaymentReceiptDetailsDto paymentReceiptDetailsDto = new PaymentReceiptDetailsDto();
					paymentReceiptDetailsDto.setPaymentModeId(10); //razor pay
					paymentReceiptDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
					paymentReceiptDetailsDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
					paymentReceiptDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					paymentReceiptDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					paymentReceiptDetailsDto.setOrgnizationId(billingMasterDto.getOrganisationId());
					paymentReceiptDetailsDto.setUnitId(billingMasterDto.getUnitId());
					paymentReceiptDetailsDto.setStatus('A');
					paymentReceiptDetailsDto.setAmount(billingMasterDto.getTotalAmountFromApp());
					paymentReceiptDetailsDto.setTransactionNo(billingMasterDto.getTransactionNo());
					paymentReceiptDetailsDtoList.add(paymentReceiptDetailsDto);
					billingMasterDto.setPaymentReceiptDetailsDtosList(paymentReceiptDetailsDtoList);
			}
			billingMasterDto.setConcessionAmount(0.00);
			billingMasterDto.setTotalBillAmount(selfPayable);
			billingMasterDto.setDiscountAmount(0.0);
			billingMasterDto.setNetAmount(selfPayable);
			billingMasterDto.setSelfPayable(selfPayable);
			List<BillPayeeDetailsDto> billPayeeDetailsDtosList = new ArrayList<>();
			
			BillPayeeDetailsDto billPayeeDetailsDto = new BillPayeeDetailsDto();
			billPayeeDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			billPayeeDetailsDto.setBillAmount(selfPayable); 
			billPayeeDetailsDto.setOrgId(billingMasterDto.getOrganisationId());
			billPayeeDetailsDto.setUnitId(billingMasterDto.getUnitId());
			billPayeeDetailsDtosList.add(billPayeeDetailsDto);
			billPayeeDetailsDto.setCreatedDate(billingMasterDto.getCreatedDate());
			billPayeeDetailsDto.setUpdatedDate(billingMasterDto.getUpdatedDate());
			billPayeeDetailsDto.setCreatedBy(billingMasterDto.getCreatedBy());
			billPayeeDetailsDto.setUpdatedBy(billingMasterDto.getUpdatedBy());
			billPayeeDetailsDto.setStatus('A');
			billingMasterDto.setBillPayeeDetailsDtosList(billPayeeDetailsDtosList);
			Response billRes = iBillingMasterService.saveBillService(billingMasterDto);
			if (billRes.getStatus().equalsIgnoreCase(SUCCESS) && billRes.getResObject()!=null) 
			{
				BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) billRes.getResObject();
					reqDto.setIsPaidAppointment('N');
					reqDto.setBillNumber(billingMasterDto.getBillNumber()!=null? billingMasterDto.getBillNumber(): null);
				Response recieptRes = iBillingMasterService.collectPayment(reqDto);
				Integer recieptId = null;
				Integer depositConsumptionId = null;
				if(recieptRes.getObject()!=null) {
					obj = (BillRecieptDto)recieptRes.getObject();
					 recieptId = obj.getRecieptId();
					 depositConsumptionId = obj.getDepositConsumptionId();
					// iTDoctorShareService.getProcessDocShare(billingMasterDto.getUnitId());
				}
					if (billRes.getStatus().equals(SUCCESS) && reqDto.getIsInterimOrFinalBill() != null
							&& reqDto.getIsInterimOrFinalBill() == 2 && reqDto.getVisitTypeId() == OP) {
						SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
						BillListResDto dto1  = iBillingMasterService.getBillSumsEncounterId(billingMasterDto.getEncounterId());
						emailinfo.setRecieptId(recieptId);
						emailinfo.setRecieptNo(obj != null ? obj.getReceiptNo(): "");
						emailinfo.setBillId((Integer)billRes.getObject());
						emailinfo.setPatientId(reqDto.getPatientId());
						emailinfo.setDepositeConsumptionId(depositConsumptionId);
						emailinfo.setBillNo(billingMasterDto.getBillNumber());
						emailinfo.setUnitId(billingMasterDto.getUnitId());
						emailinfo.setOrganizationId(billingMasterDto.getOrganisationId());
						
						 if(billingMasterDto.getPaymentEntitlementId() != null && billingMasterDto.getPaymentEntitlementId() == INSURANCE) {
                         	
                      	   emailinfo.setCompanyId(null);
                           emailinfo.setInsuranceId(billingMasterDto.getPayeeId()!=null ? billingMasterDto.getPayeeId(): null);
                             
						 }else  if( billingMasterDto.getPaymentEntitlementId() != null && billingMasterDto.getPaymentEntitlementId() == COMPANY) {
                      	
                      	   emailinfo.setCompanyId(billingMasterDto.getPayeeId()!=null ? billingMasterDto.getPayeeId(): null);
                           emailinfo.setInsuranceId(null);
                             
						 }else {
                      	   emailinfo.setCompanyId(null);
                           emailinfo.setInsuranceId(null);
						 }
						 if(dto1!=null) {
							 emailinfo.setBillAmount(new BigDecimal(dto1.getNetAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							 emailinfo.setSelfAmount(
										new BigDecimal(dto1.getTotalSelfPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							 emailinfo.setCreditAmount(
										new BigDecimal(dto1.getTotalCreditPayable()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							 emailinfo.setOutstandingAmount(
										new BigDecimal(dto1.getOutstanding()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						 }else {
							 emailinfo.setBillAmount(new BigDecimal(0.00));
							 emailinfo.setSelfAmount(new BigDecimal(0.00));
							 emailinfo.setCreditAmount(new BigDecimal(0.00));
							 emailinfo.setOutstandingAmount(new BigDecimal(0.00));
						 }
						 
						 emailinfo.setDateTime(reqDto.getDateOfReciept());
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.OPBILL, emailinfo);
					}
					
				
				if(recieptRes.getStatus().equals(SUCCESS)) 
				{
					return new Response<>(SUCCESS, SUCCESS_CODE, billRes.getMessage(), null, billRes.getObject(), (BillRecieptDto)recieptRes.getObject());
				}	
					
			}
			return billRes;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/Pharmacy/payee/bill/{saleId}", method = RequestMethod.GET)
	public Response getBillPharmacyPayeeByBillIdCreditNote(@PathVariable("saleId") int saleId) {
		try {
			return iBillPayeeDetailsService.getBillPharmacyPayeeByBillIdCreditNote(saleId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		//	throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancelPharmacyReceipt", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelPharmacyReceipt(@RequestBody BillPaymentCollectionReqDto dto) {
		try {
			return iBillPayeeDetailsService.cancelPharmacyReceipt(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllInclusivePackageConsumeAmount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllInclusivePackageConsumeAmount(@RequestBody BillingMasterDto dto) {
		try {
			return iBillingMasterService.getAllInclusivePackageConsumeAmount(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getFreeTextOrderServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFreeTextOrderServices(@RequestBody BillingMasterDto dto) {
		try {
			return iBillingMasterService.getFreeTextOrderServices(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getTallyInterfaceDataInJsonFormat", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getTallyInterfaceDataInJsonFormat(@RequestBody CabinMasterDto dto) {
		try {
			return iBillingMasterService.getTallyInterfaceDataInJsonFormat(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateTallyInterfaceDataById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String updateTallyInterfaceDataById(@RequestBody CabinMasterDto dto) {
		try {
			return iBillingMasterService.updateTallyInterfaceDataById(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDataForCheckBillStatusByBillNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataForCheckBillStatusByBillNumber(@RequestBody BillingMasterDto dto) {
		try {
			return iBillingMasterService.getDataForCheckBillStatusByBillNumber(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getFreeTextOrderServices/updateDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRenderedDate(@RequestBody BillingMasterDto dto) {
		try {
			return iBillingMasterService.updateRenderedDate(dto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/tallyInterfaceDataByModetest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String cabinMasterDto) {
		
		
		Response<CabinMasterDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(cabinMasterDto, CabinMasterDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		}
	}
 
	
	@RequestMapping(value = "/tallyInterfaceDataByMode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String tallyInterfaceDataByMode(@RequestBody CabinMasterDto dto) {
		try {
			
			if(dto.getMode() == 1) {
				return iBillingMasterService.getTallyInterfaceDataInJsonFormatByMode(dto);
			}else {
				return iBillingMasterService.updateTallyInterfaceDataByIdByMode(dto);
			}
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/BillList/{billId}/{unitId}/{orgId}/{visitTypeId}/{randomNumber}")
	public void getBillingRptURL(@PathVariable("billId")Integer billId,
			                     @PathVariable("unitId")Integer unitId,
			                     @PathVariable("orgId")int orgId,
			                     @PathVariable("visitTypeId")int visitTypeId,
			                     @PathVariable("randomNumber")String randomNumber,
			                     HttpServletRequest request,
			                     HttpServletResponse response){
		try{
			
			String rptString =null;
			if(visitTypeId == 1) {
				 rptString = "{\"appName\": \"HIS\"," + 
						" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
						"\"reportDesignFilePath\": \"BillListOP.rpt\"," + 
						"\"reportFormat\": \"PDF\"," + 
						"\"exportFileName\": \"BillListOP\"," + 
						"\"reportParams\": [" + 
						"{" + 
						" \"paramName\": \"@bill_id\"," + 
						" \"paramType\": \"Integer\"," + 
						" \"paramValue\":"+billId.toString()+ 
						" }" + 
						"]}";
			}else {
				 rptString = "{\"appName\": \"HIS\"," + 
						" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
						"\"reportDesignFilePath\": \"BillListIP.rpt\"," + 
						"\"reportFormat\": \"PDF\"," + 
						"\"exportFileName\": \"BillListIP\"," + 
						"\"reportParams\": [" + 
						"{" + 
						" \"paramName\": \"@bill_id\"," + 
						" \"paramType\": \"Integer\"," + 
						" \"paramValue\":"+billId.toString()+ 
						" }" + 
						"]}";
			}


			String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);
			ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
			HttpPost pst = new HttpPost(API_URL);
			pst.addHeader("Content-Type", "application/json");
			pst.setEntity(new StringEntity(rptString));
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse result = httpClient.execute(pst);
			HttpEntity resEntity = result.getEntity();
			resEntity.writeTo(byteOutPutStream);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=randomNumber.pdf");
			ServletOutputStream output = response.getOutputStream();
			output.write(byteOutPutStream.toByteArray());
			output.close();
		}catch(Exception e){
			
		}
	}
	
	@RequestMapping(value = "/order/docShare/forCreditNote", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDocShareForLineItemForCreditNote(@RequestBody OrderMasterDto orderMasterDto) {
		try {
			return iOrderMasterService.getDocShareForLineItemForCreditNote(orderMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	@RequestMapping(value = "/order/docShare/clearDoctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response clearDoctor(@RequestBody OrderMasterDto orderMasterDto) {
		try {
			/*JsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return null;*/
			return iOrderMasterService.clearDoctor(orderMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDummyHiddenServiceFlag/{flag}/{orderDetailsId}/{value}/{isPackage}/{packageId}/{admissionId}", method = RequestMethod.GET)
	public Response updateDummyHiddenServiceFlag(@PathVariable("flag")String flag,
			@PathVariable("orderDetailsId")int orderDetailsId,
			@PathVariable("packageId")int packageId,
			@PathVariable("admissionId")int admissionId,
			@PathVariable("value")boolean value,
			@PathVariable("isPackage")boolean isPackage) {
		try {
			return iOrderDetailsMasterService.updateDummyHiddenServiceFlag(flag,orderDetailsId,value,isPackage,packageId,admissionId);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDataForFinanacialRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataForFinanacialRequest(@RequestBody RequestDiscountDto requestDiscountDto) {
		try {
			return iBillingMasterService.getDataForFinanacialRequest(requestDiscountDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDataOfPatientToCheckDueAmountOfPharmacyService", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDataOfPatientToCheckDueAmountOfPharmacyService(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iBillingMasterService.getDataOfPatientToCheckDueAmountOfPharmacyService(billingMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillIdByEncounterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillIdByEncounterId(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iBillingMasterService.getBillIdByEncounterId(billingMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/checkCapWithPackageBalanceAmount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkCapWithPackageBalanceAmount(@RequestBody BillingMasterDto billingMasterDto) {
		try {
			return iBillingMasterService.checkCapWithPackageBalanceAmount(billingMasterDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value = "/getBillNoByPatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillNoByPatientForMyLife(@RequestBody BillListReqDto billListReqDto) {
		try {
			return iBillingMasterService.getBillNoByPatientForMyLife(billListReqDto);
		} catch (Exception e) {

			throw e;
		}
	}

	@RequestMapping(value = "/getBillReceiptBytesMyLife", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillReceiptBytesMyLife(@RequestBody BillListReqDto billListReqDto, HttpServletResponse servletResponse) {
		try{
			String reportParam = "{\"appName\": \"HIS\"," +
					" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," +
					"\"reportDesignFilePath\": \"opdbillreceipt.rpt\"," +
					"\"reportFormat\": \"PDF\"," +
					"\"exportFileName\": \"PatientBill\"," +
					"\"reportParams\": [" +
					"{" +
					" \"paramName\": \"@billid\"," +
					" \"paramType\": \"Integer\"," +
					" \"paramValue\":"+billListReqDto.getBillingMasterId()+
					" }, " +
					"{" +
					" \"paramName\": \"@isDuplicate\"," +
					" \"paramType\": \"Integer\"," +
					" \"paramValue\":"+0+
					" }" +
					"]}";

			String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(billListReqDto.getHospitalId(), billListReqDto.getUnitId());
			ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();

			HttpPost pst = new HttpPost(API_URL);
			pst.addHeader("Content-Type", "application/json");
			pst.setEntity(new StringEntity(reportParam));
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse result = httpClient.execute(pst);

			HttpEntity resEntity = result.getEntity();
			resEntity.writeTo(byteOutPutStream);
//			servletResponse.setContentType("application/pdf");
//			servletResponse.setHeader("Content-Disposition", "inline; filename=opdbillreceipt.pdf");
//			ServletOutputStream output = servletResponse.getOutputStream();
//			output.write(byteOutPutStream.toByteArray());
//			output.close();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, null,byteOutPutStream.toByteArray());

		}catch(Exception e){
			return new Response(ERROR, ERROR_CODE, ERROR, null,null);
		}
	}
	
	
	@RequestMapping(value = "/updateDoctorIdAndSpecialityId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDoctorIdAndSpecialityId(@RequestBody AdmissionDto dto) {
		try {
			return iBillingMasterService.updateDoctorIdAndSpecialityId(dto);
		} catch (Exception e) {

			throw e;
		}
	}


	@RequestMapping(value = "/kiosk/saveAndPayAppointment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveAndPayAppointment(
			@RequestBody EncounterMasterDto encounterMasterDto) {
		try {

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response resUnit = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(resUnit.getObject()!= null){
				 unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) resUnit.getObject();
			}else{
				return new Response<>(ERROR, ERROR_CODE," Application Configuration is not.", null, null);
			}

			long currentMillis = System.currentTimeMillis();
			Date date = new Date(currentMillis);

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = formatter.format(date);

			encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getBillingBedCategoryId());
			encounterMasterDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			encounterMasterDto.setIsPreOrPostBilling(unitApplicationConfigurationDto.getIsPreOrPostBilling());
			encounterMasterDto.setPaymentEntitlementId(1);
			encounterMasterDto.setPaymentEntitlementTypeId(1);
			encounterMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.setStatus('A');
			encounterMasterDto.setIsAppointmentRequired('Y');
			encounterMasterDto.setIsReferal('N');
			encounterMasterDto.setIsScheduledAppointment('A');
			encounterMasterDto.setEncounterTypeId(1);
			encounterMasterDto.setSystemGeneratedVisitTypeId(1);
			encounterMasterDto.setUserDefinedVisitTypeId(1);
			encounterMasterDto.setDeptWiseAppointment('N');
			encounterMasterDto.setIsTransferToActiveDeposit('N');
			encounterMasterDto.setIsNursingAssessmentRequiredForSpeciality('N');
			encounterMasterDto.setIsReshedule('N');
			encounterMasterDto.setIsFromAppointmentScheduling('Y');
			encounterMasterDto.setRegistrationTypeId(5);
			encounterMasterDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			encounterMasterDto.setCreatedDate(currentMillis);
			encounterMasterDto.setUpdatedDate(currentMillis);
			encounterMasterDto.setEncounterDate(currentMillis);
			encounterMasterDto.setPatientId(encounterMasterDto.getPatientId() != null ? encounterMasterDto.getPatientId() : 0);
            encounterMasterDto.getAppointmentObject().setPatientId(encounterMasterDto.getPatientId() != null ? encounterMasterDto.getPatientId() : 0);

			PatientSponsorDetailsDto patSpon = new PatientSponsorDetailsDto();
			patSpon.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			patSpon.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			patSpon.setPaymentEntitlementId(1);
			patSpon.setStatus('A');
			patSpon.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			patSpon.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			patSpon.setPriorityId(1);
			patSpon.setVisitTypeId(1);
			patSpon.setUnitId(encounterMasterDto.getUnitId());
			patSpon.setOrganizationId(encounterMasterDto.getOrganizationId());
			patSpon.setCreatedDate(formattedDate);
			patSpon.setUpdatedDate(formattedDate);

			if (encounterMasterDto.getPatientSponsorDetailsDtosList() == null) {
				encounterMasterDto.setPatientSponsorDetailsDtosList(new ArrayList<>());
			}
			encounterMasterDto.getPatientSponsorDetailsDtosList().add(patSpon);

			encounterMasterDto.getPaymentReceiptDetailsDtosList().get(0).setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.getPaymentReceiptDetailsDtosList().get(0).setPaymentModeId(8);
			encounterMasterDto.getPaymentReceiptDetailsDtosList().get(0).setStatus('A');
			encounterMasterDto.getPaymentReceiptDetailsDtosList().get(0).setTransTransactionDate(formattedDate);
			encounterMasterDto.getPaymentReceiptDetailsDtosList().get(0).setTransactionTypeId(6);


			encounterMasterDto.getAppointmentObject().setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.getAppointmentObject().setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.getAppointmentObject().setStatus('A');
			encounterMasterDto.getAppointmentObject().setUnitId(encounterMasterDto.getUnitId());
			encounterMasterDto.getAppointmentObject().setOrganizationId(encounterMasterDto.getOrganizationId());
			encounterMasterDto.getAppointmentObject().setCreatedDate(formattedDate);
			encounterMasterDto.getAppointmentObject().setUpdatedDate(formattedDate);
			encounterMasterDto.getAppointmentObject().setPriorityId(1);
			encounterMasterDto.getAppointmentObject().setSlotStatusId(4);
			encounterMasterDto.getAppointmentObject().setIsFromEncounter('N');
			encounterMasterDto.getAppointmentObject().setAppointmentTypeId(1);
			encounterMasterDto.getAppointmentObject().setIsTeleconsultationAppointment(0);
			encounterMasterDto.getAppointmentObject().setGenerateToken('N');
			if(encounterMasterDto.getPaymentReceiptDetailsDtosList()!= null) {

				for (PaymentReceiptDetailsDto dto : encounterMasterDto.getPaymentReceiptDetailsDtosList()) {

					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) {
							return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
				}
			}
			Character isReshedule = encounterMasterDto.getIsReshedule() != null ? encounterMasterDto.getIsReshedule() : 'N';

			if(isReshedule.equals('Y'))
			{
				encounterMasterDto.setAppointmentIdOld(encounterMasterDto.getAppointmentId());
			}

			Response res = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
			if (res.getStatus().equals(SUCCESS)) {
				BillPaymentCollectionReqDto reqDto = (BillPaymentCollectionReqDto) res.getResObject();
				reqDto.setIsPaidAppointment('Y');
				reqDto.setPatientCouponId(encounterMasterDto.getPatientCouponTemplateMapperId() != null ? encounterMasterDto.getPatientCouponTemplateMapperId() : null);
				iBillingMasterService.collectPayment(reqDto);
				if(encounterMasterDto.getPatientCouponTemplateMapperId() != null){
					iBillingMasterService.patientCouponTemplate(encounterMasterDto.getPatientCouponTemplateMapperId());
				}
			}
			return res;
		} catch (Exception e) {

			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value="/orderPackageAndService", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response orderPackageAndService(@RequestBody EncounterMasterDto encounterMasterDto,HttpServletRequest request) {
		try {

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response resUnit = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(resUnit.getObject()!= null){
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) resUnit.getObject();
			}else{
				return new Response<>(ERROR, ERROR_CODE," Application Configuration is not.", null, null);
			}

			long currentMillis = System.currentTimeMillis();
			Date date = new Date(currentMillis);

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = formatter.format(date);

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate1 = formatter1.format(date);

			encounterMasterDto.setPaymentEntitlementId(1);
			encounterMasterDto.setPaymentEntitlementTypeId(1);
			encounterMasterDto.setNewPatient(false);
			encounterMasterDto.setEncounterDate(currentMillis);
			encounterMasterDto.setIsReferal('A');
			encounterMasterDto.setCreatedDate(currentMillis);
			encounterMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.setUpdatedDate(currentMillis);
			encounterMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			encounterMasterDto.setStatus('A');
			encounterMasterDto.setIsScheduledAppointment('I');
			encounterMasterDto.setEncounterTypeId(7);
			encounterMasterDto.setIsAppointmentRequired('Y');
			encounterMasterDto.setCurrentDate(formattedDate);
			encounterMasterDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			encounterMasterDto.setDeptWiseAppointment('N');
			encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());

			PatientSponsorDetailsDto spon = new PatientSponsorDetailsDto();
			spon.setUpdatedDate(formattedDate);
			spon.setCreatedDate(formattedDate);
			spon.setPriorityId(1);
			spon.setStatus('A');
			spon.setUnitId(encounterMasterDto.getUnitId());
			spon.setOrganizationId(encounterMasterDto.getOrganizationId());
			spon.setVisitTypeId(1);
			spon.setPaymentEntitlementId(1);
			spon.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			spon.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			spon.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			spon.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());

			if (encounterMasterDto.getPatientSponsorDetailsDtosList() == null) {
				encounterMasterDto.setPatientSponsorDetailsDtosList(new ArrayList<>());
			}
			encounterMasterDto.getPatientSponsorDetailsDtosList().add(spon);

			SlotMasterDto app = new SlotMasterDto();

			app.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			app.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			app.setStatus('A');
			app.setUnitId(encounterMasterDto.getUnitId());
			app.setOrganizationId(encounterMasterDto.getOrganizationId());
			app.setCreatedDate(formattedDate);
			app.setUpdatedDate(formattedDate);
			app.setPriorityId(1);
			app.setAppointmentTypeId(7);
			app.setIsTeleconsultationAppointment(0);
			app.setGenerateToken('N');
			app.setPatientId(encounterMasterDto.getPatientId());
			app.setIsDirectEncounter('Y');
			app.setAppointmentDate(formattedDate1);
			encounterMasterDto.setAppointmentObject(app);

			Response res = iEncounterMasterService.generateEncounter(encounterMasterDto);
			encounterMasterDto = (EncounterMasterDto)res.getObject();
			if(res.getStatus().equals(SUCCESS))
			{
				if(encounterMasterDto != null  && res.getStatus().equals(SUCCESS))
				{
					String appointmentDate;
					String appointmentTime;

					appointmentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd/MM/yyyy") ;
					appointmentTime = encounterMasterDto.getAppoitmentTime() != null
							? encounterMasterDto.getAppoitmentTime()
							: (encounterMasterDto.getAppointmentObject() != null
							? encounterMasterDto.getAppointmentObject().getFromTimeString()
							: encounterMasterDto.getEncounterTime().toString());

					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(encounterMasterDto.getUnitId());
					smsInfo.setPatientId(encounterMasterDto.getPatientId());
					smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId()!= null ? encounterMasterDto.getDoctorId() : null);
					smsInfo.setReferringDoctorId(encounterMasterDto.getReferralId() != null ? encounterMasterDto.getReferralId() : null);
					if(appointmentTime != null) {
						smsInfo.setDateTime(appointmentDate+" at "+appointmentTime);
					}else {
						smsInfo.setDateTime(appointmentDate);
					}

					smsInfo.setEncounterTypeId(encounterMasterDto.getEncounterTypeId());
					smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
					smsInfo.setEncounterId(encounterMasterDto.getEncounterId());
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER,smsInfo);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER, smsInfo);
				}

			}

			if(encounterMasterDto!= null) {
				if (encounterMasterDto.isSyncScheduling() == true) {

					String mylife = myfileSynch(encounterMasterDto);
				}
			}

			Optional<ServiceMasterDto> filteredDto = encounterMasterDto.getListServiceMasterDto().stream()
					.filter(dto -> dto.getPackageMasterId() != null)
					.findFirst();

			ServiceMasterDto filteredDtoObj = filteredDto.orElse(null);

			if (filteredDtoObj != null) {
				ServiceSearchResDto searchResDto = new ServiceSearchResDto();
				searchResDto.setPackageMasterId(filteredDtoObj.getPackageMasterId());
				searchResDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				searchResDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
				searchResDto.setBasePrice(new BigDecimal(filteredDtoObj.getBasePrice()));
			    searchResDto.setEncounterId(encounterMasterDto.getEncounterId());
				searchResDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				searchResDto.setVisitTypeId(1);
				searchResDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
				searchResDto.setApplyAuth(false);
				searchResDto.setUnitId(encounterMasterDto.getUnitId());
				searchResDto.setServiceMasterId(filteredDtoObj.getServiceMasterId());
				searchResDto.setIsRateEditable('N');
				searchResDto.setIsDiscountApplicable('N');
				searchResDto.setIsQuantityEditable('N');
				searchResDto.setIsRefDoctorShare('N');
				searchResDto.setDiscount(new BigDecimal(0));
				searchResDto.setMaxAge(100);
				searchResDto.setMinAge(0);
				searchResDto.setPackageTypeId(filteredDtoObj.getPackageTypeId());
				searchResDto.setQuantity(1);
				searchResDto.setSpecialityId(filteredDtoObj.getSpecialityId());
				searchResDto.setSubSpecialityId(filteredDtoObj.getSubSpecialityId());
				searchResDto.setTaxPercentage(0.0);
				searchResDto.setIsTaxApplicable('N');
				searchResDto.setMaxRateEditable(new BigDecimal(0));
				searchResDto.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				searchResDto.setSexId(0);
				searchResDto.setUpdateCap('N');
				Response packageRes = iContractConsumptionService.consumePackageFromContract(searchResDto);

				if(packageRes.getObject()!= null){

					ContractConsumptionDto finalDto = (ContractConsumptionDto) packageRes.getObject();

					List<Integer> intPackageIds = new LinkedList<>();
					intPackageIds.add(filteredDtoObj.getPackageMasterId());
					Response packageDetails = iMPackageMasterService.getPackageDetailsByIdForBilling(intPackageIds);

					if(packageDetails.getListObject()!= null && packageDetails.getListObject().size()>0){
						List<PackageListForOpBillResDto> listPackService = packageDetails.getListObject();
						encounterMasterDto.setRegistrationTypeId(filteredDtoObj.getPackageTypeId()); //use as package type
						List<TPackageServicesDetailsDto> listPackServiceD = listPackService.get(0).getListTPackageServicesDetailsDto();
						MPackageMasterDto mPackageDto = listPackService.get(0).getmPackageMasterDto();
						mPackageDto.setSpecialityId(filteredDtoObj.getSpecialityId());
						mPackageDto.setSubSpecialityId(filteredDtoObj.getSubSpecialityId());
						BillingMasterDto master12 = mapToBillingMaster(null, encounterMasterDto, unitApplicationConfigurationDto,listPackServiceD,mPackageDto);

						orderAndUpdatePackage(master12);

					}
				}

			}

			List<Integer> serviceIds = encounterMasterDto.getListServiceMasterDto().stream()
					.filter(dto -> dto.getPackageMasterId() == null)
					.map(ServiceMasterDto::getServiceMasterId)
					.collect(Collectors.toList());

			List<ContractConsumptionDto> listCon = new ArrayList<>();


			for (Integer se :serviceIds){
				ContractConsumptionDto conDto = new ContractConsumptionDto();

				conDto.setEncounterId(encounterMasterDto.getEncounterId());
				conDto.setContractId(0);
				conDto.setServiceId(se);
				conDto.setCurrentDate(formattedDate1);
				conDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				conDto.setUnitId(encounterMasterDto.getUnitId());
				conDto.setVisitTypeId(1);
				conDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
				conDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
				conDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				conDto.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				conDto.setQuantity(1.0);
				conDto.setPatientId(encounterMasterDto.getPatientId());
				conDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				conDto.setPaymentEntitlementId(1);
				listCon.add(conDto);

			}
			int count = getGlobalServiceRate(listCon,encounterMasterDto,unitApplicationConfigurationDto);

			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();

			serviceMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
			serviceMasterDto.setVisitTypeId(1);
			serviceMasterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			serviceMasterDto.setUnitId(encounterMasterDto.getUnitId());

			Response orderServiceList = iBillingMasterService.getOrderedServicesByEncounterIdForPreBilling(serviceMasterDto);

			if(orderServiceList.getListObject()!= null && orderServiceList.getListObject().size()>0){

				List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList = orderServiceList.getListObject();

				BillingMasterDto billingMasterDto = ganarateFinalBillObj(encounterMasterDto,orderDetailsByEncounterIdDtosList,unitApplicationConfigurationDto);
				saveBill(billingMasterDto,request);
			}

			return res;
		}catch(ConstraintViolationException e){
			return new Response(ERROR, ALREADY_EXIST_CODE, "Encounter is alread generated for selected appointment...!!", null, null);
		}catch(DataIntegrityViolationException e){
			return new Response(ERROR, ALREADY_EXIST_CODE, "Encounter is alread generated for selected appointment...!!", null, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	private BillingMasterDto ganarateFinalBillObj(EncounterMasterDto encounterMasterDto, List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList, UnitApplicationConfigurationDto unitApplicationConfigurationDto) {

		long currentMillis = System.currentTimeMillis();
		Date date = new Date(currentMillis);

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate1 = formatter1.format(date);

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = formatter.format(date);
		List<OrderDetailsMasterDto> orderDetailsMasterDtosList = new ArrayList<>();
		BillingMasterDto target = new BillingMasterDto();

		target.setPatientId(encounterMasterDto.getPatientId());
		target.setEncounterId(encounterMasterDto.getEncounterId());
		target.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
		target.setCreatedDate(System.currentTimeMillis());
		target.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
		target.setUpdatedDate(System.currentTimeMillis());
		target.setOrderDate(formattedDate1);
		target.setOrganisationId(encounterMasterDto.getOrganizationId());
		target.setOrgId(encounterMasterDto.getOrganizationId());
		target.setUnitId(encounterMasterDto.getUnitId());
		target.setIsFinalBill('Y');
		target.setOrdDoctorId(0);
		target.setVisitTypeId(1);
		target.setBillDateTime(System.currentTimeMillis());
		target.setNetAmount(encounterMasterDto.getPaidAmount());
		target.setDiscountAmount(0.0);
		target.setConcessionAmount(0.0);
		target.setGst(0.0);
		target.setTotalBillAmount(encounterMasterDto.getPaidAmount());
		target.setRoundOff(0.0);
		target.setIsCreditBill('N');
		target.setBillCancelled('N');
		target.setIsFinancialClearance('N');
		target.setBillRemark("Nakoda");
		target.setBillStatusId(1);
		target.setSelfPayable(encounterMasterDto.getPaidAmount());
		target.setCreditPayable(0.0);
		target.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
		target.setPaymentEntitlementId(1);
		target.setPatientTypeId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
		target.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
		target.setIsInterimBill('N');
		target.setIsDrNote('N');
		target.setContractId(0);
		target.setCounterId(0);
		target.setEncounterSpecialityId(0);
		target.setPatientClassId(1);
		target.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
		target.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
		target.setHomeCollection(false);
		target.setApplyAuth(false);
		target.setNeedToUpdatePharmacyService(false);
		target.setUpdatePayee(false);
		target.setIsPreBilling('Y');
		target.setTotalDepositConsumption(0.0);
		target.setIsTransferToActiveDeposit('N');
		target.setBillPayeeDetailsDtosList(new ArrayList<>()); // Initialize the list

		BillPayeeDetailsDto billPayeeDetailsDtosList = new BillPayeeDetailsDto();
		billPayeeDetailsDtosList.setBillAmount(encounterMasterDto.getPaidAmount());
		billPayeeDetailsDtosList.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
		target.getBillPayeeDetailsDtosList().add(billPayeeDetailsDtosList);
		if(orderDetailsByEncounterIdDtosList!= null){
			for (OrderDetailsByEncounterIdDto order : orderDetailsByEncounterIdDtosList) {

				OrderDetailsMasterDto orderDto = new OrderDetailsMasterDto();
				orderDto.setOrderDetailsId(order.getOrderDetailsId());
				orderDto.setIsOrderVisibleDepartment('A');
				orderDto.setIsConsultationService(order.getIsConsultationService());
				orderDto.setOrderId(order.getOrderId());
				orderDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDto.setOrderQty(1);
				orderDto.setServiceId(order.getServiceMasterId());
				orderDto.setPriorityId(1);
				orderDto.setIsOutsourced('N');
				orderDto.setOrderDate(System.currentTimeMillis());
				orderDto.setOrderSchDate(System.currentTimeMillis());
				orderDto.setPayeeId(order.getPayeeId());
				orderDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				orderDto.setBillingClassId(unitApplicationConfigurationDto.getOpdBillingCatId());
				orderDto.setServiceRendered(0);
				orderDto.setServiceRenderingDeptId(order.getSpecialityId());
				orderDto.setOrdRenderDatetime(System.currentTimeMillis());
				orderDto.setServiceIsBilled(0);
				orderDto.setBillingClassId(1);
				orderDto.setStatus('A');
				orderDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setUpdatedDate(System.currentTimeMillis());
				orderDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setCreatedDate(System.currentTimeMillis());
				orderDto.setOrdCancelled('N');
				orderDto.setServiceAmt(order.getBasePrice());
				orderDto.setCoPayPer(order.getCoPayPer());
				orderDto.setOrdTotalAmt(order.getTotalAmt());
				orderDto.setTaxPer(order.getTaxPercentage());
				orderDto.setTaxAmount(order.getTaxAmt());
				orderDto.setNetAmount(order.getNetPay());
				orderDto.setSelfPayable(order.getSelfPayable());
				orderDto.setCreditPayable(BigDecimal.valueOf(0));
				orderDto.setTaxId(order.getTaxId());
				orderDto.setIsDiscountApplicable('N');
				orderDto.setIsDiscount('N');
				orderDto.setOrderEmergencyFlag('N');
				orderDto.setIsDrug('N');
				orderDto.setIsPeriodicityService('N');
				orderDto.setIsInvestigationOrder(order.getIsInvestigationOrder());
				orderDto.setPackageId(order.getPackageId());
				orderDto.setPackageConsumptionAmt(order.getPackageConsumptionAmt());
				orderDto.setPackageTypeId(order.getPackageTypeId());
				orderDto.setDepartmentId(order.getSpecialityId());
				orderDto.setSubDepartmentId(order.getServiceSubSpecialityId());
				orderDto.setServiceRenderingDeptId(order.getServiceRenderingDeptId());
				orderDto.setIsRefDoctorShare('N');
				orderDto.setIsDocReq(order.getIsDoctorRequired());
				orderDto.setIsLabService(order.getPackageId()== null ? 'Y':'N');
				orderDto.setContractId(0);
				orderDto.setDebitNoteService(false);
				orderDto.setEncounterId(encounterMasterDto.getEncounterId());
				orderDto.setIsPackageService(order.getIsPackageService());
				orderDto.setIsPackage(order.getIsPackage());
				if (orderDto.getOrderDetailsPackageMapperDtosList() == null) {
					orderDto.setOrderDetailsPackageMapperDtosList(new ArrayList<>()); // Initialize the list
					orderDto.getOrderDetailsPackageMapperDtosList().addAll(order.getOrderDetailsPackageMapperDtosList());
				}

				if (orderDto.getOrderDetailsPayeeMapperDtosList() == null) {
					orderDto.setOrderDetailsPayeeMapperDtosList(new ArrayList<>()); // Initialize the list
					orderDto.getOrderDetailsPayeeMapperDtosList().addAll(order.getOrderDetailsPayeeMapperDtosList());
				}
				orderDetailsMasterDtosList.add(orderDto);
			}
		}
		target.setOrderDetailsMasterDtosList(orderDetailsMasterDtosList);

		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String formattedDate2 = formatter2.format(date);
		List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList = new ArrayList<>();
		PaymentReceiptDetailsDto payDto = new PaymentReceiptDetailsDto();
		payDto.setPaymentModeId(8);
		payDto.setTransactionType("UPI Payment");
		payDto.setTransactionTypeId(6);
		payDto.setAmount(encounterMasterDto.getPaidAmount());
	    payDto.setTransTransactionDate(formattedDate2);
		payDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
		paymentReceiptDetailsDtosList.add(payDto);
		target.setPaymentReceiptDetailsDtosList(paymentReceiptDetailsDtosList);

		return target;
	}

	private int getGlobalServiceRate(List<ContractConsumptionDto> listCon,EncounterMasterDto encounterMasterDto,UnitApplicationConfigurationDto unitApplicationConfigurationDto) {

		List<ContractConsumptionDto> rateList = new ArrayList<>();

		for (ContractConsumptionDto dto : listCon) {

			Response res =  iContractConsumptionService.getSerivceRateGlobal(dto, false, false);

			if(res.getObject() != null){

				ContractConsumptionDto obj =  (ContractConsumptionDto) res.getObject();
				obj.setKeyIndex(dto.getKeyIndex());
				rateList.add(obj);
			}
		}

		BillingMasterDto master12 = mapToBillingMaster(rateList, encounterMasterDto, unitApplicationConfigurationDto,null,null);
		try {
			updateService(master12);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return 0;
	}

	private BillingMasterDto mapToBillingMaster(List<ContractConsumptionDto> list, EncounterMasterDto encounterMasterDto,UnitApplicationConfigurationDto unitApplicationConfigurationDto,List<TPackageServicesDetailsDto> listPackService,MPackageMasterDto mPackageDto ) {

		long currentMillis = System.currentTimeMillis();
		Date date = new Date(currentMillis);

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate1 = formatter1.format(date);

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = formatter.format(date);

		BillingMasterDto target = new BillingMasterDto();

		target.setPatientId(encounterMasterDto.getPatientId());
		target.setEncounterId(encounterMasterDto.getEncounterId());
		target.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
		target.setCreatedDate(System.currentTimeMillis());
		target.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
		target.setUpdatedDate(System.currentTimeMillis());
		target.setOrderDate(formattedDate1);
		target.setOrganisationId(encounterMasterDto.getOrganizationId());
		target.setOrgId(encounterMasterDto.getOrganizationId());
		target.setUnitId(encounterMasterDto.getUnitId());
		target.setIsFinalBill('Y');
		target.setOrdDoctorId(0);
		target.setVisitTypeId(1);
		target.setBillDateTime(System.currentTimeMillis());
		target.setNetAmount(0.0);
		target.setDiscountAmount(0.0);
		target.setConcessionAmount(0.0);
		target.setGst(0.0);
		target.setTotalBillAmount(0.0);
		target.setRoundOff(0.0);
		target.setIsCreditBill('N');
		target.setBillCancelled('N');
		target.setIsFinancialClearance('N');
		target.setBillRemark("Nakoda");
		target.setBillStatusId(1);
		target.setSelfPayable(0.0);
		target.setCreditPayable(0.0);
		target.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
		target.setPaymentEntitlementId(1);
		target.setPatientTypeId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
		target.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
		target.setIsInterimBill('N');
		target.setIsDrNote('N');
		target.setContractId(0);
		target.setCounterId(0);
		target.setEncounterSpecialityId(0);
		target.setPatientClassId(1);
		target.setPharmacyServiceId(unitApplicationConfigurationDto.getPharmacyServiceId());
		target.setSelfPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
		target.setHomeCollection(false);
		target.setApplyAuth(false);
		target.setNeedToUpdatePharmacyService(false);
		target.setUpdatePayee(false);
		target.setIsPreBilling('Y');
		target.setCapExist(false);
		//target.setPackageExist(false);

		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();

		if(list!= null){
			for (ContractConsumptionDto order : list) {

				OrderDetailsMasterDto orderDto = new OrderDetailsMasterDto();
				orderDto.setOrderDetailsId(order.getOrderDetailsId());
				orderDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDto.setOrderQty(1);
				orderDto.setServiceId(order.getServiceId());
				orderDto.setPriorityId(1);
				orderDto.setIsOutsourced('N');
				orderDto.setOrderDate(System.currentTimeMillis());
				orderDto.setOrderSchDate(System.currentTimeMillis());
				orderDto.setPayeeId(order.getPayeeId());
				orderDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				orderDto.setBillingClassId(unitApplicationConfigurationDto.getOpdBillingCatId());
				orderDto.setServiceRendered(0);
				orderDto.setServiceRenderingDeptId(order.getSpecialityId());
				orderDto.setOrdRenderDatetime(System.currentTimeMillis());
				orderDto.setServiceIsBilled(0);
				orderDto.setStatus('A');
				orderDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setUpdatedDate(System.currentTimeMillis());
				orderDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setCreatedDate(System.currentTimeMillis());
				orderDto.setOrdCancelled('N');
				orderDto.setServiceAmt(BigDecimal.valueOf(order.getServiceRate()));
				orderDto.setCoPayPer(BigDecimal.valueOf(order.getCoShare()));
				orderDto.setOrdTotalAmt(BigDecimal.valueOf(order.getOrdTotalAmount()));
				orderDto.setTaxPer(BigDecimal.valueOf(order.getTaxPercentage()));
				orderDto.setTaxAmount(BigDecimal.valueOf(order.getTaxAmt()));
				orderDto.setNetAmount(BigDecimal.valueOf(order.getNetAmount()));
				orderDto.setSelfPayable(BigDecimal.valueOf(order.getSelfPayable()));
				orderDto.setCreditPayable(BigDecimal.valueOf(order.getCreaditPayable()));
				orderDto.setTaxId(order.getTaxId());
				orderDto.setIsDiscountApplicable('N');
				orderDto.setIsDiscount('N');
				orderDto.setOrderEmergencyFlag('N');
				orderDto.setIsDrug('N');
				orderDto.setPackageId(order.getPackageMasterId());
				orderDto.setPackageConsumptionAmt(order.getPackageConsumptionAmt());
				orderDto.setDepartmentId(order.getSpecialityId());
				orderDto.setSubDepartmentId(order.getSubSpecialityId());
				orderDto.setIsRefDoctorShare('N');
				orderDto.setIsDocReq('N');
				orderDto.setIsLabService(order.getPackageMasterId()== null ? 'Y':'N');
				orderDto.setContractId(order.getContractId());
				orderDto.setDebitNoteService(false);
				orderDto.setEncounterId(encounterMasterDto.getEncounterId());

				if (orderDto.getOrderDetailsPackageMapperDtosList() == null) {
					orderDto.setOrderDetailsPackageMapperDtosList(new ArrayList<>()); // Initialize the list
					orderDto.getOrderDetailsPackageMapperDtosList().addAll(order.getOrderDetailsPackageMapperDtosList());
				}

				if (orderDto.getOrderDetailsPayeeMapperDtosList() == null) {
					orderDto.setOrderDetailsPayeeMapperDtosList(new ArrayList<>()); // Initialize the list
					orderDto.getOrderDetailsPayeeMapperDtosList().addAll(order.getOrderDetailsPayeeMapperDtosList());
				}
				orderDetailsList.add(orderDto);
			}
		}else if(listPackService != null){

			OrderDetailsMasterDto orderDtoPack = new OrderDetailsMasterDto();
			orderDtoPack.setOrgId(encounterMasterDto.getOrganizationId());
			orderDtoPack.setOrgUnitId(encounterMasterDto.getUnitId());
			orderDtoPack.setOrderQty(1);
			orderDtoPack.setServiceId(mPackageDto.getServiceId());
			orderDtoPack.setPriorityId(1);
			orderDtoPack.setIsOutsourced('N');
			orderDtoPack.setOrderDate(System.currentTimeMillis());
			orderDtoPack.setOrderSchDate(System.currentTimeMillis());
			orderDtoPack.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			orderDtoPack.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			orderDtoPack.setBillingClassId(unitApplicationConfigurationDto.getOpdBillingCatId());
			orderDtoPack.setServiceRendered(0);
			orderDtoPack.setServiceRenderingDeptId(mPackageDto.getSpecialityId());
			orderDtoPack.setOrdRenderDatetime(System.currentTimeMillis());
			orderDtoPack.setServiceIsBilled(0);
			orderDtoPack.setStatus('A');
			orderDtoPack.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			orderDtoPack.setUpdatedDate(System.currentTimeMillis());
			orderDtoPack.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
			orderDtoPack.setCreatedDate(System.currentTimeMillis());
			orderDtoPack.setOrdCancelled('N');
			orderDtoPack.setServiceAmt(BigDecimal.valueOf(mPackageDto.getPackageCost()));
			orderDtoPack.setCoPayPer(BigDecimal.valueOf(0));
			orderDtoPack.setOrdTotalAmt(BigDecimal.valueOf(mPackageDto.getPackageCost()));
			orderDtoPack.setTaxPer(BigDecimal.valueOf(0));
			orderDtoPack.setTaxAmount(BigDecimal.valueOf(0));
			orderDtoPack.setNetAmount(BigDecimal.valueOf(mPackageDto.getPackageCost()));
			orderDtoPack.setSelfPayable(BigDecimal.valueOf(0));
			orderDtoPack.setCreditPayable(BigDecimal.valueOf(0));
			orderDtoPack.setTaxId(null);
			orderDtoPack.setIsDiscountApplicable('N');
			orderDtoPack.setIsDiscount('N');
			orderDtoPack.setPackageApplicableDate(formattedDate);
			orderDtoPack.setServiceRenderingDeptId(mPackageDto.getSubSpecialityId());
			orderDtoPack.setIsPackageService('N');
			orderDtoPack.setOrderEmergencyFlag('N');
			orderDtoPack.setIsDrug('N');
			orderDtoPack.setIsPackage(true);
			orderDtoPack.setPackageId(mPackageDto.getPackageMasterId());
			orderDtoPack.setPackageTypeId(mPackageDto.getPackageTypeId()); // use as package type
			orderDtoPack.setPackageConsumptionAmt(BigDecimal.valueOf(0));
			orderDtoPack.setDepartmentId(mPackageDto.getSpecialityId());
			orderDtoPack.setSubDepartmentId(mPackageDto.getSubSpecialityId());
			orderDtoPack.setIsRefDoctorShare('N');
			orderDtoPack.setIsDocReq('N');
			orderDtoPack.setIsLabService('N');
			orderDtoPack.setContractId(0);
			orderDtoPack.setDocSharePer(BigDecimal.valueOf(0));
			orderDtoPack.setRefDocSharePer(BigDecimal.valueOf(0));
			orderDtoPack.setDebitNoteService(false);
			orderDtoPack.setEncounterId(encounterMasterDto.getEncounterId());

			if (orderDtoPack.getOrderDetailsPackageMapperDtosList() == null) {
				orderDtoPack.setOrderDetailsPackageMapperDtosList(new ArrayList<>()); // Initialize the list
				OrderDetailsPackageMapperDto orderPack = new OrderDetailsPackageMapperDto();
				orderPack.setPackageAmount(BigDecimal.valueOf(0));
				orderPack.setIsCapService('N');
				orderPack.setServiceId(mPackageDto.getServiceId());
				orderPack.setServiceAmount(BigDecimal.valueOf(mPackageDto.getPackageCost()));
				orderPack.setIsPackageService('N');
				orderPack.setStatus('A');
				orderDtoPack.getOrderDetailsPackageMapperDtosList().add(orderPack);
			}

			if (orderDtoPack.getOrderDetailsPayeeMapperDtosList() == null) {
				orderDtoPack.setOrderDetailsPayeeMapperDtosList(new ArrayList<>()); // Initialize the list
				OrderDetailsPayeeMapperDto payeePack = new OrderDetailsPayeeMapperDto();
				payeePack.setCoShare(0.0);
				payeePack.setCreditPayable(BigDecimal.valueOf(0));
				payeePack.setIsRateEditable('N');
				payeePack.setPayeeAmount(BigDecimal.valueOf(mPackageDto.getPackageCost()));
				payeePack.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				payeePack.setSelfPayable(BigDecimal.valueOf(mPackageDto.getPackageCost()));
				payeePack.setServiceId(mPackageDto.getServiceId());

				orderDtoPack.getOrderDetailsPayeeMapperDtosList().add(payeePack);
			}
			orderDetailsList.add(orderDtoPack);


			for (TPackageServicesDetailsDto order : listPackService) {

				OrderDetailsMasterDto orderDto = new OrderDetailsMasterDto();
				orderDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDto.setOrderQty(1);
				orderDto.setServiceId(order.getServiceId());
				orderDto.setPriorityId(1);
				orderDto.setIsOutsourced('N');
				orderDto.setOrderDate(System.currentTimeMillis());
				orderDto.setOrderSchDate(System.currentTimeMillis());
				orderDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				orderDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				orderDto.setBillingClassId(unitApplicationConfigurationDto.getOpdBillingCatId());
				orderDto.setServiceRendered(0);
				orderDto.setServiceRenderingDeptId(order.getSpecialityId());
				orderDto.setOrdRenderDatetime(System.currentTimeMillis());
				orderDto.setServiceIsBilled(0);
				orderDto.setStatus('A');
				orderDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setUpdatedDate(System.currentTimeMillis());
				orderDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId());
				orderDto.setCreatedDate(System.currentTimeMillis());
				orderDto.setOrdCancelled('N');
				orderDto.setServiceAmt(BigDecimal.valueOf(order.getServicePrice()));
				orderDto.setCoPayPer(BigDecimal.valueOf(0));
				orderDto.setOrdTotalAmt(BigDecimal.valueOf(order.getServicePrice()));
				orderDto.setTaxPer(BigDecimal.valueOf(0));
				orderDto.setTaxAmount(BigDecimal.valueOf(0));
				orderDto.setNetAmount(BigDecimal.valueOf(order.getServicePrice()));
				orderDto.setSelfPayable(BigDecimal.valueOf(0));
				orderDto.setCreditPayable(BigDecimal.valueOf(0));
				orderDto.setTaxId(null);
				orderDto.setIsDiscountApplicable('N');
				orderDto.setIsDiscount('N');
				orderDto.setOrderEmergencyFlag('N');
				orderDto.setIsDrug('N');
				orderDto.setIsPackage(false);
				orderDto.setPackageId(order.getPackageId());
				orderDto.setPackageTypeId(encounterMasterDto.getRegistrationTypeId()); // use as package type
				orderDto.setPackageConsumptionAmt(BigDecimal.valueOf(order.getServicePrice()));
				orderDto.setDepartmentId(order.getSpecialityId());
				orderDto.setSubDepartmentId(order.getSubSpecialityId());
				orderDto.setServiceRenderingDeptId(order.getSubSpecialityId());
				orderDto.setIsRefDoctorShare(order.getIsRefDoctorShare());
				orderDto.setIsDocReq(order.getIsDocReq());
				orderDto.setIsPackageService('Y');
				orderDto.setIsLabService('Y');
				orderDto.setContractId(0);
				orderDto.setDocSharePer(BigDecimal.valueOf( order.getDocSharePer()));
				orderDto.setRefDocSharePer(BigDecimal.valueOf( order.getRefSharePer()));
				orderDto.setDebitNoteService(false);
				orderDto.setEncounterId(encounterMasterDto.getEncounterId());

				if (orderDto.getOrderDetailsPackageMapperDtosList() == null) {
					orderDto.setOrderDetailsPackageMapperDtosList(new ArrayList<>()); // Initialize the list
					OrderDetailsPackageMapperDto orderPack = new OrderDetailsPackageMapperDto();
					orderPack.setPackageAmount(BigDecimal.valueOf(order.getServicePrice()));
					orderPack.setIsCapService('N');
					orderPack.setServiceId(order.getServiceId());
					orderPack.setServiceAmount(BigDecimal.valueOf(order.getServicePrice()));
					orderPack.setIsPackageService('Y');
					orderPack.setStatus('A');
					orderPack.setPackageId(order.getPackageId());
					orderPack.setUnitId(encounterMasterDto.getUnitId());
					orderPack.setOrganizationId(encounterMasterDto.getOrganizationId());

					orderDto.getOrderDetailsPackageMapperDtosList().add(orderPack);
				}

				if (orderDto.getOrderDetailsPayeeMapperDtosList() == null) {
					orderDto.setOrderDetailsPayeeMapperDtosList(new ArrayList<>()); // Initialize the list
					OrderDetailsPayeeMapperDto payeePack = new OrderDetailsPayeeMapperDto();
					payeePack.setCoShare(0.0);
					payeePack.setCreditPayable(BigDecimal.valueOf(0));
					payeePack.setIsRateEditable('N');
					payeePack.setPayeeAmount(BigDecimal.valueOf(order.getServicePrice()));
					payeePack.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
					payeePack.setSelfPayable(BigDecimal.valueOf(order.getServicePrice()));
					payeePack.setServiceId(order.getServiceId());

					orderDto.getOrderDetailsPayeeMapperDtosList().add(payeePack);
				}
				orderDetailsList.add(orderDto);
		    }
		}
		target.setOrderDetailsMasterDtosList(orderDetailsList);

		return target;
	}

	private String myfileSynch(EncounterMasterDto encounterMasterDto) {

		try {

			String Global_Scheduling_URI = null;
			String MY_LIFE_URI = null;
			URI url = null;
			Response resp = iScheduleLogsDao.getConnectionStringForMyLife();
			MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) resp.getObject();
			if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
					&& connectionLinkDto.getMyLifeLink() != ""
					&& connectionLinkDto.getGlobalSchedulingLink() != null
					&& connectionLinkDto.getGlobalSchedulingLink() != "") {
				Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
				MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
			}

			Response response = iAppointmentMasterDao
					.getDoctorAppointmentToSync(encounterMasterDto.getAppointmentId());
			AppointmentSyncSchedulingDto appointment = (AppointmentSyncSchedulingDto) response.getObject();
			if (appointment != null) {

				appointment.setCreatedDate(
						GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy"));
				appointment.setApplicationFlag(true);
				String stringResponse = null;
				if (appointment.getDirectEncounter() == null) {
					appointment.setDirectEncounter('N');
				}
				appointment.setAppointmentStatusId(appointment.getAppointmentStatusId());

				if (appointment.getAppointmentStatusId() == 8) {
					appointment.setAppointmentStatusId(AppointmentStatusMasterConstant.PENDING);
				}
				if (appointment.getAppointmentStatusId() == AppointmentStatusMasterConstant.PENDING
						|| (appointment.getDirectEncounter().equals('Y')
						&& appointment.getAppointmentStatusId() == APPOINTMENTSTATUSID1)) {
					try {
						url = URI.create(Global_Scheduling_URI + "/appointment/bookAppointment");
						ObjectMapper objectMapper = new ObjectMapper();
						String json = objectMapper.writeValueAsString(appointment);
						System.out.println("Appointment SChedule JSON is:" + json);
						HttpHeaders headers = new HttpHeaders();
						String token = SmsUtility.generateJWTTokenMylife(Global_Scheduling_URI);
						headers.set("Authorization", "Bearer " + token);
						headers.set("userName", SmsUtility.USER_TOKEN_MYLIFE);
						headers.set("channelId",
								(appointment.getChannelId() != null && appointment.getChannelId() != 0)
										? appointment.getChannelId().toString()
										: "1");
						org.springframework.http.HttpEntity<AppointmentSyncSchedulingDto> request = new org.springframework.http.HttpEntity<>(appointment, headers);
						stringResponse = restTemplate.postForObject(url, request, String.class);
						Response commonResponse = null;
						commonResponse = gson.fromJson(stringResponse, Response.class);
						if (commonResponse.getStatus().equalsIgnoreCase(com.param.global.common.ICommonConstants.SUCCESS)) {
							if(encounterMasterDto.getPatientId() != null && encounterMasterDto.getAppointmentId() != null){
								iAppointmentSchedulingDao.updateIsAppointmentFromMyLife(encounterMasterDto.getPatientId(), encounterMasterDto.getAppointmentId());
							}

							System.out.println("Appointment Booked Successfully");
						} else {
							System.out.println("Appointment Booking Failed");
						}

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}

				}
			}

			return "OK";
		}catch (Exception e){

		}
		return "OK";
	}

    @RequestMapping(value = "/service/preBilling/orderedServicesByMobileNoForWebUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response orderedServicesByMobileNoForWebUser(@RequestBody AdmissionDto admissionDto) {
        try {
            return iBillingMasterService.orderedServicesByMobileNoForWebUser(admissionDto);
        } catch (Exception e) {

            throw e;
        }
        //return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
