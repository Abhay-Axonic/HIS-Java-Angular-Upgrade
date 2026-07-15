package com.param.global.billing.service;

import javax.transaction.Transactional;

import com.param.global.billing.dto.BankBranchMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IBankBranchMasterService {

	Response saveBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getBankBranchMasterById(int branchId, int orgnisationId) throws ApplicationException;

	Response getBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response updateBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response updateBankBranchMasterStatus(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getBankBranchMasterCount(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

	Response getActiveBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException;

}
