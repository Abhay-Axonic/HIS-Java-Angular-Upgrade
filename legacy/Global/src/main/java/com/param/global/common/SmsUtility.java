package com.param.global.common;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dto.BulkMessageQueueDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.MailDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.mail.IMailingService;
import com.param.global.model.MessageQueue;
import com.param.global.service.IBulkMessageQueueService;
import com.param.global.service.IMessageQueueService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.unit.dto.BulkEmailDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.IwhatsAppServiceImpl;

@Service
@SuppressWarnings({"unused","rawtypes"})
public class SmsUtility {

	final static Logger logger = Logger.getLogger(SmsUtility.class);

	@Autowired
	private  RestTemplate restTemplate;

	private static final String APK_LINK = "https://drive.google.com/open?id=1EFzTut97vwxD9p_P6dZOwpq71oEBu_7G";
	public static Integer sendMail = 1;
	public static Integer sendWhatsApp = 2;
	public static Integer PATHOLOGY_REPORT = 30;
	public static Integer USER_DEFINED_COTE = 15;

	public static String USER_TOKEN_MYLIFE = "ClinivantageAdminUser";
	@Autowired
	private Gson gson;

	String countrycode = "254";

	@Autowired
	IMailingService iMailingService;

	@Autowired
	IwhatsAppServiceImpl iwhatsAppServiceImpl;

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	IMessageQueueService iMessageQueueService;

	@Autowired
	IBulkMessageQueueService iBulkMessageQueueService;

	@Autowired
	IEncounterMasterService iEncounterMasterService;



	// Book Appointment
	@Async
	public  void sendAppointmentConfirmationSms(SlotMasterDto appointmentConfirmationSMSDto) {
		try{
			 new Thread(new Runnable() {
			 public void run() {
				 try {
					 String user = "clinivantage";
					 String password = "1234562018";
					 //String sender = "SHIVAM";
//					 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"CHTHOS";
					 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
					 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
					 String message = "Hello " + appointmentConfirmationSMSDto.getFirstName() + ", Your appointment for "+appointmentConfirmationSMSDto.getDoctorName()+" has been confirmed.\nAppointment DateTime: "+appointmentConfirmationSMSDto.getAppointmentDate2()+" "+appointmentConfirmationSMSDto.getFromTimeString();
					 message =  URLEncoder.encode(message, "UTF-8");

					 String first= sender.replace("@message", message);
					 String second= first.replace("@mobileNumber", mobileNumber);
					 URI uri = URI.create(second);
					String smsJson = restTemplate.getForObject(uri, String.class);
					 //System.out.println(smsJson);

				 } catch (Exception e) {
				 	logger.error("sendAppointmentConfirmationSms failed", e);
				 	throw new ServiceException(e.getMessage());
				 }
			 }
			 }).start();
		 }catch(Exception e){
		 	logger.error("sendAppointmentConfirmationSms failed", e);
		 	throw new ServiceException(e.getMessage());
		 }
	}

	// Cancel Appointment
	@Async
	public  void sendAppointmentCancelationSms(SlotMasterDto appointmentConfirmationSMSDto) {
		try{
			 new Thread(new Runnable() {
			 public void run() {
				 try {
					 String user = "clinivantage";
					 String password = "1234562018";
//					 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"CHTHOS";
					 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";

					 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
					 String message = "Hello " + appointmentConfirmationSMSDto.getFirstName() + ", Your appointment for "+appointmentConfirmationSMSDto.getDoctorName()+" has been Cancelled.\nAppointment DateTime: "+appointmentConfirmationSMSDto.getAppointmentDate2()+" "+appointmentConfirmationSMSDto.getFromTimeString();
					 message =  URLEncoder.encode(message, "UTF-8");
					 String first= sender.replace("@message", message);
					 String second= first.replace("@mobileNumber", mobileNumber);
					 URI uri = URI.create(second);
					 String smsJson = restTemplate.getForObject(uri, String.class);
					 //System.out.println(smsJson);

				 } catch (Exception e) {
				 	logger.error("sendAppointmentCancelationSms failed", e);
				 	throw new ServiceException(e.getMessage());
				 }
			 }
			 }).start();
		 }catch(Exception e){
		 	logger.error("sendAppointmentCancelationSms failed", e);
		 	throw new ServiceException(e.getMessage());
		 }
	}

	// Reschedul Appointment
	@Async
	public void rescheduledAppointmentSms(SlotMasterDto appointmentConfirmationSMSDto) {
		try{
			 new Thread(new Runnable() {
			 public void run() {
				 try {
					 String user = "clinivantage";
					 String password = "1234562018";
//					 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"CHTHOS";
					 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
					 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
					 String message = "Hello " + appointmentConfirmationSMSDto.getFirstName() + ", Your appointment for "+appointmentConfirmationSMSDto.getDoctorName()+" has been Rescheduled.\nAppointment DateTime: "+appointmentConfirmationSMSDto.getAppointmentDate2()+" "+appointmentConfirmationSMSDto.getFromTimeString();
					 message =  URLEncoder.encode(message, "UTF-8");
					 String first= sender.replace("@message", message);
					 String second= first.replace("@mobileNumber", mobileNumber);
					 URI uri = URI.create(second);
					 String smsJson = restTemplate.getForObject(uri, String.class);
					 //System.out.println(smsJson);

				 } catch (Exception e) {
				 	logger.error("rescheduledAppointmentSms failed", e);
				 	throw new ServiceException(e.getMessage());
				 }
			 }
			 }).start();
		 }catch(Exception e){
		 	logger.error("rescheduledAppointmentSms failed", e);
		 	throw new ServiceException(e.getMessage());
		 }
	}

