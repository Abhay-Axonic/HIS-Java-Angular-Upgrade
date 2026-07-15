package com.param.CSSD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.dto.CssdOpeningBalanceRequest;
import com.param.CSSD.dto.CssdOpeningStockDto;
import com.param.CSSD.service.ICSSDOpeningStockService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/api/cssd/stock/opening/balances")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CSSDOpeningStockController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(CSSDOpeningStockController.class);


	@Autowired
	private ICSSDOpeningStockService cssdOpeningStockService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveOpeningStock(@RequestBody CssdOpeningStockDto cssdOpeningStockDto) {
		try {
			return cssdOpeningStockService.saveOpeningStock(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("saveOpeningStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveOpeningStock(@RequestBody CssdOpeningBalanceRequest cssdOpeningStockDto) {
		try {
			return cssdOpeningStockService.getCssdOpeningBalanceList(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("saveOpeningStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response approveCssdOpeningBalanceStock(@RequestBody CssdOpeningBalanceRequest request) {
		try {
			return cssdOpeningStockService.approveCssdOpeningBalanceStock(request);
		} catch (Exception e) {
			logger.error("approveCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancelCssdStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelCssdOpeningBalanceStock(@RequestBody CssdOpeningBalanceRequest request) {
		try {
			return cssdOpeningStockService.cancelCssdOpeningBalanceStock(request);
		} catch (Exception e) {
			logger.error("cancelCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/getCssdOpeningBalanceImplantById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCssdOpeningBalanceImplantByOpeningBalanceId(@RequestBody CssdOpeningBalanceRequest request) {
		try {
			return cssdOpeningStockService.getCssdOpeningBalanceImplantByOpeningBalanceId(request);
		} catch (Exception e) {
			logger.error("getCssdOpeningBalanceImplantByOpeningBalanceId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCssdOpeningBalanceStock(@RequestBody CssdOpeningStockDto cssdOpeningStockDto) {
		try {
			return cssdOpeningStockService.updateCssdOpeningBalanceStock(cssdOpeningStockDto);
		} catch (Exception e) {
			logger.error("updateCssdOpeningBalanceStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
