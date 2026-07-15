package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.CorporateEmpDependentDetailsDto;
import com.param.global.dto.CorporateEmployeeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICorporateEmployeeMasterDao {
	
	Response saveCorporateEmployeeMaster(CorporateEmployeeMasterDto dto) throws ApplicationException;
	Response saveCorporateEmpDependentDetails(CorporateEmpDependentDetailsDto dto) throws ApplicationException;
	Response getCorporateEmployeeMasterDetails(CorporateEmployeeMasterDto dto) throws ApplicationException;
	Response getCorporateEmployeeDependantsDetails(CorporateEmployeeMasterDto dto)throws ApplicationException;
	Response updateCorporateEmployeeMaster(CorporateEmployeeMasterDto dto)throws ApplicationException;
	Response getCorporateEmployeeMasterDetailsById(CorporateEmployeeMasterDto dto)throws ApplicationException;
	Response searchCorporateEmployee(CorporateEmployeeMasterDto dto)throws ApplicationException;
	Response updateCorporateEmployeeMasterStatus(CorporateEmployeeMasterDto dto)throws ApplicationException;
	Response updateCorporateEmpDependents(CorporateEmployeeMasterDto dto)throws ApplicationException;
	
	Response verifyCorporateEmployee(CorporateEmployeeMasterDto dto)throws ApplicationException;

}
