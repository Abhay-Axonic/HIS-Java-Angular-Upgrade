package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagLoanInDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBloodBagLoanInDao {

	Long getNextValBloodBagStockId()throws ApplicationException;

	Response savaloanIn(BloodBagLoanInDto bloodBagLoandto)throws ApplicationException;

	Response searchLoanInData(BloodBagLoanInDto dto)throws ApplicationException;

	Response getLoanInDataForApproval(BloodBagLoanInDto dto)throws ApplicationException;

}
