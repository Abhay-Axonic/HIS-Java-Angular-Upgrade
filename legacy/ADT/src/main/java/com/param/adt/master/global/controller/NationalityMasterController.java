package com.param.adt.master.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.NationalityMasterDto;
import com.param.adt.master.global.service.INationalityMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class NationalityMasterController implements ICommonConstants
{
	@Autowired
	INationalityMasterService iNationalityMasterService;
	
	@Autowired
	 SessionService sessionService;
	
	@RequestMapping(value="/saveNationalityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNationalityMaster(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			
			return iNationalityMasterService.addNationalityMaster(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getNationalityMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNationalityMasterList(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			return iNationalityMasterService.getNationalityMasterList(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	 
	@RequestMapping(value="/updateNationalityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNationalityMaster(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			return iNationalityMasterService.updateNationalityMaster(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getNationalityById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNationalityById(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			
			return iNationalityMasterService.getNationalityById(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForNationality" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForNationality(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			
			return iNationalityMasterService.updateStatusForNationality(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveNationalityList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveNationalityList(HttpServletRequest request){
		try{
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}

			return iNationalityMasterService.getActiveNationalityList(sessionService.getOrganizationId());

		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getNationalityCount", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNationalityCount(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			return iNationalityMasterService.getNationalityCount(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/getNationalityByCountryId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNationalityByCountryId(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			return iNationalityMasterService.getNationalityByCountryId(nationalityMasterDto);

		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveNationalityMasterListByOrgId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveNationalityMasterListByOrgId(@RequestBody NationalityMasterDto nationalityMasterDto){
		try{
			return iNationalityMasterService.getActiveNationalityMasterListByOrgId(nationalityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
}
