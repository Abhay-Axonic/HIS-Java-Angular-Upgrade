package com.param.equipmentchecklistmapper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.equipmentchecklist.Dto.EquipmentChecklistMapperDto;
import com.param.equipmentchecklist.service.IEquipmentChecklistMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/equipmentchecklistmapper")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EquipmentChecklistMapperController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(EquipmentChecklistMapperController.class);

	

	@Autowired
	IEquipmentChecklistMapperService iEquipmentChecklistMapperService;
	
	@RequestMapping(value = "/saveChecklistEquipmentMapperMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveChecklistEquipmentMapperMaster(@RequestBody EquipmentChecklistMapperDto equipmentChecklistMapperDto) {
		try {
			return iEquipmentChecklistMapperService.saveChecklistEquipmentMapperMaster(equipmentChecklistMapperDto);
		} catch (Exception e) {
		logger.error("saveChecklistEquipmentMapperMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateChecklistEquipmentMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateChecklistEquipmentMapperStatus(@RequestBody EquipmentChecklistMapperDto equipmentChecklistMapperDto) {
		try {
			return iEquipmentChecklistMapperService.updateChecklistEquipmentMapperStatus(equipmentChecklistMapperDto);
		} catch (Exception e) {
		logger.error("updateChecklistEquipmentMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getAllEquipmentChecklistMapperMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllEquipmentChecklistMapperMasterList(@RequestBody EquipmentChecklistMapperDto equipmentChecklistMapperDto) {
		try {
			return iEquipmentChecklistMapperService.getAllEquipmentChecklistMapperMasterList(equipmentChecklistMapperDto);
		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMapperMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/*@RequestMapping(value = "/getEquipmentChecklistNameListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEquipmentChecklistNameListById(@RequestBody EquipmentChecklistMapperDto equipmentChecklistMapperDto) {
		try {
			return iEquipmentChecklistMapperService.getEquipmentChecklistNameListById(equipmentChecklistMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
}
