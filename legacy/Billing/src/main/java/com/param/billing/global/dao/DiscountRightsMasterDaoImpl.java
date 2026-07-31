package com.param.billing.global.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.dto.DiscountRightsMasterDto;
import com.param.billing.global.model.DiscountRightsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.VerifiedIdentityService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DiscountRightsMasterDaoImpl extends GenericDao<DiscountRightsMaster, Integer>implements IDiscountRightsMasterDao, ICommonConstants{

	public DiscountRightsMasterDaoImpl() {
		super(DiscountRightsMaster.class);

		
	}


	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	@Override
	public Response saveDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			DiscountRightsMaster discountRightsMaster=new DiscountRightsMaster();
				discountRightsMaster.setUserId(discountRightsMasterDto.getUserId());
				discountRightsMaster.setFromDiscountAmount(discountRightsMasterDto.getFromDiscountAmount()!=null ? discountRightsMasterDto.getFromDiscountAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToDiscountAmount(discountRightsMasterDto.getToDiscountAmount()!=null ? discountRightsMasterDto.getToDiscountAmount() : BigDecimal.ZERO);
				discountRightsMaster.setOrganisationId(discountRightsMasterDto.getOrganisationId());
				discountRightsMaster.setUnitId(discountRightsMasterDto.getUnitId());
				discountRightsMaster.setStatus(discountRightsMasterDto.getStatus());
				discountRightsMaster.setCreatedBy(discountRightsMasterDto.getCreatedBy());
				discountRightsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(discountRightsMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				discountRightsMaster.setUpdatedBy(discountRightsMasterDto.getUpdatedBy());
				discountRightsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(discountRightsMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				discountRightsMaster.setIsFinancialClearance(discountRightsMasterDto.getIsFinancialClearance());
				discountRightsMaster.setIsCreditNoteApproval(discountRightsMasterDto.getIsCreditNoteApproval());
				discountRightsMaster.setIsExpenseApproval(discountRightsMasterDto.getIsExpenseApproval());
				discountRightsMaster.setIsBadDebit(discountRightsMasterDto.getIsBadDebit());
				discountRightsMaster.setFromBadDebitAmount(discountRightsMasterDto.getFromBadDebitAmount()!=null ? discountRightsMasterDto.getFromBadDebitAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToBadDebitAmount(discountRightsMasterDto.getToBadDebitAmount()!=null ? discountRightsMasterDto.getToBadDebitAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromFinancialClearanceAmount(discountRightsMasterDto.getFromFinancialClearanceAmount()!=null ? discountRightsMasterDto.getFromFinancialClearanceAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToFinancialClearanceAmount(discountRightsMasterDto.getToFinancialClearanceAmount()!=null ? discountRightsMasterDto.getToFinancialClearanceAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromCreditNoteAmount(discountRightsMasterDto.getFromCreditNoteAmount()!=null ? discountRightsMasterDto.getFromCreditNoteAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToCreditNoteAmount(discountRightsMasterDto.getToCreditNoteAmount()!=null ? discountRightsMasterDto.getToCreditNoteAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromExpenseApprovalAmount(discountRightsMasterDto.getFromExpenseApprovalAmount()!=null ? discountRightsMasterDto.getFromExpenseApprovalAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToExpenseApprovalAmount(discountRightsMasterDto.getToExpenseApprovalAmount()!=null ? discountRightsMasterDto.getToExpenseApprovalAmount() : BigDecimal.ZERO);
				discountRightsMaster.setRoleId(discountRightsMasterDto.getRoleId());
				discountRightsMaster.setIsDiscountRights(discountRightsMasterDto.getIsDiscountRights());
				discountRightsMaster.setDiscountType(discountRightsMasterDto.getDiscountType());
				discountRightsMaster.setBadDebitType(discountRightsMasterDto.getBadDebitType());
				discountRightsMaster.setFinancialClearanceType(discountRightsMasterDto.getFinancialClearanceType());
				discountRightsMaster.setCreditNoteType(discountRightsMasterDto.getCreditNoteType());
				discountRightsMaster.setExpenseApprovalType(discountRightsMasterDto.getExpenseApprovalType());
			    discountRightsMaster.setIsRefundApproval(discountRightsMasterDto.getIsRefundApproval());
			    discountRightsMaster.setFromRefundAmount(discountRightsMasterDto.getFromRefundAmount());
			    discountRightsMaster.setToRefundAmount(discountRightsMasterDto.getToRefundAmount());
			    discountRightsMaster.setRefundType(discountRightsMasterDto.getRefundType());
				//discountRightsMaster.setIsUHIDMergeApproval(discountRightsMasterDto.getIsUHIDMergeApproval());
			save(discountRightsMaster);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDiscountRightsMaster(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			DiscountRightsMaster discountRightsMaster=findById(discountRightsMasterDto.getDiscountRightId());
			if (discountRightsMaster == null || !discountRightsMaster.getOrganisationId().equals(verifiedIdentityService.getVerifiedOrganizationId()) || !discountRightsMaster.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
			throw new ApplicationException("Discount rights do not belong to the current organization");
			}
				discountRightsMaster.setUserId(discountRightsMasterDto.getUserId());
				discountRightsMaster.setFromDiscountAmount(discountRightsMasterDto.getFromDiscountAmount()!=null ? discountRightsMasterDto.getFromDiscountAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToDiscountAmount(discountRightsMasterDto.getToDiscountAmount()!=null ? discountRightsMasterDto.getToDiscountAmount() : BigDecimal.ZERO);
				discountRightsMaster.setOrganisationId(discountRightsMasterDto.getOrganisationId());
				discountRightsMaster.setUnitId(discountRightsMasterDto.getUnitId());
				discountRightsMaster.setUpdatedBy(discountRightsMasterDto.getUpdatedBy());
				discountRightsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(discountRightsMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				discountRightsMaster.setIsFinancialClearance(discountRightsMasterDto.getIsFinancialClearance());
				discountRightsMaster.setIsCreditNoteApproval(discountRightsMasterDto.getIsCreditNoteApproval());
				discountRightsMaster.setIsExpenseApproval(discountRightsMasterDto.getIsExpenseApproval());
				discountRightsMaster.setIsBadDebit(discountRightsMasterDto.getIsBadDebit());
				discountRightsMaster.setFromBadDebitAmount(discountRightsMasterDto.getFromBadDebitAmount()!=null ? discountRightsMasterDto.getFromBadDebitAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToBadDebitAmount(discountRightsMasterDto.getToBadDebitAmount()!=null ? discountRightsMasterDto.getToBadDebitAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromFinancialClearanceAmount(discountRightsMasterDto.getFromFinancialClearanceAmount()!=null ? discountRightsMasterDto.getFromFinancialClearanceAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToFinancialClearanceAmount(discountRightsMasterDto.getToFinancialClearanceAmount()!=null ? discountRightsMasterDto.getToFinancialClearanceAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromCreditNoteAmount(discountRightsMasterDto.getFromCreditNoteAmount()!=null ? discountRightsMasterDto.getFromCreditNoteAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToCreditNoteAmount(discountRightsMasterDto.getToCreditNoteAmount()!=null ? discountRightsMasterDto.getToCreditNoteAmount() : BigDecimal.ZERO);
				discountRightsMaster.setFromExpenseApprovalAmount(discountRightsMasterDto.getFromExpenseApprovalAmount()!=null ? discountRightsMasterDto.getFromExpenseApprovalAmount() : BigDecimal.ZERO);
				discountRightsMaster.setToExpenseApprovalAmount(discountRightsMasterDto.getToExpenseApprovalAmount()!=null ? discountRightsMasterDto.getToExpenseApprovalAmount() : BigDecimal.ZERO);
				discountRightsMaster.setRoleId(discountRightsMasterDto.getRoleId());
				discountRightsMaster.setIsDiscountRights(discountRightsMasterDto.getIsDiscountRights());
				discountRightsMaster.setDiscountType(discountRightsMasterDto.getDiscountType());
				discountRightsMaster.setBadDebitType(discountRightsMasterDto.getBadDebitType());
				discountRightsMaster.setFinancialClearanceType(discountRightsMasterDto.getFinancialClearanceType());
				discountRightsMaster.setCreditNoteType(discountRightsMasterDto.getCreditNoteType());
				discountRightsMaster.setExpenseApprovalType(discountRightsMasterDto.getExpenseApprovalType());
				discountRightsMaster.setIsUHIDMergeApproval(discountRightsMasterDto.getIsUHIDMergeApproval());
				discountRightsMaster.setIsRefundApproval(discountRightsMasterDto.getIsRefundApproval());
				discountRightsMaster.setFromRefundAmount(discountRightsMasterDto.getFromRefundAmount());
				discountRightsMaster.setToRefundAmount(discountRightsMasterDto.getToRefundAmount());
			    discountRightsMaster.setRefundType(discountRightsMasterDto.getRefundType());
			update(discountRightsMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateDiscountRightsMasterStatus(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try {
			DiscountRightsMaster discountRightsMaster=findById(discountRightsMasterDto.getDiscountRightId());
			if (discountRightsMaster == null || !discountRightsMaster.getOrganisationId().equals(verifiedIdentityService.getVerifiedOrganizationId()) || !discountRightsMaster.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
			throw new ApplicationException("Discount rights do not belong to the current organization");
			}
			
			discountRightsMaster.setStatus(discountRightsMasterDto.getStatus());
			discountRightsMaster.setUpdatedBy(discountRightsMasterDto.getUpdatedBy());
			discountRightsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(discountRightsMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(discountRightsMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getDiscountRightsMasterList(DiscountRightsMasterDto discountRightsMasterDto)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DISCOUNT_RIGHTS_MASTER_LIST")
				.setParameter("organisationId", discountRightsMasterDto.getOrganisationId())
				.setParameter("unitId", discountRightsMasterDto.getUnitId())
					/*
					 * .setFirstResult(discountRightsMasterDto.getOffset() != null ?
					 * discountRightsMasterDto.getOffset() : 0)
					 * .setMaxResults(discountRightsMasterDto.getNoOfRecordsPerPage() != null ?
					 * discountRightsMasterDto.getNoOfRecordsPerPage() : 10)
					 */
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
//	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getDiscountRightsMasterById(Integer discountRightId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DISCOUNT_RIGHTS_MASTER_BY_ID") 
				.setParameter("discountRightId", discountRightId)
				.setParameter("organisationId", organisationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveDiscountRightsMaster() throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_DISCOUNT_RIGHTS_MASTER")
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getDiscountRightsMasterCount() throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(DiscountRightsMaster.class);
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkDiscountRightsMasterAvailable(DiscountRightsMasterDto discountRightsMasterDto) 
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("CHECK_DISCOUNT_RIGHTS_MASTER_AVAILABLE")
				.setParameter("userId", discountRightsMasterDto.getUserId())
				.setParameter("orgId", discountRightsMasterDto.getOrganisationId())
				.setParameter("unitId", discountRightsMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getUsersList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				//.setInteger("roleId", roleId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getUsersDiscRights(Integer orgId, Integer unitId, Integer userId)	throws ApplicationException {
		try{
			DiscountRightsMasterDto discountRightsMasterDto=(DiscountRightsMasterDto)sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_DISC_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, null, null, null, discountRightsMasterDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersListHavingDiscRights(Integer orgId, Integer unitId, BigDecimal amount,BigDecimal percentage)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_HAVING_DISC_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", amount)
				.setParameter("percentage", percentage)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getUsersListHavingFinancialClearanceRights(Integer orgId, Integer unitId, BigDecimal amount)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_HAVING_FINANCIAL_CLEARANCE_FLAG")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", amount)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersCreditNoteRights(Integer orgId, Integer unitId, Double amount) throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_CREDIT_NOTE_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", BigDecimal.valueOf(amount))
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response isUserHaveCreditNoteRight(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try{
			DiscountRightsMasterDto dto=(DiscountRightsMasterDto) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_CREDIT_NOTE_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class))
				.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, dto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersListByOrgUnit(Integer orgId, Integer unitId, Integer roleId) throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_BY_ROLE_ID")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("roleId", roleId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
	
	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
	//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersHavingRefundRights(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_REFUND_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response isUserHavingRefundRight(Integer orgId, Integer unitId, Integer userId) throws ApplicationException {
		try{
			DiscountRightsMasterDto dto=(DiscountRightsMasterDto) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_REFUND_RIGHTS")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class))
				.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, dto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersListHavingDiscRightsForBadDebit(Integer orgId, Integer unitId, BigDecimal amount)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_HAVING_DISC_RIGHTS_FOR_BAD_DEBIT")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", amount)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersListHavingDiscRightsForCredit(Integer orgId, Integer unitId, BigDecimal amount)
			throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_HAVING_DISC_RIGHTS_FOR_CREDIT_NOTE")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", amount)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersHavingExpenseRights(Integer orgId, Integer unitId, BigDecimal amount) {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_USERS_LIST_HAVING_RIGHTS_FOR_EXPENSE")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setParameter("amount", amount)
				.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUsersListHavingCreditRights(Integer orgId, Integer unitId, BigDecimal amount,BigDecimal percentage)
			throws ApplicationException {
		try {
			List<DiscountRightsMasterDto> discountRightsMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USERS_LIST_HAVING_CREDIT_RIGHTS")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("amount", amount)
					.setParameter("percentage", percentage)
					.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, discountRightsMasterDtoList, null);
		} catch (HibernateException e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response isUserHavingDepositRefundRight(Integer orgId, Integer unitId, Integer userId,Double amount) throws ApplicationException {
		try{
			DiscountRightsMasterDto  dto = new DiscountRightsMasterDto();
			Long count =(Long) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DEPOSIT_REFUND_RIGHTS")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("userId", userId)
					.setParameter("amount",BigDecimal.valueOf(amount))
					.uniqueResult();
			dto.setIsRefundApproval(count!=null && count > 0 ?  'Y' :'N');
			return new Response<>(SUCCESS, null, null, null, dto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserHavingRefundRigtsOfGivenAmount(Integer orgId, Integer unitId, Double amount) throws ApplicationException {
		try{
			List<DiscountRightsMasterDto> discountRightsMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USERS_LIST_DEPOSIT_REFUND_RIGHTS_WITH_AMOUNT")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("amount", BigDecimal.valueOf(amount))
					.setResultTransformer(Transformers.aliasToBean(DiscountRightsMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, discountRightsMasterDtoList, null);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
