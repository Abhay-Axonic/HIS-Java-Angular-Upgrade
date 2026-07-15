package com.param;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.commons.httpclient.URI;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

import in.param.exception.ApplicationException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
	import org.hibernate.Session;
	import org.hibernate.SessionFactory;
	import org.hibernate.transform.Transformers;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;
	import okhttp3.OkHttpClient;
	import okhttp3.Request;
	import okhttp3.RequestBody;
	import okhttp3.MediaType;
	import org.json.JSONArray;
	import org.json.JSONObject;
	import javax.crypto.Cipher;
	import java.security.KeyFactory;
	import java.security.PublicKey;
	import java.security.spec.X509EncodedKeySpec;
	import java.nio.charset.StandardCharsets;
	import java.util.Base64;

	@Service
	@SuppressWarnings({"rawtypes","unchecked"})
	public class AbdmCommonUtil implements ICommonConstants {

	    private static final Logger logger = Logger.getLogger(AbdmCommonUtil.class);

	    @Autowired
	    private SessionFactory sessionFactory;

	  @Transactional
	    public Response getPublicKey(AbdmCredentialdDto abdmCredentialdDto) {
	        try {
	        	
	        	UUID uuid = UUID.randomUUID();
				String uuidString = uuid.toString();
				Instant currentTime = Instant.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				String formattedTime = currentTime.atZone(java.time.ZoneOffset.UTC).format(formatter);
	            AbdmCredentialdDto dto = new  AbdmCredentialdDto();
	            dto.setEventId(EVENT_1);
	            Response masterRes = getAbdmUrlDetails(dto);
	            AbdmCredentialdDto master = (AbdmCredentialdDto) masterRes.getObject();
                String url=master.getBaseUrl() + master.getBasePath() + master.getApiUrl();
	            OkHttpClient client = new OkHttpClient().newBuilder().build();
	            Request request = new Request.Builder()
	                    .url(url)
	                    .method("GET", null)
	                    .addHeader("accept", "*/*")
	                    .addHeader("Accept-Language", "en-US")
	                    .addHeader("REQUEST-ID", uuidString)
						.addHeader("TIMESTAMP", formattedTime)
	                    .addHeader("Authorization", "Bearer " +abdmCredentialdDto.getAccessToken() + " ")
	                    .addHeader("X-CM-ID", abdmCredentialdDto.getXcmId())
	                    .build();

	            okhttp3.Response response = client.newCall(request).execute();
	            if (response.code() == 200) {
	                String publicKey = response.body().string();
	                JSONObject jsonObject1 = new JSONObject(publicKey);
	                String key = jsonObject1.getString("publicKey");
	                System.out.println("Key is: "+key);
				/*
				 * String key = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
				 * .replace("-----END PUBLIC KEY-----", "") .replaceAll("\\s", "");
				 */
	                return new Response(SUCCESS, SUCCESS_CODE, null, null, key);
	            }
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            throw new ServiceException(e.getMessage());
	        }
	        return new Response(ERROR, ERROR_CODE, "Connection Timeout", null, null);
	    }

	    @Transactional
	    public Response getCredentialDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException {
	        try {
	            AbdmCredentialdDto abdmCredentialdDtoswer = (AbdmCredentialdDto) sessionFactory.getCurrentSession()
	                    .createSQLQuery("select ddcds.client_id as \"clientId\", ddcds.client_secret as \"clientSecret\" ,ddcds.dev_url as \"devUrl\" ,"
	                            + " unit.unit_name as \"name\", "
	                            + " ddcds.healthidsbx_url as \"healthidsbxUrl\" , ddcds.dev_abdm_url as \"devAbdmUrl\" ,ddcds.phrsbx_url as \"phrsbxUrl\" , "
	                            + " ddcds.id as \"id\" , ddcds.alise_Name as \"aliseName\" , "
	                            + " ddcds.gateway_url as \"gatewayUrl\",  "
	                            + " ddcds.abhasbx_url as \"abhasbxUrl\" "
	                            + " from abdm.m_abdm_credential ddcds "
	                            + " inner join public.m_unit_master unit on  unit.unit_id = ddcds.unit_id "
	                            + " where ddcds.organization_id =:organizationId"
	                            + " and ddcds.unit_id =:unitId "
	                            + " and ddcds.status ='A'")
	                    .setInteger("unitId", abdmCredentialdDto.getUnitId())
	                    .setInteger("organizationId", abdmCredentialdDto.getOrganizationId())
	                    .setResultTransformer(Transformers.aliasToBean(AbdmCredentialdDto.class)).uniqueResult();

	            JSONArray jsonArray = new JSONArray();
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("clientId", abdmCredentialdDtoswer.getClientId());
	            jsonObject.put("clientSecret", abdmCredentialdDtoswer.getClientSecret());
	            jsonArray.put(jsonObject);

	            String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

	            OkHttpClient client = new OkHttpClient().newBuilder().build();
	            MediaType mediaType = MediaType.parse("application/json");
	            RequestBody body = RequestBody.create(mediaType, requestJson);
	            Request request = new Request.Builder()
	                    .url(abdmCredentialdDtoswer.getDevAbdmUrl() + "/v0.5/sessions")
	                    .method("POST", body)
	                    .addHeader("Content-Type", "application/json")
	                    .addHeader("Cookie", "TS011c04bd=01445fed043cb96b1a405b5d7d8778ec346ec2ed8c6f8860d192d25b6a6a052543bc06fe6815b487e9b1a863c3ac529b1615d19190")
	                    .build();
	            okhttp3.Response response = client.newCall(request).execute();
	            JSONObject jsonObject1 = new JSONObject(response.body().string());
	            String accessToken = jsonObject1.getString("accessToken");
	            abdmCredentialdDtoswer.setAccessToken(accessToken);

	            return new Response(SUCCESS, SUCCESS_CODE, null, null, abdmCredentialdDtoswer);
	        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	    }


	    @Transactional
	    public Response getAbdmUrlDetails(AbdmCredentialdDto abdmCredentialdDto) throws ApplicationException, Exception {
	        try {
	            Session session = sessionFactory.getCurrentSession();
	            String query = "select pr.base_url as \"baseUrl\" , pr.base_path  as \"basePath\" , pr.api_url  as \"apiUrl\" from abdm.m_url_master pr where pr.event_id ='" + abdmCredentialdDto.getEventId() + "' limit 1";

	            AbdmCredentialdDto dto = (AbdmCredentialdDto) session.createSQLQuery(query)
	                    .setResultTransformer(Transformers.aliasToBean(AbdmCredentialdDto.class)).uniqueResult();

	            return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            throw new ServiceException(e.getMessage());
	        }
	    }

	    @SuppressWarnings("unused")
	    public String encryptForV3(String value, String publicKeyString) throws Exception {
	        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
	        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

	       // Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", "BC");
	        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

	        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
	    }
	}

	


	
	
