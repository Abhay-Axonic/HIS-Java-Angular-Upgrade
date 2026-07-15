package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.TransactionTypeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITransactionTypeMasterService {
	
	public Response getTransactionTypes(int orgId, int unitId)throws ApplicationException;

	@Transactional
	Response getTransactionTypeMasterList(TransactionTypeMasterDto transactionTypeMasterDto) throws ApplicationException;

	@Transactional
	Response saveTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto) throws ApplicationException;

	@Transactional
	Response updateTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;

	@Transactional
	Response updateStatusForTransactionType(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;
	

}
