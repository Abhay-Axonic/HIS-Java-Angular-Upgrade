package com.param.opd.coversheet.service;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.InstructionDTO;

@SuppressWarnings("rawtypes")
public interface IinstructionService {
	
	public Response saveInvestigation(InstructionDTO instructionDTO)throws ApplicationException;
	
	public Response getMasterInstruction()throws ApplicationException;

	public Response getInstructions(Integer patientId,Integer encounterId) throws ApplicationException;
}
