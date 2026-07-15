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
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.service.materialPicker.IMaterialPickerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/picker")
public class MaterialPickerController {

	private static final Logger logger = Logger.getLogger(MaterialPickerController.class);

	@Autowired
	IMaterialPickerService MaterialPickerService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialPickerService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material Picker by search", response = MaterialPickerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialPickerList(@RequestBody MaterialPickerRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialPickerService.getMaterialPickerList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getMaterialPickerList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Picker", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialPicker(@RequestBody MaterialPickerRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialPickerService.saveMaterialPicker(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveMaterialPicker failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Picker details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialPicker(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialPickerService.getMaterialPicker(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getMaterialPicker failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/close/{id}", method = RequestMethod.PATCH)
	 * 
	 * @ApiOperation(value = "Cancel Material Picker", response =
	 * CommonResponse.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id,
	 * 
	 * @RequestBody MaterialPickerRequest request) throws Exception { try {
	 * request.setId(id); return new
	 * ResponseEntity<Object>(MaterialPickerService.cancelMaterialPicker(request),
	 * HttpStatus.OK); } catch (Exception ex) { return new ResponseEntity<Object>(
	 * new ErrorResponse("HIS-1175", "Error while cancelling the Material Picker",
	 * ex), HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
}
