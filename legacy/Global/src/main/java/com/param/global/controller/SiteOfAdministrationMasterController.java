package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SiteOfAdministrationMasterDto;
import com.param.global.service.SiteOfAdministrationMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global/clinical")
@SuppressWarnings({"rawtypes"})
public class SiteOfAdministrationMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(SiteOfAdministrationMasterController.class);

	@Autowired
	private SiteOfAdministrationMasterService siteOfAdministrationMasterService;
	
	@Autowired
	SessionService sessionService;
	
	@RequestMapping(value = "/saveSiteMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveSiteMaster(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto){
		
		try {
			return siteOfAdministrationMasterService.saveSiteOfAdministrationMaster(siteOfAdministrationMasterDto);
			
		} catch (Exception e) {
		logger.error("saveSiteMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateSiteMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateSiteMaster(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto){
		
		try {
			return siteOfAdministrationMasterService.updateSiteOfAdministrationMaster(siteOfAdministrationMasterDto);
			
		} catch (Exception e) {
		logger.error("updateSiteMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateSiteMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateSiteMasterStatus(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto){
		
		try {
			return siteOfAdministrationMasterService.updateSiteOfAdministrationMasterStatus(siteOfAdministrationMasterDto);
		} catch (Exception e) {
		logger.error("updateSiteMasterStatus failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getSiteMasterById/{siteId}/{organizationId}", method = RequestMethod.GET)
	public Response getSiteMasterById(@PathVariable ("siteId") Integer siteId,
									@PathVariable ("organizationId") Integer organizationId
									){
		
		try {
			return siteOfAdministrationMasterService.getSiteOfAdministrationMasterById(siteId, organizationId);
			
		} catch (Exception e) {
		logger.error("getSiteMasterById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getSiteMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSiteMasterList(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto, HttpServletRequest request){

		try {
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}
			return siteOfAdministrationMasterService.getSiteOfAdministrationMasterList(siteOfAdministrationMasterDto, sessionService.getOrganizationId());

		} catch (Exception e) {
		logger.error("getSiteMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getSiteMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSiteMasterCount(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto){
		
		try {
			return siteOfAdministrationMasterService.getSiteOfAdministrationMasterCount(siteOfAdministrationMasterDto);
			
		} catch (Exception e) {
		logger.error("getSiteMasterCount failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveSiteMaster/{organizationId}", method = RequestMethod.GET)
	public Response getActiveSiteMaster(@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return siteOfAdministrationMasterService.getActiveSiteOfAdministrationMaster(organizationId);
			
		} catch (Exception e) {
		logger.error("getActiveSiteMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getSiteMasterListSearch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSiteMasterListSearch(@RequestBody SiteOfAdministrationMasterDto siteOfAdministrationMasterDto, HttpServletRequest request){

		try {
				
			return siteOfAdministrationMasterService.getSiteOfAdministrationMasterSearchList(siteOfAdministrationMasterDto);

		} catch (Exception e) {
		logger.error("getSiteMasterListSearch failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
