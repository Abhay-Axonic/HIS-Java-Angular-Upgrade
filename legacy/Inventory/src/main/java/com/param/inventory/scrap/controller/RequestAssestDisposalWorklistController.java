package com.param.inventory.scrap.controller;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.AssestDisposalRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.inventory.scrap.service.requestAssestDisposalWorklist.IRequestAssestDisposalWorklistService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/scrap/request/asset/disposal/worklist")
public class RequestAssestDisposalWorklistController {

	private static final Logger logger = Logger.getLogger(RequestAssestDisposalWorklistController.class);

	@Autowired
	IRequestAssestDisposalWorklistService RequestAssestDisposalWorklistService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ScrapsDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(RequestAssestDisposalWorklistService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Request Assest Disposal Worklist by search", response = ScrapResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getRequestAssestDisposalWorklistList(@RequestBody ScrapRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(
					RequestAssestDisposalWorklistService.getRequestAssestDisposalWorklistList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Request Assest Disposal Worklist list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/* old save implementation */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Request Assest Disposal Worklist", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveRequestAssestDisposalWorklist(@RequestBody AssestDisposalRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(
					RequestAssestDisposalWorklistService.saveRequestAssestDisposalWorklist(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Saving Request Assest Disposal Worklist", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/* Changing Save to Approve */
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Request Assest Disposal Worklist", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateRequestAssestDisposalWorklist(@PathVariable Integer id,
			@RequestBody AssestDisposalRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(
					RequestAssestDisposalWorklistService.updateRequestAssestDisposalWorklist(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Updating the Request Assest Disposal Worklist", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Scrap Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getRequestAssestDisposalWorklistDetails(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(
					RequestAssestDisposalWorklistService.getRequestAssestDisposalWorklistDetails(id,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Scrap Request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
