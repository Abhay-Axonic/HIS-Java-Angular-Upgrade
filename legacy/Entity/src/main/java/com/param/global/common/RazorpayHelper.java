package com.param.global.common;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Razorpay payment-link helper — extracted verbatim from Mpesa.java
 * (fix/dead-code-removal step 1) so the Razorpay flow does not depend on the
 * abandoned-in-baseline M-Pesa module. Sole caller: RazorPaymentController
 * (getOrderIdUnitSubscriptionPay). Creates a Razorpay payment link and returns
 * {paymentLinkId: shortUrl}. NOTE: the Razorpay key/secret come from the unit
 * config DTO; the empty placeholders in jdbc.properties are the dev state.
 */
public class RazorpayHelper {

	public static Map<String, String> RazorPaytransactionStatus(String key, String secretKey, String name, String email, String mobileNo,
			Integer amount, String callBackUrl, String referenceId, Long expireBy) throws IOException {
		
		Integer intAmount = amount.intValue();
	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("amount", intAmount);
		jsonObject.put("currency","INR");
		jsonObject.put("accept_partial", false);
		jsonObject.put("first_min_partial_amount", 100);
		jsonObject.put("expire_by", expireBy);
		jsonObject.put("reference_id", referenceId);
		jsonObject.put("description", "Payment for #"+referenceId);
		
		JSONObject jsonObjectCustmer = new JSONObject();
		jsonObjectCustmer.put("name", name);
		jsonObjectCustmer.put("contact","+91"+mobileNo);
		jsonObjectCustmer.put("email", email);
		jsonObject.put("customer", jsonObjectCustmer);
		

		JSONObject jsonObjectNotify = new JSONObject();
		jsonObjectNotify.put("sms", true);
		jsonObjectNotify.put("email",true);
		jsonObject.put("notify", jsonObjectNotify);
		
		jsonObject.put("reminder_enable", true);
		
		JSONObject jsonObjectNotes = new JSONObject();
		jsonObjectNotes.put("policy_name", "Clinivantage");
		jsonObject.put("notes", jsonObjectNotes);
		
		
		jsonObject.put("callback_url",callBackUrl);
		jsonObject.put("callback_method", "get");
		

		String requestJson = jsonObject.toString();
		
		String appKeySecret = key + ":" + secretKey;
		byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
		String encoded = Base64.getEncoder().encodeToString(bytes);
		
		OkHttpClient client = new OkHttpClient();
		String url = "https://api.razorpay.com/v1/payment_links";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestJson);
		Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
				.addHeader("authorization", "Basic " + encoded).addHeader("cache-control", "no-cache").build();

		okhttp3.Response response = client.newCall(request).execute();

		JSONObject jsonObject1 = new JSONObject(response.body().string());
		String s= jsonObject1.get("id").toString();
		Map<String,String> map = new HashMap<String, String>();
		String shortUrl= jsonObject1.get("short_url").toString();  
		map.put(s,shortUrl);    
		return map;
	}
}
