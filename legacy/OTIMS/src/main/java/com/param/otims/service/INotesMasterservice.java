package com.param.otims.service;

import javax.transaction.Transactional;
import com.param.global.common.Response;
import com.param.global.otims.dto.NotesMasterDto;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface INotesMasterservice {

	Response saveNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException;
	
	//added by minesh for update NotesMaster.
	Response updateNotesMaster(NotesMasterDto notesMasterDto) throws ApplicationException;

	Response updateNotesMasterStatus(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterList(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterListByNoteTypeId(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterListPagination(NotesMasterDto notesMasterDto)throws ApplicationException;

	Response getNotesMasterListCount(NotesMasterDto notesMasterDto)throws ApplicationException;

}
