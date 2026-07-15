package com.param.billing.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.dao.ICorporateStatementDao;
import com.param.billing.unit.dao.ICorporateStatementDetailsChecklistDao;
import com.param.global.dto.CorporateStatementDetailsChecklistDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })

public class CorporateStatementDetailsChecklistServiceImpl implements ICorporateStatementDetailsChecklistService , ICommonConstants{
	
	@Autowired
	ICorporateStatementDetailsChecklistDao iCorporateStatementDetailsChecklistDao;
	

	@Override
	public Response saveCorporateStatementDetailsChecklist(
			CorporateStatementDetailsChecklistDto corporateStatementDetailsChecklistDto) throws ApplicationException {
		try {
			return iCorporateStatementDetailsChecklistDao.saveCorporateStatementDetailsChecklist(corporateStatementDetailsChecklistDto);

	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
