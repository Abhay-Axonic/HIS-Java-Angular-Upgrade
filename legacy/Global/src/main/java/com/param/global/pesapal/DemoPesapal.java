package com.param.global.pesapal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Base64;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import scala.metaprogramming.Mirror.Public;

public class DemoPesapal {

public static void main(String args[]) throws ClientProtocolException, IOException {
	
	  CloseableHttpClient httpClient = null;
	  httpClient = HttpClientBuilder.create().build();
	  String consumer_key = "GTUUncYasQ/WER9DCnKoSY4LfLEXePcE";
      String consumer_secret_key = "682ZJ7vy8YL4+u/fBx9Ag3fPbV8=";
      String appKey = consumer_key + ":" + consumer_secret_key;
      byte[] bytes = appKey.getBytes("ISO-8859-1");
      String encodedKey= Base64.getEncoder().encodeToString(bytes);
      
      String callbackUrl="http://localhost:8080/HIS-WAR/pesapalSuccessResponsePage";
     String oauth_signature_method="HMAC-SHA1";
     String reqUrl = "https://demo.pesapal.com/api/PostPesapalDirectOrderV4";
     callbackUrl = URLEncoder.encode(callbackUrl, "UTF-8");
        String amount = "1000.00";
		String desc = "descsdfds";
		String type = "MERCHANT";
		String reference = "1111";

		String email = "sidhu.lamture@gmail.com";
		String phonenumber = "0123456789";
		String first_name = "Sidheshwar";
		String last_name = "Lamture";
		
		String post_xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><PesapalDirectOrderInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" Amount=\""
				+ amount
				+ "\" Description=\""
				+ desc
				+ "\" Type=\""
				+ type
				+ "\" Reference=\""
				+ reference
				+ "\" FirstName=\""
				+ first_name
				+ "\" LastName=\""
				+ last_name
				+ "\" Email=\""
				+ email
				+ "\" PhoneNumber=\""
				+ phonenumber
				+ "\" xmlns=\"http://www.pesapal.com\" />";
		post_xml = StringEscapeUtils.escapeXml(post_xml);
    HttpGet  get = new HttpGet();
    get.addHeader("Content-Type","application/xml");
     get.addHeader("consumerKey", "GTUUncYasQ/WER9DCnKoSY4LfLEXePcE");
	 get.addHeader("oauth_signature_method", "HMAC-SHA1");
	 get.addHeader("consumerSecret", "682ZJ7vy8YL4+u/fBx9Ag3fPbV8=");
	 get.addHeader("oauth_nonce", "aF3Jd6");
	 get.addHeader("oauth_version", "1.0");
	 get.addHeader("timpstamp", "1572783102");
	 get.addHeader("pesapal_request_data",post_xml);
	 get.addHeader("oauth_callback",callbackUrl);

    HttpResponse result = httpClient.execute(get);
    HttpEntity resEntity = result.getEntity(); 
    
    
   
}

}
