package com.param.global.common;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Mpesa {

	public static String authenticate(String app_key2, String app_secret2) throws IOException {
		String app_key = app_key2;//"REDACTED";/* "REDACTED" */
		String app_secret = app_secret2 ;//"REDACTED";// REDACTED
		String appKeySecret = app_key + ":" + app_secret;
		byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
		String encoded = Base64.getEncoder().encodeToString(bytes);

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials").get()
				.addHeader("content-type", "application/json").addHeader("authorization", "Basic " + encoded)
				.addHeader("cache-control", "no-cache").build();

		okhttp3.Response response = client.newCall(request).execute();
		JSONObject jsonObject = new JSONObject(response.body().string());
		return jsonObject.getString("access_token");
	}

	public static okhttp3.Response STKPushSimulation(String businessShortCode, String password, String timestamp,
			String transactionType, String amount, String phoneNumber, String partyA, String partyB, String callBackURL,
			String accountReference, String transactionDesc, String app_key, String app_secret) throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("BusinessShortCode", businessShortCode);
		jsonObject.put("Password", password);
		jsonObject.put("Timestamp", timestamp);
		jsonObject.put("TransactionType", transactionType);
		jsonObject.put("Amount", amount);
		jsonObject.put("PhoneNumber", phoneNumber);
		jsonObject.put("PartyA", partyA);
		jsonObject.put("PartyB", partyB);
		jsonObject.put("CallBackURL", callBackURL);
		jsonObject.put("AccountReference", "test1");
// jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
		jsonObject.put("TransactionDesc", transactionDesc);

		jsonArray.put(jsonObject);

		String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

		OkHttpClient client = new OkHttpClient();
		String url = "https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestJson);
		Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
				.addHeader("authorization", "Bearer " + authenticate(app_key,app_secret)).addHeader("cache-control", "no-cache").build();

		okhttp3.Response response = client.newCall(request).execute();
		return response;
	}

	public static okhttp3.Response validateSTKPushSimulation(String businessShortCode, String password,
			String timestamp, String CheckoutRequestID, String app_key, String app_secret) throws IOException {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("BusinessShortCode", businessShortCode);
		jsonObject.put("Password", password);
		jsonObject.put("Timestamp", timestamp);
		jsonObject.put("CheckoutRequestID", CheckoutRequestID);
		jsonArray.put(jsonObject);

		String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

		OkHttpClient client = new OkHttpClient();
		String url = "https://api.safaricom.co.ke/mpesa/stkpushquery/v1/query";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestJson);
		Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
				.addHeader("authorization", "Bearer " + authenticate(app_key,app_secret)).addHeader("cache-control", "no-cache").build();

		okhttp3.Response response = client.newCall(request).execute();
		return response;
	}

	public static okhttp3.Response transactionStatus(String mpesaTxcode, String initiator, String statusResultURL, String validationPassword, String PartyA, String app_key, String app_secret) throws IOException {
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Initiator", initiator);
		jsonObject.put("SecurityCredential",validationPassword);
		jsonObject.put("CommandID", "TransactionStatusQuery");
		jsonObject.put("TransactionID", mpesaTxcode);
		jsonObject.put("PartyA", PartyA);
		jsonObject.put("IdentifierType", "4");
		jsonObject.put("ResultURL", statusResultURL);
		jsonObject.put("QueueTimeOutURL", statusResultURL);
		jsonObject.put("Remarks", "test");
		jsonObject.put("Occasion", "test");

		jsonArray.put(jsonObject);

		
		String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

		OkHttpClient client = new OkHttpClient();
		String url = "https://api.safaricom.co.ke/mpesa/transactionstatus/v1/query";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestJson);
		Request request = new Request.Builder().url(url).post(body).addHeader("content-type", "application/json")
				.addHeader("authorization", "Bearer " + authenticate(app_key,app_secret)).addHeader("cache-control", "no-cache").build();

		okhttp3.Response response = client.newCall(request).execute();

		JSONObject jsonObject1 = new JSONObject(response.body().string());

		return response;
	}
	
	
	
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
		
		//String appKeySecret = "rzp_test_anfj0dAFLonpUk" + ":" + "u6pZ9xhnyNCtRfnuaR4Ci9F7";
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
	
	
	

	public static void main(String[] args) throws IOException {
		Mpesa m = new Mpesa();

		//m.authenticate();
//m.tranc();

// Mpesa.STKPushSimulation("208158","MjA4MTU4ZDhhMTE3YzQzNDk5OTFjZTJhOGZjOWJkM2IzNTMxOTk5ZmJkZmRlNDBkMGJkZmIyM2IxZDkwMmQxN2VmOGQ0ZjIwMjIxMDA0MTUxMDIw","20190413123520","CustomerPayBillOnline","1","254708374149","254708374149","174379","https://be070c6d.ngrok.io/hooks/mpesa","re","re");

////System.out.println("Hello World!");


	}

}
