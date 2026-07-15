package com.param.pharmacy.theatreManagement.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.procedure.dto.OTCompletionRequest;
import com.param.global.service.IOTCompletionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/theatre/management")
public class OTCompletionController {

	@Autowired
	IOTCompletionService OTCompletionService;

	private static final Logger logger = Logger.getLogger(OTCompletionController.class);

	@RequestMapping(value = "/{isImplant}/{search}/{storeId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> search(@PathVariable("isImplant") boolean isImplant, @PathVariable("search") String search, @PathVariable("storeId") Integer storeId,
			@PathVariable("unitId") Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(OTCompletionService.itemsSearch(isImplant, search, storeId, unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveOTCompletion(@RequestBody OTCompletionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(OTCompletionService.saveOTCompletion(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@RequestMapping(value = "getIssueAcceptanceData", method = RequestMethod.GET)
	public Response getIssueAcceptanceData(){
		try {
			return OTCompletionService.getIssueAcceptanceData();
			
		} catch (Exception e) {
			
			return new Response<>("error", "500", "error", null, null);
		}
	}
	*/
	@RequestMapping(value = "getIssueAcceptanceData", method = RequestMethod.POST)
	public Response getIssueAcceptanceData(@RequestBody OtcCompletionInvMedicationDto otcCompletionInvMedicationDto){
	try {
	return OTCompletionService.getIssueAcceptanceData(otcCompletionInvMedicationDto);

	} catch (Exception e) {
	logger.error(e.getMessage(), e);
	throw new ServiceException(e.getMessage());
	}
	}
}
