package com.param.procurement.consignment.controller.ImplantReturnAcceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceDropdown;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceRequest;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceResponse;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceSave;
import com.param.procurement.consignment.service.implantReturnAcceptance.IImplantReturnAcceptanceService;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignments/ir/acceptance")
public class ImplantReturnAcceptanceController
{
	final static Logger logger = Logger.getLogger(ImplantReturnAcceptanceController.class);

	@Autowired
	private IImplantReturnAcceptanceService implantReturnAcceptanceService;

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = ImplantReturnAcceptanceDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(implantReturnAcceptanceService.getDropdownList(unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/consignment/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get return acceptance list ", response = ImplantReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> consignmentReturnAcceptanceSearch(@RequestBody ImplantReturnAcceptanceRequest implantReturnAcceptanceRequest) {
		try
		{
			return new ResponseEntity<Object>(implantReturnAcceptanceService.consignmentReturnAcceptanceSearch(implantReturnAcceptanceRequest), HttpStatus.OK);
			
		}
		catch (Exception ex) {
			logger.error("consignmentReturnAcceptanceSearch failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/implant/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get return acceptance list ", response = ImplantReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> implantReturnAcceptanceSearch(@RequestBody ImplantReturnAcceptanceRequest implantReturnAcceptanceRequest) {
		try
		{
			return new ResponseEntity<Object>(implantReturnAcceptanceService.implantReturnAcceptanceSearch(implantReturnAcceptanceRequest), HttpStatus.OK);
			
		}
		catch (Exception ex) {
			logger.error("implantReturnAcceptanceSearch failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get return acceptance details ", response = ImplantReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnAcceptanceDetails(@PathVariable("id") Integer id, @PathVariable("id") Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(implantReturnAcceptanceService.implantReturnAcceptanceDetails(id, unitId), HttpStatus.OK);
			
		}
		catch (Exception ex) {
			logger.error("returnAcceptanceDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/save", method = RequestMethod.PATCH,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "save Patient Return Acceptance", response = ImplantReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> implantReturnAcceptanceSave(@RequestBody ImplantReturnAcceptanceSave returnAcceptanceSave) {
		try
		{
			return new ResponseEntity<Object>(implantReturnAcceptanceService.implantReturnAcceptanceConsignmentSave(returnAcceptanceSave), HttpStatus.OK);
		}
		catch (Exception ex) {
			logger.error("implantReturnAcceptanceSave failed", ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
}
