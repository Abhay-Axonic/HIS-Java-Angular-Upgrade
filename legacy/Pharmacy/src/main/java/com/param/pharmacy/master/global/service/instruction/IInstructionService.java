package com.param.pharmacy.master.global.service.instruction;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.instruction.InstructionRequest;

public interface IInstructionService {

	CommonResponse create(InstructionRequest instructionRequest);

	CommonResponse update(InstructionRequest instructionRequest);

}
