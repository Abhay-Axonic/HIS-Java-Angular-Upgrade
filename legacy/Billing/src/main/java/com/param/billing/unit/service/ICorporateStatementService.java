package com.param.billing.unit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.Response;
import com.param.global.dto.CorporateStatementDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface ICorporateStatementService {

	Response saveCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)throws ApplicationException;

	Response updateCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)throws ApplicationException;

	Response getCorporateStatementMasterById(Integer billId, Integer organizationId, Integer unitId)throws ApplicationException;

	Response checkAlreadyExit(CorporateStatementDto corporateStatementDto)throws ApplicationException;

}
