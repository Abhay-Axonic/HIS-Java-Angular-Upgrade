package com.param.pharmacy.ip.controller;

import org.apache.log4j.Logger;
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
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerSave;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.service.assignPicker.IAssignPickerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/assign/picker")
public class AssignPickerController {

	private static final Logger logger = Logger.getLogger(AssignPickerController.class);

	@Autowired
	private IAssignPickerService assignPickerService;

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try {
			return new ResponseEntity<Object>(assignPickerService.getDropdownList(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get assign picker list ", response = PharmacologyValidationSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> assignPickerSearch(
			@RequestBody PharmacologyValidationSearch pharmacologyValidationSearch) {
		try {
			return new ResponseEntity<Object>(assignPickerService.assignPickerSearchBP(pharmacologyValidationSearch),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{storeId}/{userId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get assign picker details ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pharmacologyValidationDetails(@PathVariable("id") Integer id,
			@PathVariable Integer unitId,
			@PathVariable Integer orgId,
			@PathVariable Integer storeId,
			@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(assignPickerService.assignPickerDetailsBP(id,unitId,orgId,storeId,userId), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@RequestMapping(value = "/substitute/{unitId}/{id}/{itemId}/{isNarcotic}/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get substitute By Id", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getSubstitute(@PathVariable("id") Integer id,
									            @PathVariable("itemId") Integer itemId, 
									            @PathVariable("isNarcotic") Boolean isNarcotic, 
									            @PathVariable("storeId") Integer storeId,
												@PathVariable("unitId") Integer unitId)
	{
		try {
			return new ResponseEntity<Object>(assignPickerService.getSubstitute(id, itemId, isNarcotic, storeId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1214", "Error while getting substitute by id", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}*/
	
	/**Added by chetana on 26-11-2021 for HIS-17599 QNRI - Substitute screen should sort depending on formulation type added in prescription. Old API has been commented above.**/
	@RequestMapping(value = "/substitute/{unitId}/{id}/{itemId}/{isNarcotic}/{storeId}/{formulationTypeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get substitute By Id", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getSubstitute(@PathVariable("id") Integer id,
									            @PathVariable("itemId") Integer itemId, 
									            @PathVariable("isNarcotic") Boolean isNarcotic, 
									            @PathVariable("storeId") Integer storeId,
												@PathVariable("unitId") Integer unitId,
												@PathVariable("formulationTypeId") Integer formulationTypeId)
	{
		try {
			return new ResponseEntity<Object>(assignPickerService.getSubstitute(id, itemId, isNarcotic, storeId,unitId, formulationTypeId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save assign picker", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> assignPickerSave(@RequestBody AssignPickerSave assignPickerSave) {
		try {
			 
			new ResponseEntity<Object>(assignPickerService.assignPickerSave(assignPickerSave, false), HttpStatus.OK);
			
			return new ResponseEntity<Object>(assignPickerService.assignPickerSave(assignPickerSave,true), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	
	@RequestMapping(value = "/cancel", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel assign picker", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelAssignPicker(@RequestBody CancelRequest cancelRequest) {
		try {
			assignPickerService.cancelAssignPicker(cancelRequest);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

}