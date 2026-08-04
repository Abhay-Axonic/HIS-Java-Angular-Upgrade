package com.param.billing.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dao.IAccountPayableMasterDao;
import com.param.global.common.Response;
import com.param.global.dto.AccountPayableMasterDto;
import com.param.global.service.VerifiedIdentityService;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AccountPayableMasterServiceImpl implements IAccountPayableMasterService, ICommonConstants {

	@Autowired
	IAccountPayableMasterDao iAccountPayableMasterDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		try {
			return iAccountPayableMasterDao.saveAccountPayable(accountPayableMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		// BATCH-4 org-trust fix (live-proven 2026-08-04): list read bound client-supplied
		// org/unit -- forged org-999 DTO from org-1 session returned the org-999 victim.
		// Bind the JWT-verified tenant.
		accountPayableMasterDto.setOrgId(verifiedIdentityService.getVerifiedOrganizationId());
		accountPayableMasterDto.setOrgUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iAccountPayableMasterDao.getAccountPayable(accountPayableMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAccountPayableStatus(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		try {
			return iAccountPayableMasterDao.updateAccountPayableStatus(accountPayableMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveAccountPayable(AccountPayableMasterDto accountPayableMasterDto) throws Exception {
		// BATCH-4 org-trust fix (live-proven 2026-08-04): list read bound client-supplied
		// org/unit -- forged org-999 DTO from org-1 session returned the org-999 victim.
		// Bind the JWT-verified tenant.
		accountPayableMasterDto.setOrgId(verifiedIdentityService.getVerifiedOrganizationId());
		accountPayableMasterDto.setOrgUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iAccountPayableMasterDao.getActiveAccountPayable(accountPayableMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
