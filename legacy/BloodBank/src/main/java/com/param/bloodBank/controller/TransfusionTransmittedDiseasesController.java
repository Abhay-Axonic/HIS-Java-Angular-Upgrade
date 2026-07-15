package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.service.ITransfusionTransmittedDiseasesService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TransfusionTransmittedDiseasesController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(TransfusionTransmittedDiseasesController.class);

	
	@Autowired 
	ITransfusionTransmittedDiseasesService iTransfusionTransmittedDiseasesService;
	
	
	@RequestMapping(value = "/saveTransfusionTransmittedDiseases", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTransfusionTransmittedDiseases(@RequestBody TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) {
		try {
			return iTransfusionTransmittedDiseasesService.saveTransfusionTransmittedDiseases(transfusionTransmittedDiseasesDto);
		} catch (Exception e) {
			logger.error("saveTransfusionTransmittedDiseases failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getTTDDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTTDDetailsById(@RequestBody TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) {
		try {
			return iTransfusionTransmittedDiseasesService.getTTDDetailsById(transfusionTransmittedDiseasesDto);
		} catch (Exception e) {
			logger.error("getTTDDetailsById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getTTDListOnRedCellGroupingAndSerum", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTTDListOnRedCellGroupingAndSerum(@RequestBody TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto) {
		try {
			return iTransfusionTransmittedDiseasesService.getTTDListOnRedCellGroupingAndSerum(transfusionTransmittedDiseasesDto);
		} catch (Exception e) {
			logger.error("getTTDListOnRedCellGroupingAndSerum failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
