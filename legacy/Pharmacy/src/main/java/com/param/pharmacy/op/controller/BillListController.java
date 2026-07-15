
package com.param.pharmacy.op.controller;

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
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.pharmacy.op.dto.billList.BillListDropdownResponse;
import com.param.pharmacy.op.dto.billList.BillListRequest;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;
import com.param.pharmacy.op.service.billList.IBillListService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/sales/bills")
public class BillListController {

	private static final Logger logger = Logger.getLogger(BillListController.class);

	@Autowired
	IBillListService BillListService;
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService; 

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = BillListDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropDownValues() throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.getDropdowns(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of sales bill list by search", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBillList(@RequestBody BillListRequest billListRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.getBillList(billListRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{selfPayeeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get sale bill details", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBillDetails(@PathVariable("id") Long id,@PathVariable Integer unitId,@PathVariable Integer selfPayeeId) throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.getBillDetails(id,selfPayeeId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/save/bill", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save sale bill details", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody synchronized ResponseEntity<Object> saveBillDetails(@RequestBody BillSaveRequest request) throws Exception {
		try {
			
			PharamcyBillPayeeDetails master =BillListService.checkSelfDueAmount(request);
		/*	UnitApplicationConfigurationDto configDto = new UnitApplicationConfigurationDto();
			configDto.setOrganizationId(request.getOrgnizationId());
			configDto.setUnitId(request.getUnitId());
			com.param.global.common.Response configRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(configDto);
			if (configRes.getObject() != null) {
				configDto = (UnitApplicationConfigurationDto) configRes.getObject();
			}*/
			
			if(master != null && request.getBalanceAmount() != null){
				
				if(!request.getBalanceAmount().equals( master.getDueAmount())){
					CommonResponse response = new CommonResponse();
					response.setId(request.getBillingMasterId());
					response.setMessage("This transaction already settled by other user ");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
			}
			
			return new ResponseEntity<Object>(BillListService.saveBillDetails(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/badDebtBillsearch", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of sales bill list by search", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> badDebtBillsearch(@RequestBody BillListRequest billListRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.badDebtBillsearch(billListRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/badDebtBillDetials/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get sale bill details", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getbadDebtBillDetials(@PathVariable("id") Long id) throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.badDebtBillDetials(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/badDebtBillPayeeDetails/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get sale bill payee details", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> badDebtBillPayeeDetials(@PathVariable("id") Long id) throws Exception {
		try {
			return new ResponseEntity<Object> (BillListService.badDebtBillPayeeDetials(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/pharmacyReceiptCancellation", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of sales bill list by search", response = BillListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> pharmacyReceiptCancellationFlag(@RequestBody BillListRequest billListRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BillListService.pharmacyReceiptCancellationFlag(billListRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
}