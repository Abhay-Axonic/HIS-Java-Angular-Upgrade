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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.RaceMasterDto;
import com.param.adt.master.global.service.IRaceMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RaceMasterController implements ICommonConstants {

	@Autowired
	IRaceMasterService iRaceMasterService;

	@Autowired
	SessionService sessionService;
	
	@RequestMapping(value = "/saveRaceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveRaceMaster(@RequestBody RaceMasterDto raceMasterDto) {
		try {

			return iRaceMasterService.addRaceMaster(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getRaceMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRaceMasterList(@RequestBody RaceMasterDto raceMasterDto) {
		try {
			return iRaceMasterService.getRaceMasterList(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateRaceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateRaceMaster(@RequestBody RaceMasterDto raceMasterDto) {
		try {
			return iRaceMasterService.updateRaceMaster(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getRaceById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRaceById(@RequestBody RaceMasterDto raceMasterDto) {
		try {

			return iRaceMasterService.getRaceById(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateStatusForRace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForRace(@RequestBody RaceMasterDto raceMasterDto) {
		try {

			return iRaceMasterService.updateStatusForRace(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveRaceList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveRaceList(HttpServletRequest request) {
		try {
			
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}

			return iRaceMasterService.getActiveRaceList(sessionService.getOrganizationId());

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getRaceCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRaceCount(@RequestBody RaceMasterDto raceMasterDto) {
		try {
			return iRaceMasterService.getRaceCount(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getDefaultRaceList/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody Response getDefaultRaceList(@PathVariable ("organizationId") Integer organizationId) {
		try {
				return iRaceMasterService.getDefaultRaceList(organizationId);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
