package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.NotesMasterDto;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface INotesMasterDao {
	
	Response saveNotesMaster(NotesMasterDto notesMasterDto)throws ApplicationException;
	
	Response updateNotesMaster(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response updateNotesMasterStatus(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterList(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterListByNoteTypeId(NotesMasterDto notesMasterDto) throws ApplicationException;

	Response getNotesMasterListPagination(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterListCount(NotesMasterDto notesMasterDto)throws ApplicationException;
}
