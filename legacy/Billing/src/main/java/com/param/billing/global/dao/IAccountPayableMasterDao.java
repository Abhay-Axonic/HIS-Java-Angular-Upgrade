package com.param.billing.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AccountPayableMasterDto;
@SuppressWarnings("rawtypes")
public interface IAccountPayableMasterDao {

	
	Response saveAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response getAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response updateAccountPayableStatus(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

	Response getActiveAccountPayable(AccountPayableMasterDto accountPayableMasterDto)throws Exception;

}
