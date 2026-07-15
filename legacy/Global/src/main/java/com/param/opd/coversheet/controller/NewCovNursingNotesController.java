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
import com.param.opd.coversheet.dto.NewCovNursingNotesDto;
import com.param.opd.coversheet.service.INewCovNursingNotesService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewCovNursingNotesController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(NewCovNursingNotesController.class);

	@Autowired 
	private INewCovNursingNotesService iNewCovNursingNotesService;
	
	@RequestMapping(value="/saveNewCovNursingNotes", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNewCovNursingNotes(@RequestBody NewCovNursingNotesDto newCovNursingNotesDto) {
		try {
			return iNewCovNursingNotesService.saveNewCovNursingNotes(newCovNursingNotesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateNewCovNursingNotes", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewCovNursingNotes(@RequestBody NewCovNursingNotesDto newCovNursingNotesDto) {
		try {
			return iNewCovNursingNotesService.updateNewCovNursingNotes(newCovNursingNotesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateNewCovNursingNotesStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewCovNursingNotesStatus(@RequestBody NewCovNursingNotesDto newCovNursingNotesDto) {
		try {
			return iNewCovNursingNotesService.updateNewCovNursingNotesStatus(newCovNursingNotesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovNursingNotesList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewCovNursingNotesList(@RequestBody NewCovNursingNotesDto newCovNursingNotesDto) {
		try {
			return iNewCovNursingNotesService.getNewCovNursingNotesList(newCovNursingNotesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovNursingNotesById/{newCovNursingNotesId}/{organizationId}/{unitId}", method=RequestMethod.GET)
	public @ResponseBody Response getNewCovNursingNotesById(@PathVariable ("newCovNursingNotesId") Integer newCovNursingNotesId,  @PathVariable ("organizationId") Integer organizationId, @PathVariable ("unitId") Integer unitId) {
		try {
			return iNewCovNursingNotesService.getNewCovNursingNotesById(newCovNursingNotesId, organizationId, unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovNursingNotesCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewCovNursingNotesCount(@RequestBody NewCovNursingNotesDto newCovNursingNotesDto) {
		try {
			return iNewCovNursingNotesService.getNewCovNursingNotesCount(newCovNursingNotesDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
