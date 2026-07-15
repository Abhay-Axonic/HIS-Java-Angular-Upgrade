package com.param.procurement.consignment.controller.consignmentreturn;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.common.ServiceException;
import com.param.procurement.consignment.dto.consignmentReturn.ConsignmentReturnDropdown;
import com.param.procurement.consignment.dto.consignmentReturn.ConsignmentReturnResponse;
import com.param.procurement.consignment.dto.consignmentReturn.ImplantReturnSave;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstIssueResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnDetails;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnRequest;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnResponse;
import com.param.procurement.consignment.service.consignmentReturn.IConsignmentReturnService;
import com.wordnik.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/procurements/consignments/implantreturn/")
public class ConsignmentReturnController {

	final static Logger logger = Logger.getLogger(ConsignmentReturnController.class);

	@Autowired
		IConsignmentReturnService consignmentReturnService;

		@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list for populating dropdown values", response = ConsignmentReturnDropdown.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
			try {
				return new ResponseEntity<Object>(consignmentReturnService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error("getDropdownList failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/consignment/search", method = RequestMethod.POST)
		@ApiOperation(value = "Get Patient Issue Return list ", response = PatientAgainstIssueResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> consignmentMainSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.consignmentMainSearch(patientIssueReturnRequest), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("consignmentMainSearch failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
	
		@RequestMapping(value = "/implant/search", method = RequestMethod.POST)
		@ApiOperation(value = "Get Patient Issue Return list ", response = PatientAgainstItemResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> implantMainSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.implantMainSearch(patientIssueReturnRequest), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("implantMainSearch failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get Patient Issue Return list ", response = PatientIssueReturnDetails.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> patientIssueReturnById(@PathVariable Integer id, @PathVariable Integer unitId ) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.patientIssueReturnById(id, unitId), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("patientIssueReturnById failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		} 
		
		@RequestMapping(value = "/ConsAndImplagainstIssue/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiOperation(value = "Get Patient Against Issue list ", response = PatientAgainstIssueResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })	
		public ResponseEntity<Object> patientAgainstIssueSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.patientAgainstIssueSearch(patientIssueReturnRequest), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("patientAgainstIssueSearch failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/ConsAndImplagainstItem/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiOperation(value = "Get Implant Against Item list ", response = PatientAgainstItemResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> patientAgainstItemSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.patientAgainstItemSearch(patientIssueReturnRequest), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("patientAgainstItemSearch failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/AgainstIssueAndAgainstItem/new", method = RequestMethod.POST)
		@ApiOperation(value = "Add issue to Implant return", response =PatientIssueReturnAddItemResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> patientIssueReturnAddNew(@RequestBody PatientIssueReturnAddItem patientIssueReturnAddItem) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.patientIssueReturnAddNew(patientIssueReturnAddItem), HttpStatus.OK);

			}
			catch(Exception ex)
			{
				logger.error("patientIssueReturnAddNew failed", ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/save", method = RequestMethod.POST)
		@ApiOperation(value = "save Implant Return", response = ImplantIndent.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> implantReturnSave(@RequestBody ImplantReturnSave implantReturnSave) {
			try
			{
				return new ResponseEntity<Object>(consignmentReturnService.implantReturnSave(implantReturnSave), HttpStatus.OK);
			}
			catch(Exception ex)
			{
				logger.error("implantReturnSave failed", ex);
				throw new ServiceException(ex.getMessage());
			}
			
		}
		
}
