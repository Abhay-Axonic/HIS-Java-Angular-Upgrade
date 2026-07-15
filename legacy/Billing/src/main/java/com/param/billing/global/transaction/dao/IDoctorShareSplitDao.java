package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsDto;
import com.param.billing.global.transaction.model.TDoctorShareSplitLogs;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IDoctorShareSplitDao extends IGenericDao<TDoctorShareSplitLogs, Integer>{
	public Response saveDoctorShareSplit(TDoctorShareSplitLogsDto masterDto)throws ServiceException;

	Response updateDoctorShareSplit(TDoctorShareSplitLogs masterDto) throws ServiceException;

	Response findByIdDoctorShareSplit(Integer id) throws ServiceException;
	
}
