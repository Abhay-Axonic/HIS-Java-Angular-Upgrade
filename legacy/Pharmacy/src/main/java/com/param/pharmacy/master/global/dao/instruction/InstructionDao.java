package com.param.pharmacy.master.global.dao.instruction;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Instruction;

@Repository("InstructionDao")
public class InstructionDao extends AbstractDao<Integer, Instruction> implements IInstructionDao {

	@Override
	public Instruction create(Instruction instruction) {
		getSession().save(instruction);
		return instruction;
	}

	@Override
	public Instruction getById(Integer instructionId) {
		Criteria criteria = getSession().createCriteria(Instruction.class);
		criteria.add(Restrictions.eq("id", instructionId));
		//criteria.add(Restrictions.eq("isDeleted", false));
		return (Instruction) criteria.uniqueResult();
	}

	@Override
	public Integer update(Instruction instruction) {
		getSession().update(instruction);
		return instruction.getId();
	}

}
