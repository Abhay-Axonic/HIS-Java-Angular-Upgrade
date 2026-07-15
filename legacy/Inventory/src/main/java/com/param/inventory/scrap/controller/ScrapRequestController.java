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
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.inventory.scrap.service.scrapRequest.IScrapRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/scraps/scrap/request")
public class ScrapRequestController {

	private static final Logger logger = Logger.getLogger(ScrapRequestController.class);

	@Autowired
	IScrapRequestService ScrapRequestService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ScrapsDropdownResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/*@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ScrapsDropdownResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownListByUserId(@PathVariable Integer userId) throws Exception {
		try {
			//return new ResponseEntity<Object>(ScrapRequestService.getDropownsByUserId(userId), HttpStatus.OK);
			return null;
		} catch (Exception ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1601", "Error while fetching dropdowns", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Scrap Request by search", response = ScrapResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getScrapRequestList(@RequestBody ScrapRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestService.getScrapRequestList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getScrapRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Scrap Request", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveScrapRequest(@RequestBody ScrapRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestService.saveScrapRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveScrapRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Scrap Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getScrapRequestDetails(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestService.getScrapRequestDetails(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getScrapRequestDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Scrap Request", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateScrapRequest(@PathVariable Integer id, @RequestBody ScrapRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ScrapRequestService.updateScrapRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateScrapRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Scrap Request", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveScrapRequest(@RequestBody ScrapRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestService.approveScrapRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveScrapRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Scrap Request", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelScrapRequest(@PathVariable Integer id, @RequestBody ScrapRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ScrapRequestService.cancelScrapRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelScrapRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
