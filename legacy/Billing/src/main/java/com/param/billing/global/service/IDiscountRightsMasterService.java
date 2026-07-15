package com.param.billing.global.service;


import java.math.BigDecimal;

import javax.transaction.Transactional;

import com.param.billing.global.dto.DiscountRightsMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IDiscountRightsMasterService {
	
	public Response saveDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto) throws ApplicationException;
	
	public Response updateDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto) throws ApplicationException;
	
	public Response updateDiscountRightsMasterStatus(DiscountRightsMasterDto discountRightsMasterDto) throws ApplicationException;
	
	public Response getDiscountRightsMasterList(DiscountRightsMasterDto discountRightsMasterDto) throws ApplicationException;
	
	public Response getDiscountRightsMasterById(Integer discountRightId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveDiscountRightsMaster() throws ApplicationException;
	
	public Response getDiscountRightsMasterCount() throws ApplicationException;
	
	public Response checkDiscountRightsMasterAvailable(DiscountRightsMasterDto discountRightsMasterDto) throws ApplicationException;
	
	public Response getUsersList(Integer orgId, Integer unitId) throws ApplicationException;

	public Response getUsersDiscRights(Integer orgId, Integer unitId, Integer userId)throws ApplicationException;

	public Response getUsersListHavingDiscRights(Integer orgId, Integer unitId, BigDecimal amount, BigDecimal percentage)throws ApplicationException;
	
	public Response getUsersListHavingFinancialClearanceRights(Integer orgId, Integer unitId, BigDecimal amount)throws ApplicationException;
	
	public Response getUsersCreditNoteRights(Integer orgId, Integer unitId, Double amount)throws ApplicationException;

	public Response isUserHaveCreditNoteRight(Integer orgId, Integer unitId,Integer userId)throws ApplicationException;

	public Response getUsersListByOrgUnit(Integer orgId, Integer unitId, Integer roleId)throws ApplicationException;
	
	public Response getUsersHavingRefundRights(Integer orgId, Integer unitId)throws ApplicationException;

	public Response isUserHavingRefundRight(Integer orgId, Integer unitId,Integer userId)throws ApplicationException;

	public Response getUsersListHavingDiscRightsForBadDebit(Integer orgId, Integer unitId, BigDecimal amount)throws ApplicationException;

	public Response getUsersListHavingDiscRightsForCredit(Integer orgId, Integer unitId, BigDecimal amount)throws ApplicationException;

	public Response getUsersHavingExpenseRights(Integer orgId, Integer unitId, BigDecimal amount);


	public Response getUsersListHavingCreditRights(Integer orgId, Integer unitId, BigDecimal amount,BigDecimal percentage);

	public Response isUserHavingDepositRefundRight(Integer orgId, Integer unitId, Integer userId,Double amount) throws ApplicationException;

	public Response getUserHavingRefundRigtsOfGivenAmount(Integer orgId, Integer unitId,Double amount) throws ApplicationException;


}
