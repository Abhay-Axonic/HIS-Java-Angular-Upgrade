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
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.drugRecall.DrugRecallDropdownResponse;
import com.param.inventory.store.dto.drugRecall.DrugRecallRequest;
import com.param.inventory.store.dto.drugRecall.DrugRecallResponse;
import com.param.inventory.store.service.drugRecall.IDrugRecallService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/drug/recall")
public class DrugRecallController {
	private static final Logger logger = Logger.getLogger(DrugRecallController.class);

	@Autowired
	IDrugRecallService DrugRecallService;

	@RequestMapping(value = "/reason/dropdown", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = DrugRecallDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {

		try {
			return new ResponseEntity<Object>(DrugRecallService.getDropdowns(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of drug recall by search", response = DrugRecallResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDrugRecallList(@RequestBody DrugRecallRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(DrugRecallService.getDrugRecallList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the drug recall list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Drug Recall", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveDrugRecall(@RequestBody DrugRecallRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(DrugRecallService.saveDrugRecall(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the drug recall", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get drug recall details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDrugRecall(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(DrugRecallService.getDrugRecall(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Drug Recall", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel drug recall", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelDrugRecall(@PathVariable Integer id, @RequestBody DrugRecallRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(DrugRecallService.cancelDrugRecall(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the Drug Recall", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "update drug recall", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateDrugRecall(@PathVariable Integer id,
			@RequestBody DrugRecallRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(DrugRecallService.updateDrugRecall(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating drug recall", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Drug Recall", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveDrugRecall(@RequestBody DrugRecallRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(DrugRecallService.approveDrugRecall(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Drug Recall", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
