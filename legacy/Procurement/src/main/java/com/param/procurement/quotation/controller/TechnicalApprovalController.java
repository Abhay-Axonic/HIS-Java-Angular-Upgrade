package com.param.procurement.quotation.controller;

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
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalRequestDto;
import com.param.procurement.quotation.service.technicalApproval.ITechnicalApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/technical/approval")
public class TechnicalApprovalController {

	final static Logger logger = Logger.getLogger(TechnicalApprovalController.class);

	@Autowired
	ITechnicalApprovalService technicalApprovalService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getTechnicalApprovalDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(technicalApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getTechnicalApprovalDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Technical Approval by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getTechnicalApprovalList(@RequestBody TechnicalApprovalRequestDto techApprovalRequestDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(technicalApprovalService.getTechnicalApprovalList(techApprovalRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getTechnicalApprovalList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Technical Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveTechnicalApproval(@RequestBody TechnicalApprovalRequestDto techApprovalRequestDto) throws Exception {
		try {
			return new ResponseEntity<Object>(technicalApprovalService.saveTechnicalApproval(techApprovalRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveTechnicalApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Technical Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateTechnicalApproval(@PathVariable Integer id, @RequestBody TechnicalApprovalRequestDto techApprovalRequestDto)
			throws Exception {
		try {
			techApprovalRequestDto.setId(id);
			    return new ResponseEntity<Object>(technicalApprovalService.updateTechnicalApproval( techApprovalRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateTechnicalApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Technical Approval details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getTechnicalApproval(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(technicalApprovalService.getTechnicalApproval(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getTechnicalApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Technical Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelTechnicalApproval(@PathVariable Integer id, @RequestBody TechnicalApprovalRequestDto techApprovalRequestDto)
			throws Exception {
		try {
			techApprovalRequestDto.setId(id);
			return new ResponseEntity<Object>(technicalApprovalService.cancelTechnicalApproval(techApprovalRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelTechnicalApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
