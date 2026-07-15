package com.param.assetManagement.indent.assetIssueAcceptance.controller;

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
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.assetManagement.indent.assetIssueAcceptance.service.IAssetIssueAcceptanceService;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/issue/acceptance")
public class AssetIssueAcceptanceController {

	private static final Logger logger = Logger.getLogger(AssetIssueAcceptanceController.class);

	@Autowired
	IAssetIssueAcceptanceService AssetIssueAcceptanceService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AssetIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetIssueAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Asset Issue Acceptance List by search", response = AssetIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetIssueAcceptanceList(@RequestBody AssetIssueRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(AssetIssueAcceptanceService.getAssetIssueAcceptanceList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Asset Issue Acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Asset Issue Acceptance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAssetIssueAcceptance(@PathVariable Integer id,
			@RequestBody AssetIssueRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(AssetIssueAcceptanceService.saveAssetIssueAcceptance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Updating the Asset Issue Acceptance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Asset Issue Acceptance List details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAssetIssueAcceptanceDetails(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(AssetIssueAcceptanceService.getAssetIssueAcceptanceDetails(id,unitId,orgId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Asset Issue Acceptance Details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
