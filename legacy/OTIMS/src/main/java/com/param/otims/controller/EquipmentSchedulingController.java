package com.param.otims.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.EquipmentSchedulingDto;
import com.param.otims.service.IEquipmentSchedulingService;

@RestController
@RequestMapping("/global/otims/equipmentScheduling")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EquipmentSchedulingController  implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(EquipmentSchedulingController.class);

	@Autowired
	IEquipmentSchedulingService iEquipmentSchedulingService;

	@RequestMapping(value = "/saveEquipmentScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveEquipmentScheduling(@RequestBody EquipmentSchedulingDto equipmentSchedulingDto) {
		try {
			return iEquipmentSchedulingService.saveEquipmentScheduling(equipmentSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getEquipmentScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEquipmentScheduling(@RequestBody EquipmentSchedulingDto equipmentSchedulingDto) {
		try {
			return iEquipmentSchedulingService.getEquipmentScheduling(equipmentSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deActivateEquipmentScheduling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deActivateEquipmentScheduling(@RequestBody EquipmentSchedulingDto equipmentSchedulingDto) {
		try {
			return iEquipmentSchedulingService.deActivateEquipmentScheduling(equipmentSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/checkEquipmentavailableOrNot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkEquipmentavailableOrNot(@RequestBody EquipmentSchedulingDto equipmentSchedulingDto) {
		try {
			return iEquipmentSchedulingService.checkEquipmentavailableOrNot(equipmentSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/checkEquipmentListAvailableOrNot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkEquipmentListAvailableOrNot(@RequestBody List<EquipmentSchedulingDto> equipmentSchedulingDto) {
		try {
			return iEquipmentSchedulingService.checkEquipmentListAvailableOrNot(equipmentSchedulingDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
