package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdDietOrderDto;
import com.param.opd.coversheet.service.IIpdDietOrderService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IpdDietOrderController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(IpdDietOrderController.class);

	@Autowired
	IIpdDietOrderService iIpdDietOrderService;

	@RequestMapping(value = "/saveIpdDietOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIpdDietOrder(@RequestBody IpdDietOrderDto ipdDietOrderDto) {
		try {
			return iIpdDietOrderService.saveIpdDietOrder(ipdDietOrderDto);
		} catch (Exception e) {
		logger.error("saveIpdDietOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getIpdDietOrderList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIpdDietOrderList(@RequestBody IpdDietOrderDto ipdDietOrderDto) {
		try {
			return iIpdDietOrderService.getIpdDietOrderList(ipdDietOrderDto);
		} catch (Exception e) {
		logger.error("getIpdDietOrderList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/updateIpdDietOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIpdDietOrder(@RequestBody IpdDietOrderDto ipdDietOrderDto) {
		try {
			return iIpdDietOrderService.updateIpdDietOrder(ipdDietOrderDto);
		} catch (Exception e) {
		logger.error("updateIpdDietOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/cancelDietOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelDietOrder(@RequestBody IpdDietOrderDto ipdDietOrderDto) {
		try {
			return iIpdDietOrderService.cancelDietOrder(ipdDietOrderDto);
		} catch (Exception e) {
		logger.error("cancelDietOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
