package com.param.assetManagement.maintenance.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.assetManagement.maintenance.service.ICorrectiveMaintenanceService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/api/assetManagement/maintenance")
@SuppressWarnings({"rawtypes"})
public class CorrectiveMaintenanceController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceController.class);

	@Autowired
	private ICorrectiveMaintenanceService iCorrectiveMaintenanceService;

	@RequestMapping(value = "/saveCorrectiveMaintenance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCorrectiveMaintenance(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.saveCorrectiveMaintenance(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while saving corrective maintenance", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCorrectiveMaintenanceListByMultipleCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCorrectiveMaintenanceListByMultipleCriteria(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.getCorrectiveMaintenanceListByMultipleCriteria(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while fetching corrective maintenance list by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSerialNoByItemId/{itemId}/{unitId}/{organizationId}", method = RequestMethod.GET)
	public Response getSerialNoByItemId(@PathVariable Integer itemId,@PathVariable Integer unitId , @PathVariable Integer organizationId) {
		try {
			return iCorrectiveMaintenanceService.getSerialNoByItemId(itemId,unitId,organizationId);

		} catch (Exception e) {
			logger.error("Error while fetching serial no by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/exportedToCorrectiveMaintenanceWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response ExportedToCorrectiveMaintenanceWorklist(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.ExportedToCorrectiveMaintenanceWorklist(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while exporting to corrective maintenance worklist", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/CorrectiveMaintenanceRequestCompleteByRequester", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response CorrectiveMaintenanceRequestCompleteByRequester(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.CorrectiveMaintenanceRequestCompleteByRequester(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while completing corrective maintenance request by requester", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/updateCorrectiveMaintenancePendingToNewRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCorrectiveMaintenancePendingToNewRequest(@RequestBody List<CorrectiveMaintenanceDto> correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.updateCorrectiveMaintenancePendingToNewRequest(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while updating corrective maintenance pending to new request", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancelCorrectiveMaintenanceRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelCorrectiveMaintenanceRequest(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
		try {
			return iCorrectiveMaintenanceService.cancelCorrectiveMaintenanceRequest(correctiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while cancelling corrective maintenance request", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
    @RequestMapping(value = "/getPMSDetailsByItemId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getPMSDetailsByItemId(@RequestBody CorrectiveMaintenanceDto correctiveMaintenanceDto) {
        try {
            return iCorrectiveMaintenanceService.getPMSDetailsByItemId(correctiveMaintenanceDto);

        } catch (Exception e) {
            logger.error("Error while fetching PMS details by item id", e);
            throw new ServiceException(e.getMessage());
        }
    }
}
