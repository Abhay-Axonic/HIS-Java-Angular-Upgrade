package com.param.billing.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.dao.ICompanyContractDao;
import com.param.billing.unit.dao.ICorporateStatementDao;
import com.param.global.dto.CorporateStatementDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })

public class CorporateStatementServiceImpl implements ICorporateStatementService , ICommonConstants {

	
	@Autowired
	ICorporateStatementDao iCorporateStatementDao;
	
	@Override
	public Response saveCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)
			throws ApplicationException {
		try {
				return iCorporateStatementDao.saveCorporateStatementMasterDetails(corporateStatementDto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)
			throws ApplicationException {
		try {
			return iCorporateStatementDao.updateCorporateStatementMasterDetails(corporateStatementDto);

	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getCorporateStatementMasterById(Integer billId, Integer organizationId,
			Integer unitId) throws ApplicationException {
		try {
			
			return iCorporateStatementDao.getCorporateStatementMasterById(billId, organizationId, unitId);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response checkAlreadyExit(CorporateStatementDto corporateStatementDto) throws ApplicationException {
		try {
			
			return iCorporateStatementDao.checkAlreadyExit(corporateStatementDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	

}
