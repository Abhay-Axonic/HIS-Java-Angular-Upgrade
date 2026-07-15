package com.param.pharmacy.qms.dao;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSDeptMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IQMSDeptMasterDao {

	
	Response saveQMSDeptMaster(QMSDeptMasterDto dto) throws ApplicationException;

	Response updateQMSDeptMaster(QMSDeptMasterDto dto)throws ApplicationException;

	Response updateQMSDeptMasterStatus(QMSDeptMasterDto dto)throws ApplicationException;

	Response getQMSDeptMasterList(QMSDeptMasterDto dto)throws ApplicationException;

	Response getQMSDeptMasterCount(QMSDeptMasterDto dto)throws ApplicationException;

	Response getQMSDeptMasterById(QMSDeptMasterDto dto)throws ApplicationException;
	
	Response getQMSDeptMasterByName(QMSDeptMasterDto dto)throws ApplicationException;
	
	Response getQMSDeptMasterByNameNotById(QMSDeptMasterDto dto)throws ApplicationException;

	Response getActiveQMSDeptMasterList(QMSDeptMasterDto dto)throws ApplicationException;

	Response getReceptionListByKiosk(QMSDeptMasterDto dto)throws ApplicationException;

	Response getActiveDeptListByKioskId(QMSDeptMasterDto dto)throws ApplicationException;

}
