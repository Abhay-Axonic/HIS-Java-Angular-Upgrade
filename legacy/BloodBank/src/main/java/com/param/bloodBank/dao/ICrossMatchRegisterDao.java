package com.param.bloodBank.dao;


import java.util.List;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ICrossMatchRegisterDao {

	Response saveCrossMatchRegister(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException;

	Response saveCrossMatchRegisterDetails(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException;

	Response getCrossMatchRegisteredOnBagReservation(CrossMatchRegisterDto dto) throws ApplicationException;

	Response updateCrossMatchRegister(List<CrossMatchRegisterDto> dto)throws ApplicationException;

	Response getCrossMatchRegisteredOnIssue(CrossMatchRegisterDto dto)throws ApplicationException;

	Response getCrossMatchRegisterDetailsByCrossMatchID(CrossMatchRegisterDto dto) throws ApplicationException;

	Response updateCrossMatchingReqQtyByCrossMatchId(IssueBloodBagDto issueBloodBagDto)throws ApplicationException;

}
