//package com.param.pharmacy.op.controller;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Map;
//
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.param.pharmacy.op.dto.OpWalkinSalesDto;
//import com.param.pharmacy.op.dto.PharmaSalesReturnMasterDto;
//import com.param.pharmacy.op.dto.SaveResponseDto;
//import com.param.pharmacy.op.service.BillSalesReturnService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
///*@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST,
//		RequestMethod.PATCH }, allowedHeaders = "*")*/
//@RestController
//@RequestMapping(value = "/api/pharmacy")
//public class BillSalesReturnController {
//
//	@Autowired
//	BillSalesReturnService billSalesReturnService;
//	 
//	 
//	
//	@RequestMapping(value = "/billSalesReturns/billdetails/{billId}", method = RequestMethod.GET)
//	@ApiOperation(value = "Get BIll Details", response = OpWalkinSalesDto.class)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
//			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
//			@ApiResponse(code = 404, message = "not found!!!") })
//	public @ResponseBody ResponseEntity<Map> getBillListDetailsById(@PathVariable("billId") Integer billId){
//		Map  billListDetails = billSalesReturnService.getBillListDetailsById(billId) ;
//		return new ResponseEntity<Map>(billListDetails, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/billSalesReturns/search", method = RequestMethod.POST, consumes = "application/json")
//	@ApiOperation(value = "Get Return master  Details ", response = OpWalkinSalesDto.class)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
//			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
//			@ApiResponse(code = 404, message = "not found!!!") })
//	public @ResponseBody ResponseEntity<org.json.simple.JSONArray> getSalesReturnDetails(@RequestBody OpWalkinSalesDto opWalkinSalesDto) throws JSONException{
//		String fromDate = opWalkinSalesDto.getFromDate();
//		String toDate = opWalkinSalesDto.getToDate();
//		
//		SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
//		SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");
//		
//		if(fromDate != "" && fromDate != null )
//		{
//			 try {
//			 Date date = inSDF.parse(fromDate);
//			 fromDate = outSDF.format(date);
//			 } catch (ParseException ex){ 
//		        }   
//		}
//		
//		if(toDate != "" && toDate != null)
//		{
//			 try {
//				 Date date = inSDF.parse(toDate);
//				 toDate = outSDF.format(date);
//				 } catch (ParseException ex){ 
//			        }
//			
//		}
//		opWalkinSalesDto.setFromDate(fromDate);
//		opWalkinSalesDto.setToDate(toDate);
//		org.json.simple.JSONArray walkInSales = billSalesReturnService.getSalesReturnDetails(opWalkinSalesDto) ;
//		return new ResponseEntity<org.json.simple.JSONArray>(walkInSales, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/billSalesReturns/details/{returnId}", method = RequestMethod.GET)
//	@ApiOperation(value = "Get Return Details By Id", response = OpWalkinSalesDto.class)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
//			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
//			@ApiResponse(code = 404, message = "not found!!!") })
//	public @ResponseBody ResponseEntity<Map> findReturnsDetailsListsById(@PathVariable("returnId") Integer returnId) throws JSONException{
//		Map  returnDetails = billSalesReturnService.findReturnsDetailsListsById(returnId) ;
//		return new ResponseEntity<Map>(returnDetails, HttpStatus.OK);
//	}
//	
//	
//	@RequestMapping(value = "/billSalesReturns/findbillListDetails", method = RequestMethod.POST, consumes = "application/json")
//	@ApiOperation(value = "Get Bill List Details for Return", response = OpWalkinSalesDto.class)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
//			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
//			@ApiResponse(code = 404, message = "not found!!!") })
//	public @ResponseBody ResponseEntity<org.json.simple.JSONArray> findbillListDetails(@RequestBody OpWalkinSalesDto opWalkinSalesDto) throws JSONException{
//		String fromDate = opWalkinSalesDto.getFromDate();
//		String toDate = opWalkinSalesDto.getToDate();
//		
//		SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
//		SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");
//		
//		if(fromDate != "" && fromDate != null)
//		{
//			 try {
//			 Date date = inSDF.parse(fromDate);
//			 fromDate = outSDF.format(date);
//			 } catch (ParseException ex){ 
//		        }   
//		}
//		
//		if(toDate != "" && toDate != null)
//		{
//			 try {
//				 Date date = inSDF.parse(toDate);
//				 toDate = outSDF.format(date);
//				 } catch (ParseException ex){ 
//			        }
//			
//		}
//		
//		ArrayList<Integer> salesTypeIdarray = opWalkinSalesDto.getSalesTypeIds();
//		
//		String salesTypeId = "";
//		 
//		if(!(salesTypeIdarray.isEmpty()))
//		{
//			salesTypeId = salesTypeIdarray.toString();
//			
//			salesTypeId = salesTypeId.substring(1, salesTypeId.length()-1);
//		}
//		
//		opWalkinSalesDto.setSalesTypeId(salesTypeId);
//		opWalkinSalesDto.setFromDate(fromDate);
//		opWalkinSalesDto.setToDate(toDate);
//		org.json.simple.JSONArray walkInSales = billSalesReturnService.findbillListDetails(opWalkinSalesDto) ;
//		return new ResponseEntity<org.json.simple.JSONArray>(walkInSales, HttpStatus.OK);
//	}
//	
//	
//	@RequestMapping(value = "/billSalesReturns/create", method = RequestMethod.POST, consumes = "application/json")
//	@ApiOperation(value = "Create Sales Return ", notes = "creating a New Sales Return ")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return  Created successfully"),
//			@ApiResponse(code = 401, message = "You are Not authorized to create Return In Sales"),
//			@ApiResponse(code = 403, message = "Create Return In Sales is forbidden"),
//			@ApiResponse(code = 404, message = "Resource Not found"),
//			@ApiResponse(code = 405, message = "Method Not Allowed") })
//	public ResponseEntity<SaveResponseDto> saveReturnSales(@RequestBody PharmaSalesReturnMasterDto salesReturnMasterDto) {
//		return new ResponseEntity<SaveResponseDto> (billSalesReturnService.createReturnSales(salesReturnMasterDto), HttpStatus.OK);
//	}
//	
//}
//
