package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTCompletionNoteDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTCompletionNoteDao {

	public Response saveOtCompletionNote(OTCompletionNoteDto dto)throws ApplicationException;

	public Response getOtNoteList(int proReqId)throws ApplicationException;
}
