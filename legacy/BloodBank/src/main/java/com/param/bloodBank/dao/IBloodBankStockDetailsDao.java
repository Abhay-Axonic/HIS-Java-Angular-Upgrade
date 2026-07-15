package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IBloodBankStockDetailsDao {

	Response searchBloodBankStockDetails(BloodBankStockDetailsDto dto)throws ApplicationException;

	Response saveBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException;

	Response updateBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException;

	Response getBloodBankStockByComponentId(BloodBankStockDetailsDto dto)throws ApplicationException;

	Response updateRedCellStatus(Integer redCellId)throws ApplicationException;

}
