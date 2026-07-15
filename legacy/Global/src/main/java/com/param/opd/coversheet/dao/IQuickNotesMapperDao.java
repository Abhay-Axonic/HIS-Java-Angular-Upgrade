package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;

import in.param.exception.ApplicationException;

public interface IQuickNotesMapperDao {

	public Response saveQuickNotesMapper(QuickNotesMapperDto quickNotesMapperDto)throws ApplicationException;

	public Response getQuickNotesMapperList(QuickNotesMapperDto quickNotesMapperDto)throws ApplicationException;

}
