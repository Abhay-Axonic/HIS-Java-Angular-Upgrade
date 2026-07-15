package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBloodBagLoanOutDao {
	
	Response saveloanout(BloodBagLoanOutDto dto) throws ApplicationException;

	Response getLoanOutList(BloodBagLoanOutDto dto)throws ApplicationException;

	Response approveLoanOut(BloodBagLoanOutDto dto);

}
