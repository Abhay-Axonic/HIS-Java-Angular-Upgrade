package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceReminderMasterDto;
import com.param.billing.global.transaction.model.CompanyInvoiceReminderMaster;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyRemainderDao extends IGenericDao<CompanyInvoiceReminderMaster, Integer>{
	
	public Response saveCompanyRemainder(CompanyInvoiceReminderMasterDto  reqDto) throws ApplicationException;
	public Response updateCompanyRemainderByCompanyInvoiceReminderId(CompanyInvoiceReminderMasterDto reqDto) throws ApplicationException; 
}
