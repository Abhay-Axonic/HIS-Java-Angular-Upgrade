package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdRylesTubeDto;
import com.param.opd.coversheet.service.IIpdRylesTubesService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/opd")
@SuppressWarnings({"rawtypes", "unchecked"})
public class IpdRylesTubeController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(IpdRylesTubeController.class);

	
	@Autowired
	IIpdRylesTubesService iIpdRylesTubesService;
	
	@RequestMapping(value = "/saveIpdRylesTubeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIpdRylesTubeOrder(@RequestBody IpdRylesTubeDto ipdRylesTubeDto) {
		try {
			return iIpdRylesTubesService.saveIpdRylesTubeOrder(ipdRylesTubeDto);
		} catch (Exception e) {
		logger.error("saveIpdRylesTubeOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getIpdRylesTubeOrderList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIpdDietOrderList(@RequestBody IpdRylesTubeDto ipdRylesTubeDto) {
		try {
			return iIpdRylesTubesService.getIpdRylesTubeOrderList(ipdRylesTubeDto);
		} catch (Exception e) {
		logger.error("getIpdDietOrderList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateIpdRylesTubeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateIpdRylesTubeOrder(@RequestBody IpdRylesTubeDto ipdRylesTubeDto) {
		try {
			return iIpdRylesTubesService.updateIpdRylesTubeOrder(ipdRylesTubeDto);
		} catch (Exception e) {
		logger.error("updateIpdRylesTubeOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/cancelRylesTubeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelRylesTubeOrder(@RequestBody IpdRylesTubeDto ipdRylesTubeDto) {
		try {
			return iIpdRylesTubesService.cancelRylesTubeOrder(ipdRylesTubeDto);
		} catch (Exception e) {
		logger.error("cancelRylesTubeOrder failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


}
