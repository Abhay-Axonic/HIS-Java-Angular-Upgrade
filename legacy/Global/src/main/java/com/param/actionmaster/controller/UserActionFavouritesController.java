package com.param.actionmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.actionmaster.service.IUserActionFavouritesService;
import com.param.data.dto.UserActionFavouritesDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping(value="/api/global/UserActionFavourites")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserActionFavouritesController implements ICommonConstants 
{
	final static Logger logger = Logger.getLogger(UserActionFavouritesController.class);


	@Autowired
	IUserActionFavouritesService iUserActionFavouritesService ;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response save(@RequestBody UserActionFavouritesDto userActionFavouritesDto) {
		try {
			return iUserActionFavouritesService.saveUserActionFavourites(userActionFavouritesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public @ResponseBody Response remove(@PathVariable("id") Integer id) {
		try {
			return iUserActionFavouritesService.remove(id);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/get/{unitId}/{userId}", method = RequestMethod.GET)
	public @ResponseBody Response get(@PathVariable("unitId") Integer unitId,@PathVariable("userId") Integer userId) {
		try {
			return iUserActionFavouritesService.get(userId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
