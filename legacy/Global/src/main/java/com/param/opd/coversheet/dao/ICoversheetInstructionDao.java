package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.model.CoversheetInstruction;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface ICoversheetInstructionDao extends IGenericDao<CoversheetInstruction, Integer>{

	Response saveCoversheetInstruction(CoversheetInstructionDto coversheetInstructionDto)throws ApplicationException;

	Response coversheetInstructionList(Integer visitId,int visitTypeId)throws ApplicationException;

	Response updateCoversheetInstructionStatus(CoversheetInstructionDto coversheetInstructionDto)throws ApplicationException;

	Response coversheetInstructionListForBookView(CoversheetInstructionDto coversheetInstructionDto)throws ApplicationException;

	Response updateCoversheetInstructionList(CoversheetTemplateMasterDto coversheetTemplateMasterDto);

}
