package com.param.pharmacy.nursing.crashCart.controller;

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
import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;
import com.param.pharmacy.nursing.crashCart.service.crashCartMedication.ICrashCartMedicationAdministrationApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/pharmacy/nursing/clinicalWorkstation/order/crashCart/medications/approval")
public class CrashCartMedicationAdministrationApprovalController {

	private static final Logger logger = Logger.getLogger(CrashCartMedicationAdministrationApprovalController.class);

	@Autowired
	ICrashCartMedicationAdministrationApprovalService CrashCartMedicationAdministrationApprovalService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Crash Cart Medication by search", response = CrashCartResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getCrashCartMedicationList(@RequestBody CrashCartRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(
					CrashCartMedicationAdministrationApprovalService.getCrashCartMedicationList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Crash Cart Medication details ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> crashCartMedicationDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(
					CrashCartMedicationAdministrationApprovalService.crashCartMedicationDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Crash Cart Medication", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateCrashCartMedication(@PathVariable Integer id,
			@RequestBody CrashCartRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(
					CrashCartMedicationAdministrationApprovalService.updateCrashCartMedication(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
