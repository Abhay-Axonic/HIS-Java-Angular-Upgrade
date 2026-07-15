package com.param.global.mail;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.param.data.dto.MailReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.RegistrationType;
import com.param.global.common.Response;
import com.param.global.dao.IMessageQueueDao;
import com.param.global.dao.IUserMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AmazonMailAttachement;
import com.param.global.dto.AmazonMailDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.MailDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderPatientEducationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.unit.dto.BulkEmailDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import in.param.exception.ApplicationException;

import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class MailingServiceImpl<E> implements IMailingService,ICommonConstants,IMailConstants{
	
	final static Logger logger = Logger.getLogger(MailingServiceImpl.class);
	@Autowired
	private IUserMasterDao iUserMasterDao;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private  RestTemplate restTemplate;
	
	@Autowired
	IMessageQueueDao iMessageQueueDao;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;

	@Autowired
	private SessionFactory SessionFactory;
	   
	private MailDto loadProperties()
	{
		MailDto mailDto = new MailDto();
		
		mailDto.setId(sessionService!=null && sessionService.getCookieObjectAppConfig()!=null && sessionService.getCookieObjectAppConfig().getId()!=null ? sessionService.getCookieObjectAppConfig().getId() : "info@axonichealth.co.in");
		mailDto.setPassword(sessionService!=null && sessionService.getCookieObjectAppConfig()!=null && sessionService.getCookieObjectAppConfig().getPassword()!=null ? sessionService.getCookieObjectAppConfig().getPassword() : System.getenv("GLOBAL_SMTP_PASSWORD"));
		mailDto.setHost(sessionService!=null && sessionService.getCookieObjectAppConfig()!=null && sessionService.getCookieObjectAppConfig().getHost()!=null ? sessionService.getCookieObjectAppConfig().getHost() : "smtp.gmail.com");
		mailDto.setPort(sessionService!=null && sessionService.getCookieObjectAppConfig()!=null && sessionService.getCookieObjectAppConfig().getPort()!=null ? sessionService.getCookieObjectAppConfig().getPort() : "465");

		Properties props = new Properties();	
			props.put("mail.smtp.host", mailDto.getHost());
	    	props.put("mail.smtp.socketFactory.port", mailDto.getPort());
	    	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.port", mailDto.getPort());
	    	props.put("mail.smtp.localhost", "127.0.0.1");
	    	props.put("mail.smtp.debug", "true");
	    mailDto.setProperties(props);
	    
	    return mailDto;
	}
	
	
	private MailDto loadPropertiesForAutoJob()
	{
		MailDto mailDto = new MailDto();
		
		mailDto.setId("info@axonichealth.co.in");
		mailDto.setPassword(System.getenv("GLOBAL_SMTP_PASSWORD"));
		mailDto.setHost("smtp.gmail.com");
		mailDto.setPort("465");

		Properties props = new Properties();	
			props.put("mail.smtp.host", mailDto.getHost());
	    	props.put("mail.smtp.socketFactory.port", mailDto.getPort());
	    	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.port", mailDto.getPort());
	    	props.put("mail.smtp.localhost", "127.0.0.1");
	    	props.put("mail.smtp.debug", "true");
	    mailDto.setProperties(props);
	    
	    return mailDto;
	}


	@Override
	public Response forgotPassword(UserMasterDto userMasterDto)
			throws ApplicationException {
		try{
			
			MailDto mailDto = loadProperties();
			
			userMasterDto = iUserMasterDao.userLogin(userMasterDto).getListObject().size() > 0 ? (UserMasterDto)iUserMasterDao.userLogin(userMasterDto).getListObject().get(0) : null;
			if(userMasterDto != null){
				/*if(userMasterDto.getRoleId() == PATIENT)
					// get patient by UserId
					return iPatientMasterDao.getPatientByUserId(userMasterDto);
				if(userMasterDto.getRoleId() == DOCTOR)
					//get doctor by userId
					return iDoctorMasterDao.getDoctorByUserId(userMasterDto);*/
				
			}
			}catch(Exception e){
			logger.error("forgotPassword failed", e);
			}
			return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response sendEmailToUserWithNewPassword(UserMasterDto userMasterDto)
			throws ApplicationException {
		try{
			MailDto mailDto = loadProperties();	
			
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication(){
						return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
					}
				});
	
			try{
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(mailDto.getId()));
					message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(userMasterDto.getEmailId()));
					message.setSubject(MAIL_SUBJECT);
					message.setText(MESSAGE + userMasterDto.getPassword() + NOTE);
					Transport.send(message);
					//System.out.println("Mail Sent successfully");
					return new Response(SUCCESS, null, PASSWORD_CHANGE_MAIL_SENT_SUCCESS, null,null);	
			}catch(Exception e){
			logger.error("sendEmailToUserWithNewPassword failed", e);
			}	
		}catch(Exception e){
		logger.error("sendEmailToUserWithNewPassword failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	public String getMailBodyForforgotPassword(String password){
		return "Hi.. your new password is"+password+"\n Note : Please change your passwrod after login from my profile.";
	}
	
	public String generateNewPassword(){
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		String output = sb.toString();
		//System.out.println(output);
		return output;
	}
	@Override
	public Response sendUserName(UserMasterDto userMasterDto,RegistrationType registration)throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			RegistrationType registrationType = registration;
			
			String message = SALUTATION + userMasterDto.getFirstName();
			switch(registrationType){
				case REG_PATIENT:
					message = PATIENT_REG_CONTENT + "<br>"+USERNAME + userMasterDto.getUserName() + "<br>"+PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword())+ CLINI_REG_SALUTATION ;
					break;
				case REG_DOCTOR:
					message = DOCTOR_REG_CONTENT +"<br>"+ USERNAME + userMasterDto.getUserName()  + "<br>"+PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()) + CLINI_REG_SALUTATION ;
					break;
				case REG_EMPLOYEE:
					message = EMPLOYEE_REG_CONTENT +"<br>"+ USERNAME + userMasterDto.getUserName() + "<br>"+PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()) + CLINI_REG_SALUTATION ;
					break;
				default :
					message = "";
					break;
			}
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(userMasterDto.getEmailId()));
			mimeMessage.setSubject(REG_SUBJECT);
			if(registrationType == RegistrationType.REG_PATIENT) {
				mimeMessage.setContent(message, "text/html");
			}else {
				mimeMessage.setContent(message, "text/html");
				//mimeMessage.setText(message);
			}
			
			
			Transport.send(mimeMessage);
			//System.out.println("Mail Sent successfully");
			return new Response(SUCCESS, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendUserName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response sendUserPassword(UserMasterDto userMasterDto,RegistrationType registration)throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			RegistrationType registrationType =registration;
			
			String message = SALUTATION + userMasterDto.getFirstName();
			switch(registrationType){
				case REG_PATIENT:
					message = PATIENT_REG_CONTENT  +"<br>"+PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword())+ CLINI_REG_SALUTATION ;
					break;
				case REG_DOCTOR:
					message = DOCTOR_REG_CONTENT +"<br>"+  PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword())+  CLINI_REG_SALUTATION ;
					break;
				case REG_EMPLOYEE:
					message = EMPLOYEE_REG_CONTENT + "<br>" +PASSWORD + GlobalCommonDateUtils.decrypt(userMasterDto.getPassword()) + CLINI_REG_SALUTATION ;
					break;
				default :
					message = "";
					break;
			}
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(userMasterDto.getEmailId()));
			mimeMessage.setSubject(REG_SUBJECT);
			if(registrationType == RegistrationType.REG_PATIENT) {
				mimeMessage.setContent(message, "text/html");
			}else {
				mimeMessage.setContent(message, "text/html");
			}
			
			Transport.send(mimeMessage);
			//System.out.println("Mail Sent successfully");
			return new Response(SUCCESS, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendUserPassword failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	
/*	@Override
	public Response sendMailToUserWithPasswordProtectedDocumets(
			SharePatientDocumentDto documentDto, List<String> listUrl) throws ApplicationException {
		
		try{
			if(listUrl != null && listUrl.size() > 0){

			Session session = Session.getDefaultInstance(props,new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			String urlListText =  "";
			for(String url : listUrl){
				urlListText +="\n "+ url;
			}
				try{
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(mailDto.getId()));
						message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(documentDto.getEmailId()));
						message.setSubject(PASSWORD_PROTECTED_DOCUMENT_SUBJECT);
						message.setText(PASSWORD_PROTECTED_DOCUMENT_MESSAGE + urlListText);
						
						//messageBodyPart.setText(msg);
						Transport.send(message);
						//System.out.println("Mail Sent successfully");
						return new Response(SUCCESS, null, DOCUMENT_MAIL_SENT_SUCCESS, null);	
				}catch(Exception e){
					
					return new Response(ERROR, null, WRONG_EMAIL_ID, null);
				}	
			}
		}catch(Exception e){
			
			return new Response(ERROR, null, WRONG_EMAIL_ID, null);
		}
		return new Response(ERROR, null, WRONG_EMAIL_ID, null);
	
	}*/
	
	public String createLink(String link) {
		return "<a href=\""+link+"\" target=\"_blank\">MyLife</a>";
	}
	

	@Override
	public Response resendUserName(String emailID , String userName)throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = SALUTATION ;
			
					message =  USERNAME_MAIL_CONTENT +userName+"<br>"+  CLINI_REG_SALUTATION ;
					
					//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailID));
			mimeMessage.setSubject(RESEND_USERNAME_SUBJECT);
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
	//		//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("resendUserName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response resendUserNameAndPassword(String emailID , String userName,String password)throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = SALUTATION ;
			
					message =  USERNAME_MAIL_CONTENT +userName+"<br>"+ PWD_CONTENT+password+"<br>"+ CLINI_REG_SALUTATION ;
					
					//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailID));
			mimeMessage.setSubject(RESEND_USERNAME_AND_PWD_SUBJECT);
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
	//		//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("resendUserNameAndPassword failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	public void sendMail(String email,String fileName,ByteArrayOutputStream report,UnitApplicationConfigurationDto dto){
		  
		//MailDto mailDto = loadProperties();
		
		new Runnable() {
		   
		   @Override
		   public void run() {
		    try{
		    	Properties mailprops = new Properties();	
		    	mailprops.put("mail.smtp.host", dto.getHost());
		    	mailprops.put("mail.smtp.socketFactory.port", dto.getPort());
		    	mailprops.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		    	mailprops.put("mail.smtp.auth", "true");
		    	mailprops.put("mail.smtp.port", dto.getPort());	
		    	
		    //get authentication 
		     Session session = Session.getDefaultInstance(mailprops,new Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication(){
		       return new PasswordAuthentication(dto.getId(), dto.getPassword());
		      }
		     });
		     
		   
	            byte[] bytes = report.toByteArray();
	             
	            //construct the pdf body part
	            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

	            // Create a default MimeMessage object.
		            Message message = new MimeMessage(session);

		            // Set From: header field of the header.
		            message.setFrom(new InternetAddress(dto.getId()));

		            // Set To: header field of the header.
		            message.setRecipients(Message.RecipientType.TO,
		               InternetAddress.parse(email));

		            // Set Subject: header field
		            message.setSubject("RIS Report");

		            // Create the message part
		            BodyPart messageBodyPart = new MimeBodyPart();

		            // Now set the actual message
		            messageBodyPart.setText("");

		            // Create a multipar message
		            Multipart multipart = new MimeMultipart();

		            // Set text message part
		            multipart.addBodyPart(messageBodyPart);

		            // Part two is attachment
		            messageBodyPart = new MimeBodyPart();
		            
		            final MimeBodyPart attachmentPart = new MimeBodyPart();
		            
		            attachmentPart.setDataHandler(new DataHandler(dataSource));
		            attachmentPart.setFileName(fileName);
		            multipart.addBodyPart(attachmentPart);

		            // Send the complete message parts
		            message.setContent(multipart);

		            // Send message
		            Transport.send(message);

		            //System.out.println("Sent message successfully....");
		    }catch(Exception e){
		    logger.error("run failed", e);
		    }
		   }
		  }.run();
		 }

	@Override
	public Response sendOTP(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();

			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			String lastFourDigits = "";
			lastFourDigits = dto.getCardNo().substring(dto.getCardNo().length() - 4);
			String userName = dto.getFirstName()!=null ? dto.getFirstName() : " ";
			String message = "Dear "+userName;
			message+=" ,<br><br> Greetings for Day!!!." + ",<br><br>"+"   Your Loyality Card XXXX"+lastFourDigits+" has been used for a transaction of INR "+dto.getConsumeAmount()+" on "+ GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss")
					+ "    Your one time password (OTP) for this transaction  is "+dto.getOtp()+".<br>"
					+ "   Please do not share the OTP with anyone. <br><br><br><br><br><br>"
					+ "  Thanks <br>"
					+dto.getUnitName() ;
//					message =  USERNAME_MAIL_CONTENT +userName+"<br>"+ PWD_CONTENT+password+"<br>"+ CLINI_REG_SALUTATION ;
					
					//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getEmail()));
			mimeMessage.setSubject("Loyalty card OTP");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
	//		//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendOTP failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response sentPatinetEducationTemplate(ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto)
			throws ApplicationException {
		try{
			//send procedure order patient education template to patient
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = SALUTATION ;
			message =  "<br>"+ procedureOrderPatientEducationDto.getPatientEducationTempale() ;
			//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(procedureOrderPatientEducationDto.getEmail()));
			mimeMessage.setSubject("Patient Education Template ");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sentPatinetEducationTemplate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response consumeLoyaltyAmount(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			String lastFourDigits = "";
			//double cardAmt = dto.getCardAmount();
		//	double consumeAmount = dto.getConsumeAmount();
			double availableAmount =  dto.getCardAmount() - dto.getConsumeAmount();
			String finalAvailableAmount =  String.format("%.2f", availableAmount);
			lastFourDigits = dto.getCardNo().substring(dto.getCardNo().length() - 4);
			String message = "Dear "+dto.getFirstName()!=null ? dto.getFirstName() : "" + ",<br> Greetings for Day!!!. <br>";
			message+=" Your a/c no. XXXXXXXXXXXX"+lastFourDigits+" is debited by Rs."+dto.getConsumeAmount()+"<br>"
					+ "on "+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss")+" your Available bal is Rs. "+finalAvailableAmount;
					
					//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getEmail()));
			mimeMessage.setSubject("Loyalty card Details");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
	//		//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("consumeLoyaltyAmount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addLoyaltyAmount(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			String lastFourDigits = "";
			double availableAmount = dto.getCardAmount() + dto.getConsumeAmount();
			lastFourDigits = dto.getCardNo().substring(dto.getCardNo().length() - 4);
			String message = "Dear "+dto.getFirstName()!=null ? dto.getFirstName() : "" + ",<br> Greetings for Day!!!. <br>";
			message+=" Your loyalty card  a/c no. XXXXXXXXXXXX"+lastFourDigits+" is credited with Rs."+dto.getConsumeAmount()+"<br>"
					+ "on "+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy HH:mm:ss")+" your Available bal is Rs. "+availableAmount;
					
					//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getEmail()));
			mimeMessage.setSubject("Loyalty card OTP");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
	//		//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("addLoyaltyAmount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response sendSchedulingNotification(ProcedureStaffScheduleDto procedureStaffScheduleDto,
			ProcedureSchedulingDto procedureSchedulingDto) {
		try{
			Integer doctorId = 0;
			Integer employeeId = 0;
			
			 doctorId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
					"select dm.doctor_id as \"doctorId\" from doctor.m_doctor_master dm where dm.user_id =:userId")
					.setInteger("userId", procedureStaffScheduleDto.getUserId()).uniqueResult();
			 
			 
			 employeeId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
						"select em.employee_id  as \"employeeId\" from public.m_employee_master em where em.user_id =:userId")
						.setInteger("userId", procedureStaffScheduleDto.getUserId()).uniqueResult();
				 
			 
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = simpleDateFormat.format(GlobalCommonDateUtils.getDate(procedureStaffScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
			Integer RescheduleOrNot = 0;
			if(procedureSchedulingDto.getRescheduleOrNot().equals(RescheduleOrNot)) {
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
				smsInfo.setDateTime(date != null
	          		  ? (date.substring(0, 10) + " at "
	                            + procedureStaffScheduleDto.getFromTime())
	                    : (date.substring(0, 10) + " at "
	                            + procedureStaffScheduleDto.getFromTime()));
					smsInfo.setOrganizationId(procedureSchedulingDto.getOrganizationId());
					smsInfo.setUnitId(procedureSchedulingDto.getUnitId());
					smsInfo.setProceduceName(procedureSchedulingDto.getProcedureName());
					smsInfo.setPatientId(procedureSchedulingDto.getPatientId());
					if(doctorId != null && doctorId != 0) {
						smsInfo.setAdmitingDoctorId(doctorId);
					}
					if(employeeId != null && employeeId != 0) {
						smsInfo.setEmployeeId(employeeId);
					}
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.SURGERY_SCHEDULING,smsInfo);
				
				if(procedureStaffScheduleDto.getEmailId()!=null && procedureStaffScheduleDto.getEmailId().length()>0) 
				{
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					emailinfo.setUnitId(procedureSchedulingDto.getUnitId());
					emailinfo.setOrganizationId(procedureSchedulingDto.getOrganizationId());
					emailinfo.setPatientEmail(procedureStaffScheduleDto.getEmailId());
					emailinfo.setPatientId(procedureSchedulingDto.getPatientId());
					emailinfo.setProceduceName(procedureSchedulingDto.getProcedureName());
					emailinfo.setDateTime(date != null
			          		  ? (date.substring(0, 10) + " at "
			                            + procedureStaffScheduleDto.getFromTime())
			                    : (date.substring(0, 10) + " at "
			                            + procedureStaffScheduleDto.getFromTime()));
					if(doctorId != null && doctorId != 0) {
						emailinfo.setAdmitingDoctorId(doctorId);
					}
					if(employeeId != null && employeeId != 0) {
						emailinfo.setEmployeeId(employeeId);
					}
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.SURGERY_SCHEDULING, emailinfo);
				}
				}else {
			SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
			smsInfo.setDateTime(date != null
          		  ? (date.substring(0, 10) + " at "
                            + procedureStaffScheduleDto.getFromTime())
                    : (date.substring(0, 10) + " at "
                            + procedureStaffScheduleDto.getFromTime()));
				smsInfo.setOrganizationId(procedureSchedulingDto.getOrganizationId());
				smsInfo.setUnitId(procedureSchedulingDto.getUnitId());
				smsInfo.setPatientId(procedureSchedulingDto.getPatientId());
				smsInfo.setProceduceName(procedureSchedulingDto.getProcedureName());
				if(doctorId != null && doctorId != 0) {
					smsInfo.setAdmitingDoctorId(doctorId);
				}
				
				if(employeeId != null && employeeId != 0) {
					smsInfo.setEmployeeId(employeeId);
				}
			smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.SURGERY_RESCHEDULING,smsInfo);
			
			if(procedureStaffScheduleDto.getEmailId()!=null && procedureStaffScheduleDto.getEmailId().length()>0) 
			{
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(procedureSchedulingDto.getUnitId());
				emailinfo.setOrganizationId(procedureSchedulingDto.getOrganizationId());
				emailinfo.setPatientEmail(procedureStaffScheduleDto.getEmailId());
				emailinfo.setPatientId(procedureSchedulingDto.getPatientId());
				emailinfo.setProceduceName(procedureSchedulingDto.getProcedureName());
				emailinfo.setDateTime(date != null
		          		  ? (date.substring(0, 10) + " at "
		                            + procedureStaffScheduleDto.getFromTime())
		                    : (date.substring(0, 10) + " at "
		                            + procedureStaffScheduleDto.getFromTime()));
				if(doctorId != null && doctorId != 0) {
					emailinfo.setAdmitingDoctorId(doctorId);
				}
				
				if(employeeId != null && employeeId != 0) {
					emailinfo.setEmployeeId(employeeId);
				}
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.SURGERY_RESCHEDULING, emailinfo);
			}
			}
			
		/*	//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String DoctorRole = "";
		switch(procedureStaffScheduleDto.getRoleTypeId()) {
			
			case 10 : 
				
				DoctorRole = "OT Manager";
				break;
				
			case 12 : 
				
				DoctorRole = "Admin";
				break;
			case 8 : 
				
				DoctorRole = "Surgeon";
				break;
				
			case 9 : 
				
				DoctorRole = "Anaesthetist";
				break;
			case 11 : 
				
				DoctorRole = "Nurse";
				break;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = simpleDateFormat.format(GlobalCommonDateUtils.getDate(procedureStaffScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
			
			Integer RescheduleOrNot = 0;
			String message = "Dear "+DoctorRole;
			String mailSubject = "";
			if(procedureSchedulingDto.getRescheduleOrNot().equals(RescheduleOrNot)) {
				message+="<br> <br> <br>  You have surgery scheduled on "+date+" at "+procedureStaffScheduleDto.getFromTime()
						+ " for <b>"+procedureSchedulingDto.getProcedureName()+"</b> for <b>"+procedureSchedulingDto.getPatientName()+"</b> at OT  "+procedureSchedulingDto.getOtType()+""
						+ "<br> <br> <br>  Thank you ";
				mailSubject = " OT Scheduling ";
			}else {
				
				String datdfbe = simpleDateFormat.format(GlobalCommonDateUtils.getDate(procedureSchedulingDto.getOldDate(), "dd-M-yyyy HH:mm:ss"));
				
				message+="<br> <br> <br>  A Surgery scheduled on "+datdfbe+" at "+procedureSchedulingDto.getOldTime()
				+ " for <b>"+procedureSchedulingDto.getProcedureName()+"</b> for <b>"+procedureSchedulingDto.getPatientName()+"</b> at OT  "+procedureSchedulingDto.getOtType()+" "
				+ " been rescheduled on "+date+" at "+procedureStaffScheduleDto.getFromTime() 
				+" for <b>"+procedureSchedulingDto.getProcedureName()+"</b> for <b>"+procedureSchedulingDto.getPatientName()+"</b> at OT "+procedureSchedulingDto.getOtType()
				+ "<br> <br> <br>  Thank you ";
				mailSubject = " OT Rescheduling ";
				
			}
		
			//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(procedureStaffScheduleDto.getEmailId()));
			mimeMessage.setSubject(mailSubject);
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);*/
			//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendSchedulingNotification failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	public static void main(String[] args) {
	
		MailingServiceImpl obj = new MailingServiceImpl();
		ProcedureStaffScheduleDto procedureStaffScheduleDto = new ProcedureStaffScheduleDto();
		ProcedureSchedulingDto procedureSchedulingDto = new ProcedureSchedulingDto();
		procedureStaffScheduleDto.setEmailId("shital@clinivantage.com");
		procedureStaffScheduleDto.setFromTime("08:00:00");
		procedureStaffScheduleDto.setProdeureDate("10-12-2019 00:00:00");
		procedureStaffScheduleDto.setRoleId(3);
		
		procedureSchedulingDto.setProcedureName("Test Procedure,Knee Replacement");
		procedureSchedulingDto.setPatientName("namita M Patil");
		procedureSchedulingDto.setOldDate("2029-12-02");
		procedureSchedulingDto.setOldTime("09:00:00");
		procedureSchedulingDto.setOtType("Test1");
		procedureSchedulingDto.setRescheduleOrNot(1);
		obj.sendSchedulingNotification(procedureStaffScheduleDto,procedureSchedulingDto);
	}

	@Override
	public Response sendSchedulingConcelledNotification(ProcedureStaffScheduleDto procedureStaffScheduleDto,
			OTReservationDto dto) throws ApplicationException {
		try{
			
			Integer doctorId = 0;
			Integer employeeId = 0;
			
			 doctorId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
					"select dm.doctor_id as \"doctorId\" from doctor.m_doctor_master dm where dm.user_id =:userId")
					.setInteger("userId", procedureStaffScheduleDto.getUserId()).uniqueResult();
			 
			 
			 employeeId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
						"select em.employee_id  as \"employeeId\" from public.m_employee_master em where em.user_id =:userId")
						.setInteger("userId", procedureStaffScheduleDto.getUserId()).uniqueResult();
				 
			 
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = procedureStaffScheduleDto.getProdeureDate();
			SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
			smsInfo.setDateTime(date != null
          		  ? (date.substring(0, 10) + " at "
                            + procedureStaffScheduleDto.getFromTime())
                    : (date.substring(0, 10) + " at "
                            + procedureStaffScheduleDto.getFromTime()));
				smsInfo.setOrganizationId(dto.getOrganizationId());
				smsInfo.setUnitId(dto.getUnitId());
				smsInfo.setPatientId(dto.getPatientId());
				smsInfo.setProceduceName(dto.getProcedureName());
				if(doctorId != null && doctorId != 0) {
					smsInfo.setAdmitingDoctorId(doctorId);
				}
				
				if(employeeId != null && employeeId != 0) {
					smsInfo.setEmployeeId(employeeId);
				}
			smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.SURGERY_CANCEL,smsInfo);
			
			if(procedureStaffScheduleDto.getEmailId()!=null && procedureStaffScheduleDto.getEmailId().length()>0) 
			{
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(dto.getUnitId());
				emailinfo.setOrganizationId(dto.getOrganizationId());
				emailinfo.setPatientEmail(procedureStaffScheduleDto.getEmailId());
				emailinfo.setPatientId(dto.getPatientId());
				emailinfo.setProceduceName(dto.getProcedureName());
				emailinfo.setDateTime(date != null
		          		  ? (date.substring(0, 10) + " at "
		                            + procedureStaffScheduleDto.getFromTime())
		                    : (date.substring(0, 10) + " at "
		                            + procedureStaffScheduleDto.getFromTime()));
				if(doctorId != null && doctorId != 0) {
					emailinfo.setAdmitingDoctorId(doctorId);
				}
				
				if(employeeId != null && employeeId != 0) {
					emailinfo.setEmployeeId(employeeId);
				}
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.SURGERY_CANCEL, emailinfo);
			}
			
			
			/*//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String DoctorRole = "";
			switch(procedureStaffScheduleDto.getRoleTypeId()) {
			
			case 10 : 
				
				DoctorRole = "OT Manager";
				break;
				
			case 12 : 
				
				DoctorRole = "Admin";
				break;
			case 8 : 
				
				DoctorRole = "Surgeon";
				break;
				
			case 9 : 
				
				DoctorRole = "Anaesthetist";
				break;
			case 11 : 
				
				DoctorRole = "Nurse";
				break;
			}
		
			String message = "Dear "+DoctorRole;
			String mailSubject = "";
		
				message+="<br> <br> <br>  You have surgery scheduled on "+dto.getScheduleDate()+ " for <b>"
				+dto.getProcedureName()+"</b> for <b>"+dto.getPatientName()+"</b> at OT  "+dto.getTableName()+" been cancelled, please contact admin in case of rescheduling "
						+ "<br> <br> <br>  Thank you ";
				mailSubject = " OT Scheduling Cancellation";
		
			//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(procedureStaffScheduleDto.getEmailId()));
			mimeMessage.setSubject(mailSubject);
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);*/
			//System.out.println("Mail Sent successfully");
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendSchedulingConcelledNotification failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response sentRegistrationMailToPatient(PatientRegistrationDto dto)
			throws ApplicationException {
		try{
			//send procedure order patient education template to patient
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = "Dear "+dto.getFirstName() +" "+dto.getMiddleName()+" "+dto.getLastName()+", Greetings for Day!!!."
					+ "<br>"+ " Thank you for the choosing "+ sessionService.getCookieObjectDto().getUnitNme() +" "
							+ "for your Health Care Services provider.<br><br>"+CLINI_REG_SALUTATION;
			//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getEmail()));
			mimeMessage.setSubject("Patient Registration ");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sentRegistrationMailToPatient failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response sendMailInvite(String url, String mails, String date ,String time ,String remark)
			throws ApplicationException {
		try{
			//send procedure order patient education template to patient
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = "Hello "+", Greetings for Day!!!."
					+ "<br><br>"+ "Please find Invite link for the meeting dated:  "+ date +" at time:  "+ time +" "
					+ "<br><br>"+ remark +" "
					+ "<br>"	+ "Link :<a href="+url+">"+url+"</a>" 
					+ "<br>"+ " Thank you for the choosing "+ sessionService.getCookieObjectDto().getUnitNme() +" "
					+ "for your Health Care Services provider.<br><br>";
			
					AmazonMailDto amazonDto = new AmazonMailDto();
					amazonDto.setData(message);
					amazonDto.setFrom(mailDto.getId()!=null ? mailDto.getId() : "info@axonichealth.co.in");
					amazonDto.setSubject("Greetings From " + sessionService.getCookieObjectDto().getUnitNme());
					amazonDto.setTo(mails);
					Gson gson = new Gson();
					HttpHeaders headers = new HttpHeaders();
					headers.set("Content-Type", "application/json");
					org.springframework.http.HttpEntity<AmazonMailDto> request = new org.springframework.http.HttpEntity<>(amazonDto, headers);
					URI urlam = URI.create("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail");
					String stringResponse = restTemplate.postForObject(urlam, request, String.class);
					Response commonResponse = gson.fromJson(stringResponse, Response.class);
			
			/*Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mails));
			mimeMessage.setSubject("Meeting Invite");
			mimeMessage.setContent(message, "text/html");
			System.out.println(mimeMessage);
			Transport.send(mimeMessage);*/
			return new Response(EMAIL_SENT, null, null, null, url);	
		}catch(Exception e){
		logger.error("sendMailInvite failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response sendNotificationToDoctor(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try{
			//send procedure order patient education template to patient
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = SALUTATION ;
			message =  "Dear "+preOperativeDetailsDto.getDoctorName();
			//System.out.println("message"+message);
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(preOperativeDetailsDto.getEmail()));
			mimeMessage.setSubject("OT Emergency ");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendNotificationToDoctor failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response triggerEmail(String textMsg, String toEmailId,String mailSubject,String fileName,ByteArrayOutputStream report,MailDto mail) throws ApplicationException {
		try{
			//send procedure order patient education template to patient
			Properties props = new Properties();	
				props.put("mail.smtp.host", mail.getHost().trim());
		    	props.put("mail.smtp.socketFactory.port", mail.getPort().trim());
		    	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		    	props.put("mail.smtp.auth", "true");
		    	props.put("mail.smtp.port", mail.getPort().trim());
		    	props.put("mail.smtp.localhost", "127.0.0.1");
		    	props.put("mail.smtp.debug", "true");
			//MailDto mailDto = loadProperties();
			Session session = Session.getInstance(props,new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mail.getId().trim(), mail.getPassword().trim());
				}
			});
			
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mail.getId().trim()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmailId));
			 // Set Subject: header field
            mimeMessage.setSubject(mailSubject!=null ? mailSubject : "Mail Subject"); //mailSubject
            
            if(report!=null && report.size()>0) {
			  byte[] bytes = report.toByteArray();
	             
	            //construct the pdf body part
	            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

		            // Create the message part
		            BodyPart messageBodyPart = new MimeBodyPart();

		            // Now set the actual message
		           // messageBodyPart.setText(textMsg);
		            messageBodyPart.setContent(textMsg,"text/html");

		            // Create a multipar message
		            Multipart multipart = new MimeMultipart();

		            // Set text message part
		            multipart.addBodyPart(messageBodyPart);

		            // Part two is attachment
		            messageBodyPart = new MimeBodyPart();
		            
		            final MimeBodyPart attachmentPart = new MimeBodyPart();
		            
		            attachmentPart.setDataHandler(new DataHandler(dataSource));
		            attachmentPart.setFileName(fileName);
		            multipart.addBodyPart(attachmentPart);

		            // Send the complete message parts
		            mimeMessage.setContent(multipart);
            }else {
            	 mimeMessage.setContent(textMsg,"text/html");
            }
			
			Transport.send(mimeMessage);
			return new Response(EMAIL_SENT, "200", null, null, null);	
		}catch(Exception e){
		logger.error("triggerEmail failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response sendOutSourceMail(String patientEmail, String mailData) throws ApplicationException {
		try{
			//get authentication 
			MailDto mailDto = loadProperties();
			Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
				}
			});
			
			String message = SALUTATION ;
			
					message +=  mailData;
					
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(mailDto.getId()));
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(patientEmail));
			mimeMessage.setSubject("Pateint Out Source Test");
			mimeMessage.setContent(message, "text/html");
			Transport.send(mimeMessage);
			return new Response(EMAIL_SENT, null, null, null, null);	
		}catch(Exception e){
		logger.error("sendOutSourceMail failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	  }
	

	    @Override
		public Response sendFeedBackTemplateMailtoPatient(MailReqDto dto)throws ApplicationException{
		    try{
		  		MailDto mailDto = loadProperties();
					Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication(){
							return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
						}
					});  
		               
		            Message message = new MimeMessage(session);
		            BodyPart messageBodyPart = new MimeBodyPart();
		            String hospitalName="Clinivantage Healthcare";
		            String subject = "Feedback Request for your appointment with "  +dto.getDoctorName();
		            message.setFrom(new InternetAddress(SMTP_USER_ID,hospitalName));
		            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getpEmailId()));
		            message.setSubject(subject);
		            String message1 = "Dear " +dto.getPatientName()+ " , ";
		            String doctorName =dto.getDoctorName();
		            
		            if(dto.getEncounterId()!=null  && dto.getEncounterId()>0) {
		            	if(doctorName!=null)
		            		message1 += "<br>For your appointment with "+doctorName+" on "+dto.getAppointmentDate()+"";
		            	else
		            		message1 += "<br>For your appointment of "+dto.getSpecialityName()+" on "+dto.getAppointmentDate()+"";
		            }
		            else {
		            	if(doctorName!=null)
		            		message1 += "<br>For your admission with  "+doctorName+" on "+dto.getAppointmentDate()+"";
		            	else
		            		message1 += "<br>For your admission of "+dto.getSpecialityName()+" on "+dto.getAppointmentDate()+"";
		            }
		            message1 += " Please provide feedback.";
		            message1+="<br>"+dto.getFormTemplate().replace("\n", "<br>");
		            message1+="<br><br>Regards, ";
		            message1+="<br>Clinivantage Team ";
		             
		            messageBodyPart.setContent(message1 ,"text/html");
		            Multipart multipart = new MimeMultipart();
		            multipart.addBodyPart(messageBodyPart);
		            message.setContent(multipart);
		            Transport.send(message);
		            return new Response(EMAIL_SENT, null, null, null, null);
		    }catch(Exception e){
		     
		     logger.error("Exception occurred while sending file on mail due to : " + e.getCause() + " : " + e.getMessage());
		    }
		    return new Response(ERROR, null, null, null, null);	
		}


		@Override
		public Response sendTemplateMailtoPatient(MailReqDto dto) throws ApplicationException {
			 try{
		    		MailDto mailDto = loadProperties();
					Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication(){
							return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
						}
					});  
		                 
		              Message message = new MimeMessage(session);
		              BodyPart messageBodyPart = new MimeBodyPart();
		              String hospitalName="Clinivantage Healthcare";
		              
		              String subject = null;
		              if(dto.getSpecialityName()!=null)
		            	  subject =  "Information Request for your appointment for  "+dto.getSpecialityName();
		              else if(dto.getDoctorName()!=null)
		            	  subject =  "Information Request for your appointment with  "+dto.getDoctorName();
		              
		              message.setFrom(new InternetAddress(SMTP_USER_ID,hospitalName));
		              message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getpEmailId()));
		              message.setSubject(subject);
		              String message1 = "Dear " +dto.getPatientName()+ " , ";
		              
		              if(dto.getSpecialityName()!=null)
		              message1 += "<br>For your upcoming appointment for "+dto.getSpecialityName()+" on "+dto.getAppointmentDate()+" ,";
		              
		              else if(dto.getDoctorName()!=null)
		            	  message1 += "<br>For your upcoming appointment with "+dto.getDoctorName()+" on "+dto.getAppointmentDate()+" ,";
		            	  
		              message1 += "the doctor has recommended that you provide the following information before the consultation";
		              message1+="<br>"+dto.getFormTemplate().replace("\n", "<br>");
		              message1+="<br><br>Regards, ";
		              message1+="<br>Clinivantage Team ";
		               
		              messageBodyPart.setContent(message1 ,"text/html");
		              Multipart multipart = new MimeMultipart();
		              multipart.addBodyPart(messageBodyPart);
		              message.setContent(multipart);
		              Transport.send(message);
		              return new Response(EMAIL_SENT, null, null, null, null);
		      }catch(Exception e){
		       
		       logger.error("Exception occurred while sending file on mail due to : " + e.getCause() + " : " + e.getMessage());
		      }
		      return new Response(ERROR, null, null, null, null);	
		}


		@Override
		public Response sendTemplateFormToDoctor(MailReqDto dto) throws ApplicationException {
			try{
	    		MailDto mailDto = loadProperties();
				Session session = Session.getDefaultInstance(mailDto.getProperties(),new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication(){
						return new PasswordAuthentication(mailDto.getId(), mailDto.getPassword());
					}
				});  
	              Message message = new MimeMessage(session);
	              BodyPart messageBodyPart = new MimeBodyPart();
	              String hospitalName="Clinivantage Healthcare";
	              String subject = "Feedback response from "  +dto.getPatientName();
	              message.setFrom(new InternetAddress(SMTP_USER_ID,hospitalName));
	              message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(dto.getUnitEmailId()));
	              message.setSubject(subject);
	              String message1 = "Hello, ";
	              message1 += "Feedback response is given bellow. ";
	              message1+="<br>"+dto.getFormTemplate().replace("\n", "<br>");
	              message1+="<br><br>Regards, ";
	              message1+="<br>Clinivantage Team ";
	               
	              messageBodyPart.setContent(message1 ,"text/html");
	              Multipart multipart = new MimeMultipart();
	              multipart.addBodyPart(messageBodyPart);
	              message.setContent(multipart);
	              Transport.send(message);
	              return new Response(EMAIL_SENT, null, null, null, null);
	      }catch(Exception e){
	       
	       logger.error("Exception occurred while sending file on mail due to : " + e.getCause() + " : " + e.getMessage());
	      }
	      return new Response(ERROR, null, null, null, null);	
		}


		@Override
		public Response triggerBulkEmail(String message, String toEmailId, String mailSubject,
				List<BulkEmailDto> bulkmailList, MailDto mail) throws ApplicationException {
			try{
				//send procedure order patient education template to patient
				Properties props = new Properties();	
					props.put("mail.smtp.host", mail.getHost().trim());
			    	props.put("mail.smtp.socketFactory.port", mail.getPort().trim());
			    	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			    	props.put("mail.smtp.auth", "true");
			    	props.put("mail.smtp.port", mail.getPort().trim());
			    	props.put("mail.smtp.localhost", "127.0.0.1");
			    	props.put("mail.smtp.debug", "true");
				//MailDto mailDto = loadProperties();
				Session session = Session.getInstance(props,new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication(){
						return new PasswordAuthentication(mail.getId().trim(), mail.getPassword().trim());
					}
				});
				
				Message mimeMessage = new MimeMessage(session);
				mimeMessage.setFrom(new InternetAddress(mail.getId().trim()));
				mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmailId));
				 // Set Subject: header field
	            mimeMessage.setSubject(mailSubject!=null ? mailSubject : "Mail Subject"); //mailSubject
	            
	         // Create a multipar message
            	Multipart multipart = new MimeMultipart();
	            for (BulkEmailDto newDto : bulkmailList) {
		            if(newDto.getByteArrayOutputStream()!=null && newDto.getByteArrayOutputStream().size()>0) {
						  byte[] bytes = newDto.getByteArrayOutputStream().toByteArray();
				             
				            //construct the pdf body part
				            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

					            final MimeBodyPart attachmentPart = new MimeBodyPart();
					            
					            attachmentPart.setDataHandler(new DataHandler(dataSource));
					            attachmentPart.setFileName(newDto.getAttachmentFileName());
					            multipart.addBodyPart(attachmentPart);
					           
			            }
				}
	            // Create the message part
	            BodyPart messageBodyPart = new MimeBodyPart();

	            // Now set the actual message
	            messageBodyPart.setContent(message,"text/html");

	         // Send the complete message parts

	            // Set text message part
	            multipart.addBodyPart(messageBodyPart);

	            // Part two is attachment
	            messageBodyPart = new MimeBodyPart();
	            
	            mimeMessage.setContent(multipart);
				Transport.send(mimeMessage);
				return new Response(EMAIL_SENT, "200", null, null, null);	
			}catch(Exception e){
			logger.error("triggerBulkEmail failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}


		@Override
		public Response sendAutoMail(MessageQueueDto dto,List<Attachments> attachments,String mailProvider) throws ApplicationException {
			
				String mailRes = "";
			
				Email from = new Email(dto.getSmtpUserId());
			//	Email from = new Email("vishal@clinivantage.com");
                //String subject = dto.getMailSubject();
				//Email to = new Email( dto.getToMailId());
			    Content content = new Content("text/html",  dto.getMailContent());
			    
			  
              //  Mail mail = new Mail(from, subject, to, content);
			    Mail mail = new Mail();
			    mail.setFrom(from);
			    mail.setSubject(dto.getMailSubject());
			    mail.addContent(content);
			    
			    Personalization personalization = new Personalization();
			    Email to = new Email();
			    to.setEmail(dto.getToMailId());
			    personalization.addTo(to);
			    
			    if(dto.getBccEmailId()!=null) {
					Email bcc = new Email();
					bcc.setEmail(dto.getBccEmailId());
					personalization.addBcc(bcc);
			    }
			    mail.addPersonalization(personalization);
		/*
		 * Personalization p1 = new Personalization(); p1.addBcc(new
		 * Email(dto.getSmtpUserId(),dto.getSmtpUserId())); mail.addPersonalization(p1);
		 */
			    for (Attachments attachments2 : attachments) {
			    	mail.addAttachments(attachments2);
				}
			   
			    SendGrid sg = new SendGrid(mailProvider); //"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"
			    Request request = new Request();
			    try {
			      request.setMethod(Method.POST);
			      request.setEndpoint("mail/send");
			      request.setBody(mail.build());
			      com.sendgrid.Response response = sg.api(request);
			      Integer res =  response.getStatusCode();
			      return new Response(EMAIL_SENT, res.toString(), null, null, null);	
			    } catch(Exception e){
			    logger.error("sendAutoMail failed", e);

				mailRes = e.getMessage();
			}
		    return new Response(null, null, mailRes, null, null);
		}


		@Override
		public Response sendOTPMail(MessageQueueDto dto, String mailCredential, String OTP)
				throws ApplicationException {

			String mailRes = "";
		
			Email from = new Email(dto.getSmtpUserId());
		    String subject = "Registration OTP ";
		    Email to = new Email( dto.getToMailId());
		    Content content = new Content("text/plain", OTP);
		    
		    Mail mail = new Mail(from, subject, to, content);
		    SendGrid sg = new SendGrid(mailCredential); //"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"
		    Request request = new Request();
		    try {
		      request.setMethod(Method.POST);
		      request.setEndpoint("mail/send");
		      request.setBody(mail.build());
		      com.sendgrid.Response response = sg.api(request);
		      Integer res =  response.getStatusCode();
		      return new Response(EMAIL_SENT, res.toString(), null, null, null);	
		    } catch(Exception e){
		    logger.error("sendOTPMail failed", e);

			mailRes = e.getMessage();
		}
		    return new Response(null, null, mailRes, null, null);
		}


		@Override
		public Response sendMailForpostRoomRentAutoJobFail(String key, List<Integer> unitIds)
				throws Exception {

					try{	
						
						String pattern = "yyyy-MM-dd";
			        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			        	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitIds.get(0)));
						
					String Messg = "Batch job is not executed for " +unitIds.toString()+  " units on "+date+" date ";
					AmazonMailDto amazonDto = new AmazonMailDto();
					amazonDto.setData(Messg);
					amazonDto.setFrom("info@axonichealth.co.in");
					amazonDto.setSubject("Auto batch job status");
					amazonDto.setTo("admin@clinivantage.com");
					
					try {
					
					Gson gson = new Gson();
					HttpHeaders headers = new HttpHeaders();
					headers.set("Content-Type", "application/json");
					org.springframework.http.HttpEntity<AmazonMailDto> request = new org.springframework.http.HttpEntity<>(amazonDto, headers);
					URI url = URI.create("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail");
					String stringResponse = restTemplate.postForObject(url, request, String.class);
					Response commonResponse = gson.fromJson(stringResponse, Response.class);
					
					
					
					}catch(Exception e) {
					logger.error("sendMailForpostRoomRentAutoJobFail failed", e);
					}
			}catch(Exception e){
			logger.error("sendMailForpostRoomRentAutoJobFail failed", e);
			}
			return null;
		
		}
		
}


