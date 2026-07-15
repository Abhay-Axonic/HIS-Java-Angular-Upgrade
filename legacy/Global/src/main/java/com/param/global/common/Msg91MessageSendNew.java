package com.param.global.common;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.param.global.dto.MessageQueueDto;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked","rawtypes","unused"})
@Service
public class Msg91MessageSendNew {
	final static Logger logger = Logger.getLogger(Msg91MessageSendNew.class);

	
@Transactional	
public String msg91Postdata(String textMsg,String mobile,int orgId,int unitId,SessionFactory sessionFactory) {
	try {

		String queryStr = "select sms_type_json from t_unit_application_billing_configuration " +
				"where unit_id = :unitId and organization_id = :orgId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
		query.setParameter("unitId", unitId);
		query.setParameter("orgId", orgId);
		String msgTypeJson = (String) query.uniqueResult();
		Gson gson = new Gson();
	MessageQueueDto msg91dto = gson.fromJson(msgTypeJson,
			MessageQueueDto.class);
		
	 System.out.println("IN mSGGGG Type22222222 methoddddd");	
	String first =textMsg;	
	 String second = first.replace("@mobileNumber", "91"+mobile);
	 
	 HttpClient httpClient = HttpClients.createDefault();
       // Create HttpPost request
      // HttpPost httpPost = new HttpPost("https://control.msg91.com/api/v5/flow");
	 HttpPost httpPost = new HttpPost(msg91dto.getUrl());
       // Set request headers
       httpPost.setHeader("Content-Type", "application/json");
       //httpPost.setHeader("authkey", "249406AHnpqzvbM64808197P1");
       httpPost.setHeader("authkey", msg91dto.getAuthkey());

       // Create JSON payload
       String payload = second;
       // Set the payload as StringEntity
       StringEntity entity = new StringEntity(payload);
       httpPost.setEntity(entity);

       // Execute the request
       HttpResponse msg91Response = httpClient.execute(httpPost);

       // Get the response entity
       HttpEntity responseEntity = msg91Response.getEntity();

       // Convert the response entity to a string
       String responseBody = EntityUtils.toString(responseEntity);
       

       // Print response
       System.out.println("Response Code: " + msg91Response.getStatusLine().getStatusCode());
       System.out.println("Response: " + responseBody);
      return "success";
	}catch(Exception e) {
	logger.error("msg91Postdata failed", e);
	}

	return "";
	}
	
	
}
