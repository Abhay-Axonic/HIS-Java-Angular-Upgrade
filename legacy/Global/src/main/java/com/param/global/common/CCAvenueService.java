package com.param.global.common;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;


@SuppressWarnings({ "rawtypes", "unused", "unchecked","static-access" })
public class CCAvenueService {
	
	  private static String wsURL="https://api.ccavenue.com/apis/servlet/DoWebTrans";
	    //Production API URL:-https://login.ccavenue.com/apis/servlet/DoWebTrans
	    //Staging API URL:-https://180.179.175.17/apis/servlet/DoWebTrans
	  final static Logger logger = Logger.getLogger(CCAvenueService.class);
	  
	  public String callCCavenueStatusApi(String pXmlData,String pAccessCode,String pCommand,String aesKey,String pRequestType,String pResponseType,String pVersion, String apiUrl){
	      String vResponse="",encXMLData="",encResXML="",decXML="";
	      StringBuffer wsDataBuff=new StringBuffer();
	      try {
	          if(aesKey!=null && !aesKey.equals("") && pXmlData!=null && !pXmlData.equals("")){
	              AesUtil aesUtil=new AesUtil(aesKey);
	              encXMLData  = aesUtil.encrypt(pXmlData);
	          }
	          wsDataBuff.append("enc_request="+encXMLData+"&access_code="+pAccessCode+"&command="+pCommand+"&response_type="+pResponseType+"&request_type="+pRequestType+"&version"+pVersion);
	          vResponse = processUrlConnectionReq(wsDataBuff.toString(), apiUrl);
	          if(vResponse!=null && !vResponse.equals("")){
	            
	        	  Map hm=tokenizeToHashMap(vResponse, "&", "=");
	              encResXML = hm.containsKey("enc_response")?hm.get("enc_response").toString():"";
	              String vStatus = hm.containsKey("status")?hm.get("status").toString():"";
	              String vError_code = hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
	           
	              if(vStatus.equals("1")){//If Api call failed
	            	  logger.info("enc_response : "+ encResXML);
	            	  logger.error("error_code : "+ vError_code);
	                  return vResponse;
	              }
	              if(!encResXML.equals("")) {
	                  AesUtil aesUtil=new AesUtil(aesKey);
	                  decXML = aesUtil.decrypt(encResXML);
	                  logger.info("enc_response : "+decXML);                    
	                  return vResponse;
	              }
	          }
	      }
	      catch (Exception e) {
	    	  logger.info("enc_response : "+ e.getMessage());
	      }
		return null;
	  }
	  
	  public String generateInvoiceCCavenueApi(String pXmlData,String pAccessCode,String pCommand,String aesKey,String pRequestType,String pResponseType, String apiUrl){
	     
		  String vResponse="",encXMLData="",encResXML="",decXML="";
	      StringBuffer wsDataBuff=new StringBuffer();
	      try {
	          if(aesKey!=null && !aesKey.equals("") && pXmlData!=null && !pXmlData.equals("")){
	              AesUtil aesUtil=new AesUtil(aesKey);
	              encXMLData  = aesUtil.encrypt(pXmlData);
	          }
	          wsDataBuff.append("enc_request="+encXMLData+"&access_code="+pAccessCode+"&command="+pCommand+"&response_type="+pResponseType+"&request_type="+pRequestType);
	          vResponse = processUrlConnectionReq(wsDataBuff.toString(), apiUrl);
	          if(vResponse!=null && !vResponse.equals("")){
	        	  
	        	//  return vResponse;
				
				  Map hm=tokenizeToHashMap(vResponse, "&", "="); encResXML =
				  hm.containsKey("enc_response")?hm.get("enc_response").toString():""; String
				  vStatus = hm.containsKey("status")?hm.get("status").toString():""; String
				  vError_code =
				  hm.containsKey("enc_error_code")?hm.get("enc_error_code").toString():"";
				  
				  if(vStatus.equals("1")){//If Api call failed
				  logger.info("enc_response : "+ encResXML);
				  logger.error("error_code : "+ vError_code); return vResponse; }
				  if(!encResXML.equals("")) { AesUtil aesUtil=new AesUtil(aesKey); decXML =
				  aesUtil.decrypt(encResXML); 
				  logger.info("enc_response : "+decXML);
				  return vResponse; }
				 
	          }
	      }
	      catch (Exception e) {
	    	  logger.info("enc_response : "+ e.getMessage());
	      }
		return null;
	  }
		
