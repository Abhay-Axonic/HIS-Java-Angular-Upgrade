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
import com.param.global.otims.dto.NotesMasterDto;
import com.param.otims.service.INotesMasterservice;

@RestController
@RequestMapping("/global/otims")
@SuppressWarnings({ "rawtypes" })
public class NotesMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(NotesMasterController.class);

	@Autowired
	INotesMasterservice iNotesMasterservice;


	@RequestMapping(value = "/saveNotesMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNotesMaster(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.saveNotesMaster(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	//added by minesh for update NotesMaster.
	@RequestMapping(value = "/updateNotesMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNotesMaster(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.updateNotesMaster(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateNotesMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNotesMasterStatus(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.updateNotesMasterStatus(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getNotesMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNotesMasterList(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.getNotesMasterList(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getNotesMasterListByNoteTypeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNotesMasterListByNoteTypeId(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.getNotesMasterListByNoteTypeId(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getNotesMasterListPagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNotesMasterListPagination(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.getNotesMasterListPagination(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getNotesMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNotesMasterListCount(@RequestBody NotesMasterDto notesMasterDto) {
		try {
			return iNotesMasterservice.getNotesMasterListCount(notesMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
}
