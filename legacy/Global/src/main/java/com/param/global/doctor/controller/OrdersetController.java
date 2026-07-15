package com.param.global.doctor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrderSetReqDto;
import com.param.global.doctor.dto.OrdersetCreationReqDto;
import com.param.global.doctor.dto.OrdersetMasterDto;
import com.param.global.doctor.service.IOrdersetMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/orderset")
@SuppressWarnings("rawtypes")
public class OrdersetController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OrdersetController.class);

	
	@Autowired
	private IOrdersetMasterService iOrdersetMasterService;
	
	@RequestMapping(value = "/create" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createOrderSet(@RequestBody OrdersetCreationReqDto reqDto){
		try{
			return iOrdersetMasterService.createOrderset(reqDto);
		}catch(Exception e){
		logger.error("createOrderSet failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderSet(@RequestBody OrderSetReqDto reqDto){
		try{
			return iOrdersetMasterService.getOrderSet(reqDto);
		}catch(Exception e){
		logger.error("getOrderSet failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	@RequestMapping(value = "/orders" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderSetOrders(@RequestBody List<OrderSetReqDto> reqDto){
		try{
			return iOrdersetMasterService.getOrderSetOrders(reqDto);
		}catch(Exception e){
		logger.error("getOrderSetOrders failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	//Ganesh Start 04-06-19
	
	
	@RequestMapping(value = "/orderSetSearch/{unitId}/{searchkeyWord}/{orderSetActiveTab}" , method = RequestMethod.GET)
	public Response orderSetSearch(@PathVariable Map<String,String> params){
		try{
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int orderSetActiveTab = Integer.parseInt(params.get("orderSetActiveTab").trim());
			String searchkeyWord = params.get("searchkeyWord").trim();
			return iOrdersetMasterService.orderSetSearch(unitId,searchkeyWord,orderSetActiveTab);
		}catch(Exception e){
		logger.error("orderSetSearch failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	@RequestMapping(value = "/inactivateOrderSet/{ordersetMasterId}/{status}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response changeStatusOrderSet(@PathVariable Map<String,String> params){
		try{
			int ordersetMasterId = Integer.parseInt(params.get("ordersetMasterId").trim());
			Character status = params.get("status").trim().charAt(0);
			return iOrdersetMasterService.changeStatusOrderSet(ordersetMasterId,status);
		}catch(Exception e){
		logger.error("changeStatusOrderSet failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/orderSetsList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderSetsList(@RequestBody OrdersetMasterDto dto){
		try{
			return iOrdersetMasterService.getOrderSetsList(dto);
		}catch(Exception e){
		logger.error("getOrderSetsList failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	@RequestMapping(value = "/orderSetsCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderSetsListCount(@RequestBody OrdersetMasterDto dto){
		try{
			return iOrdersetMasterService.getOrderSetsListCount(dto);
		}catch(Exception e){
		logger.error("getOrderSetsListCount failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/orderSetsDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response orderSetsDetails(@RequestBody OrdersetMasterDto dto){
		try{
			return iOrdersetMasterService.orderSetsDetails(dto);
		}catch(Exception e){
		logger.error("orderSetsDetails failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateOrderSetsDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOrderSetsDetails(@RequestBody OrdersetCreationReqDto reqDto){
		try{
			return iOrdersetMasterService.updateOrderSetsDetails(reqDto);
		}catch(Exception e){
		logger.error("updateOrderSetsDetails failed", e);
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}
	
	
}
