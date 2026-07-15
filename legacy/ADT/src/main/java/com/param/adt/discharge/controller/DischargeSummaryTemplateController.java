package com.param.adt.discharge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.service.IDischargeSummaryTemplateService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryTemplateController implements ICommonConstants{
	
	@Autowired
	IDischargeSummaryTemplateService iDischargeSummaryTemplateService;
	
	@RequestMapping(value = "/saveDischargeSummaryTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDischargeSummaryTemplate(@RequestBody DischargeSummaryTemplateDto dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.saveDischargeSummaryTemplate(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargeSummaryTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeSummaryTemplate(@RequestBody DischargeSummaryTemplateDto dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.updateDischargeSummaryTemplate(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargeSummaryTemplateStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeSummaryTemplateStatus(@RequestBody DischargeSummaryTemplateDto dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.updateDischargeSummaryTemplateStatus(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryTemplateById/{dischargeSummaryTempId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDischargeSummaryTemplateById(@PathVariable ("dischargeSummaryTempId") Integer dischargeSummaryTempId ,
												 @PathVariable ("organizationId") Integer organizationId, 
												 @PathVariable ("unitId") Integer unitId) {
		try {
			return iDischargeSummaryTemplateService.getDischargeSummaryTemplateById(dischargeSummaryTempId, organizationId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryTemplateMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeSummaryTemplateMasterList(@RequestBody DischargeSummaryTemplateDto dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.getDischargeSummaryTemplateMasterList(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryTemplateCount/{organizationId}/{unitId}/{searchKeyword}", method = RequestMethod.GET)
	public Response getDischargeSummaryTemplateCount( @PathVariable ("organizationId") Integer organizationId, 
												   @PathVariable ("unitId") Integer unitId,
													@PathVariable(value = "searchKeyword") String searchKeyword) {
		try {
			searchKeyword = (searchKeyword == null || searchKeyword.equalsIgnoreCase("null")) ? "" : searchKeyword;
			return iDischargeSummaryTemplateService.getDischargeSummaryTemplateCount(organizationId, unitId, searchKeyword);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveDischargeSummaryTemplate/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveDischargeSummaryTemplate( @PathVariable ("organizationId") Integer organizationId, 
			                                        @PathVariable ("unitId") Integer unitId) {
		try {
			return iDischargeSummaryTemplateService.getActiveDischargeSummaryTemplate(organizationId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	/*@RequestMapping(value = "/saveListDischargeSummaryTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveListDischargeSummaryTemplate(@RequestBody List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.saveListDischargeSummaryTemplate(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	@RequestMapping(value = "/getDischargeTempDeptListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeTempDeptListById(@RequestBody DischargeSummaryTemplateDto dischargeSummaryTemplateDto) {
		try {
			return iDischargeSummaryTemplateService.getDischargeTempDeptListById(dischargeSummaryTemplateDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
