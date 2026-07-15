package com.param.bloodBank.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagLoanInDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBankStockService {

	public Response saveBloodBankStock(List<BloodBankStockDto> list) throws ApplicationException;

	public Response searchBloodBankStock(BloodBankStockDto dto) throws ApplicationException;

	public Response updateBloodBankStockStatus(BloodBankStockDto dto)throws ApplicationException;

	public Response updateLoanInStatus(BloodBankStockDto dto)throws ApplicationException;

	public Response searchLoanInDetails(BloodBankStockDto dto) throws ApplicationException;

	public Response getAvailableBloodGroupByBankID(BloodBankStockDto dto) throws ApplicationException;

	public Response getBloodBankStockListByTTDStatus(BloodBankStockDto bloodBankStockDto)throws ApplicationException;

	public Response getBagNoListByComponentAndGroupId(BloodBankStockDto dto) throws ApplicationException;

	public Response searchLoanInData(BloodBagLoanInDto dto)throws ApplicationException;

	public Response getLoanInDataForApproval(BloodBagLoanInDto dto)throws ApplicationException;

	public Response updateDonorBloodInStock(BloodBankStockDto list)throws ApplicationException;

	public Response searchBagNoListByMultipleCriteria(BloodBankStockDto dto)throws ApplicationException;

}
