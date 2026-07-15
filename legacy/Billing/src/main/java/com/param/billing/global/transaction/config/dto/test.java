package com.param.billing.global.transaction.config.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
public class test {
    
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
                    }
        RestTemplate restTemplate = new RestTemplate();
 
       
 
        try {
            String response = restTemplate.getForObject("https://smsgateway.center/SMSApi/rest/send?userId=nahartrans&password=Naharaccount123&senderId=NMCMUM&sendMethod=simpleMsg&msgType=TEXT&msg=Greetings%20from%20NAHAR%20MEDICAL%20CENTER%20,sneha%20Your%20unique%20registration%20number%20is%20uhid900%20For%20booking%20appointment%20call%209819260962&mobile=8766851366&duplicateCheck=true&format=json", String.class);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}