package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBankStockDetailsService {

	Response searchBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException;

	Response saveBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException;

	Response updateBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException;

	Response getBloodBankStockByComponentId(BloodBankStockDetailsDto dto)throws ApplicationException;

}
