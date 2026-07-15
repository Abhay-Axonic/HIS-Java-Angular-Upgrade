package com.param.global.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.CorporateEmployeeMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ICorporateEmployeeMasterService {

	
	Response saveCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException;

	Response getCorporateEmployeeMasterDetails(CorporateEmployeeMasterDto dto)  throws ApplicationException;

	Response getCorporateEmployeeDependantsDetails(CorporateEmployeeMasterDto dto) throws ApplicationException;

	Response getCorporateEmployeeMasterDetailsById(CorporateEmployeeMasterDto dto) throws ApplicationException;

	Response updateCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException;

	Response searchCorporateEmployee(CorporateEmployeeMasterDto dto)throws ApplicationException;

	Response updateCorporateEmployeeMasterStatus(CorporateEmployeeMasterDto dto)throws ApplicationException;
	
	//verifying corporate employee

	Response verifyCorporateEmployee(CorporateEmployeeMasterDto dto)throws ApplicationException;


}
