package com.param.global.mpesa.Controller;

import java.io.File;
import java.io.FileReader;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.PinPadDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;
import com.param.global.mpesa.dto.StkCallback;
import com.param.global.mpesa.service.IMpesaMasterServiceImpl;

@RestController
@RequestMapping(value="/Billing")
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class MpesaMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(MpesaMasterController.class);

	@Autowired
	IMpesaMasterServiceImpl iMpesaMasterServiceImpl;
	
	@Autowired
	private ServletContext servletContext;
	
	public static final String xmlFilePath = "C:\\Users\\ASUS\\Documents\\employee.json";
	
	@RequestMapping(value="/validateAmountByNumber", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateAmountByNumber(@RequestBody MpesaMasterDto mpesaMasterDto) {
		
		try {
			return iMpesaMasterServiceImpl.validateAmountByNumber(mpesaMasterDto);
		}
		catch(Exception e) {
			logger.error("validateAmountByNumber failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	@RequestMapping(value="/savePinPanDetaild", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePinPanDetaild(@RequestBody PinPadDetailsDto pinPadDetailsDto) {
		
		try {
		return iMpesaMasterServiceImpl.savePinPanDetaild(pinPadDetailsDto);
		}
		catch(Exception e) {
			logger.error("savePinPanDetaild failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	@RequestMapping(value="/updatePinpadDetails", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePinpadDetails(@RequestBody PinPadDetailsDto pinPadDetailsDto) {
		
		try {
		return iMpesaMasterServiceImpl.updatePinpadDetails(pinPadDetailsDto);
		}
		catch(Exception e) {
			logger.error("updatePinpadDetails failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	@RequestMapping(value="/requestSTKPushPayment", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response requestSTKPushPayment(@RequestBody MpesaTransctionDetailsDto mpesaTransctionDetailsDto) {
		
		try {
		return iMpesaMasterServiceImpl.requestSTKPushPayment(mpesaTransctionDetailsDto);
		}
		catch(Exception e) {
			logger.error("requestSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	/*@RequestMapping(value = "/requestSTKPushPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<MpesaTransctionDetailsDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, MpesaTransctionDetailsDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/
	
	@RequestMapping(value="/validateSTKPushPayment", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateSTKPushPayment(@RequestBody MpesaTransctionDetailsDto mpesaTransctionDetailsDto) {
		
		try {
		return iMpesaMasterServiceImpl.validateSTKPushPayment(mpesaTransctionDetailsDto);
		}
		catch(Exception e) {
			logger.error("validateSTKPushPayment failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	

	
	
	@RequestMapping(value = "/resultUrl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String mpesaResponse) {
		
		
		Response<JSONObject, Integer> response = null;
		try {
			
			JSONParser parser = new JSONParser();  
			JSONObject json = (JSONObject) parser.parse(mpesaResponse);  
			JSONObject body = (JSONObject) json.get("Body");
			
			JSONObject stkCallback = (JSONObject) body.get("stkCallback");
			StkCallback stkDto = new StkCallback();
			
			stkDto.setMerchantRequestID(stkCallback.get("MerchantRequestID").toString());
			stkDto.setCheckoutRequestID(stkCallback.get("CheckoutRequestID").toString());
			stkDto.setResultCode(stkCallback.get("ResultCode").toString());
			stkDto.setResultDesc(stkCallback.get("ResultDesc").toString());
			
			JSONObject CallbackMetadata = (JSONObject) stkCallback.get("CallbackMetadata");
			JSONArray doctorChargesArray = (JSONArray) CallbackMetadata.get("Item");
			
			JSONObject transactionObject = (JSONObject) doctorChargesArray.get(1);
			
			String s = (String) transactionObject.get("Value");
			stkDto.setTransactionId(s);
			
			if(stkDto.getResultCode().equals("0")) {
				  return iMpesaMasterServiceImpl.updateTransactionDate(stkDto);
			  }
			

			return null;
		} catch (Exception e) {
			logger.error("addSample failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@RequestMapping(value="/validationResultResponse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validationResultResponse(@RequestBody String mpesaResponse) {
		
		Response<JSONObject, Integer> response = null;
		try {
			
			JSONParser parser = new JSONParser();  
			JSONObject json = (JSONObject) parser.parse(mpesaResponse);  
			JSONObject body = (JSONObject) json.get("Result");
			
			StkCallback stkDto = new StkCallback();
			stkDto.setResultCode(body.get("ResultCode").toString());
			
			if(stkDto.getResultCode().equals("0")) {

				JSONObject ResultParameters = (JSONObject) body.get("ResultParameters");
				JSONArray ResultParameter = (JSONArray) ResultParameters.get("ResultParameter");
				
				JSONObject transactionObject = (JSONObject) ResultParameter.get(10);
				stkDto.setAmount((Double) transactionObject.get("Value"));
				
				
				JSONObject transactionId = (JSONObject) ResultParameter.get(12);
				stkDto.setTransactionId(transactionId.get("Value").toString());
				
				return iMpesaMasterServiceImpl.saveValidationData(stkDto);
				
			  }
			
			
			
			return null;

		}
		catch(Exception e) {
			logger.error("validationResultResponse failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	
	
	
}
