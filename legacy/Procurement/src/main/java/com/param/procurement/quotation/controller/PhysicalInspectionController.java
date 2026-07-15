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

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDetails;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDropdown;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;
import com.param.procurement.quotation.service.physicalInspection.IPhysicalInspectionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/physical/inspection")
public class PhysicalInspectionController {

	final static Logger logger = Logger.getLogger(PhysicalInspectionController.class);

	@Autowired
	IPhysicalInspectionService PhysicalInspectionService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = PhysicalInspectionDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get physical inspection list", response = PhysicalInspectionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPhysicalnspectionList(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.getPhysicalnspectionList(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPhysicalnspectionList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ApiOperation(value = "Get physical inspection details", response = PhysicalInspectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPhysicalnspectionDetails(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.getPhysicalnspectionDetails(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPhysicalnspectionDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/detailsConsignmentStockConversion", method = RequestMethod.POST)
	@ApiOperation(value = "Get physical inspection details", response = PhysicalInspectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockConversionDetails(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.getConsignmentStockConversionDetails(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockConversionDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save physical inspection", response = PhysicalInspectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> savePhysicalnspection(@RequestBody PhysicalInspectionSave physicalInspectionSave) throws Exception {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.savePhysicalnspection(physicalInspectionSave), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("savePhysicalnspection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
