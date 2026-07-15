package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.PostOperativeDetailsDto;
import com.param.otims.service.IPostOperativeDetailsService;

@RestController
@RequestMapping(value = "/api/otims/PostOperativeDetails")
@SuppressWarnings({"rawtypes","unused"})
public class PostOperativeDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PostOperativeDetailsController.class);

	@Autowired
	IPostOperativeDetailsService iPostOperativeDetailsService;

	@RequestMapping(value = "/savePostOperativeDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePostOperativeDetails(@RequestBody PostOperativeDetailsDto postOperativeDetailsDto){
		try{
			Response res =  iPostOperativeDetailsService.savePostOperativeDetails(postOperativeDetailsDto);
			if(postOperativeDetailsDto.getPostOperativeId()!=null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,null);
			}else {
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,null);
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@RequestMapping(value = "/getPostOperativeDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPostOperativeDetails(@RequestBody PostOperativeDetailsDto postOperativeDetailsDto){
		try{
			 return  iPostOperativeDetailsService.getPostOperativeDetails(postOperativeDetailsDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

}
