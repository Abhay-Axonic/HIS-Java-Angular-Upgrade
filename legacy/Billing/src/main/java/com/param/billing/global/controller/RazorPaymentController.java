package com.param.billing.global.controller;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.*;

import com.param.global.razorpay.AxiosConfig;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
//import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.billing.global.transaction.service.RazorPayBillService;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.AesUtil;
import com.param.global.common.CCAvenueService;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Mpesa;
import com.param.global.common.Response;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.mpesa.dto.CCAvenueLogDto;
import com.param.global.opd.service.IAppointmentSchedulingService;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.razorpay.AppointmentConfirmationDto;
import com.param.global.razorpay.RazorPayOrderLogDetail;
import com.param.global.razorpay.RazorpayPaymentReqDto;
import com.param.global.service.IRazorPayService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
@SuppressWarnings({ "unchecked", "rawtypes","unused"})
@RestController
//@RequestMapping("api/global")
public class RazorPaymentController implements ICommonConstants{

	@Autowired
	IRazorPayService razorPayService;
	
	@Autowired
	IBillingMasterService iBillingMasterService;
	
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;
	
	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;
	
	@Autowired
	IEncounterMasterService encounterMasterServ;
	
	@Autowired
	private RazorPayBillService razorPayBillService;

	@Autowired
	private AxiosConfig axiosConfig;
	
	final static Logger logger = Logger.getLogger(RazorPaymentController.class);

	@RequestMapping(value="/getOrderIdForRazorPayPayment", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOrderIdForRazorPayPayment(@RequestBody RazorpayPaymentReqDto reqDto ){
		try
		{
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
			UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
			unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
			unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationService
					.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
			if (appConfigRes.getObject() != null) {
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			}

			RazorpayClient razorpay = new RazorpayClient(unitApplicationConfigurationDto.getRazorPayKeyId(), unitApplicationConfigurationDto.getRazorPaySecretKey());
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", reqDto.getRazorPayBillAmount()); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			Order order = razorpay.Orders.create(orderRequest);

			reqDto.setOrderId(order.get("id"));
			razorPayService.logRazorPayOrder(reqDto);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reqDto);
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
		}
	}

	@RequestMapping(value="/getOrderIdUnitSubscriptionPay", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOrderIdUnitSubscriptionPay(@RequestBody RazorpayPaymentReqDto reqDto ){
		try
		{
			RazorpayClient razorpay = new RazorpayClient(axiosConfig.getRazorpayKeyId(), axiosConfig.getRazorpaySecretId());
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", reqDto.getRazorPayBillAmount()); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			Order order = razorpay.Orders.create(orderRequest);

			reqDto.setOrderId(order.get("id"));
			razorPayService.logRazorPayOrder(reqDto);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reqDto,(Object)axiosConfig.getRazorpayKeyId());
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
		}
	}

