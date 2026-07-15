package com.param.global.org.common.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.global.InstructionMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IInstructionMasterService {

	
	Response saveInspectionMaster(InstructionMasterDto instructionMasterDto) throws ApplicationException;

	Response getInspectionMasterListById(int inspectionId, int orgId) throws ApplicationException;
	
	Response getInspectionMasterList(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response updateInspectionMasterStatus(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getInspectionMasterCount(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response updateInspectionMaster(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getActiveInspectionMasterList(InstructionMasterDto instructionMasterDto) throws ApplicationException;

	Response getDeptSpecilityById(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getSpecialityByDept(InstructionMasterDto instructionMasterDto);

	Response convertText()throws ApplicationException;

	Response getConvertedLanguageListById(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getSearchedInstructionList(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getMostFrequentlyUsedInstructions(InstructionMasterDto instructionMasterDto)throws ApplicationException;

	Response getSearchedFreeTextInstructionList(InstructionMasterDto instructionMasterDto)throws ApplicationException;
	

}
