package com.param.global.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Msg91MessageSendNew;
import com.param.global.common.Response;
import com.param.global.dao.IMessageQueueDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.ITemplateTransactionDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AmazonMailAttachement;
import com.param.global.dto.AmazonMailDto;
import com.param.global.dto.BulkMessageQueueDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.mail.IMailingService;
import com.param.global.model.MessageLog;
import com.param.global.unit.dto.BulkEmailDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.whatsAppRequestDto;
import com.param.global.unit.service.IwhatsAppServiceImpl;
import com.sendgrid.Attachments;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;

import in.param.exception.ApplicationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import scala.tools.cmd.Demo;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Service
public class MessageQueueServiceImpl implements IMessageQueueService,ICommonConstants,Serializable{
	final static Logger logger = Logger.getLogger(MessageQueueServiceImpl.class);


	@Autowired
	IMessageQueueDao iMessageQueueDao;
	
	@Autowired 
	IMailingService iMailingService;
	
	@Autowired
	IwhatsAppServiceImpl iwhatsAppServiceImpl;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Autowired
	private  RestTemplate restTemplate;
	
	@Autowired
	IMessageLogService iMessageLogService;
	
	@Autowired 
	IBulkMessageQueueService iBulkMessageQueueService;
	
	@Autowired
	ITemplateTransactionDao iTemplateTransactionDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private IPatientRegistrationDao iPatientRegistrationDao;

