package com.param.global.unit.service;

import java.net.URI;
import java.util.*;

import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.utility.UserComponent;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.HttpMethod;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.MessageQueueDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.whatsAppRequestDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import in.param.exception.ApplicationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })

public class whatsAppServiceImpl  implements IwhatsAppServiceImpl, ICommonConstants {

	private static final Logger logger = Logger.getLogger(whatsAppServiceImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	
	@Override
	public Response sendWhatsAppMessage(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		 try {
			 RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				 
	              headers.add("authority", "api.maytapi.com");
	              headers.add("path", "/api/2329f77b-5116-477f-b21e-d0e718b3397d/3413/sendMessage");
	              headers.add("scheme", "https");
	              headers.add("accept", "application/json, text/javascript, */*");
	              headers.add("accept-encoding", "gzip, deflate, br");
	              headers.add("accept-language", "en-GB,en;q=0.9,en-US;q=0.8,en-AU;q=0.7,mr");
	              headers.add("content-type","application/json");
	              headers.add("dnt", "1");
	              headers.add("origin", "http://localhost:8080/HIS-WAR");
	              headers.add("sec-fetch-dest", "empty");
	              headers.add("sec-fetch-mode", "cors");
	              headers.add("sec-fetch-site", "cross-site");
	              headers.add("sec-fetch-mode", "cors");
	              headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
	              headers.add("x-maytapi-key", "c2f83596-8815-4c11-bfd4-a0440b49b28b");
				HttpEntity<whatsAppRequestDto> request  = new HttpEntity<>( wDhatsappRequestDto, headers);
				URI url = URI.create("https://api.maytapi.com/api/2329f77b-5116-477f-b21e-d0e718b3397d/3413/sendMessage");
				String stringResponse = restTemplate.postForObject(url, request, String.class);
	            
	             }catch(Exception e)
	            {
	                 logger.error("sendWhatsAppMessage failed", e);
	            }
		 return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

	}

	@Override
    public Response sendWhatsAppMessageTwilio(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		       try {
		       
		       //get credential W
		       UnitMasterDto unitMasterDto = (UnitMasterDto)sessionFactory.getCurrentSession()
						.createSQLQuery("select wtcr.account_sid as \"accountSid\", "
		                          +  " wtcr.auth_token as \"authToken\", "
								+ " wtcr.from_number as \"fromNumber\" "
		                        + " from public.m_whatsapp_credential wtcr" 
								+ " where wtcr.organization_id =:organizationId"
		                        + " and wtcr.unit_id =:unitId "
		                        + " and wtcr.type_of =:typeOf ")
							.setInteger("unitId", wDhatsappRequestDto.getUnitId())
							.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
							.setInteger("typeOf", wDhatsappRequestDto.getTypeOf())
						.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
				
		       if(wDhatsappRequestDto.getMediaUrl() != null) {
		    	   
		    	   //working code
					Twilio.init(unitMasterDto.getAccountSid(), unitMasterDto.getAuthToken());
					
					 Message message1 = Message.creator(
				                new com.twilio.type.PhoneNumber("whatsapp:"+wDhatsappRequestDto.getTo_number()),
				                new com.twilio.type.PhoneNumber(unitMasterDto.getFromNumber()),
				                wDhatsappRequestDto.getMessage())
							 .create();
					Message message = Message.creator(
			                new com.twilio.type.PhoneNumber("whatsapp:"+wDhatsappRequestDto.getTo_number()),
			                new com.twilio.type.PhoneNumber(unitMasterDto.getFromNumber()),
			                
			                Arrays.asList(URI.create(wDhatsappRequestDto.getMediaUrl())))
			            .create();
					 
					 return new Response(SUCCESS, SUCCESS_CODE, null, null, message);
		       }else {
		    	   return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		       }
		      
			  
	             }catch(Exception e)
	            {
	                 
	logger.error(e.getMessage(), e);
	                 return new Response(ERROR, ERROR_CODE, null, null, null);
	            }
		}
	
	@Override
	public Response sendWhatsAppMessageVonage(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		 try {
			 
			 
			 UnitMasterDto unitMasterDto = (UnitMasterDto)sessionFactory.getCurrentSession()
						.createSQLQuery("select wtcr.account_sid as \"accountSid\", "
		                          +  " wtcr.auth_token as \"authToken\", "
								+ " wtcr.from_number as \"fromNumber\" "
		                        + " from public.m_whatsapp_credential wtcr" 
								+ " where wtcr.organization_id =:organizationId"
		                        + " and wtcr.unit_id =:unitId"
								+ " and wtcr.type_of =:typeOf ")
							.setInteger("unitId", wDhatsappRequestDto.getUnitId())
							.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
							.setInteger("typeOf", wDhatsappRequestDto.getTypeOf())
						.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			 
			    String username = unitMasterDto.getAccountSid();
				String passward = unitMasterDto.getAuthToken();
				
		
				String appKeySecret = username + ":" + passward;
				byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
				String encoded = Base64.getEncoder().encodeToString(bytes);
				
				
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("from", unitMasterDto.getFromNumber());
				jsonObject.put("to", wDhatsappRequestDto.getTo());
				jsonObject.put("message_type", "text");
				jsonObject.put("text", wDhatsappRequestDto.getText());
				jsonObject.put("channel",  "whatsapp");
				jsonArray.put(jsonObject);

				String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

				OkHttpClient client = new OkHttpClient();
//				String url = "https://messages-sandbox.nexmo.com/v1/messages"; sand-box URL AND below production
				String url = "https://api.nexmo.com/v1/messages";
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, requestJson);
				Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
						.addHeader("authorization", "Basic " + encoded)
						.addHeader("cache-control", "no-cache").build();

				okhttp3.Response response = client.newCall(request).execute();
				
					 return new Response(SUCCESS, SUCCESS_CODE, null, null, response);
			       
	             }catch(Exception e)
	            {
	                 
	logger.error(e.getMessage(), e);
	                 return new Response(ERROR, ERROR_CODE, null, null, null);
	            }

	}
	
	
	@Override
    public Response authontificationKeyGenerationForHeloAPI(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		       try {
		       
		       //get credential W
		    	   whatsAppRequestDto dto = (whatsAppRequestDto)sessionFactory.getCurrentSession()
						.createSQLQuery("select wtcr.account_sid as \"accountSid\", "
		                          +  " wtcr.auth_token as \"authToken\", "
		                          +  " wtcr.api_key as \"apiKey\" "
		                        + " from public.m_whatsapp_credential wtcr" 
								+ " where wtcr.organization_id =:organizationId"
		                        + " and wtcr.unit_id =:unitId "
		                        + " and wtcr.type_of =3 ")
							.setInteger("unitId", wDhatsappRequestDto.getUnitId())
							.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(whatsAppRequestDto.class)).uniqueResult();
				
		       String username = dto.getAccountSid();
				String passward = dto.getAuthToken();
				
		
				String appKeySecret = username + ":" + passward;
				byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
				String encoded = Base64.getEncoder().encodeToString(bytes);
				
				
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("apiKey", dto.getApiKey());
				jsonArray.put(jsonObject);

				String requestJson =  jsonArray.toString().replaceAll("[\\[\\]]", "");

				OkHttpClient client = new OkHttpClient();
				String url = "https://api-whatsapp.helo.ai/helowhatsapp/api/users/authorize";
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, requestJson);
				Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
						.addHeader("authorization", "Bearer " + encoded).addHeader("cache-control", "no-cache").build();

