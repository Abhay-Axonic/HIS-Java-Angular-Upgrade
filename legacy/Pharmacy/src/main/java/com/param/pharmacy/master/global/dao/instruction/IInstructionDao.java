package com.param.pharmacy.master.global.dao.instruction;

import com.param.entity.model.pharmacy.Instruction;

public interface IInstructionDao {

	Instruction create(Instruction instruction);

	Instruction getById(Integer instructionId);

	Integer update(Instruction instruction);

}
