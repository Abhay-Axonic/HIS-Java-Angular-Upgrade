package com.param.linenLaundry.rejectedLinenAcceptance.controller;

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
import com.param.linenLaundry.rejectedLinenAcceptance.dto.LinensDropdownResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenRequest;
import com.param.linenLaundry.rejectedLinenAcceptance.dto.RejectedLinenResponse;
import com.param.linenLaundry.rejectedLinenAcceptance.service.IRejectedLinenAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

	@RestController
	@RequestMapping(value = "/api/linenLaundry/reject/linen/acceptance")
	public class RejectedLinenAcceptanceController {

		private static final Logger logger = Logger.getLogger(RejectedLinenAcceptanceController.class);

		@Autowired
		IRejectedLinenAcceptanceService RejectedLinenAcceptanceService;

		@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
		@ApiOperation(value = "Get list for populating dropdown values", response = LinensDropdownResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {
			try {
				return new ResponseEntity<Object>(RejectedLinenAcceptanceService.getDropdowns(), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("Error while fetching dropdowns", ex);
				throw new ServiceException(ex.getMessage());
			}
		}


	/*	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
		@ApiOperation(value = "Get list of Reject Linen Acceptance List by search", response = RejectedLinenResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getRejectLinenAcceptanceList(
				@RequestBody RejectedLinenRequest request) throws Exception {
			try {
				return new ResponseEntity<Object>(
						RejectedLinenAcceptanceService.getRejectLinenAcceptanceList(request), HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<Object>(
						new ErrorResponse("HIS-1282", "Error while fetching the Linen Issue Acceptance list", ex),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}*/
	}
/*
		@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		@ApiOperation(value = "Get Reject Linen Acceptance details by id", response = CommonResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getRejectLinenAcceptanceDetails(@PathVariable Integer id) throws Exception {
			try {
				return new ResponseEntity<Object>(RejectedLinenAcceptanceService.getRejectLinenAcceptanceDetails(id),
						HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<Object>(
						new ErrorResponse("HIS-1284", "Error while fetching the Linen Issue Acceptance Details", ex),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

		@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
		@ApiOperation(value = "Update Reject Linen Acceptance", response = CommonResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> saveRejectLinenAcceptance(@PathVariable Integer id,
				@RequestBody LinenIssueRequest request) throws Exception {
			try {
				request.setId(id);
				return new ResponseEntity<Object>(RejectedLinenAcceptanceService.saveRejectLinenAcceptance(request),
						HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<Object>(
						new ErrorResponse("HIS-1283", "Error while Updating the Linen Issue Acceptance", ex),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
*/