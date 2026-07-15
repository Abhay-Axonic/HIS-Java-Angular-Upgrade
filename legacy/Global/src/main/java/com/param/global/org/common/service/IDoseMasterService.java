package com.param.global.org.common.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.org.common.dto.DoseMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IDoseMasterService {

	
	Response saveDoseMaster(DoseMasterDto doseMasterDto) throws ApplicationException;

	Response getDoseMasterListById(int doseId, int orgId)  throws ApplicationException;

	Response getDoseMasterList(DoseMasterDto doseMasterDto)  throws ApplicationException;

	Response updateDoseMaster(DoseMasterDto doseMasterDto)  throws ApplicationException;

	Response updateDoseMasterStatus(DoseMasterDto doseMasterDto)  throws ApplicationException;

	Response getDoseMasterCount(DoseMasterDto doseMasterDto) throws ApplicationException;

	Response getActiveDoseMasterList(DoseMasterDto doseMasterDto) throws ApplicationException;

}
