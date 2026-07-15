package com.param.assetManagement.indent.assetReturn.controller;

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

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.assetManagement.indent.assetReturn.service.IAssetReturnService;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/management/asset/return")
public class AssetReturnController {

	private static final Logger logger = Logger.getLogger(AssetReturnController.class);

	@Autowired
	IAssetReturnService AssetReturnService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AssetIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Asset Return List by search", response = AssetReturnResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetReturnList(@RequestBody AssetIndentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.getAssetReturnList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Asset Return list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAssetReturn(@RequestBody AssetReturnRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.saveAssetReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Asset Return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Asset Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateAssetReturn(@PathVariable Integer id, @RequestBody AssetIndentRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(AssetReturnService.updateAssetReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Asset return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Asset Return details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAssetReturnDetails(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.getAssetReturnDetails(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Asset Return details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Asset Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveAssetReturn(@RequestBody AssetReturnRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.approveAssetReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Approving the Asset Return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Asset Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelMaterialReturn(@PathVariable Integer id,
			@RequestBody AssetReturnRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(AssetReturnService.cancelAssetReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Cancelling the Asset Return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/againstIssue/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Asset Return List by search", response = AssetReturnResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetReturnAgainstIssueList(@RequestBody AssetIndentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(AssetReturnService.getAssetReturnAgainstIssue(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Asset Return list against issue", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
