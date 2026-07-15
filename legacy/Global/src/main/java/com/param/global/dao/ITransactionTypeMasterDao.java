package com.param.global.dao;

import com.param.billing.global.model.TransactionTypeMaster;
import com.param.global.common.Response;
import com.param.global.dto.TransactionTypeMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITransactionTypeMasterDao extends IGenericDao<TransactionTypeMaster, Integer>{
	public Response getTransactionTypes(int orgId, int unitId)throws ApplicationException;

	
	Response getTransactionTypeMasterList(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;

	Response getTransactionTypeByName(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;

	Response saveTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;


	Response getTransactionTypeByNameNotId(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;


	Response updateTransactionTypeMaster(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;


	Response updateStatusForTransactionType(TransactionTypeMasterDto transactionTypeMasterDto)throws ApplicationException;
	
}
