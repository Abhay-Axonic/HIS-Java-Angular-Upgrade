package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;
import com.param.opd.coversheet.service.IQuickNotesMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QuickNotesMapperController  implements ICommonConstants {
	final static Logger logger = Logger.getLogger(QuickNotesMapperController.class);

	@Autowired 
	private IQuickNotesMapperService iQuickNotesMapperService;
	
	@RequestMapping(value="/saveQuickNotesMapper", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveQuickNotesMapper(@RequestBody QuickNotesMapperDto quickNotesMapperDto) {
		try {
			return iQuickNotesMapperService.saveQuickNotesMapper(quickNotesMapperDto);
		} catch (Exception e) {
			logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getQuickNotesMapperList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getQuickNotesMapperList(@RequestBody QuickNotesMapperDto quickNotesMapperDto) {
		try {
			return iQuickNotesMapperService.getQuickNotesMapperList(quickNotesMapperDto);
		} catch (Exception e) {
			logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	


}
