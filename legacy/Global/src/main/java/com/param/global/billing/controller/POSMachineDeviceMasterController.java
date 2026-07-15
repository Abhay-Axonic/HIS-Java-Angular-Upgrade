package com.param.global.billing.controller;


import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.billing.service.IPOSMachineDeviceMasterService;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/api")
@SuppressWarnings({"rawtypes","unchecked"})
public class POSMachineDeviceMasterController implements ICommonConstants,IBillingStatusMasterConstant {

	final static Logger logger = Logger.getLogger(POSMachineDeviceMasterController.class);

	@Autowired
	private IPOSMachineDeviceMasterService iMosambeeDeviceMasterService;
	
	@Autowired
	private IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
	
	@RequestMapping(value = "/getPOSMachineDeviceMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPOSMachineDeviceMasterList(@RequestBody POSMachineDeviceMasterDto pOSMachineDeviceMasterDto){
		try{
			return iMosambeeDeviceMasterService.getPOSMachineDeviceMasterList(pOSMachineDeviceMasterDto);

		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/savePOSMachineDeviceMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePOSMachineDeviceMaster(@RequestBody POSMachineDeviceMasterDto pOSMachineDeviceMasterDto){
		try{
			return iMosambeeDeviceMasterService.savePOSMachineDeviceMaster(pOSMachineDeviceMasterDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/uploadToServerForPOSMachine" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response uploadToServerForPOSMachine(@RequestBody POSMachineDeviceMasterDto pOSMachineDeviceMasterDto){
		try{
			return iMosambeeDeviceMasterService.uploadToServerForPOSMachine(pOSMachineDeviceMasterDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/resultPosMachineUrl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String posMachineResponse) {
		
		
		Response<JSONObject, Integer> response = null;
		try {
			
			JSONParser parser = new JSONParser();  
			JSONObject json = (JSONObject) parser.parse(posMachineResponse);  

			POSMachineResponseMapperDto dto = new POSMachineResponseMapperDto();
//			String invoiceNum=json.get("invoiceNumber").toString();
//			dto.setCredCardHolderName(json.get("cardHolderName").toString());
//			dto.setCredCardTypeId(json.get("cardType").toString());
//			dto.setCredAmount(json.get("amount").toString());
//			dto.setCredCardNumber(json.get("cardNumber").toString());
//			dto.setTransactionNo(json.get("transactionId").toString());
//			dto.setCredExpiryDate(json.get("expiryDate").toString());
//			dto.setGeneratedReponse(json.toString());
//			String invoice=json.get("invoice").toString();
			dto.setRequestedUniqueId(json.get("billNumber").toString());
//			dto.setDeviceId(json.get("deviceId").toString());
//			dto.setDeviceId(json.get("deviceId")!= null ? json.get("deviceId").toString() :"1");
			dto.setGeneratedReponse(posMachineResponse);
//			if() {
				  return iPOSMachineResponseMapperDao.updatePosMachineTransactionResponse(dto);
//			  }
//

//			return null;
		} catch (Exception e) {
			logger.error("addSample failed", e);
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updatePOSMachineDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePOSMachineDetails(@RequestBody POSMachineDeviceMasterDto pOSMachineDeviceMasterDto){
		try{
			return iMosambeeDeviceMasterService.updatePOSMachineDetails(pOSMachineDeviceMasterDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
}
