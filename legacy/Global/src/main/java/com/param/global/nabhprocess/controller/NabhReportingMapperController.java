package com.param.global.nabhprocess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.nabhprocess.service.INabhReportingMapperService;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.dto.NabhReportingMapperDto;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes" })
@RequestMapping("/api/unit")
public class NabhReportingMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NabhReportingMapperController.class);

	@Autowired
	private INabhReportingMapperService iNabhReportingMapperService;

	@RequestMapping(value = "/saveNabhReportingMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNabhReportingMapper(@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
		try {
			return iNabhReportingMapperService.saveNabhReportingMapper(nabhReportingMapperDto);

		} catch (Exception e) {
		logger.error("saveNabhReportingMapper failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
//	@RequestMapping(value = "/updateNabhReportingMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response updateNabhReportingMapper(@RequestBody IncidentReportingMapperDto incidentReportingMapperDto) {
//		try {
//			return iNabhReportingMapperService.updateNabhReportingMapper(nabhReportingMapperDto);          
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
//
	@RequestMapping(value = "/getNabhReportingMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNabhReportingMapperById(@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
		try {
			return iNabhReportingMapperService.getNabhReportingMapperById(nabhReportingMapperDto);

		} catch (Exception e) {
		logger.error("getNabhReportingMapperById failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}

	@RequestMapping(value = "/getNabhReportingMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNabhReportingMapperList(@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
		try {
			return iNabhReportingMapperService.getNabhReportingMapperList(nabhReportingMapperDto);

		} catch (Exception e) {
		logger.error("getNabhReportingMapperList failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}

//	@RequestMapping(value = "/updateNabhReportingMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)  
//	public Response updateNabhReportingMapperStatus(                             
//			@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
//		try {
//			return iNabhReportingMapperService.updateNabhReportingMapperStatus(nabhReportingMapperDto);
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}

//	@RequestMapping(value = "/getActivenabhReportingMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response getActiveNabhReportingMapperList(
//			@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
//		try {
//			return iNabhReportingMapperService.getActiveNabhReportingMapperList(nabhReportingMapperDto);
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}

//	@RequestMapping(value = "/getNabhReportingMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE) 
//	public Response getNabhReportingMapperCount(
//			@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
//		try {
//			return iNabhReportingMapperService.getActiveNabhReportingMapperList(nabhReportingMapperDto);
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}

//	@RequestMapping(value = "/getRoleWiseUserList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)       
//	public Response getRoleWiseUserList(@RequestBody NabhReportingMapperDto nabhReportingMapperDto) {
//		try {
//			return iNabhReportingMapperService.getRoleWiseUserList(nabhReportingMapperDto);
//
//		} catch (Exception e) {
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
}