				okhttp3.Response response = client.newCall(request).execute();
				
				JSONObject jsonObject1 = new JSONObject(response.body().string());
				JSONObject jsonObjectData = new JSONObject(jsonObject1.getString("data"));
				String c = jsonObjectData.getString("apiToken");
				String msg = jsonObject1.getString("msg");
				String code = jsonObject1.getString("code");
					 return new Response(SUCCESS, code, null, null, c,msg);
		      
			  
	             }catch(Exception e)
	            {
	                 
	logger.error(e.getMessage(), e);
	                 return new Response(ERROR, ERROR_CODE, null, null, null);
	            }
		}

	@Override
    public Response sendWhatsAppMessageHelo(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		       try {
		    	   
		    	   whatsAppRequestDto dto = (whatsAppRequestDto)sessionFactory.getCurrentSession()
							.createSQLQuery("select wtcr.template_id as \"templateId\", "
									+ " wtcr.from_number as \"fromNumber\" "
			                        + " from public.m_whatsapp_credential wtcr" 
									+ " where wtcr.organization_id =:organizationId"
			                        + " and wtcr.unit_id =:unitId "
			                        + " and wtcr.type_of =3 ")
								.setInteger("unitId", wDhatsappRequestDto.getUnitId())
								.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(whatsAppRequestDto.class)).uniqueResult();
		    	   
		   		JSONArray jsonArray = new JSONArray();
		   		JSONObject jsonObject = new JSONObject();
		   		//language
		   		JSONObject language = new JSONObject();
		   		String a ="deterministic";
		   		String b ="en";
		   		language.put("policy", a);
		   		language.put("code", b);
		   	//COMPONENET
		   	//COMPONENET
		   		JSONArray components = new JSONArray();
		   		JSONObject statusArray = new JSONObject();
		   		JSONArray parameters = new JSONArray();
		   		JSONObject statusObh = new JSONObject();
		   		JSONObject statusObh1 = new JSONObject();
		   		JSONObject statusObh2 = new JSONObject();
		   		
		   		/*List<MessageQueueDto> list =  new ArrayList<MessageQueueDto>();
		   		Response res =  iMessageQueueDao.getMessageFromQueqe(type);
		   		list.addAll(res.getListObject());
		   		for (MessageQueueDto messageQueueDto : list) {
		   			statusObh.put("type", "text");
			   		statusObh.put("text", messageQueueDto.getp);
		   		}*/
		   		statusObh.put("type", "text");
		   		statusObh.put("text", "sneha");
		   		
		   		statusObh1.put("type", "text");
		   		statusObh1.put("text", "UHID");
		   		
		   		statusObh2.put("type", "text");
		   		statusObh2.put("text", "http://mrrch.com");
		   		
		   		parameters.put(statusObh);
		   		parameters.put(statusObh1);
		   		parameters.put(statusObh2);
		   		statusArray.put("parameters", parameters);
		   		statusArray.put("type", "body");
		   		components.put(statusArray);
		   		
		   		// template
		   		JSONObject template = new JSONObject();
		   		String c = dto.getTemplateId();
		   		template.put("templateId", c);
		   		template.put("language", language);
		   		template.put("components", components);
		   		template.put("components", components);
		   		
		   		   // whatsApp
		   				JSONObject whatsApp = new JSONObject();
		   				String d ="template";
		   				String e =dto.getFromNumber();
		   				whatsApp.put("contentType", d);
		   				whatsApp.put("from", e);
		   				whatsApp.put("template", template);
		   			//main Json
		   			JSONArray channels = new JSONArray();
		   			channels.put("whatsapp");
		   				String f="IN";
		   				String g=wDhatsappRequestDto.getTo();
		   				jsonObject.put("to", g);
		   				jsonObject.put("countryCode", f);
		   				jsonObject.put("whatsapp", whatsApp);
		   				jsonObject.put("channels", channels);
		   		
//		   		String	vv = wDhatsappRequestDto.getText().toString().replaceAll("\\", "");
		   		jsonArray.put(jsonObject);

		   		String requestJson = wDhatsappRequestDto.getText();
		   		OkHttpClient client = new OkHttpClient().newBuilder()
		   			  .build();
		   			MediaType mediaType = MediaType.parse("application/json");
		   			RequestBody body = RequestBody.create(mediaType,requestJson);
		   			Request request = new Request.Builder()
		   			  .url("https://api-whatsapp.helo.ai/helowhatsapp/api/chat/v1/messages/single")
		   			  .method("POST", body)
		   			  .addHeader("Authorization",wDhatsappRequestDto.getResponseJson())
		   			  .addHeader("Content-Type", "application/json")
		   			  .build();
		   			okhttp3.Response response = client.newCall(request).execute();
				JSONObject jsonObject1 = new JSONObject(response.body().string());
				return new Response(SUCCESS, SUCCESS_CODE, null, null, response);
		      
	             }catch(Exception e)
	            {
	                 
	logger.error(e.getMessage(), e);
	                 return new Response(ERROR, ERROR_CODE, null, null, null);
	            }
		}
	
	@Override
    public Response sendWhatsAppMessageHeloBulkMessages(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		       try {
		    	   
		    	   whatsAppRequestDto dto = (whatsAppRequestDto)sessionFactory.getCurrentSession()
							.createSQLQuery("select wtcr.template_id as \"templateId\", "
									+ " wtcr.from_number as \"fromNumber\" "
			                        + " from public.m_whatsapp_credential wtcr" 
									+ " where wtcr.organization_id =:organizationId"
			                        + " and wtcr.unit_id =:unitId "
			                        + " and wtcr.type_of =3 ")
								.setInteger("unitId", wDhatsappRequestDto.getUnitId())
								.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(whatsAppRequestDto.class)).uniqueResult();
		    		
			   		JSONArray jsonArray = new JSONArray();
			   		
			   		//Json 1
			   		JSONObject jsonObject1 = new JSONObject();
			   		//language
			   		JSONObject language = new JSONObject();
			   		String a ="deterministic";
			   		String b ="en";
			   		language.put("policy", a);
			   		language.put("code", b);
			   		
			   		// template
			   		JSONObject template = new JSONObject();
			   		String c = dto.getTemplateId();
			   		template.put("templateId", c);
			   		template.put("language", language);
			   		   // whatsApp
			   				JSONObject whatsApp = new JSONObject();
			   				String d ="template";
			   				String e =dto.getFromNumber();
			   				whatsApp.put("contentType", d);
			   				whatsApp.put("from", e);
			   				whatsApp.put("template", template);
			   				
			   			//main Json
			   			JSONArray channels = new JSONArray();
			   			channels.put("whatsapp");
			   			
			   				String f="IN";
			   				String g=wDhatsappRequestDto.getTo();
			   				jsonObject1.put("to", g);
			   				jsonObject1.put("countryCode", f);
			   				jsonObject1.put("whatsapp", whatsApp);
			   				jsonObject1.put("channels", channels);
			   		
			   		  		//Json 2
			   		   		JSONObject jsonObject2 = new JSONObject();
			   		   		
			   	
			   		   		   // whatsApp
			   		   				JSONObject whatsApp2 = new JSONObject();
			   		   			String text = "hello";
			   		   		    String customOne = "customOne";
			   		   		    String customTwo = "customTwo";
			   		   		    String customThree = "three";
			   		   		    String customFour = "customFour";
			   		   		    String d1 ="text";
			   		        	  whatsApp2.put("from", e);
			   		   			  whatsApp2.put("contentType", d1);
			   		   		      whatsApp2.put("text", text);
			   		   		      whatsApp2.put("customOne", customOne);
			   		   		      whatsApp2.put("customTwo", customTwo);
			   		   		      whatsApp2.put("customThree", customThree);
			   		   		      whatsApp2.put("customFour", customFour);
			   		   				
			   		   				
			   		   			//main Json
			   		   			
				   		   			jsonObject2.put("to", g);
				   		   		    jsonObject2.put("countryCode", f);
				   		   		    jsonObject2.put("whatsapp", whatsApp2);
				   		   		    jsonObject2.put("channels", channels);
				   		   		    
				   		   	//Json 3
					   		   		JSONObject jsonObject3 = new JSONObject();
					          	//location
							   		   	JSONObject location = new JSONObject();
							   		    double l1 = 7.4954884;
								   		double l2 = 51.5005765;
								   		location.put("longitude", l1);
								   		location.put("latitude", l2);
								   	     String d2 ="location";
					   		   		   // whatsApp
					   		   				JSONObject whatsApp3 = new JSONObject();
					   		        	  whatsApp3.put("from", e);
					   		   			  whatsApp3.put("contentType", d2);
					   		   			  whatsApp3.put("location", location);
					   		   		      whatsApp3.put("customOne", customOne);
					   		   		      whatsApp3.put("customTwo", customTwo);
					   		   		      whatsApp3.put("customThree", customThree);
					   		   		      whatsApp3.put("customFour", customFour);
					   		   				
					   		   				
					   		   			//main Json
					   		   			
						   		   			jsonObject3.put("to", g);
						   		   		    jsonObject3.put("countryCode", f);
						   		   		    jsonObject3.put("whatsapp", whatsApp3);
						   		   		    jsonObject3.put("channels", channels);
						   		   	
						   		   	//Json 4
							   		   		JSONObject jsonObject4 = new JSONObject();
							          	//location
									   		   	JSONObject media = new JSONObject();
									   		    String type = "image";
									   		    String url = "https://i.ibb.co/kXgjphY/925869358s.png";
									   		    String caption = "viva connect";
									   		    media.put("type", type);
									   		    media.put("url", url);
									   		    media.put("caption", caption);
										   	     String d3 ="media";
							   		   		   // whatsApp
							   		   				JSONObject whatsApp4 = new JSONObject();
							   		        	  whatsApp4.put("from", e);
							   		   			  whatsApp4.put("contentType", d3);
							   		   			  whatsApp4.put("media", media);
							   		   		      whatsApp4.put("customOne", customOne);
							   		   		      whatsApp4.put("customTwo", customTwo);
							   		   		      whatsApp4.put("customThree", customThree);
							   		   		      whatsApp4.put("customFour", customFour);
							   		   				
							   		   				
							   		   			//main Json
							   		   			
								   		   			jsonObject4.put("to", g);
								   		   		    jsonObject4.put("countryCode", f);
								   		   		    jsonObject4.put("whatsapp", whatsApp4);
								   		   		    jsonObject4.put("channels", channels);
			   				
			   		//array
			   		jsonArray.put(jsonObject1);
			   		jsonArray.put(jsonObject2);
			   		jsonArray.put(jsonObject3);
			   		jsonArray.put(jsonObject4);

			   		String requestJson = jsonArray.toString();

			   		
			   		OkHttpClient client = new OkHttpClient().newBuilder()
			   			  .build();
			   			MediaType mediaType = MediaType.parse("application/json");
			   			RequestBody body = RequestBody.create(mediaType,requestJson);
			   			Request request = new Request.Builder()
			   			  .url("https://api-whatsapp.helo.ai/helowhatsapp/api/chat/v1/messages/bulk")
			   			  .method("POST", body)
			   			  .addHeader("Authorization",wDhatsappRequestDto.getResponseJson())
			   			  .addHeader("Content-Type", "application/json")
			   			  .build();
			   			okhttp3.Response response = client.newCall(request).execute();

					JSONObject jsonObjectx = new JSONObject(response.body().string());
					return new Response(SUCCESS, SUCCESS_CODE, null, null, response);
		      
	             }catch(Exception e)
	            {
	                 
	logger.error(e.getMessage(), e);
	                 return new Response(ERROR, ERROR_CODE, null, null, null);
	            }
		}

	@Override
	public Response sendWhatsAppMessagePinbot(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException {
		try {
			whatsAppRequestDto dto = (whatsAppRequestDto)sessionFactory.getCurrentSession()
				.createSQLQuery("select wtcr.template_id as \"templateId\", "
					+ " wtcr.from_number as \"fromNumber\", "
					+ " wtcr.account_sid as \"accountSid\", "
					+ " wtcr.auth_token as \"authToken\", "
					+ " wtcr.api_key as \"apiKey\" "
					+ " from public.m_whatsapp_credential wtcr "
					+ " where wtcr.organization_id =:organizationId "
					+ " and wtcr.unit_id =:unitId "
					+ " and wtcr.type_of = 4 ")
				.setInteger("unitId", wDhatsappRequestDto.getUnitId())
				.setInteger("organizationId", wDhatsappRequestDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(whatsAppRequestDto.class)).uniqueResult();

			String[] concentValuesList = null;

			JSONObject json = new JSONObject();
			JSONObject templateObject = new JSONObject();
			JSONObject languageObject = new JSONObject();
			JSONObject documentObject = new JSONObject();
			JSONArray componentsArray = new JSONArray();
			JSONObject componentObject = new JSONObject();
			JSONArray whatsAppValuesArray = new JSONArray();
			JSONArray headerParamsArray = new JSONArray();
			JSONObject bodyObject = new JSONObject();
			JSONObject headerObject = new JSONObject();

			json.put("messaging_product", "whatsapp");
			json.put("preview_url", false);
			json.put("recipient_type", "individual");
			json.put("to", wDhatsappRequestDto.getTo());
			json.put("type", "template");

			templateObject.put("name", wDhatsappRequestDto.getWhatsAppTemplateName() != null ? wDhatsappRequestDto.getWhatsAppTemplateName() : "");
			languageObject.put("code", "en");
			templateObject.put("language", languageObject);

			if (wDhatsappRequestDto.getText() != null && !wDhatsappRequestDto.getText().isEmpty()) {
				concentValuesList = wDhatsappRequestDto.getText().split(",");
			}

			if (concentValuesList != null) {
				for (String value : concentValuesList) {
					if(value != null && !(value.trim()).isEmpty()){
						JSONObject parameterObject = new JSONObject();
						parameterObject.put("type", "text");
						parameterObject.put("text", value.trim());
						whatsAppValuesArray.put(parameterObject);
					}
				}
			}

			if(wDhatsappRequestDto.getWhatsAppMessageType() != null && wDhatsappRequestDto.getWhatsAppMessageType().equalsIgnoreCase("text")){

				componentObject.put("type", "body");

				componentObject.put("parameters", whatsAppValuesArray);
				componentsArray.put(componentObject);

				templateObject.put("components", componentsArray);
			}
			else{
				String documentURL = wDhatsappRequestDto.getMediaUrl() != null ? wDhatsappRequestDto.getMediaUrl() : "";

				documentObject.put("link", documentURL);

				JSONObject documentParam = new JSONObject();

				documentParam.put("type", "DOCUMENT");
				documentParam.put("document", documentObject);
				headerParamsArray.put(documentParam);

				headerObject.put("type", "header");
				headerObject.put("parameters", headerParamsArray);

				bodyObject.put("type", "body");
				bodyObject.put("parameters", whatsAppValuesArray);

				componentsArray.put(headerObject);
				componentsArray.put(bodyObject);
				templateObject.put("components", componentsArray);
			}

			json.put("template", templateObject);

			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, json.toString());
			System.out.println("Json for whatsapp request : "+ json.toString());

			Request request = new Request.Builder()
				.url("https://partners.pinbot.ai/v1/messages")
				.method("POST", body)
				.addHeader("Content-Type", "application/json")
				.addHeader("wanumber", dto.getFromNumber())
				.addHeader("apikey", dto.getApiKey())
				.build();

			okhttp3.Response response= client.newCall(request).execute();
			String responseBody = response.body().string();

//			if (response.isSuccessful()) {
//				System.out.println("Response: " + responseBody);
//			} else {
//				System.err.println("Request failed with status code: " + response.code());
//			}

			if(responseBody.contains("wa_id")){
				return new Response(SUCCESS, SUCCESS_CODE, null, null, response);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}

	@Override
	public Response sendOTPOverMail(String email, String otpResult) throws ApplicationException, IOException {
		
		JSONObject json = new JSONObject();
		String message =  "Hi  Dear Patron, <br>   <br>Greetings from Axonic health.  <br> <br> OTP for reset password is: "+otpResult.toString()+"<br> <br> Sincerely your <br> Team Axonic health ";

		try {
			json.put("to", email);
			json.put("from", "info@axonichealth.com");
			json.put("subject", " Reset Password OTP");
			json.put("data", message);
		
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, json.toString());
		System.out.println("Json for whatsapp request : "+ json.toString());

		Request request = new Request.Builder()
			.url("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail")
			.method("POST", body)
			.addHeader("Content-Type", "application/json")
			.build();

		okhttp3.Response response= client.newCall(request).execute();
		String responseBody = response.body().string();
		return new Response(EMAIL_SENT, null, responseBody, null, null);
		} catch (JSONException e) {
			logger.error("sendOTPOverMail failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response sendOrgCreationResponse(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException, IOException {
		String videoLink= null;
		JSONObject json = new JSONObject();
		OrganizationMasterDto organizationUserDetailsVideo = (OrganizationMasterDto) sessionFactory
				.getCurrentSession()
				.createSQLQuery("select um.app_id as  \"appId\",um.video_name as \"videoName\", um.video_url as \"videoUrl\" from public.app_video_url um" +
						" where um.app_id=:appId limit 1")
				.setParameter("appId", organizationMasterForWebDto.getAppId())
				.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();

		String orgLink = "<a href=\"" + organizationMasterForWebDto.getCompleteSubDomainName() + "\" target=\"_blank\">"+organizationMasterForWebDto.getCompleteSubDomainName()+"</a>";
		if(organizationUserDetailsVideo.getVideoName()!= null && organizationUserDetailsVideo.getVideoUrl()!= null) {
		videoLink = "<a href=\"" + organizationUserDetailsVideo.getVideoUrl() + "\" target=\"_blank\">" + organizationUserDetailsVideo.getVideoName() + "</a>";
		}
//		String message =  "Hi  Dear "+organizationMasterForWebDto.getContactPerson()+", <br>   <br>Greetings from Axonic health.  <br> <br> You have successfully created Organization <b>"+organizationMasterForWebDto.getOrganizationName()+".</b><br>Below are your login credentials." +
//				"<br><b>UserName</b>: "+organizationMasterForWebDto.getUserName()+"<br><b>Temporary Password:</b>  "+organizationMasterForWebDto.getPassword()+"<br><b>URL</b> : "+orgLink+"<br> " +
//				"<br><b>Training Resources</b>:<br>" +
//				"<br><b>Videos</b>: "+videoLink+ "<br>" +
//				"<br>Sincerely your <br> Team Axonic health ";


		String message =  "Dear "+organizationMasterForWebDto.getContactPerson()+", <br>   <br>Greeting from Axonic Health! Thank you for choosing us to be your technology partner. We are excited to inform you that our deployment resource will reach out to you within the next 24 hours to assist you further.  <br> <br> In the meantime, please feel free to access the pre-recorded training videos and training document for learning purposes. This will help you get a head start on understanding the system and its functionalities.<br> <br><b>Organization Name: "+organizationMasterForWebDto.getOrganizationName()+".</b><br><br>Below are your login credentials-" +
				"<br><b>UserName</b>: "+organizationMasterForWebDto.getUserName()+"<br><b>Temporary Password:</b>  "+organizationMasterForWebDto.getPassword()+"<br><br><b>Organization Link</b> : "+orgLink+"<br> " +
				"<br><b>Training Resources</b>:<br>" ;
		if (organizationUserDetailsVideo.getVideoName() != null && organizationUserDetailsVideo.getVideoUrl() != null) {
			message += "<br><b>Videos</b>: " + videoLink + "<br>";
		}

		message += "<br>Sincerely,<br>Team Axonic Health";


		Map<String, String> awsConfig = UserComponent.getAwsConfig();
		String template = awsConfig.get("htmlTemplate");
		template = template.replaceAll("@ConfigHtmlContent",message);
		try {
			json.put("to", organizationMasterForWebDto.getEmailId());
			json.put("from", "info@axonichealth.com");
			json.put("subject", " New Org Creation");
			json.put("data", template);


			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, json.toString());
			System.out.println("Json for whatsapp request : "+ json.toString());

			Request request = new Request.Builder()
					.url("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();

			okhttp3.Response response= client.newCall(request).execute();
			String responseBody = response.body().string();
			return new Response(EMAIL_SENT, null, responseBody, null, null);
		} catch (JSONException e) {
			logger.error("sendOrgCreationResponse failed", e);
		}
		return null;
	}

	@Override
	public Response sendOrgCreationFailureResponse(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException, IOException {
		JSONObject json = new JSONObject();
		String message =  "We regret to inform you that the attempt to create the organization "+organizationMasterForWebDto.getOrganizationName()+" was unsuccessful due to an error in the process.<br>Please contact Admin for Further details.<br><br> Sincerely your <br> Team Axonic health";

		try {
			json.put("to", organizationMasterForWebDto.getOrganizationEmailId());
			json.put("from", "info@axonichealth.com");
			json.put("subject", " Organization Creation Failed");
			json.put("data", message);


			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, json.toString());
			System.out.println("Json for whatsapp request : "+ json.toString());

			Request request = new Request.Builder()
					.url("https://ojw0jjra11.execute-api.ap-south-1.amazonaws.com/prod/sendEmail")
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();

			okhttp3.Response response= client.newCall(request).execute();
			String responseBody = response.body().string();
			return new Response(EMAIL_SENT, null, responseBody, null, null);
		} catch (JSONException e) {
			logger.error("sendOrgCreationFailureResponse failed", e);
		}
		return null;
	}

}
