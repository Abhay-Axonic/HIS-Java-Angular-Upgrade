package com.param.pharmacy.op.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PatientCategoryMasterDto;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnDto;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSave;
import com.param.pharmacy.op.dto.salesReturn.ResponseAddBill;
import com.param.pharmacy.op.dto.salesReturn.ResponseReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.ResponseSalesReturnBillList;
import com.param.pharmacy.op.dto.salesReturn.ReturnDetails;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnBillListSerach;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnListSearch;
import com.param.pharmacy.op.service.salesReturn.ISalesReturnService;

import in.param.exception.ApplicationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/op/sale/return")
public class SalesReturnController implements ICommonConstants, IError{

	@Autowired
	private ISalesReturnService ISalesReturnService;

	private static final Logger logger = Logger.getLogger(SalesReturnController.class);

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get sales return list ", response = ResponseReturnListSearch.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> salesReturnListSearch(@RequestBody SalesReturnListSearch salesReturnList) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.salesReturnListSearchBP(salesReturnList),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save sale return", response = ResponseAddBill.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> salesReturnSave(@RequestBody RequestSaleReturnSave requestSaleReturnSave) {
		try {
			List<Long> saleIds=new ArrayList<Long>();
			for (RequestSaleReturnSave iterable_element : requestSaleReturnSave.getReuquestSaleReturnList()) {
				saleIds.add(iterable_element.getSaleIds().getFirst()); 
			}
			Double getSalesAmount=ISalesReturnService.getsalesAmount(saleIds,requestSaleReturnSave.getSelfPayeeId());
			Double getReturnedAmount=ISalesReturnService.getReturnedAmount(saleIds);
			DecimalFormat df = new DecimalFormat("#.##");
			if(getSalesAmount<Double.parseDouble(df.format((getReturnedAmount+requestSaleReturnSave.getTotalRefundAmount()))))
			{
				CommonResponse response = new CommonResponse();
				response.setMessage("This transaction already processed by other user");
				return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<Object>(ISalesReturnService.saveSalesReturn(requestSaleReturnSave),
					HttpStatus.OK);
		} 
		catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Id", response = ReturnDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> salesReturnDetailsByIdBP(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.salesReturnDetailsByIdBP(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/get/bill/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of bills", response = ResponseSalesReturnBillList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> salesReturnbillListSearch(
			@RequestBody SalesReturnBillListSerach salesReturnBillListSerach) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.salesReturnBillListSerachBP(salesReturnBillListSerach),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/add/bill/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "add bill by id", response = ResponseAddBill.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> salesReturnGetBillById(@PathVariable("id") List<Long> id) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.salesReturnGetBillByIdBP(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/cancel/{unitId}/{id}/{storeId}", method = RequestMethod.PUT)
	@ApiOperation(value = "cancel sales return bill by id", response = ResponseAddBill.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelsalesReturnById(@PathVariable("unitId") Integer unitId,@PathVariable("id") Integer id,@PathVariable("storeId") Integer storeId) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.cancelsalesReturnById(id,storeId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/update/{unitId}/{id}", method = RequestMethod.PUT)
	@ApiOperation(value = "update sales return bill by id", response = ResponseAddBill.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateSalesReturnById(@PathVariable("unitId") Integer unitId,@RequestBody RequestSaleReturnSave requestSaleReturnSave,@PathVariable("id") Integer id) {
		try {
			return new ResponseEntity<Object>(ISalesReturnService.updateSalesReturnById(requestSaleReturnSave, id,unitId),
					HttpStatus.OK);
		} 
		catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getPatientNameSearchForSalesReturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientCategoryMaster(@RequestBody RequestSaleReturnDto requestSaleReturnDto) {
		try {
			return ISalesReturnService.getPatientNameSearchForSalesReturn(requestSaleReturnDto);
		} catch (ApplicationException ae) {
			logger.error(ae.getMessage(), ae);
			throw new ServiceException(ae.getMessage());
		}
	}

}