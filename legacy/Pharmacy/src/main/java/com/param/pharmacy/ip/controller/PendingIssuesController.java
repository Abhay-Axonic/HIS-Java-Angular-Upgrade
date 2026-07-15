package com.param.pharmacy.ip.controller;


import java.util.List;

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

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.pharmacology.DrugItems;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.service.pendingIssues.IPendingIssuesService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping(value = "/api/pharmacy/ip/pending/issues")
public class PendingIssuesController implements ICommonConstants{

	@Autowired
	private IPendingIssuesService pendingIssuesService;
	
	@Autowired
	IWalkInSalesService WalkInSalesService;

	private static final Logger logger = Logger.getLogger(PendingIssuesController.class);

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try
		{
			return new ResponseEntity<Object>(pendingIssuesService.getDropdownList(), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pending issues list ", response = PharmacologyValidationSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesSearch(@RequestBody PharmacologyValidationSearch pharmacologyValidationSearch) {
		try
		{
			return new ResponseEntity<Object>(pendingIssuesService.pendingIssuesSearch(pharmacologyValidationSearch), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/pendingpreviousissues", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pending issues list ", response = PharmacologyValidationSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingPreviousIssues(@RequestBody PharmacologyValidationSearch pharmacologyValidationSearch) {
		try
		{
			return new ResponseEntity<Object>(pendingIssuesService.pendingPreviousIssues(pharmacologyValidationSearch), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/{unitId}/{orgId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get pending issues details ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer storeId) {
		try
		{
			return new ResponseEntity<Object>(pendingIssuesService.pendingIssuesDetails(id,unitId,storeId,orgId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save pending issues", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> pendingIssuesSave(@RequestBody PendingIssuesSave pendingIssuesSave) {
		try {

			Response statausres = pendingIssuesService.pendingIssueStatus(pendingIssuesSave, 0);
			
			if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
					|| statausres.getObject() == com.param.common.constants.Status.Closed.getStatus()) {
				CommonResponse response = new CommonResponse();
				response.setMessage("This transaction already processed by other user");
				return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			pendingIssuesSave.setFromPendingIssue(true);
			CommonResponse res = pendingIssuesService.pendingIssuesSave(pendingIssuesSave);

			List<SaleDetail> details = WalkInSalesService.getSaleDetailsBySaleId(res.getId());

			if (details != null && details.size() > 0) {
				pendingIssuesService.updateInsertIntoStoreStock(details, res.getId(), pendingIssuesSave);
			}

			if (res.getObject() != null) {
				PendingIssuesSave issueData = (PendingIssuesSave) res.getObject();
				pendingIssuesService.savePatientIssueAcceptance(issueData);
			}
			CommonResponse finalResponse = new CommonResponse<>();
			finalResponse.setId(res.getId());
			return new ResponseEntity<Object>(finalResponse,HttpStatus.OK);
		} catch (com.param.common.dto.CustomException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/get/drug/items", method = RequestMethod.POST)
	@ApiOperation(value = "save pending issues", response = DrugItems.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDrugItems(@RequestBody PharmacologyValidationSearch request) {
		try
		{
			return new ResponseEntity<Object>(pendingIssuesService.getDrugItems(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel pending issues", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPendingIssues(@RequestBody CancelRequest cancelReq) {
		try{
			pendingIssuesService.cancelPendingIssues(cancelReq);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/cancelPendingIssue", method = RequestMethod.POST)
	@ApiOperation(value = "Cancel pending issues", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response cancelPendingIssuesRequest(@RequestBody CancelRequest cancelReq) {
		try{
			return pendingIssuesService.cancelPendingIssuesRequest(cancelReq);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pendingIssueStatus/{id}", method = RequestMethod.POST)
	@ApiOperation(value = "Cancel pending issues", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response pendingIssueStatus(@RequestBody PendingIssuesSave cancelReq,@PathVariable("id") Integer id) {
		try{
			return pendingIssuesService.pendingIssueStatus(cancelReq,id);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	
	
}