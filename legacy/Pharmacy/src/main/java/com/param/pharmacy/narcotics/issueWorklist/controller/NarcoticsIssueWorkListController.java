package com.param.pharmacy.narcotics.issueWorklist.controller;


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
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.service.pendingIssues.IPendingIssuesService;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDropdown;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;
import com.param.pharmacy.narcotics.issueWorklist.service.INarcoticsIssueWorklistService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/narcotics/issue/worklist")
public class NarcoticsIssueWorkListController {

	@Autowired
	private INarcoticsIssueWorklistService narcoticsIssueWorklistService;
	
	@Autowired
	private IPendingIssuesService pendingIssuesService;
	
	@Autowired
	IWalkInSalesService WalkInSalesService;

	private static final Logger logger = Logger.getLogger(NarcoticsIssueWorkListController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = NarcoticsDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(narcoticsIssueWorklistService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get issue worklist ", response = NarcoticsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> narcoticsIssueWorklistSearch(@RequestBody NarcoticsRequest narcoticsRequest) {
		try
		{
			return new ResponseEntity<Object>(narcoticsIssueWorklistService.narcoticsIssueWorklistSearchPT(narcoticsRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get issue worklist details ", response = PharmacologyValidationDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> narcoticsIssueWorklistDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId,@PathVariable Integer storeId) {
		try
		{
			return new ResponseEntity<Object>(narcoticsIssueWorklistService.narcoticsDetails(id,unitId,storeId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save issue worklist", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> pendingIssuesSave(@RequestBody PendingIssuesSave request) {
		try {

			Response statausres = pendingIssuesService.pendingIssueStatus(request, 1);

			if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
					|| statausres.getObject() == com.param.common.constants.Status.Closed.getStatus()) {
				CommonResponse response = new CommonResponse();
				response.setMessage("This transaction already processed by other user");
				return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			request.setFromNarcotics(true);
			CommonResponse res = pendingIssuesService.pendingIssuesSave(request);

			List<SaleDetail> details = WalkInSalesService.getSaleDetailsBySaleId(res.getId());

			if (details != null && details.size() > 0) {
				pendingIssuesService.updateInsertIntoStoreStock(details, res.getId(), request);
			}

			if (res.getObject() != null) {
				PendingIssuesSave issueData = (PendingIssuesSave) res.getObject();
				pendingIssuesService.savePatientIssueAcceptance(issueData);
			}

			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (com.param.common.dto.CustomException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}