package com.param.billing.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dto.CardTypeMasterDto;
import com.param.billing.global.service.ICardTypeMasterService;

@RestController
@RequestMapping("api/billing/")
@SuppressWarnings({"rawtypes","unchecked"})
public class CardTypeMasterController implements ICommonConstants{

	@Autowired
	private ICardTypeMasterService iCardTypeMasterService;
	
	@RequestMapping(value="cardTypeMaster",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveBankMasterList(@RequestBody CardTypeMasterDto cardTypeMasterDto) {
		try {

			return iCardTypeMasterService.gerActiveCardTypeMaster(cardTypeMasterDto);

		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveCardTypeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCardTypeMaster(@RequestBody CardTypeMasterDto cardTypeMasterDto){
		try{
			
			return iCardTypeMasterService.addCardTypeMaster(cardTypeMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getCardTypeMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCardTypeMasterList(@RequestBody CardTypeMasterDto cardTypeMasterDto){
		try{
			return iCardTypeMasterService.getCardTypeMasterList(cardTypeMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, null, null, null,null);
	}
	
	 
	@RequestMapping(value="/updateCardTypeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCardTypeMaster(@RequestBody CardTypeMasterDto cardTypeMasterDto){
		try{
			return iCardTypeMasterService.updateCardTypeMaster(cardTypeMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, null, null, null,null);
	}
	
	
	
	@RequestMapping(value="/updateStatusForCardType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForCardType(@RequestBody CardTypeMasterDto cardTypeMasterDto){
		try{
			
			return iCardTypeMasterService.updateStatusForCardType(cardTypeMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, null, null, null,null);
	}
}
