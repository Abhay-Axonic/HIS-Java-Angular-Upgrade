package com.param.global.pesapal;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pesapal.dto.PesapalPaymentDto;
import com.param.global.pesapal.model.PesaPalTransactionDetail;
import com.param.global.pesapal.service.IPesaPalIntegrationService;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;

@RestController
@RequestMapping("/api/global")
@SuppressWarnings("rawtypes")
public class PesaPalIntegrationController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(PesaPalIntegrationController.class);

	@Autowired
	private IPesaPalIntegrationService pesapalService;
	
	  @RequestMapping(value = "/callPesaPalPaymentRequest" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	  public  Response callPesapalPaymentRequest(@RequestBody PesapalPaymentDto reqDto) throws IOException 
	 {
	   try{
			String amount =reqDto.getAmount();
			String desc = "Appointment Payment";
			String type = "MERCHANT";
			String reference = reqDto.getTransactionReferenceId();
			String email = "rohanray@gmail.com";
			String phonenumber = reqDto.getPhoneNumber();
			String first_name = reqDto.getFirstName();
			String last_name = reqDto.getLastName();
		//System.out.println("request called");
		reqDto.setCallbackUrl(reqDto.getCallbackUrl());	
		reqDto.setRequestUrl("https://demo.pesapal.com/api/PostPesapalDirectOrderV4");
		Response res=pesapalService.getPesaPalAuthorizationData(reqDto);
		PesapalPaymentDto configResponseObj=(PesapalPaymentDto)res.getObject();
		
		if(res.getStatus().equals("error") || configResponseObj.getConsumerKey()==null || configResponseObj.getConsumerSecretKey()==null) {
			 reqDto.setErrorMessage("PesaPal Consumer and secret key not configured for unit");
			return new Response<>(ERROR, ERROR_CODE, "PesaPal Consumer and secret key not configured for unit", null, reqDto);
		}
		PesapalPaymentDto responseDto=(PesapalPaymentDto)res.getObject();
		OAuthAccessor accessor = createOAuthAccessor(reqDto.getRequestUrl(),responseDto.getConsumerKey(),responseDto.getConsumerSecretKey(),reqDto.getCallbackUrl());
		PesapalIntegration pesapal=new PesapalIntegration();
		OAuthMessage response=pesapal.postPesapalRequest(amount,
				desc, type, reference, email, phonenumber, first_name,
				last_name,reqDto.getCallbackUrl(),accessor);
		 String reponseBody=response.readBodyAsString();
	     String url=response.URL;
	   
	     //System.out.println("response url"+url);
	     if(reponseBody.contains("Problem: consumer_key_unknown | Advice: >  | ")) {
	    	 reqDto.setErrorMessage("IncorrectConsumerKey");
	    	 return new Response<>(ERROR, ERROR_CODE, null, null, reqDto);
	    }else if(reponseBody.contains("Problem: signature_invalid | Advice: >  | ")){
	    	 reqDto.setErrorMessage("IncorrectConsumerSecretKey");
	    	 return new Response<>(ERROR, ERROR_CODE, null, null, reqDto);
	    }else {
	     reqDto.setPesapalUrl(url);
	     Response saveReponse=pesapalService.savePesaPalPaymentRequestDetails(reqDto);
	     return new Response<>(SUCCESS, SUCCESS_CODE, url, null, reqDto);
	     }
	   }catch(Exception e)
	   {
		   logger.error("callPesapalPaymentRequest failed", e);
		   throw new ServiceException(e.getMessage());
	   }
	  }

	  @RequestMapping(value = "/getPesapayPaymentStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	  public  Response getPesapalPaymentStatus(@RequestBody PesapalPaymentDto reqDto) throws IOException 
	 {
	   try{
		   reqDto.setCallbackUrl(reqDto.getCallbackUrl());	
			reqDto.setRequestUrl("https://demo.pesapal.com/API/QueryPaymentStatus");
			
			Response authorizationResponse=pesapalService.getPesaPalAuthorizationData(reqDto);
			if(authorizationResponse.getStatus().equals("error")) {
				 reqDto.setErrorMessage("PesaPal Consumer and secret key not configured for unit");
				return new Response<>(ERROR, ERROR_CODE, "PesaPal Consumer and secret key not configured for unit", null, reqDto);
			}
			PesapalPaymentDto responseDto=(PesapalPaymentDto)authorizationResponse.getObject();
		
			if(reqDto.getTransactionReferenceId()==null) {
			   Response transRes=pesapalService.getTransactionReferenceByTransactionId(reqDto);
			   if(transRes.getStatus().equals("error")) {
				   reqDto.setTransactionStatus("INVALID");
				   return new Response<>(ERROR, ERROR_CODE, "Transaction Id is invalid", null, reqDto);
			   }
			   PesaPalTransactionDetail reqDto2=(PesaPalTransactionDetail)transRes.getObject();   
			   if(reqDto2.getTransactionStatus().equals("CONSUMED")){
				   reqDto.setTransactionStatus(reqDto2.getTransactionStatus());
				   return new Response<>(SUCCESS, SUCCESS_CODE, "Transaction Id is already consumed", null, reqDto);
			   }
			   reqDto.setTransactionReferenceId(reqDto2.getReferenceId());
		   }else {
			   Response transRes=pesapalService.getTransactionReferenceByTransactionId(reqDto);
			   if(transRes.getStatus().equals("error")) {
				   reqDto.setTransactionStatus("INVALID");
				   return new Response<>(ERROR, ERROR_CODE, "Transaction Id is invalid", null, reqDto);
			   }
			   PesaPalTransactionDetail reqDto2=(PesaPalTransactionDetail)transRes.getObject();
			   if(reqDto2.getTransactionStatus()!=null){
				   if(reqDto2.getTransactionStatus().equals("CONSUMED")){
				   reqDto.setTransactionStatus(reqDto2.getTransactionStatus());
				   return new Response<>(SUCCESS, SUCCESS_CODE, "Transaction Id is already consumed", null, reqDto);
				   }
			   }
		   }
		   //payment Status
		   PesapalIntegration pesapal=new PesapalIntegration();
		   OAuthAccessor accessor = createOAuthAccessor(reqDto.getRequestUrl(),responseDto.getConsumerKey(),responseDto.getConsumerSecretKey(),reqDto.getCallbackUrl());
		   OAuthMessage paymentStatusRes=pesapal.getPaymentStatus(reqDto.getTransactionReferenceId(),reqDto.getTransactionId(),accessor);
			String[] resStatus=paymentStatusRes.readBodyAsString().split("=");
			reqDto.setTransactionStatus(resStatus[1]);
			
			//get Payment status
			String requestUrlForStatus="https://demo.pesapal.com/API/QueryPaymentDetails";
			 OAuthAccessor accessor1 = createOAuthAccessor(requestUrlForStatus,responseDto.getConsumerKey(),responseDto.getConsumerSecretKey(),reqDto.getCallbackUrl());
			 OAuthMessage detailPaymentRes=pesapal.getPesapalPaymentDetails(reqDto.getTransactionReferenceId(),reqDto.getTransactionId(),accessor1);
			String[] detailResBody=detailPaymentRes.readBodyAsString().split(",");
				reqDto.setTransactionType(detailResBody[1]);
				
			Response pesapalResponse=pesapalService.updatePesapalTransactionStatus(reqDto);
			if(reqDto.getTransactionStatus().equals("INVALID")) {
				 return new Response<>(ERROR, ERROR_CODE, null, null, reqDto);
			}
			
			//if(reqDto.getTransactionStatus().equals("COMPLETED")) {
				Response transactionDetail=pesapalService.getTransactionDetailByTransactionId(reqDto);
				 PesaPalTransactionDetail transactionDetObj=(PesaPalTransactionDetail)transactionDetail.getObject();
				 reqDto.setAmount(transactionDetObj.getAmount().toString());
			//}
			
	    return new Response<>(SUCCESS, SUCCESS_CODE, null, null, reqDto);
	   }catch(Exception e)
	   {
		   logger.error("getPesapalPaymentStatus failed", e);
		   throw new ServiceException(e.getMessage());
	   }
	  }

	private OAuthAccessor createOAuthAccessor(String reqUrl,String consumerKey,String consumerSecret,String callbackUrl) {
		OAuthServiceProvider provider = new OAuthServiceProvider(reqUrl,
				reqUrl, reqUrl);
		OAuthConsumer consumer = new OAuthConsumer(callbackUrl, consumerKey,
				consumerSecret, provider);
		return new OAuthAccessor(consumer);
	}

	 
}
