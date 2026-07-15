package com.param.global.common;


import org.apache.xmlbeans.impl.soap.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.XML;


@Service
@SessionAttributes("sessionObject")
public class MpesaUtility {

	private static final Logger logger = Logger.getLogger(MpesaUtility.class.getName());

	public static String callSoapWebService(String soapEndpointUrl, String soapAction,String transactionCode ,String senderMsisdn, String terminalMsisdn, String password) {
		String jsonPrettyPrintString="";
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction,transactionCode,senderMsisdn,terminalMsisdn,password), soapEndpointUrl);

			// Print the SOAP Response
			//System.out.println("Response SOAP Message:");

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			String strMsg = new String(out.toByteArray());

			JSONObject xmlJSONObj = XML.toJSONObject(strMsg);
			/*System.out.println("xmlJSONObj" + xmlJSONObj.getJSONObject("soapenv:Envelope").getJSONObject("soapenv:Body")
					.getJSONObject("ns1:retrieveTransactionResponse").getJSONObject("transaction").get("mpesaBalance"));*/

			 jsonPrettyPrintString = xmlJSONObj.getJSONObject("soapenv:Envelope").getJSONObject("soapenv:Body")
					.getJSONObject("ns1:retrieveTransactionResponse").getJSONObject("transaction").toString(4);

			//System.out.println(jsonPrettyPrintString);

			
			soapConnection.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "callSoapWebService failed", e);
		}
		return jsonPrettyPrintString;
	}

	private static SOAPMessage createSOAPRequest(String soapAction,String transactionCode,String senderMsisdn ,String terminalMsisdn,String password) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();

		createSoapEnvelope(soapMessage,transactionCode,senderMsisdn,terminalMsisdn,password);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("Content-Type", soapAction);
		headers.addHeader("Accept", "Application/xml");

		soapMessage.saveChanges();

		//System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		//System.out.println("\n");

		return soapMessage;
	}

	private static void createSoapEnvelope(SOAPMessage soapMessage ,String transactionCode,String senderMsisdn,String terminalMsisdn,String password) throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();

		String myNamespace = "ipn";
		String myNamespaceURI = "http://ipn";

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("retrieveTransaction", myNamespace);
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("transactionCode", myNamespace);
		soapBodyElem1.addTextNode(transactionCode);
		soapBodyElem1 = soapBodyElem.addChildElement("senderMsisdn", myNamespace);
		soapBodyElem1.addTextNode(senderMsisdn);
		soapBodyElem1 = soapBodyElem.addChildElement("terminalMsisdn", myNamespace);
		soapBodyElem1.addTextNode(terminalMsisdn);
		soapBodyElem1 = soapBodyElem.addChildElement("password", myNamespace);
		soapBodyElem1.addTextNode(password);
	}
	
	// SAAJ - SOAP Client Testing
		public static void main(String args[]) {

			
		}
}
