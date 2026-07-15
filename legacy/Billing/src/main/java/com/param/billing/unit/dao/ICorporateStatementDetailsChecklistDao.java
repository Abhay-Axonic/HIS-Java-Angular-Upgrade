package com.param.billing.unit.dao;

import com.param.billing.common.Response;
import com.param.global.dto.CorporateStatementDetailsChecklistDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface ICorporateStatementDetailsChecklistDao {

	Response saveCorporateStatementDetailsChecklist( CorporateStatementDetailsChecklistDto corporateStatementDetailsChecklistDto)throws ApplicationException;

}
