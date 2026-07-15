package com.param.pharmacy.qms.dao;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IQMSCounterDao {

	Response saveQMSCounter(QMSCounterMasterDto dto)throws ApplicationException;

	Response updateQMSCounterStatus(QMSCounterMasterDto dto)throws ApplicationException;

	Response inactiveMaster(QMSCounterMasterDto dto)throws ApplicationException;

}
