package com.param.inventory.store.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.service.materialIssuePending.IMaterialIssuePendingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/issue/pending")
public class MaterialIssuePendingController {

	private static final Logger logger = Logger.getLogger(MaterialIssuePendingController.class);

	@Autowired
	IMaterialIssuePendingService MaterialIssuePendingService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,
			@PathVariable Integer orgId, @PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssuePendingService.getDropdowns(unitId, orgId, userId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material Issue Pending by search", response = MaterialPickerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialIssuePending(@RequestBody MaterialPickerRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssuePendingService.getMaterialIssuePendingList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Material Issue Pending list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Issue", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialIssuePending(@RequestBody MaterialPickerRequest request)
			throws Exception {
		try {
            Response statausres = MaterialIssuePendingService.materialIssueStatus(request.getId());
            
            if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
                    || statausres.getObject() == com.param.common.constants.Status.Closed.getStatus()) {
                CommonResponse response = new CommonResponse();
                response.setMessage("This transaction already processed by other user");
                return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
			return new ResponseEntity<Object>(MaterialIssuePendingService.saveMaterialIssue(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the material issue pending", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Issue Pending details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialIssuePending(@PathVariable Integer id, @PathVariable Integer unitId,
			@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssuePendingService.getMaterialIssue(id, orgId, unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material issue pending details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
