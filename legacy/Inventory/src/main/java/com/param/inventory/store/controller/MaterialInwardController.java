package com.param.inventory.store.controller;

import java.util.List;

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
import com.param.inventory.store.dto.MaterialInwardRequestNew;
import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;
import com.param.inventory.store.service.materialInward.IMaterialInwardService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/inward")
public class MaterialInwardController {

	private static final Logger logger = Logger.getLogger(MaterialInwardController.class);

	@Autowired
	IMaterialInwardService MaterialInwardService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = MaterialInwardDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {

		try {
			return new ResponseEntity<Object>(MaterialInwardService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material inwards by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialInwardList(@RequestBody MaterialInwardRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.getMaterialInwardList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material inwards list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Inward", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialInward(@RequestBody MaterialInwardRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.saveMaterialInward(request), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-1803", "Error while saving the material inward", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	// updated by sharad rindhe(15-11-2019)
	// purpose : save material inward for multiple gatepass 
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Inward", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialInward(@RequestBody MaterialInwardRequestNew request) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.saveMaterialInward( request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the material inward", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "update Material Inward", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateMaterialInward(@PathVariable Integer id,
			@RequestBody MaterialInwardRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialInwardService.updateMaterialInward(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the material inward", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get material inward details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialInward(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.getMaterialInward(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material inward", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel material inward", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelMaterialInward(@PathVariable Integer id,
			@RequestBody MaterialInwardRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialInwardService.cancelMaterialInward(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the material inward", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/returnablegatepass/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of returnable getpass by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getReturnableGatepass(@RequestBody MaterialInwardRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.getReturnableGatepassList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable getpass list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/*New Implementation For Detail Api*/
	@RequestMapping(value = "/returnableGatepass/detail/{id}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of returnable getpass by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getReturnableGatepassSearch(@PathVariable Integer id,
			@RequestBody MaterialInwardRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialInwardService.getReturnableGatepassSearchList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable getpass list", ex);
			throw new ServiceException(ex.getMessage());
	   }
	}
	
	
	/*New Implementation For multiple gatepass number Detail Api*/
	@RequestMapping(value = "/returnableGatepass/multiplegatepassdetail/{unitId}/{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of multiple returnable getpass by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMultipleReturnableGatepassSearch(@RequestBody List<MaterialInwardRequest> request,@PathVariable Integer orgId,@PathVariable Integer unitId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.getMultipleReturnableGatepassSearch(request,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable getpass list", ex);
			throw new ServiceException(ex.getMessage());
	   }
	}


	/*New Implementation For Detail Api*/
	@RequestMapping(value = "/returnableGatepass/vendorwisecreditnote/{vendorid}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of returnable getpass by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getReturnableGatepassVendorWiseSearch(@PathVariable Integer vendorid)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardService.getReturnableGatepassVendorWiseSearch(vendorid), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable getpass list", ex);
			throw new ServiceException(ex.getMessage());
	   }
	}
}
