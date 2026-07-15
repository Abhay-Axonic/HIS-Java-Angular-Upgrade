package com.param.pharmacy.qms.dao;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSTokenMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IQMSTokenMasterDao {


	Response saveToken(QMSTokenMasterDto dto) throws ApplicationException;

	Integer getTokenNumber(QMSTokenMasterDto dto)throws ApplicationException;

	Response getTokenList(QMSTokenMasterDto dto) throws ApplicationException;

	Response updateToken(QMSTokenMasterDto dto)throws ApplicationException;

	Response transferToken(QMSTokenMasterDto dto)throws ApplicationException;

	Response saveTokenlog(QMSTokenMasterDto dto)throws ApplicationException;

	Response updateTokenlog(QMSTokenMasterDto dto)throws ApplicationException;

	Response resetToken(QMSTokenMasterDto dto)throws ApplicationException;

	Response getTokenListForDisplay(QMSTokenMasterDto dto)throws ApplicationException;

	Response getTokenDisplayMessage(QMSTokenMasterDto dto)throws ApplicationException;

	Response checkingIsCalledOrNot(QMSTokenMasterDto dto)throws ApplicationException;

	Response releaseTokenNumber(QMSTokenMasterDto dto)throws ApplicationException;

	Response updateQMSTokenMaster(QMSTokenMasterDto dto)throws ApplicationException;

}
