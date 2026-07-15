package com.param.equipmentchecklist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.equipmentchecklist.service.IEquipmentChecklistService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.global.otims.dto.ChecklistMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/equipmentchecklist")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EquipmentChecklistController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(EquipmentChecklistController.class);

	

	@Autowired
	IEquipmentChecklistService iEquipmentChecklistService;
	
	@RequestMapping(value = "/saveChecklistEquipmentMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveChecklistEquipmentMaster(@RequestBody EquipmentChecklistDto equipmentChecklistDto) {
		try {
			return iEquipmentChecklistService.saveEquipmentChecklist(equipmentChecklistDto);
		} catch (Exception e) {
		logger.error("saveChecklistEquipmentMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateChecklistEquipmentMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateChecklistEquipmentMasterStatus(@RequestBody EquipmentChecklistDto equipmentChecklistDto) {
		try {
			return iEquipmentChecklistService.updateChecklistEquipmentMasterStatus(equipmentChecklistDto);
		} catch (Exception e) {
		logger.error("updateChecklistEquipmentMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getAllEquipmentChecklistMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllEquipmentChecklistMasterList(@RequestBody EquipmentChecklistDto equipmentChecklistDto) {
		try {
			return iEquipmentChecklistService.getAllEquipmentChecklistMasterList(equipmentChecklistDto);
		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getEquipmentChecklistNameListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEquipmentChecklistNameListById(@RequestBody EquipmentChecklistDto equipmentChecklistDto) {
		try {
			return iEquipmentChecklistService.getEquipmentChecklistNameListById(equipmentChecklistDto);
		} catch (Exception e) {
		logger.error("getEquipmentChecklistNameListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getAllEquipmentChecklistMasterListPaginationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllEquipmentChecklistMasterListPaginationList(@RequestBody  EquipmentChecklistDto equipmentChecklistDto) {

		try {
			return iEquipmentChecklistService.getAllEquipmentChecklistMasterListPaginationList(equipmentChecklistDto);

		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMasterListPaginationList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAllEquipmentChecklistMasterListPaginationListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllEquipmentChecklistMasterListPaginationListCount(@RequestBody  EquipmentChecklistDto equipmentChecklistDto) {
		
		try {
			return iEquipmentChecklistService.getAllEquipmentChecklistMasterListPaginationListCount(equipmentChecklistDto);
			
		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMasterListPaginationListCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}
