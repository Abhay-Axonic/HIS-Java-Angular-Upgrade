package com.param.billing.global.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.dao.IDiscountRightsMasterDao;
import com.param.billing.global.dto.DiscountRightsMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class DiscountRightsMasterServiceImpl implements IDiscountRightsMasterService, ICommonConstants{
 @Autowired
 IDiscountRightsMasterDao iDiscountRightsMasterDao;
	@Override
	public Response saveDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try{
			Response response=iDiscountRightsMasterDao.checkDiscountRightsMasterAvailable(discountRightsMasterDto);
		
		if(response.getListObject()!=null && response.getListObject().size()>0){
			
			return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
		}else{
			return iDiscountRightsMasterDao.saveDiscountRightsMaster(discountRightsMasterDto);
		}
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response updateDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try{
			
			return iDiscountRightsMasterDao.updateDiscountRightsMaster(discountRightsMasterDto);
						
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response updateDiscountRightsMasterStatus(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.updateDiscountRightsMasterStatus(discountRightsMasterDto);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}



	@Override
	public Response getDiscountRightsMasterList(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getDiscountRightsMasterList(discountRightsMasterDto);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getDiscountRightsMasterById(Integer discountRightId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getDiscountRightsMasterById(discountRightId, organisationId, unitId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getActiveDiscountRightsMaster() throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getActiveDiscountRightsMaster();

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getDiscountRightsMasterCount() throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getDiscountRightsMasterCount();

		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response checkDiscountRightsMasterAvailable(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.checkDiscountRightsMasterAvailable(discountRightsMasterDto);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersList(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersList(orgId,unitId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersDiscRights(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersDiscRights(orgId,unitId,userId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersListHavingDiscRights(Integer orgId, Integer unitId, BigDecimal amount,BigDecimal percentage)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersListHavingDiscRights(orgId,unitId,amount,percentage);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}
	
	@Override
	public Response getUsersListHavingFinancialClearanceRights(Integer orgId,Integer unitId, BigDecimal amount)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersListHavingFinancialClearanceRights(orgId,unitId,amount);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersCreditNoteRights(Integer orgId, Integer unitId,Double amount) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersCreditNoteRights(orgId,unitId,amount);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}




	@Override
	public Response isUserHaveCreditNoteRight(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.isUserHaveCreditNoteRight(orgId,unitId,userId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersListByOrgUnit(Integer orgId, Integer unitId, Integer roleId) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersListByOrgUnit(orgId,unitId,roleId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersHavingRefundRights(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersHavingRefundRights(orgId,unitId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response isUserHavingRefundRight(Integer orgId, Integer unitId, Integer userId) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.isUserHavingRefundRight(orgId,unitId,userId);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersListHavingDiscRightsForBadDebit(Integer orgId, Integer unitId, BigDecimal amount)
		throws ApplicationException {
			try {
				return iDiscountRightsMasterDao.getUsersListHavingDiscRightsForBadDebit(orgId,unitId,amount);

			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersListHavingDiscRightsForCredit(Integer orgId, Integer unitId, BigDecimal amount)
			throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUsersListHavingDiscRightsForCredit(orgId,unitId,amount);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getUsersHavingExpenseRights(Integer orgId, Integer unitId, BigDecimal amount) {
		try {
			return iDiscountRightsMasterDao.getUsersHavingExpenseRights(orgId,unitId,amount);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response isUserHavingDepositRefundRight(Integer orgId, Integer unitId, Integer userId,Double amount) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.isUserHavingDepositRefundRight(orgId,unitId,userId,amount);

		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUsersListHavingCreditRights(Integer orgId, Integer unitId, BigDecimal amount,
			BigDecimal percentage) {
		try {
			return iDiscountRightsMasterDao.getUsersListHavingCreditRights(orgId, unitId, amount, percentage);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUserHavingRefundRigtsOfGivenAmount(Integer orgId, Integer unitId, Double amount) throws ApplicationException {
		try {
			return iDiscountRightsMasterDao.getUserHavingRefundRigtsOfGivenAmount(orgId,unitId,amount);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}



}
