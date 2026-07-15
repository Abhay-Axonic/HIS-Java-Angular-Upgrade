package com.param.pharmacy.qms.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.Response;
import com.param.pharmacy.qms.dto.QMSTokenMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IQMSTokenMasterService {
	
	Response saveToken(QMSTokenMasterDto dto) throws ApplicationException;

	Response getTokenList(QMSTokenMasterDto dto) throws ApplicationException;

	Response updateToken(QMSTokenMasterDto dto)throws ApplicationException;

	Response transferToken(QMSTokenMasterDto dto)throws ApplicationException;

	Response resetTokenSequence(QMSTokenMasterDto dto)throws ApplicationException;

	Response getTokenListForDisplay(QMSTokenMasterDto dto)throws ApplicationException;

	Response getTokenDisplayMessage(QMSTokenMasterDto dto)throws ApplicationException;

	Response checkingIsCalledOrNot(QMSTokenMasterDto dto)throws ApplicationException;

	Response releaseTokenNumber(QMSTokenMasterDto dto)throws ApplicationException;

}
