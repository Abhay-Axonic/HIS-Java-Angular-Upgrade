package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.service.IChargeConversionService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.ChargeConversionReqDto;


@RestController
@RequestMapping(value="unit/ChargeConversion")
@SuppressWarnings({"rawtypes","unchecked"})
public class ChargeConversionController  implements ICommonConstants{
	@Autowired
	private IChargeConversionService  iChargeConversionService;
	
	/**
	 * @author Suraj
	 * save Charge Conversion Details
	 * @param reqDto
	 * @return object
	 */
	@RequestMapping(value = "/saveChargeConversion" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveChargeConversion(@RequestBody ChargeConversionReqDto reqDto){
		try{
			return iChargeConversionService.saveChargeConversionMasterList(reqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	
}
