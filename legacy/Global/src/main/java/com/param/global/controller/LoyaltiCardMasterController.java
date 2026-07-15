package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.LoyaltiCardMasterDto;
import com.param.global.service.ILoyaltiCardMasterService;
@RestController
@RequestMapping(value="/api/unit")
@SuppressWarnings({"rawtypes"})
public class LoyaltiCardMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(LoyaltiCardMasterController.class);

	@Autowired
	private ILoyaltiCardMasterService iLoyaltiCardMasterService;
	
	@RequestMapping(value = "/saveLoyaltiCardMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveLoyaltiCardMaster(@RequestBody LoyaltiCardMasterDto loyaltiCardMasterDto){
		
		try {
			return iLoyaltiCardMasterService.saveLoyaltiCardMaster(loyaltiCardMasterDto);

		} catch (Exception e) {
			logger.error("Error while saving loyalti card master", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateLoyaltiCardMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateLoyaltiCardMasterStatus(@RequestBody LoyaltiCardMasterDto loyaltiCardMasterDto){
		
		try {
			return iLoyaltiCardMasterService.updateLoyaltiCardMasterStatus(loyaltiCardMasterDto);

		} catch (Exception e) {
			logger.error("Error while updating loyalti card master status", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
		
	@RequestMapping(value = "/getLoyaltiCardMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoyaltiCardMasterList(@RequestBody LoyaltiCardMasterDto loyaltiCardMasterDto){

		try {
			return iLoyaltiCardMasterService.getLoyaltiCardMasterList(loyaltiCardMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching loyalti card master list", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getLoyaltiCardMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoyaltiCardMasterCount(@RequestBody LoyaltiCardMasterDto loyaltiCardMasterDto){
		
		try {
			return iLoyaltiCardMasterService.getLoyaltiCardMasterCount(loyaltiCardMasterDto);

		} catch (Exception e) {
			logger.error("Error while fetching loyalti card master count", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/loyaltyCardNumber/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getLoyaltiCardNumber(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId){
		
		try {
			return iLoyaltiCardMasterService.getLoyaltiCardNumber(orgId,unitId);

		} catch (Exception e) {
			logger.error("Error while fetching loyalti card number", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/checkLoyaltyCard/{orgId}/{unitId}/{cardNo}", method = RequestMethod.GET)
	public Response checkLoyaltycardNumber(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId,@PathVariable ("cardNo") String cardNo){
		
		try {
			return iLoyaltiCardMasterService.checkLoyaltycardNumber(orgId,unitId,cardNo);

		} catch (Exception e) {
			logger.error("Error while checking loyalti card number", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}

