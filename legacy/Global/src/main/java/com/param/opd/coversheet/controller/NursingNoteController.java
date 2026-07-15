package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingNoteDto;
import com.param.opd.coversheet.service.INursingNoteService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd/nursingNote")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingNoteController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingNoteController.class);

	@Autowired 
	private INursingNoteService iNursingNoteService;
	
	@RequestMapping(value="/saveNursingNote", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNursingNote(@RequestBody NursingNoteDto dto) {
		try {
			return iNursingNoteService.saveNursingNote(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNursingNoteList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNursingNoteList(@RequestBody NursingNoteDto dto) {
		try {
			return iNursingNoteService.getNursingNoteList(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
