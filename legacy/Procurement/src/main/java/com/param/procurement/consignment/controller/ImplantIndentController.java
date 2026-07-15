package com.param.procurement.consignment.controller;

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
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentDropdown;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;
import com.param.procurement.consignment.service.implantIndent.IImplantIndentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignments/implant/indent")
public class ImplantIndentController {

	final static Logger logger = Logger.getLogger(ImplantIndentController.class);

	@Autowired
	IImplantIndentService ImplantIndentService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ImplantIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ImplantIndentService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Implant Indent list ", response = ImplantIndentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> searchImplantIndent(@RequestBody ImplantIndentRequest request) {
		try {
			return new ResponseEntity<Object>(ImplantIndentService.searchImplantIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("searchImplantIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Implant Indent details ", response = ImplantIndentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> implantIndentDetails(@PathVariable("id") Integer id,@PathVariable Integer storeId,@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(ImplantIndentService.implantIndentDetails(id,storeId,unitId), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("implantIndentDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Implant Indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveImplantIndent(@RequestBody ImplantIndentRequest request) {
		try {
			return new ResponseEntity<Object>(ImplantIndentService.saveImplantIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveImplantIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Implant Indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateImplantIndent(@PathVariable Integer id,
			@RequestBody ImplantIndentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ImplantIndentService.updateImplantIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateImplantIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Implant Indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveImplantIndent(@RequestBody ImplantIndentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ImplantIndentService.approveImplantIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveImplantIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Implant Indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelImplantIndent(@PathVariable Integer id,
			@RequestBody ImplantIndentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ImplantIndentService.cancelImplantIndent(request), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("cancelImplantIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
