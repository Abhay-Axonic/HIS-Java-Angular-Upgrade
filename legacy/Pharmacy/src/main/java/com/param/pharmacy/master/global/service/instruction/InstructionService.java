package com.param.pharmacy.master.global.service.instruction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.pharmacy.Instruction;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.dao.instruction.IInstructionDao;
import com.param.pharmacy.master.global.dto.instruction.InstructionRabbitDto;
import com.param.pharmacy.master.global.dto.instruction.InstructionRequest;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;

@Service("InstructionService")
public class InstructionService implements IInstructionService {

	private static final Logger logger = Logger.getLogger(InstructionService.class);

	@Autowired
	IInstructionDao InstructionDao;
	
	@Autowired
	IEmrService EmrService;

	@Autowired
	SessionService sessionService;

	@Override
	@Transactional
	public CommonResponse create(InstructionRequest instructionRequest) {
		try {
		Instruction instruction = new Instruction();
		instruction.setInstructionCode(instructionRequest.getInstructionCode());
		instruction.setInstructionDesc(instructionRequest.getInstructionDesc());
		instruction.setIsMedication(instructionRequest.getIsMedication());
		
		Instruction instructionObj = InstructionDao.create(instruction);
		CommonResponse response = new CommonResponse();
		response.setId(instructionObj.getId().longValue());
		response.setMessage("Instruction Inserted Successfully");
		
		InstructionRabbitDto instructionRabbitDto = new InstructionRabbitDto(instructionObj.getId(),instructionObj.getInstructionCode(),
				instructionObj.getInstructionDesc(),instructionObj.getIsMedication(),instructionObj.getStatus(),
				instructionObj.getAddedBy().getUserId(),instructionObj.getAddedDate(),instructionObj.getUpdatedBy().getUserId(),
				instructionObj.getUpdatedDate());
		
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(instructionRabbitDto);
		request.setKey(MasterKeyConstants.InstructionMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		return response;
	}
	catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	@Transactional
	public CommonResponse update(InstructionRequest instructionRequest) {
		Instruction instruction = InstructionDao.getById(instructionRequest.getInstructionId());
		
		if (instructionRequest.getInstructionCode() != null && instructionRequest.getInstructionCode() != "") {
			instruction.setInstructionCode(instructionRequest.getInstructionCode());
		}

		if (instructionRequest.getInstructionDesc() != null && instructionRequest.getInstructionDesc() != "") {
			instruction.setInstructionDesc(instructionRequest.getInstructionDesc());
		}

		if (instructionRequest.getIsMedication() != null) {
			instruction.setIsMedication(instructionRequest.getIsMedication());
		}
		instruction.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
		//instruction.setUpdatedBy(instruction.getUser());
		
		CommonResponse response = new CommonResponse();
		response.setId(InstructionDao.update(instruction).longValue());
		response.setMessage("Instruction Updated Successfully");
		
		InstructionRabbitDto instructionRabbitDto = new InstructionRabbitDto(response.getId().intValue(),instruction.getInstructionCode(),
				instruction.getInstructionDesc(),instruction.getIsMedication(),instruction.getStatus(),
				instruction.getAddedBy().getUserId(),instruction.getAddedDate(),instruction.getUpdatedBy().getUserId(),
				instruction.getUpdatedDate());
		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(instructionRabbitDto);
		request.setKey(MasterKeyConstants.InstructionMasterHISKey.toString());
		request.setPayload(payload);
		//EmrService.publish(request);
		return response;
	}
	
}