 @RequestMapping(value="/confirmAppointment", method=RequestMethod.POST)
	public ModelAndView confirmAppointment(HttpServletRequest request, Model model) {
	       ModelAndView modelAndView = new ModelAndView("web-appointment-confirmation");
		try {
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
			Date currentDate = new Date();
			AppointmentConfirmationDto reqDto = null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strCurrentDate = formatter.format(currentDate);
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			String strCurrentDate2 = formatter1.format(currentDate);

			String razorPayPaymentId = request.getParameter("razorpay_payment_id");
			String razorPayOrderId = request.getParameter("razorpay_order_id");
			String razorPaySignature = request.getParameter("razorpay_signature");
			Response razorPayRes = razorPayService.getAppointmentDataByRazorPayOrderId(razorPayOrderId);
			reqDto = (AppointmentConfirmationDto) razorPayRes.getObject();
			UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
			unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
			unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationService
					.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
			if (appConfigRes.getObject() != null) {
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			}

			// Verify this is a genuine, completed Razorpay payment before creating a bill from
			// it -- previously razorPaySignature was read and never checked, and the amount
			// used below came from a client-supplied field with no independent verification.
			// See docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md.
			Response verifyRes = razorPayService.verifyRazorpayPayment(razorPayOrderId, razorPayPaymentId, razorPaySignature,
					reqDto.getUnitId(), reqDto.getOrganizationId());
			if (!SUCCESS.equals(verifyRes.getStatus())) {
				logger.error("PAYMENT_RECONCILIATION_GAP: Razorpay verification failed on /confirmAppointment for order " + razorPayOrderId + " -- bill NOT created. " + verifyRes.getMessage());
				return modelAndView;
			}
			Double razorpayVerifiedPaidAmount = (Double) verifyRes.getObject();

			SlotMasterDto appointmentDto = new SlotMasterDto();
			appointmentDto.setUnitId(reqDto.getUnitId());
			appointmentDto.setOrganizationId(reqDto.getOrganizationId());
			appointmentDto.setUpdatedBy(0);
			appointmentDto.setUpdatedDate(strCurrentDate);
			appointmentDto.setCreatedDate(strCurrentDate);
			appointmentDto.setStatus('A');
			String splitAppDate[] = reqDto.getScheduledDate().split("-");

			appointmentDto.setAppointmentDate(splitAppDate[2] + "-" + splitAppDate[1] + "-" + splitAppDate[0]);
			appointmentDto.setPatientId(reqDto.getPatientId());
			appointmentDto.setAppointmentStatusId(IAppointmentAndSlotStatusMasterConstants.Pending);
			appointmentDto.setAppointmentTypeId(APPOINTMENTTYPEID1);
			appointmentDto.setPriorityId(1);
			appointmentDto.setRemark(reqDto.getRemark());
			appointmentDto.setIsTeleconsultationAppointment(reqDto.getIsTeleConsultApp());
			appointmentDto.setIsPaidAppointment('Y');
			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();

			encounterMasterDto.setAppointmentId(reqDto.getAppointmentId());
			encounterMasterDto.setPatientId(reqDto.getPatientId());
			encounterMasterDto.setIsAppointmentFromMyLife('N');
			encounterMasterDto.setOrganizationId(reqDto.getOrganizationId());
			encounterMasterDto.setUnitId(reqDto.getUnitId());
			encounterMasterDto.setDoctorId(reqDto.getDoctorId());
			encounterMasterDto.setDeptId(reqDto.getSpecialityId());
			encounterMasterDto.setEncounterDate(currentDate.getTime());
			encounterMasterDto.setEncounterTime(reqDto.getFromTimeSlot());
			encounterMasterDto.setPaymentEntitlementTypeId(SELF);
			encounterMasterDto.setCreatedBy(0);
			encounterMasterDto.setUpdatedBy(0);
			encounterMasterDto.setCreatedDate(currentDate.getTime());
			encounterMasterDto.setUpdatedDate(currentDate.getTime());
			encounterMasterDto.setUserDefinedVisitTypeId(reqDto.getVisitTypeId());
			encounterMasterDto.setSystemGeneratedVisitTypeId(reqDto.getVisitTypeId());
			encounterMasterDto.setStatus('A');
			encounterMasterDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			encounterMasterDto.setIsScheduledAppointment('A');
			encounterMasterDto.setSpecialityId(reqDto.getSpecialityId());
			encounterMasterDto.setEncounterTypeId(1);
			encounterMasterDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			encounterMasterDto.setBillingBedCategoryId(1);
			encounterMasterDto.setIsPreOrPostBilling('Y');
			encounterMasterDto.setDeptWiseAppointment('N');
			encounterMasterDto.setIsTransferToActiveDeposit('N');
			encounterMasterDto.setIsReshedule('N');
			encounterMasterDto.setTotalBillAmt(razorpayVerifiedPaidAmount);
			encounterMasterDto.setPaidAmount(razorpayVerifiedPaidAmount);
			encounterMasterDto.setCurrentDate(strCurrentDate2);
			// encounterMasterDto.setIsReshedule('N');
			List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList = new ArrayList<PaymentReceiptDetailsDto>();
			PaymentReceiptDetailsDto paymentReceiptDetailsDto = new PaymentReceiptDetailsDto();
			paymentReceiptDetailsDto.setPaymentModeId(8);
			paymentReceiptDetailsDto.setAmount(razorpayVerifiedPaidAmount);
			paymentReceiptDetailsDto.setTransactionNo(razorPayPaymentId);
			paymentReceiptDetailsDto.setTransTransactionDate(strCurrentDate);
			paymentReceiptDetailsDto.setCreatedBy(0);
			paymentReceiptDetailsDtosList.add(paymentReceiptDetailsDto);
			encounterMasterDto.setPaymentReceiptDetailsDtosList(paymentReceiptDetailsDtosList);

			List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList = new ArrayList<PatientSponsorDetailsDto>();
			PatientSponsorDetailsDto patientSponsorDetailsDto = new PatientSponsorDetailsDto();
			patientSponsorDetailsDto.setOrganizationId(reqDto.getOrganizationId());
			patientSponsorDetailsDto.setUnitId(reqDto.getUnitId());
			patientSponsorDetailsDto.setVisitTypeId(OP);
			patientSponsorDetailsDto.setPaymentEntitlementId(SELF);
			patientSponsorDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			patientSponsorDetailsDto.setPriorityId(1);
			patientSponsorDetailsDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			patientSponsorDetailsDto.setCreatedBy(0);
			patientSponsorDetailsDto.setUpdatedBy(0);
			patientSponsorDetailsDto.setCreatedDate(strCurrentDate);
			patientSponsorDetailsDto.setUpdatedDate(strCurrentDate);
			patientSponsorDetailsDto.setStatus('A');
			patientSponsorDetailsDtosList.add(patientSponsorDetailsDto);
			encounterMasterDto.setPatientSponsorDetailsDtosList(patientSponsorDetailsDtosList);
			encounterMasterDto.setAppointmentObject(appointmentDto);
			encounterMasterDto.setListKinDetailsDto(new ArrayList<KinDetailsDto>());
			encounterMasterDto.setIsFromWebAppointment('Y');
			
			String visit = "";
			if (reqDto.getVisitTypeId().equals(OP)
					&& (reqDto.getIsTeleConsultApp() == null || reqDto.getIsTeleConsultApp() == 0)) {
				visit = "Consultation(In Person)";
			} else if (reqDto.getVisitTypeId().equals(IP)
					&& (reqDto.getIsTeleConsultApp() == null || reqDto.getIsTeleConsultApp() == 0)) {
				visit = "Consultation(Follow Up)";
			} else if (reqDto.getVisitTypeId().equals(OP)
					&& (reqDto.getIsTeleConsultApp() != null || reqDto.getIsTeleConsultApp() == 1)) {
				visit = "Consultation(Video)";
			} else {
				visit = "Consultation(Follow Up Video)";
			}
			
			
			modelAndView.addObject("patientName", reqDto.getPatientName());
			modelAndView.addObject("doctorName", reqDto.getDoctorName());
			modelAndView.addObject("mobileNumber", reqDto.getMobileNumber());
			modelAndView.addObject("emailId", reqDto.getEmailId());
			modelAndView.addObject("unitAddress", reqDto.getUnitAddress());
			modelAndView.addObject("doctorImage", reqDto.getDoctorImage());
			modelAndView.addObject("areaOfExperty", reqDto.getAreaOfExperty());
			modelAndView.addObject("doctorDegrees", reqDto.getDoctorDegrees());
			modelAndView.addObject("scheduledDate", reqDto.getScheduledDate());
			modelAndView.addObject("consultingSpeciality", reqDto.getConsultingSpeciality());
			modelAndView.addObject("visitDuration", reqDto.getVisitDuration());
			// NOTE: kept as the pre-verification client-asserted amount deliberately -- this is
			// a cosmetic confirmation-page display value only, not used for anything financial
			// (the actual bill above now uses razorpayVerifiedPaidAmount). Flagged in
			// docs/REVIEW_DRAFT_razorpay-auth-fix-NOT-APPLIED.md sec 1 as an open question for
			// the owner (switching this too would be a Behavioral Change if the two ever
			// disagree) -- left as-is pending that decision, not silently changed.
			modelAndView.addObject("visitFee", reqDto.getPaidAmount());
			modelAndView.addObject("appointmentReason", reqDto.getAppointmentReason());
			modelAndView.addObject("fromTime", reqDto.getFromTime());
			modelAndView.addObject("patientVisit", visit);
			
			Response billRes = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
			BillPaymentCollectionReqDto collectionReqObj=(BillPaymentCollectionReqDto)billRes.getResObject();
			razorPayBillService.settleBill(collectionReqObj);
			
//		Response billRes = iBillingMasterService.saveBillFromKioskFutureDate(encounterMasterDto);
//		if (billRes.getStatus().equals(SUCCESS)) {
//			BillPaymentCollectionReqDto billCollReq = (BillPaymentCollectionReqDto) billRes.getResObject();
//			billCollReq.setIsPaidAppointment('Y');
//			iBillingMasterService.collectPayment(billCollReq);
//		}
//
//		Response paidAppRes=iAppointmentSchedulingService.updateAppointmentPaidStatus(reqDto.getAppointmentId());
		return modelAndView;
		
	
		} catch (Exception e) {
			logger.error("PAYMENT_RECONCILIATION_GAP: exception in confirmAppointment (/confirmAppointment) after a Razorpay payment redirect -- the bill/appointment for this payment may not have been created; manual reconciliation may be needed. " + e.getMessage(), e);
			return modelAndView;
		}
	}


