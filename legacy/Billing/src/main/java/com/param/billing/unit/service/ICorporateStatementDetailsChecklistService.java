package com.param.billing.unit.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.Response;
import com.param.global.dto.CorporateStatementDetailsChecklistDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface ICorporateStatementDetailsChecklistService {

	Response saveCorporateStatementDetailsChecklist(CorporateStatementDetailsChecklistDto corporateStatementDetailsChecklistDto)throws ApplicationException;

}
