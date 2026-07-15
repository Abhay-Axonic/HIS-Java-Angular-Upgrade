package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodRedCellDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.service.IBloodRedCellService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank/redcell")
@SuppressWarnings({"rawtypes"})
public class BloodRedCellController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodRedCellController.class);


	@Autowired
	private IBloodRedCellService iBloodRedCellService;
	
	@RequestMapping(value = "/saveOrUpdateRedCell", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOrUpdateRedCell(@RequestBody BloodRedCellDto dto) {
		try {
			return iBloodRedCellService.saveOrUpdateRedCell(dto);

		} catch (Exception e) {
			logger.error("saveOrUpdateRedCell failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getRedCellDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRedCellDetails(@RequestBody BloodRedCellDto dto) {
		try {
			return iBloodRedCellService.getRedCellDetails(dto);

		} catch (Exception e) {
			logger.error("getRedCellDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getRedCellDetailsOnComponentSeparation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRedCellDetailsOnComponentSeparation(@RequestBody TransfusionTransmittedDiseasesDto dto) {
		try {
			return iBloodRedCellService.getRedCellDetailsOnComponentSeparation(dto);

		} catch (Exception e) {
			logger.error("getRedCellDetailsOnComponentSeparation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
