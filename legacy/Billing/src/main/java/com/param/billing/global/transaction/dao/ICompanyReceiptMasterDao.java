package com.param.billing.global.transaction.dao;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.model.CompanyReceiptMaster;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptMasterDao extends IGenericDao<CompanyReceiptMaster, Integer>{
	public Response saveCompanyReceiptMaster(CompanyReceiptMasterDto dto)throws ApplicationException;
	public Response mergeCompanyReceipt(CompanyReceiptMaster master)throws ApplicationException;
}
