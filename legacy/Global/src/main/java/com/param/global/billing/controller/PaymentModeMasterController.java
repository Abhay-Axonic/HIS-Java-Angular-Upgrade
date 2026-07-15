package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.service.IPaymentModeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PaymentEntitlementMasterDto;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })

public class PaymentModeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(PaymentModeMasterController.class);

	@Autowired
	private IPaymentModeMasterService iPaymentModeMasterService;


	@RequestMapping(value = "/savePaymentModeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePaymentModeMaster(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.savePaymentModeMaster(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("savePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value = "/getPaymentModeMasterById/{payModeId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getPaymentModeMasterById(@PathVariable("payModeId") int payModeId,
			@PathVariable("orgId") int orgId) {
		try {
			return iPaymentModeMasterService.getPaymentModeMasterById(payModeId, orgId);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getPaymentModeMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getPaymentModeMasterList(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.getPaymentModeMasterList(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updatePaymentModeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePaymentModeMaster(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.updatePaymentModeMaster(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("updatePaymentModeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updatePaymentModeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePaymentModeMasterStatus(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.updatePaymentModeMasterStatus(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("updatePaymentModeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getPaymentModeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPaymentModeMasterCount(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.getPaymentModeMasterCount(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getPaymentModeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActivePaymentModeMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getActivePaymentModeMasterList(@RequestBody PaymentEntitlementMasterDto paymentEntitlementMasterDto) {
		try {
			return iPaymentModeMasterService.getActivePaymentModeMasterList(paymentEntitlementMasterDto);
		} catch (Exception e) {
			logger.error("getActivePaymentModeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
