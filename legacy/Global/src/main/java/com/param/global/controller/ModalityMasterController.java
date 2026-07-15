package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ModalityMasterDto;
import com.param.global.service.IModalityMasterService;

@Controller
@RequestMapping("/api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModalityMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ModalityMasterController.class);

	@Autowired
	IModalityMasterService iModalityMasterService;
	
	@RequestMapping(value = "/saveModalityMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveModalityMaster(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {

			return iModalityMasterService.saveModalityMaster(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while saving modality master", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getModalityMasterList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getModalityMasterList(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getModalityMasterList(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching modality master list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateModalityMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateModalityMaster(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.updateModalityMaster(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while updating modality master", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/updateStatusForModality", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForModality(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {

			return iModalityMasterService.updateStatusForModality(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while updating status for modality", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveModalityList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActivePrefixList(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getActiveModalityList(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching active modality list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getModalityCount",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getModalityCount(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getModalityCount(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching modality count", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getModalityById",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getModalityById(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getModalityById(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching modality by id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getModalityBySubSpecialityId",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getModalityBySubSpecialityId(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getModalityBySubSpecialityId(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching modality by sub speciality id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getModalityBySubSpecialityArray",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getModalityBySubSpecialityArray(@RequestBody ModalityMasterDto modalityMasterDto) {
		try {
			return iModalityMasterService.getModalityBySubSpecialityArray(modalityMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching modality by sub speciality array", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/equipments/{unitId}",  method = RequestMethod.GET)
	public @ResponseBody Response getEquipments(@PathVariable("unitId") int unitId) {
		try {
			return iModalityMasterService.getEquipments(unitId);
		} catch (Exception e) {
			logger.error("Error while fetching equipments", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/equipmentsChargable/{unitId}/{productCategoryId}",  method = RequestMethod.GET)
	public @ResponseBody Response getEquipmentsChargable(@PathVariable("unitId") Integer unitId,@PathVariable("productCategoryId") Integer productCategoryId) {
		try {
			return iModalityMasterService.getEquipmentsChargable(unitId,productCategoryId);
		} catch (Exception e) {
			logger.error("Error while fetching chargable equipments", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