 // /confirmAppointment1 removed 2026-07-15: unauthenticated debug/test endpoint hardcoding a
 // single specific Razorpay order ID ("order_HREZNzb6cSwewS"), taking no real request
 // parameters. Full-tree search (Java + JS) found zero callers. Was an unauthenticated
 // information-disclosure surface for whichever real patient/appointment that hardcoded order
 // belonged to. See docs/REVIEW_DRAFT_payment-fixes-FINAL.md sec 2b.
 
 
 
	@RequestMapping(value = "/getRazerPay", method = RequestMethod.GET)
	public @ResponseBody Response getRazerPay(@RequestParam(value = "razorpay_payment_id") String razorpay_payment_id,
			@RequestParam(value = "razorpay_payment_link_id") String razorpay_payment_link_id, @RequestParam(value = "razorpay_payment_link_reference_id") String searchPar,
			@RequestParam(value = "razorpay_payment_link_status") String razorpay_payment_link_status, @RequestParam(value = "razorpay_signature") String razorpay_signature) {
		try {
			
			String razorpayPaymentId = razorpay_payment_id ;
			String razorpayPaymentLinkId = razorpay_payment_link_id ;
			String searchPar1 = searchPar ;
			String razorpayPaymentLinkStatus = razorpay_payment_link_status ;
			String razorpaySignature = razorpay_signature ;
					
			razorPayService.updatePaymentLinkLog(razorpayPaymentLinkStatus,razorpayPaymentLinkId); 
			
			return null;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/razorPayPaymentLink", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response razorPayPaymentLink(@RequestBody RazorpayPaymentReqDto reqDto ) {
		try
		{
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
			UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
			unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
			unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationService
					.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
			if (appConfigRes.getObject() != null) {
				
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
				
				if(unitApplicationConfigurationDto.getPayGatewayProviderId() != null && unitApplicationConfigurationDto.getPayGatewayProviderId() == 1) {
					
					Response paymentDetails = razorPayService.getPayementDetailsByMobileNo(reqDto.getMobileNo());
						
						Boolean isUsed = true;
						Boolean checkIsUsed = false;
						
						if(paymentDetails.getObject() == null) {
							
							isUsed = false;
							
						}else {
							
							RazorpayPaymentReqDto dto = (RazorpayPaymentReqDto) paymentDetails.getObject();
							
							if(dto.getIsUsed().equals(checkIsUsed)) {
								
								String pattern = "yyyy-MM-dd HH:mm:ss";
								 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
									
								 	
									Date targetTime =  TimeZoneComponent.getDateByzone(reqDto.getUnitId());
									String date1 = simpleDateFormat.format(targetTime);
									Date getAddedDate = simpleDateFormat.parse(date1);   
									
									
									Date getDate = simpleDateFormat.parse(dto.getCreatedDate());
									getDate = DateUtils.addMinutes(getDate, 20); 
								
									if (getAddedDate.after(getDate)) {
									    
										razorPayService.updateRazorPayStatus(dto.getRazorpayOrderDetId());
									    isUsed = false;
									    
									}else {
										
										return new Response(SUCCESS, SUCCESS_CODE, "Already you have sent payment link on "+reqDto.getMobileNo(), null, dto);
									
									}
								
							}else {
								
								isUsed = false;
							}
						}
						
						if(isUsed.equals(checkIsUsed)) {
							
							BigDecimal razorAmountInRupees = BigDecimal.valueOf(reqDto.getRazorAmount());
							Integer manupulateAmount = razorAmountInRupees.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
							
							Date targetTime =  TimeZoneComponent.getDateByzone(reqDto.getUnitId());
							targetTime = DateUtils.addMinutes(targetTime,20); 
							Long expireBy  = targetTime.getTime()/1000L;
							
							String referenceId = expireBy.toString();
							
							Map<String, String> res = Mpesa.RazorPaytransactionStatus(unitApplicationConfigurationDto.getRazorPayKeyId(),unitApplicationConfigurationDto.getRazorPaySecretKey(),reqDto.getName(),reqDto.getEmail(),reqDto.getMobileNo(),manupulateAmount,unitApplicationConfigurationDto.getRazorPayCallBackUrl(),referenceId,expireBy);
							
							  for (Map.Entry<String,String> entry : res.entrySet()) {
						            System.out.println("Key = " + entry.getKey() +
						                             ", Value = " + entry.getValue());
						            reqDto.setUniqueId(entry.getKey());
						            reqDto.setShortUrl(entry.getValue());
							}
							razorPayService.savePaymentLinkLog(reqDto);
							
							return new Response(SUCCESS, SUCCESS_CODE, "Payment link send successfully", null, reqDto);
						}
				
				}else {
					
					return new Response(ERROR, COMMON_ERROR_CODE, "Please configure CCAvenue payment gateway and API URL", null, reqDto);
				}
				
			}else {
				
				return new Response(ERROR, COMMON_ERROR_CODE, "Provided Razor Pay key is Invalid ", null, reqDto);
			}
			
			
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, "Provided Razor Pay key is Invalid ", null, reqDto);
	}
	
	@RequestMapping(value="/validateRazorPayPaymentLink", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response ValidateRazorPayPaymentLink(@RequestBody RazorpayPaymentReqDto reqDto ) {
		try
		{
				String status = "paid";
				Response paymentDetails = razorPayService.getPayementDetailsByMobileNo(reqDto.getMobileNo());
				
				Boolean isUsed = true;
				Boolean checkIsUsed = false;
				
				if(paymentDetails.getObject() == null) {
					
					return new Response(ERROR, COMMON_ERROR_CODE, "Please first send payment link request then validate", null, null);
					
				}else {
					
					RazorpayPaymentReqDto dto = (RazorpayPaymentReqDto) paymentDetails.getObject();
					
					if(dto.getIsUsed().equals(checkIsUsed)) {
						
						if(dto.getPaid() != null) {
							
							return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
							
						}else {
							
							return new Response(ERROR, COMMON_ERROR_CODE, "Please wait user did not pay amount", null, null);

						}

					}else {

						return new Response(ERROR, COMMON_ERROR_CODE, "Please first send payment link request then validate", null, null);
					}
				}



		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
		}
	}
	
	@RequestMapping(value="/getOrderIdForRazorPayPaymentOnBilling", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOrderIdForRazorPayPaymentOnBilling(@RequestBody RazorpayPaymentReqDto reqDto ) {
		try
		{
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
			UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
			unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
			unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationService
					.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
			if (appConfigRes.getObject() != null) {
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			}
			
			BigDecimal razorAmountInRupees = BigDecimal.valueOf(reqDto.getRazorAmount());
			Integer manupulateAmount = razorAmountInRupees.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
			
			RazorpayClient razorpay = new RazorpayClient(unitApplicationConfigurationDto.getRazorPayKeyId(), unitApplicationConfigurationDto.getRazorPaySecretKey());
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", manupulateAmount); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");
			
			Order order = razorpay.Orders.create(orderRequest);
			
			reqDto.setUniqueId(order.get("id"));
			reqDto.setApiKey(unitApplicationConfigurationDto.getRazorPayKeyId());
			//reqDto.setRazorAmount(manupulateAmount.doubleValue());
			razorPayService.savePaymentLinkLog(reqDto);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reqDto);
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
		}
	}
	
	
	 
@RequestMapping(value="/confirmPayment", method=RequestMethod.POST)
	public ModelAndView confirmPayment(HttpServletRequest request, Model model) {
	       ModelAndView modelAndView = new ModelAndView("web-appointment-confirmation");
		try {
			
			String razorPayPaymentId = request.getParameter("razorpay_payment_id");
			String razorPayOrderId = request.getParameter("razorpay_order_id");
			String razorPaySignature = request.getParameter("razorpay_signature");

			// Verify before trusting the caller's claim -- previously this marked the log
			// "paid" unconditionally based on nothing but the caller hitting this URL. See
			// docs/REVIEW_DRAFT_payment-fixes-FINAL.md sec 2a (also documents a separate,
			// pre-existing, out-of-scope bug found here: this endpoint's updatePaymentLinkLog
			// call matches on unique_id, not order_id, so it likely already silently no-ops --
			// not fixed here, only the missing verification is).
			Response orderLogRes = razorPayService.getOrderLogDetailsByOrderId(razorPayOrderId);
			if (orderLogRes == null || orderLogRes.getObject() == null) {
				logger.error("PAYMENT_RECONCILIATION_GAP: confirmPayment -- no order log found for " + razorPayOrderId + " -- log NOT updated.");
				return null;
			}
			RazorPayOrderLogDetail orderLog = (RazorPayOrderLogDetail) orderLogRes.getObject();
			Response verifyRes = razorPayService.verifyRazorpayPayment(razorPayOrderId, razorPayPaymentId, razorPaySignature,
					orderLog.getUnitId(), orderLog.getOrganizationId());
			if (!SUCCESS.equals(verifyRes.getStatus())) {
				logger.error("PAYMENT_RECONCILIATION_GAP: confirmPayment -- Razorpay verification failed for order " + razorPayOrderId + " -- log NOT updated. " + verifyRes.getMessage());
				return null;
			}
			razorPayService.updatePaymentLinkLog("paid", razorPayOrderId);
		return null;


		} catch (Exception e) {
			logger.error("PAYMENT_RECONCILIATION_GAP: exception in confirmPayment (/confirmPayment) after a Razorpay payment redirect -- the payment-link log for this payment may not have been updated; manual reconciliation may be needed. " + e.getMessage(), e);
			return modelAndView;
		}
	}
	
@RequestMapping(value="/sendCCAvenueLink", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody Response sendCCAvenueLink(@RequestBody CCAvenueLogDto reqDto ) {
	try
	{
		UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
		UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
		unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
		unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
		Integer addMinuteTime = null;
		Response appConfigRes = iUnitApplicationBillingConfigurationService
				.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
		if (appConfigRes.getObject() != null) {
			
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			
			Response ApiUrl = razorPayService.getCCAvenueAPIUrl(reqDto.getUnitId());
			CCAvenueLogDto ccurl = new CCAvenueLogDto();
			if(ApiUrl.getObject() != null) {
				ccurl = (CCAvenueLogDto)ApiUrl.getObject();
			}
			
			if(unitApplicationConfigurationDto.getPayGatewayProviderId() != null && unitApplicationConfigurationDto.getPayGatewayProviderId() == 2 && ccurl.getPayLink()!= null) {
				
				Response paymentDetails = razorPayService.getCCAvenuePayementDetailsByMobileNo(reqDto.getMobileNo(),reqDto.getPatientId());
				addMinuteTime = ccurl.getCcavenueUrlExpiredTime()!=null ?  ccurl.getCcavenueUrlExpiredTime()  : 15; 
				Boolean isUsed = true;
				Boolean checkIsUsed = false;
				
				if(paymentDetails.getObject() == null) {
					
					isUsed = false;
					
				}else {
					
					CCAvenueLogDto dto = (CCAvenueLogDto) paymentDetails.getObject();
					
					if(dto.getIsUsed().equals(checkIsUsed)) {
						
						 String pattern = "yyyy-MM-dd HH:mm:ss";
						 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
							
						 	
							Date targetTime =  TimeZoneComponent.getDateByzone(reqDto.getUnitId());
							String date1 = simpleDateFormat.format(targetTime);
							Date getAddedDate = simpleDateFormat.parse(date1);   
							
							
							Date getDate = simpleDateFormat.parse(dto.getCreatedDate());
							getDate = DateUtils.addMinutes(getDate, addMinuteTime); 
							if (getAddedDate.after(getDate)) {
							    
							    razorPayService.updateCCAvenueStatus(dto.getCcCvenueLogId());
							    isUsed = false;
							}else {
								
								if(reqDto.getRequestAmount().equals(dto.getRequestAmount())) {
									return new Response(SUCCESS, SUCCESS_CODE, "Payment link send successfully", null, dto);
								}else {
									
									razorPayService.updateCCAvenueStatus(dto.getCcCvenueLogId());
									
									isUsed = false;
								}
								
							}
						
						
					}else {
						
						isUsed = false;
					}
				}
				
				if(isUsed.equals(checkIsUsed)) {
					
					String email = null;
					String billDeliveryType = "EMAIL";
					if((reqDto.getEmailId() == null  || reqDto.getEmailId() == "") && reqDto.getPatientId()!= null) {
						
						 email =  razorPayService.getpatientEmail(reqDto.getPatientId());
					}else {
						 email = reqDto.getEmailId();
					}
					
					if(email == null || email.equals("") ) {
						billDeliveryType = "SMS";
						email = "test@gmail.com";
					}
					
					  String generatepXmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
					    		"<Generate_Invoice_Query>" + 
					    		"<customer_name>"+reqDto.getName()+"</customer_name>" + 
					    		"<bill_delivery_type>"+billDeliveryType+"</bill_delivery_type>" + 
					    		"<customer_mobile_no>"+reqDto.getMobileNo()+"</customer_mobile_no>" + 
					    		"<customer_email_id>"+email+ 
					    		"</customer_email_id>" + 
					    		"<customer_email_subject>Test</customer_email_subject>" + 
					    		"<invoice_description>Test</invoice_description>" + 
					    		"<currency>INR</currency>" + 
					    		"<valid_for>"+addMinuteTime+"</valid_for>" + 
					    		"<valid_type>minutes</valid_type>" + 
					    		"<amount>"+reqDto.getRequestAmount()+"</amount>" + 
					    		"<merchant_reference_no>123456987123</merchant_reference_no>" + 
					    		"<sms_content>Pls pay your  bill  Invoice_ID for" + 
					    		"Invoice_CurrencyInvoice_Amountonlineat" + 
					    		"Pay_Link.</sms_content> </Generate_Invoice_Query>";
					    String generatepCommand="generateQuickInvoice";
					    String pAccessCode=unitApplicationConfigurationDto.getRazorPayKeyId();//"AVIS57IK34AY45SIYA";
					    String aesKey=unitApplicationConfigurationDto.getRazorPaySecretKey();//"B363F6F03C432E3D899A6E326160A931";
					    String pRequestType="xml";
					    String pResponseType="xml";
					    String GenerateInvoiceCCavenueApi = new CCAvenueService().generateInvoiceCCavenueApi(generatepXmlData, pAccessCode, generatepCommand, aesKey, pRequestType, pResponseType,ccurl.getPayLink());
					    
					    if(GenerateInvoiceCCavenueApi!=null && !GenerateInvoiceCCavenueApi.equals("")){
					    	
				              Map hm=new CCAvenueService().tokenizeToHashMap(GenerateInvoiceCCavenueApi, "&", "=");
				             String encResXML = hm.containsKey("enc_response")?hm.get("enc_response").toString():"";
				              String vStatus = hm.containsKey("status")?hm.get("status").toString():"";
				              String vError_code = hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
				           
				              if(vStatus.equals("1")){//If Api call failed
				            	  
				            	  return new Response(ERROR, COMMON_ERROR_CODE, "CCAvenue payment gateway is down please try after some time", null, reqDto);
				              }
				              if(!encResXML.equals("")) {
				            	  
				                  AesUtil aesUtil=new AesUtil(aesKey);
				                  String decXML = aesUtil.decrypt(encResXML);
				                  System.out.println("enc_response : "+decXML); 
				                  
				                  DocumentBuilderFactory dbf =
				                          DocumentBuilderFactory.newInstance();
				                      DocumentBuilder db = dbf.newDocumentBuilder();
				                      InputSource is = new InputSource();
				                      is.setCharacterStream(new StringReader(decXML));
				                  
				                  DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				                  InputSource src = new InputSource();
				                  src.setCharacterStream(new StringReader(decXML));
				                  Document doc = db.parse(is);
				                  NodeList tiny_url = doc.getElementsByTagName("tiny_url");
				                  Element tinyUrl = (Element) tiny_url.item(0);
				                  System.out.println("Name: " + getCharacterDataFromElement(tinyUrl));
				                  String url =  getCharacterDataFromElement(tinyUrl);
				                  
				                  NodeList invoice_id = doc.getElementsByTagName("invoice_id");
				                  Element invoiceId = (Element) invoice_id.item(0);
				                  System.out.println("Name: " + getCharacterDataFromElement(invoiceId));
				                  String orderCode =  getCharacterDataFromElement(invoiceId);
						             reqDto.setOrderNo(orderCode);
						             reqDto.setPayLink(url);
						             reqDto.setResultCode(vStatus);
				                  razorPayService.saveCCAvenueLog(reqDto);
				                  return new Response(SUCCESS, SUCCESS_CODE, "Payment link send successfully", null, reqDto);
				              }
				              
				          }else {
				        	  
				        	  return new Response(ERROR, COMMON_ERROR_CODE, "CCAvenue payment gateway is down please try after some time", null, reqDto);
				          }
				}
				
				
			}else {
				
				return new Response(ERROR, COMMON_ERROR_CODE, "Please configure CCAvenue payment gateway and API URL", null, reqDto);
			}
			
		}else {
			
			return new Response(ERROR, COMMON_ERROR_CODE, "Please configure unit ", null, reqDto);
		}
		
		
	}catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, "Provided Pay key is Invalid ", null, reqDto);
}

	


@RequestMapping(value="/validateCCAvenuePayment", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody Response validateCCAvenuePayment(@RequestBody CCAvenueLogDto reqDto ) {
	try
	{
		
		
		UnitApplicationConfigurationDto unitApplicationConfigurationDto = null;
		UnitApplicationConfigurationDto unitAppConfigReqDto = new UnitApplicationConfigurationDto();
		unitAppConfigReqDto.setOrganizationId(reqDto.getOrganizationId());
		unitAppConfigReqDto.setUnitId(reqDto.getUnitId());
		Response appConfigRes = iUnitApplicationBillingConfigurationService
				.getUnitApplicationBillingConfigByOrgUnit(unitAppConfigReqDto);
		
		if (appConfigRes.getObject() != null) {
			
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();
			
			Response ApiUrl = razorPayService.getCCAvenueAPIUrl(reqDto.getUnitId());
			CCAvenueLogDto url = new CCAvenueLogDto();
			if(ApiUrl.getObject() != null) {
				url = (CCAvenueLogDto)ApiUrl.getObject();
			}
			
			if(unitApplicationConfigurationDto.getPayGatewayProviderId() != null && unitApplicationConfigurationDto.getPayGatewayProviderId() == 2 && url.getPayLink()!= null) {
				Response paymentDetails = razorPayService.getCCAvenuePayementDetailsByMobileNo(reqDto.getMobileNo(),reqDto.getPatientId());
				String resultCode = "0";
				Boolean checkIsUsed = false;
				
				if(paymentDetails.getObject() == null) {
					
					return new Response(ERROR, COMMON_ERROR_CODE, "Please first send payment link request then validate", null, null);
					
				}else {
					
					CCAvenueLogDto dto = (CCAvenueLogDto) paymentDetails.getObject();
					
					if(dto.getIsUsed().equals(checkIsUsed)) {
						
						if(dto.getResultCode() != null  && dto.getResultCode().equals(resultCode) && dto.getPaidAmount()!= null) {
							
							return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
							
						}else {
							
							  	String pXmlData="<?xml version='1.0' encoding='UTF-8' standalone='yes'?><Order_Status_Query order_no= '"+dto.getOrderNo()+"' reference_no=''/>";
							    String pAccessCode=unitApplicationConfigurationDto.getRazorPayKeyId();//"AVIS57IK34AY45SIYA";
							    String aesKey=unitApplicationConfigurationDto.getRazorPaySecretKey();//"B363F6F03C432E3D899A6E326160A931";
							    String pCommand="orderStatusTracker";
							    String pRequestType="xml";
							    String pResponseType="xml";
							    String pVersion="1.2";
							    
							    String resStatus = new CCAvenueService().callCCavenueStatusApi(pXmlData, pAccessCode, pCommand, aesKey, pRequestType, pResponseType,pVersion,url.getPayLink());
							    if(resStatus!=null && !resStatus.equals("")){
						              Map hm=new CCAvenueService().tokenizeToHashMap(resStatus, "&", "=");
						              String encResXML = hm.containsKey("enc_response")?hm.get("enc_response").toString():"";
						              String vStatus = hm.containsKey("status")?hm.get("status").toString():"";
						              String vError_code = hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
						           
						              if(vStatus.equals("1")){//If Api call failed
						            	  
						            	  logger.info("enc_response : "+ encResXML);
						            	  logger.error("error_code : "+ vError_code);
						                  return new Response(ERROR, COMMON_ERROR_CODE, "Please wait user did not pay amount", null, null);
						                  
						              }
						              if(!encResXML.equals("")) {
						            	  
						                  AesUtil aesUtil=new AesUtil(aesKey);
						                  String decXML = aesUtil.decrypt(encResXML);
						                  logger.info("enc_response : "+decXML);  
						                  
						                  DocumentBuilderFactory dbf =
						                          DocumentBuilderFactory.newInstance();
						                      DocumentBuilder db = dbf.newDocumentBuilder();
						                      InputSource is = new InputSource();
						                      is.setCharacterStream(new StringReader(decXML));
						                  
						                  DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						                  InputSource src = new InputSource();
						                  src.setCharacterStream(new StringReader(decXML));
						                  Document doc = db.parse(is);
						                  NodeList ordeAm = doc.getElementsByTagName("order_amt");
						                  Element ordeAmLi = (Element) ordeAm.item(0);
						                  String padAmount =  getCharacterDataFromElement(ordeAmLi);
						                  
						                  NodeList order_status = doc.getElementsByTagName("order_status");
						                  Element orderstatus = (Element) order_status.item(0);
						                  String status =  getCharacterDataFromElement(orderstatus);
						                  
						                  String shipped = "Shipped";
						                  String successful = "Successful";
						                  if(status.equals(shipped) || status.equals(successful)) {
						                	  dto.setPaidAmount(Double.parseDouble(padAmount));
						                	  dto.setResultCode(vStatus);
						                	  dto.setResultDescription(status);
							                  razorPayService.updatePaidAmount(dto);
							                  
							                  return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
						                  }else {
						                	  
						                	  return new Response(ERROR, COMMON_ERROR_CODE, "This transaction is "+status, null, null);
						                  }
						                  
						              }
						          }
							
						}
						
					}else {
						
						return new Response(ERROR, COMMON_ERROR_CODE, "Please first send payment link request then validate", null, null);
					}
				}
				
				
			}else {
				
				return new Response(ERROR, COMMON_ERROR_CODE, "Please configure CCAvenue payment gateway and API URL", null, reqDto);
			}
			
		}else {
			
			return new Response(ERROR, COMMON_ERROR_CODE, "Please configure unit ", null, reqDto);
		}
		
		
	}catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, reqDto);
	}
	return null;
}



@RequestMapping(value="/validatePayment/callBackUrl", method=RequestMethod.POST)
public String confirmPayment(@RequestBody String resStatus) {
	try {
		

        Map hm=new CCAvenueService().tokenizeToHashMap(resStatus, "&", "=");
        String encResXML = hm.containsKey("enc_response")?hm.get("enc_response").toString():"";
        String vStatus = hm.containsKey("status")?hm.get("status").toString():"";
        String vError_code = hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
     
        if(vStatus.equals("1")){//If Api call failed
      	  
        	logger.info("enc_response : "+ encResXML);
        	logger.error("error_code : "+ vError_code);
            return null;
            
        }
        if(!encResXML.equals("")) {
      	  
            AesUtil aesUtil=new AesUtil("AVIS57IK34AY45SIYA");
            String decXML = aesUtil.decrypt(encResXML);
            logger.info("enc_response : "+decXML);  
            
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(decXML));
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(decXML));
            Document doc = db.parse(is);
            NodeList ordeAm = doc.getElementsByTagName("order_amt");
            Element ordeAmLi = (Element) ordeAm.item(0);
            String padAmount =  getCharacterDataFromElement(ordeAmLi);
            
            NodeList order_status = doc.getElementsByTagName("order_status");
            Element orderstatus = (Element) order_status.item(0);
            String status =  getCharacterDataFromElement(orderstatus);
            
            NodeList order_No = doc.getElementsByTagName("order_no");
            Element orderNo = (Element) order_No.item(0);
            String orderNumber =  getCharacterDataFromElement(orderNo);
            
            CCAvenueLogDto dto = new CCAvenueLogDto();
            
        }
    
	return null;


	} catch (Exception e) {
		logger.error("PAYMENT_RECONCILIATION_GAP: exception in the CCAvenue callback webhook (/validatePayment/callBackUrl) -- this payment's status may not have been processed; manual reconciliation may be needed. Response behavior intentionally left unchanged pending verification of CCAvenue's webhook/retry contract (see docs/BUSINESS_RULE_MANIFEST.md BR-012). " + e.getMessage(), e);
		return null;
	}
}



public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
       CharacterData cd = (CharacterData) child;
       return cd.getData();
    }
    return "?";
  }

 
}

