package com.param.pharmacy.qms.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSKioskMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IQMSKioskMasterService {
	
	Response saveQMSMaster(QMSKioskMasterDto dto) throws ApplicationException;

	Response updateQMSMaster(QMSKioskMasterDto dto) throws ApplicationException;

	Response updateQMSMasterStatus(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterList(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterCount(QMSKioskMasterDto dto)throws ApplicationException;

	Response getQMSMasterById(QMSKioskMasterDto dto)throws ApplicationException;

	Response getActiveQMSMasterList(QMSKioskMasterDto dto)throws ApplicationException;

}
