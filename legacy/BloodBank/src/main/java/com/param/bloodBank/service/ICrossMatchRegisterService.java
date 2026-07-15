package com.param.bloodBank.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ICrossMatchRegisterService {

	Response saveCrossMatchRegister(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException;

	Response getCrossMatchRegisteredOnBagReservation(CrossMatchRegisterDto dto) throws ApplicationException;

	Response updateCrossMatchRegisteredOnBagReservation(List<CrossMatchRegisterDto> dto)throws ApplicationException;

	Response updateStock(CrossMatchRegisterDto crossMatchRegisterDto)throws ApplicationException;

	Response getCrossMatchRegisteredOnIssue(CrossMatchRegisterDto dto)throws ApplicationException;

	Response getCrossMatchRegisterDetailsByCrossMatchID(CrossMatchRegisterDto dto) throws ApplicationException;

}
