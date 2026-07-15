package com.param.pharmacy.qms.dao;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSKioskMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IQMSKioskMasterDao {

	Response saveQMSMaster(QMSKioskMasterDto dto) throws ApplicationException;

	Response updateQMSMaster(QMSKioskMasterDto dto) throws ApplicationException;

	Response updateQMSMasterStatus(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterList(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterCount(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterById(QMSKioskMasterDto dto)throws ApplicationException;
	
	Response getQMSMasterListByName(QMSKioskMasterDto dto)throws ApplicationException;
	
	Response getQMSMasterListByNameNotById(QMSKioskMasterDto dto)throws ApplicationException;

	Response getActiveQMSMasterList(QMSKioskMasterDto dto)throws ApplicationException;

}
