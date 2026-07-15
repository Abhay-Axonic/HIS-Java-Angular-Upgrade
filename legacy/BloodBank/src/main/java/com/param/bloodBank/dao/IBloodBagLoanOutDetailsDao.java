package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagLoanOutDetailsDto;
import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBloodBagLoanOutDetailsDao {

	Response saveBloodBagLoanOutDetails(BloodBagLoanOutDetailsDto dtailsDto)throws ApplicationException;

	Response getLoanOutDeatilsList(BloodBagLoanOutDto dto)throws ApplicationException;

	Response approveBloodBagLoanOutDetails(BloodBagLoanOutDetailsDto dtailsDto);

}
