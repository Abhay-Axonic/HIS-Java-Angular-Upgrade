package com.param.assetManagement.maintenance.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.service.ICorrectiveMaintenanceWorklistService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/api/assetManagement/maintenance")
@SuppressWarnings({"rawtypes"})
public class CorrectiveMaintenanceWorklistController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(CorrectiveMaintenanceWorklistController.class);

	@Autowired
	ICorrectiveMaintenanceWorklistService iCorrectiveMaintenanceWorklistservice;

	@RequestMapping(value = "/saveCorrectiveMaintenance/worklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCorrectiveMaintenanceWorklist(@RequestBody CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) {
		try {

				return iCorrectiveMaintenanceWorklistservice.saveCorrectiveMaintenanceWorklist(correctiveMaintenanceWorklistDto);

		} catch (Exception e) {
			logger.error("saveCorrectiveMaintenanceWorklist failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getCorrectiveMaintenanceWorklistListByMultipleCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCorrectiveMaintenanceWorklistListByMultipleCriteria(@RequestBody CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) {
		try {
			return iCorrectiveMaintenanceWorklistservice.getCorrectiveMaintenanceWorklistListByMultipleCriteria(correctiveMaintenanceWorklistDto);

		} catch (Exception e) {
			logger.error("getCorrectiveMaintenanceWorklistListByMultipleCriteria failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
