package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;

import in.param.exception.ApplicationException;
@Transactional
public interface IQuickNotesMapperService {

	public Response saveQuickNotesMapper(QuickNotesMapperDto quickNotesMapperDto)throws ApplicationException;

	public Response getQuickNotesMapperList(QuickNotesMapperDto quickNotesMapperDto)throws ApplicationException;

}