		public  HashMap tokenizeToHashMap(String msg, String delimPairValue, String delimKeyPair){
		    HashMap keyPair = new HashMap();
		    ArrayList respList = new ArrayList();
		    String part = "";
		    StringTokenizer strTkn = new StringTokenizer(msg, delimPairValue,true);
		    while (strTkn.hasMoreTokens())
		    {
		        part = (String)strTkn.nextElement(); 
		        if(part.equals(delimPairValue)) {
		            part=null; 
		        }
		        else {
		            String str[]=part.split(delimKeyPair, 2);
		            keyPair.put(str[0], str.length>1?(str[1].equals("")?null:str[1]):null);
		        }
		        if(part == null) continue;
		        if(strTkn.hasMoreTokens()) strTkn.nextElement();
		    }        
		    return keyPair.size() > 0 ? keyPair : null;
		}

		public static String processUrlConnectionReq(String pBankData,String pBankUrl) throws Exception{URL	vUrl = null;
					URLConnection vHttpUrlConnection = null;
					DataOutputStream vPrintout = null;
					DataInputStream	vInput = null;
					StringBuffer vStringBuffer=null;
					vUrl = new URL(pBankUrl);
			
					if(vUrl.openConnection() instanceof HttpsURLConnection)
					{
						vHttpUrlConnection = (HttpsURLConnection)vUrl.openConnection();
					} 
					else if(vUrl.openConnection() instanceof HttpsURLConnection)
					{
						vHttpUrlConnection = (HttpsURLConnection)vUrl.openConnection();
					} else
					{
						vHttpUrlConnection = (URLConnection)vUrl.openConnection();
					}
					vHttpUrlConnection.setDoInput(true);
					vHttpUrlConnection.setDoOutput(true);
					vHttpUrlConnection.setUseCaches(false);
					vHttpUrlConnection.connect();
					vPrintout = new DataOutputStream (vHttpUrlConnection.getOutputStream());
					vPrintout.writeBytes(pBankData);
					vPrintout.flush();
					vPrintout.close();
					try {
						BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(vHttpUrlConnection.getInputStream()));
						vStringBuffer = new StringBuffer();
						String vRespData;
						while((vRespData = bufferedreader.readLine()) != null) 
							if(vRespData.length() != 0)
								vStringBuffer.append(vRespData.trim());
						bufferedreader.close();
						bufferedreader = null;
					}finally {  
						if (vInput != null)
							vInput.close();  
						if (vHttpUrlConnection != null)  
							vHttpUrlConnection = null;  
					}  
					return vStringBuffer.toString();
		}
			  
			  
	public static void main(String[] args) {
		
	    String pXmlData="<?xml version='1.0' encoding='UTF-8' standalone='yes'?><Order_Status_Query order_no= '19830084' reference_no=''/>";
	    String pAccessCode="AVIS57IK34AY45SIYA";//AVIS57IK34AY45SIYA
	    String aesKey="B363F6F03C432E3D899A6E326160A931";
	    String pCommand="orderStatusTracker";
	    String pRequestType="xml";
	    String pResponseType="xml";
	    String pVersion="1.2";
	    
	    String resStatus = new CCAvenueService().callCCavenueStatusApi(pXmlData, pAccessCode, pCommand, aesKey, pRequestType, pResponseType,pVersion,wsURL);
		
	    
	    String generatepXmlData="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
	    		"<Generate_Invoice_Query>" + 
	    		"<customer_name>Nitin</customer_name>" + 
	    		"<bill_delivery_type>EMAIL</bill_delivery_type>" + 
	    		"<customer_mobile_no>9370521829</customer_mobile_no>" + 
	    		"<customer_email_id>nitin@clinivantage.com" + 
	    		"</customer_email_id>" + 
	    		"<customer_email_subject>Test</customer_email_subject>" + 
	    		"<invoice_description>Test</invoice_description>" + 
	    		"<currency>INR</currency>" + 
	    		"<valid_for>2</valid_for>" + 
	    		"<valid_type>days</valid_type>" + 
	    		"<amount>1.0</amount>" + 
	    		"<merchant_reference_no>123456987123</merchant_reference_no>" + 
	    		"<sms_content>Pls pay your  bill  Invoice_ID for" + 
	    		"Invoice_CurrencyInvoice_Amountonlineat" + 
	    		"Pay_Link.</sms_content> </Generate_Invoice_Query>";
	    String generatepCommand="generateQuickInvoice";
	   String GenerateInvoiceCCavenueApi = new CCAvenueService().generateInvoiceCCavenueApi(generatepXmlData, pAccessCode, generatepCommand, aesKey, pRequestType, pResponseType,wsURL);
	    
		/*
		 * String encResXML =
		 * "66fdc7217c00dbf4d1680906aad0098882f8efefec1a91c65f133806fcb7c8c4db677528c1fe141e3fdaee7531f3c14783dcde97b9dfd45104b6ec11a0e7ef3f534d35655db7c005cee48265e02a83f1a8e3d9729cdf5ac7f8faa8655e55bf24e50a57e6982cf21701edf37c5602d7a0170ce065eb9aad87230e86123adf874da1b2d6d7151a6f809ce0526d1c2c303f5ad5f497ae111fe17e8f6dad06a64ba222425e0b64dfff57a7edd80d5575033cfbdbb1d380d90d77a0397e3416c2fa8a3779c8d45ffc52a04e18bb5a63748f50e7901b734353d2ad78e6b481d4c0fcc4614cf0b00feb5fc1da385b2693512246cadbd7fee34327fe02cb5ad424ff88c4b0ec46b2528bd534ee76f2dc3e32e7c3efb1763787fdde314a724de29d1ca1cfbbe0ccca94ae11747a034d6265675f5e63ef41ac587f9ba62d41982a73fb0f6da87100bcc184b01669854f6c89a9798e05d1c8e3f0f5f8c742a75ffbe82eac6b385add64dee8f70765c49832d0ad4251ad54066243f18e081e108b315d0048a7669ab778401633a971b78caf6295c2ab4d124508480d72f8a84a26a2617457b46774148541174182632b53ff02d7ebd382496e020282c7b695e8871faa2585401f51d54e53bd08dfcbec6eb537debb65d23e968ec1637d802ada8640734a6ec84895a36585792864541c20f1a88410866e8076b1fa505de444ff2b596c1c55fb967d0959c018c4885f39f85a086c78b1af71be6b382bc9e0e153c5b31bca8e29e6780d2aaecec161cc08f5d16482d2d0e6ca0fab69b5fc52dc97935bc7525b8514c69812e2a96f430825313bd553846137ed840f7144db3f020c4baeb2a735ff33841be871daf84424c6756888e9e037a4b68a7abcb32f53f8714825a932dba60b304d7f675d3935fb0db16ae0b80dd0cf406fdb477c540bd11e17f9c456f65e86eca92fd71d4a9542e5af53796c39d2af1e1322cc371d382fc0fdb5e15f636447215e326d5b8e881d6b5a7fcf02c39397022b06a6d70cd2b537177b534924dccf57efcb6a6c7bc7e4fe9ad8096f386e03023e5c1b14e8e9a1dd0f8cffc3e0cdfc7165b1b0c974cc1c7706527356629c8c195dc5cc50de3ecdade0318e9afdf2b3591b7bfac2e38937018f53e7968556454011f6667b6b4bc77583840a47076a1742dc0647e87b8d10bbae22d833b21566e622cbff92da4bd74ef57dad06377725f60680796fa5422480432a2a258f9fad159e951d1bc8173354e2152fa2beae547518d87eda673cc91d4078bb588e485206d95b30f70b33fc6650aca9967efd2afdc9b017d1936d2740862ea10703cb76062c783c4e4a1f6ec3a83b05f5337b842cbdbce2a9ce0cf5290e7e5b1147ac90160c6999438ea7f3d4829bf07821d92ebf7dd419fb5b7b6f71551ce8472b3c218b77c729718d20dd4a246d6a44639a4cc6fdaaad408142e25d3e58a7d39452371e4579941c5ba048667aac87dd95c3baea345472419710eca17791e9689e8fbc28b055669a147172cc19b669fb2e397f23d1c4049c44095f14ed67d145c2f45d896e813d1485c68492746d093b866ba314110c9232943835b3b079c5adfdff29c96fef7bac0ab2ad9ef7ae805e669059759d90e2670e3497f51532b267d401e4992c85220c5c0b0d34a2b016379b3db7de3e6f7dd50899d593112cd2c5358f9bd6e930436d74289955a78daff9124d7acda6ebd02e7bf8a8d36ea478ac8c4e4afa65b9b38579b41ac8289a39c866508d839b30f10715c2336c59f1883a4b904b3f2d743d217952ae3a764debbc72258d39fb38c619912b80d7abe45d6bc25c87497466be8252c840b17dfd70c181a3475b86c69950a4c397ca45bfdcd547d07f5aa63e18456f0b407cb500c3c2ccd5c909261b897b5b7d9a8cfe205545b0b250e50807d1c5bfce2deb77356d9c04a981858468744231ab825f7a3bd36e98bae888ab54190a262dbe4fc77d04e4ff9aac3a9a231c63324667af795c79b6c88172ae19bb3bda68293a925d650381802a76e173147494ba60dc78ba8161e001c4471f4fcfe0724f5a212f0fc851caf0328a119de276b1fe645c5ba29dc99c40c7b4abd3109f3791d215754a511912c372868f05bff0b29779e72dc6c0142b9c70481b6d99e51f782c76f5fa8d02ec841fd0ba52e5a63ad1ff0628b1a97690e53d5df67ecf7c8a2fd81b824f4534da7f68dbe90127fe16a875bdc461bb9f3c62245b16ce67fc6652609001175f63cc35e6e7d01d0e27f784393b98a4772fecf4cddcfc2340d2be7179f9ea2ad64629b95f9e2c88ed443d59c1173a3245b794066ff56b69cac5962b714aa673f1b24185f1eefe5da8f59fcd10441d625cf84709f1b4e12bf0eb146087ec78dd0d385410a3e979b033b2695f1019b7ed102cb3fdc70ad7bc62b42dd75d2292b74f1d43282698c06881b9ed754045b267f8171faa809673dd054af099ee554a95f935f6c753683edd64d07512fbd1ef07c146baab0a91350da8f33860bd381701cc1b09ac51db70507b6db89ea66dc881ce3015e2334dd5c617e376add7338523ccee7d45256bc38928711cf80f718e422fb2cc8ab5644002b0825ecc61bc08e62e685a94a2f01ee7a5fd527b685f3352b7361184f8420b2873909579935351b416117cdbc2aac85e4175ba2073b27aa492d9e65d7ea121ec87768ec935feb5e1084b204b66";
		 * String aesKey1="B363F6F03C432E3D899A6E326160A931"; AesUtil aesUtil=new
		 * AesUtil(aesKey1);
		 * 
		 * String decXML = aesUtil.decrypt(encResXML);
		 * System.out.println("enc_response : "+decXML);
		 */
		 	    
	    
	    
	}
		
}
