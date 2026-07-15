package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationNoteDao {

	Response saveProcedureConfigNote(List<ProcedureConfigurationNoteDto> noteList, int procedureConfigId) throws ApplicationException;

	Response getNoteByProcedureConfigId(ProcedureConfigurationDto reqDto)throws ApplicationException;

}
