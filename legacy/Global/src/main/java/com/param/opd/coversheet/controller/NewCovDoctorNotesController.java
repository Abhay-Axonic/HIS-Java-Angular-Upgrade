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
import com.param.opd.coversheet.dto.NewCovDoctorNotesDto;
import com.param.opd.coversheet.service.INewCovDoctorNotesService;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewCovDoctorNotesController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(NewCovDoctorNotesController.class);

	@Autowired
	private INewCovDoctorNotesService iNewCovDoctorNotesService;
	
	@RequestMapping(value="/saveNewCovDoctorNotes", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNewCovDoctorNotes(@RequestBody NewCovDoctorNotesDto newCovDoctorNotesDto) {
		try {
			return iNewCovDoctorNotesService.saveNewCovDoctorNotes(newCovDoctorNotesDto);
		} catch (Exception e) {
			logger.error("saveNewCovDoctorNotes failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateNewCovDoctorNotes", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewCovDoctorNotes(@RequestBody NewCovDoctorNotesDto newCovDoctorNotesDto) {
		try {
			return iNewCovDoctorNotesService.updateNewCovDoctorNotes(newCovDoctorNotesDto);
		} catch (Exception e) {
			logger.error("updateNewCovDoctorNotes failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateNewCovDoctorNotesStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewCovDoctorNotesStatus(@RequestBody NewCovDoctorNotesDto newCovDoctorNotesDto) {
		try {
			return iNewCovDoctorNotesService.updateNewCovDoctorNotesStatus(newCovDoctorNotesDto);
		} catch (Exception e) {
			logger.error("updateNewCovDoctorNotesStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovDoctorNotesList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewCovDoctorNotesList(@RequestBody NewCovDoctorNotesDto newCovDoctorNotesDto) {
		try {
			return iNewCovDoctorNotesService.getNewCovDoctorNotesList(newCovDoctorNotesDto);
		} catch (Exception e) {
			logger.error("getNewCovDoctorNotesList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovDoctorNotesById/{newCovDoctorNotesId}/{organizationId}/{unitId}", method=RequestMethod.GET)
	public @ResponseBody Response getNewCovDoctorNotesById(@PathVariable ("newCovDoctorNotesId") Integer newCovDoctorNotesId,  @PathVariable ("organizationId") Integer organizationId, @PathVariable ("unitId") Integer unitId) {
		try {
			return iNewCovDoctorNotesService.getNewCovDoctorNotesById(newCovDoctorNotesId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getNewCovDoctorNotesById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getNewCovDoctorNotesCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewCovDoctorNotesCount(@RequestBody NewCovDoctorNotesDto newCovDoctorNotesDto) {
		try {
			return iNewCovDoctorNotesService.getNewCovDoctorNotesCount(newCovDoctorNotesDto);
		} catch (Exception e) {
			logger.error("getNewCovDoctorNotesCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
