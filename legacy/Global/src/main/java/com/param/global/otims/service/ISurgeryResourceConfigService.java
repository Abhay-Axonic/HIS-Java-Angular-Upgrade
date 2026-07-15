package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.SurgeryResourceConfigByCritertiaReqDto;
import com.param.global.otims.dto.SurgeryResourceConfigDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface ISurgeryResourceConfigService {
	
	Response saveSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException;

	Response updateSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto)  throws ApplicationException;

	Response getSurgeryResourceConfigList(SurgeryResourceConfigDto surgeryResourceConfigDto)  throws ApplicationException;

	Response getSurgeryResourceConfigListById(Integer surgeryResourceConfigId)  throws ApplicationException;

	Response getSurgeryResourceConfigListCount(SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException;

	Response updateSurgeryResourceConfigStatus(SurgeryResourceConfigDto surgeryResourceConfigDto)  throws ApplicationException;
	
	Response checkAlreadyExistOrNot(SurgeryResourceConfigDto surgeryResourceConfigDto)  throws ApplicationException;
	public Response getConfigForProcPostingByCriteria(SurgeryResourceConfigByCritertiaReqDto reqDto)throws ApplicationException;

	Response getSurgeryResourceConfigListByProcedureId(SurgeryResourceConfigDto surgeryResourceConfigDto)throws ApplicationException;
	

}
