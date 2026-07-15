package com.param.global.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PaymentEntitlementMasterDto;
import com.param.global.service.IPaymentEntitlementMasterService;

@RestController
@RequestMapping(value="/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PaymentEntitlementController implements ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(PaymentEntitlementController.class);

	@Autowired
	IPaymentEntitlementMasterService iPaymentEntitlementMasterService;
	
	
	@RequestMapping(value="/getPaymentEntitlement", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getPaymentEntitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.getPaymentEntitlementMaster(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("getPaymentEntitlement failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	
	@RequestMapping(value="/paymententitlement/{orgId}", method=RequestMethod.GET)
	public Response getPaymentEntitlementByOrg(@PathVariable("orgId")int orgId){
		try{
			return iPaymentEntitlementMasterService.getPaymentEntitlementListByOrgId(orgId);
		}catch(ApplicationException ae){
			logger.error("getPaymentEntitlementByOrg failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	@RequestMapping(value="/getPaymententitlementById/orgId/{orgId}/paymentEntId/{paymentEntId}", method=RequestMethod.GET)
	public Response getPaymententitlementById(@PathVariable("orgId")int orgId,@PathVariable("paymentEntId")int paymentEntId){
		try{
			return iPaymentEntitlementMasterService.getPaymententitlementById(orgId,paymentEntId);
		}catch(ApplicationException ae){
			logger.error("getPaymententitlementById failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	@RequestMapping(value="/getPaymententitlementList", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getPaymententitlementList(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.getPaymententitlementList(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("getPaymententitlementList failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	@RequestMapping(value="/savePaymententitlement", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response savePaymententitlement(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.savePaymententitlement(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("savePaymententitlement failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	@RequestMapping(value="/updatePaymententitlement", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updatePaymententitlement(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.updatePaymententitlement(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("updatePaymententitlement failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}

	@RequestMapping(value="/updatePaymententitlementStatus", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updatePaymententitlementStatus(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.updatePaymententitlementStatus(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("updatePaymententitlementStatus failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}
	@RequestMapping(value="/getPaymententitlementCount", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getPaymententitlementCount(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto){
		try{
			return iPaymentEntitlementMasterService.getPaymententitlementCount(paymentEntitlementMasterDto);
		}catch(ApplicationException ae){
			logger.error("getPaymententitlementCount failed", ae);
			throw new ServiceException(ae.getMessage());
		}
	}

}
