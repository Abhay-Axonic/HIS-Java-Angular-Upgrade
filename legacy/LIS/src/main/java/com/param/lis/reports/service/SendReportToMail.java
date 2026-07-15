package com.param.lis.reports.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class SendReportToMail {
	final static Logger logger = Logger.getLogger(SendReportToMail.class);
	 static final Properties props = new Properties();
	 static{
	  props.put("mail.smtp.host", "smtp.gmail.com");
	     props.put("mail.smtp.socketFactory.port", "465");
	     props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	     props.put("mail.smtp.auth", "true");
	     props.put("mail.smtp.port", "465");
	 }
	 
	 public static void sendMail(String email,String fileName,ByteArrayOutputStream report){
		  new Runnable() {
		   
		   @Override
		   public void run() {
		    try{
		     //get authentication 
		     Session session = Session.getDefaultInstance(props,new Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication(){
		       return new PasswordAuthentication("info@clinivantage.com", System.getenv("LIS_SMTP_PASSWORD"));
		      }
		     });
		     
		   
	            byte[] bytes = report.toByteArray();
	             
	            //construct the pdf body part
	            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
		     
		     
		     
		      // Create a default MimeMessage object.
		            Message message = new MimeMessage(session);

		            // Set From: header field of the header.
		            message.setFrom(new InternetAddress("info@clinivantage.com"));

		            // Set To: header field of the header.
		            message.setRecipients(Message.RecipientType.TO,
		               InternetAddress.parse(email));

		            // Set Subject: header field
		            message.setSubject("Lab Report");

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
		     
		     logger.error("Exception occurred while sending file on mail due to : " + e.getCause() + " : " + e.getMessage());
		    }
		   }
		  }.run();
		 }
	 
	/*  public static void writePdf(OutputStream outputStream) throws Exception {
	        Document document = new Document();
	        PdfWriter.getInstance(document, outputStream);
	         
	        document.open();
	         
	        document.addTitle("Test PDF");
	        document.addSubject("Testing email PDF");
	        document.addKeywords("iText, email");
	        document.addAuthor("Jee Vang");
	        document.addCreator("Jee Vang");
	         
	        Paragraph paragraph = new Paragraph();
	        paragraph.add(new Chunk("helloasdfsdafsdfsdfsdfsdfsdfdsf!"));
	        document.add(paragraph);
	         
	        document.close();
	    }
	  
	  public static void main(String[] args) {
		  SendReportToMail sendReportToMail = new SendReportToMail();
		  sendReportToMail.sendMail("ganesh@clinivantage.com", "abc.pdf", null);
		
	}
	 */
}
