package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.service.IBloodBankStockDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank/stockDetails")
@SuppressWarnings({"rawtypes"})
public class BloodBankStockDetailsController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockDetailsController.class);


	@Autowired
	private IBloodBankStockDetailsService iBloodBankStockDetailsService;
	
	@RequestMapping(value = "/searchBloodBankStockDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchBloodBankStockDetails(@RequestBody BloodBankStockDetailsDto dto) {
		try {
			return iBloodBankStockDetailsService.searchBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("searchBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveBloodBankStockDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBankStockDetails(@RequestBody BloodBankStockDetailsDto dto) {
		try {
			return iBloodBankStockDetailsService.saveBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("saveBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankStockDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBankStockDetails(@RequestBody BloodBankStockDetailsDto dto) {
		try {
			return iBloodBankStockDetailsService.updateBloodBankStockDetails(dto);

		} catch (Exception e) {
			logger.error("updateBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankStockByComponentId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBankStockByComponentId(@RequestBody BloodBankStockDetailsDto dto) {
		try {
			return iBloodBankStockDetailsService.getBloodBankStockByComponentId(dto);

		} catch (Exception e) {
			logger.error("getBloodBankStockByComponentId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
