package com.param.billing.global.service;

import javax.transaction.Transactional;
import com.param.global.common.Response;
import com.param.global.dto.AccountPayableMasterDto;

@SuppressWarnings("rawtypes")
@Transactional
public interface IAccountPayableMasterService {

	Response saveAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response getAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response updateAccountPayableStatus(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response getActiveAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

}
