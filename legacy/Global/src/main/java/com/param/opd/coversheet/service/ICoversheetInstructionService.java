package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface ICoversheetInstructionService {

	@Transactional
	Response saveCoversheetInstruction(CoversheetInstructionDto coversheetInstructionDto)throws ApplicationException;

	@Transactional
	Response coversheetInstructionList(Integer visitId,int visitTypeId)throws ApplicationException;

	@Transactional
	Response updateCoversheetInstructionStatus(CoversheetInstructionDto coversheetInstructionDto)throws ApplicationException;

	@Transactional
	Response coversheetInstructionListForBookView(CoversheetInstructionDto coversheetInstructionDto) throws ApplicationException;

}
