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

import com.param.global.billing.dto.CurrencyMasterDto;
import com.param.global.billing.service.ICurrencyMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })

public class CurrencyMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(CurrencyMasterController.class);

	@Autowired
	ICurrencyMasterService iCurrencyMasterService;

	@RequestMapping(value = "/saveCurrencyMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCurrencyMaster(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.saveCurrencyMaster(currencyMasterDto);
		} catch (Exception e) {
			logger.error("saveCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getCurrencyMasterById/{currencyId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getCurrencyMasterById(@PathVariable("currencyId") int currencyId,@PathVariable("orgId") int orgId) {
		try {
			return iCurrencyMasterService.getCurrencyMasterById(currencyId,orgId);
		} catch (Exception e) {
			logger.error("getCurrencyMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCurrencyMasterList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCurrencyMasterList(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.getCurrencyMasterList(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	//Added by Dilip
	@RequestMapping(value = "/getAllCurrencyMasterList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllCurrencyMasterList(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.getAllCurrencyMasterList(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getAllCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateCurrencyMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCurrencyMaster(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.updateCurrencyMaster(currencyMasterDto);
		} catch (Exception e) {
			logger.error("updateCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateCurrencyMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCurrencyMasterStatus(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.updateCurrencyMasterStatus(currencyMasterDto);
		} catch (Exception e) {
			logger.error("updateCurrencyMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getCurrencyCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCurrencyCount(@RequestBody CurrencyMasterDto currencyMasterDto) {
		try {
			return iCurrencyMasterService.getCurrencyCount(currencyMasterDto);
		} catch (Exception e) {
			logger.error("getCurrencyCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
