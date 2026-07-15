package com.param.assetManagement.indent.assetIndent.controller;

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
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponse;
import com.param.assetManagement.indent.assetIndent.service.IAssetIndentService;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionResponse;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/indent/assetIndent")
public class AssetIndentController {

	private static final Logger logger = Logger.getLogger(AssetIndentController.class);

	@Autowired
	IAssetIndentService assetIndentService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = AssetIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(assetIndentService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("Error while getting dropdowns by screen id", ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of asset indent", response = AssetIndentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetIndentList(@RequestBody AssetIndentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(assetIndentService.getAssetIndentList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the asset indent list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get asset indent by id", response = ItemAuctionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAssetIndentById(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(assetIndentService.getAssetIndentById(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the asset indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save asset indent ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAssetIndent(@RequestBody AssetIndentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(assetIndentService.saveAssetIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the asset indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update asset indent ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateAssetIndent(@PathVariable Integer id, @RequestBody AssetIndentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(assetIndentService.updateAssetIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the asset indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel asset indent ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelAssetIndent(@PathVariable Integer id, @RequestBody AssetIndentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(assetIndentService.cancelAssetIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the asset indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve asset indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveAssetIndent(@RequestBody AssetIndentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(assetIndentService.approveAssetIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the asset indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}