	@Override
	public Response addTextSMSMessageInQueue(MessageQueueDto dto) {
		try
		{
			return iMessageQueueDao.addTextSMSMessageInQueue(dto);

		} catch (Exception e)
		{
		logger.error("addTextSMSMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response addMailMessageInQueue(MessageQueueDto dto) {
		try
		{
			return iMessageQueueDao.addMailMessageInQueue(dto);

		} catch (Exception e)
		{
		logger.error("addMailMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response addWhatsAppMessageInQueue(MessageQueueDto dto) {
		try
		{
			return iMessageQueueDao.addWhatsAppMessageInQueue(dto);

		} catch (Exception e)
		{
		logger.error("addWhatsAppMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
public static void main(String[] args) {
        
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }};

                   try {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    } catch (Exception e) {
                    logger.error("main failed", e);
                    }
        
    }


	@Override
	@Transactional
	public Response getMessageFromQueqe(int type) throws ApplicationException {
		try
		{
			List<MessageQueueDto> list =  new ArrayList<MessageQueueDto>();
			Response res =  iMessageQueueDao.getMessageFromQueqe(type);
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				list.addAll(res.getListObject());
				
				for (MessageQueueDto messageQueueDto : list) {
					
					boolean faildLogSaveFlag = false;
					boolean successLogSaveFlag = false;
					String errorMessage = "";
					MessageLog logDto = new MessageLog();
					
					if(messageQueueDto.getMessageType().equals(SendMail)) {
						
						logDto.setToMailId(messageQueueDto.getToMailId());
						ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						
						if (messageQueueDto.getPdfGenerationUrl() != null && messageQueueDto.getMailContent() != null) {
							
							
							sendBulkMail(messageQueueDto);
							
							
							if(messageQueueDto.getTriggerId().equals(BulkTrigger)) {                           // this is for bulk mail  triggeris 40
								
								Integer LogId = null;
								Response LogDetails =  iMessageLogService.getLogDetailsByQuequeId(messageQueueDto.getMessageQueueId());    
								
								if(LogDetails.getObject() !=  null) { 
									
									LogId = (Integer) LogDetails.getObject();
								}
								
								List<BulkEmailDto> bulkmailList  = new ArrayList<>();
								Response bulkRes = iBulkMessageQueueService.getbulkMessageListByMessageQId(messageQueueDto.getMessageQueueId());
								
								if(bulkRes.getListObject() != null && bulkRes.getListObject().size()>0) {
									
									List<BulkMessageQueueDto> bulkList  = new ArrayList<>();
									bulkList.addAll(bulkRes.getListObject());
									Integer bulkMailFaildCount = 0;
									
									List<AmazonMailAttachement> attacheBase64List =new ArrayList<AmazonMailAttachement>(); // AWS AttachemantList
									List<Attachments> attachments = new ArrayList<Attachments>(); // SendGrid Attachement List
									
									for (BulkMessageQueueDto bulkDto : bulkList) {
										
										HttpPost pst = new HttpPost(messageQueueDto.getPdfGenerationUrl());
										pst.addHeader("Content-Type", "application/json");
										pst.setEntity(new StringEntity(bulkDto.getReportString()));
										HttpResponse result = httpClient.execute(pst);
										HttpEntity resEntity = result.getEntity();
										resEntity.writeTo(byteOutPutStream);
										
										if(result.getStatusLine().getStatusCode() == 200 ) {       // RPF file generated successfully 
											
											BulkEmailDto bulkmailDto = new BulkEmailDto();
											bulkmailDto.setAttachmentFileName(bulkDto.getMailFileName());
											bulkmailDto.setByteArrayOutputStream(byteOutPutStream);	
											bulkmailDto.setBulkMessageQueueId(bulkDto.getBulkMessageQueueId());
											bulkmailDto.setRetryAttemps(bulkDto.getRetryAttemps());
											bulkmailDto.setUniqueId(bulkDto.getUniqueId());
											bulkmailList.add(bulkmailDto);

											String b64 = Base64.getMimeEncoder().encodeToString(byteOutPutStream.toByteArray());
											
											if(messageQueueDto.getMailProviderId() ==2) {
												Attachments attacDto = new Attachments();
												attacDto.setContent(b64);
												attacDto.setDisposition("attachment");
												attacDto.setFilename(bulkDto.getMailFileName() != null ? bulkDto.getMailFileName(): "test.pdf");
												attacDto.setType("application/pdf");
												attachments.add(attacDto);
												
											}else {
												
												AmazonMailAttachement attacheBase64 = new  AmazonMailAttachement();
												attacheBase64.setName(bulkDto.getMailFileName() != null ? bulkDto.getMailFileName(): "test.pdf" );
												attacheBase64.setType("base64");
												attacheBase64.setValue(b64);
												attacheBase64List.add(attacheBase64);
											}
											
										 
										}else {                                                  // RPF file Not generated  
											
											bulkMailFaildCount ++;
											errorMessage = RPTFILLISSUE;
											
											if(bulkDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {
												
												  if(LogId == null) {
													  
													  	LogId = saveLogDetails(messageQueueDto,errorMessage,false);
													  	iBulkMessageQueueService.updateBulkMailLogId(bulkDto.getBulkMessageQueueId(),errorMessage,LogId);  // if last retry attemps
													  
												  }else {
													  
													  iBulkMessageQueueService.updateBulkMailLogId(bulkDto.getBulkMessageQueueId(),errorMessage,LogId);   // if last retry attemps
													  
												  }
												
											}else {
												
												iBulkMessageQueueService.updateBulkMailRetryCount(bulkDto.getBulkMessageQueueId(),errorMessage); // minus retry attemps by 1 
											}
										}
										
									}
									
									if((bulkmailList != null && bulkmailList.size()>0) || (attachments != null && attachments.size()>0)) {
										
										String mailStatus = null;
										Boolean mailSend = false;
										String sendRes = "";
										if(messageQueueDto.getMailProviderId() ==2 ) {
											
											Response mailres = iMailingService.sendAutoMail(messageQueueDto,attachments,messageQueueDto.getMailCredential());
											
											if(mailres.getStatus() != null) {
												mailSend = true;
												sendRes = mailres.getCode();
											}else {
												mailStatus = mailres.getMessage();
											}
											
										}else {
											AmazonMailDto amazonDto = new AmazonMailDto();
											amazonDto.setData(messageQueueDto.getMailContent());
											amazonDto.setFrom(messageQueueDto.getSmtpUserId()!=null ? messageQueueDto.getSmtpUserId() : "info@axonichealth.co.in");
											amazonDto.setSubject(messageQueueDto.getMailSubject()!=null ? messageQueueDto.getMailSubject() : "");
											amazonDto.setTo(messageQueueDto.getToMailId());
											amazonDto.setAttachment(attacheBase64List);
											
											
											try {
											
											Gson gson = new Gson();
											HttpHeaders headers = new HttpHeaders();
											headers.set("Content-Type", "application/json");
											org.springframework.http.HttpEntity<AmazonMailDto> request = new org.springframework.http.HttpEntity<>(amazonDto, headers);
											URI url = URI.create("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail");
											String stringResponse = restTemplate.postForObject(url, request, String.class);
											Response commonResponse = gson.fromJson(stringResponse, Response.class);
											
											if(commonResponse.getMessage()!= null && commonResponse.getMessage().equalsIgnoreCase("success")) {
												mailSend = true;
											}
											
											}catch(Exception e) {
											logger.error("getMessageFromQueqe failed (mail send sub-step)", e);
											mailSend = false;
											}
											
											
										}
										
										if(mailSend == false) {
											
											errorMessage = mailStatus != null ? mailStatus : MAILNOTSEND;
											updateBulkMail(messageQueueDto,bulkmailList,LogId,errorMessage,false);
											
											if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {        // if last retry attemps
												
												iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); 
												
											}else {                                           // minus retry attemps by 1 
												iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
											}
											
										}else {                                               // Mail send  successfully
											
											errorMessage = sendRes +" "+ MAILSENT;
											updateBulkMail(messageQueueDto,bulkmailList,LogId,errorMessage,true);
											iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
											
										}
									}else {
										
										if(bulkList.size() == bulkMailFaildCount) {
											
											errorMessage =RPTFILLISSUE;
											updateBulkMail(messageQueueDto,bulkmailList,LogId,errorMessage,false);
											
											if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {        // if last retry attemps
												
												iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); 
												
											}else {                                           // minus retry attemps by 1 
												
												iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
											}
										}
										
									}									
								}
								
								
							}else {
									
								Boolean failedMail = false;
								try {
									String mailStatus = null;
									String sendRes = "";
									boolean reportIsNull = false;
									boolean attachement = false;
									String dob = "";
									
									if (messageQueueDto.getReportString() != null && !messageQueueDto.getReportString().equals("")) {
										
										HttpPost pst = new HttpPost(messageQueueDto.getPdfGenerationUrl());
										pst.addHeader("Content-Type", "application/json");
										pst.setEntity(new StringEntity(messageQueueDto.getReportString()));
										HttpResponse result = httpClient.execute(pst);
										HttpEntity resEntity = result.getEntity();
										resEntity.writeTo(byteOutPutStream);

										Character ispdfPasswordRequire = (Character) sessionFactory.getCurrentSession()
												.createSQLQuery("SELECT ispdf_password_require FROM t_unit_application_billing_configuration " +
														"WHERE unit_id = :unitId AND organization_id = :organizationId")
												.setParameter("unitId", messageQueueDto.getUnitId())
												.setParameter("organizationId", messageQueueDto.getOrganizationId())
												.uniqueResult();

										if(messageQueueDto.getCategoryName().equals("PATIENT") && 'Y' == ispdfPasswordRequire) {
											Response patResponse =iPatientRegistrationDao.getPatientDetailsById(messageQueueDto.getUserId());
											PatientDetailsDto dto=(PatientDetailsDto) patResponse.getObject();
											dob=dto.getDob();
											byte[] pdfData = byteOutPutStream.toByteArray();/* Your PDF data */;
								            // Password to protect the PDF
											System.out.println("dob iss"+dob);
									        byteOutPutStream = passwordProtectPDF(pdfData, dob);
										}
							             
										
										if (result.getStatusLine().getStatusCode() == 200) {
											reportIsNull = true;
											attachement = true;
										}
									} else {
										reportIsNull = true;
									}

									if(reportIsNull) {       // RPF file generated successfully 
										
										
										String b64 = Base64.getEncoder().encodeToString(byteOutPutStream.toByteArray());
									
										List<AmazonMailAttachement> attacheBase64List =new ArrayList<AmazonMailAttachement>();
										List<Attachments> attachments = new ArrayList<Attachments>(); // SendGrid Attachement List
										
										if(attachement) {
											
											if(messageQueueDto.getMailProviderId() ==2) {
												Attachments attacDto = new Attachments();
												attacDto.setContent(b64);
												attacDto.setDisposition("attachment");
												attacDto.setFilename(messageQueueDto.getMailFileName() != null ? messageQueueDto.getMailFileName(): "test.pdf");
												attacDto.setType("application/pdf");
												attachments.add(attacDto);
												
											}else {
											       
												AmazonMailAttachement attacheBase64 = new  AmazonMailAttachement();
												String mailBody =messageQueueDto.getMailContent();
												attacheBase64.setName(messageQueueDto.getMailFileName() != null ? messageQueueDto.getMailFileName(): "test.pdf" );
												attacheBase64.setType("base64");
												attacheBase64.setValue(b64);
												attacheBase64List.add(attacheBase64);
												
											}
											
										}
										Boolean mailSend = false;
										if(messageQueueDto.getMailProviderId() == 2) {
											if(messageQueueDto.getBccEmailId()!=null && !messageQueueDto.getBccEmailId().trim().equals("")) {
												messageQueueDto.setBccEmailId(messageQueueDto.getBccEmailId());
											}else {
												messageQueueDto.setBccEmailId(messageQueueDto.getSmtpUserId());
											}
											
											Response mailres = iMailingService.sendAutoMail(messageQueueDto,attachments,messageQueueDto.getMailCredential());
											
											if(mailres.getStatus() != null) {
												mailSend = true;
												sendRes = mailres.getCode();
											}else {
												mailStatus = mailres.getMessage();
											}
											
											
										}else {
											
											
											AmazonMailDto amazonDto = new AmazonMailDto();
											amazonDto.setData(messageQueueDto.getMailContent());
											amazonDto.setFrom(messageQueueDto.getSmtpUserId()!=null ? messageQueueDto.getSmtpUserId() : "info@axonichealth.co.in");
											amazonDto.setSubject(messageQueueDto.getMailSubject()!=null ? messageQueueDto.getMailSubject() : "");
											amazonDto.setTo(messageQueueDto.getToMailId());
											amazonDto.setAttachment(attacheBase64List);
											if(messageQueueDto.getBccEmailId()!=null && !messageQueueDto.getBccEmailId().trim().equals("")) {
												amazonDto.setBcc(messageQueueDto.getBccEmailId());
											}else {
												amazonDto.setBcc(amazonDto.getFrom());
											}
											
											try {
											
											Gson gson = new Gson();
											HttpHeaders headers = new HttpHeaders();
											headers.set("Content-Type", "application/json");
											org.springframework.http.HttpEntity<AmazonMailDto> request = new org.springframework.http.HttpEntity<>(amazonDto, headers);
											URI url = URI.create("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail");
											String stringResponse = restTemplate.postForObject(url, request, String.class);
											Response commonResponse = gson.fromJson(stringResponse, Response.class);
											
											if(commonResponse.getMessage()!= null && commonResponse.getMessage().equalsIgnoreCase("success")) {
												mailSend = true;
											}
											
											}catch(Exception e) {
											logger.error("getMessageFromQueqe failed (mail send sub-step)", e);
											mailSend = false;
											}
										
										}
										
										
										
										if(mailSend) {
										
											// Mail send  successfully
											
											errorMessage = sendRes +" "+ MAILSENT;
											successLogSaveFlag = true;
											iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
											
											if(messageQueueDto.getTriggerId().equals(MailTrigger)) {
												
												String[] resUnq = messageQueueDto.getUniqueId().split("[,]", 0);
												
											      for(String myStr: resUnq){
											    	  String noSpaceStr = myStr.replaceAll("\\s", ""); // using built in method 
											    	  iTemplateTransactionDao.updateLabSampleDetailsMailStatus(Integer.parseInt(noSpaceStr));
											      }
												
											}
										
											
										}else {  
											
											errorMessage = mailStatus != null ? mailStatus : MAILNOTSEND;
											
											if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {        // if last retry attemps
												
												iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); 
												faildLogSaveFlag = true;
												
											}else {                                           // minus retry attemps by 1 
												
												iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
											}
										}
										
									}else {                                                  // RPF file Not generated  
										
										errorMessage =RPTFILLISSUE;
										
										if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) ||  messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {
											
											iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); // if last retry attemps
											faildLogSaveFlag = true;
											
										}else {
											
											iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage); // minus retry attemps by 1 
										}
									}
									
								}catch(Exception e) {
								logger.error("getMessageFromQueqe failed (bulk mail sub-step)", e);
								failedMail = true;
								}
								
								if(failedMail) {
									 faildLogSaveFlag =  updateQueueWhenException(messageQueueDto,errorMessage,1);
									//iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage); // minus retry attemps by 1 
								}
							}
							
							
						}else {                                                  // Pdf Generation Url not found 
							
							errorMessage =PDFURLISSUE;
							if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {
								
								iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); // if last retry attemps
								faildLogSaveFlag = true;
								
							}else {
								
								iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage); // minus retry attemps by 1 
							}
						}
						
					}else if(messageQueueDto.getMessageType().equals(SendWhatsapp)) {

						logDto.setToWhatsappNumber(messageQueueDto.getToWhatsappNumber());
						sendWhatsAppNewMethod(messageQueueDto);

 					}else if(messageQueueDto.getMessageType().equals(SendTextMessage)) {
						
						
						logDto.setMobileNumber(messageQueueDto.getMobileNumber());
						
						if(messageQueueDto.getSenderId() != null) {
							boolean smsFaild = false;
							try {	
								 //String sender = messageQueueDto.getSenderId();
								 //String first = sender.replace("@message", messageQueueDto.getTextMessageContent());
						 
								Integer msgType = (Integer) sessionFactory.getCurrentSession()
										.createSQLQuery("select sms_type from t_unit_application_billing_configuration where unit_id ="
												+ messageQueueDto.getUnitId() + " and organization_id ="
												+ messageQueueDto.getOrganizationId())
										.uniqueResult();
								
								
								
								String msgTypeJson = (String) sessionFactory.getCurrentSession()
										.createSQLQuery("select sms_type_json from t_unit_application_billing_configuration where unit_id ="
												+ messageQueueDto.getUnitId() + " and organization_id ="
												+ messageQueueDto.getOrganizationId())
										.uniqueResult();
								
								
								Gson gson = new Gson();
								MessageQueueDto msg91dto = gson.fromJson(msgTypeJson,
										MessageQueueDto.class);
								
								
								if(msgType == null) {
									msgType=1;
								}
								 if(msgType == 1) {
									 
									 
								        TrustManager[] trustAllCerts = new TrustManager[]{
								                new X509TrustManager() {
								                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
								                        return null;
								                    }
								                    public void checkClientTrusted(
								                        java.security.cert.X509Certificate[] certs, String authType) {
								                    }
								                    public void checkServerTrusted(
								                        java.security.cert.X509Certificate[] certs, String authType) {
								                    }
								                }};
								                   try {
								                    SSLContext sc = SSLContext.getInstance("SSL");
								                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
								                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
								                    } catch (Exception e) {
								                    logger.error("getMessageFromQueqe failed (SSL context setup)", e);
								                    }
								        
								 
									 String sender = messageQueueDto.getSenderId();
									 String first = sender.replace("@message", messageQueueDto.getTextMessageContent());
									 String second = first.replace("@mobileNumber", messageQueueDto.getMobileNumber());
									 System.out.println(second);
									 RestTemplate restTemplate = new RestTemplate();
									 String response = restTemplate.getForObject(second, String.class);
							            System.out.println("Response: " + response);
							            
								       /* OkHttpClient client = new OkHttpClient();
								        Request request = new Request.Builder()
								                .url(second)
								                .build();

								        Closeable response = client.newCall(request).execute();
								        System.out.println(response.toString());*/
								 }else {
								 	System.out.println("IN mSGGGG Type22222222:" +msgType);
									 Msg91MessageSendNew hr = new Msg91MessageSendNew();
									 hr.msg91Postdata(messageQueueDto.getTextMessageContent(), messageQueueDto.getMobileNumber(), messageQueueDto.getOrganizationId(), messageQueueDto.getUnitId(), sessionFactory);						
								 }
								 /*URI uri = URI.create(second); Old code 
								 
								String smsJson = restTemplate.getForObject(uri, String.class);*/
								
							} catch (Exception e) {
							logger.error("getMessageFromQueqe failed (sms send sub-step)", e);
							smsFaild = true;
							}
							if(smsFaild == false) {
								errorMessage =TEXTMSGSENT;
								successLogSaveFlag = true;
								iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());    // Text SMS send  successfully
								
							}else {
								
								errorMessage =TEXTMSGNOTSENT;
								if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
									
									iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
									faildLogSaveFlag = true;
									
								}else {                                                    // minus retry attemps by 1 
									
									iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);  
								}
							}
							
							
						}else {
							errorMessage =SENDERIDISSUE;
							if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
								
								iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
								faildLogSaveFlag = true;
								
							}else {                                                    // minus retry attemps by 1 
								
								iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);  
							}
						}
						
					}
					
					
					if(faildLogSaveFlag == true) {
						
						 saveLogDetails(messageQueueDto,errorMessage,false);
						
					}else if(successLogSaveFlag == true) {
						
						 saveLogDetails(messageQueueDto,errorMessage,true);
					}
					
					
				}
			}

		} catch (Exception e)
		{
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	private void sendBulkMail(MessageQueueDto messageQueueDto) {
		// TODO Auto-generated method stub
		
	}

	private Integer updateBulkMail(MessageQueueDto messageQueueDto, List<BulkEmailDto> bulkmailList, Integer logId, String errorMessage, boolean saveFlag) {
		try {
			
			  if(logId == null) {
				  logId = saveLogDetails(messageQueueDto,errorMessage,saveFlag); 
			  }
			
			for (BulkEmailDto bulkEmailDto : bulkmailList) {
				
				if(saveFlag == true) {
					
					iBulkMessageQueueService.updateBulkMailLogId(bulkEmailDto.getBulkMessageQueueId(),errorMessage,logId);  // if last retry attemps
					
					String[] res = bulkEmailDto.getUniqueId().split("[,]", 0);
				      for(String myStr: res){
				    	  String noSpaceStr = myStr.replaceAll("\\s", ""); // using built in method 
				    	  iTemplateTransactionDao.updateLabSampleDetailsMailStatus(Integer.parseInt(noSpaceStr));
				      }
					
				}else {
					
					if(bulkEmailDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {
						  
						iBulkMessageQueueService.updateBulkMailLogId(bulkEmailDto.getBulkMessageQueueId(),errorMessage,logId);  // if last retry attemps
					  
					}else {
						
						iBulkMessageQueueService.updateBulkMailRetryCount(bulkEmailDto.getBulkMessageQueueId(),errorMessage); // minus retry attemps by 1 
					}
				}
				
			}
		
		} catch (Exception e)
		{
		logger.error("updateBulkMail failed", e);
		}
		return logId;
		
	}

	private Integer saveLogDetails(MessageQueueDto messageQueueDto, String errorMessage, boolean saveFlag) {
		try
		{
			
			MessageLog logDto = new MessageLog();
			logDto.setCategoryId(messageQueueDto.getCategoryId());
			logDto.setCategoryName(messageQueueDto.getCategoryName());
			logDto.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			logDto.setMessageStatus(saveFlag); // message sent status
			logDto.setErrorMessage(errorMessage);
			logDto.setMessageType(messageQueueDto.getMessageType());
			logDto.setOrganizationId(messageQueueDto.getOrganizationId());
			logDto.setPatientIdList(messageQueueDto.getPatientIdList() != null ? messageQueueDto.getPatientIdList():null);
			logDto.setStatus('A');
			logDto.setUniqueId(messageQueueDto.getUniqueId()!= null ? messageQueueDto.getUniqueId(): null);
			logDto.setToMailId(messageQueueDto.getToMailId()!= null ?  messageQueueDto.getToMailId() : null);
			logDto.setMobileNumber(messageQueueDto.getMobileNumber() != null ? messageQueueDto.getMobileNumber(): null);
			logDto.setToWhatsappNumber(messageQueueDto.getToWhatsappNumber() != null ? messageQueueDto.getToWhatsappNumber() : null);
			logDto.setMessageQueueId(messageQueueDto.getMessageQueueId());
			logDto.setTriggerId(messageQueueDto.getTriggerId());
			logDto.setUnitId(messageQueueDto.getUnitId());
			logDto.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			logDto.setUserId(messageQueueDto.getUserId());
			logDto.setMessageSentStatus(saveFlag);
			logDto.setFromEmail(messageQueueDto.getSmtpUserId() != null ? messageQueueDto.getSmtpUserId() : null);
			logDto.setMailContent(messageQueueDto.getMailContent() != null ? messageQueueDto.getMailContent() : null);
			logDto.setMailSubject(messageQueueDto.getMailSubject() != null ? messageQueueDto.getMailSubject() : null);
			logDto.setReportString(messageQueueDto.getReportString() != null ? messageQueueDto.getReportString() : null);
			logDto.setTextMessageContent(messageQueueDto.getTextMessageContent() != null ?  messageQueueDto.getTextMessageContent() : null);
			logDto.setWhatsAppTemplateMasterId(messageQueueDto.getWhatsAppTemplateMasterId() != null ?  messageQueueDto.getWhatsAppTemplateMasterId() : null);
			Response res = iMessageLogService.saveMessageLog(logDto);
			if(res.getObject()!=null) {
				return (Integer) res.getObject();
			}else {
				return null;
			}
			

		} catch (Exception e)
		{
		logger.error("saveLogDetails failed", e);
		}
		return null;
	}

	@Override
	public Response checkAlreadyExitsOrNot(String uniqueId, Integer messageType, Integer triggerId) throws ApplicationException {
		try
		{
			return iMessageQueueDao.checkAlreadyExitsOrNot(uniqueId,messageType,triggerId);

		} catch (Exception e)
		{
		logger.error("checkAlreadyExitsOrNot failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response send(MessageQueueDto dto) throws ApplicationException {
		try
		{
			
			UnitApplicationConfigurationDto appDto = new UnitApplicationConfigurationDto();
			appDto.setOrganizationId(dto.getOrganizationId());
			appDto.setUnitId(dto.getUnitId());
			Response appRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigList(appDto);
			List<UnitApplicationConfigurationDto>  appList = new ArrayList<>();
			appList.addAll(appRes.getListObject());
			dto.setSmtpUserId(appList.get(0).getEmailId());
			 //for getting report URL
			String OTP= new DecimalFormat("000000").format(new Random().nextInt(999999));
			
			if(appList.get(0).getMailProviderId() ==2 ) {
				
				Response mailres = iMailingService.sendOTPMail(dto,appList.get(0).getMailCredential(),OTP);
				
			}else {
				
				AmazonMailDto amazonDto = new AmazonMailDto();
				amazonDto.setData(OTP);
				amazonDto.setFrom(dto.getSmtpUserId()!=null ? dto.getSmtpUserId() : "info@axonichealth.co.in");
				amazonDto.setSubject("Registration OTP");
				amazonDto.setTo(dto.getToMailId());
				
				try {
				
				Gson gson = new Gson();
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "application/json");
				org.springframework.http.HttpEntity<AmazonMailDto> request = new org.springframework.http.HttpEntity<>(amazonDto, headers);
				URI url = URI.create("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail");
				String stringResponse = restTemplate.postForObject(url, request, String.class);
				Response commonResponse = gson.fromJson(stringResponse, Response.class);

				}catch(Exception e) {
					logger.error(e.getMessage(), e);
					return new Response(ERROR, ERROR_CODE, "No Records Found.", null, OTP.toString());
				}
			}

			return new Response(SUCCESS, "","No Records Found.",null ,OTP.toString());

		} catch (Exception e)
		{
		logger.error("send failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response sendWhatsAppNewMethod(MessageQueueDto messageQueueDto) throws ApplicationException {
		
		try {
			
		ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		boolean faildLogSaveFlag = false;
		Boolean checkException = false;
		boolean successLogSaveFlag = false;
		String errorMessage = "";
		if (messageQueueDto.getPdfGenerationUrl() != null && messageQueueDto.getMailContent() != null) {
			
			boolean reportIsNull = false;
			try {
				
			
				if(messageQueueDto.getReportString()!= null && !messageQueueDto.getReportString().equals("")) {
					
					HttpPost pst = new HttpPost(messageQueueDto.getPdfGenerationUrl());
					pst.addHeader("Content-Type", "application/json");
					pst.setEntity(new StringEntity(messageQueueDto.getReportString()));

					HttpResponse result = httpClient.execute(pst);
					HttpEntity resEntity = result.getEntity();
					resEntity.writeTo(byteOutPutStream);
					
					if(result.getStatusLine().getStatusCode() == 200) {
						reportIsNull = true;
					}
					
				}else {
					reportIsNull = true;
				}
				
					if (reportIsNull) { // RPF file generated successfully

						try {

							Response resMsg = null;
							UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
							unitApplicationConfigurationDto.setOrganizationId(messageQueueDto.getOrganizationId());
							unitApplicationConfigurationDto.setUnitId(messageQueueDto.getUnitId());
							com.param.global.common.Response appConfigRes = iUnitApplicationBillingConfigurationDao
									.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
							UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
									.getObject();
							if (unitApplicationConfigurationDtos.getWhatsAppOption() == null) {
								Integer whh = 1;
								unitApplicationConfigurationDtos.setWhatsAppOption(whh);
							}
							if (unitApplicationConfigurationDtos.getWhatsAppOption() == 1) {
								whatsAppRequestDto newDto = new whatsAppRequestDto();
								newDto.setTo_number(messageQueueDto.getToWhatsappNumber());
								SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
								smsInfo.setUnitId(messageQueueDto.getUnitId());
								smsInfo.setOrganizationId(messageQueueDto.getOrganizationId());
								smsInfo.setUnitFolder(messageQueueDto.getUnitFolder());

								String filePath = docUploadUtility.uploadwhatsAppDocumentFile(byteOutPutStream,
										messageQueueDto.getMailFileName(), smsInfo);
								newDto.setUnitId(messageQueueDto.getUnitId());
								newDto.setOrganizationId(messageQueueDto.getOrganizationId());
								newDto.setMediaUrl(filePath);
								newDto.setMessage(messageQueueDto.getMailContent());
								newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
								resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageTwilio(newDto);
							} else if (unitApplicationConfigurationDtos.getWhatsAppOption() == 2) {
								whatsAppRequestDto newDto = new whatsAppRequestDto();
								newDto.setUnitId(messageQueueDto.getUnitId());
								newDto.setOrganizationId(messageQueueDto.getOrganizationId());
								newDto.setTo(messageQueueDto.getToWhatsappNumber());
								newDto.setText(messageQueueDto.getMailContent());
								newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());

								resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageVonage(newDto);
							} else if (unitApplicationConfigurationDtos.getWhatsAppOption() == 3) {

								whatsAppRequestDto newDto = new whatsAppRequestDto();
								newDto.setUnitId(messageQueueDto.getUnitId());
								newDto.setOrganizationId(messageQueueDto.getOrganizationId());
								// Authorization
								Response resss = iwhatsAppServiceImpl.authontificationKeyGenerationForHeloAPI(newDto);

								if (resss.getCode().equals("2000")) {
									newDto.setUnitId(messageQueueDto.getUnitId());
									newDto.setOrganizationId(messageQueueDto.getOrganizationId());
									newDto.setTo(messageQueueDto.getToWhatsappNumber());
									newDto.setText(messageQueueDto.getMailContent());
									newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
									newDto.setResponseJson(resss.getObject());
									resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageHelo(newDto);
								}

							} else if (unitApplicationConfigurationDtos.getWhatsAppOption() == 4) {

								whatsAppRequestDto newDto = new whatsAppRequestDto();
								newDto.setUnitId(messageQueueDto.getUnitId());
								newDto.setOrganizationId(messageQueueDto.getOrganizationId());

								SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
								smsInfo.setUnitId(messageQueueDto.getUnitId());
								smsInfo.setOrganizationId(messageQueueDto.getOrganizationId());
								smsInfo.setUnitFolder(messageQueueDto.getUnitFolder());
								String filePath = "";
								if (messageQueueDto.getMailFileName() != null
										&& !messageQueueDto.getMailFileName().trim().isEmpty()) {
									filePath = docUploadUtility.uploadwhatsAppDocumentFile(byteOutPutStream,
											messageQueueDto.getMailFileName(), smsInfo);

								}
								newDto.setUnitId(messageQueueDto.getUnitId());
								newDto.setOrganizationId(messageQueueDto.getOrganizationId());
								newDto.setTo(messageQueueDto.getToWhatsappNumber());
								newDto.setText(messageQueueDto.getMailContent());
								newDto.setWhatsAppTemplateMasterId(messageQueueDto.getWhatsAppTemplateMasterId());
								newDto.setWhatsAppTemplateName(messageQueueDto.getWhatsAppTemplateName());
								newDto.setMediaUrl((filePath != null && !(filePath.trim()).isEmpty()) ? filePath
										: messageQueueDto.getWhatsappDocumentUrl());
								newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
								newDto.setWhatsAppMessageType(messageQueueDto.getWhatsAppMessageType());

								resMsg = iwhatsAppServiceImpl.sendWhatsAppMessagePinbot(newDto);

							}

							if (resMsg.getObject() == null) {

								errorMessage = WHATSAPPNOTSENT;

								if (messageQueueDto.getRetryAttemps().equals(LastRetryAttemps)
										|| messageQueueDto.getRetryAttemps() < LastRetryAttemps) { // if last retry
																									// attemps

									iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
									faildLogSaveFlag = true;

								} else { // minus retry attemps by 1

									iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),
											errorMessage);
								}

							} else { // whatsapp send successfully
								errorMessage = WHATSAPPSENT;
								successLogSaveFlag = true;
								iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
							}
						
							
							

						}catch (Exception e) {
						logger.error("sendWhatsAppNewMethod failed", e);
						checkException = true;
						}
						
						if(checkException){
							
							 faildLogSaveFlag =  updateQueueWhenException(messageQueueDto,errorMessage,3);
							
						}
						

					}else {                                                        // RPF file Not generated  
					errorMessage =RPTFILLISSUE;
					if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
						
						iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
						faildLogSaveFlag = true;
						
					}else {                                                    // minus retry attemps by 1
						
						iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
					}
				}
			}catch(Exception e) {
			logger.error("sendWhatsAppNewMethod failed", e);
			checkException = true;
			}
			
			if(checkException){
				
				 faildLogSaveFlag =  updateQueueWhenException(messageQueueDto,errorMessage,3);
				
			}
			
		}else {                                                       // Pdf Generation Url not found 
			errorMessage =PDFURLISSUE;
			if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
				
				iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
				faildLogSaveFlag = true;
				
			}else {                                                    // minus retry attemps by 1
				
				iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
			}
		}
		
		if(faildLogSaveFlag == true) {
			
			 saveLogDetails(messageQueueDto,errorMessage,false);
			
		}else if(successLogSaveFlag == true) {
			
			 saveLogDetails(messageQueueDto,errorMessage,true);
		}
		
		return new Response(SUCCESS,SUCCESS_CODE ,SUCCESS_MESSAGE, null, null);
		
		} catch (Exception e)
		{
		logger.error("sendWhatsAppNewMethod failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	private  Boolean updateQueueWhenException(MessageQueueDto messageQueueDto, String errorMessage, int from) {
		
		Boolean faildLogSaveFlag = false;
		
		errorMessage = WHATSAPPNOTSENT;

		if (messageQueueDto.getRetryAttemps().equals(LastRetryAttemps)
				|| messageQueueDto.getRetryAttemps() < LastRetryAttemps) { // if last retry
																			// attemps

			iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
			faildLogSaveFlag = true;

		} else { // minus retry attemps by 1

			iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),
					errorMessage);
		}
		return faildLogSaveFlag;
		
	}

	@Override
	public Response addTextWhatsAppMessageInMessageLog(MessageQueueDto messageQueueDto) throws ApplicationException {
		try
		{
					boolean faildLogSaveFlag = false;
					boolean successLogSaveFlag = false;
					String errorMessage = "";
					MessageLog logDto = new MessageLog();
						
						logDto.setToWhatsappNumber(messageQueueDto.getToWhatsappNumber());
						
						ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
							
							try {
									 
									 Response resMsg = null;
								    UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
									unitApplicationConfigurationDto.setOrganizationId(messageQueueDto.getOrganizationId());
									unitApplicationConfigurationDto.setUnitId(messageQueueDto.getUnitId());
							    com.param.global.common.Response appConfigRes= iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
								UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
								if(unitApplicationConfigurationDtos.getWhatsAppOption() == null) {
									Integer whh = 1;
									unitApplicationConfigurationDtos.setWhatsAppOption(whh);
								}
							if( unitApplicationConfigurationDtos.getWhatsAppOption() == 1) {
										whatsAppRequestDto newDto = new whatsAppRequestDto();
										newDto.setTo_number(messageQueueDto.getToWhatsappNumber());
										SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
										smsInfo.setUnitId(messageQueueDto.getUnitId());
										smsInfo.setOrganizationId(messageQueueDto.getOrganizationId());
										smsInfo.setUnitFolder(messageQueueDto.getUnitFolder());
										
							        String filePath =  docUploadUtility.uploadwhatsAppDocumentFile(byteOutPutStream,messageQueueDto.getMailFileName(),smsInfo);
							        newDto.setUnitId(messageQueueDto.getUnitId());  
							        newDto.setOrganizationId(messageQueueDto.getOrganizationId());
									newDto.setMediaUrl(filePath);
									newDto.setMessage(messageQueueDto.getMailContent());
									newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
									 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageTwilio(newDto);
									}else if( unitApplicationConfigurationDtos.getWhatsAppOption() == 2) {
										whatsAppRequestDto newDto = new whatsAppRequestDto();
										newDto.setUnitId(messageQueueDto.getUnitId());
										newDto.setOrganizationId(messageQueueDto.getOrganizationId());
										newDto.setTo(messageQueueDto.getToWhatsappNumber());
									    newDto.setText(messageQueueDto.getMailContent());
										newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
									
									 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageVonage(newDto);
							       }else if( unitApplicationConfigurationDtos.getWhatsAppOption() == 3) {
							    	   
							    		whatsAppRequestDto newDto = new whatsAppRequestDto();
							    	    newDto.setUnitId(messageQueueDto.getUnitId());
										newDto.setOrganizationId(messageQueueDto.getOrganizationId());
										//Authorization
							    	   Response resss = iwhatsAppServiceImpl.authontificationKeyGenerationForHeloAPI(newDto);
							    	   
									   if(resss.getCode().equals("2000")) {
										    newDto.setUnitId(messageQueueDto.getUnitId());
											newDto.setOrganizationId(messageQueueDto.getOrganizationId());
											newDto.setTo(messageQueueDto.getToWhatsappNumber());
										    newDto.setText(messageQueueDto.getMailContent());
											newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
											newDto.setResponseJson(resss.getObject());
										 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageHelo(newDto);
									   }
										
							       }
									else if( unitApplicationConfigurationDtos.getWhatsAppOption() == 4) {

										whatsAppRequestDto newDto = new whatsAppRequestDto();

										SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
										smsInfo.setUnitId(messageQueueDto.getUnitId());
										smsInfo.setOrganizationId(messageQueueDto.getOrganizationId());
										smsInfo.setUnitFolder(messageQueueDto.getUnitFolder());
										String filePath = "";
										if(messageQueueDto.getMailFileName() != null && !messageQueueDto.getMailFileName().trim().isEmpty()){
											filePath =  docUploadUtility.uploadwhatsAppDocumentFile(byteOutPutStream,messageQueueDto.getMailFileName(),smsInfo);

										}

										newDto.setUnitId(messageQueueDto.getUnitId());
										newDto.setOrganizationId(messageQueueDto.getOrganizationId());
										newDto.setTo(messageQueueDto.getToWhatsappNumber());
										newDto.setText(messageQueueDto.getMailContent());
										newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
										newDto.setWhatsAppTemplateName(messageQueueDto.getWhatsAppTemplateName());
										newDto.setMediaUrl((filePath != null && !(filePath.trim()).isEmpty()) ? filePath : messageQueueDto.getWhatsappDocumentUrl());
										newDto.setWhatsAppTemplateMasterId(messageQueueDto.getWhatsAppTemplateMasterId());
										resMsg = iwhatsAppServiceImpl.sendWhatsAppMessagePinbot(newDto);
									}
									
									if(resMsg.getObject() == null) {
										errorMessage =WHATSAPPNOTSENT;
										faildLogSaveFlag = true;
									}else {                                                    // whatsapp send  successfully
										errorMessage =WHATSAPPSENT;
										successLogSaveFlag = true;
									}
						
							}catch(Exception e) {
							logger.error("addTextWhatsAppMessageInMessageLog failed", e);
							}
							
					if(faildLogSaveFlag == true) {
						
						 saveLogDetails(messageQueueDto,errorMessage,false);
						
					}else if(successLogSaveFlag == true) {
						
						 saveLogDetails(messageQueueDto,errorMessage,true);
					}
					
					

		} catch (Exception e)
		{
		logger.error("addTextWhatsAppMessageInMessageLog failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response addTextSMSMessageInMessageLogText(MessageQueueDto messageQueueDto) throws ApplicationException {
		try
		{
					boolean faildLogSaveFlag = false;
					boolean successLogSaveFlag = false;
					String errorMessage = "";
					MessageLog logDto = new MessageLog();
						
						logDto.setMobileNumber(messageQueueDto.getMobileNumber());
						
						if(messageQueueDto.getSenderId() != null) {
							boolean smsFaild = false;
							try {	
								
								 String sender = messageQueueDto.getSenderId();
								 String first = sender.replace("@message", messageQueueDto.getTextMessageContent());
								
								 String second = first.replace("@mobileNumber", messageQueueDto.getMobileNumber());
								 
								 
								 OkHttpClient client = new OkHttpClient();
								 Request request = new Request.Builder()
											.url(second).get().build();
								 okhttp3.Response response = client.newCall(request).execute();
								 
								
							} catch (Exception e) {
							logger.error("addTextSMSMessageInMessageLogText failed", e);
							smsFaild = true;
							}
							if(smsFaild == false) {
								errorMessage =TEXTMSGSENT;
								successLogSaveFlag = true;
								
							}else {
								    errorMessage =TEXTMSGNOTSENT;
									faildLogSaveFlag = true;
							}
						}else {
							    errorMessage =SENDERIDISSUE;
								faildLogSaveFlag = true;
						}
						
					if(faildLogSaveFlag == true) {
						 messageQueueDto.setMessageType(SendTextMessage);
						 saveLogDetails(messageQueueDto,errorMessage,false);
						
					}else if(successLogSaveFlag == true) {
						
						 saveLogDetails(messageQueueDto,errorMessage,true);
					}

		} catch (Exception e)
		{
		logger.error("addTextSMSMessageInMessageLogText failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response sendOTPonMobile(MessageQueueDto dto) throws ApplicationException {

		try
		{
			
			UnitApplicationConfigurationDto appDto = new UnitApplicationConfigurationDto();
			appDto.setOrganizationId(dto.getOrganizationId());
			appDto.setUnitId(dto.getUnitId());
			Response appRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigList(appDto);
			List<UnitApplicationConfigurationDto>  appList = new ArrayList<>();
			appList.addAll(appRes.getListObject());
			dto.setSmtpUserId(appList.get(0).getSenderId());
			 //for getting report URL
			String OTP= new DecimalFormat("000000").format(new Random().nextInt(999999));
			String message = "OTP for verification  "	;
				 String sender = dto.getSmtpUserId();
				 String first = sender.replace("@message", message+" "+OTP);
				 String second = first.replace("@mobileNumber", dto.getMobileNumber());
				 OkHttpClient client = new OkHttpClient();
				 Request request = new Request.Builder()
							.url(second).get().build();
				 okhttp3.Response response = client.newCall(request).execute();
			
			return new Response(SUCCESS, "","No Records Found.",null ,OTP.toString());

		} catch (Exception e)
		{
		logger.error("sendOTPonMobile failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	public ByteArrayOutputStream passwordProtectPDF(byte[] pdfData, String password) throws IOException {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfData))) {

            PDDocumentInformation pdd = document.getDocumentInformation();
            pdd.setTitle("Protected PDF");
            pdd.setAuthor("Your Name or Company");
            pdd.setSubject("PDF Encryption");
            pdd.setKeywords("password protection, PDFBox");
            pdd.setCustomMetadataValue("Info", "The password to open the PDF is the date of birth of the patient in dd-mm-yyyy format.");
            
            StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(password, password, new AccessPermission());
            document.setDocumentInformation(pdd);
            protectionPolicy.setEncryptionKeyLength(128);
 
            protectionPolicy.setPermissions(new AccessPermission());
 
            document.protect(protectionPolicy);
 
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream;
        }
    }

}
