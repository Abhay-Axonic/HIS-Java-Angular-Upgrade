package com.param.CSSD.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.service.IPreSterilizationProcessInstrumentService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessInstrumentController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessInstrumentController.class);


	@Autowired
	private IPreSterilizationProcessInstrumentService iPreSterilizationProcessEquipmentService;

	/**
	 * This method is used to update equipments in the kits for pre sterilization process.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	@RequestMapping(value = "/updatePreSterilizationProcessEquipment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePreSterilizationEquipmentProcess(@RequestBody PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto) {
		try {
			return iPreSterilizationProcessEquipmentService.updatePreSterilizationProcessEquipment(preSterilizationProcessEquipmentDto);
		} catch (Exception e) {
			logger.error("updatePreSterilizationEquipmentProcess failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to get the list of equipments in the particular kit.
	 * @param kitId
	 * @return Response
	 */
	@RequestMapping(value = "/getPreSterilizationProcessEquipmentList", method = RequestMethod.GET)
	public @ResponseBody Response getPreSterilizationProcessEquipmentList(@RequestParam("kitId") Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentService.getPreSterilizationProcessEquipmentList(kitId);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessEquipmentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to clean the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	@RequestMapping(value = "/cleanEquipment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cleanEquipment(@RequestBody List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentService.cleanEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("cleanEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to inspect the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	@RequestMapping(value = "/inspectEquipment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response inspectEquipment(@RequestBody List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentService.inspectEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("inspectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to dis infect the particular equipment.
	 * @param preSterilizationProcessEquipmentDto
	 * @return Response
	 */
	@RequestMapping(value = "/disInfectEquipment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response disInfectEquipment(@RequestBody List<PreSterilizationProcessInstrumentDto> preSterilizationProcessEquipmentDtoList) {
		try {
			return iPreSterilizationProcessEquipmentService.disInfectEquipment(preSterilizationProcessEquipmentDtoList);
		} catch (Exception e) {
			logger.error("disInfectEquipment failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the list of equipments pending for inspection.
	 * @param kitId
	 * @return Response
	 */
	@RequestMapping(value = "/getEquipmentListPendingForInspection", method = RequestMethod.GET)
	public @ResponseBody Response getEquipmentListPendingForInspection(@RequestParam("kitId") Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentService.getEquipmentListPendingForInspection(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the list of equipments pending for dis infection.
	 * @param kitId
	 * @return Response
	 */
	@RequestMapping(value = "/getEquipmentListPendingForDisInfection", method = RequestMethod.GET)
	public @ResponseBody Response getEquipmentListPendingForForDisInfection(@RequestParam("kitId") Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentService.getEquipmentListPendingForDisInfection(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the list of equipments pending for kit formation.
	 * @param kitId
	 * @return Response
	 */
	@RequestMapping(value = "/getEquipmentListPendingForKitFormation", method = RequestMethod.GET)
	public @ResponseBody Response getEquipmentListPendingForForKitFormation(@RequestParam("kitId") Integer kitId) {
		try {
			return iPreSterilizationProcessEquipmentService.getEquipmentListPendingForKitFormation(kitId);
		} catch (Exception e) {
			logger.error("getEquipmentListPendingForForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
