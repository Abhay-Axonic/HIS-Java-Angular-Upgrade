package com.param.assetManagement.indent.assetReturnAcceptance.controller;

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
import com.param.assetManagement.indent.assetReturnAcceptance.service.IAssetReturnAcceptanceService;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/management/asset/return/acceptance")
public class AssetReturnAcceptanceController {

	private static final Logger logger = Logger.getLogger(AssetReturnAcceptanceController.class);

	@Autowired
	IAssetReturnAcceptanceService AssetReturnAcceptanceService;
	
	 @RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list for populating dropdown values", response = AssetIndentDropdown.class)
		@ApiResponses(value = { 
				@ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"),
				@ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
			try {
				return new ResponseEntity<Object>(AssetReturnAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("Error while fetching dropdowns", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
	 
	 @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
		@ApiOperation(value = "Get list of Material Return Acceptance by search", response = AssetReturnResponse.class)
		@ApiResponses(value = { 
				@ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), 
				@ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getAssetReturnAcceptanceList(@RequestBody AssetIndentRequest request)
				throws Exception {
			try {
				return new ResponseEntity<Object>(AssetReturnAcceptanceService.getAssetReturnAcceptanceList(request), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("Error while fetching the Assset Return Acceptance list", ex);
				throw new ServiceException(ex.getMessage());
			}
		}

	    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
		@ApiOperation(value = "Update Asset Return Acceptance", response = CommonResponse.class)
		@ApiResponses(value = { 
				@ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"),
				@ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> updateAssetReturnAcceptance(@PathVariable Integer id, @RequestBody AssetReturnRequest request)
				throws Exception {
			try {
				request.setId(id);
				return new ResponseEntity<Object>(AssetReturnAcceptanceService.updateAssetReturnAcceptance(request), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("Error while Updating the Asset Return Acceptanc", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
	    
		@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get Asset Return Acceptance details by id", response = CommonResponse.class)
		@ApiResponses(value = { 
				@ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"),
				@ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getAssetReturnAcceptanceDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
			try {
				return new ResponseEntity<Object>(AssetReturnAcceptanceService.getAssetReturnAcceptanceDetails(id,unitId), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("Error while fetching the Asset Return Acceptance details", ex);
				throw new ServiceException(ex.getMessage());
			}
		}

}
