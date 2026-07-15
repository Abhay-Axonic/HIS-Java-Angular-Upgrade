package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingNoteDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface INursingNoteDao {

	public Response saveNursingNote(NursingNoteDto dto)throws ApplicationException;

	public Response getNursingNoteList(NursingNoteDto dto)throws ApplicationException;

}
