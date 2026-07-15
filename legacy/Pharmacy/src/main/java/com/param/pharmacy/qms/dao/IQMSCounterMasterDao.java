package com.param.pharmacy.qms.dao;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IQMSCounterMasterDao {
	
	Response saveQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException;

	Response updateQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException;

	Response updateQMSCounterMasterStatus(QMSCounterMasterDto dto) throws ApplicationException;

	Response getQMSCounterMasterList(QMSCounterMasterDto dto) throws ApplicationException;

	Response getQMSCounterMasterCount(QMSCounterMasterDto dto) throws ApplicationException;

	Response getQMSCounterMasterById(QMSCounterMasterDto dto) throws ApplicationException;
	
	Response getQMSCounterMasterListByName(QMSCounterMasterDto dto) throws ApplicationException;
	
	Response getQMSCounterMasterListByNameNotById(QMSCounterMasterDto dto) throws ApplicationException;

	Response getQMSCounterListByDeptId(QMSCounterMasterDto dto) throws ApplicationException;

}
