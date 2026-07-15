package com.param.global.billing.service;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.amazonaws.util.Base64;
import com.param.global.billing.dao.IPOSMachineDeviceMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import javax.crypto.Mac;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class POSMachineDeviceMasterServiceImpl implements IPOSMachineDeviceMasterService, ICommonConstants,IBillingCommonConstants {

	final static Logger logger = Logger.getLogger(POSMachineDeviceMasterServiceImpl.class);

	@Autowired
	IPOSMachineDeviceMasterDao iPOSMachineDeviceMasterDao;
	
	@Autowired
	IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;

	 public static final int AES_KEY_SIZE = 256;
	 public static final int GCM_IV_LENGTH = 12;
	 public static final int GCM_TAG_LENGTH = 16;
	private Cache<Integer, List<POSMachineDeviceMasterDto>> POSMachineDeviceMasterCache;

	@PostConstruct
	public void initCache() {
		POSMachineDeviceMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	    
//	static String dto.getHmacKey() = "357538782F4125442A472D4B6150645367566B59703373367639792442264528";
//	static String nonKey = "17E8D2681BDD5DA5865E06F1";

//	static String partnercode = "MRRCH";

	@Override
	@Transactional
	public Response getPOSMachineDeviceMasterList(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		// TODO Auto-generated method stub
		List<POSMachineDeviceMasterDto> responseCache = POSMachineDeviceMasterCache.getIfPresent(pOSMachineDeviceMasterDto.getOrganizationId());
		if (responseCache != null && !responseCache.isEmpty()) {
			return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
		}
		Response res = iPOSMachineDeviceMasterDao.getPOSMachineDeviceMasterList(pOSMachineDeviceMasterDto);
		if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
			POSMachineDeviceMasterCache.put(pOSMachineDeviceMasterDto.getOrganizationId(), res.getListObject());

			return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
		}
		return res;
	}
	
	@Override
	@Transactional
	public Response savePOSMachineDeviceMaster(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		try {
			JSONParser parser = new JSONParser();  
			JSONObject jsonNew = (JSONObject) parser.parse(pOSMachineDeviceMasterDto.getSelectedPaymentModeJson());  
			
			POSMachineResponseMapperDto dto = new POSMachineResponseMapperDto();
			dto.setDeviceId(pOSMachineDeviceMasterDto.getPosMachineDeviceId().toString());
			dto.setIsConsumed(false);
			dto.setRequestedUniqueId(jsonNew.get("invoiceNo").toString());
			dto.setRequestedJson(pOSMachineDeviceMasterDto.getSelectedPaymentModeJson());
			dto.setTransactionAmount(pOSMachineDeviceMasterDto.getTransactionAmount());

			dto.setPatientId(pOSMachineDeviceMasterDto.getPatientId());
			dto.setCreatedBy(pOSMachineDeviceMasterDto.getCreatedBy());
			dto.setCreatedDate(pOSMachineDeviceMasterDto.getCreatedDate());
			dto.setUpdatedBy(pOSMachineDeviceMasterDto.getUpdatedBy());
			dto.setUpdatedDate(pOSMachineDeviceMasterDto.getUpdatedDate());
			dto.setUnitId(pOSMachineDeviceMasterDto.getUnitId());
			dto.setOrganizationId(pOSMachineDeviceMasterDto.getOrganizationId());
			dto.setStatus('A');
			
			Response res1 = iPOSMachineResponseMapperDao.getPOSMachineConfigDetails(dto);
			POSMachineResponseMapperDto newDto=(POSMachineResponseMapperDto)res1.getObject();
			JSONParser parser1 = new JSONParser();  
			JSONObject json = (JSONObject) parser.parse(newDto.getMachineConfigDetails());  
			dto.setHmacKey(json.get("hmacKey").toString());
			dto.setNonKey(json.get("nonKey").toString());
			dto.setPartnercode(json.get("partnercode").toString());
			pOSMachineDeviceMasterDto.setPartnercode(json.get("partnercode").toString());
			 String InitiateBody  = encrypt(pOSMachineDeviceMasterDto.getSelectedPaymentModeJson(), dto.getHmacKey(), dto.getNonKey());
		     String Json1 = "{\"transactionDetails\":\"" + InitiateBody + "\"" +
		                "}";       
		        
		        String initiateHmca = computeHMAC(Json1, dto.getHmacKey());
		        
		        pOSMachineDeviceMasterDto.setRequestedJsonString(Json1);
		        pOSMachineDeviceMasterDto.setRequestHmac(initiateHmca);
		        pOSMachineDeviceMasterDto.setUploadLink(json.get("uploadLink").toString());
	        	uploadToServerForPOSMachine(pOSMachineDeviceMasterDto);

		        if(pOSMachineDeviceMasterDto.getResponseResult().equals("failed")) {
		        	return new Response<>(ERROR, ERROR_CODE, pOSMachineDeviceMasterDto.getResponse(), null, null);
		        	 
		        }else {
					POSMachineDeviceMasterCache.invalidate(pOSMachineDeviceMasterDto.getOrganizationId());
		        	Response res=iPOSMachineResponseMapperDao.savePOSMachineUniqueId(dto);
		        	 return new Response<>(SUCCESS, SUCCESS_CODE, pOSMachineDeviceMasterDto.getResponse(), null, null);
		        }
		       
			 
		} catch (Exception e) {
			logger.error("savePOSMachineDeviceMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//NEW UPDATES
	public static String encrypt(String plainText, String secKey, String ivData) throws GeneralSecurityException {
		Cipher cipher = buildCipher(secKey, ivData, Cipher.ENCRYPT_MODE);
		byte[] cipherText = cipher.doFinal(plainText.getBytes());
		return byteToHexString(cipherText);
	}

	public static String decrypt(String encHexString, String secKey, String ivData) throws GeneralSecurityException {
		Cipher cipher = buildCipher(secKey, ivData, Cipher.DECRYPT_MODE);
		byte[] decryptedText = cipher.doFinal(hexToByteArray(encHexString));
		return new String(decryptedText);
	}

	private static Cipher buildCipher(String secretKey, String ivData, int cipherMode) throws GeneralSecurityException {

		SecretKeySpec keySpec = new SecretKeySpec(hexToByteArray(secretKey), "AES");
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, hexToByteArray(ivData));
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(cipherMode, keySpec, gcmParameterSpec);
		return cipher;
	}

	private static String byteToHexString(byte[] byteData) {
		StringBuilder stringBuilder = new StringBuilder(byteData.length * 2);
		for (byte hashByte : byteData) {
			stringBuilder.append(String.format("%02x", hashByte));
		}
		return stringBuilder.toString().toUpperCase();
	}

	private static byte[] hexToByteArray(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

	public static String computeHMAC(String message, String signatureKey) throws GeneralSecurityException {
		byte[] signatureKeyInByteArray = hexStringToByteArray(signatureKey);
		SecretKeySpec secretKeySpec = new SecretKeySpec(signatureKeyInByteArray, "HmacSHA512");
		Mac mac = Mac.getInstance("HmacSHA512");
		mac.init(secretKeySpec);
		return Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes()));
	}
	public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }



    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) +
                    Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

	@Override
	@Transactional
	public Response uploadToServerForPOSMachine(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		try {
			String query =pOSMachineDeviceMasterDto.getUploadLink();
			String json = pOSMachineDeviceMasterDto.getRequestedJson();
			String output="";
			StringBuilder stringBuilder = new StringBuilder();
			URL url = new URL(query);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setDoOutput(true);
			 conn.setRequestMethod("POST");
			 conn.setRequestProperty("Content-Type", "application/json");
			 conn.setRequestProperty("partnerCode", pOSMachineDeviceMasterDto.getPartnercode());
	         conn.setRequestProperty("hmac", pOSMachineDeviceMasterDto.getRequestHmac());
			 String input = pOSMachineDeviceMasterDto.getRequestedJsonString();
			 OutputStream os = conn.getOutputStream();
			 os.write(input.getBytes());
			 os.flush();
			 BufferedReader br = new BufferedReader(new InputStreamReader(
			 (conn.getInputStream())));
			 String response = null;
			 while ((output = br.readLine()) != null) {
			 stringBuilder.append(output);
			 JSONParser parser = new JSONParser();  
				JSONObject json1 = (JSONObject) parser.parse(output);  
				
				if(json1.get("result").toString().equals("success")) {
					response=json1.get("message").toString()+"("+" Device Identifier"+" "+":"+json1.get("deviceIdentifier").toString()+")";
				}else {
					response =json1.get("message").toString();
				}
				
				pOSMachineDeviceMasterDto.setResponseResult(json1.get("result").toString());
				pOSMachineDeviceMasterDto.setResponse(response);

			 }
				

				if(pOSMachineDeviceMasterDto.getResponse() != null) {
//					conn.disconnect();
					conn.disconnect();
					 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, pOSMachineDeviceMasterDto);
				}else {
					 pOSMachineDeviceMasterDto.setRequestedJsonString(null);
					 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

		} catch (Exception e) {
			logger.error("uploadToServerForPOSMachine failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePOSMachineDetails(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		try {
			Response res = iPOSMachineResponseMapperDao.checkGeneratedResponseAvailable(pOSMachineDeviceMasterDto);
			if(res.getListObject()!= null && res.getListObject().size() > 0) {
				 return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res);
			}else {
				 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
		} catch (Exception e) {
			logger.error("updatePOSMachineDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
