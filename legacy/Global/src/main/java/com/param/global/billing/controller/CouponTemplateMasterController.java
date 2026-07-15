package com.param.global.billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.CouponTemplateMasterDto;
import com.param.global.billing.service.ICouponTemplateMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/billing/coupon")
@SuppressWarnings({ "rawtypes" })
public class CouponTemplateMasterController implements ICommonConstants{

	
	@Autowired
	private ICouponTemplateMasterService iCouponTemplateMasterService;
	
	@RequestMapping(value = "/saveCouponTemplateMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCouponTemplateMaster(@RequestBody CouponTemplateMasterDto dto) {
		try {
			return iCouponTemplateMasterService.saveCouponTemplateMaster(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getCouponTemplateMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCouponTemplateMaster(@RequestBody CouponTemplateMasterDto dto) {
		try {
			return iCouponTemplateMasterService.getCouponTemplateMaster(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody CouponTemplateMasterDto dto) {
		try {
			return iCouponTemplateMasterService.updateStatus(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getCouponTemplateDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCouponTemplateDetails(@RequestBody CouponTemplateMasterDto dto) {
		try {
			return iCouponTemplateMasterService.getCouponTemplateDetails(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
}
