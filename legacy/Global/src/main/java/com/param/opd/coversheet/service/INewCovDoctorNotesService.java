package com.param.opd.coversheet.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NewCovDoctorNotesDto;

import in.param.exception.ApplicationException;
@Transactional
public interface INewCovDoctorNotesService {
public Response saveNewCovDoctorNotes(NewCovDoctorNotesDto observationDto) throws ApplicationException;
	
	public Response updateNewCovDoctorNotes(NewCovDoctorNotesDto observationDto) throws ApplicationException;
	
	public Response updateNewCovDoctorNotesStatus(NewCovDoctorNotesDto observationDto) throws ApplicationException;
	
	public Response getNewCovDoctorNotesList(NewCovDoctorNotesDto observationDto) throws ApplicationException;
	
	public Response getNewCovDoctorNotesById(Integer observationId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getNewCovDoctorNotesCount(NewCovDoctorNotesDto observationDto) throws ApplicationException;
}
