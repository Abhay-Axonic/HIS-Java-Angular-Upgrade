
package com.param.global.pesapal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;


public class PesapalIntegration {
	
	String callbackUrl="http://localhost:8080/HIS-WAR/#/pesapalProcessPage";
	private OAuthAccessor createOAuthAccessor(String reqUrl) {
		String consumerKey = System.getenv("PESAPAL_CONSUMER_KEY");
		String consumerSecret = System.getenv("PESAPAL_CONSUMER_SECRET");
		OAuthServiceProvider provider = new OAuthServiceProvider(reqUrl,
				reqUrl, reqUrl);
		OAuthConsumer consumer = new OAuthConsumer(callbackUrl, consumerKey,
				consumerSecret, provider);
		return new OAuthAccessor(consumer);
	}

	
	@SuppressWarnings("rawtypes")
	public OAuthMessage postPesapalRequest(String amount, String desc,
			String type, String reference, String email, String phonenumber,
			String first_name, String last_name,String callbackUrl,OAuthAccessor accessor) throws IOException,
			OAuthException, URISyntaxException {
		OAuthClient client = new OAuthClient(new HttpClient4());

		// construct pesapal xml
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

		// add other parameters
		Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
		List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(1)
				: new ArrayList<Map.Entry>(parameters);
		p.add(new OAuth.Parameter("pesapal_request_data", post_xml));
		p.add(new OAuth.Parameter("oauth_callback", callbackUrl));
		parameters = p;

		// make request
		//InputStream res=client.getRequestResponse(accessor, "GET", parameters).getBodyAsStream();
		//InputStream res=client.getRequestResponse(accessor, "GET", parameters).getBodyAsStream();
		   //client.getRequestResponse(accessor, "GET", parameters).URL.
		return client.getRequestResponse(accessor, "GET", parameters);
				//client.getRequestResponse(accessor, "GET", parameters);

	}

	
	@SuppressWarnings("rawtypes")
	public OAuthMessage PostPesapalDirectOrderV4(String post_xml)
			throws IOException, OAuthException, URISyntaxException {
		String reqUrl = "https://demo.pesapal.com/api/PostPesapalDirectOrderV4";
		OAuthAccessor accessor = createOAuthAccessor(reqUrl);
		OAuthClient client = new OAuthClient(new HttpClient4());
		post_xml = StringEscapeUtils.escapeXml(post_xml);
		// add other parameters
		Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
		List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(1)
				: new ArrayList<Map.Entry>(parameters);
		p.add(new OAuth.Parameter("pesapal_request_data", post_xml));
		p.add(new OAuth.Parameter("oauth_callback", callbackUrl));
		parameters = p;

		// make request
		return client.getRequestResponse(accessor, "GET", parameters);

	}


	@SuppressWarnings("rawtypes")
	public OAuthMessage getPaymentStatus(String reference, String trackingId,OAuthAccessor accessor)
			throws IOException, OAuthException, URISyntaxException {
		OAuthClient client = new OAuthClient(new HttpClient4());

		//reference = URLEncoder.encode(reference, "UTF-8");
		//trackingId = URLEncoder.encode(trackingId, "UTF-8");

		// add other parameters
		Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
		List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(1)
				: new ArrayList<Map.Entry>(parameters);
		p.add(new OAuth.Parameter("pesapal_merchant_reference", reference));
		p.add(new OAuth.Parameter("pesapal_transaction_tracking_id", trackingId));
		parameters = p;

		// make request
		return client.getRequestResponse(accessor, "GET", parameters);

	}

	
	@SuppressWarnings("rawtypes")
	public OAuthMessage getPaymentStatusByMerchant(String reference,OAuthAccessor accessor)
			throws IOException, OAuthException, URISyntaxException {
		String reqUrl = "https://demo.pesapal.com/API/QueryPaymentStatusByMerchantRef";
		//OAuthAccessor accessor = createOAuthAccessor(reqUrl);
		OAuthClient client = new OAuthClient(new HttpClient4());

		reference = URLEncoder.encode(reference, "UTF-8");

		// add other parameters
		Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
		List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(1)
				: new ArrayList<Map.Entry>(parameters);
		p.add(new OAuth.Parameter("pesapal_merchant_reference", reference));
		parameters = p;

		// make request
		return client.getRequestResponse(accessor, "GET", parameters);

	}

	
	@SuppressWarnings("rawtypes")
	public OAuthMessage getPesapalPaymentDetails(String reference, String trackingId,OAuthAccessor accessor)
			throws IOException, OAuthException, URISyntaxException {
		String reqUrl = "https://demo.pesapal.com/API/QueryPaymentDetails";

		//OAuthAccessor accessor = createOAuthAccessor(reqUrl);
		OAuthClient client = new OAuthClient(new HttpClient4());

		reference = URLEncoder.encode(reference, "UTF-8");
		trackingId = URLEncoder.encode(trackingId, "UTF-8");

		// add other parameters
		Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
		List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(1)
				: new ArrayList<Map.Entry>(parameters);
		p.add(new OAuth.Parameter("pesapal_merchant_reference", reference));
		p.add(new OAuth.Parameter("pesapal_transaction_tracking_id", trackingId));
		parameters = p;

		// make request
		return client.getRequestResponse(accessor, "GET", parameters);

	}

	
	@SuppressWarnings("rawtypes")
	public OAuthMessage getInstantPaymentNotification(String notificationType,
			String reference, String trackingId,OAuthAccessor accessor) throws IOException,
			OAuthException, URISyntaxException {

		if (notificationType == "CHANGE" && trackingId != "") {
			String reqUrl = "https://demo.pesapal.com/api/PostPesapalDirectOrderV4";
			//OAuthAccessor accessor = createOAuthAccessor(reqUrl);
			OAuthClient client = new OAuthClient(new HttpClient4());

			reference = URLEncoder.encode(reference, "UTF-8");
			trackingId = URLEncoder.encode(trackingId, "UTF-8");

			// add other parameters
			Collection<? extends Map.Entry> parameters = new ArrayList<Map.Entry>();
			List<Map.Entry> p = (parameters == null) ? new ArrayList<Map.Entry>(
					1) : new ArrayList<Map.Entry>(parameters);
			p.add(new OAuth.Parameter("pesapal_merchant_reference", reference));
			p.add(new OAuth.Parameter("pesapal_transaction_tracking_id",
					trackingId));
			parameters = p;

			// make request
			return client.getRequestResponse(accessor, "GET", parameters);
		} else {
			return null;
		}

	}

	
	
}
