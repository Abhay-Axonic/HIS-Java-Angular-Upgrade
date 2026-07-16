package com.param.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import in.param.exception.ApplicationException;
import org.apache.http.cookie.CookieSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.GenderMasterDto;
import com.param.global.service.IGenderMasterService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class GenderMasterController implements ICommonConstants{

	 private static final Logger logger = Logger.getLogger(GenderMasterController.class);

	 @Autowired
	  private IGenderMasterService iGenderMasterService;
	 
	 @Autowired
	 SessionService sessionService;
	 
	 @RequestMapping(value="/getGenderMasterList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getGenderMasterList(@RequestBody GenderMasterDto genderMasterDto){
			try{
				return iGenderMasterService.getGenderMasterList(genderMasterDto);
			}catch(Exception e){
				logger.error("Error while fetching the gender master list", e);
			}
		return new Response(ERROR, null, null, null,null);
		}
	 
	 @RequestMapping(value="/saveGenderMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveGenderMaster(@RequestBody GenderMasterDto genderMasterDto){
			try{

				return iGenderMasterService.addGenderMaster(genderMasterDto);
			}catch(Exception e){
				logger.error("Error while saving the gender master", e);
			}
		return new Response(ERROR, null, null, null,null);
		}
		
	 @RequestMapping(value="/getGenderById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getGenderById(@RequestBody GenderMasterDto genderMasterDto){
			try{
				return iGenderMasterService.getGenderById(genderMasterDto);
			}catch(Exception e){
				
				throw e;
			}
		}



	 @RequestMapping(value="/updateGenderMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateGenderMaster(@RequestBody GenderMasterDto genderMasterDto){
			try{
				return iGenderMasterService.updateGenderMaster(genderMasterDto);
			}catch(Exception e){
				logger.error("Error while updating the gender master", e);
			}
		return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/updateStatusForGender" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateStatusForGender(@RequestBody GenderMasterDto genderMasterDto){
			try{

				return iGenderMasterService.updateStatusForGender(genderMasterDto);
			}catch(Exception e){
				logger.error("Error while updating the gender status", e);
			}
		return new Response(ERROR, null, null, null,null);
		}
		@RequestMapping(value="/getActiveGenderList", method = RequestMethod.GET)
		public @ResponseBody Response getActiveGenderList(HttpServletRequest request){
			try{
				
				if(request.getCookies()!=null)
	        	{
	        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
	                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
	        		 if (cookies != null && cookies.size() > 0) {
	                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
	                 }
	        	}

				return iGenderMasterService.getActiveGenderList();

			}catch(Exception e){
				logger.error("Error while fetching the active gender list", e);
			}
		return new Response(ERROR, null, null, null,null);
		}

		@RequestMapping(value="/getGenderCount", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getGenderCount(@RequestBody GenderMasterDto genderMasterDto){
			try{
				return iGenderMasterService.getActiveGenderList(genderMasterDto);
			}catch(Exception e){
				logger.error("Error while fetching the gender count", e);
			}
		return new Response(ERROR, null, null, null,null);
		}

}
