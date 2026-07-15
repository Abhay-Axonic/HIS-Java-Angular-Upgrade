package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IBloodBankStockDao {

	public Response saveBloodBankStock(BloodBankStockDto dto) throws ApplicationException;

	public Response searchBloodBankStock(BloodBankStockDto dto) throws ApplicationException;

	public Long getNextValBloodBagStockId() throws ApplicationException;

	public Response updateBloodBankStockStatus(BloodBankStockDto dto)throws ApplicationException;

	public Response updateLoanInStatus(BloodBankStockDto dto)throws ApplicationException;

	public Response searchLoanInDetails(BloodBankStockDto dto) throws ApplicationException;

	public Response getAvailableBloodGroupByBankID(BloodBankStockDto dto) throws ApplicationException;

	public Response getBloodBankStockListByTTDStatus(BloodBankStockDto bloodBankStockDto) throws ApplicationException;

	public Response updateStockTtdFlag(int bloodBankStockId)throws ApplicationException;

	public Response getBagNoListByComponentAndGroupId(BloodBankStockDto dto) throws ApplicationException;

	public Response updateDonorBloodInStock(BloodBankStockDto dto);

	public Response searchBagNoListByMultipleCriteria(BloodBankStockDto dto)throws ApplicationException;



}
