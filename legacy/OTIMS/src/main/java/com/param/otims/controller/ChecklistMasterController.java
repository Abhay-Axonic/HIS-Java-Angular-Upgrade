package com.param.otims.controller;

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
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.otims.service.IChecklistMasterService;

@RestController
@RequestMapping("/global/otims")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChecklistMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ChecklistMasterController.class);

	@Autowired
	IChecklistMasterService iChecklistMasterService;

	@RequestMapping(value = "/saveChecklistMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveChecklistMaster(@RequestBody ChecklistMasterDto checklistMasterDto) {
		try {
			return iChecklistMasterService.saveChecklistMaster(checklistMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateChecklistMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateChecklistMasterStatus(@RequestBody ChecklistMasterDto checklistMasterDto) {
		try {
			return iChecklistMasterService.updateChecklistMasterStatus(checklistMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllChecklistMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllChecklistMasterList(@RequestBody ChecklistMasterDto checklistMasterDto) {
		try {
			return iChecklistMasterService.getAllChecklistMasterList(checklistMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getChecklistMasterDetailsByMasterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChecklistMasterDetailsByMasterId(@RequestBody ChecklistMasterDetailsDto dto) {
		try {
			return iChecklistMasterService.getChecklistMasterDetailsByMasterId(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getChecklistMasterByStageId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChecklistMasterByStageId(@RequestBody ChecklistMasterDto dto) {
		try {
			return iChecklistMasterService.getChecklistMasterByStageId(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
