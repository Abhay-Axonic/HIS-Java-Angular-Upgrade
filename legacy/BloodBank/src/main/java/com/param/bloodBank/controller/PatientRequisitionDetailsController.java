package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.service.IPatientRequisitionDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/api/bloodBank")
@SuppressWarnings({ "rawtypes" })
public class PatientRequisitionDetailsController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(PatientRequisitionDetailsController.class);


	@Autowired
	IPatientRequisitionDetailsService iPatientRequisitionDetailsService;
	
	@RequestMapping(value = "/getComponentListByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getComponentListByPatientId(@RequestBody PatientRequisitionDetailsDto dto) {
		try {
			return iPatientRequisitionDetailsService.getComponentListByPatientId(dto);

		} catch (Exception e) {
			logger.error("getComponentListByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/savePatientRequisitionDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientRequisitionDetails(@RequestBody PatientRequisitionDetailsDto dto) {
		try {
			return iPatientRequisitionDetailsService.savePatientRequisitionDetails(dto);

		} catch (Exception e) {
			logger.error("savePatientRequisitionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPatientRequisitionDetailsOnCrossMatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientRequisitionDetailsOnCrossMatch(@RequestBody PatientRequisitionDetailsDto dto) {
		try {
			return iPatientRequisitionDetailsService.getPatientRequisitionDetailsOnCrossMatch(dto);

		} catch (Exception e) {
			logger.error("getPatientRequisitionDetailsOnCrossMatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateExportedToCrossMatchStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateExportedToCrossMatchStatus(@RequestBody PatientRequisitionDetailsDto dto) {
		try {
			return iPatientRequisitionDetailsService.updateExportedToCrossMatchStatus(dto);

		} catch (Exception e) {
			logger.error("updateExportedToCrossMatchStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
