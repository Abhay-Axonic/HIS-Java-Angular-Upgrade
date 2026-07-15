package com.param.global.otims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.IssueAcceptanceDto;
import com.param.global.otims.dto.ImplantDetailsDto;
import com.param.global.otims.service.IImplantDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/otims")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImplantDetailsController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(ImplantDetailsController.class);

	@Autowired
	private IImplantDetailsService iImplantDetailsService;

	@RequestMapping(value = "/saveImplantdetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveImplantdetails(@RequestBody  List<ImplantDetailsDto> implantDetailsDto) {
		try {
			return iImplantDetailsService.saveImplantdetails(implantDetailsDto);
		} catch (Exception e) {
		logger.error("saveImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getImplantdetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getImplantdetailsList(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.getImplantdetailsList(implantDetailsDto);
		} catch (Exception e) {
		logger.error("getImplantdetailsList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getImplantdetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getImplantdetails(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.getImplantdetails(implantDetailsDto);
		} catch (Exception e) {
		logger.error("getImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getKitdetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getKitdetails(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.getKitdetails(implantDetailsDto);
		} catch (Exception e) {
		logger.error("getKitdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/searchImplantItems", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchImplantItems(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.searchImplantItems(implantDetailsDto);
		} catch (Exception e) {
		logger.error("searchImplantItems failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getImplantDetailsByProcedureId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getImplantDetailsByProcedureId(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.getImplantDetailsByProcedureId(implantDetailsDto);
		} catch (Exception e) {
		logger.error("getImplantDetailsByProcedureId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/searchImplantKit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchImplantKit(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.searchImplantKit(implantDetailsDto);
		} catch (Exception e) {
		logger.error("searchImplantKit failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "getImplantIndentAcceptanceData", method = RequestMethod.POST)
	public Response getImplantIndentAcceptanceData(@RequestBody IssueAcceptanceDto issueAcceptanceDto){
	try {
	return iImplantDetailsService.getImplantIndentAcceptanceData(issueAcceptanceDto);

	} catch (Exception e) {
	logger.error("getImplantIndentAcceptanceData failed", e);
	}
	return new Response<>("error", "500", "error", null, null);
	}
	
	@RequestMapping(value = "/updateImplantDetailsIsExported", method = RequestMethod.POST)
	public Response updateImplantDetailsIsExported(@RequestBody List<ImplantDetailsDto> list){
	try {
	return iImplantDetailsService.updateImplantDetailsIsExported(list);

	} catch (Exception e) {
	logger.error("updateImplantDetailsIsExported failed", e);
	}
	return new Response<>("error", "500", "error", null, null);
	}
	
	@RequestMapping(value = "/getImplantDetailsByProcedureIdOnOTIMS", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getImplantDetailsByProcedureIdOnOTIMS(@RequestBody  ImplantDetailsDto implantDetailsDto) {
		try {
			return iImplantDetailsService.getImplantDetailsByProcedureIdOnOTIMS(implantDetailsDto);
		} catch (Exception e) {
		logger.error("getImplantDetailsByProcedureIdOnOTIMS failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