	// Patient Registration
		@Async
		public void patientRegistrationSms(PatientRegistrationDto appointmentConfirmationSMSDto) {
			try{
				 new Thread(new Runnable() {
				 public void run() {
					 try {
						 String user = "clinivantage";
						 String password = "1234562018";
						// String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId() :"SHIVAM";
//						 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"CHTHOS";
						 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
						 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
						 String message = "Hello " + appointmentConfirmationSMSDto.getFirstName() + ", You are successfully registered with UHID:"+appointmentConfirmationSMSDto.getUhidNumber();
						 message =  URLEncoder.encode(message, "UTF-8");
						 String first= sender.replace("@message", message);
						 String second= first.replace("@mobileNumber", mobileNumber);
						 URI uri = URI.create(second);
						 String smsJson = restTemplate.getForObject(uri, String.class);
						 //System.out.println(smsJson);

					 } catch (Exception e) {
					 	logger.error("patientRegistrationSms failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				 }
				 }).start();
			 }catch(Exception e){
			 	logger.error("patientRegistrationSms failed", e);
			 	throw new ServiceException(e.getMessage());
			 }
		}



		// Patient Admission Registration
				@Async
				public void patientAdmissionRegistrationSms(SlotMasterDto appointmentConfirmationSMSDto) {
					try{
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String user = "clinivantage";
								 String password = "1234562018";
//								 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"CHTHOS";
								 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
								 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
								 String message = "Hello " + appointmentConfirmationSMSDto.getFirstName() + ", Your registration successfully completed.";
								 message =  URLEncoder.encode(message, "UTF-8");

								 String first= sender.replace("@message", message);
								 String second= first.replace("@mobileNumber", mobileNumber);
								 URI uri = URI.create(second);
								 String smsJson = restTemplate.getForObject(uri, String.class);
								 //System.out.println(smsJson);


							 } catch (Exception e) {
							 	logger.error("patientAdmissionRegistrationSms failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
					 }catch(Exception e){
					 	logger.error("patientAdmissionRegistrationSms failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}
				// Patient Admission Registration
				@Async
				public void sendOTP(SlotMasterDto appointmentConfirmationSMSDto) {
					try{
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String sender = appointmentConfirmationSMSDto.getSender() != null ? appointmentConfirmationSMSDto.getSender().trim() :"CHTHOS";
								 String mobileNumber = appointmentConfirmationSMSDto.getMobileNumber() != null ? appointmentConfirmationSMSDto.getMobileNumber().trim() : "";
								 String message = "  Your one time password(OTP) for transaction of loyality card is "+appointmentConfirmationSMSDto.getOpt() +" on "+appointmentConfirmationSMSDto.getMobileNumber() ;
								 message =  URLEncoder.encode(message, "UTF-8");

								 String first= sender.replace("@message", message);
								 String second= first.replace("@mobileNumber", mobileNumber);
								 URI uri = URI.create(second);
								 String smsJson = restTemplate.getForObject(uri, String.class);
								 //System.out.println(smsJson);

							 } catch (Exception e) {
							 	logger.error("sendOTP failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
					 }catch(Exception e){
					 	logger.error("sendOTP failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}

				@Async
				public void sendSmsConfig(List<SmsUtilityConfigInfoDto> smsInfoList) {
					try{
						for(SmsUtilityConfigInfoDto smsInfo:smsInfoList) {
						 new Thread(new Runnable() {
						 public void run() {
							 try {

								 String sender=smsInfo.getSenderId();
								 String mobileNumber = smsInfo.getMobileNo() != null ? smsInfo.getMobileNo().trim() :null;
								 SmsUtilityConfigInfoDto response = createMessageByReplaycingData(smsInfo);
								 String message="";
								 String whatsaAppmessage = "";
								 String whatsaAppmessagehel = "";

								 whatsaAppmessage =  response.getMessage();
								 message =  response.getMessage();
								 whatsaAppmessagehel = response.getWhatsaAppmessagehel();

								 if(smsInfo.getCategoryId() == USER_DEFINED_COTE) {
									 List<String> elephantList = Arrays.asList(smsInfo.getUserDefinedMail().split(","));

									 for (String UserMail : elephantList) {

										 MessageQueueDto dto = new MessageQueueDto();
										 dto.setTextMessageContent(message);
										 dto.setMobileNumber(UserMail);
										 dto.setCategoryId(smsInfo.getCategoryId());
										 dto.setCategoryName(smsInfo.getCategoryName());
										 dto.setOrganizationId(smsInfo.getOrganizationId());
										 dto.setTriggerId(smsInfo.getTriggerId());
										 dto.setUniqueId(smsInfo.getUniqueId());
										 dto.setUnitId(smsInfo.getUnitId());
										 dto.setUserId(smsInfo.getUserId());
										 dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
										 if(smsInfo.getSkipSaveMessageQueue() == 1) {
											 iMessageQueueService.addTextSMSMessageInQueue(dto);
										 }else {
											 iMessageQueueService.addTextSMSMessageInMessageLogText(dto);
										 }

									}

								 }else {

									 if(mobileNumber!= null && mobileNumber != "" && !mobileNumber.isEmpty() && mobileNumber.length()>8) {
											if(smsInfo.getIsSMSSend()!=null && smsInfo.getIsSMSSend() =='Y') {
									 MessageQueueDto dto = new MessageQueueDto();
									 dto.setTextMessageContent(message);
									 dto.setMobileNumber(mobileNumber);
									 dto.setCategoryId(smsInfo.getCategoryId());
									 dto.setCategoryName(smsInfo.getCategoryName());
									 dto.setOrganizationId(smsInfo.getOrganizationId());
									 dto.setTriggerId(smsInfo.getTriggerId());
									 dto.setUniqueId(smsInfo.getUniqueId());
									 dto.setUnitId(smsInfo.getUnitId());
									 dto.setUserId(smsInfo.getUserId());
									 dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
									 dto.setSenderId(smsInfo.getSenderId());
									 if(smsInfo.getSkipSaveMessageQueue() == 1) {
									 iMessageQueueService.addTextSMSMessageInQueue(dto);
									 }else {
										 iMessageQueueService.addTextSMSMessageInMessageLogText(dto);
									 }
								}}

								 }

								 if(smsInfo.getToMobileNumber() != null && smsInfo.getToMobileNumber() != "") {
											if(smsInfo.getIsWhatsappSend()!=null && smsInfo.getIsWhatsappSend()=='Y') {

												if(smsInfo.getTriggerId()!=null && smsInfo.getTriggerId().equals(ISmsUtilityConstants.SUBSCRIPTION_REMINDER))
												{
													MessageQueueDto dto = new MessageQueueDto();
													dto.setCategoryId(smsInfo.getCategoryId());
													dto.setCategoryName(smsInfo.getCategoryName());
													if( smsInfo.getWhatsAppOption() ==3) {
														dto.setMailContent(whatsaAppmessagehel);
													}else {
														dto.setMailContent(whatsaAppmessage);
													}
													dto.setToWhatsappNumber(smsInfo.getToMobileNumber());
													dto.setOrganizationId(smsInfo.getOrganizationId());
													dto.setTriggerId(smsInfo.getTriggerId());
													dto.setUniqueId(smsInfo.getUniqueId());
													dto.setUnitId(smsInfo.getUnitId());
													dto.setUserId(smsInfo.getUserId());
													dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
													dto.setReportString(smsInfo.getReportString());
													dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
													dto.setWhatsAppTemplateName(smsInfo.getWhatsAppTemplateName() != null ? smsInfo.getWhatsAppTemplateName() : null);
													dto.setMailFileName(smsInfo.getRptFileName() != null && !smsInfo.getRptFileName().isEmpty() ? smsInfo.getRptFileName() : null);
													dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
													dto.setWhatsAppTemplateName(smsInfo.getWhatsAppTemplateName() != null ? smsInfo.getWhatsAppTemplateName() : null);
													if(smsInfo.getSkipSaveMessageQueue() == 1) {
														iMessageQueueService.addWhatsAppMessageInQueue(dto);
													}else {
														iMessageQueueService.addTextWhatsAppMessageInMessageLog(dto);
													}
												}
												else{
													Response res = iMessageQueueService.checkAlreadyExitsOrNot(smsInfo.getUniqueId(),sendWhatsApp,PATHOLOGY_REPORT);
													if(res.getListObject() == null) {
														MessageQueueDto dto = new MessageQueueDto();
														dto.setCategoryId(smsInfo.getCategoryId());
														dto.setCategoryName(smsInfo.getCategoryName());
														if( smsInfo.getWhatsAppOption() ==3) {
															dto.setMailContent(whatsaAppmessagehel);
														}else {
															dto.setMailContent(whatsaAppmessage);
														}
														dto.setToWhatsappNumber(smsInfo.getToMobileNumber());
														dto.setOrganizationId(smsInfo.getOrganizationId());
														dto.setTriggerId(smsInfo.getTriggerId());
														dto.setUniqueId(smsInfo.getUniqueId());
														dto.setUnitId(smsInfo.getUnitId());
														dto.setUserId(smsInfo.getUserId());
														dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
														dto.setReportString(smsInfo.getReportString());
														dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
														dto.setWhatsAppTemplateName(smsInfo.getWhatsAppTemplateName() != null ? smsInfo.getWhatsAppTemplateName() : null);
														dto.setMailFileName(smsInfo.getRptFileName() != null && !smsInfo.getRptFileName().isEmpty() ? smsInfo.getRptFileName() : null);
														dto.setWhatsappDocumentUrl(smsInfo.getReportMsgUrl() != null && !smsInfo.getReportMsgUrl().isEmpty() ? smsInfo.getReportMsgUrl() : smsInfo.getLabReportUrl());
														if(smsInfo.getSkipSaveMessageQueue() == 1) {
															iMessageQueueService.addWhatsAppMessageInQueue(dto);
														}else {
															iMessageQueueService.addTextWhatsAppMessageInMessageLog(dto);
														}
													}
												}
											}

								 }

							 } catch (Exception e) {
							 	logger.error("sendSmsConfig failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
						}
					 }catch(Exception e){
					 	logger.error("sendSmsConfig failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}

				protected SmsUtilityConfigInfoDto createMessageByReplaycingData(SmsUtilityConfigInfoDto smsInfo) {


					String message=smsInfo.getSmsTemplateMessage();
					 String whatsaAppmessage = smsInfo.getSmsTemplateMessage();
					 String whatsaAppmessagehel = smsInfo.getSmsTemplateMessage();

					 if(message.contains("@hospitalName"))
						 message=message.replace("@hospitalName",smsInfo.getHospitalName()!=null ? smsInfo.getHospitalName():"");
					     String hospitalName = smsInfo.getHospitalName()!=null ? smsInfo.getHospitalName():"";
				         whatsaAppmessagehel=whatsaAppmessagehel.replace("@hospitalName",'"'+hospitalName+'"');

					 if(message.contains("@patient"))
						 message=message.replace("@patient",smsInfo.getPatientName()!=null ? smsInfo.getPatientName():"");
					     String name = smsInfo.getPatientName()!=null ? smsInfo.getPatientName():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@patient",'"'+name+'"');

					 if(message.contains("@FeedbackForm"))
						 message=message.replace("@FeedbackForm",smsInfo.getCriticalUrl()!=null ? smsInfo.getCriticalUrl():"");
					     String feedbackForm = smsInfo.getCriticalUrl()!=null ? smsInfo.getCriticalUrl():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@FeedbackForm",'"'+feedbackForm+'"');

					 if(message.contains("@uhid"))
						 message=message.replace("@uhid",smsInfo.getPatientUhidNo()!=null ? smsInfo.getPatientUhidNo():"");
					     String uhid = smsInfo.getPatientUhidNo()!=null ? smsInfo.getPatientUhidNo():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@uhid",'"'+uhid+'"');

					if(message.contains("@toNumber"))
							 message=message.replace("@toNumber",smsInfo.getToMobileNumber()!=null ? smsInfo.getToMobileNumber():"");
						     String number = smsInfo.getToMobileNumber()!=null ? smsInfo.getToMobileNumber():"";
						     whatsaAppmessagehel=whatsaAppmessagehel.replace("@toNumber",'"'+number+'"');

					 if(message.contains("@treatingDoc"))
						 message=message.replace("@treatingDoc", smsInfo.getTreatingDoctorName()!=null ? smsInfo.getTreatingDoctorName():"");
					     String tretDoc = smsInfo.getTreatingDoctorName();
				         whatsaAppmessagehel=whatsaAppmessagehel.replace("@treatingDoc",'"'+tretDoc+'"');

					 if(message.contains("@admittingDoc"))
						 message=message.replace("@admittingDoc",smsInfo.getAdmitingDoctorName()!=null ? smsInfo.getAdmitingDoctorName():"");
					     String admiDoc = smsInfo.getAdmitingDoctorName()!=null ? smsInfo.getAdmitingDoctorName():"";
			             whatsaAppmessagehel=whatsaAppmessagehel.replace("@admittingDoc",'"'+admiDoc+'"');

					 if(message.contains("@ConsultingDoc"))
						 message=message.replace("@ConsultingDoc", smsInfo.getConsultingDoctorName()!=null ? smsInfo.getConsultingDoctorName():"");
					    String consDoc = smsInfo.getConsultingDoctorName()!=null ? smsInfo.getConsultingDoctorName():"";
		                whatsaAppmessagehel=whatsaAppmessagehel.replace("@ConsultingDoc",'"'+consDoc+'"');

					 if(message.contains("@dateTime"))
						 message=message.replace("@dateTime", smsInfo.getDateTime()!=null ? smsInfo.getDateTime():"");
					     String dateTime = smsInfo.getDateTime()!=null ? smsInfo.getDateTime():"";
		                whatsaAppmessagehel=whatsaAppmessagehel.replace("@dateTime",'"'+dateTime+'"');


					 if(message.contains("@bedDetails"))
						 message=message.replace("@bedDetails",smsInfo.getBedDetails()!=null ? smsInfo.getBedDetails():"");
					     String bedDetails = smsInfo.getBedDetails()!=null ? smsInfo.getBedDetails():"";
		                 whatsaAppmessagehel=whatsaAppmessagehel.replace("@bedDetails",'"'+bedDetails+'"');

					 if(message.contains("@referralDoc"))
						 message=message.replace("@referralDoc",smsInfo.getReferringDoctorName()!=null ? smsInfo.getReferringDoctorName():"");
					    String referralDoc = smsInfo.getReferringDoctorName()!=null ? smsInfo.getReferringDoctorName():"";
		                whatsaAppmessagehel=whatsaAppmessagehel.replace("@referralDoc",'"'+referralDoc+'"');

					 if(message.contains("@billAmount"))
						 message=message.replace("@billAmount",smsInfo.getBillAmount()!=null ? smsInfo.getBillAmount().toString():"");

					 if(message.contains("@selfAmount"))
						 message=message.replace("@selfAmount",smsInfo.getSelfAmount()!=null ? smsInfo.getSelfAmount().toString():"");
					 	 whatsaAppmessagehel=whatsaAppmessagehel.replace("@selfAmount",smsInfo.getSelfAmount()!=null ? smsInfo.getSelfAmount().toString():"");
					 if(message.contains("@creditAmount"))
						 message=message.replace("@creditAmount",smsInfo.getCreditAmount()!=null ? smsInfo.getCreditAmount().toString():"");

					 if(message.contains("@dueAmount"))
						 message=message.replace("@dueAmount",smsInfo.getOutstandingAmount()!=null ? smsInfo.getOutstandingAmount().toString():"");

					 if(message.contains("@depositNumber"))
						 message=message.replace("@depositNumber",smsInfo.getDepositNo()!=null ? smsInfo.getDepositNo().toString():"");

					 if(message.contains("@billNumber"))
						 message=message.replace("@billNumber",smsInfo.getBillNo()!=null ? smsInfo.getBillNo().toString():"");

					 if(message.contains("@recieptNumber"))
						 message=message.replace("@recieptNumber",smsInfo.getRecieptNo()!=null ? smsInfo.getRecieptNo().toString():"");

					 if(message.contains("@admissionNumber"))
						 message=message.replace("@admissionNumber",smsInfo.getAdmissionNo()!=null ? smsInfo.getAdmissionNo().toString():"");

					 if(message.contains("@encounterNumber"))
						 message=message.replace("@encounterNumber",smsInfo.getEncounterNo()!=null ? smsInfo.getEncounterNo().toString():"");
//					 added by dinesh
					 if(message.contains("@companyAdmPatCount"))
						 message=message.replace("@companyAdmPatCount",smsInfo.getCompanyAdmissionPatientCount()!=null ? smsInfo.getCompanyAdmissionPatientCount().toString():"");

					 if(message.contains("@selfAdmPatCount"))
						 message=message.replace("@selfAdmPatCount",smsInfo.getSelfAdmissionPatientCount()!=null ? smsInfo.getSelfAdmissionPatientCount().toString():"");

					 if(message.contains("@selfDisPatCount"))
						 message=message.replace("@selfDisPatCount",smsInfo.getSelfDischargePatientCount()!=null ? smsInfo.getSelfDischargePatientCount().toString():"");

					 if(message.contains("@companyDisPatCount"))
						 message=message.replace("@companyDisPatCount",smsInfo.getCompanyDischargePatientCount()!=null ? smsInfo.getCompanyDischargePatientCount().toString():"");

					 if(message.contains("@opdNewCount"))
						 message=message.replace("@opdNewCount", smsInfo.getOpdNewCount()!=null ? smsInfo.getOpdNewCount().toString():"");

					 if(message.contains("@opdFollowUpCount"))
						 message=message.replace("@opdFollowUpCount",smsInfo.getOpdFollowUpCount()!=null ? smsInfo.getOpdFollowUpCount().toString():"");

					 if(message.contains("@collectionAmt"))
						 message=message.replace("@collectionAmt",smsInfo.getCollectionAmount()!=null ? smsInfo.getCollectionAmount().toString():"");

					 if(message.contains("@refundAmt"))
					 message=message.replace("@refundAmt",smsInfo.getRefundAmount()!=null ? smsInfo.getRefundAmount().toString():"");

					 if(message.contains("@depositAmt"))
						 message=message.replace("@depositAmt",smsInfo.getDepositeAmount()!=null ? smsInfo.getDepositeAmount().toString():"");

					 if(message.contains("@deadCount"))
						 message=message.replace("@deadCount",smsInfo.getDeathCount()!=null ? smsInfo.getDeathCount().toString():"");

					 if(message.contains("@age"))
						 message=message.replace("@age",smsInfo.getAge()!=null ? smsInfo.getAge().toString():"");

					 if(message.contains("@cyclicOrderDate"))
						 message=message.replace("@cyclicOrderDate",smsInfo.getCyclicOrderDate()!=null ? smsInfo.getCyclicOrderDate().toString():"");
					 whatsaAppmessagehel=whatsaAppmessagehel.replace("@cyclicOrderDate",smsInfo.getCyclicOrderDate()!=null ? smsInfo.getCyclicOrderDate().toString():"");

					 if(message.contains("@labReportUrl")) {
						    message=message.replace("@labReportUrl", smsInfo.getLabReportUrl()!=null ? smsInfo.getLabReportUrl():"");
					 }

					 if(message.contains("@testName"))
						 message=message.replace("@testName",smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc():"");
					 whatsaAppmessagehel=whatsaAppmessagehel.replace("@testName",smsInfo.getTestDesc()!=null ? smsInfo.getTestDesc():"");

					 if(message.contains("@srfId"))
						 message=message.replace("@srfId",smsInfo.getSrfId()!=null ? smsInfo.getSrfId():"");

					 if(message.contains("@vaccineName")) {
						 message=message.replace("@vaccineName",smsInfo.getVaccineName()!=null ? smsInfo.getVaccineName():"");
					 }

					 if(message.contains("@siteName")) {
						 message=message.replace("@siteName",smsInfo.getSiteName()!=null ? smsInfo.getSiteName():"");
					 }

					 if(message.contains("@seriesName")) {
						 message=message.replace("@seriesName",smsInfo.getSeriesName()!=null ? smsInfo.getSeriesName():"");
					 }
					 if(message.contains("@videoCallLink")) {
						 message=message.replace("@videoCallLink",smsInfo.getVideoCallLink()!=null ? smsInfo.getVideoCallLink():"");
					 }

					 if(message.contains("@appointmentUniqueId")) {
						 message=message.replace("@appointmentUniqueId",smsInfo.getAppointmentUniqueId()!= null ?smsInfo.getAppointmentUniqueId().toString() : "" );
					 }
					 if(message.contains("@reportUrlLink")) {
						 message=message.replace("@reportUrlLink",smsInfo.getReportUrl()!=null ? smsInfo.getReportUrl():"");
					     String reportLink = smsInfo.getReportUrl()!=null ? smsInfo.getReportUrl():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@reportUrlLink",'"'+reportLink+'"');
					 }

					 if(message.contains("@doctor")) {
						 message=message.replace("@doctor",smsInfo.getAdmitingDoctorName() !=null ? smsInfo.getAdmitingDoctorName():"");
					 }

					 if(message.contains("@userName")) {
						 message=message.replace("@userName",smsInfo.getUserName() != null ? smsInfo.getUserName():"");
					 }

					 if(message.contains("@password")) {
						 message=message.replace("@password",smsInfo.getPassword() != null ? smsInfo.getPassword():"");
					 }

					 if(message.contains("@applicationLink")) {
						 message=message.replace("@applicationLink",smsInfo.getApplicationLink() != null ? smsInfo.getApplicationLink():"");
					 }

					 if(message.contains("@employeeName")) {
						 message=message.replace("@employeeName",(smsInfo.getEmployeeName()!=null ? smsInfo.getEmployeeName() : ""));
					 }

					 if(message.contains("@companyName")) {
						 message=message.replace("@companyName",smsInfo.getCompanyName()!= null ?smsInfo.getCompanyName(): ""  );
					 }

					 if(message.contains("@insuranceName")) {
						 message=message.replace("@insuranceName",smsInfo.getInsuranceName() != null ?smsInfo.getInsuranceName(): "" );
					 }

                     if(message.contains("@mobileNo")) {
						 message=message.replace("@mobileNo",smsInfo.getMobileNo()!= null ? smsInfo.getMobileNo() : " ");
					 }

					 if(message.contains("@gender")) {
						 message=message.replace("@gender",smsInfo.getGender() != null ? smsInfo.getGender():"");
					 }

					 if(message.contains("@admissionDate")) {
						 message=message.replace("@admissionDate",smsInfo.getDateTime() != null ?  smsInfo.getDateTime() : " ");
					 }

					 if(message.contains("@consentName")) {
						 message=message.replace("@consentName",smsInfo.getConsentMasterName()!= null ? smsInfo.getConsentMasterName():"");
					 }

					 if(message.contains("@consentType")) {
						 message=message.replace("@consentType",smsInfo.getConsentTypeMasterName() != null ? smsInfo.getConsentTypeMasterName():"");
					 }

					 if(message.contains("@procedureName")) {
						 message=message.replace("@procedureName",smsInfo.getProceduceName() != null ? smsInfo.getProceduceName() : " ");
					 }

					 if(message.contains("@totalPendingDuaAmount")) {
						 message=message.replace("@totalPendingDuaAmount",smsInfo.getTotalPendingDuaAmount() != null ? smsInfo.getTotalPendingDuaAmount().toString() : "");
						 whatsaAppmessagehel=whatsaAppmessagehel.replace("@totalPendingDuaAmount",smsInfo.getTotalPendingDuaAmount() != null ? smsInfo.getTotalPendingDuaAmount().toString() : "");
					 }

					 if(message.contains("@totalDepositAmount")) {
						 message=message.replace("@totalDepositAmount",smsInfo.getTotalDepositAmount() != null ? smsInfo.getTotalDepositAmount().toString() : " ");
						 whatsaAppmessagehel=whatsaAppmessagehel.replace("@totalDepositAmount",smsInfo.getTotalDepositAmount() != null ? smsInfo.getTotalDepositAmount().toString() : " ");
					 }
                    //new added by avishkar
					 if(message.contains("@visitorPassNumber")) {
						 message=message.replace("@visitorPassNumber",smsInfo.getPassNumber()!=null ? smsInfo.getPassNumber():"");
					     String visitorPassNumber = smsInfo.getPassNumber()!=null ? smsInfo.getPassNumber():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@visitorPassNumber",'"'+visitorPassNumber+'"');
					 }

					 if(message.contains("@proceduceName")) {
						 message=message.replace("@proceduceName",smsInfo.getProceduceName()!=null ? smsInfo.getProceduceName():"");
					     String proceduceName = smsInfo.getProceduceName()!=null ? smsInfo.getProceduceName():"";
					     whatsaAppmessagehel=whatsaAppmessagehel.replace("@proceduceName",'"'+proceduceName+'"');
					 }
					if(message.contains("@subscriptionEndDate")) {
						message=message.replace("@subscriptionEndDate",smsInfo.getSubscriptionEndDate()!=null ? smsInfo.getSubscriptionEndDate():"");
						String subscriptionEndDate = smsInfo.getSubscriptionEndDate()!=null ? smsInfo.getSubscriptionEndDate():"";
						whatsaAppmessagehel=whatsaAppmessagehel.replace("@subscriptionEndDate",'"'+subscriptionEndDate+'"');
					}

					Pattern pattern = Pattern.compile("@\\w+");
					List<String> paramNames = new ArrayList<>();
					Matcher matcher = pattern.matcher(message);
					if(matcher!=null)
					{
						PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
						while (matcher.find())
						{
							paramNames.add(matcher.group());
						}
						if(!paramNames.isEmpty())
						{
							consentMapperDto.setPatientId(smsInfo.getPatientId());
							consentMapperDto.setEncounterId(smsInfo.getEncounterId());
							consentMapperDto.setAdmissionId(smsInfo.getAdmissionId());
							consentMapperDto.setTemplateDesc(message);
							Response res = iEncounterMasterService.appendCensentData(consentMapperDto);
							consentMapperDto = (PatientConsentMapperDto) res.getObject();
							message = consentMapperDto.getTemplateDesc();
						}
					}

					 smsInfo.setMessage(message);
					 smsInfo.setWhatsaAppmessagehel(whatsaAppmessagehel);
					return smsInfo;

				}

				// Patient Admission Registration
				@Async
				public void sendLabReportReleaseMessage(String patientName,String mobileNo,String senderId)
				{
					try{
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String user = "clinivantage";
								 String password = "1234562018";
//								 String sender = appointmentConfirmationSMSDto.getSenderId() != null ? appointmentConfirmationSMSDto.getSenderId().trim() :"CHTHOS";
								 String sender = senderId != null ? senderId.trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
								 String mobileNumber = mobileNo != null ? mobileNo.trim() : "";
								 String message = " " + patientName + ", your lab report has been released.";
								 message =  URLEncoder.encode(message, "UTF-8");

								 String first= sender.replace("@message", message);
								 String second= first.replace("@mobileNumber", mobileNumber);
								 URI uri = URI.create(second);
								 String smsJson = restTemplate.getForObject(uri, String.class);
								 //System.out.println(smsJson);


							 } catch (Exception e) {
							 	logger.error("sendLabReportReleaseMessage failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
					 }catch(Exception e){
					 	logger.error("sendLabReportReleaseMessage failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}
				@Async
				public void sendRadiologyReportReleaseMessage(String patientName,String mobileNo,String senderId)
				{
					try{
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String user = "clinivantage";
								 String password = "1234562018";
								 String sender = senderId != null ? senderId.trim() :"http://bhashsms.com/api/sendmsg.php?user=clinivantage&pass=1234562018&sender=CHTHOS&phone=@mobileNumber&text=@message&priority=ndnd&stype=normal";
								 String mobileNumber = mobileNo != null ? mobileNo.trim() : "";
								 String message = " " + patientName + ", your radiology report has been released.";
								 message =  URLEncoder.encode(message, "UTF-8");

								 String first= sender.replace("@message", message);
								 String second= first.replace("@mobileNumber", mobileNumber);
								 URI uri = URI.create(second);
								 String smsJson = restTemplate.getForObject(uri, String.class);
								 //System.out.println(smsJson);


							 } catch (Exception e) {
							 	logger.error("sendRadiologyReportReleaseMessage failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
					 }catch(Exception e){
					 	logger.error("sendRadiologyReportReleaseMessage failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}
				public void sendEmalConfig(List<SmsUtilityConfigInfoDto> configInfoList,String fileName,ByteArrayOutputStream report, MailDto mailDto) {
					try{
						for(SmsUtilityConfigInfoDto smsInfo:configInfoList) {
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String message="";
								 SmsUtilityConfigInfoDto response = createMessageByReplaycingData(smsInfo);
								 message=response.getMessage();

								 if((smsInfo.getToEmailId()!=null && smsInfo.getToEmailId()!="" && !smsInfo.getToEmailId().isEmpty())  || (smsInfo.getCategoryId() == USER_DEFINED_COTE)) {

									 if(smsInfo.getTriggerId()!=null && smsInfo.getTriggerId().equals(ISmsUtilityConstants.SUBSCRIPTION_REMINDER))
									 {
										 MessageQueueDto dto = new MessageQueueDto();
										 dto.setCategoryId(smsInfo.getCategoryId());
										 dto.setCategoryName(smsInfo.getCategoryName());
										 dto.setMailContent(message);
										 dto.setMailFileName(fileName);
										 if(smsInfo.getCriticalReport()!= null && smsInfo.getCriticalReport()== 'Y'){
											 String imp = "Important :- " +smsInfo.getMailSubject();
											 dto.setMailSubject(imp);
										 }else {
											 dto.setMailSubject(smsInfo.getMailSubject());
										 }
										 dto.setOrganizationId(smsInfo.getOrganizationId());
										 dto.setReportString(smsInfo.getReportString());
										 dto.setToMailId(smsInfo.getToEmailId());
										 dto.setTriggerId(smsInfo.getTriggerId());
										 dto.setUniqueId(smsInfo.getUniqueId());
										 dto.setUnitId(smsInfo.getUnitId());
										 dto.setUserId(smsInfo.getUserId());
										 dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
										 iMessageQueueService.addMailMessageInQueue(dto);
									 }
									 else{
										 Response res = iMessageQueueService.checkAlreadyExitsOrNot(smsInfo.getUniqueId(),sendMail,PATHOLOGY_REPORT);
										 if(res.getListObject() == null) {

											 if(smsInfo.getCategoryId() == USER_DEFINED_COTE) {
												 List<String> elephantList = Arrays.asList(smsInfo.getUserDefinedMail().split(","));

												 for (String UserMail : elephantList) {

													 MessageQueueDto dto = new MessageQueueDto();
													 dto.setCategoryId(smsInfo.getCategoryId());
													 dto.setCategoryName(smsInfo.getCategoryName());
													 dto.setMailContent(message);
													 dto.setMailFileName(fileName);
													 if(smsInfo.getCriticalReport()!= null && smsInfo.getCriticalReport()== 'Y'){
														 String imp = "Important :- " +smsInfo.getMailSubject();
														 dto.setMailSubject(imp);
													 }else {
														 dto.setMailSubject(smsInfo.getMailSubject());
													 }

													 dto.setOrganizationId(smsInfo.getOrganizationId());
													 dto.setReportString(smsInfo.getReportString());
													 dto.setToMailId(UserMail);
													 dto.setTriggerId(smsInfo.getTriggerId());
													 dto.setUniqueId(smsInfo.getUniqueId());
													 dto.setUnitId(smsInfo.getUnitId());
													 dto.setUserId(smsInfo.getUserId());
													 dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
													 iMessageQueueService.addMailMessageInQueue(dto);
												 }

											 }else {
												 MessageQueueDto dto = new MessageQueueDto();
												 dto.setCategoryId(smsInfo.getCategoryId());
												 dto.setCategoryName(smsInfo.getCategoryName());
												 dto.setMailContent(message);
												 dto.setMailFileName(fileName);
												 if(smsInfo.getCriticalReport()!= null && smsInfo.getCriticalReport()== 'Y'){
													 String imp = "Important :- " +smsInfo.getMailSubject();
													 dto.setMailSubject(imp);
												 }else {
													 dto.setMailSubject(smsInfo.getMailSubject());
												 }
												 dto.setOrganizationId(smsInfo.getOrganizationId());
												 dto.setReportString(smsInfo.getReportString());
												 dto.setToMailId(smsInfo.getToEmailId());
												 dto.setTriggerId(smsInfo.getTriggerId());
												 dto.setUniqueId(smsInfo.getUniqueId());
												 dto.setUnitId(smsInfo.getUnitId());
												 dto.setUserId(smsInfo.getUserId());
												 dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
												 iMessageQueueService.addMailMessageInQueue(dto);

											 }

										 }
									 }

								 }


								 if(smsInfo.getToMobileNumber() != null && smsInfo.getToMobileNumber() != "" && !smsInfo.getToMobileNumber().isEmpty() && smsInfo.getToMobileNumber().length()>8) {
											if(smsInfo.getIsWhatsappSend()!=null && smsInfo.getIsWhatsappSend()=='Y') {

												if(smsInfo.getTriggerId()!=null && smsInfo.getTriggerId().equals(ISmsUtilityConstants.SUBSCRIPTION_REMINDER))
												{
													MessageQueueDto dto = new MessageQueueDto();
													dto.setCategoryId(smsInfo.getCategoryId());
													dto.setCategoryName(smsInfo.getCategoryName());
//													 String whmessage =  URLDecoder.decode(message, "UTF-8");
//													 String whmessage = message.replaceAll("<p><em><strong></strong></em><.*?>", "");
													String whmessage = message.replaceAll("<p>", "");
													String whmessage1 = whmessage.replaceAll("<em>", "");
													String whmessage2 = whmessage1.replaceAll("</em>", "");
													String whmessage3 = whmessage2.replaceAll("<strong>", "");
													String whmessage4 = whmessage3.replaceAll("</strong>", "");
													String whmessage5 = whmessage4.replaceAll("</p>", "");
													if(smsInfo.getLabReportUrl() != null) {
														String  msg = whmessage5 + "  " +smsInfo.getLabReportUrl();
														dto.setMailContent(msg);
													}else {
														dto.setMailContent(whmessage5);
													}
													dto.setMailFileName(fileName);
													dto.setToWhatsappNumber(smsInfo.getToMobileNumber());
													dto.setOrganizationId(smsInfo.getOrganizationId());
													dto.setReportString(smsInfo.getReportString());
													dto.setTriggerId(smsInfo.getTriggerId());
													dto.setUniqueId(smsInfo.getUniqueId());
													dto.setUnitId(smsInfo.getUnitId());
													dto.setUserId(smsInfo.getUserId());
													dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
													dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
													dto.setWhatsAppTemplateName(smsInfo.getWhatsAppTemplateName() != null ? smsInfo.getWhatsAppTemplateName() : null);
													iMessageQueueService.addWhatsAppMessageInQueue(dto);
												}else{
													Response res = iMessageQueueService.checkAlreadyExitsOrNot(smsInfo.getUniqueId(),sendWhatsApp,PATHOLOGY_REPORT);
													if(res.getListObject() == null) {
														MessageQueueDto dto = new MessageQueueDto();
														dto.setCategoryId(smsInfo.getCategoryId());
														dto.setCategoryName(smsInfo.getCategoryName());
//													 String whmessage =  URLDecoder.decode(message, "UTF-8");
//													 String whmessage = message.replaceAll("<p><em><strong></strong></em><.*?>", "");
														String whmessage = message.replaceAll("<p>", "");
														String whmessage1 = whmessage.replaceAll("<em>", "");
														String whmessage2 = whmessage1.replaceAll("</em>", "");
														String whmessage3 = whmessage2.replaceAll("<strong>", "");
														String whmessage4 = whmessage3.replaceAll("</strong>", "");
														String whmessage5 = whmessage4.replaceAll("</p>", "");
														if(smsInfo.getLabReportUrl() != null) {

															String  msg = whmessage5 + "  " +smsInfo.getLabReportUrl();

															dto.setMailContent(msg);
														}else {
															dto.setMailContent(whmessage5);
														}

														dto.setMailFileName(fileName);
														dto.setToWhatsappNumber(smsInfo.getToMobileNumber());
														dto.setOrganizationId(smsInfo.getOrganizationId());
														dto.setReportString(smsInfo.getReportString());
														dto.setTriggerId(smsInfo.getTriggerId());
														dto.setUniqueId(smsInfo.getUniqueId());
														dto.setUnitId(smsInfo.getUnitId());
														dto.setUserId(smsInfo.getUserId());
														dto.setCyclicOrderDate(smsInfo.getCyclicOrderDate());
														dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
														dto.setWhatsAppTemplateName(smsInfo.getWhatsAppTemplateName() != null ? smsInfo.getWhatsAppTemplateName() : null);
														dto.setWhatsappDocumentUrl(smsInfo.getReportMsgUrl() != null && !smsInfo.getReportMsgUrl().isEmpty() ? smsInfo.getReportMsgUrl() : smsInfo.getLabReportUrl());
														iMessageQueueService.addWhatsAppMessageInQueue(dto);
													}

												}

											}
								 }
							 } catch (Exception e) {
							 	logger.error("sendEmalConfig failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
						}
					 }catch(Exception e){
					 	logger.error("sendEmalConfig failed", e);
					 	throw new ServiceException(e.getMessage());
					 }

				}

				public void sendBulkEmailConfig(List<SmsUtilityConfigInfoDto> configInfoList,
						SmsUtilityConfigInfoDto emailInfo, MailDto mailDto) {
					try{
						for(SmsUtilityConfigInfoDto smsInfo:configInfoList) {
						 new Thread(new Runnable() {
						 public void run() {
							 try {
								 String message=smsInfo.getSmsTemplateMessage();

								 if(message.contains("@hospitalName") && smsInfo.getHospitalName()!=null)
									 message=message.replace("@hospitalName",smsInfo.getHospitalName());

								 if(message.contains("@patient") && smsInfo.getPatientName()!=null)
									 message=message.replace("@patient",smsInfo.getPatientName());

								 if(message.contains("@uhid") && smsInfo.getPatientUhidNo()!=null)
									 message=message.replace("@uhid",smsInfo.getPatientUhidNo());

								 if(message.contains("@dateTime") && smsInfo.getDateTime()!=null)
									 message=message.replace("@dateTime", smsInfo.getDateTime());

								 if(message.contains("@testName") && smsInfo.getTestDesc()!=null)
									 message=message.replace("@testName",smsInfo.getTestDesc());

								 if(message.contains("@doctor") && smsInfo.getAdmitingDoctorName()!=null) {
									 message=message.replace("@doctor",smsInfo.getAdmitingDoctorName());
								 }

								 if(smsInfo.getCompanyName()!=null && message.contains("@companyName")) {
									 message=message.replace("@companyName",smsInfo.getCompanyName());
								 }

								 if(smsInfo.getInsuranceName()!=null && message.contains("@insuranceName")) {
									 message=message.replace("@insuranceName",smsInfo.getInsuranceName());
								 }
								 if(smsInfo.getReportUrl()!=null && message.contains("@reportUrlLink")) {
									 message=message.replace("@reportUrlLink",smsInfo.getReportUrl());
								 }

								 if(emailInfo.getPatientList()!=null && emailInfo.getPatientList().size()>0) {

									 message = message  +"<style>"
											 +"  table {margin: auto;border-collapse: separate;border-spacing: 0;}"
											 +"  th {padding: 5px;border: 1px solid #ddd; vertical-align: top;vertical-align: middle !important; background-color: #2e6ea2; /*  #F5F5A6 */color: #FFFFFF;}"
											 +"  td {padding: 5px; border: 1px solid #ddd; vertical-align: top; vertical-align: middle !important;}"
											 +" thead th { color: #fff; position: -webkit-sticky; position: sticky;top: 0;}"
											 +"</style>"
											 +"<table  align='center' border='1' cellpadding='1' cellspacing='1' style='width:1000px'>"
											 +"	<thead>"
											 +"		<tr>"
											 +"			<th><strong>Patient Name</strong></th>"
											 +"			<th><strong>UHID No.</strong></th>"
											 +"			<th><strong>Date</strong></th>"
											 +"			<th><strong>Test Name</strong></th>"
											 +"		</tr>"
											 +"		</thead>";

											 for (BulkEmailDto emailDto : emailInfo.getPatientList()) {
												 message = message + "<tbody><tr><td>"+emailDto.getPatientDetails()+"</td>";
												 message = message + "<td>"+emailDto.getUhid()+"</td>";
												 String pattern = "yyyy-MM-dd";
												 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
												 String date = simpleDateFormat.format(new Date(emailDto.getOrderDate()));
												 message = message + "<td>"+date+"</td>";
												 message = message + "<td>"+emailDto.getTestDesc()+"</td>";
											}

											 message = message + "</tbody></table>";
											 message = message + "<br><br><br><p><b>Thanks &amp; Best Regards,</b></p>"+smsInfo.getHospitalName()+".";


								 }
								 if(smsInfo.getToEmailId()!=null && smsInfo.getToEmailId()!="" && !smsInfo.getToEmailId().isEmpty()) {
									// iMailingService.triggerBulkEmail(message,smsInfo.getToEmailId(),smsInfo.getMailSubject(),emailInfo,mailDto);

									 List<BulkEmailDto> bulkList = new ArrayList<BulkEmailDto>();

									 for (BulkEmailDto emailDto : emailInfo.getPatientList()) {
										 Response res = iBulkMessageQueueService.checkAlreadyExitOrNot(emailDto.getUniqueId());

										 if(res.getListObject() != null && res.getListObject().size()>0 ) {

										 }else {
											 bulkList.add(emailDto);
										 }
									 }

									 if(bulkList != null && bulkList.size()>0) {

										 MessageQueueDto dto = new MessageQueueDto();
										 dto.setCategoryId(smsInfo.getCategoryId());
										 dto.setCategoryName(smsInfo.getCategoryName());
										 dto.setOrganizationId(smsInfo.getOrganizationId());
										 dto.setToMailId(smsInfo.getToEmailId());
										 dto.setMailSubject(smsInfo.getMailSubject());
										 dto.setMailContent(message);
										 dto.setTriggerId(smsInfo.getTriggerId());
										 dto.setUnitId(smsInfo.getUnitId());
										 dto.setUserId(smsInfo.getUserId());
										 Response res = iMessageQueueService.addMailMessageInQueue(dto);

										 if(res.getObject() != null ) {
											 MessageQueue mstr  = (MessageQueue) res.getObject();
											 for (BulkEmailDto emailDto : emailInfo.getPatientList()) {

												 BulkMessageQueueDto bulkDto = new BulkMessageQueueDto();
												 bulkDto.setMailContent(message);
												 bulkDto.setMailFileName(emailDto.getAttachmentFileName());
												 bulkDto.setMailSubject(smsInfo.getMailSubject());
												 bulkDto.setOrganizationId(smsInfo.getOrganizationId());
												 bulkDto.setReportString(emailDto.getReportString());
												 bulkDto.setUniqueId(emailDto.getUniqueId());
												 bulkDto.setUnitId(smsInfo.getUnitId());
												 bulkDto.setPatientId(emailDto.getPatientId());
												 bulkDto.setMessageQueueId(mstr.getId());
												 iBulkMessageQueueService.addBulkMailMessageInQueue(bulkDto);

											}


										 }

									 }




								 }
							 } catch (Exception e) {
							 	logger.error("sendBulkEmailConfig failed", e);
							 	throw new ServiceException(e.getMessage());
							 }
						 }
						 }).start();
						}
					 }catch(Exception e){
					 	logger.error("sendBulkEmailConfig failed", e);
					 	throw new ServiceException(e.getMessage());
					 }
				}

				public void getSmsUtilityByTriggerIdForMinStockQty(MessageQueueDto dto) {
					iMessageQueueService.addMailMessageInQueue(dto);

				}
				public static String generateJWTTokenMylife(String url) {

					RestTemplate restTemplate = new RestTemplate();
					String urlWithParams = url + "/generateToken" + "?userName=" + USER_TOKEN_MYLIFE;
					// String urlWithParams =
					// "http://localhost:9091/MyLifeGlobal/api"+"/generateToken"+
					// "?userName=" +
					// uname;

					Map<String, String> mapResponse = restTemplate.getForObject(urlWithParams, Map.class);
					String token = null;
					if (mapResponse != null) {
						token = mapResponse.get("token"); // Adjust key according to the
															// response structure
					}

					return token;
				}


}
