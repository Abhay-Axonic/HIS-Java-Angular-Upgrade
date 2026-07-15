package com.param.pharmacy.master.global.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
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
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.tax.GetTaxResponse;
import com.param.pharmacy.master.global.dto.tax.TaxDetailsResponse;
import com.param.pharmacy.master.global.dto.tax.TaxListRespone;
import com.param.pharmacy.master.global.dto.tax.TaxRequest;
import com.param.pharmacy.master.global.dto.tax.TaxResponseList;
import com.param.pharmacy.master.global.service.tax.ITaxService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "api/globals/tax/")
public class TaxController {

	private static final Logger logger = Logger.getLogger(TaxController.class);

	@Autowired
	ITaxService TaxService;

	@RequestMapping(value = "/dropdown/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = TaxResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getTax(@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(TaxService.getDropdownList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "org/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Tax", response = TaxListRespone.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GetTaxResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<GetTaxResponse>> response = new ResponseEntity<List<GetTaxResponse>>(TaxService.getAll(orgId),
				HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Tax", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tax Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax"),
			@ApiResponse(code = 403, message = "Update Tax is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(@RequestBody TaxRequest taxRequest) {
		return new ResponseEntity<CommonResponse>(TaxService.saveTax(taxRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Tax By Id", response = TaxDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<TaxDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<TaxDetailsResponse> response = new ResponseEntity<TaxDetailsResponse>(TaxService.getById(id),
				HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Tax", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tax Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax"),
			@ApiResponse(code = 403, message = "Update Tax is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody TaxRequest taxRequest) {
		taxRequest.setId(id);
		try {
			return new ResponseEntity<Object>(TaxService.updateTax(taxRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

}
