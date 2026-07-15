package com.param.opd.coversheet.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NewCovNursingNotesDto;

import in.param.exception.ApplicationException;
@Transactional
public interface INewCovNursingNotesService {
	public Response saveNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException;
	
	public Response updateNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException;
	
	public Response updateNewCovNursingNotesStatus(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException;
	
	public Response getNewCovNursingNotesList(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException;
	
	public Response getNewCovNursingNotesById(Integer newCovNursingNotesId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getNewCovNursingNotesCount(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException;
}
