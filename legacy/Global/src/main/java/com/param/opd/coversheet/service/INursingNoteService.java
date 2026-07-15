package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingNoteDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface INursingNoteService {

	public Response saveNursingNote(NursingNoteDto dto)throws ApplicationException;

	public Response getNursingNoteList(NursingNoteDto dto)throws ApplicationException;

}
