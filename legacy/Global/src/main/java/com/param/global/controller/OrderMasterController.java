package com.param.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/global")
@SuppressWarnings("rawtypes")
public class OrderMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OrderMasterController.class);


	@Autowired
	private IOrderMasterService iOrderMasterService;
	
	@Autowired
	private IOrderDetailsMasterService iOrderDetailsMasterService; 
	
	@RequestMapping(value="orderMaster",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveOrderMaster(@RequestBody OrderMasterDto orderMasterDto) {
		try {
			//RequestJsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return iOrderMasterService.saveOrderMaster(orderMasterDto);
		}catch (Exception e) {
		logger.error("saveOrderMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	@RequestMapping(value="/saveNeuronPlusorderedServices",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveNeuronPlusorderedServices(@RequestBody OrderMasterDto orderMasterDto) {
		try {
			return iOrderMasterService.saveNeuronPlusorderedServices(orderMasterDto);
		}catch (Exception e) {
		logger.error("saveNeuronPlusorderedServices failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getOrderedMedication",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOrderedMedication(@RequestBody BillingMasterDto dto ) {
		try {
			//RequestJsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return iOrderMasterService.getOrderedMedication(dto);
		}catch (Exception e) {
		logger.error("getOrderedMedication failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getVirtualStoreId",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getVirtualStoreId(@RequestBody BillingMasterDto dto ) {
		try {
			//RequestJsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return iOrderMasterService.getVirtualStoreId(dto);
		}catch (Exception e) {
		logger.error("getVirtualStoreId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/orderMedicationService",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response orderMedicationService(@RequestBody List<OrderDetailsMasterDto> list ) {
		try {
			//RequestJsonCheck.mapJsonToObject(orderMasterDto, OrderMasterDto.class);
			return iOrderMasterService.orderMedicationService(list);
		}catch (Exception e) {
		logger.error("orderMedicationService failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateDoctorShareRisByChangingDoctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorShareRisByChangingDoctor(@RequestBody OrderDetailsMasterDto orderDetailsMasterDto) {
		try {
			return iOrderDetailsMasterService.updateDoctorShareRisByChangingDoctor(orderDetailsMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	/*@RequestMapping(value="/saveNeuronPlusorderedServices", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveNeuronPlusorderedServices(@RequestBody String json ) {
	try {

	JsonCheck.mapJsonToObject(json, OrderMasterDto.class);
	return null;
	DoctorMasterDto doctorMasterDto
	//return iDoctorMasterService.saveDoctorRegistration(doctorMasterDto);
	}catch (Exception e) {
	
	}
	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}*/
/*	@RequestMapping(value="cancelOrderServices",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response creditNoteCancelSerivces(@RequestBody ordr orderMasterDto,Boolean flag) {
		try {
			return iOrderMasterService.creditNoteCancelSerivces(orderMasterDto,flag);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
}
