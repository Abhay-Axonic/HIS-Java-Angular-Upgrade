package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBagLoanOutService {

	Response saveloanout(BloodBagLoanOutDto dto)throws ApplicationException;

	Response getLoanOutList(BloodBagLoanOutDto dto)throws ApplicationException;

	Response getLoanOutDeatilsList(BloodBagLoanOutDto dto)throws ApplicationException;

	Response loanOutListApproval(BloodBagLoanOutDto dto)throws ApplicationException;

}
