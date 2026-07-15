package com.param.linenLaundry.sendToCondemnate.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.linenLaundry.sendToCondemnate.dto.SendCondemnateDropdownResponse;
import com.param.linenLaundry.sendToCondemnate.service.ISendCondemnateService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


	@EnableWebMvc
	@RestController
	@RequestMapping(value = "/api/linenLaundry/sendToCondemnate")
	public class SendToCondemnateController {

	private static final Logger logger = Logger.getLogger(SendToCondemnateController.class);

		@Autowired
		ISendCondemnateService SendCondemnateService;

		
		@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
		@ApiOperation(value = "Get list for populating dropdown values", response = SendCondemnateDropdownResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {
			try {
				return new ResponseEntity<Object>(SendCondemnateService.getDropdowns(), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
		}
	}
