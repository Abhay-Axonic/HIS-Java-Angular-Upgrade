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

import com.param.global.billing.dto.CurrencyDenominationMasterDto;
import com.param.global.billing.service.ICurrencyDenominationMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CurrencyDenominationMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(CurrencyDenominationMasterController.class);

	@Autowired
	ICurrencyDenominationMasterService iCurrencyDenominationMasterService;

	@RequestMapping(value = "/saveCurrencyDenominationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCurrencyDenominationMaster(@RequestBody CurrencyDenominationMasterDto currencyDenominationMasterDto) {
		try {
			return iCurrencyDenominationMasterService.saveCurrencyDenominationMaster(currencyDenominationMasterDto);
		} catch (Exception e) {
			logger.error("saveCurrencyDenominationMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getCurrencyDenominationMasterById/{currencyId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getCurrencyDenominationMasterById(@PathVariable("currencyId") int currencyId,@PathVariable("orgId") int orgId) {
		try {
			return iCurrencyDenominationMasterService.getCurrencyDenominationMasterById(currencyId,orgId);
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCurrencyDenominationMasterList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCurrencyDenominationMasterList(@RequestBody CurrencyDenominationMasterDto   currencyDenominationMasterDto) {
		try {
			return iCurrencyDenominationMasterService.getCurrencyDenominationMasterList(currencyDenominationMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateCurrencyDenominationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCurrencyDenominationMaster(@RequestBody CurrencyDenominationMasterDto currencyDenominationMasterDto) {
		try {
			return iCurrencyDenominationMasterService.updateCurrencyDenominationMaster(currencyDenominationMasterDto);
		} catch (Exception e) {
			logger.error("updateCurrencyDenominationMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateCurrencyDenominationMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCurrencyDenominationMasterStatus(@RequestBody CurrencyDenominationMasterDto currencyDenominationMasterDto) {
		try {
			return iCurrencyDenominationMasterService.updateCurrencyDenominationMasterStatus(currencyDenominationMasterDto);
		} catch (Exception e) {
			logger.error("updateCurrencyDenominationMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCurrencyDenominationCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCurrencyDenominationCount(@RequestBody CurrencyDenominationMasterDto currencyDenominationMasterDto) {
		try {
			return iCurrencyDenominationMasterService.getCurrencyDenominationCount(currencyDenominationMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyDenominationCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
