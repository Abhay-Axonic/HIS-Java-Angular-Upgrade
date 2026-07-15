package com.param.global.billing.dao;

import com.param.global.billing.dto.BankBranchMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBankBranchMasterDao {


	Response saveBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getBankBranchMasterById(int branchId, int orgnisationId) throws ApplicationException;

	Response getBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response updateBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response updateBankBranchMasterStatus(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getBankBranchMasterCount(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;
	
	Response getBankBranchMasterByNameNotId(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;
	
	Response getBankBranchMasterByName(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getActiveBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto)throws ApplicationException;
}
